# 测试用户注册接口 - 包含中文name字段

$url = "http://localhost:8080/api/auth/register"
$body = @{
    username = "testuser_chinese2"
    password = "Password123!"
    name = "测试用户2"
    phone = "13800138002"
    email = "test_chinese2@example.com"
    branch = "测试党支部"
    position = "测试职位"
}

# 转换为JSON格式
$jsonBody = $body | ConvertTo-Json
Write-Host "请求体JSON: $jsonBody"

# 发送POST请求
try {
    $response = Invoke-RestMethod -Method POST -Uri $url -Body $jsonBody -ContentType "application/json; charset=utf-8"
    Write-Host "\n注册请求成功!"
    Write-Host "响应内容:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 5
} catch {
    Write-Host "\n注册请求失败:" -ForegroundColor Red
    Write-Host "错误信息: $($_.Exception.Message)"
    if ($_.ErrorDetails) {
        Write-Host "响应内容: $($_.ErrorDetails.Message)"
    }
}

