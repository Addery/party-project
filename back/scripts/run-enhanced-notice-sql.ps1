# 执行增强版公告数据SQL脚本
$mysqlPath = "mysql"
$user = "root"
$password = "123456"
$database = "party_management_system"
$sqlFile = "h:\dev\project\partyproject\v3\back\sql\insert-notice-data-enhanced.sql"

# 使用utf8mb4字符集执行SQL文件
& $mysqlPath -u $user -p$password --default-character-set=utf8mb4 -D $database -e "SET NAMES utf8mb4;" < $sqlFile