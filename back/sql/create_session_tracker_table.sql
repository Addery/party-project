-- 创建会话跟踪表
CREATE TABLE IF NOT EXISTS `session_tracker` (
  `session_id` VARCHAR(36) NOT NULL PRIMARY KEY COMMENT '会话ID',
  `user_id` VARCHAR(36) NULL COMMENT '用户ID',
  `ip_address` VARCHAR(45) NOT NULL COMMENT 'IP地址',
  `user_agent` TEXT COMMENT '用户代理',
  `start_time` DATETIME NOT NULL COMMENT '会话开始时间',
  `last_access_time` DATETIME NOT NULL COMMENT '最后访问时间',
  `request_count` INT NOT NULL DEFAULT 1 COMMENT '请求次数',
  `client_type` VARCHAR(20) NOT NULL DEFAULT 'Unknown' COMMENT '客户端类型(Desktop/Mobile/Unknown)',
  `is_expired` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否过期(0:未过期,1:已过期)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会话跟踪表';

-- 创建索引以提高查询性能
CREATE INDEX `idx_last_access_time` ON `session_tracker` (`last_access_time`);
CREATE INDEX `idx_start_time` ON `session_tracker` (`start_time`);
CREATE INDEX `idx_ip_address` ON `session_tracker` (`ip_address`);
CREATE INDEX `idx_is_expired` ON `session_tracker` (`is_expired`);