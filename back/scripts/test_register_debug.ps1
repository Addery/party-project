# 测试活动报名接口 - 详细调试版

# 设置测试参数
$activityId = 13
$userId = 1
$userName = "测试用户"
$userPhone = "13800138000"

# 构建请求URL
$url = "http://localhost:8080/api/activities/$activityId/register"

# 构建请求体
$requestBody = @{
    userId = $userId
    name = $userName
    phone = $userPhone
}

# 转换为JSON并显示
$jsonBody = $requestBody | ConvertTo-Json
Write-Host "请求URL: $url"
Write-Host "请求体: $jsonBody"
Write-Host "请求方式: POST"
Write-Host "Content-Type: application/json"

# 发送请求并显示详细信息
try {
    $response = Invoke-RestMethod -Method POST -Uri $url -Body $jsonBody -ContentType "application/json" -Verbose
    Write-Host "\n请求成功！"
    Write-Host "响应状态码: 200"
    Write-Host "响应内容:"
    $response | ConvertTo-Json -Depth 5
} catch {
    Write-Host "\n请求失败！"
    Write-Host "错误信息: $($_.Exception.Message)"
    if ($_.ErrorDetails) {
        Write-Host "响应内容: $($_.ErrorDetails.Message)"
    }
    if ($_.Exception.Response) {
        $response = $_.Exception.Response
        $statusCode = [int]$response.StatusCode
        Write-Host "响应状态码: $statusCode"
        
        # 读取响应流
        $reader = New-Object System.IO.StreamReader($response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "响应体: $responseBody"
    }
}