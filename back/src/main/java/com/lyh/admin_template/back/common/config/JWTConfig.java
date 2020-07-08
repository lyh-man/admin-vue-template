package com.lyh.admin_template.back.common.config;

import com.lyh.admin_template.back.common.utils.GsonUtil;
import com.lyh.admin_template.back.common.utils.JwtUtil;
import com.lyh.admin_template.back.common.utils.RedisUtil;
import com.lyh.admin_template.back.common.utils.Result;
import com.lyh.admin_template.back.modules.sys.vo.JwtVo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Slf4j
@Configuration
public class JWTConfig {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 定义拦截器，拦截请求。
     * 其中：
     *      addPathPatterns 用于添加需要拦截的请求。
     *      excludePathPatterns 用于添加不需要拦截的请求。
     * 此处：
     *      拦截所有请求，但是排除 登录、注册 请求 以及 swagger 请求。
     */
    @Bean(name = "JWTInterceptor")
    public WebMvcConfigurer JWTInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new JWTInterceptor())
                    // 拦截所有请求
                    .addPathPatterns("/**")
                    // 不拦截 登录、注册、忘记密码请求
                    .excludePathPatterns("/sys/sys-user/login/*", "/sys/sys-user/register")
                    // 不拦截 swagger 请求
                    .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
            }
        };
    }

    /**
     * 定义一个拦截器，用于拦截请求，并对 JWT 进行验证
     */
    class JWTInterceptor extends HandlerInterceptorAdapter {

        /**
         * 访问 controller 前被调用
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // 获取 token（从 header 或者 参数中获取）
            String token = request.getHeader("token");
            if (StringUtils.isBlank(token)) {
                token = request.getParameter("token");
            }
            // 验证 token 是否过期（根据时间戳比较）
            if (JwtUtil.checkToken(token)) {
                // 刷新 token(过期时间小于 5 分钟，会重新创建 token，否则 使用原 token)
                token = refreshToken(token, response);
                // 获取 token 中的数据
                Claims claims = JwtUtil.getTokenBody(token);
                JwtVo jwt = GsonUtil.fromJson(String.valueOf(claims.get("data")), JwtVo.class);
                // 获取 redis 中存储的 token
                String redisToken = redisUtil.get(String.valueOf(jwt.getId()));
                // 当前 token 与 redis 中存储的 token 进行比较
                if (StringUtils.isNotEmpty(redisToken)) {
                    // 获取 redis 中 token 的数据
                    JwtVo redisJwt = GsonUtil.fromJson(String.valueOf(JwtUtil.getTokenBody(redisToken).get("data")), JwtVo.class);
                    // 若两者 token 相同，则为同一用户再次访问系统，放行
                    if (redisToken.equals(token)) {
                        return true;
                    } else if (redisJwt.getTime() <= jwt.getTime()){
                        // redis 中 token 生成时间戳 小于等于 当前 token 生成时间戳，即当前用户为最新登录者
                        // redis 保存当前最新的 token，并放行
                        redisUtil.set(String.valueOf(redisJwt.getId()), token, 60 * 30);
                        return true;
                    }
                }
            }
            // 认证失败，返回数据，并返回 401 状态码
            returnJsonData(response);
            return false;
        }
    }

    /**
     * 返回 json 格式的数据
     */
    public void returnJsonData(HttpServletResponse response) {
        PrintWriter pw = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            pw = response.getWriter();
            // 返回 code 为 401，表示 token 失效。
            pw.print(GsonUtil.toJson(Result.error().message("token 失效或过期").code(HttpStatus.SC_UNAUTHORIZED)));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 刷新 token
     * @param token 旧 token 值
     * @return 新 token
     */
    public String refreshToken(String token, HttpServletResponse response) {
        // 获取 token 中的数据
        Claims claims = JwtUtil.getTokenBody(token);
        // 获取 token 过期时间戳
        Long expire = claims.getExpiration().getTime();
        // 获取当前时间戳
        Long currentTime = new Date().getTime();
        // 若当前 token 过期时间 小于 5 分钟，则 更新 token
        if ((expire - currentTime) / 1000 < 300) {
            // 获取 token 数据
            JwtVo jwt = GsonUtil.fromJson(String.valueOf(claims.get("data")), JwtVo.class);
            // 重新生成 token
            token = JwtUtil.getJwtToken(jwt, 1000L * 60 * 30);
            // 把新 token 保存在 redis 中
            redisUtil.set(String.valueOf(jwt.getId()), token, 60 * 30);
            response.setHeader("token", token);
        }
        return token;
    }
}
