package com.lyh.admin_template.back.modules.sys.vo;

import com.lyh.admin_template.back.common.validator.group.sys.RegisterGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 注册时对应的视图数据类（view object），
 * 用于接收并处理 注册时的数据。
 */
@Data
public class RegisterVo {
    @NotEmpty(message = "{sys.user.name.notEmpty}", groups = {RegisterGroup.class})
    @Pattern(message = "{sys.user.name.format.error}", regexp = "^.*[^\\d].*$", groups = {RegisterGroup.class})
    private String userName;
    @NotEmpty(message = "{sys.user.password.notEmpty}", groups = {RegisterGroup.class})
    private String password;
    @NotEmpty(message = "{sys.user.phone.notEmpty}", groups = {RegisterGroup.class})
    @Pattern(message = "{sys.user.phone.format.error}", regexp = "0?(13|14|15|18|17)[0-9]{9}", groups = {RegisterGroup.class})
    private String phone;
    @NotEmpty(message = "{sys.user.code.notEmpty}", groups = {RegisterGroup.class})
    private String code;
}
