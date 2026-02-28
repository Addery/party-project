# 测试活动报名接口
# 设置请求URL
$url = "http://localhost:8080/api/activities/13/register"

# 设置请求体
$requestBody = @{
    userId = 1
    name = "test user"
    phone = "13800138000"
}

# 转换为JSON格式
$jsonBody = $requestBody | ConvertTo-Json

# 发送POST请求
try {
    $response = Invoke-RestMethod -Method POST -Uri $url -Body $jsonBody -ContentType "application/json"
    Write-Host "Request successful!"
    Write-Host "Response content:"
    $response | ConvertTo-Json -Depth 5
} catch {
    Write-Host "Request failed:"
    Write-Host "Error message: $($_.Exception.Message)"
    if ($_.ErrorDetails) {
        Write-Host "Response content: $($_.ErrorDetails.Message)"
    }
}