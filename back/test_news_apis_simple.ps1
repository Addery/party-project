# 简单的新闻接口测试脚本
$baseUrl = "http://localhost:8080/api/news"

Write-Host "开始测试新闻相关接口..."
Write-Host "=" * 50

# 函数：测试单个GET接口
function Test-GetApi($name, $url) {
    Write-Host "$name: GET $url"
    try {
        $response = Invoke-RestMethod -Uri $url -Method GET
        Write-Host "   ✅ 成功，状态码: 200"
        if ($response.data -is [array]) {
            Write-Host "   返回数据条数: $($response.data.Length)"
        } elseif ($response.data -is [pscustomobject]) {
            Write-Host "   返回对象类型: $($response.data.GetType().Name)"
        }
        return $response
    } catch {
        Write-Host "   ❌ 失败: $($_.Exception.Message)"
        return $null
    }
}

# 1. 测试获取新闻分类接口
Test-GetApi "1. 获取新闻分类" "$baseUrl/categories"

Write-Host "-" * 50

# 2. 测试获取新闻列表接口
$newsList = Test-GetApi "2. 获取新闻列表" "$baseUrl?page=1&size=5"

Write-Host "-" * 50

# 3. 测试获取头条新闻接口（复数形式）
Test-GetApi "3. 获取头条新闻" "$baseUrl/headlines?limit=3"

Write-Host "-" * 50

# 4. 测试获取头条新闻接口（单数形式）
Test-GetApi "4. 获取头条新闻（单数）" "$baseUrl/headline?limit=2"

Write-Host "-" * 50

# 5. 测试获取新闻详情接口
if ($newsList -and $newsList.data.content.Length -gt 0) {
    $newsId = $newsList.data.content[0].id
    Test-GetApi "5. 获取新闻详情" "$baseUrl/$newsId"
} else {
    Write-Host "5. 获取新闻详情: 跳过（没有新闻数据）"
}

Write-Host "-" * 50

# 6. 测试获取相关新闻接口
if ($newsList -and $newsList.data.content.Length -gt 0) {
    $news = $newsList.data.content[0]
    $newsId = $news.id
    $category = $news.category
    $relatedUrl = "$baseUrl/related?category=$category&excludeId=$newsId"
    Test-GetApi "6. 获取相关新闻" $relatedUrl
} else {
    Write-Host "6. 获取相关新闻: 跳过（没有新闻数据）"
}

Write-Host "=" * 50
Write-Host "所有接口测试完成！"