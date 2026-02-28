USE party_management_system;

-- Insert 6 activity records
INSERT INTO activities (title, date, time, location, category, status, description, content, can_register, register_deadline, participants, capacity, views, image) VALUES
('Party History Learning', '2025-12-15', '09:00-11:30', 'Auditorium', 'theme', 'published', 'Learn Party history', 'Content for Party history learning', 1, '2025-12-10', 3, 50, 120, '/images/activity1.jpg'),
('Ideology Study', '2025-12-20', '14:00-16:30', 'Meeting Room A', 'learning', 'published', 'Study ideology', 'Content for ideology study', 1, '2025-12-15', 2, 40, 95, '/images/activity2.jpg'),
('Volunteer Service', '2025-12-25', '08:30-12:00', 'Sunshine Community', 'volunteer', 'published', 'Serve community', 'Content for volunteer service', 1, '2025-12-20', 3, 60, 150, '/images/activity3.jpg'),
('Branch Meeting', '2025-12-08', '15:00-17:00', 'Branch Office', 'meeting', 'published', 'Branch work', 'Content for branch meeting', 1, '2025-12-05', 3, 10, 45, '/images/activity4.jpg'),
('New Year Tea Party', '2025-12-31', '16:00-18:00', 'Multifunctional Hall', 'social', 'published', 'Celebrate new year', 'Content for new year tea party', 1, '2025-12-25', 4, 80, 200, '/images/activity5.jpg'),
('Clean Government Education', '2025-12-18', '10:00-12:00', 'Lecture Hall', 'other', 'published', 'Clean government education', 'Content for clean government education', 1, '2025-12-15', 4, 100, 280, '/images/activity6.jpg');

-- Insert activity attachments
INSERT INTO activity_attachments (name, url, activity_id) VALUES
('learning-material.pdf', '/uploads/activity1.pdf', 1),
('meeting-agenda.docx', '/uploads/activity2.docx', 2),
('activity-schedule.xlsx', '/uploads/activity3.xlsx', 3),
('meeting-minutes.docx', '/uploads/activity4.docx', 4),
('program-list.pdf', '/uploads/activity5.pdf', 5),
('lecture-ppt.pptx', '/uploads/activity6.pptx', 6);

-- Insert activity registrations
INSERT INTO activity_registrations (activity_id, user_id, name, phone, register_time, status) VALUES
(1, 2, 'Zhang San', '13800138000', '2025-12-01 10:30:00', 'registered'),
(1, 3, 'Li Si', '13800138001', '2025-12-02 14:20:00', 'registered'),
(1, 4, 'Wang Wu', '13800138002', '2025-12-03 09:15:00', 'registered'),
(2, 2, 'Zhang San', '13800138000', '2025-12-05 16:45:00', 'registered'),
(2, 5, 'Zhao Liu', '13800138003', '2025-12-06 11:20:00', 'registered'),
(3, 3, 'Li Si', '13800138001', '2025-12-07 14:10:00', 'registered'),
(3, 5, 'Zhao Liu', '13800138003', '2025-12-08 09:30:00', 'registered'),
(3, 6, 'Sun Qi', '13800138004', '2025-12-09 15:25:00', 'registered'),
(4, 2, 'Zhang San', '13800138000', '2025-12-01 10:30:00', 'registered'),
(4, 3, 'Li Si', '13800138001', '2025-12-02 14:20:00', 'registered'),
(4, 4, 'Wang Wu', '13800138002', '2025-12-03 09:15:00', 'registered'),
(5, 2, 'Zhang San', '13800138000', '2025-12-10 16:45:00', 'registered'),
(5, 3, 'Li Si', '13800138001', '2025-12-11 11:20:00', 'registered'),
(5, 5, 'Zhao Liu', '13800138003', '2025-12-12 14:10:00', 'registered'),
(5, 6, 'Sun Qi', '13800138004', '2025-12-13 09:30:00', 'registered'),
(6, 2, 'Zhang San', '13800138000', '2025-12-05 15:25:00', 'registered'),
(6, 3, 'Li Si', '13800138001', '2025-12-06 10:30:00', 'registered'),
(6, 4, 'Wang Wu', '13800138002', '2025-12-07 14:20:00', 'registered'),
(6, 5, 'Zhao Liu', '13800138003', '2025-12-08 09:15:00', 'registered');

-- Update activity participants count
UPDATE activities a
SET participants = (
    SELECT COUNT(*) 
    FROM activity_registrations ar 
    WHERE ar.activity_id = a.id AND ar.status = 'registered'
);

COMMIT;