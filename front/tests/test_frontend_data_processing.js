// 模拟前端的新闻数据处理逻辑
function processNewsData(response) {
  console.log('原始响应数据:', response)
  console.log('\n尝试从以下字段获取新闻数据：content, news, list, newsList, data')
  
  // 模拟前端的新闻数据处理逻辑
  const newsData = response.data.content || response.data.news || response.data.list || response.data.newsList || response.data.data
  
  console.log('最终获取的新闻数据:', newsData)
  console.log('新闻数据类型:', Array.isArray(newsData) ? '数组' : typeof newsData)
  
  if (!Array.isArray(newsData)) {
    // 如果newsData不是数组，尝试检查response.data本身是否是数组
    if (Array.isArray(response.data)) {
      console.log('发现response.data本身是数组，使用它作为新闻数据')
      return response.data.map(news => ({
        id: news.id,
        title: news.title,
        date: news.publishDate || news.date,
        category: news.category,
        summary: news.description || news.summary,
        author: news.author,
        image: news.coverImage || news.cover || news.imageUrl || ''
      }))
    } else {
      console.error('获取新闻列表失败：所有可能的新闻数据字段都不是数组', response.data)
      return []
    }
  }
  
  // 映射API返回数据到组件需要的格式
  return newsData.map(news => ({
    id: news.id,
    title: news.title,
    date: news.publishDate || news.date,
    category: news.category,
    summary: news.description || news.summary,
    author: news.author,
    image: news.coverImage || news.cover || news.imageUrl || ''
  }))
}

// 测试数据1：包含newsList字段
const testData1 = {
  code: 200,
  message: "获取成功",
  data: {
    newsList: [
      {
        id: 1,
        title: "测试新闻1",
        content: "测试内容1",
        description: "测试摘要1",
        category: "technology",
        author: "测试作者",
        publishDate: "2025年10月28日",
        coverImage: "https://picsum.photos/id/1/800/400"
      }
    ],
    total: 1,
    pageSize: 5,
    page: 1
  }
}

// 测试数据2：包含list字段
const testData2 = {
  code: 200,
  message: "获取成功",
  data: {
    list: [
      {
        id: 2,
        title: "测试新闻2",
        content: "测试内容2",
        summary: "测试摘要2",
        category: "policy",
        author: "测试作者",
        date: "2025年10月29日",
        cover: "https://picsum.photos/id/2/800/400"
      }
    ],
    total: 1,
    pageSize: 5,
    page: 1
  }
}

// 测试数据3：包含content字段
const testData3 = {
  code: 200,
  message: "获取成功",
  data: {
    content: [
      {
        id: 3,
        title: "测试新闻3",
        content: "测试内容3",
        description: "测试摘要3",
        category: "economy",
        author: "测试作者",
        publishDate: "2025年10月30日",
        imageUrl: "https://picsum.photos/id/3/800/400"
      }
    ],
    total: 1,
    pageSize: 5,
    page: 1
  }
}

// 测试数据4：直接包含数据数组
const testData4 = {
  code: 200,
  message: "获取成功",
  data: [
    {
      id: 4,
      title: "测试新闻4",
      content: "测试内容4",
      summary: "测试摘要4",
      category: "health",
      author: "测试作者",
      date: "2025年10月31日",
      coverImage: "https://picsum.photos/id/4/800/400"
    }
  ]
}

// 运行测试
console.log('=== 测试前端新闻数据处理逻辑 ===')

console.log('\n1. 测试数据1（包含newsList字段）:')
const result1 = processNewsData(testData1)
console.log('处理后的数据:', result1)

console.log('\n2. 测试数据2（包含list字段）:')
const result2 = processNewsData(testData2)
console.log('处理后的数据:', result2)

console.log('\n3. 测试数据3（包含content字段）:')
const result3 = processNewsData(testData3)
console.log('处理后的数据:', result3)

console.log('\n4. 测试数据4（直接包含数据数组）:')
const result4 = processNewsData(testData4)
console.log('处理后的数据:', result4)

console.log('\n=== 测试完成 ===')
