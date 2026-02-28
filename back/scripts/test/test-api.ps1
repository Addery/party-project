# Test script for learning resources API
$baseUrl = "http://localhost:8080/api/learning"
$userId = 1

# Function to display result
function Show-Result {
    param(
        [string]$message,
        [object]$data = $null
    )
    Write-Host "`n=== $message ==="
    if ($data) {
        $data | ConvertTo-Json -Depth 5
    }
}

# 1. Get resources list
Show-Result "1. Get resources list"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources?page=1&pageSize=3" -Method Get
    Show-Result "Resources list retrieved successfully" $response
    
    $resources = $response.data
    if ($resources.resources -and $resources.resources.Count -gt 0) {
        $resourceId = $resources.resources[0].id
        Write-Host "Selected resource ID: $resourceId"
    } else {
        Write-Host "No resources found"
        exit 1
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    exit 1
}

# 2. Get resource detail
Show-Result "2. Get resource detail"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId" -Method Get
    Show-Result "Resource detail retrieved successfully" $response
    $resourceDetail = $response.data
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# 3. Toggle like
Show-Result "3. Toggle like"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/like?userId=$userId" -Method Post
    Show-Result "Like toggled successfully" $response
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# 4. Add comment
Show-Result "4. Add comment"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/comment?content=This+is+a+test+comment" -Method Post
    Show-Result "Comment added successfully" $response
    $commentId = $response.data
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# 5. Get comments
Show-Result "5. Get comments"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/comment?page=1&pageSize=10" -Method Get
    Show-Result "Comments retrieved successfully" $response
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# 6. Share resource
Show-Result "6. Share resource"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/share?sharePlatform=WeChat" -Method Post
    Show-Result "Resource shared successfully" $response
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# 7. Get updated resource detail
Show-Result "7. Get updated resource detail"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId" -Method Get
    Show-Result "Updated resource detail retrieved successfully" $response
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

Show-Result "All tests completed!"