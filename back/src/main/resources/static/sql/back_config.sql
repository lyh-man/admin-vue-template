USE admin_template;

-- 系统配置信息
CREATE TABLE back_config (
    id bigint NOT NULL COMMENT '配置信息 ID',
    param_key varchar(50) COMMENT 'key',
    param_value varchar(2000) COMMENT 'value',
    status tinyint DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
    remark varchar(500) COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE INDEX (param_key)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='系统配置信息表';
