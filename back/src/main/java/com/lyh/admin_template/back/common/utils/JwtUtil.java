package com.lyh.admin_template.back.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JWT 操作工具类
 */
public class JwtUtil {

    // 设置默认过期时间（15 分钟）
    private static final long DEFAULT_EXPIRE = 1000L * 60 * 15;
    // 设置 jwt 生成 secret（随意指定）
    private static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    /**
     * 生成 jwt token，并指定默认过期时间 15 分钟
     */
    public static String getJwtToken(Object data) {
        return getJwtToken(data, DEFAULT_EXPIRE);
    }

    /**
     * 生成 jwt token，根据指定的 过期时间
     */
    public static String getJwtToken(Object data, Long expire) {
        String JwtToken = Jwts.builder()
                // 设置 jwt 类型
                .setHeaderParam("typ", "JWT")
                // 设置 jwt 加密方法
                .setHeaderParam("alg", "HS256")
                // 设置 jwt 主题
                .setSubject("admin-user")
                // 设置 jwt 发布时间
                .setIssuedAt(new Date())
                // 设置 jwt 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                // 设置自定义数据
                .claim("data", data)
                // 设置密钥与算法
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                // 生成 token
                .compact();
        return JwtToken;
    }

    /**
     * 判断token是否存在与有效，true 表示未过期，false 表示过期或不存在
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            // 获取 token 数据
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
            // 判断是否过期
            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断token是否存在与有效
     */
    public static boolean checkToken(HttpServletRequest request) {
        return checkToken(request.getHeader("token"));
    }

    /**
     * 根据 token 获取数据
     */
    public static Claims getTokenBody(HttpServletRequest request) {
        return getTokenBody(request.getHeader("token"));
    }

    /**
     * 根据 token 获取数据
     */
    public static Claims getTokenBody(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        return claimsJws.getBody();
    }
}
