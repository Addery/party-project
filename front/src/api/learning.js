import axios from 'axios'

// 创建axios实例，使用环境变量中的API基础URL
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000
})

const learningApi = {
  // 获取学习资源分类列表
  getResourceCategories: async () => {
    try {
      // 真实API调用
      const response = await service.get('/learning/categories')
      return response.data
    } catch (error) {
      console.error('获取学习资源分类列表失败:', error)
      throw error
    }
  },

  // 获取学习资源列表
  getLearningResources: async (params = {}) => {
    try {
      // 真实API调用
      const response = await service.get('/learning/resources', { params })
      return response.data
    } catch (error) {
      console.error('获取学习资源列表失败:', error)
      throw error
    }
  },

  // 获取学习资源详情
  getResourceDetail: async (id) => {
    try {
      // 真实API调用
      const response = await service.get(`/learning/resources/${id}`)
      return response.data
    } catch (error) {
      console.error('获取学习资源详情失败:', error)
      throw error
    }
  },

  // 获取相关资源
  getRelatedResources: async (id, count = 5) => {
    try {
      // 真实API调用
      const response = await service.get(`/learning/resources/${id}/related`, { params: { count } })
      return response.data
    } catch (error) {
      console.error('获取相关资源失败:', error)
      throw error
    }
  },

  // 检查收藏状态
  checkFavorite: async (id) => {
    try {
      // 真实API调用
      const response = await service.get(`/learning/resources/${id}/favorite`)
      return response.data
    } catch (error) {
      console.error('检查收藏状态失败:', error)
      throw error
    }
  },

  // 添加收藏
  addFavorite: async (id) => {
    try {
      // 真实API调用
      const response = await service.post(`/learning/resources/${id}/favorite`)
      return response.data
    } catch (error) {
      console.error('添加收藏失败:', error)
      throw error
    }
  },

  // 取消收藏
  removeFavorite: async (id) => {
    try {
      // 真实API调用
      const response = await service.delete(`/learning/resources/${id}/favorite`)
      return response.data
    } catch (error) {
      console.error('取消收藏失败:', error)
      throw error
    }
  },

  // 开始学习资源
  startResource: async (id) => {
    try {
      // 真实API调用
      const response = await service.post(`/learning/resources/${id}/start`)
      return response.data
    } catch (error) {
      console.error('开始学习资源失败:', error)
      throw error
    }
  }
}

export default learningApi