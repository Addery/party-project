# Test learning resource APIs
Write-Host "Testing learning resource APIs..."

# Test 1: Get categories
Write-Host "\n1. Testing GET /api/learning/categories"
try {
    $categoriesResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/learning/categories" -Method GET -ContentType "application/json"
    Write-Host "Status Code: $($categoriesResponse.GetType().Name)"  # For RESTful APIs, Invoke-RestMethod returns content directly
    Write-Host "Response: $($categoriesResponse | ConvertTo-Json -Depth 5)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# Test 2: Get resources
Write-Host "\n2. Testing GET /api/learning/resources"
try {
    $resourcesResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/learning/resources?page=1&pageSize=10" -Method GET -ContentType "application/json"
    Write-Host "Status Code: $($resourcesResponse.GetType().Name)"
    Write-Host "Response: $($resourcesResponse | ConvertTo-Json -Depth 5)"
    
    # Extract resource ID for next test if available
    if ($resourcesResponse.data.resources -and $resourcesResponse.data.resources.Count -gt 0) {
        $resourceId = $resourcesResponse.data.resources[0].id
        Write-Host "\nFound resource ID: $resourceId"
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# Test 3: Get resource by ID (if we found an ID)
if ($resourceId) {
    Write-Host "\n3. Testing GET /api/learning/resources/$resourceId"
    try {
        $resourceDetailResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/learning/resources/$resourceId" -Method GET -ContentType "application/json"
        Write-Host "Status Code: $($resourceDetailResponse.GetType().Name)"
        Write-Host "Response: $($resourceDetailResponse | ConvertTo-Json -Depth 5)"
    } catch {
        Write-Host "Error: $($_.Exception.Message)"
    }
    
    # Test 4: Start learning resource
    Write-Host "\n4. Testing POST /api/learning/resources/$resourceId/start"
    try {
        $startLearningResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/learning/resources/$resourceId/start" -Method POST -ContentType "application/json"
        Write-Host "Status Code: $($startLearningResponse.GetType().Name)"
        Write-Host "Response: $($startLearningResponse | ConvertTo-Json -Depth 5)"
    } catch {
        Write-Host "Error: $($_.Exception.Message)"
    }
} else {
    Write-Host "\nNo resource ID found for further tests."
}

Write-Host "\nAPI testing completed."