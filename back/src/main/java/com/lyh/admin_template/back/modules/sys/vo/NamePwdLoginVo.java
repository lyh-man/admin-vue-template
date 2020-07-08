package com.lyh.admin_template.back.modules.sys.vo;

import com.lyh.admin_template.back.common.validator.group.sys.LoginGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 登录时的视图数据类（view object），
 * 用于接收使用 用户名 + 密码 登陆的数据与操作。
 */
@Data
public class NamePwdLoginVo {
    @NotEmpty(message = "{sys.user.name.notEmpty}", groups = {LoginGroup.class})
    private String userName;
    @NotEmpty(message = "{sys.user.password.notEmpty}", groups = {LoginGroup.class})
    private String password;
}
