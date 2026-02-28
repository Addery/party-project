import axios from 'axios'

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL, // 使用环境变量中的API基础URL
  timeout: 15000 // 请求超时时间
})

// respone拦截器
// service.interceptors.response.use(
//   response => response,
//   /**
//    * 下面的注释为通过response自定义code来标示请求状态，当code返回如下情况为权限有问题，登出并返回到登录页
//    * 如通过xmlhttprequest 状态码标识 逻辑可写在下面error中
//    */
//   error => {
//     console.log('err' + error)// for debug
//     return Promise.reject(error)
//   }
// )

export default {
  // 1. 获取就业统计数据
  getEmploymentStats() {
    return service.get('/employment/stats')
      .then(response => response.data)
      .catch(error => {
        console.error('获取就业统计数据失败:', error)
        throw error
      })
  },

  // 2. 获取老师指导学生情况
  getTeacherGuidance(params) {
    return service.get('/employment/graduate', { params })
      .then(response => response.data)
      .catch(error => {
        console.error('获取老师指导学生情况失败:', error)
        throw error
      })
  },

  // 3. 获取就业资讯列表
  getEmploymentNews(params) {
    return service.get('/employment/news', { params })
      .then(response => response.data)
      .catch(error => {
        console.error('获取就业资讯列表失败:', error)
        throw error
      })
  },

  // 4. 获取就业资讯详情
  getEmploymentDetail(id) {
    return service.get(`/employment/detail/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error('获取就业资讯详情失败:', error)
        throw error
      })
  },

  // 5. 获取企业招聘信息列表
  getCompanyRecruitment(params) {
    return service.get('/employment/recruitment', { params })
      .then(response => response.data)
      .catch(error => {
        console.error('获取企业招聘信息列表失败:', error)
        throw error
      })
  },

  // 6. 获取就业指导资源列表
  getEmploymentGuidance(params) {
    return service.get('/employment/guidance', { params })
      .then(response => response.data)
      .catch(error => {
        console.error('获取就业指导资源列表失败:', error)
        throw error
      })
  },

  // 7. 获取就业政策列表
  getEmploymentPolicies(params) {
    return service.get('/employment/policies', { params })
      .then(response => response.data)
      .catch(error => {
        console.error('获取就业政策列表失败:', error)
        throw error
      })
  },
  
  // 8. 获取就业政策详情
  getEmploymentPolicyDetail(id) {
    return service.get(`/employment/policies/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error('获取就业政策详情失败:', error)
        throw error
      })
  },

  // 9. 获取企业招聘详情
  getEmploymentCompanyRecruitmentDetail(id) {
    return service.get(`/employment/recruitment/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error('获取企业招聘详情失败:', error)
        throw error
      })
  },

  // 10. 获取就业指导详情
  getEmploymentGuidanceDetail(id) {
    return service.get(`/employment/guidance/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error('获取就业指导详情失败:', error)
        throw error
      })
  },

  // 10. 获取就业服务列表
  getEmploymentServices() {
    return service.get('/employment/services')
      .then(response => response.data)
      .catch(error => {
        console.error('获取就业服务列表失败:', error)
        throw error
      })
  }
}