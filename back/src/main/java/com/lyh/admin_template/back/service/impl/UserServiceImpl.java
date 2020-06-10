package com.lyh.admin_template.back.service.impl;

import com.lyh.admin_template.back.entity.User;
import com.lyh.admin_template.back.mapper.UserMapper;
import com.lyh.admin_template.back.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2020-06-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
