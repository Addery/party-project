# PowerShell script to test learning resource APIs

$baseUrl = "http://localhost:8080/api/learning"

Write-Host "=== Testing Learning Resource APIs ===="
Write-Host ""

# 1. Test getting resource categories
Write-Host "1. Testing GET $baseUrl/categories"
$categoriesResult = Invoke-RestMethod -Uri "$baseUrl/categories" -Method GET -ContentType "application/json"
Write-Host "Status code: $($categoriesResult.status)"
Write-Host "Response:"
$categoriesResult | ConvertTo-Json
Write-Host ""

# 2. Test getting learning resources
Write-Host "2. Testing GET $baseUrl/resources"
$resourcesResult = Invoke-RestMethod -Uri "$baseUrl/resources" -Method GET -ContentType "application/json"
Write-Host "Status code: $($resourcesResult.status)"
Write-Host "Response:"
$resourcesResult | ConvertTo-Json
Write-Host ""

# 3. Test getting resource details
# Get the first resource ID from the resources list
if ($resourcesResult.status -eq 200 -and $resourcesResult.data -and $resourcesResult.data.list -and $resourcesResult.data.list.Count -gt 0) {
    $firstResourceId = $resourcesResult.data.list[0].id
    
    Write-Host "3. Testing GET $baseUrl/resources/$firstResourceId"
    $resourceDetailResult = Invoke-RestMethod -Uri "$baseUrl/resources/$firstResourceId" -Method GET -ContentType "application/json"
    Write-Host "Status code: $($resourceDetailResult.status)"
    Write-Host "Response:"
    $resourceDetailResult | ConvertTo-Json
    Write-Host ""
    
    # 4. Test starting learning resource
    Write-Host "4. Testing POST $baseUrl/resources/$firstResourceId/start"
    $startLearningResult = Invoke-RestMethod -Uri "$baseUrl/resources/$firstResourceId/start" -Method POST -ContentType "application/json"
    Write-Host "Status code: $($startLearningResult.status)"
    Write-Host "Response:"
    $startLearningResult | ConvertTo-Json
    Write-Host ""
} else {
    Write-Host "No learning resources found, cannot test resource detail and start learning APIs"
}

Write-Host "=== Testing Complete ==="