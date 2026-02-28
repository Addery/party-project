import axios from 'axios'

// 创建axios实例
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 测试technology分类接口
async function testTechnologyCategory() {
  try {
    console.log('=== 测试technology分类接口 ===')
    
    // 调用technology分类接口
    const response = await apiClient.get('/news', {
      params: {
        category: 'technology',
        page: 1,
        pageSize: 10
      }
    })
    
    console.log('API状态:', response.status)
    console.log('API响应:', JSON.stringify(response.data, null, 2))
    
    // 检查返回的新闻数据
    const newsData = response.data.data?.newsList || response.data.data?.content || response.data.data?.list || response.data.data?.news || response.data.newsList || response.data.content || response.data.list || response.data.news || []
    
    console.log('\n解析到的新闻数据:', newsData)
    console.log('新闻数量:', Array.isArray(newsData) ? newsData.length : 0)
    
    if (Array.isArray(newsData) && newsData.length > 0) {
      console.log('\n第一条新闻详情:')
      console.log('标题:', newsData[0].title)
      console.log('分类:', newsData[0].category)
      console.log('发布日期:', newsData[0].publishDate || newsData[0].date)
      console.log('作者:', newsData[0].author)
      console.log('描述:', newsData[0].description || newsData[0].summary)
    }
    
    console.log('\n=== 测试完成 ===')
    
  } catch (error) {
    console.error('测试失败:', error.message)
    if (error.response) {
      console.error('响应状态:', error.response.status)
      console.error('响应数据:', error.response.data)
    }
  }
}

// 运行测试
testTechnologyCategory()
