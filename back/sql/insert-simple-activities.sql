USE party_management_system;

-- 清空现有数据
DELETE FROM activity_registrations;
DELETE FROM activity_attachments;
DELETE FROM activities;

-- 设置id自增从1开始
ALTER TABLE activities AUTO_INCREMENT = 1;
ALTER TABLE activity_attachments AUTO_INCREMENT = 1;
ALTER TABLE activity_registrations AUTO_INCREMENT = 1;

-- 插入6条活动数据（近一年以内的日期）
INSERT INTO activities (title, date, time, location, category, status, description, content, can_register, register_deadline, participants, capacity, views, image) VALUES
('活动1', '2024-12-15', '09:00-11:30', '地点1', 'theme', 'published', '描述1', '内容1', 1, '2024-12-10', 3, 50, 120, '/images/activity1.jpg'),
('活动2', '2024-11-20', '14:00-16:30', '地点2', 'learning', 'published', '描述2', '内容2', 1, '2024-11-15', 2, 40, 95, '/images/activity2.jpg'),
('活动3', '2024-10-25', '08:30-12:00', '地点3', 'volunteer', 'published', '描述3', '内容3', 1, '2024-10-20', 3, 60, 150, '/images/activity3.jpg'),
('活动4', '2024-09-08', '15:00-17:00', '地点4', 'meeting', 'published', '描述4', '内容4', 1, '2024-09-05', 3, 10, 45, '/images/activity4.jpg'),
('活动5', '2024-12-31', '16:00-18:00', '地点5', 'social', 'published', '描述5', '内容5', 1, '2024-12-25', 4, 80, 200, '/images/activity5.jpg'),
('活动6', '2024-11-18', '10:00-12:00', '地点6', 'other', 'published', '描述6', '内容6', 1, '2024-11-15', 4, 100, 280, '/images/activity6.jpg');

COMMIT;
