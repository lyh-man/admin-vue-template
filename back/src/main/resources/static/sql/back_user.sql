-- DROP DATABASE IF EXISTS admin_template;
--
-- CREATE DATABASE admin_template;

USE admin_template;
-- 用户表
CREATE TABLE back_user (
    id bigint NOT NULL COMMENT '用户 ID',
    name varchar(50) NOT NULL COMMENT '用户名',
    mobile varchar(20) NOT NULL COMMENT '用户手机号',
    password varchar(64) NOT NULL COMMENT '用户密码',
    create_time datetime COMMENT '创建时间',
    update_time datetime COMMENT '修改时间',
    delete_flag tinyint COMMENT '逻辑删除标志，0 表示未删除， 1 表示删除',
    version tinyint COMMENT '版本号',
    PRIMARY KEY(id),
    UNIQUE INDEX(name)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='用户表';
