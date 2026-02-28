import axios from 'axios'
import { useUserStore } from '../stores/user' // 导入user store

// 创建axios实例
const authApi = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL + '/auth', // 使用环境变量配置API地址
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    'Accept': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
authApi.interceptors.request.use(
  config => {
    // 从user store获取token
    const userStore = useUserStore()
    const token = userStore.token
    // 如果token存在，则在请求头中添加Authorization字段
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 登录接口
export const login = async (username, password) => {
  try {
    const response = await authApi.post('/login', {
      username,
      password
    })
    return response.data
  } catch (error) {
    console.error('登录失败:', error)
    throw error
  }
}

// 注册接口
export const register = async (registerData) => {
  try {
    const response = await authApi.post('/register', registerData)
    return response.data
  } catch (error) {
    console.error('注册失败:', error)
    throw error
  }
}
