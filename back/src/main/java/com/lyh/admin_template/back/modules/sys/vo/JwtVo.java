package com.lyh.admin_template.back.modules.sys.vo;

import lombok.Data;

/**
 * 保存 JWT 对应存储的数据
 */
@Data
public class JwtVo {
    // 保存用户 ID
    private Long id;
    // 保存用户名
    private String name;
    // 保存用户手机号
    private String phone;
    // 保存 JWT 创建时间戳
    private Long time;
}
