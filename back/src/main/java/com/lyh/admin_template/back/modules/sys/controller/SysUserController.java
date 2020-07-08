package com.lyh.admin_template.back.modules.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.admin_template.back.common.utils.*;
import com.lyh.admin_template.back.common.validator.group.sys.LoginGroup;
import com.lyh.admin_template.back.common.validator.group.sys.RegisterGroup;
import com.lyh.admin_template.back.modules.sys.entity.SysUser;
import com.lyh.admin_template.back.modules.sys.service.SysUserService;
import com.lyh.admin_template.back.modules.sys.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author lyh
 * @since 2020-07-02
 */
@RestController
@RequestMapping("/sys/sys-user")
@Api(tags = "用户登录、注册操作")
public class SysUserController {

    /**
     * 用于操作 sys_user 表
     */
    @Autowired
    private SysUserService sysUserService;
    /**
     * 用于操作 redis
     */
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 用于操作 短信验证码发送
     */
    @Autowired
    private SmsUtil smsUtil;
    /**
     * 常量，表示用户密码登录操作
     */
    private static final String USER_NAME_STATUS = "0";
    /**
     * 常量，表示手机号密码登录操作
     */
    private static final String PHONE_STATUS = "1";

    /**
     * 获取 jwt
     * @return jwt
     */
    private String getJwt(SysUser sysUser) {
        // 获取需要保存在 jwt 中的数据
        JwtVo jwtVo = new JwtVo();
        jwtVo.setId(sysUser.getId());
        jwtVo.setName(sysUser.getName());
        jwtVo.setPhone(sysUser.getMobile());
        jwtVo.setTime(new Date().getTime());
        // 获取 jwt 数据，设置过期时间为 30 分钟
        String jwt = JwtUtil.getJwtToken(jwtVo, 1000L * 60 * 30);
        // 判断用户是否重复登录（code 有值则重复登录，需要保留最新的登录者，剔除前一个登录者）
        String code = redisUtil.get(String.valueOf(sysUser.getId()));
        // 获取当前时间戳
        Long currentTime = new Date().getTime();
        // 如果 redis 中存在 jwt 数据，则根据时间戳比较谁为最新的登陆者
        if (StringUtils.isNotEmpty(code)) {
            // 获取 redis 中存储的 jwt 数据
            JwtVo redisJwt = GsonUtil.fromJson(String.valueOf(JwtUtil.getTokenBody(code).get("data")), JwtVo.class);
            // redis jwt 大于 当前时间戳，则 redis 中 jwt 为最新登录者，当前登录失败
            if (redisJwt.getTime() > currentTime) {
                return null;
            }
        }
        // 把数据存放在 redis 中，设置过期时间为 30 分钟
        redisUtil.set(String.valueOf(sysUser.getId()), jwt, 60 * 30);
        return jwt;
    }

    /**
     * 使用密码进行真实登录操作
     * @param account 账号（用户名或手机号）
     * @param pwd 密码
     * @param status 是否使用用户名登录（0 表示用户名登录，1 表示手机号登录）
     * @return jwt
     */
    private String pwdLogin(String account, String pwd, String status) {
        // 新增查询条件
        QueryWrapper queryWrapper = new QueryWrapper();
        // 如果是用户名 + 密码登录，则根据 姓名 + 密码 查找数据
        if (USER_NAME_STATUS.equals(status)) {
            queryWrapper.eq("name", account);
        }
        // 如果是手机号 + 密码登录，则根据 手机号 + 密码 查找数据
        if (PHONE_STATUS.equals(status)) {
            queryWrapper.eq("mobile", account);
        }
        // 添加密码条件，密码进行 MD5 加密后再与数据库数据比较
        queryWrapper.eq("password", MD5Util.encrypt(pwd));
        // 获取用户数据
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        // 如果存在用户数据
        if (sysUser != null) {
            return getJwt(sysUser);
        }
        return null;
    }

    /**
     * 使用 验证码进行真实登录操作
     * @param phone 手机号
     * @param code 验证码
     * @return jwt
     */
    private String codeLogin(String phone, String code) {
        // 获取 redis 中存放的验证码
        String redisCode = redisUtil.get(phone);
        // 存在验证码，且输入的验证码与 redis 存放的验证码相同,则根据手机号去数据库查询数据
        if (StringUtils.isNotEmpty(redisCode) && code.equals(redisCode)) {
            // 新增查询条件
            QueryWrapper queryWrapper = new QueryWrapper();
            // 根据手机号去查询数据
            queryWrapper.eq("mobile", phone);
            SysUser sysUser = sysUserService.getOne(queryWrapper);
            // 如果存在用户数据
            if (sysUser != null) {
                return getJwt(sysUser);
            }
        }
        return null;
    }

    @ApiOperation(value = "使用用户名、密码登录")
    @PostMapping("/login/namePwdLogin")
    public Result namePwdLogin(@Validated({LoginGroup.class}) @RequestBody NamePwdLoginVo namePwdLoginVo) {
        String jwt = pwdLogin(namePwdLoginVo.getUserName(), namePwdLoginVo.getPassword(), USER_NAME_STATUS);
        if (StringUtils.isNotEmpty(jwt)) {
            return Result.ok().message("登录成功").data("token", jwt);
        }
        return Result.error().message("登录失败").code(HttpStatus.SC_UNAUTHORIZED);
    }

    @ApiOperation(value = "使用手机号、密码登录")
    @PostMapping("/login/phonePwdLogin")
    public Result phonePwdLogin(@Validated({LoginGroup.class}) @RequestBody PhonePwdLoginVo phonePwdLoginVo) {
        String jwt = pwdLogin(phonePwdLoginVo.getPhone(), phonePwdLoginVo.getPassword(), PHONE_STATUS);
        if (StringUtils.isNotEmpty(jwt)) {
            return Result.ok().message("登录成功").data("token", jwt);
        }
        return Result.error().message("登录失败").code(HttpStatus.SC_UNAUTHORIZED);
    }

    @ApiOperation(value = "使用手机号、验证码登录")
    @PostMapping("/login/phoneCodeLogin")
    public Result phoneCodeLogin(@Validated({LoginGroup.class}) @RequestBody PhoneCodeLoginVo phoneCodeLoginVo) {
        String jwt = codeLogin(phoneCodeLoginVo.getPhone(), phoneCodeLoginVo.getCode());
        if (StringUtils.isNotEmpty(jwt)) {
            return Result.ok().message("登录成功").data("token", jwt);
        }
        return Result.error().message("登录失败").code(HttpStatus.SC_UNAUTHORIZED);
    }

    @ApiOperation(value = "获取短信验证码")
    @GetMapping("/login/getCode")
    public Result getCode(String phone) {
        // 设置默认过期时间
        Long defaultTime = 60L * 5;
        // 先判断 redis 中是否存储过验证码(设置期限为 1 分钟)，防止重复获取验证码
        Long expire = redisUtil.getExpire(phone);
        if (expire != null && (defaultTime - expire < 60)) {
            return Result.error().message("验证码已发送，1 分钟后可再次获取验证码");
        } else {
            // 获取 短信验证码
            String code = smsUtil.sendSms(phone);
            if (StringUtils.isNotEmpty(code)) {
                // 把验证码存放在 redis 中，并设置 过期时间 为 5 分钟
                redisUtil.set(phone, code, defaultTime);
                return Result.ok().message("验证码获取成功").data("code", code);
            }
        }
        return Result.error().message("验证码获取失败");
    }

    @ApiOperation(value = "用户登出")
    @GetMapping("/logout")
    public Result logout(@RequestParam String userName) {
        // 先获取用户数据
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", userName);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        // 用户存在时
        if (sysUser != null) {
            // 生成并返回一个无效的 token
            String jwt = JwtUtil.getJwtToken(null, 1000L);
            // 删除 redis 中的 token
            redisUtil.del(String.valueOf(sysUser.getId()));
            return Result.ok().message("登出成功").data("token", jwt);
        }
        return Result.error().message("登出失败");
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public Result register(@Validated({RegisterGroup.class}) @RequestBody RegisterVo registerVo) {
        if (save(registerVo)) {
            return Result.ok().message("用户注册成功");
        }
        return Result.error().message("用户注册失败").code(HttpStatus.SC_UNAUTHORIZED);
    }

    /**
     * 真实注册操作
     * @param registerVo 注册数据
     * @return true 为插入成功， false 为失败
     */
    public boolean save(RegisterVo registerVo) {
        // 判断 redis 中是否存在 验证码
        String code = redisUtil.get(registerVo.getPhone());
        // redis 中存在验证码且与当前验证码相同
        if (StringUtils.isNotEmpty(code) && code.equals(registerVo.getCode())) {
            SysUser sysUser = new SysUser();
            sysUser.setName(registerVo.getUserName()).setPassword(MD5Util.encrypt(registerVo.getPassword()));
            sysUser.setMobile(registerVo.getPhone());
            return sysUserService.saveUser(sysUser);
        }
        return false;
    }

    @ApiOperation(value = "测试刷新 token")
    @GetMapping("/test")
    public Result testRefreshToken() {
        return Result.ok();
    }
}

