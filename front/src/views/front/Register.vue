<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { register } from '../../api/auth'

const router = useRouter()
const userStore = useUserStore()

// 注册表单数据
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  gender: '男', // 性别：男/女
  age: '',
  partyStatus: '正式党员', // 党员身份
  branch: '第一党支部', // 所属支部
  phone: '',
  email: '',
  idCard: '', // 身份证号
  position: '', // 职务
  education: '本科', // 学历
  workYears: 0, // 工作年限
  avatar: '', // 头像
  role: 'member' // 系统角色：admin/manager/member
})

// 错误信息
const errorMsg = ref('')
// 成功信息
const successMsg = ref('')
// 加载状态
const loading = ref(false)

// 表单验证
const validateForm = () => {
  if (!registerForm.username) {
    errorMsg.value = '请输入用户名'
    return false
  }
  
  if (registerForm.username.length < 3 || registerForm.username.length > 50) {
    errorMsg.value = '用户名长度应在3-50个字符之间'
    return false
  }
  
  if (!registerForm.password) {
    errorMsg.value = '请输入密码'
    return false
  }
  
  // 密码长度必须在6-20个字符之间，包含大小写字母和数字
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,20}$/;
  if (!passwordRegex.test(registerForm.password)) {
    errorMsg.value = '密码不符合要求：密码长度必须在6-20个字符之间，包含大小写字母和数字'
    return false
  }
  
  if (registerForm.password !== registerForm.confirmPassword) {
    errorMsg.value = '两次输入的密码不一致'
    return false
  }
  
  if (!registerForm.name) {
    errorMsg.value = '请输入姓名'
    return false
  }
  
  if (!registerForm.gender) {
    errorMsg.value = '请选择性别'
    return false
  }
  
  if (!registerForm.age) {
    errorMsg.value = '请输入年龄'
    return false
  }
  
  if (parseInt(registerForm.age) < 18 || parseInt(registerForm.age) > 100) {
    errorMsg.value = '年龄必须在18-100岁之间'
    return false
  }
  
  if (!registerForm.partyStatus) {
    errorMsg.value = '请选择党员身份'
    return false
  }
  
  if (!registerForm.branch) {
    errorMsg.value = '请选择所属支部'
    return false
  }
  
  // 手机号码验证（必填）
  if (!registerForm.phone) {
    errorMsg.value = '请输入手机号码'
    return false
  }
  if (!/^1[3-9]\d{9}$/.test(registerForm.phone)) {
    errorMsg.value = '手机号码格式不正确'
    return false
  }
  
  // 邮箱验证（可选，非必填）
  if (registerForm.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(registerForm.email)) {
    errorMsg.value = '邮箱格式不正确'
    return false
  }
  
  // 身份证号验证
  if (!registerForm.idCard) {
    errorMsg.value = '请输入身份证号'
    return false
  }
  
  // 简单的身份证号格式验证（18位数字或最后一位为X）
  if (!/^\d{17}[\dXx]$/.test(registerForm.idCard)) {
    errorMsg.value = '身份证号格式不正确'
    return false
  }
  
  // 入党日期验证：不能是未来日期
  if (registerForm.joinDate) {
    const joinDate = new Date(registerForm.joinDate);
    const today = new Date();
    if (joinDate > today) {
      errorMsg.value = '入党日期不能是未来日期'
      return false
    }
  }
  
  return true
}

// 注册函数
const handleRegister = async () => {
  // 表单验证
  if (!validateForm()) {
    return
  }
  
  loading.value = true
  errorMsg.value = ''
  
  try {
    // 准备注册数据（排除不需要发送的字段）
    const registerData = {
      username: registerForm.username,
      password: registerForm.password,
      name: registerForm.name,
      gender: registerForm.gender,
      age: parseInt(registerForm.age),
      partyStatus: registerForm.partyStatus,
      branch: registerForm.branch,
      phone: registerForm.phone,
      email: registerForm.email,
      idCard: registerForm.idCard,
      joinDate: registerForm.joinDate || new Date(),
      position: registerForm.position,
      education: registerForm.education,
      workYears: parseInt(registerForm.workYears) || 0,
      avatar: registerForm.avatar,
      role: registerForm.role
    }
    
    // 调用后端注册接口
    const response = await register(registerData)
    
    // 调试日志：打印完整响应
    console.log('注册响应:', response)
    
    // 注册成功的条件：如果响应存在，且没有error字段，或者response.success为true，或者response.code为200
    if (response && !response.error && response.code === 200) {
      // 注册成功
      successMsg.value = '注册成功！将在3秒后自动跳转到登录页面...'
      errorMsg.value = ''
      
      // 跳转到登录页面
      setTimeout(() => {
        console.log('执行注册成功跳转...')
        router.push({ name: 'Login' })
      }, 3000)
    } else {
      // 注册失败，同时支持message和msg字段
      errorMsg.value = response?.message || response?.msg || '注册失败，请稍后重试'
      successMsg.value = ''
    }
  } catch (error) {
    // 处理错误
    console.error('注册错误:', error)
    if (error.response) {
      // 服务器返回了错误响应，同时支持message和msg字段
      errorMsg.value = error.response.data?.message || error.response.data?.msg || '注册失败，请稍后重试'
    } else if (error.request) {
      // 请求已发送但没有收到响应
      errorMsg.value = '服务器无响应，请检查网络连接'
    } else {
      // 请求配置错误
      errorMsg.value = '注册请求失败，请稍后重试'
    }
    successMsg.value = ''
  } finally {
    loading.value = false
  }
}

// 返回登录页面
const goToLogin = () => {
  router.push('/login')
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

    <!-- 右侧注册表单区域 -->
    <div class="login-right">
      <div class="login-form-wrapper">
        <form class="login-form" @submit.prevent="handleRegister">
          <!-- 表单标题 -->
          <div class="form-header">
            <img src="@/assets/images/logo_touming.png" alt="红芯云课堂" class="login-logo">
            <h2>用户注册</h2>
            <p>欢迎注册红芯软炬党建教育平台</p>
          </div>
          
          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-user"></i>
              <input
                type="text"
                id="username"
                v-model="registerForm.username"
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
                v-model="registerForm.password"
                class="form-control"
                placeholder="请输入密码"
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-password"></i>
              <input
                type="password"
                id="confirmPassword"
                v-model="registerForm.confirmPassword"
                class="form-control"
                placeholder="请再次输入密码"
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-name"></i>
              <input
                type="text"
                id="name"
                v-model="registerForm.name"
                class="form-control"
                placeholder="请输入姓名"
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-gender"></i>
              <select
                id="gender"
                v-model="registerForm.gender"
                class="form-control"
              >
                <option value="男">男</option>
                <option value="女">女</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-age"></i>
              <input
                type="number"
                id="age"
                v-model="registerForm.age"
                class="form-control"
                placeholder="请输入年龄"
                min="18"
                max="100"
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-party-status"></i>
              <select
                id="partyStatus"
                v-model="registerForm.partyStatus"
                class="form-control"
              >
                <option value="入党申请人">入党申请人</option>
                <option value="入党积极分子">入党积极分子</option>
                <option value="发展对象">发展对象</option>
                <option value="预备党员">预备党员</option>
                <option value="正式党员">正式党员</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-branch"></i>
              <select
                id="branch"
                v-model="registerForm.branch"
                class="form-control"
              >
                <option value="党委办公室">党委办公室</option>
                <option value="组织部">组织部</option>
                <option value="宣传部">宣传部</option>
                <option value="第一党支部">第一党支部</option>
                <option value="第二党支部">第二党支部</option>
                <option value="第三党支部">第三党支部</option>
                <option value="第四党支部">第四党支部</option>
                <option value="第五党支部">第五党支部</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-phone"></i>
              <input
                type="tel"
                id="phone"
                v-model="registerForm.phone"
                class="form-control"
                placeholder="请输入手机号码"
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-email"></i>
              <input
                type="email"
                id="email"
                v-model="registerForm.email"
                class="form-control"
                placeholder="请输入邮箱"
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-id-card"></i>
              <input
                type="text"
                id="idCard"
                v-model="registerForm.idCard"
                class="form-control"
                placeholder="请输入身份证号"
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-education"></i>
              <select
                id="education"
                v-model="registerForm.education"
                class="form-control"
              >
                <option value="初中">初中</option>
                <option value="高中">高中</option>
                <option value="大专">大专</option>
                <option value="本科">本科</option>
                <option value="硕士研究生">硕士研究生</option>
                <option value="博士研究生">博士研究生</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-work-years"></i>
              <input
                type="number"
                id="workYears"
                v-model="registerForm.workYears"
                class="form-control"
                placeholder="请输入工作年限"
                min="0"
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <i class="icon-role"></i>
              <select
                id="role"
                v-model="registerForm.role"
                class="form-control"
              >
                <option value="member">普通用户</option>
                <option value="manager">部门管理员</option>
                <option value="admin">系统管理员</option>
              </select>
            </div>
          </div>

          <div v-if="errorMsg" class="error-message">
    {{ errorMsg }}
  </div>
  <div v-if="successMsg" class="success-message">
    {{ successMsg }}
    <button class="link-button" @click="router.push('/login')">立即登录</button>
  </div>

          <button
            type="submit"
            class="login-btn party-btn"
            :disabled="loading"
          >
            <span v-if="loading" class="loading-spinner"></span>
            <span v-else>注册</span>
          </button>

          <!-- 已有账号？去登录 -->
          <div class="register-link">
            已有账号？
            <button type="button" class="link-btn" @click="goToLogin">去登录</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 复用登录页面的大部分样式 */
/* 全局登录容器 */
.login-container {
  min-height: 100vh;
  display: flex;
  position: relative;
  overflow: hidden;
  height: 100vh;
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
  height: 100vh;
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
  align-items: flex-start;
  justify-content: center;
  padding: 40px;
  min-height: 100vh;
  overflow-y: auto;
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
  margin-bottom: 20px;
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

.icon-name::before {
  content: "📝";
}

.icon-gender::before {
  content: "👥";
}

.icon-age::before {
  content: "📅";
}

.icon-party-status::before {
  content: "⚡";
}

.icon-branch::before {
  content: "🏢";
}

.icon-phone::before {
  content: "📱";
}

.icon-email::before {
  content: "📧";
}

.icon-education::before {
  content: "🎓";
}

.icon-work-years::before {
  content: "💼";
}

.icon-role::before {
  content: "🎭";
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

/* 成功信息 */
.success-message {
  background-color: #f6ffed;
  border: 1px solid #b7eb8f;
  color: #52c41a;
  padding: 12px 20px;
  border-radius: 8px;
  margin-bottom: 28px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: space-between;
}

.success-message::before {
  content: "✅";
}

/* 链接按钮 */
.link-button {
  background: none;
  border: none;
  color: #1890ff;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  padding: 0;
  margin-left: 10px;
  transition: color 0.3s ease;
}

.link-button:hover {
  color: #40a9ff;
  text-decoration: underline;
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