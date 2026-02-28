// 模拟前端fetchNewsList函数的完整逻辑
function simulateFetchNewsList(response) {
  console.log('=== 模拟前端fetchNewsList函数处理逻辑 ===')
  console.log('原始响应数据:', JSON.stringify(response, null, 2))
  console.log('\n响应data字段:', JSON.stringify(response.data, null, 2))
  
  // 检查API返回的数据格式是否正确
  if (!response) {
    console.error('获取新闻列表失败：API返回为空')
    return { success: false, error: '获取新闻列表失败，请稍后重试' }
  }
  
  if (response.code !== 200) {
    console.error('获取新闻列表失败：API返回错误码', response.code, '消息:', response.message)
    return { success: false, error: response.message || '获取新闻列表失败，请稍后重试' }
  }
  
  if (!response.data) {
    console.error('获取新闻列表失败：API返回data为空')
    return { success: false, error: '获取新闻列表失败，请稍后重试' }
  }
  
  // 新的数据提取逻辑：增加对response.data.data.newsList等字段的支持
  console.log('\n尝试从以下字段获取新闻数据：data.newsList, data.content, data.news, data.list, data.data, newsList, content, news, list, data')
  const newsData = response.data.data?.newsList || response.data.data?.content || response.data.data?.news || response.data.data?.list || response.data.data?.data || response.data.newsList || response.data.content || response.data.news || response.data.list || response.data.data
  
  console.log('\n最终获取的新闻数据:', JSON.stringify(newsData, null, 2))
  console.log('新闻数据类型:', Array.isArray(newsData) ? '数组' : typeof newsData)
  
  if (!Array.isArray(newsData)) {
    // 如果newsData不是数组，尝试检查response.data本身是否是数组
    if (Array.isArray(response.data)) {
      console.log('\n发现response.data本身是数组，使用它作为新闻数据')
      const mappedNews = response.data.map(news => ({
        id: news.id,
        title: news.title,
        date: news.publishDate || news.date,
        category: news.category,
        summary: news.description || news.summary,
        author: news.author,
        image: news.coverImage || news.cover || news.imageUrl || ''
      }))
      
      // 尝试从response或response.data中获取分页信息
      const total = response.total || response.data.total || response.data.totalCount || 0
      const currentPage = response.page || response.data.page || 1
      const pageSize = response.pageSize || response.data.pageSize || 10
      
      console.log('\n映射后的新闻数据:', JSON.stringify(mappedNews, null, 2))
      console.log('分页信息:', { total, currentPage, pageSize })
      
      return { success: true, newsList: mappedNews, total, currentPage, pageSize }
    } else {
      console.error('\n获取新闻列表失败：所有可能的新闻数据字段都不是数组', response.data)
      return { success: false, error: '获取新闻列表失败，请稍后重试' }
    }
  }
  
  // 映射API返回数据到组件需要的格式
  const mappedNews = newsData.map(news => ({
    id: news.id,
    title: news.title,
    date: news.publishDate || news.date,
    category: news.category,
    summary: news.description || news.summary,
    author: news.author,
    image: news.coverImage || news.cover || news.imageUrl || ''
  }))
  
  // 更新分页信息
  const total = response.data.total || response.data.totalCount || 0
  const currentPage = response.data.page || 1
  const pageSize = response.data.pageSize || 10
  
  console.log('\n映射后的新闻数据:', JSON.stringify(mappedNews, null, 2))
  console.log('分页信息:', { total, currentPage, pageSize })
  
  return { success: true, newsList: mappedNews, total, currentPage, pageSize }
}

// 模拟后端返回的真实数据结构
const mockResponse = {
  "code": 200,
  "message": "获取成功",
  "data": {
    "newsList": [
      {
        "id": 42,
        "title": "嫦娥七号完成首次月球背面采样任务",
        "content": "嫦娥七号探测器在月球背面成功着陆并完成采样任务，标志着我国探月工程取得重大突破。",
        "description": "嫦娥七号探测器在月球背面成功着陆并完成采样任务，标志着我国探月工程取得重大突破。",
        "category": "technology",
        "author": "人民日报",
        "publishDate": "2025年12月10日",
        "coverImage": "https://picsum.photos/id/113/800/400",
        "viewsCount": null,
        "createdAt": null,
        "updatedAt": null,
        "status": "published",
        "isTop": true,
        "sortOrder": 14
      },
      {
        "id": 44,
        "title": "我国高温超导悬浮列车研制成功",
        "content": "我国自主研制的高温超导悬浮列车在试验中达到620公里每小时的速度，打破世界纪录。",
        "description": "我国高温超导悬浮列车研制成功，我国轨道交通技术领先世界。",
        "category": "technology",
        "author": "科技日报",
        "publishDate": "2025年11月05日",
        "coverImage": "https://picsum.photos/id/115/800/400",
        "viewsCount": null,
        "createdAt": null,
        "updatedAt": null,
        "status": "published",
        "isTop": true,
        "sortOrder": 15
      }
    ],
    "total": 7,
    "pageSize": 5,
    "page": 1
  }
}

// 运行模拟测试
console.log('开始测试前端修复后的逻辑...\n')
const result = simulateFetchNewsList(mockResponse)

console.log('\n=== 测试结果 ===')
if (result.success) {
  console.log('成功获取新闻列表!')
  console.log('新闻数量:', result.newsList.length)
  console.log('总页数:', Math.ceil(result.total / result.pageSize))
} else {
  console.log('获取新闻列表失败:', result.error)
}

console.log('\n=== 测试完成 ===')
