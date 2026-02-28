# 测试认证与用户管理接口脚本

# 服务器地址
$baseUrl = "http://localhost:8080"

# 测试用用户名和密码
$testUsername = "testuser_$(Get-Random -Minimum 1000 -Maximum 9999)"
$testPassword = "TestPass123!"
$testName = "Test User"
$updatedName = "Updated Test User"

# 存储token用于后续请求
$token = $null
$userId = $null

Write-Host "=== Testing Authentication and User Management APIs ==="
Write-Host ""

# Test 1: User Registration
Write-Host "1. Testing User Registration API..."
try {
    $registerBody = @{
        username = $testUsername
        password = $testPassword
        name = $testName
        gender = "男"
        age = 30
        partyStatus = "入党积极分子"
        branch = "测试党支部"
        phone = "13800138000"
        email = "$testUsername@example.com"
        joinDate = "2023-01-01"
        position = "测试职位"
        education = "本科"
        workYears = 5
        avatar = ""
        role = "member"
    }
    
    $registerResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/register" -Method Post -Body ($registerBody | ConvertTo-Json) -ContentType "application/json; charset=utf-8"
    
    if ($registerResponse.code -eq 200) {
        Write-Host "   ✓ Registration successful!"
        Write-Host "   User ID: $($registerResponse.data.id)"
        Write-Host "   Username: $($registerResponse.data.username)"
        Write-Host "   Name: $($registerResponse.data.name)"
        $userId = $registerResponse.data.id
    } else {
        Write-Host "   ✗ Registration failed: $($registerResponse.message)"
        exit 1
    }
} catch {
    Write-Host "   ✗ Registration request failed: $($_.Exception.Message)"
    exit 1
}

Write-Host ""

# Test 2: User Login
Write-Host "2. Testing User Login API..."
try {
    $loginBody = @{
        username = $testUsername
        password = $testPassword
    }
    
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body ($loginBody | ConvertTo-Json) -ContentType "application/json; charset=utf-8"
    
    if ($loginResponse.code -eq 200) {
        Write-Host "   ✓ Login successful!"
        Write-Host "   Token: $($loginResponse.data.token)"
        Write-Host "   User ID: $($loginResponse.data.user.id)"
        Write-Host "   Name: $($loginResponse.data.user.name)"
        $token = $loginResponse.data.token
    } else {
        Write-Host "   ✗ Login failed: $($loginResponse.message)"
        exit 1
    }
} catch {
    Write-Host "   ✗ Login request failed: $($_.Exception.Message)"
    exit 1
}

Write-Host ""

# Test 3: Get User Profile
Write-Host "3. Testing Get User Profile API..."
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    $profileResponse = Invoke-RestMethod -Uri "$baseUrl/api/user/profile" -Method Get -Headers $headers -ContentType "application/json; charset=utf-8"
    
    if ($profileResponse.code -eq 200) {
        Write-Host "   ✓ Get user profile successful!"
        Write-Host "   User ID: $($profileResponse.data.id)"
        Write-Host "   Username: $($profileResponse.data.username)"
        Write-Host "   Name: $($profileResponse.data.name)"
        Write-Host "   Gender: $($profileResponse.data.gender)"
        Write-Host "   Age: $($profileResponse.data.age)"
        Write-Host "   Party Status: $($profileResponse.data.partyStatus)"
        Write-Host "   Branch: $($profileResponse.data.branch)"
        Write-Host "   Phone: $($profileResponse.data.phone)"
        Write-Host "   Email: $($profileResponse.data.email)"
        Write-Host "   Join Date: $($profileResponse.data.joinDate)"
        Write-Host "   Position: $($profileResponse.data.position)"
        Write-Host "   Education: $($profileResponse.data.education)"
        Write-Host "   Work Years: $($profileResponse.data.workYears)"
        Write-Host "   Role: $($profileResponse.data.role)"
        Write-Host "   Status: $($profileResponse.data.status)"
    } else {
        Write-Host "   ✗ Get user profile failed: $($profileResponse.message)"
        exit 1
    }
} catch {
    Write-Host "   ✗ Get user profile request failed: $($_.Exception.Message)"
    exit 1
}

Write-Host ""

# Test 4: Update User Profile
Write-Host "4. Testing Update User Profile API..."
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    $updateBody = @{
        name = $updatedName
        age = 31
        phone = "13900139000"
        position = "更新后的测试职位"
    }
    
    $updateResponse = Invoke-RestMethod -Uri "$baseUrl/api/user/profile" -Method Put -Headers $headers -Body ($updateBody | ConvertTo-Json) -ContentType "application/json; charset=utf-8"
    
    if ($updateResponse.code -eq 200) {
        Write-Host "   ✓ Update user profile successful!"
        Write-Host "   Updated Name: $($updateResponse.data.name)"
        Write-Host "   Updated Age: $($updateResponse.data.age)"
        Write-Host "   Updated Phone: $($updateResponse.data.phone)"
        Write-Host "   Updated Position: $($updateResponse.data.position)"
    } else {
        Write-Host "   ✗ Update user profile failed: $($updateResponse.message)"
        exit 1
    }
} catch {
    Write-Host "   ✗ Update user profile request failed: $($_.Exception.Message)"
    exit 1
}

Write-Host ""
Write-Host "=== All API Tests Completed! ==="