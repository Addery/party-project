@echo off
mysql -u root -p123456 --default-character-set=utf8mb4 party_management_system << EOF
source h:/dev/project/partyproject/v3/back/sql/insert-notice-data.sql
EOF
echo SQL execution completed.
pause