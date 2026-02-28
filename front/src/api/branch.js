import axios from 'axios'

// 创建axios实例，使用环境变量中的API基础URL
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000
})

// 样板支部相关API调用函数
const branchApi = {
  // 获取样板支部列表
  getModelBranches: async (params = {}) => {
    try {
      const response = await service.get('/branches/model', { params })
      return response.data
    } catch (error) {
      console.error('获取样板支部列表失败:', error)
      throw error
    }
  },

  // 获取样板支部详情
  getModelBranchDetail: async (id) => {
    try {
      const response = await service.get(`/branches/model/${id}`)
      return response.data
    } catch (error) {
      console.error('获取样板支部详情失败:', error)
      throw error
    }
  }
}

export default branchApi
