@echo off
mysql --default-character-set=utf8mb4 -u root -p123456 party_management_system < fixed_activity_data.sql
echo SQL execution completed.
pause