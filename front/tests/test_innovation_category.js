import axios from 'axios'

// 创建axios实例
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // 使用.env文件中配置的API地址
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 测试科技创新分类接口
async function testInnovationCategory() {
  try {
    console.log('=== 测试科技创新分类接口 ===')
    console.log('API基础URL:', process.env.VITE_API_BASE_URL)
    
    // 测试获取分类列表
    console.log('\n1. 获取新闻分类列表:')
    const categoriesResponse = await apiClient.get('/news/categories')
    console.log('分类列表API状态:', categoriesResponse.status)
    console.log('分类列表API响应:', JSON.stringify(categoriesResponse.data, null, 2))
    
    // 检查是否包含科技创新分类
    if (Array.isArray(categoriesResponse.data)) {
      const hasInnovation = categoriesResponse.data.some(category => 
        category === 'innovation' || category.name === 'innovation'
      )
      console.log('分类列表是否包含innovation:', hasInnovation)
    }
    
    // 测试获取科技创新分类的新闻
    console.log('\n2. 获取科技创新分类的新闻:')
    const newsResponse = await apiClient.get('/news', {
      params: {
        category: 'innovation',
        page: 1,
        pageSize: 10
      }
    })
    
    console.log('新闻列表API状态:', newsResponse.status)
    console.log('新闻列表API响应:', JSON.stringify(newsResponse.data, null, 2))
    
    // 验证返回的新闻是否为科技创新分类
    if (newsResponse.data.content || newsResponse.data.list || Array.isArray(newsResponse.data)) {
      const newsData = newsResponse.data.content || newsResponse.data.list || newsResponse.data
      console.log('\n3. 返回的新闻数据:')
      console.log('新闻总数:', Array.isArray(newsData) ? newsData.length : '未知')
      
      if (Array.isArray(newsData)) {
        newsData.forEach((news, index) => {
          console.log(`\n新闻${index + 1}:`)
          console.log('标题:', news.title)
          console.log('分类:', news.category)
          console.log('日期:', news.date)
        })
        
        // 检查是否所有新闻都是科技创新分类
        const allInnovation = newsData.every(news => news.category === 'innovation')
        console.log('\n所有返回的新闻是否都为innovation分类:', allInnovation)
      }
    }
    
    console.log('\n=== 科技创新分类接口测试完成 ===')
    
  } catch (error) {
    console.error('测试失败:', error.message)
    if (error.response) {
      console.error('响应状态:', error.response.status)
      console.error('响应数据:', JSON.stringify(error.response.data, null, 2))
    }
  }
}

// 运行测试
testInnovationCategory()