-- 党建管理系统 - 党员画像模拟数据插入脚本
INSERT INTO party_member_portrait (user_id, username, party_years, education, job, party_status, gender, age, branch_name, learning_hours, course_completions, activity_participation, volunteer_hours, award_count, evaluation_score, last_update_date, created_at, updated_at) VALUES
(2, 'manager1', 15, 'BACHELOR', '组织部部长', 'ACTIVE', 'MALE', '40', '组织部', 120, 8, 15, 50, 3, 95.5, NOW(), NOW(), NOW()),
(3, 'manager2', 12, 'MASTER', '宣传部部长', 'ACTIVE', 'FEMALE', '38', '宣传部', 100, 6, 12, 40, 2, 92.8, NOW(), NOW(), NOW()),
(4, 'member1', 2, 'BACHELOR', '学生党员', 'PROBATIONARY', 'MALE', '28', '计算机学院学生第一党支部', 60, 4, 8, 15, 1, 85.2, NOW(), NOW(), NOW()),
(5, 'member2', 5, 'DOCTOR', '教师党员', 'ACTIVE', 'FEMALE', '30', '马克思主义学院教工党支部', 150, 10, 20, 60, 4, 98.0, NOW(), NOW(), NOW()),
(6, 'member3', 1, 'BACHELOR', '学生党员', 'PROBATIONARY', 'MALE', '29', '机械工程学院学生第二党支部', 45, 3, 5, 10, 0, 82.5, NOW(), NOW(), NOW()),
(7, 'member4', 7, 'MASTER', '教师党员', 'ACTIVE', 'FEMALE', '32', '电子工程学院教工第一党支部', 130, 9, 18, 55, 3, 94.6, NOW(), NOW(), NOW()),
(8, 'member5', 4, 'MASTER', '教师党员', 'ACTIVE', 'MALE', '31', '经济管理学院学生第三党支部', 90, 7, 14, 35, 2, 91.3, NOW(), NOW(), NOW()),
(9, 'member6', 0, 'MASTER', '教师', 'APPLICANT', 'FEMALE', '27', '外国语学院教工第二党支部', 30, 2, 3, 8, 0, 78.9, NOW(), NOW(), NOW()),
(10, 'member7', 1, 'BACHELOR', '学生党员', 'PROBATIONARY', 'MALE', '26', '计算机学院学生第一党支部', 50, 3, 7, 12, 1, 84.7, NOW(), NOW(), NOW());

COMMIT;