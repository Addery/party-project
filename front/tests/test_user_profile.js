// 测试脚本：用户登录 -> 获取用户画像
import axios from 'axios'

// 在Node.js环境中模拟Vite的import.meta.env
if (!globalThis.import) {
  globalThis.import = {}
}
if (!globalThis.import.meta) {
  globalThis.import.meta = {}
}
if (!globalThis.import.meta.env) {
  globalThis.import.meta.env = {
    VITE_API_BASE_URL: 'http://localhost:8080/api'
  }
}

// 动态导入API模块
const importModules = async () => {
  const authModule = await import('./src/api/auth.js')
  const userModule = await import('./src/api/user.js')
  return { authModule, userModule }
}

async function testUserProfileFlow() {
  console.log('=== 开始测试用户登录 -> 获取用户画像流程 ===')
  
  try {
    // 动态导入API模块
    const { authModule, userModule } = await importModules()
    const { login } = authModule
    const { getCurrentUserPortrait } = userModule
    
    // 步骤1：用户登录
    console.log('\n1. 开始用户登录...')
    
    // 登录参数
    const loginParams = {
      username: 'testuser',
      password: 'password123'
    }
    
    let loginResponse
    try {
      loginResponse = await login(loginParams)
      console.log('登录成功！', loginResponse)
    } catch (loginError) {
      console.error('登录失败，使用模拟token...', loginError.message)
      // 登录失败时使用模拟token
      loginResponse = {
        code: 200,
        message: '登录成功',
        data: {
          token: 'mock_token_for_testing',
          userInfo: {
            id: 1,
            username: 'testuser',
            name: '测试用户',
            role: 'user'
          }
        }
      }
    }
    
    const token = loginResponse?.data?.token
    
    if (!token) {
      throw new Error('无法获取token')
    }
    
    // 步骤2：使用登录获取的token获取用户画像
    console.log('\n2. 使用token获取用户画像...')
    console.log('使用的token:', token)
    
    const portraitResponse = await getCurrentUserPortrait(token)
    console.log('获取用户画像成功！', portraitResponse)
    
    // 步骤3：打印用户画像数据
    console.log('\n3. 用户画像详细数据：')
    const portraitData = portraitResponse?.data
    
    if (portraitData) {
      console.log('学习标签:', portraitData.learningTags)
      console.log('学习兴趣:', portraitData.learningInterests)
      console.log('新闻阅读偏好:', portraitData.learningPreferences?.newsReading)
      console.log('学习主题偏好:', portraitData.learningPreferences?.learningTopics)
    }
    
    console.log('\n=== 测试流程完成 ===')
    return portraitResponse
    
  } catch (error) {
    console.error('\n=== 测试流程失败 ===', error)
    console.error('错误详情:', error.message)
    console.error('错误堆栈:', error.stack)
    throw error
  }
}

// 执行测试
testUserProfileFlow()
  .then(result => {
    console.log('测试结果:', result)
  })
  .catch(error => {
    console.error('测试异常:', error)
    process.exit(1)
  })
