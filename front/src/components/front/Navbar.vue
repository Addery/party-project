<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 添加调试信息
console.log('当前用户信息:', userStore.userInfo)
console.log('当前用户角色:', userStore.userInfo.role)
console.log('用户是否登录:', userStore.isLoggedIn)

const goToLogin = () => {
  router.push('/login')
}

const logout = () => {
  userStore.logout()
  router.push('/')
}

const goToProfile = () => {
  router.push('/profile')
}
</script>

<template>
  <nav class="site-nav">
    <div class="nav-content">
      <div class="site-title">
        <img src="@/assets/images/logo_touming.png" alt="红芯软炬党建教育平台" class="logo-img">
        红芯软炬党建教育平台
      </div>
      <div class="nav-links">
        <router-link to="/" class="nav-link">首页</router-link>
        <router-link to="/news" class="nav-link">薪火速递</router-link>
        <router-link to="/notice" class="nav-link">炬令通传</router-link>
        <router-link to="/learning" class="nav-link">红芯云课堂</router-link>
        <router-link to="/activities" class="nav-link">党日华章</router-link>
        <router-link to="/employment" class="nav-link">就业服务</router-link>
        <router-link to="/about" class="nav-link">炬立初心</router-link>
      </div>
      <div class="login-info">
        <div v-if="userStore.isLoggedIn" class="user-info">
          <span class="user-name">{{ userStore.userInfo.name }}</span>
          <button v-if="['admin', 'manager'].includes(userStore.userInfo.role)" class="btn-admin" @click="router.push('/admin')">后台管理</button>
          <button class="btn-logout" @click="logout">退出</button>
          <button class="btn-profile" @click="goToProfile" title="个人中心">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
              <circle cx="12" cy="7" r="4" />
            </svg>
          </button>
        </div>
        <button v-else class="btn-login" @click="goToLogin">登录</button>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.site-nav {
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
  position: sticky;
  top: 0;
  left: 0;
  z-index: 1000;
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 80px;
}

.site-title {
  font-size: 20px;
  font-weight: bold;
  color: #d93025;
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-img {
  width: 40px;
  height: auto;
}

.nav-links {
  display: flex;
  gap: 30px;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-size: 16px;
  transition: color 0.3s ease;
}

.nav-link:hover {
  color: #d93025;
}

.router-link-active {
  color: #d93025;
  font-weight: bold;
}

.login-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-name {
  font-size: 14px;
  color: #666;
}

.btn-login, .btn-logout, .btn-admin {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.btn-login {
  background-color: #d93025;
  color: white;
}

.btn-login:hover {
  background-color: #c5221f;
}

.btn-logout {
  background-color: #f0f0f0;
  color: #333;
}

.btn-logout:hover {
  background-color: #e0e0e0;
}

.btn-admin {
  background-color: #3367d6;
  color: white;
}

.btn-admin:hover {
  background-color: #2a55cc;
}

.btn-profile {
  background-color: transparent;
  color: #333;
}

.btn-profile:hover {
  background-color: #f0f0f0;
}
</style>