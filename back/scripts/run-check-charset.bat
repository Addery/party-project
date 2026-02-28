@echo off
mysql -u root -p123456 party_management_system < check-charset.sql
echo Charset check completed.
pause