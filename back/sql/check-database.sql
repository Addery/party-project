-- 检查数据库表结构
USE party_management_system;

-- 检查用户画像相关表是否存在
SHOW TABLES LIKE 'user%';

-- 查看用户表结构
DESC users;

-- 查看用户画像表结构
DESC user_portrait;
DESC user_profiles;
DESC user_preferences;
DESC user_personality;

-- 查看已有数据
SELECT * FROM users;
SELECT * FROM user_portrait;
SELECT * FROM user_profiles;
SELECT * FROM user_preferences;
SELECT * FROM user_personality;
