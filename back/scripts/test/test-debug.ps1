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
        Write-Host "Response type: $($data.GetType().Name)"
        Write-Host "Response properties: $($data.PSObject.Properties.Name -join ', ')"
        $data | ConvertTo-Json -Depth 5
    }
}

# 1. Get resources list
Show-Result "1. Get resources list"
try {
    $resources = Invoke-RestMethod -Uri "$baseUrl/resources?page=1&size=3&sortBy=updateTime&sortOrder=desc" -Method Get
    Show-Result "Resources list retrieved successfully" $resources
    
    # Debug: Check if content exists and what type it is
    if ($resources.PSObject.Properties.Name -contains "content") {
        Write-Host "Content property exists"
        Write-Host "Content type: $($resources.content.GetType().Name)"
        if ($resources.content -is [array] -and $resources.content.Count -gt 0) {
            $resourceId = $resources.content[0].id
            Write-Host "Selected resource ID: $resourceId"
        } else {
            Write-Host "Content is not an array or is empty"
            exit 1
        }
    } else {
        Write-Host "Content property does not exist"
        Write-Host "All properties: $($resources.PSObject.Properties.Name -join ', ')"
        exit 1
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    exit 1
}

# 2. Get resource detail
Show-Result "2. Get resource detail"
try {
    $detail = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId" -Method Get
    Show-Result "Resource detail retrieved successfully" $detail
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# 3. Toggle like
Show-Result "3. Toggle like"
try {
    $likeResult = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/like?userId=$userId" -Method Post
    Show-Result "Like toggled successfully" $likeResult
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

Show-Result "Basic tests completed!"