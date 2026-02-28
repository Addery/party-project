import axios from 'axios'

// 创建axios实例，使用环境变量中的API基础URL
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000
})

const newsApi = {
  // 获取头条新闻列表
  getHeadlineNews: async (params = {}) => {
    try {
      const response = await service.get('/home/headline-news', { params })
      return response.data
    } catch (error) {
      console.error('获取头条新闻失败:', error)
      throw error
    }
  },

  // 获取新闻详情
  getNewsDetail: async (id) => {
    try {
      const response = await service.get(`/news/${id}`)
      return response.data
    } catch (error) {
      console.error('获取新闻详情失败:', error)
      throw error
    }
  },

  // 获取新闻列表（通用接口）
  getNewsList: async (params = {}) => {
    try {
      const response = await service.get('/news', { params })
      return response.data
    } catch (error) {
      console.error('获取新闻列表失败:', error)
      throw error
    }
  },

  // 获取相关新闻
  getRelatedNews: async (category, excludeId) => {
    try {
      const response = await service.get('/news/related', {
        params: { category, excludeId }
      })
      return response.data
    } catch (error) {
      console.error('获取相关新闻失败:', error)
      throw error
    }
  },

  // 获取新闻分类
  getNewsCategories: async () => {
    try {
      const response = await service.get('/news/categories')
      return response.data
    } catch (error) {
      console.error('获取新闻分类失败:', error)
      throw error
    }
  }
}

export default newsApi
