<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { login } from '../../api/auth'

const router = useRouter()
const userStore = useUserStore()

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

// 错误信息
const errorMsg = ref('')
// 加载状态
const loading = ref(false)



// 登录函数
const handleLogin = async () => {
  // 表单验证
  if (!loginForm.username || !loginForm.password) {
    errorMsg.value = '请输入用户名和密码'
    return
  }

  loading.value = true
  errorMsg.value = ''

  try {
    // 调用后端登录接口
    const response = await login(loginForm.username, loginForm.password)
    
    // 添加调试信息
    console.log('登录响应数据:', response)
    
    if (response) {
      console.log('登录响应结构:', JSON.stringify(response))
      
      // 检查token是否存在（可能在不同的字段名中）
      const token = response.token || response.data?.token || response.accessToken || response.data?.accessToken
      const userInfo = response.user || response.data?.user
      
      console.log('提取的token:', token)
      console.log('提取的用户信息:', userInfo)
      
      if (token && userInfo) {

        
        // 登录成功，保存用户信息和token
        userStore.login(userInfo, token)
        
        // 所有用户登录后都跳转到首页
        console.log('准备跳转到首页')
        // 使用nextTick确保状态更新后再跳转
        setTimeout(() => {
          router.push('/')
          console.log('执行跳转')
        }, 100)
      } else {
        // 登录失败
        errorMsg.value = response?.message || response?.data?.message || '登录失败，请检查用户名和密码'
        console.log('登录失败原因:', response?.message || response?.data?.message)
      }
    } else {
      errorMsg.value = '登录失败，未收到有效响应'
      console.log('未收到有效响应')
    }
  } catch (error) {
    // 处理错误
    console.error('登录错误:', error)
    
    if (error.response) {
      // 服务器返回了错误响应
      errorMsg.value = error.response.data?.message || '登录失败，请稍后重试'
    } else if (error.request) {
      // 请求已发送但没有收到响应
      errorMsg.value = '服务器无响应，请检查网络连接'
    } else {
      // 请求配置错误
      errorMsg.value = '登录请求失败，请检查用户名和密码'
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <!-- 左侧党建主题区域 -->
    <div class="login-left">
      <div class="party-banner">
        <div class="banner-content">
          <div class="party-logo-large">
            <img src="/src/assets/images/dangwei.png" alt="党徽" width="80" height="80">
          </div>
          <h1 class="banner-title">红芯软炬党建教育平台</h1>
          <div class="banner-subtitle">加强党员教育，提升管理水平</div>
          <div class="party-quotes">
            <div class="quote-item">
              <i class="quote-icon">“</i>
              <p class="quote-text">不忘初心，牢记使命</p>
            </div>
            <div class="quote-item">
              <i class="quote-icon">“</i>
              <p class="quote-text">为人民服务</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧登录表单区域 -->
    <div class="login-right">
      <div class="login-form-wrapper">
        <form class="login-form" @submit.prevent="handleLogin">
          <!-- 表单标题 -->
          <div class="form-header">
            <img src="@/assets/images/logo_touming.png" alt="红芯云课堂" class="login-logo">
            <h2>用户登录</h2>
            <p>欢迎登录红芯软炬党建教育平台</p>
          </div>
          
          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-user"></i>
              <input
                type="text"
                id="username"
                v-model="loginForm.username"
                class="form-control"
                placeholder="请输入用户名"
                autocomplete="off"
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-password"></i>
              <input
                type="password"
                id="password"
                v-model="loginForm.password"
                class="form-control"
                placeholder="请输入密码"
              />
            </div>
          </div>

          <div class="form-group remember-me">
            <input
              type="checkbox"
              id="rememberMe"
              v-model="loginForm.rememberMe"
              class="remember-checkbox"
            />
            <label for="rememberMe" class="remember-label">记住我</label>
          </div>

          <div v-if="errorMsg" class="error-message">
            {{ errorMsg }}
          </div>

          <button
            type="submit"
            class="login-btn party-btn"
            :disabled="loading"
          >
            <span v-if="loading" class="loading-spinner"></span>
            <span v-else>登录</span>
          </button>



          <!-- 没有账号？去注册 -->
          <div class="register-link">
            没有账号？
            <router-link to="/register" class="link-btn">去注册</router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 全局登录容器 */
.login-container {
  min-height: 100vh;
  display: flex;
  position: relative;
  overflow: hidden;
}

/* 左侧党建主题区域 */
.login-left {
  flex: 1;
  background: linear-gradient(135deg, #d93025 0%, #b71c1c 50%, #8b0000 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

/* 党建背景装饰 */
.login-left::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -20%;
  width: 80%;
  height: 200%;
  background: rgba(255, 255, 255, 0.05);
  transform: rotate(30deg);
}

.login-left::after {
  content: '';
  position: absolute;
  bottom: -30%;
  left: -10%;
  width: 60%;
  height: 120%;
  background: rgba(255, 255, 255, 0.03);
  transform: rotate(-20deg);
}

/* 党建横幅内容 */
.party-banner {
  width: 100%;
  max-width: 500px;
  padding: 40px;
  color: white;
  position: relative;
  z-index: 1;
  animation: fadeInLeft 0.8s ease-out;
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 党徽展示 */
.party-logo-large {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100px;
  height: 100px;
  background-color: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  border: 3px solid rgba(255, 255, 255, 0.3);
  margin: 0 auto 30px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.party-logo-large img {
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

/* 横幅标题 */
.banner-title {
  font-size: 36px;
  font-weight: bold;
  text-align: center;
  margin: 0 0 10px 0;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  line-height: 1.3;
}

.banner-subtitle {
  font-size: 18px;
  text-align: center;
  margin: 0 0 40px 0;
  opacity: 0.95;
  font-weight: 300;
}

/* 党建语录 */
.party-quotes {
  display: flex;
  flex-direction: column;
  gap: 25px;
  margin-top: 50px;
}

.quote-item {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.quote-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.quote-icon {
  font-size: 32px;
  color: rgba(255, 255, 255, 0.5);
  line-height: 1;
  margin-top: -5px;
}

.quote-text {
  font-size: 18px;
  font-weight: 500;
  margin: 0;
  line-height: 1.6;
}

/* 右侧登录表单区域 */
.login-right {
  flex: 1;
  background-color: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-form-wrapper {
  width: 100%;
  max-width: 420px;
  animation: fadeInRight 0.8s ease-out;
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 登录表单 */
.login-form {
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 15px 45px rgba(0, 0, 0, 0.1);
  padding: 50px;
  width: 100%;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.login-form:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 55px rgba(0, 0, 0, 0.15);
}

/* 表单标题 */
.form-header {
  text-align: center;
  margin-bottom: 40px;
  padding-bottom: 25px;
  border-bottom: 2px solid #f0f0f0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.login-logo {
  width: 60px;
  height: auto;
}

.form-header h2 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 28px;
  font-weight: 700;
  letter-spacing: -0.5px;
}

.form-header p {
  margin: 0;
  color: #666;
  font-size: 15px;
  line-height: 1.5;
}

/* 表单样式 */
.form-group {
  margin-bottom: 28px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-wrapper i {
  position: absolute;
  left: 18px;
  color: #999;
  font-size: 18px;
  transition: color 0.3s ease;
}

/* 输入框样式 */
.form-control {
  width: 100%;
  padding: 16px 18px 16px 50px;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  font-size: 16px;
  font-family: inherit;
  transition: all 0.3s ease;
  background-color: #fafafa;
}

.form-control:focus {
  outline: none;
  border-color: #d93025;
  box-shadow: 0 0 0 4px rgba(217, 48, 37, 0.1);
  background-color: white;
}

.form-control:focus + i {
  color: #d93025;
}

/* 输入框图标样式 */
.icon-user::before {
  content: "👤";
}

.icon-password::before {
  content: "🔒";
}

/* 记住我 */
.remember-me {
  display: flex;
  align-items: center;
  margin-bottom: 28px;
}

.remember-checkbox {
  margin-right: 10px;
  width: 18px;
  height: 18px;
  cursor: pointer;
  accent-color: #d93025;
}

.remember-label {
  font-size: 15px;
  color: #666;
  cursor: pointer;
  user-select: none;
  transition: color 0.3s ease;
}

.remember-label:hover {
  color: #d93025;
}

/* 错误信息 */
.error-message {
  background-color: #fff5f5;
  border: 1px solid #ffccc7;
  color: #ff4d4f;
  padding: 12px 20px;
  border-radius: 8px;
  margin-bottom: 28px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.error-message::before {
  content: "⚠️";
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  padding: 16px;
  border: none;
  border-radius: 10px;
  font-size: 17px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  letter-spacing: 0.5px;
}

.party-btn {
  background: linear-gradient(135deg, #d93025 0%, #b71c1c 100%);
  color: white;
  box-shadow: 0 6px 20px rgba(217, 48, 37, 0.3);
}

.party-btn:hover {
  background: linear-gradient(135deg, #c5221f 0%, #9f0000 100%);
  box-shadow: 0 8px 25px rgba(217, 48, 37, 0.4);
  transform: translateY(-2px);
}

.party-btn:active {
  transform: translateY(0);
}

.party-btn:disabled {
  background: linear-gradient(135deg, #ffccc7 0%, #ffaaa5 100%);
  box-shadow: none;
  cursor: not-allowed;
  transform: none;
}

/* 加载动画 */
.loading-spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 测试账号信息 */
.login-info {
  margin-top: 35px;
  padding: 24px;
  background-color: #f8f9fa;
  border-radius: 10px;
  border: 1px solid #e9ecef;
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.login-info:hover {
  box-shadow: inset 0 4px 15px rgba(0, 0, 0, 0.1);
}

.login-info h4 {
  font-size: 15px;
  color: #d93025;
  margin-bottom: 15px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 8px;
}

.login-info h4::before {
  content: "💡";
}

.test-account {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.test-account p {
  font-size: 14px;
  color: #555;
  margin: 0;
  line-height: 1.6;
  padding: 8px 12px;
  background-color: rgba(217, 48, 37, 0.05);
  border-radius: 6px;
  border-left: 3px solid #d93025;
}

.test-account strong {
  color: #d93025;
  font-weight: 600;
}

/* 注册链接 */
.register-link {
  text-align: center;
  margin-top: 25px;
  color: #666;
  font-size: 14px;
}

.link-btn {
  background: none;
  border: none;
  color: #d93025;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  padding: 0;
  margin-left: 5px;
  transition: color 0.3s ease;
  text-decoration: none;
}

.link-btn:hover {
  color: #b71c1c;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .login-container {
    flex-direction: column;
  }
  
  .login-left {
    min-height: 400px;
    order: 1;
  }
  
  .login-right {
    order: 2;
  }
  
  .party-banner {
    text-align: center;
  }
  
  .party-quotes {
    flex-direction: column;
    align-items: center;
  }
  
  .quote-item {
    max-width: 400px;
  }
}

@media (max-width: 576px) {
  .login-left,
  .login-right {
    padding: 20px;
  }
  
  .party-banner {
    padding: 20px;
  }
  
  .banner-title {
    font-size: 28px;
  }
  
  .login-form {
    padding: 35px 25px;
  }
  
  .form-header h2 {
    font-size: 24px;
  }
  
  .quote-text {
    font-size: 16px;
  }
}
</style>
