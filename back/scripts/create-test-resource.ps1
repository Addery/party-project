# Create a test learning resource
Write-Host "Creating test learning resource..."

# Since we don't have a create API, we'll check if there's any default data
# Let's try with a specific category ID
Write-Host "\nTesting GET /api/learning/resources with category filter"
try {
    $resourcesResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/learning/resources?page=1&pageSize=10&type=video" -Method GET -ContentType "application/json"
    Write-Host "Response: $($resourcesResponse | ConvertTo-Json -Depth 5)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# Let's check the database schema to understand the structure
Write-Host "\nChecking if there's a data.sql file for initialization"
if (Test-Path "src/main/resources/data.sql") {
    Get-Content "src/main/resources/data.sql" | Write-Host
} else {
    Write-Host "No data.sql file found"
}

Write-Host "\nAPI testing completed."