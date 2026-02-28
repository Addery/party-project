-- 检查数据库字符集
USE party_management_system;
SELECT @@character_set_database, @@collation_database;

-- 检查表的字符集
SHOW CREATE TABLE activities;
SHOW CREATE TABLE activity_attachments;
SHOW CREATE TABLE activity_registrations;