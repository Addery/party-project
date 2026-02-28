function Test-API {
    param (
        [string]$url,
        [string]$method = "GET",
        [string]$name
    )
    
    Write-Host "Testing ${name}: $url" -ForegroundColor Cyan
    
    try {
        $response = Invoke-RestMethod -Uri $url -Method $method -ContentType "application/json"
        Write-Host "Status: Success (200)" -ForegroundColor Green
        Write-Host "Response: $($response | ConvertTo-Json -Depth 3)" -ForegroundColor Yellow
        Write-Host ""
        return $true
    } catch {
        Write-Host "Status: Failed ($($_.Exception.Response.StatusCode.Value__))" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host ""
        return $false
    }
}

# 设置基础URL
$baseUrl = "http://localhost:8080/api"

# 测试/api/notices/home接口
Test-API -url "$baseUrl/notices/home?limit=5" -name "首页通知公告接口"