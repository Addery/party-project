# 设置MySQL连接信息
$mysqlHost = 'localhost'
$mysqlUser = 'root'
$mysqlPassword = '123456'
$mysqlDatabase = 'party_management_system'

# 设置CSV文件路径
$csvFile = 'H:\dev\project\partyproject\v3\back\activities.csv'

# 执行清空数据的SQL命令
Write-Host "清空现有数据..."
mysql --default-character-set=utf8mb4 -h $mysqlHost -u $mysqlUser -p$mysqlPassword -D $mysqlDatabase -e "DELETE FROM activity_registrations; DELETE FROM activity_attachments; DELETE FROM activities; ALTER TABLE activities AUTO_INCREMENT = 1; ALTER TABLE activity_attachments AUTO_INCREMENT = 1; ALTER TABLE activity_registrations AUTO_INCREMENT = 1;"

# 使用LOAD DATA命令导入中文数据
Write-Host "导入中文活动数据..."
mysql --default-character-set=utf8mb4 -h $mysqlHost -u $mysqlUser -p$mysqlPassword -D $mysqlDatabase -e "LOAD DATA INFILE '$csvFile' INTO TABLE activities CHARACTER SET utf8mb4 FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 1 ROWS (title, date, time, location, category, status, description, content, can_register, register_deadline, participants, capacity, views, image);"

Write-Host "中文活动数据导入完成！"

# 验证数据插入
Write-Host "\n验证数据插入结果："
mysql --default-character-set=utf8mb4 -h $mysqlHost -u $mysqlUser -p$mysqlPassword -D $mysqlDatabase -e "SELECT id, title, date FROM activities LIMIT 6"

# 测试API响应
Write-Host "\n测试API响应："
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/activities/1" -Method GET -UseBasicParsing
Write-Host "Status Code: $($response.StatusCode)"
Write-Host "Content: $($response.Content)"