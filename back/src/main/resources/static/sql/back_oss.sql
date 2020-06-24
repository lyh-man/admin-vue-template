USE admin_template;

-- 文件上传
CREATE TABLE back_oss (
  id bigint NOT NULL COMMENT '文件 ID',
  file_url varchar(500) COMMENT 'URL 地址',
  oss_name varchar(200) COMMENT '存储在 OSS 中的文件名',
  file_name varchar(100) COMMENT '文件名',
  create_time datetime COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE INDEX (oss_name)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='文件上传';
