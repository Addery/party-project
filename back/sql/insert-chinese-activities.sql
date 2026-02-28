-- 清空现有数据
DELETE FROM activity_registrations;
DELETE FROM activity_attachments;
DELETE FROM activities;
ALTER TABLE activities AUTO_INCREMENT = 1;
ALTER TABLE activity_attachments AUTO_INCREMENT = 1;
ALTER TABLE activity_registrations AUTO_INCREMENT = 1;

-- 插入6条活动数据（近一年以内的日期）
INSERT INTO activities (title, date, time, location, category, status, description, content, can_register, register_deadline, participants, capacity, views, image) VALUES 
('党史学习教育主题党日活动', '2024-12-15', '09:00-11:30', '大礼堂', 'theme', 'published', '深入学习党史，增强党性修养', '党史学习教育主题党日活动内容', true, '2024-12-10', 3, 50, 120, '/images/activity1.jpg'),
('习近平新时代中国特色社会主义思想学习会', '2024-11-20', '14:00-16:30', '会议室A', 'learning', 'published', '学习贯彻习近平新时代中国特色社会主义思想', '思想学习会内容', true, '2024-11-15', 2, 40, 95, '/images/activity2.jpg'),
('社区志愿服务活动', '2024-10-25', '08:30-12:00', '阳光社区', 'volunteer', 'published', '走进社区，服务群众', '志愿服务内容', true, '2024-10-20', 3, 60, 150, '/images/activity3.jpg'),
('支部委员会会议', '2024-09-08', '15:00-17:00', '支部会议室', 'meeting', 'published', '讨论近期党建工作安排', '支部会议内容', true, '2024-09-05', 3, 10, 45, '/images/activity4.jpg'),
('新年茶话会', '2024-12-31', '16:00-18:00', '多功能厅', 'social', 'published', '辞旧迎新，共话发展', '新年茶话会内容', true, '2024-12-25', 4, 80, 200, '/images/activity5.jpg'),
('党风廉政教育专题讲座', '2024-11-18', '10:00-12:00', '报告厅', 'other', 'published', '加强党风廉政建设，筑牢思想防线', '廉政教育讲座内容', true, '2024-11-15', 4, 100, 280, '/images/activity6.jpg');

-- 显示插入的活动数据
SELECT id, title, date FROM activities LIMIT 6;
