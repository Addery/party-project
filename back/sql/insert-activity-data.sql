-- 党建管理系统 - 活动模拟数据插入脚本
USE party_management_system;

-- 插入6条活动数据
INSERT INTO activities (title, content, description, category, organizer, start_time, end_time, registration_deadline, location, max_participants, current_participants, status, cover_image, created_by, created_at, updated_at, branch_id) VALUES
(  '党史学习教育主题党日活动',  '<h1>党史学习教育主题党日活动</h1><p>本次活动将深入学习中国共产党的光辉历史，增强党员的党性修养。</p><h2>活动议程</h2><ul><li>09:00-09:30 签到</li><li>09:30-10:30 主题报告</li><li>10:30-11:30 分组讨论</li></ul>',  '深入学习党史，增强党性修养',  'theme',  '党委办公室',  '2025-12-15 09:00:00',  '2025-12-15 11:30:00',  '2025-12-10 23:59:59',  '大礼堂',  50,  25,  'published',  '/images/activity1.jpg',  2,  NOW(),  NOW(),  1),
(  '习近平新时代中国特色社会主义思想学习会',  '<h1>习近平新时代中国特色社会主义思想学习会</h1><p>本次学习会将深入学习习近平新时代中国特色社会主义思想的核心要义。</p><h2>学习内容</h2><ul><li>习近平新时代中国特色社会主义思想概述</li><li>“十个明确”核心内容解读</li><li>如何将理论学习与实际工作相结合</li></ul>',  '学习贯彻习近平新时代中国特色社会主义思想',  'learning',  '宣传部',  '2025-12-20 14:00:00',  '2025-12-20 16:30:00',  '2025-12-15 23:59:59',  '会议室A',  40,  18,  'published',  '/images/activity2.jpg',  2,  NOW(),  NOW(),  1),
(  '社区志愿服务活动',  '<h1>社区志愿服务活动</h1><p>组织党员志愿者走进社区，开展清洁家园、扶老助残等志愿服务活动。</p><h2>服务内容</h2><ul><li>社区环境清洁</li><li>为独居老人提供帮助</li><li>政策宣传咨询</li></ul>',  '走进社区，服务群众',  'volunteer',  '团委',  '2025-12-25 08:30:00',  '2025-12-25 12:00:00',  '2025-12-20 23:59:59',  '阳光社区',  60,  32,  'published',  '/images/activity3.jpg',  3,  NOW(),  NOW(),  2),
(  '支部委员会会议',  '<h1>支部委员会会议</h1><p>讨论近期党建工作安排和党员发展事宜。</p><h2>会议议程</h2><ul><li>近期工作回顾</li><li>下月工作计划</li><li>党员发展讨论</li><li>其他事项</li></ul>',  '讨论近期党建工作安排',  'meeting',  '计算机学院学生第一党支部',  '2025-12-08 15:00:00',  '2025-12-08 17:00:00',  '2025-12-05 23:59:59',  '支部会议室',  10,  8,  'published',  '/images/activity4.jpg',  4,  NOW(),  NOW(),  3),
(  '新年茶话会',  '<h1>新年茶话会</h1><p>举办新年茶话会，辞旧迎新，共话发展。</p><h2>活动安排</h2><ul><li>16:00-16:30 签到入场</li><li>16:30-17:00 领导致辞</li><li>17:00-18:00 交流茶话</li></ul>',  '辞旧迎新，共话发展',  'social',  '党委办公室',  '2025-12-31 16:00:00',  '2025-12-31 18:00:00',  '2025-12-25 23:59:59',  '多功能厅',  80,  45,  'published',  '/images/activity5.jpg',  2,  NOW(),  NOW(),  1),
(  '党风廉政教育专题讲座',  '<h1>党风廉政教育专题讲座</h1><p>邀请专家开展党风廉政教育专题讲座，加强党员干部的廉洁自律意识。</p><h2>讲座内容</h2><ul><li>当前党风廉政建设形势</li><li>典型案例分析</li><li>如何加强廉洁自律</li></ul>',  '加强党风廉政建设，筑牢思想防线',  'other',  '纪委',  '2025-12-18 10:00:00',  '2025-12-18 12:00:00',  '2025-12-15 23:59:59',  '报告厅',  100,  65,  'published',  '/images/activity6.jpg',  2,  NOW(),  NOW(),  1);

-- 插入活动附件数据
INSERT INTO activity_attachments (activity_id, file_name, file_path, file_size, file_type, upload_time, created_at, updated_at) VALUES
(1, '党史学习教育资料.pdf', '/uploads/activity/20251215/123456.pdf', 1024000, 'application/pdf', NOW(), NOW(), NOW()),
(2, '习近平新时代中国特色社会主义思想学习纲要.pdf', '/uploads/activity/20251220/234567.pdf', 1536000, 'application/pdf', NOW(), NOW(), NOW()),
(3, '志愿服务活动安排表.docx', '/uploads/activity/20251225/345678.docx', 512000, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', NOW(), NOW(), NOW()),
(4, '支部委员会会议议程.docx', '/uploads/activity/20251208/456789.docx', 256000, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', NOW(), NOW(), NOW()),
(5, '新年茶话会节目单.xlsx', '/uploads/activity/20251231/567890.xlsx', 768000, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', NOW(), NOW(), NOW()),
(6, '党风廉政教育PPT.pptx', '/uploads/activity/20251218/678901.pptx', 2048000, 'application/vnd.openxmlformats-officedocument.presentationml.presentation', NOW(), NOW(), NOW()),
(1, '党史知识竞赛题库.docx', '/uploads/activity/20251215/789012.docx', 384000, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', NOW(), NOW(), NOW()),
(2, '学习会讨论题.pdf', '/uploads/activity/20251220/890123.pdf', 640000, 'application/pdf', NOW(), NOW(), NOW());

-- 插入活动报名数据
INSERT INTO activity_registrations (activity_id, user_id, registration_time, status, created_at, updated_at) VALUES
(1, 2, '2025-12-01 10:30:00', 'registered', NOW(), NOW()),
(1, 3, '2025-12-02 14:20:00', 'registered', NOW(), NOW()),
(1, 4, '2025-12-03 09:15:00', 'registered', NOW(), NOW()),
(2, 2, '2025-12-05 16:45:00', 'registered', NOW(), NOW()),
(2, 5, '2025-12-06 11:20:00', 'registered', NOW(), NOW()),
(3, 3, '2025-12-07 14:10:00', 'registered', NOW(), NOW()),
(3, 5, '2025-12-08 09:30:00', 'registered', NOW(), NOW()),
(3, 6, '2025-12-09 15:25:00', 'registered', NOW(), NOW()),
(4, 2, '2025-12-01 10:30:00', 'registered', NOW(), NOW()),
(4, 3, '2025-12-02 14:20:00', 'registered', NOW(), NOW()),
(4, 4, '2025-12-03 09:15:00', 'registered', NOW(), NOW()),
(5, 2, '2025-12-10 16:45:00', 'registered', NOW(), NOW()),
(5, 3, '2025-12-11 11:20:00', 'registered', NOW(), NOW()),
(5, 5, '2025-12-12 14:10:00', 'registered', NOW(), NOW()),
(5, 6, '2025-12-13 09:30:00', 'registered', NOW(), NOW()),
(6, 2, '2025-12-05 15:25:00', 'registered', NOW(), NOW()),
(6, 3, '2025-12-06 10:30:00', 'registered', NOW(), NOW()),
(6, 4, '2025-12-07 14:20:00', 'registered', NOW(), NOW()),
(6, 5, '2025-12-08 09:15:00', 'registered', NOW(), NOW());

-- 更新活动参与人数
UPDATE activities a
SET current_participants = (
    SELECT COUNT(*) 
    FROM activity_registrations ar 
    WHERE ar.activity_id = a.id AND ar.status = 'registered'
);

COMMIT;
