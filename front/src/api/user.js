import axios from 'axios'
import { useUserStore } from '@/stores/user'

// 创建axios实例
const userApi = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL + '/user', // 使用环境变量配置API地址
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    'Accept': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器：添加token
userApi.interceptors.request.use(
  config => {
    console.log('请求拦截器原始配置:', config)
    const token = useUserStore().token
    console.log('请求拦截器获取的token:', token)
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
      console.log('请求拦截器添加Authorization头:', config.headers.Authorization)
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 获取当前登录用户的画像
export const getCurrentUserPortrait = async (token = null) => {
  try {
    // 如果提供了token，使用自定义配置
    const config = token ? {
      headers: {
        Authorization: `Bearer ${token}`
      }
    } : {}
    
    console.log('getCurrentUserPortrait开始请求用户画像数据');
    const response = await userApi.get('/portrait/current', config)
    console.log('getCurrentUserPortrait获取到的完整响应:', JSON.stringify(response, null, 2));
    return response // 返回完整的响应对象，而不是只返回data
  } catch (error) {
    console.error('获取用户画像失败:', error)
    throw error
  }
}

// 根据用户ID获取用户画像
export const getUserPortraitById = async (userId, token = null) => {
  try {
    // 如果提供了token，使用自定义配置
    const config = token ? {
      headers: {
        Authorization: `Bearer ${token}`
      }
    } : {}
    
    const response = await userApi.get(`/portrait/${userId}`, config)
    return response.data
  } catch (error) {
    console.error(`获取用户ID为${userId}的画像失败:`, error)
    throw error
  }
}

// 手动更新当前登录用户的画像
export const updateCurrentUserPortrait = async (token = null) => {
  try {
    // 如果提供了token，使用自定义配置
    const config = token ? {
      headers: {
        Authorization: `Bearer ${token}`
      }
    } : {}
    
    const response = await userApi.post('/portrait/update', {}, config)
    return response.data
  } catch (error) {
    console.error('更新用户画像失败:', error)
    throw error
  }
}

// 处理API返回的用户画像数据（适配数据结构）
export const processPortraitData = (portraitData) => {
  if (!portraitData) return {
    learningTags: [],
    learningInterests: [],
    personalityTraits: [],
    learningPreferences: { newsReading: {}, learningTopics: {} }
  }
  
  console.log('src/api/user.js中的processPortraitData输入数据:', JSON.stringify(portraitData, null, 2))
  
  // 适配API返回的数据结构到前端期望的结构
  const processed = {
    ...portraitData,
    // 将API返回的preferenceLabels映射到前端使用的learningTags
    learningTags: portraitData.preferenceLabels || [],
    learningInterests: [],
    // 将API返回的personalityLabels映射到前端使用的personalityTraits
    personalityTraits: portraitData.personalityLabels || [],
    // 将API返回的preferences映射到前端使用的learningPreferences
    learningPreferences: {
      // API返回的preferences.newsCategories映射到learningPreferences.newsReading
      newsReading: portraitData.preferences?.newsCategories || {},
      // API返回的preferences.learningTopics映射到learningPreferences.learningTopics
      learningTopics: portraitData.preferences?.learningTopics || {}
    }
  }
  
  return processed
}
