-- DROP DATABASE IF EXISTS admin_template;
--
-- CREATE DATABASE admin_template;

-- --------------------------sys_role 角色表---------------------------------------
USE admin_template;
DROP TABLE IF EXISTS sys_role;
-- 系统用户角色表
CREATE TABLE sys_role (
    id bigint NOT NULL COMMENT '角色 ID',
    role_name varchar(20) NOT NULL COMMENT '角色名称',
	role_code varchar(20) DEFAULT NULL COMMENT '角色码',
	remark varchar(255) DEFAULT NULL COMMENT '角色备注',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    delete_flag tinyint DEFAULT NULL COMMENT '逻辑删除标志，0 表示未删除， 1 表示删除',
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='系统用户角色表';


-- 插入数据
INSERT INTO `sys_role`(`id`, `role_name`, `role_code`, `remark`, `create_time`, `update_time`, `delete_flag`)
VALUES (1278601251755451245, 'superAdmin', '1001', '超级管理员','2020-07-02 16:07:48', '2020-07-02 16:07:48', 0),
    (1278601251755452551, 'admin', '2001', '普通管理员','2020-07-02 16:07:48', '2020-07-02 16:07:48', 0),
    (1278601251755458779, 'user', '3001', '普通用户','2020-07-02 16:07:48', '2020-07-02 16:07:48', 0);

-- --------------------------sys_role 角色表---------------------------------------
