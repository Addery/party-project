-- 党建管理系统 - 党员进度模拟数据插入脚本
INSERT INTO party_member_progress (user_id, progress_type, progress_title, progress_date, description, status, created_at, updated_at) VALUES
(2, 'study', '党史学习教育', '2025-03-15', '完成党史学习教育专题课程，获得结业证书', 'completed', NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),
(3, 'activity', '志愿服务活动', '2025-03-20', '参与社区志愿服务活动，服务时长8小时', 'completed', NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),
(4, 'training', '入党积极分子培训', '2025-03-10', '参加入党积极分子培训，通过考核', 'completed', NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY),
(5, 'study', '习近平新时代中国特色社会主义思想学习', '2025-03-25', '完成习近平新时代中国特色社会主义思想专题学习', 'completed', NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY),
(6, 'activity', '主题党日活动', '2025-03-05', '参加“不忘初心、牢记使命”主题党日活动', 'completed', NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY),
(7, 'training', '党员发展对象培训', '2025-03-18', '参加党员发展对象培训，成绩优秀', 'completed', NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY),
(8, 'study', '党纪党规学习', '2025-03-22', '完成党纪党规学习测试，成绩合格', 'completed', NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY),
(9, 'activity', '民主生活会', '2025-03-08', '参加支部民主生活会，开展批评与自我批评', 'completed', NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY),
(10, 'training', '新党员培训', '2025-03-12', '参加新党员培训，学习党的基础知识', 'completed', NOW() - INTERVAL 9 DAY, NOW() - INTERVAL 9 DAY),
(2, 'study', '党的二十大精神学习', '2025-03-28', '深入学习党的二十大精神，撰写学习心得', 'in_progress', NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY);

COMMIT;