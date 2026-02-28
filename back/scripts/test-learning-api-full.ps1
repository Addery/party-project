# PowerShell script to test learning resource APIs with authentication

$baseUrl = "http://localhost:8080/api"
$username = "testuser"
$password = "123456"
$name = "Test User"
$email = "test@example.com"
$phone = "13800138000"

Write-Host "=== Testing Learning Resource APIs with Authentication ==="
Write-Host ""

# 1. Register a new user
Write-Host "1. Register a new user POST $baseUrl/auth/register"
$registerBody = @{
    username = $username
    password = $password
    name = $name
    gender = "male"
    age = 25
    partyStatus = "active"
    branch = "Test Branch"
    phone = $phone
    email = $email
    joinDate = "2023-01-01"
    position = "Ordinary Party Member"
    education = "Bachelor"
    workYears = 3
    avatar = ""
    role = "user"
} | ConvertTo-Json

$registerResult = Invoke-RestMethod -Uri "$baseUrl/auth/register" -Method POST -ContentType "application/json" -Body $registerBody
Write-Host "Status code: $($registerResult.status)"
Write-Host "Response content:"
$registerResult | ConvertTo-Json
Write-Host ""

# 2. User login
Write-Host "2. User login POST $baseUrl/auth/login"
$loginBody = @{
    username = $username
    password = $password
} | ConvertTo-Json

$loginResult = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -ContentType "application/json" -Body $loginBody
Write-Host "Status code: $($loginResult.status)"
Write-Host "Response content:"
$loginResult | ConvertTo-Json
Write-Host ""

# 3. Extract JWT token
$jwtToken = $loginResult.data.token
Write-Host "Extracted JWT token: $jwtToken"
Write-Host ""

# 4. Test GET learning categories (with authentication)
Write-Host "3. Testing GET learning categories GET $baseUrl/learning/categories"
$categoriesResult = Invoke-RestMethod -Uri "$baseUrl/learning/categories" -Method GET -ContentType "application/json" -Headers @{Authorization = "Bearer $jwtToken"}
Write-Host "Status code: $($categoriesResult.status)"
Write-Host "Response content:"
$categoriesResult | ConvertTo-Json
Write-Host ""

# 5. Test GET learning resources (with authentication)
Write-Host "4. Testing GET learning resources GET $baseUrl/learning/resources"
$resourcesResult = Invoke-RestMethod -Uri "$baseUrl/learning/resources" -Method GET -ContentType "application/json" -Headers @{Authorization = "Bearer $jwtToken"}
Write-Host "Status code: $($resourcesResult.status)"
Write-Host "Response content:"
$resourcesResult | ConvertTo-Json
Write-Host ""

# 6. Test GET resource details (with authentication)
if ($resourcesResult.status -eq 200 -and $resourcesResult.data -and $resourcesResult.data.list -and $resourcesResult.data.list.Count -gt 0) {
    $firstResourceId = $resourcesResult.data.list[0].id
    
    Write-Host "5. Testing GET resource details GET $baseUrl/learning/resources/$firstResourceId"
    $resourceDetailResult = Invoke-RestMethod -Uri "$baseUrl/learning/resources/$firstResourceId" -Method GET -ContentType "application/json" -Headers @{Authorization = "Bearer $jwtToken"}
    Write-Host "Status code: $($resourceDetailResult.status)"
    Write-Host "Response content:"
    $resourceDetailResult | ConvertTo-Json
    Write-Host ""
    
    # 7. Test start learning resource (with authentication)
    Write-Host "6. Testing start learning resource POST $baseUrl/learning/resources/$firstResourceId/start"
    $startLearningResult = Invoke-RestMethod -Uri "$baseUrl/learning/resources/$firstResourceId/start" -Method POST -ContentType "application/json" -Headers @{Authorization = "Bearer $jwtToken"}
    Write-Host "Status code: $($startLearningResult.status)"
    Write-Host "Response content:"
    $startLearningResult | ConvertTo-Json
    Write-Host ""
} else {
    Write-Host "No learning resources found, cannot test resource detail and start learning APIs"
}

Write-Host "=== Testing Complete ==="