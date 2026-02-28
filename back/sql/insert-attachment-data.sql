-- 党建管理系统 - 附件模拟数据插入脚本
INSERT INTO attachments (related_id, type, file_name, file_path, file_size, file_type, upload_time, created_at, updated_at) VALUES
(1, 'news', '党史学习教育资料.pdf', '/uploads/news/1/党史学习教育资料.pdf', 1024000, 'application/pdf', NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),
(2, 'activity', '活动议程.docx', '/uploads/activity/2/活动议程.docx', 512000, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),
(1, 'resource', '马克思主义基本原理概论.pdf', '/uploads/resource/1/马克思主义基本原理概论.pdf', 2048000, 'application/pdf', NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY),
(3, 'news', '党的二十大报告全文.pdf', '/uploads/news/3/党的二十大报告全文.pdf', 3072000, 'application/pdf', NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY),
(4, 'activity', '志愿者培训手册.pptx', '/uploads/activity/4/志愿者培训手册.pptx', 1536000, 'application/vnd.openxmlformats-officedocument.presentationml.presentation', NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY),
(2, 'resource', '习近平新时代中国特色社会主义思想学习纲要.docx', '/uploads/resource/2/习近平新时代中国特色社会主义思想学习纲要.docx', 768000, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY),
(5, 'news', '校园党建工作简报第10期.pdf', '/uploads/news/5/校园党建工作简报第10期.pdf', 896000, 'application/pdf', NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY),
(6, 'activity', '民主生活会记录表.xlsx', '/uploads/activity/6/民主生活会记录表.xlsx', 640000, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY),
(3, 'resource', '中国共产党章程.txt', '/uploads/resource/3/中国共产党章程.txt', 128000, 'text/plain', NOW() - INTERVAL 9 DAY, NOW() - INTERVAL 9 DAY, NOW() - INTERVAL 9 DAY),
(7, 'news', '党风廉政建设警示教育片.mp4', '/uploads/news/7/党风廉政建设警示教育片.mp4', 5120000, 'video/mp4', NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY);

COMMIT;