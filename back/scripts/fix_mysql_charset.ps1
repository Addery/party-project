# 修改MySQL配置文件以设置正确的字符集

# 找到my.ini文件位置
$mysqlConfig = Get-Content "C:\ProgramData\MySQL\MySQL Server 8.0\my.ini"

# 添加字符集配置
$mysqlConfig = $mysqlConfig + "\n# Character set configuration" + "\ncharacter-set-client-handshake = FALSE" + "\ncharacter-set-server = utf8mb4" + "\ncollation-server = utf8mb4_unicode_ci" + "\ninit_connect='SET NAMES utf8mb4'"

# 保存修改后的配置文件
$mysqlConfig | Out-File "C:\ProgramData\MySQL\MySQL Server 8.0\my.ini" -Encoding ASCII

# 重启MySQL服务
Restart-Service -Name MySQL80 -Force

# 等待服务重启
Start-Sleep -Seconds 10

# 验证字符集配置
mysql -uroot -p123456 -e "SHOW VARIABLES LIKE 'character_set_%'; SHOW VARIABLES LIKE 'collation_%';"