import axios from 'axios'

// 创建axios实例，使用环境变量中的API基础URL
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000
})

const noticeApi = {
  // 获取首页最新通知公告
  getHomeNoticesList: async (params = {}) => {
    try {
      const response = await service.get('/notices/home', { params })
      return response.data
    } catch (error) {
      console.error('获取首页通知公告失败:', error)
      throw error
    }
  },

  // 获取分页通知公告列表
  getNoticeList: async (params = {}) => {
    try {
      const response = await service.get('/notices', { params })
      return response.data
    } catch (error) {
      console.error('获取通知公告列表失败:', error)
      throw error
    }
  },

  // 获取通知公告详情
  getNoticeDetail: async (id) => {
    try {
      const response = await service.get(`/notices/${id}`)
      return response.data
    } catch (error) {
      console.error('获取通知公告详情失败:', error)
      throw error
    }
  },

  // 获取公告分类
  getNoticeCategories: async () => {
    try {
      const response = await service.get('/notices/categories')
      return response.data
    } catch (error) {
      console.error('获取公告分类失败:', error)
      throw error
    }
  }
}

export default noticeApi