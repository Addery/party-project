@echo off
SET SQL_FILE=h:\dev\project\partyproject\v3\back\insert_learning_resource_mock_data.sql
mysql -u root -p < "%SQL_FILE%"
echo Mock data inserted successfully!
pause
