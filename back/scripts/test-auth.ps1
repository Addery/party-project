# 测试注册功能
Write-Host "测试用户注册功能..."
$registerData = @{
    username = "testuser"
    password = "password123"
    name = "Test User"
    gender = "MALE"
    partyStatus = "PARTICIPANT"
}
$registerJson = $registerData | ConvertTo-Json
$registerResult = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method Post -Body $registerJson -ContentType "application/json"
Write-Host "注册结果:" -ForegroundColor Green
Write-Host $registerResult

# 测试登录功能
Write-Host "\n测试用户登录功能..."
$loginData = @{
    username = "testuser"
    password = "password123"
}
$loginJson = $loginData | ConvertTo-Json
$loginResult = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body $loginJson -ContentType "application/json"
Write-Host "登录结果:" -ForegroundColor Green
Write-Host $loginResult