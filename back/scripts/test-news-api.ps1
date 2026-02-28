# 测试新闻相关接口的PowerShell脚本

$baseUrl = "http://localhost:8080/api"

# 测试函数
function Test-API {
    param(
        [string]$url,
        [string]$method = "GET",
        [string]$description
    )
    
    Write-Host "\n=== 测试 $description ==="
    Write-Host "URL: $url"
    Write-Host "Method: $method"
    
    try {
        $response = Invoke-RestMethod -Uri $url -Method $method -ContentType "application/json"
        Write-Host "状态码: 200"
        Write-Host "响应:"
        $response | ConvertTo-Json -Depth 5
        return $true
    } catch {
        Write-Host "错误: $($_.Exception.Message)"
        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode.value__
            Write-Host "状态码: $statusCode"
            try {
                $errorResponse = Invoke-RestMethod -Uri $url -Method $method -ContentType "application/json" -ErrorAction SilentlyContinue
                Write-Host "错误响应: $($errorResponse | ConvertTo-Json)"
            } catch {
                Write-Host "无法获取详细错误信息"
            }
        }
        return $false
    }
}

# 测试NewsController接口
Write-Host "\n""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
测试 NewsController 接口
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

# 1. 获取新闻列表（分页）
Test-API -url "$baseUrl/news?page=1`&size=5" -description "获取新闻列表（分页）"

# 2. 获取新闻详情（使用ID为1的新闻）
Test-API -url "$baseUrl/news/1" -description "获取新闻详情"

# 3. 获取头条新闻
Test-API -url "$baseUrl/news/headlines?limit=3" -description "获取头条新闻"

# 4. 获取相关新闻
Test-API -url "$baseUrl/news/related?category=头条新闻`&excludeId=1" -description "获取相关新闻"

# 测试HomeController接口
Write-Host "\n""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
测试 HomeController 接口
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

# 4. 获取首页推荐内容（包含新闻）
Test-API -url "$baseUrl/home/recommended" -description "获取首页推荐内容"

# 5. 获取首页头条新闻
Test-API -url "$baseUrl/home/headline-news?limit=3" -description "获取首页头条新闻"

Write-Host "\n=== 测试完成 ==="