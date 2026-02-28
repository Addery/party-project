-- 为所有用户添加或更新用户画像数据

-- 用户ID: 2 - 已有数据，保持不变

-- 用户ID: 3
INSERT INTO user_portrait (user_id, learning_interests, learning_preferences, learning_tags) VALUES 
(3, '["理论学习", "党史研究", "政策解读", "思想建设", "文化传承"]', '{"newsReading":{"理论动态":30, "党史故事":25, "政策解读":20}, "learningTopics":{"党的理论":35, "党史研究":30, "思想建设":25}}', '["理论研究者", "党史专家", "政策解读员", "思想引领者", "学习认真"]')
ON DUPLICATE KEY UPDATE 
learning_interests = VALUES(learning_interests), 
learning_preferences = VALUES(learning_preferences), 
learning_tags = VALUES(learning_tags);

-- 用户ID: 4
INSERT INTO user_portrait (user_id, learning_interests, learning_preferences, learning_tags) VALUES 
(4, '["科技创新", "工程实践", "技术研发", "智能制造", "数据分析"]', '{"newsReading":{"科技创新":30, "技术动态":25, "智能制造":20}, "learningTopics":{"科技前沿":35, "工程技术":30, "数据分析":25}}', '["科技工作者", "技术专家", "创新思维", "实践能力强", "学习能力强"]')
ON DUPLICATE KEY UPDATE 
learning_interests = VALUES(learning_interests), 
learning_preferences = VALUES(learning_preferences), 
learning_tags = VALUES(learning_tags);

-- 用户ID: 5 - 已有数据，保持不变

-- 用户ID: 6
INSERT INTO user_portrait (user_id, learning_interests, learning_preferences, learning_tags) VALUES 
(6, '["基层党建", "社区服务", "群众工作", "组织建设", "社会工作"]', '{"newsReading":{"基层党建":30, "社区动态":25, "群众工作":20}, "learningTopics":{"基层治理":35, "社区服务":30, "群众工作方法":25}}', '["基层工作者", "群众贴心人", "组织协调能力", "服务意识强", "责任心强"]')
ON DUPLICATE KEY UPDATE 
learning_interests = VALUES(learning_interests), 
learning_preferences = VALUES(learning_preferences), 
learning_tags = VALUES(learning_tags);

-- 用户ID: 7
INSERT INTO user_portrait (user_id, learning_interests, learning_preferences, learning_tags) VALUES 
(7, '["青年工作", "网络党建", "创新实践", "志愿服务", "文化活动"]', '{"newsReading":{"青年工作":30, "网络党建":25, "创新实践":20}, "learningTopics":{"青年发展":35, "网络技术":30, "创新方法":25}}', '["青年工作者", "网络达人", "创新思维", "志愿服务精神", "活力四射"]')
ON DUPLICATE KEY UPDATE 
learning_interests = VALUES(learning_interests), 
learning_preferences = VALUES(learning_preferences), 
learning_tags = VALUES(learning_tags);

-- 用户ID: 8 - 已有数据，保持不变

-- 用户ID: 9 - 日期测试用户
INSERT INTO user_portrait (user_id, learning_interests, learning_preferences, learning_tags) VALUES 
(9, '["时间管理", "数据分析", "效率提升", "项目管理", "学习方法"]', '{"newsReading":{"时间管理":30, "效率提升":25, "项目管理":20}, "learningTopics":{"时间管理技巧":35, "数据分析":30, "项目管理方法":25}}', '["时间管理专家", "效率提升达人", "数据分析能力", "项目管理经验", "学习方法好"]')
ON DUPLICATE KEY UPDATE 
learning_interests = VALUES(learning_interests), 
learning_preferences = VALUES(learning_preferences), 
learning_tags = VALUES(learning_tags);

-- 用户ID: 10 - 系统管理员
INSERT INTO user_portrait (user_id, learning_interests, learning_preferences, learning_tags) VALUES 
(10, '["系统管理", "权限控制", "数据分析", "系统优化", "政策研究"]', '{"newsReading":{"系统管理":30, "政策法规":25, "数据分析":20}, "learningTopics":{"系统架构":35, "政策研究":30, "数据分析":25}}', '["系统管理员", "政策研究专家", "数据分析能力", "管理能力", "技术背景"]')
ON DUPLICATE KEY UPDATE 
learning_interests = VALUES(learning_interests), 
learning_preferences = VALUES(learning_preferences), 
learning_tags = VALUES(learning_tags);

-- 查看所有用户画像数据
SELECT * FROM user_portrait ORDER BY user_id;