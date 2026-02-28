-- 党建管理系统 - 注册白名单表SQL脚本
USE party_management_system;

-- 创建注册白名单表
CREATE TABLE IF NOT EXISTS registration_whitelist (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '白名单ID',
    phone VARCHAR(11) NOT NULL COMMENT '手机号码',
    id_card VARCHAR(18) NOT NULL COMMENT '身份证号',
    remark VARCHAR(255) COMMENT '备注',
    UNIQUE KEY uk_phone_id_card (phone, id_card) COMMENT '手机号和身份证号组合唯一'
) COMMENT '注册白名单表';

-- 插入示例白名单数据
INSERT INTO registration_whitelist (phone, id_card, remark) VALUES
('13800138001', '110101199001011234', '测试用户1'),
('13800138002', '110101199001011235', '测试用户2'),
('13800138003', '110101199001011236', '测试用户3'),
('13800138004', '110101199001011237', '测试用户4'),
('13800138005', '110101199001011238', '测试用户5');

-- 查询白名单数据
SELECT * FROM registration_whitelist;