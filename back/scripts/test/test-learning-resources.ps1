# 学习资源接口测试脚本
$baseUrl = "http://localhost:8080"
$userId = 1  # 测试用户ID

# 函数：输出结果
function Output-Result {
    param(
        [string]$message,
        [object]$data = $null
    )
    Write-Host "`n=== $message ==="
    if ($data) {
        $data | ConvertTo-Json -Depth 10
    }
}

# 1. 获取资源列表
Output-Result "1. 获取资源列表"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources?page=1&size=5&sortBy=updateTime&sortOrder=desc" -Method Get
    Output-Result "资源列表获取成功" $response
    $resourceId = $response.records[0].id  # 获取第一个资源ID用于后续测试
} catch {
    Write-Host "错误：$($_.Exception.Message)"
    exit 1
}

# 2. 获取资源详情
Output-Result "2. 获取资源详情"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId" -Method Get
    Output-Result "资源详情获取成功" $response
} catch {
    Write-Host "错误：$($_.Exception.Message)"
}

# 3. 点赞资源
Output-Result "3. 点赞资源"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/like?userId=$userId" -Method Post
    Output-Result "点赞操作成功" $response
} catch {
    Write-Host "错误：$($_.Exception.Message)"
}

# 4. 添加评论
Output-Result "4. 添加评论"
try {
    $commentData = @{
        userId = $userId
        content = "这是一条测试评论"
        parentId = $null
    }
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/comment" -Method Post -Body ($commentData | ConvertTo-Json) -ContentType "application/json"
    Output-Result "评论添加成功" $response
    $commentId = $response.id  # 获取评论ID用于回复测试
} catch {
    Write-Host "错误：$($_.Exception.Message)"
}

# 5. 回复评论
if ($commentId) {
    Output-Result "5. 回复评论"
    try {
        $replyData = @{
            userId = $userId
            content = "这是一条回复评论"
            parentId = $commentId
        }
        $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/comment" -Method Post -Body ($replyData | ConvertTo-Json) -ContentType "application/json"
        Output-Result "回复评论成功" $response
    } catch {
        Write-Host "错误：$($_.Exception.Message)"
    }
}

# 6. 获取评论列表
Output-Result "6. 获取评论列表"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/comment?page=1&size=10&sortBy=commentTime&sortOrder=desc" -Method Get
    Output-Result "评论列表获取成功" $response
} catch {
    Write-Host "错误：$($_.Exception.Message)"
}

# 7. 分享资源
Output-Result "7. 分享资源"
try {
    $shareData = @{
        userId = $userId
        sharePlatform = "WeChat"
        shareLink = "http://example.com/share/$resourceId"
    }
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/share" -Method Post -Body ($shareData | ConvertTo-Json) -ContentType "application/json"
    Output-Result "分享操作成功" $response
} catch {
    Write-Host "错误：$($_.Exception.Message)"
}

# 8. 再次获取资源详情，查看点赞、评论和分享数量是否更新
Output-Result "8. 再次获取资源详情，检查数据更新"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId" -Method Get
    Output-Result "资源详情获取成功" $response
} catch {
    Write-Host "错误：$($_.Exception.Message)"
}

# 9. 取消点赞
Output-Result "9. 取消点赞"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId/like?userId=$userId" -Method Post
    Output-Result "取消点赞操作成功" $response
} catch {
    Write-Host "错误：$($_.Exception.Message)"
}

# 10. 再次获取资源详情，确认点赞已取消
Output-Result "10. 再次获取资源详情，确认点赞已取消"
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/resources/$resourceId" -Method Get
    Output-Result "资源详情获取成功" $response
} catch {
    Write-Host "错误：$($_.Exception.Message)"
}

Output-Result "测试完成！"