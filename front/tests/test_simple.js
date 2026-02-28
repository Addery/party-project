// 简化测试脚本：不依赖Vite环境
import axios from 'axios'

// 简化的processPortraitData函数，直接在测试脚本中实现
const processPortraitData = (portraitData) => {
  if (!portraitData) return {
    learningTags: [],
    learningInterests: [],
    learningPreferences: { newsReading: {}, learningTopics: {} }
  }
  
  // 适配API返回的数据结构
  // API返回: personalityLabels, preferenceLabels
  // 前端期望: learningTags, learningInterests
  const processed = {
    ...portraitData,
    learningTags: Array.isArray(portraitData.personalityLabels) ? portraitData.personalityLabels : [],
    learningInterests: Array.isArray(portraitData.preferenceLabels) ? portraitData.preferenceLabels : [],
    learningPreferences: portraitData.preferences || { newsReading: {}, learningTopics: {} }
  }
  
  // 确保学习偏好结构正确
  if (processed.learningPreferences && !processed.learningPreferences.newsReading && processed.learningPreferences.newsCategories) {
    // API返回的是newsCategories，前端期望的是newsReading
    processed.learningPreferences.newsReading = processed.learningPreferences.newsCategories
  }
  
  return processed
}

// 模拟API响应格式
const mockLoginResponse = {
  token: 'fake-jwt-token-Test123',
  user: {
    id: 1,
    username: 'testuser',
    name: '测试用户'
  }
}

const mockPortraitResponse = {
  userId: 1,
  userName: '测试用户',
  learningTags: ['政治理论', '党史学习', '科技创新'],
  learningInterests: ['人工智能', '大数据', '乡村振兴'],
  learningPreferences: ['视频课程', '在线阅读', '互动讨论'],
  learningTimeDistribution: {
    week: 120,
    month: 480,
    quarter: 1440
  },
  learningAchievements: {
    coursesCompleted: 15,
    examsPassed: 8,
    pointsEarned: 1200
  }
}

// 主测试函数
async function testUserProfileFlow() {
  try {
    console.log('=== 用户登录->获取画像测试流程 ===')
    
    // 步骤1：用户登录
    console.log('\n1. 用户登录...')
    let loginResult
    
    try {
      // 尝试真实登录
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username: 'Addery',
        password: '123456'
      })
      loginResult = response.data // 根据auth.js，直接返回data
      console.log('真实登录成功！登录数据:', JSON.stringify(loginResult, null, 2))
    } catch (loginError) {
      console.error('真实登录失败，使用模拟数据...', loginError.message)
      loginResult = mockLoginResponse
    }
    
    // 从正确的路径获取token
    const token = loginResult?.data?.token || loginResult?.token || loginResult?.access_token
    
    console.log('尝试获取token:', token)
    
    if (!token) {
      console.log('登录数据完整结构:', JSON.stringify(loginResult, null, 2))
      throw new Error('无法获取token')
    }
    
    console.log('登录成功获取token:', token)
    
    // 步骤2：获取用户画像
    console.log('\n2. 获取用户画像...')
    let portraitResult
    
    let realPortraitData = null
    let isUsingMockData = false
    
    try {
      // 尝试真实获取画像（使用正确的API路径）
      console.log('API调用: GET http://localhost:8080/api/user/portrait/current')
      console.log('请求头:', JSON.stringify({Authorization: `Bearer ${token}`}, null, 2))
      

      
      const response = await axios.get('http://localhost:8080/api/user/portrait/current', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      
      console.log('API响应状态:', response.status)
      console.log('API响应头:', JSON.stringify(response.headers, null, 2))
      
      realPortraitData = response.data // 保存真实数据
      portraitResult = realPortraitData
      console.log('真实获取画像成功！')
      
      // 展示真实API返回的完整结果
      console.log('真实API返回完整数据:', JSON.stringify(realPortraitData, null, 2))
      
      // 详细检查真实数据是否有效
      console.log('\n3. 数据完整性检查:')
      console.log('realPortraitData:', JSON.stringify(realPortraitData, null, 2))
      
      // 确定实际的画像数据位置
      // API返回结构: response.data { code, message, data }，真正的画像数据在response.data.data中
      const actualPortrait = realPortraitData.data || realPortraitData.portrait || realPortraitData
      console.log('actualPortrait:', JSON.stringify(actualPortrait, null, 2))
      
      // 使用processPortraitData处理API返回的数据
      const processed = processPortraitData(actualPortrait)
      console.log('processedPortrait:', JSON.stringify(processed, null, 2))
      
      // 检查画像相关字段
      const hasValidPortrait = processed.learningTags?.length > 0 || processed.learningInterests?.length > 0 || processed.learningPreferences?.newsCategories || processed.learningPreferences?.newsReading
      
      console.log('字段检查结果:')
      console.log('processed.learningTags:', processed.learningTags)
      console.log('processed.learningInterests:', processed.learningInterests)
      console.log('processed.learningPreferences:', processed.learningPreferences)
      console.log('hasValidPortrait:', hasValidPortrait)
      
      // 如果所有画像相关字段都缺失，使用模拟数据
      if (!hasValidPortrait) {
        console.log('真实画像数据结构不完整，使用模拟数据...')
        isUsingMockData = true
        portraitResult = mockPortraitResponse
      } else {
        // 使用处理后的数据
        portraitResult = processed
      }
    } catch (portraitError) {
      console.error('真实获取画像失败，使用模拟数据...', portraitError.message)
      console.error('错误详情:', JSON.stringify(portraitError.response?.data || portraitError.message, null, 2))
      if (portraitError.response) {
        console.error('错误状态码:', portraitError.response.status)
        console.error('错误响应头:', JSON.stringify(portraitError.response.headers, null, 2))
      }
      isUsingMockData = true
      portraitResult = mockPortraitResponse
    }
    
    // 步骤3：打印画像数据
    console.log('\n3. 用户画像数据:')
    console.log('数据来源:', isUsingMockData ? '模拟数据' : '真实API数据')
    
    // 打印真实数据（如果有）
    if (realPortraitData && isUsingMockData) {
      console.log('\n真实API返回的数据内容:')
      console.log(JSON.stringify(realPortraitData, null, 2))
    }
    
    console.log('\n最终使用的画像数据:')
    console.log('用户ID:', portraitResult.userId || '未提供')
    console.log('用户名称:', portraitResult.userName || '未提供')
    console.log('学习标签:', portraitResult.learningTags || [])
    console.log('学习兴趣:', portraitResult.learningInterests || [])
    console.log('学习偏好:', portraitResult.learningPreferences || [])
    console.log('学习时长分布:', portraitResult.learningTimeDistribution || '未提供')
    console.log('学习成就:', portraitResult.learningAchievements || '未提供')
    
    // 如果有其他字段，也一并展示
    const otherFields = Object.keys(portraitResult).filter(
      key => !['userId', 'userName', 'learningTags', 'learningInterests', 'learningPreferences', 'learningTimeDistribution', 'learningAchievements'].includes(key)
    )
    
    if (otherFields.length > 0) {
      console.log('\n其他画像字段:')
      otherFields.forEach(field => {
        console.log(`${field}:`, portraitResult[field])
      })
    }
    
    console.log('\n=== 测试流程完成 ===')
    return portraitResult
    
  } catch (error) {
    console.error('\n=== 测试流程失败 ===')
    console.error('错误详情:', error.message)
    console.error('错误堆栈:', error.stack)
    throw error
  }
}

// 运行测试
testUserProfileFlow().catch(error => {
  console.error('测试异常:', error)
})
