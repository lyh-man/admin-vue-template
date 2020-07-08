package com.lyh.admin_template.back.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyh.admin_template.back.modules.sys.entity.SysUser;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author lyh
 * @since 2020-07-02
 */
public interface SysUserService extends IService<SysUser> {
    public boolean saveUser(SysUser sysUser);
}
