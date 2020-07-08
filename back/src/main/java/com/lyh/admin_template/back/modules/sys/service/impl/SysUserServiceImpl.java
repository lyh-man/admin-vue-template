package com.lyh.admin_template.back.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyh.admin_template.back.modules.sys.entity.SysRole;
import com.lyh.admin_template.back.modules.sys.entity.SysUser;
import com.lyh.admin_template.back.modules.sys.entity.SysUserRole;
import com.lyh.admin_template.back.modules.sys.mapper.SysUserMapper;
import com.lyh.admin_template.back.modules.sys.service.SysRoleService;
import com.lyh.admin_template.back.modules.sys.service.SysUserRoleService;
import com.lyh.admin_template.back.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2020-07-02
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 先插入数据到 用户表 sys_user 中。
     * 再获取数据 ID 与 角色 ID 并插入到 用户角色表 sys_user_role 中。
     * @param sysUser 用户数据
     * @return true 表示插入成功， false 表示失败
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public boolean saveUser(SysUser sysUser) {
        // 向 sys_user 表中插入数据
        if (this.save(sysUser)) {
            // 获取当前用户的 ID
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("name", sysUser.getName());
            SysUser sysUser2 = this.getOne(queryWrapper);

            // 获取普通用户角色 ID
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("role_name", "user");
            SysRole sysRole = sysRoleService.getOne(queryWrapper2);

            // 插入到 用户-角色 表中（sys_user_role）
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser2.getId()).setRoleId(sysRole.getId());
            return sysUserRoleService.save(sysUserRole);
        }
        return false;
    }
}
