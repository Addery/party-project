# Set MySQL connection information
$mysqlHost = 'localhost'
$mysqlUser = 'root'
$mysqlPassword = '123456'
$mysqlDatabase = 'party_management_system'

# Read SQL file content
$sqlFilePath = "h:\dev\project\partyproject\v3\back\sql\insert-notice-data.sql"
$sqlContent = Get-Content -Path $sqlFilePath -Encoding UTF8

# Execute SQL script directly using mysql with utf8mb4 encoding
Write-Host "Executing SQL script..."
mysql --default-character-set=utf8mb4 -h $mysqlHost -u $mysqlUser -p$mysqlPassword -D $mysqlDatabase -e "$sqlContent"

# Check execution result
if ($LASTEXITCODE -eq 0) {
    Write-Host "SQL script executed successfully!"
    
    # Verify data insertion
    Write-Host "\nVerifying notice data insertion:"
    mysql --default-character-set=utf8mb4 -h $mysqlHost -u $mysqlUser -p$mysqlPassword -D $mysqlDatabase -e "SELECT id, title, category, publish_date FROM notices LIMIT 8"
    
    Write-Host "\nVerifying attachment data insertion:"
    mysql --default-character-set=utf8mb4 -h $mysqlHost -u $mysqlUser -p$mysqlPassword -D $mysqlDatabase -e "SELECT id, notice_id, name FROM attachments LIMIT 8"
} else {
    Write-Host "SQL script execution failed, error code: $LASTEXITCODE"
}