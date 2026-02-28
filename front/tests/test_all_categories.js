import axios from 'axios'

// 创建axios实例
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 测试所有新闻分类
async function testAllCategories() {
  try {
    console.log('=== 测试所有新闻分类筛选功能 ===')
    
    // 获取分类列表
    console.log('\n1. 获取所有新闻分类:')
    const categoriesResponse = await apiClient.get('/news/categories')
    console.log('分类列表API状态:', categoriesResponse.status)
    console.log('分类列表:', JSON.stringify(categoriesResponse.data, null, 2))
    
    // 测试每个分类
    const categories = [
      'policy',
      'party_building', 
      'organization',
      'technology',
      'party_discipline',
      'grassroots_party',
      'law',
      'economy',
      'health',
      'society',
      'environment',
      'education',
      'culture',
      'sports',
      'safety'
    ]
    
    for (const category of categories) {
      console.log(`\n=== 测试分类: ${category} ===`)
      
      try {
        const newsResponse = await apiClient.get('/news', {
          params: {
            category: category,
            page: 1,
            pageSize: 5
          }
        })
        
        console.log(`分类 ${category} API状态:`, newsResponse.status)
        console.log(`分类 ${category} API响应:`, JSON.stringify(newsResponse.data, null, 2))
        
        // 检查返回的新闻是否正确
        const newsData = newsResponse.data.data.content || newsResponse.data.data.news || newsResponse.data.data.list || newsResponse.data.data.newsList || []
        console.log(`分类 ${category} 返回的新闻数量:`, Array.isArray(newsData) ? newsData.length : 0)
        
        if (Array.isArray(newsData) && newsData.length > 0) {
          // 检查第一条新闻的分类是否正确
          const firstNews = newsData[0]
          console.log(`第一条新闻的分类:`, firstNews.category)
        }
        
      } catch (error) {
        console.error(`测试分类 ${category} 失败:`, error.message)
        if (error.response) {
          console.error(`响应状态:`, error.response.status)
          console.error(`响应数据:`, error.response.data)
        }
      }
    }
    
    console.log('\n=== 所有分类测试完成 ===')
    
  } catch (error) {
    console.error('测试失败:', error.message)
    if (error.response) {
      console.error('响应状态:', error.response.status)
      console.error('响应数据:', error.response.data)
    }
  }
}

// 运行测试
testAllCategories()