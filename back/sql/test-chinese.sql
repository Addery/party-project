-- 测试中文数据插入和查询
USE party_management_system;

-- 插入一条测试数据
INSERT INTO notices (title, category, content, author, department, summary, publish_date, created_at, is_top)
VALUES ('测试中文标题', '工作通知', '这是测试中文内容', '测试作者', '测试部门', '测试摘要', NOW(), NOW(), 0);

-- 查询测试数据
SELECT id, title, category, author, department FROM notices WHERE title = '测试中文标题';

-- 删除测试数据
DELETE FROM notices WHERE title = '测试中文标题';