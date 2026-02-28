-- 指定要使用的数据库
USE party_management_system;

-- 检查注册白名单表是否存在
SHOW TABLES LIKE 'registration_whitelist';

-- 如果表不存在，则创建表
CREATE TABLE IF NOT EXISTS registration_whitelist (
    id INT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL,
    id_card VARCHAR(18) NOT NULL,
    remark VARCHAR(255),
    UNIQUE KEY uk_phone_idcard (phone, id_card)
);

-- 插入一些测试数据
INSERT INTO registration_whitelist (phone, id_card, remark) VALUES
('13800138001', '110101199001011234', '测试用户1'),
('13800138002', '110101199001011235', '测试用户2'),
('13800138003', '110101199001011236', '测试用户3'),
('13800138004', '110101199001011237', '测试用户4'),
('13800138005', '110101199001011238', '测试用户5')
ON DUPLICATE KEY UPDATE remark = VALUES(remark);

-- 查询表中的数据
SELECT * FROM registration_whitelist;