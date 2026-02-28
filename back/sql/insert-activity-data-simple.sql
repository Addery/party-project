USE party_management_system;

-- 插入6条活动数据
INSERT INTO activities (title, date, time, location, category, status, description, content, can_register, register_deadline, participants, capacity, views, image) VALUES
('党史学习活动', '2025-12-15', '09:00-11:30', '大礼堂', 'theme', 'published', '学习党史', '党史学习活动内容', 1, '2025-12-10', 3, 50, 120, '/images/activity1.jpg'),
('思想学习会', '2025-12-20', '14:00-16:30', '会议室A', 'learning', 'published', '学习思想', '思想学习会内容', 1, '2025-12-15', 2, 40, 95, '/images/activity2.jpg'),
('志愿服务', '2025-12-25', '08:30-12:00', '阳光社区', 'volunteer', 'published', '服务社区', '志愿服务内容', 1, '2025-12-20', 3, 60, 150, '/images/activity3.jpg'),
('支部会议', '2025-12-08', '15:00-17:00', '支部会议室', 'meeting', 'published', '支部工作', '支部会议内容', 1, '2025-12-05', 3, 10, 45, '/images/activity4.jpg'),
('新年茶话会', '2025-12-31', '16:00-18:00', '多功能厅', 'social', 'published', '辞旧迎新', '新年茶话会内容', 1, '2025-12-25', 4, 80, 200, '/images/activity5.jpg'),
('廉政教育讲座', '2025-12-18', '10:00-12:00', '报告厅', 'other', 'published', '廉政教育', '廉政教育讲座内容', 1, '2025-12-15', 4, 100, 280, '/images/activity6.jpg');

-- 插入活动附件数据
INSERT INTO activity_attachments (name, url, activity_id) VALUES
('学习资料.pdf', '/uploads/activity1.pdf', 1),
('会议议程.docx', '/uploads/activity2.docx', 2),
('活动安排.xlsx', '/uploads/activity3.xlsx', 3),
('会议记录.docx', '/uploads/activity4.docx', 4),
('节目单.pdf', '/uploads/activity5.pdf', 5),
('讲座PPT.pptx', '/uploads/activity6.pptx', 6);

-- 插入活动报名数据
INSERT INTO activity_registrations (activity_id, user_id, name, phone, register_time, status) VALUES
(1, 2, '张三', '13800138000', '2025-12-01 10:30:00', 'registered'),
(1, 3, '李四', '13800138001', '2025-12-02 14:20:00', 'registered'),
(1, 4, '王五', '13800138002', '2025-12-03 09:15:00', 'registered'),
(2, 2, '张三', '13800138000', '2025-12-05 16:45:00', 'registered'),
(2, 5, '赵六', '13800138003', '2025-12-06 11:20:00', 'registered'),
(3, 3, '李四', '13800138001', '2025-12-07 14:10:00', 'registered'),
(3, 5, '赵六', '13800138003', '2025-12-08 09:30:00', 'registered'),
(3, 6, '孙七', '13800138004', '2025-12-09 15:25:00', 'registered'),
(4, 2, '张三', '13800138000', '2025-12-01 10:30:00', 'registered'),
(4, 3, '李四', '13800138001', '2025-12-02 14:20:00', 'registered'),
(4, 4, '王五', '13800138002', '2025-12-03 09:15:00', 'registered'),
(5, 2, '张三', '13800138000', '2025-12-10 16:45:00', 'registered'),
(5, 3, '李四', '13800138001', '2025-12-11 11:20:00', 'registered'),
(5, 5, '赵六', '13800138003', '2025-12-12 14:10:00', 'registered'),
(5, 6, '孙七', '13800138004', '2025-12-13 09:30:00', 'registered'),
(6, 2, '张三', '13800138000', '2025-12-05 15:25:00', 'registered'),
(6, 3, '李四', '13800138001', '2025-12-06 10:30:00', 'registered'),
(6, 4, '王五', '13800138002', '2025-12-07 14:20:00', 'registered'),
(6, 5, '赵六', '13800138003', '2025-12-08 09:15:00', 'registered');

-- 更新活动参与人数
UPDATE activities a
SET participants = (
    SELECT COUNT(*) 
    FROM activity_registrations ar 
    WHERE ar.activity_id = a.id AND ar.status = 'registered'
);

COMMIT;