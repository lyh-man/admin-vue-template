-- DROP DATABASE IF EXISTS admin_template;
--
-- CREATE DATABASE admin_template;

-- --------------------------sys_user_role 用户角色表---------------------------------------
USE admin_template;
DROP TABLE IF EXISTS sys_user_role;
-- 系统用户角色表
CREATE TABLE sys_user_role (
    id bigint NOT NULL COMMENT '用户角色表 ID',
    role_id bigint NOT NULL COMMENT '角色 ID',
	user_id bigint NOT NULL COMMENT '用户 ID',
    create_time datetime DEFAULT NULL COMMENT '创建时间',
    update_time datetime DEFAULT NULL COMMENT '修改时间',
    delete_flag tinyint DEFAULT NULL COMMENT '逻辑删除标志，0 表示未删除， 1 表示删除',
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='系统用户角色表';


-- 插入数据
INSERT INTO `sys_user_role`(`id`, `role_id`, `user_id`, `create_time`, `update_time`, `delete_flag`)
VALUES (1278601251755452234, '1278601251755451245', '1278601251755454466', '2020-07-02 16:07:48', '2020-07-02 16:07:48', 0),
    (1278601251755453544, '1278601251755452551', '1278601251755451232', '2020-07-02 16:07:48', '2020-07-02 16:07:48', 0),
    (1278601251755454664, '1278601251755458779', '1278601251755456778', '2020-07-02 16:07:48', '2020-07-02 16:07:48', 0);

-- --------------------------sys_user_role 用户角色表---------------------------------------
