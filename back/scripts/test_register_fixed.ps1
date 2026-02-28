# 测试活动报名接口 - PowerShell正确格式

$url = "http://localhost:8080/api/activities/13/register"
$body = @{
    userId = 1
    name = "test user"
    phone = "13800138000"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri $url -Body $body -ContentType "application/json"