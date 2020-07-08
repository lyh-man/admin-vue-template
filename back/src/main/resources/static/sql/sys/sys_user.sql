-- DROP DATABASE IF EXISTS admin_template;
--
-- CREATE DATABASE admin_template;

-- --------------------------sys_user 用户表---------------------------------------
USE admin_template;
DROP TABLE IF EXISTS sys_user;
-- 用户表
CREATE TABLE sys_user (
    id bigint NOT NULL COMMENT '用户 ID',
    name varchar(20) NOT NULL COMMENT '用户名',
    mobile varchar(20) NOT NULL COMMENT '用户手机号',
    password varchar(64) NOT NULL COMMENT '用户密码',
   sex tinyint DEFAULT NULL COMMENT '性别， 0 表示女， 1 表示男',
   age tinyint DEFAULT NULL COMMENT '年龄',
   avatar varchar(255) DEFAULT NULL COMMENT '头像',
   email varchar(100) DEFAULT NULL COMMENT '邮箱',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    delete_flag tinyint DEFAULT NULL COMMENT '逻辑删除标志，0 表示未删除， 1 表示删除',
   disabled_flag tinyint DEFAULT NULL COMMENT '禁用标志， 0 表示未禁用， 1 表示禁用',
   wx_id varchar(128) DEFAULT NULL COMMENT '微信 openid（拓展字段、用于第三方微信登录）',
   qq_id varchar(128) DEFAULT NULL COMMENT 'QQ openid（拓展字段、用于第三方 QQ 登录）',
    PRIMARY KEY(id),
    UNIQUE INDEX(name, mobile)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='系统用户表';


-- 插入数据
INSERT INTO `sys_user`(`id`, `name`, `mobile`, `password`, `sex`, `age`, `avatar`, `email`, `create_time`, `update_time`, `delete_flag`, `disabled_flag`, `wx_id`, `qq_id`)
VALUES (1278601251755454466, 'superAdmin', '17730125031', 'e10adc3949ba59abbe56e057f20f883e', 1, 23, NULL, "m_17730125031@163.com", '2020-07-02 16:07:48', '2020-07-02 16:07:48', 0, 0, NULL, NULL),
    (1278601251755451232, 'admin', '17730125032', 'e10adc3949ba59abbe56e057f20f883e', 1, 23, NULL, "m_17730125031@163.com", '2020-07-02 16:07:48', '2020-07-02 16:07:48', 0, 0, NULL, NULL),
    (1278601251755456778, 'jack', '17730125033', 'e10adc3949ba59abbe56e057f20f883e', 1, 23, NULL, "m_17730125031@163.com", '2020-07-02 16:07:48', '2020-07-02 16:07:48', 0, 0, NULL, NULL);

-- --------------------------sys_user 用户表---------------------------------------
