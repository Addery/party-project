-- 插入近期学习资源数据到learning_resources表
INSERT INTO learning_resources (title, type, category, author, upload_date, views, status, url, description, content, upload_by) VALUES 
('2025年中央经济工作会议精神解读', 'video', 'policy', '中央党校教授', '2025-12-15', 0, 'published', NULL, '解读2025年中央经济工作会议精神的权威视频课程', '2025年中央经济工作会议于12月12日至14日在北京举行，会议总结了2025年经济工作，分析了当前经济形势，部署了2026年经济工作。', NULL),
('新发展阶段党员教育手册', 'document', 'theory', '党建出版社', '2025-12-10', 0, 'published', NULL, '新发展阶段党员教育的重要参考资料', '本手册旨在帮助党员深入学习新发展阶段的理论知识，提高政治素质和工作能力。', NULL),
('科技创新与高质量发展', 'video', 'model', '科技研究院', '2025-11-28', 0, 'published', NULL, '科技创新推动高质量发展的专题讲座', '本讲座探讨了科技创新在推动经济高质量发展中的重要作用和实践路径。', NULL),
('数字经济时代的党建工作创新', 'article', 'theory', '数字党建中心', '2025-11-20', 0, 'published', NULL, '数字经济时代党建工作创新的实践指南', '本文分析了数字经济时代党建工作面临的新挑战和新机遇，提出了创新党建工作的思路和方法。', NULL);

-- 删除旧的learning_resource表
DROP TABLE IF EXISTS learning_resource;