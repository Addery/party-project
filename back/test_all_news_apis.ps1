# 测试所有新闻相关接口
$baseUrl = "http://localhost:8080/api/news"

Write-Host "开始测试新闻相关接口..."
Write-Host "=" * 50

# 1. 测试获取新闻分类接口（已测试过，但再次验证）
Write-Host "1. 测试获取新闻分类接口: GET $baseUrl/categories"
Try {
    $response = Invoke-RestMethod -Uri "$baseUrl/categories" -Method GET
    Write-Host "   状态码: 200"
    Write-Host "   结果: $($response.data -join ', ')"
    Write-Host "   测试通过 ✓"
} Catch {
    Write-Host "   错误: $($_.Exception.Message)"
    Write-Host "   测试失败 ✗"
}

Write-Host "-" * 50

# 2. 测试获取新闻列表接口
Write-Host "2. 测试获取新闻列表接口: GET $baseUrl?page=1&size=5"
Try {
    $response = Invoke-RestMethod -Uri '$baseUrl?page=1&size=5' -Method GET
    Write-Host "   状态码: 200"
    Write-Host "   总页数: $($response.data.totalPages)"
    Write-Host "   总条数: $($response.data.totalElements)"
    Write-Host "   当前页数据条数: $($response.data.content.Count)"
    Write-Host "   测试通过 ✓"
} Catch {
    Write-Host "   错误: $($_.Exception.Message)"
    Write-Host "   测试失败 ✗"
}

Write-Host "-" * 50

# 3. 测试获取头条新闻接口（复数形式）
Write-Host "3. 测试获取头条新闻接口: GET $baseUrl/headlines?limit=3"
Try {
    $response = Invoke-RestMethod -Uri '$baseUrl/headlines?limit=3' -Method GET
    Write-Host "   状态码: 200"
    Write-Host "   头条数量: $($response.data.Count)"
    Write-Host "   测试通过 ✓"
} Catch {
    Write-Host "   错误: $($_.Exception.Message)"
    Write-Host "   测试失败 ✗"
}

Write-Host "-" * 50

# 4. 测试获取头条新闻接口（单数形式，兼容性测试）
Write-Host "4. 测试获取头条新闻接口（单数形式）: GET $baseUrl/headline?limit=2"
Try {
    $response = Invoke-RestMethod -Uri '$baseUrl/headline?limit=2' -Method GET
    Write-Host "   状态码: 200"
    Write-Host "   头条数量: $($response.data.Count)"
    Write-Host "   测试通过 ✓"
} Catch {
    Write-Host "   错误: $($_.Exception.Message)"
    Write-Host "   测试失败 ✗"
}

Write-Host "-" * 50

# 5. 测试获取新闻详情接口（先获取一个新闻ID）
Write-Host "5. 测试获取新闻详情接口: GET $baseUrl/{id}"
Try {
    # 先获取新闻列表以获取一个有效的ID
    $newsList = Invoke-RestMethod -Uri '$baseUrl?page=1&size=1' -Method GET
    if ($newsList.data.content.Count -gt 0) {
        $newsId = $newsList.data.content[0].id
        $response = Invoke-RestMethod -Uri "$baseUrl/$newsId" -Method GET
        Write-Host "   状态码: 200"
        Write-Host "   新闻标题: $($response.data.title)"
        Write-Host "   新闻分类: $($response.data.category)"
        Write-Host "   测试通过 ✓"
    } else {
        Write-Host "   警告: 没有找到新闻数据，无法测试详情接口"
        Write-Host "   测试跳过 ⚠️"
    }
} Catch {
    Write-Host "   错误: $($_.Exception.Message)"
    Write-Host "   测试失败 ✗"
}

Write-Host "-" * 50

# 6. 测试获取相关新闻接口（需要一个有效的分类和新闻ID）
Write-Host "6. 测试获取相关新闻接口: GET $baseUrl/related"
Try {
    # 先获取新闻列表以获取一个有效的ID和分类
    $newsList = Invoke-RestMethod -Uri '$baseUrl?page=1&size=1' -Method GET
    if ($newsList.data.content.Count -gt 0) {
        $news = $newsList.data.content[0]
        $newsId = $news.id
        $category = $news.category
        
        $uri = '$baseUrl/related?category=' + $category + '&excludeId=' + $newsId
        $response = Invoke-RestMethod -Uri $uri -Method GET
        Write-Host "   状态码: 200"
        Write-Host "   相关新闻数量: $($response.data.Count)"
        Write-Host "   测试通过 ✓"
    } else {
        Write-Host "   警告: 没有找到新闻数据，无法测试相关新闻接口"
        Write-Host "   测试跳过 ⚠️"
    }
} Catch {
    Write-Host "   错误: $($_.Exception.Message)"
    Write-Host "   测试失败 ✗"
}

Write-Host "=" * 50
Write-Host "所有接口测试完成！"