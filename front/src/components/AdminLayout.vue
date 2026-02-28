<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'

// 路由和状态管理
const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 侧边栏折叠状态
const sidebarCollapsed = ref(false)

// 切换侧边栏
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}

// 导航菜单项 - 使用简约矢量图标
const navItems = [
  { 
    name: 'dashboard', 
    label: '仪表盘', 
    icon: 'dashboard', 
    path: '/admin',
    children: []
  },
  {
    name: 'organization',
    label: '组织与用户管理',
    icon: 'organization',
    path: '/admin/organization',
    children: [
      { name: 'organization', label: '组织架构', path: '/admin/organization' },
      { name: 'user', label: '用户管理', path: '/admin/users' }
    ]
  },
  {
    name: 'party-work',
    label: '核心党务工作',
    icon: 'document',
    path: '/admin/three-meetings',
    children: [
      { name: 'three-meetings', label: '三会一课', path: '/admin/three-meetings' },
      { name: 'theme-party-day', label: '主题党日', path: '/admin/party-day' },
      { name: 'member-development', label: '党员发展', path: '/admin/member-development' },
      { name: 'dues-collection', label: '党费收缴', path: '/admin/dues' }
    ]
  },
  {
    name: 'education',
    label: '党员教育与思政融合',
    icon: 'book',
    path: '/admin/learning-resources',
    children: [
      { name: 'learning-resources', label: '学习资源库', path: '/admin/learning-resources' },
      { name: 'exam-management', label: '在线考试', path: '/admin/exams' }
    ]
  },
  {
    name: 'information',
    label: '信息发布与宣传阵地',
    icon: 'megaphone',
    path: '/admin/news',
    children: [
      { name: 'news', label: '新闻管理', path: '/admin/news' }
    ]
  },
  {
    name: 'interaction',
    label: '互动与民主管理',
    icon: 'chat',
    path: '/admin/interaction',
    children: [
      { name: 'interaction', label: '互动管理', path: '/admin/interaction' }
    ]
  },
  {
    name: 'analysis',
    label: '数据统计与决策支持',
    icon: 'chart',
    path: '/admin/analysis',
    children: [
      { name: 'analysis', label: '数据看板', path: '/admin/analysis' }
    ]
  },
  {
    name: 'system',
    label: '系统设置',
    icon: 'settings',
    path: '/admin/settings',
    children: [
      { name: 'system', label: '系统设置', path: '/admin/settings' }
    ]
  }
]

// 计算当前激活的菜单项
const activeMenuItem = computed(() => {
  const currentPath = route.path
  for (const item of navItems) {
    if (item.path === currentPath) return item.name
    for (const child of item.children) {
      if (child.path === currentPath) return item.name
    }
  }
  return null
})

// 计算当前页面标题
const pageTitle = computed(() => {
  const currentPath = route.path
  for (const item of navItems) {
    if (item.path === currentPath) return item.label
    for (const child of item.children) {
      if (child.path === currentPath) return child.label
    }
  }
  return '党员管理系统'
})

// 展开的菜单
const expandedMenus = ref([activeMenuItem.value])

// 切换菜单展开/收起
const toggleMenu = (menuName) => {
  const index = expandedMenus.value.indexOf(menuName)
  if (index > -1) {
    expandedMenus.value.splice(index, 1)
  } else {
    expandedMenus.value.push(menuName)
  }
}

// 判断菜单是否展开
const isMenuExpanded = (menuName) => {
  return expandedMenus.value.includes(menuName)
}

// 退出登录
const logout = () => {
  userStore.logout()
  router.push({ name: 'Login' })
}

// 获取用户信息
const userInfo = computed(() => {
  return userStore.userInfo || {
    name: '管理员',
    role: '系统管理员'
  }
})
</script>

<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
      <!-- 侧边栏头部 -->
      <div class="sidebar-header">
        <div class="sidebar-logo">
          <div v-if="!sidebarCollapsed" class="logo-full">
            <span class="logo-text">红芯软炬党建教育平台</span>
          </div>
          <div v-else class="logo-short">
            <span class="logo-text">红芯</span>
          </div>
        </div>
      </div>

      <!-- 导航菜单 -->
      <nav class="sidebar-nav">
        <div 
          v-for="item in navItems" 
          :key="item.name"
          class="nav-group"
        >
          <!-- 有子菜单的导航项 -->
          <div v-if="item.children && item.children.length > 0" class="nav-group-item">
            <button 
              class="nav-item-btn"
              :class="{ active: activeMenuItem === item.name }"
              @click="toggleMenu(item.name)"
            >
              <span class="nav-icon" :class="item.icon"></span>
              <span class="nav-text" v-if="!sidebarCollapsed">{{ item.label }}</span>
              <span class="nav-arrow" v-if="!sidebarCollapsed">
                {{ isMenuExpanded(item.name) ? '▼' : '▶' }}
              </span>
            </button>
            
            <!-- 子菜单 -->
            <div 
              v-if="isMenuExpanded(item.name) && !sidebarCollapsed"
              class="nav-submenu"
            >
              <router-link 
                v-for="child in item.children" 
                :key="child.name"
                :to="child.path"
                class="nav-submenu-item"
                :class="{ active: route.path === child.path }"
              >
                {{ child.label }}
              </router-link>
            </div>
          </div>
          
          <!-- 无子菜单的导航项 -->
          <router-link 
            v-else
            :to="item.path"
            class="nav-item-btn"
            :class="{ active: activeMenuItem === item.name }"
          >
            <span class="nav-icon" :class="item.icon"></span>
            <span class="nav-text" v-if="!sidebarCollapsed">{{ item.label }}</span>
          </router-link>
        </div>
      </nav>

      <!-- 侧边栏底部 -->
      <div class="sidebar-footer">
        <div class="user-info" v-if="!sidebarCollapsed">
          <div class="user-name">{{ userInfo.name }}</div>
          <div class="user-role">{{ userInfo.role }}</div>
        </div>
        <button class="back-to-front-btn" @click="router.push('/')" title="返回前台">
          <span v-if="!sidebarCollapsed">🏠 前台</span>
          <span v-else>🏠</span>
        </button>
        <button class="logout-btn" @click="logout" title="退出登录">
          <span v-if="!sidebarCollapsed">🚪 退出</span>
          <span v-else>🚪</span>
        </button>
      </div>
    </aside>

    <!-- 主内容区域 -->
    <main class="main-content">
      <!-- 顶部导航栏 -->
      <header class="top-header">
        <div class="header-left">
          <button 
            class="sidebar-toggle-btn" 
            @click="toggleSidebar"
            title="切换侧边栏"
          >
            ☰
          </button>
          <!-- 侧边栏收缩/展开按钮 -->
          <button 
            v-if="!sidebarCollapsed" 
            class="header-sidebar-toggle header-sidebar-collapse-btn" 
            @click="toggleSidebar" 
            title="折叠侧边栏"
          >
            ◀
          </button>
          <button 
            v-else 
            class="header-sidebar-toggle header-sidebar-expand-btn" 
            @click="toggleSidebar" 
            title="展开侧边栏"
          >
            ▶
          </button>
          <h1 class="page-title">{{ pageTitle }}</h1>
        </div>
        <div class="header-right">
          <!-- 通知按钮 -->
          <div class="notification-bell" title="通知">
            🔔
          </div>
          <!-- 用户头像 -->
          <div class="user-avatar" title="用户信息">
            👤
          </div>
        </div>
      </header>

      <!-- 内容区域 -->
      <div class="content-wrapper">
        <slot></slot>
      </div>
    </main>
  </div>
</template>

<style scoped>
/* 全局样式 */
.admin-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f0f2f5;
  font-family: 'Microsoft YaHei', sans-serif;
}

/* 侧边栏 - 采用白色简约风格 */
.sidebar {
  width: 250px;
  background-color: white;
  color: #333;
  display: flex;
  flex-direction: column;
  transition: all 0.4s ease-in-out;
  box-shadow: 2px 0 15px rgba(0, 0, 0, 0.1);
  z-index: 100;
  position: fixed;
  height: 100vh;
  top: 0;
  left: 0;
  overflow: hidden;
}

/* 侧边栏装饰条 */
.sidebar::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 5px;
  height: 100%;
  background: linear-gradient(180deg, #c72c41 0%, #801336 100%);
  z-index: 1;
}

/* 侧边栏折叠状态 */
.sidebar.sidebar-collapsed {
  width: 76px;
  overflow: visible;
}

/* 折叠状态下的侧边栏头部 */
.sidebar.sidebar-collapsed .sidebar-header {
  padding: 15px 13px;
  justify-content: center;
}

/* 顶部导航栏中的侧边栏切换按钮 */
.header-sidebar-toggle {
  background: none;
  border: none;
  color: #666;
  font-size: 16px;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  margin-right: 12px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 32px;
  height: 32px;
}

.header-sidebar-toggle:hover {
  background-color: rgba(199, 44, 65, 0.1);
  color: #c72c41;
  transform: scale(1.1);
}

/* 展开按钮特定样式 */
.header-sidebar-expand-btn {
  /* 添加一些额外的样式以区分展开和收缩按钮 */
  font-weight: bold;
}

/* 侧边栏展开状态下的内容区域内边距 */
.sidebar:not(.sidebar-collapsed) + .main-content .content-wrapper {
  padding-left: 20px;
}

/* 侧边栏头部 */
.sidebar-header {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: white;
  position: relative;
  z-index: 2;
}

/* 侧边栏收缩按钮 */
.sidebar-collapse-btn {
  background: none;
  border: none;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 30px;
  height: 30px;
}

.sidebar-collapse-btn:hover {
  background-color: #f5f5f5;
  color: #c72c41;
}

.sidebar-collapse-btn:active {
  transform: scale(0.95);
}

.sidebar-logo {
  font-size: 18px;
  font-weight: 700;
  display: flex;
  align-items: center;
  position: relative;
  z-index: 2;
  width: 100%;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  color: #c72c41;
  font-family: 'Microsoft YaHei', sans-serif;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

/* 折叠状态下的logo文字样式 */
.sidebar-collapsed .logo-text {
  font-size: 20px;
  font-weight: 800;
}

/* 切换图标样式 */
.toggle-icon {
  width: 24px;
  height: 24px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

/* 收缩图标 - 减号/折叠样式 */
.collapse-icon {
  width: 24px;
  height: 24px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.collapse-icon::before {
  content: '';
  position: absolute;
  width: 16px;
  height: 2px;
  background-color: currentColor;
  border-radius: 1px;
  transition: all 0.3s ease;
}

/* 展开图标 - 加号/展开样式 */
.expand-icon {
  width: 24px;
  height: 24px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.expand-icon::before {
  content: '';
  position: absolute;
  width: 16px;
  height: 2px;
  background-color: currentColor;
  border-radius: 1px;
  transition: all 0.3s ease;
}

.expand-icon::after {
  content: '';
  position: absolute;
  width: 2px;
  height: 16px;
  background-color: currentColor;
  border-radius: 1px;
  transition: all 0.3s ease;
}

/* 红色五角星 */
.party-star {
  width: 24px;
  height: 24px;
  background-color: #c72c41;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 14px;
  font-weight: bold;
}

.party-star::before {
  content: '★';
}

/* 加号图标 */
.plus-icon {
  width: 24px;
  height: 24px;
  background-color: white;
  border: 2px solid #e0e0e0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  font-size: 18px;
  font-weight: bold;
  margin-left: 8px;
}

/* 完整logo样式 */
.logo-full {
  display: flex;
  align-items: center;
}

/* 短logo样式 */
.logo-short {
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-short .plus-icon {
  margin-left: 4px;
}

.toggle-btn:hover {
  background: rgba(199, 44, 65, 0.1);
  border-color: rgba(199, 44, 65, 0.3);
  color: #c72c41;
  transform: scale(1.1);
}

/* 导航菜单 */
.sidebar-nav {
  flex: 1;
  padding: 16px 8px;
  overflow-y: auto;
  overflow-x: hidden;
  position: relative;
  z-index: 2;
}

/* 折叠状态下的导航菜单 */
.sidebar.sidebar-collapsed .sidebar-nav {
  padding: 16px 4px;
}

/* 滚动条样式 */
.sidebar-nav::-webkit-scrollbar {
  width: 4px;
}

.sidebar-nav::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.02);
}

.sidebar-nav::-webkit-scrollbar-thumb {
  background: rgba(199, 44, 65, 0.3);
  border-radius: 2px;
}

.sidebar-nav::-webkit-scrollbar-thumb:hover {
  background: rgba(199, 44, 65, 0.5);
}

.nav-group {
  margin-bottom: 12px;
}

.nav-group-item {
  margin-bottom: 4px;
}

/* 导航按钮 */
.nav-item-btn {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 12px 16px;
  background: white;
  border: none;
  color: #555;
  text-align: left;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  margin: 0 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02);
}

/* 折叠状态下的导航按钮 */
.sidebar.sidebar-collapsed .nav-item-btn {
  padding: 14px 12px;
  justify-content: center;
  border-radius: 8px;
  margin: 0 4px 8px;
}

/* 导航按钮悬停效果 */
.nav-item-btn:hover {
  background-color: rgba(199, 44, 65, 0.08);
  color: #c72c41;
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(199, 44, 65, 0.15);
}

/* 导航按钮激活状态 */
.nav-item-btn.active {
  background: linear-gradient(135deg, rgba(199, 44, 65, 0.15) 0%, rgba(199, 44, 65, 0.1) 100%);
  color: #c72c41;
  font-weight: 600;
  box-shadow: 0 4px 16px rgba(199, 44, 65, 0.2);
  border-left: 3px solid #c72c41;
}

/* 导航图标 - 现代化图标设计 */
.nav-icon {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  margin-right: 12px;
  transition: all 0.3s ease;
}

/* 折叠状态下的导航图标 */
.sidebar.sidebar-collapsed .nav-icon {
  margin-right: 0;
  width: 26px;
  height: 26px;
}

/* 图标样式定义 */
.nav-icon::before {
  content: '';
  position: absolute;
  width: 20px;
  height: 20px;
  background-color: currentColor;
  border-radius: 4px;
  clip-path: var(--icon-clip);
  transition: all 0.3s ease;
}

/* 仪表盘图标 */
.nav-icon.dashboard {
  --icon-clip: polygon(0 0, 100% 0, 100% 100%, 0 100%);
}

.nav-icon.dashboard::after {
  content: '';
  position: absolute;
  width: 10px;
  height: 10px;
  background-color: white;
  top: 9px;
  left: 9px;
  border-radius: 2px;
}

/* 组织图标 */
.nav-icon.organization {
  --icon-clip: polygon(20% 0, 80% 0, 100% 100%, 0 100%);
}

.nav-icon.organization::after {
  content: '';
  position: absolute;
  width: 12px;
  height: 8px;
  background-color: white;
  top: 10px;
  left: 8px;
  border-radius: 2px;
}

/* 文档图标 */
.nav-icon.document {
  --icon-clip: polygon(0 10%, 100% 10%, 100% 100%, 0 100%);
}

.nav-icon.document::after {
  content: '';
  position: absolute;
  width: 12px;
  height: 2px;
  background-color: currentColor;
  top: 18px;
  left: 8px;
  border-radius: 1px;
  box-shadow: 0 -4px 0 currentColor;
}

/* 书本图标 */
.nav-icon.book {
  --icon-clip: polygon(10% 0, 90% 0, 100% 100%, 0 100%);
}

.nav-icon.book::after {
  content: '';
  position: absolute;
  width: 2px;
  height: 18px;
  background-color: white;
  top: 5px;
  left: 14px;
  transform: translateX(-50%);
}

/* 喇叭图标 */
.nav-icon.megaphone {
  --icon-clip: polygon(0 20%, 100% 0, 100% 100%, 0 80%);
}

/* 聊天图标 */
.nav-icon.chat {
  --icon-clip: circle(50% at 50% 50%);
}

.nav-icon.chat::after {
  content: '';
  position: absolute;
  width: 8px;
  height: 8px;
  background-color: white;
  top: 10px;
  left: 10px;
  border-radius: 50%;
}

/* 图表图标 */
.nav-icon.chart {
  --icon-clip: polygon(0 100%, 20% 60%, 40% 80%, 60% 40%, 80% 60%, 100% 20%, 100% 100%);
}

/* 设置图标 */
.nav-icon.settings {
  --icon-clip: polygon(50% 10%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
}

/* 导航文本 */
.nav-text {
  flex: 1;
  position: relative;
  letter-spacing: 0.3px;
}

/* 导航箭头 */
.nav-arrow {
  font-size: 14px;
  color: #aaa;
  transition: all 0.3s ease;
  font-weight: bold;
}

.nav-item-btn:hover .nav-arrow {
  color: #c72c41;
  transform: rotate(180deg);
}

/* 子菜单 */
.nav-submenu {
  background-color: white;
  padding: 8px 0;
  margin-left: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02);
}

.nav-submenu-item {
  display: block;
  padding: 10px 16px 10px 48px;
  color: #666;
  text-decoration: none;
  font-size: 14px;
  font-weight: 400;
  transition: all 0.25s ease;
  position: relative;
  border-radius: 6px;
  margin: 2px 8px;
}

/* 子菜单项前的装饰点 */
.nav-submenu-item::before {
  content: '';
  position: absolute;
  left: 32px;
  top: 50%;
  transform: translateY(-50%);
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #ddd;
  transition: all 0.3s ease;
  box-shadow: 0 0 0 3px rgba(221, 221, 221, 0.3);
}

/* 子菜单项悬停效果 */
.nav-submenu-item:hover {
  background-color: rgba(199, 44, 65, 0.08);
  color: #c72c41;
  padding-left: 52px;
  margin-left: 12px;
}

.nav-submenu-item:hover::before {
  background-color: #c72c41;
  transform: translateY(-50%) scale(1.2);
  box-shadow: 0 0 0 4px rgba(199, 44, 65, 0.2);
}

/* 子菜单项激活状态 */
.nav-submenu-item.active {
  background-color: rgba(199, 44, 65, 0.12);
  color: #c72c41;
  font-weight: 500;
  margin-left: 12px;
}

.nav-submenu-item.active::before {
  background-color: #c72c41;
  transform: translateY(-50%) scale(1.3);
  box-shadow: 0 0 0 4px rgba(199, 44, 65, 0.3);
}

/* 侧边栏底部 */
.sidebar-footer {
  padding: 16px 8px 24px;
  border-top: 1px solid rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;
  gap: 12px;
  background-color: white;
  position: relative;
  z-index: 2;
}

/* 侧边栏收缩状态下的底部区域 */
.sidebar.sidebar-collapsed .sidebar-footer {
  padding: 16px 8px 24px;
  gap: 12px;
  align-items: center;
}

/* 用户信息区域 */
.user-info {
  text-align: center;
  padding: 16px 12px;
  background-color: rgba(199, 44, 65, 0.06);
  border-radius: 12px;
  border: 1px solid rgba(199, 44, 65, 0.12);
  margin: 0 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.user-info:hover {
  background-color: rgba(199, 44, 65, 0.08);
  box-shadow: 0 4px 12px rgba(199, 44, 65, 0.1);
}

.user-name {
  font-weight: 600;
  margin-bottom: 6px;
  color: #333;
  font-size: 15px;
  letter-spacing: 0.3px;
}

.user-role {
  font-size: 13px;
  color: #c72c41;
  background-color: rgba(199, 44, 65, 0.1);
  padding: 4px 12px;
  border-radius: 16px;
  display: inline-block;
  border: 1px solid rgba(199, 44, 65, 0.2);
  box-shadow: 0 2px 4px rgba(199, 44, 65, 0.1);
}

/* 底部功能按钮 */
.back-to-front-btn,
.logout-btn {
  border: 1px solid rgba(199, 44, 65, 0.2);
  padding: 12px 16px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: #c72c41;
  background-color: rgba(199, 44, 65, 0.06);
  margin: 0 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.03);
}

/* 侧边栏收缩状态下的底部按钮 */
.sidebar.sidebar-collapsed .back-to-front-btn,
.sidebar.sidebar-collapsed .logout-btn {
  padding: 14px;
  gap: 0;
  width: 52px;
  height: 52px;
  font-size: 20px;
  border-radius: 10px;
  margin: 0;
}

/* 返回前台按钮 */
.back-to-front-btn:hover {
  background-color: rgba(199, 44, 65, 0.12);
  border-color: rgba(199, 44, 65, 0.35);
  color: #c72c41;
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(199, 44, 65, 0.18);
}

/* 退出登录按钮 */
.logout-btn {
  color: #666;
  border-color: rgba(0, 0, 0, 0.1);
  background-color: rgba(0, 0, 0, 0.03);
}

.logout-btn:hover {
  background-color: rgba(199, 44, 65, 0.12);
  border-color: rgba(199, 44, 65, 0.35);
  color: #c72c41;
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(199, 44, 65, 0.18);
}

/* 主内容区域 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-left: 250px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background-color: #f0f2f5;
}

/* 侧边栏折叠时的主内容区域 */
.sidebar.sidebar-collapsed + .main-content {
  margin-left: 60px;
}

/* 顶部导航栏 */
.top-header {
  background-color: white;
  padding: 0 25px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
  border-bottom: 2px solid #e6e6e6;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

/* 移动端侧边栏切换按钮 */
.sidebar-toggle-btn {
  background: #ffffff;
  border: 1px solid #d9d9d9;
  font-size: 18px;
  cursor: pointer;
  color: #666;
  display: none;
  width: 40px;
  height: 40px;
  border-radius: 6px;
  transition: all 0.3s ease;
  align-items: center;
  justify-content: center;
}

.sidebar-toggle-btn:hover {
  background: #f5f5f5;
  color: #c72c41;
  border-color: #c72c41;
}

/* 页面标题 */
.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
  font-family: 'Microsoft YaHei', sans-serif;
}

/* 头部右侧 */
.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

/* 通知按钮 */
.notification-bell {
  font-size: 20px;
  cursor: pointer;
  position: relative;
  color: #666;
  transition: all 0.3s ease;
  padding: 8px;
  border-radius: 6px;
}

.notification-bell:hover {
  color: #c72c41;
  background-color: #fff2f0;
}

.notification-bell::after {
  content: '';
  position: absolute;
  top: 6px;
  right: 6px;
  width: 8px;
  height: 8px;
  background-color: #ff4d4f;
  border-radius: 50%;
  border: 2px solid white;
}

/* 用户头像 */
.user-avatar {
  font-size: 24px;
  cursor: pointer;
  color: #666;
  transition: all 0.3s ease;
  padding: 6px;
  border-radius: 8px;
  background-color: #f5f5f5;
}

.user-avatar:hover {
  color: #c72c41;
  background-color: #fff2f0;
}

/* 内容包装器 */
.content-wrapper {
  padding: 25px;
  min-height: calc(100vh - 70px);
  background-color: #f0f2f5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
  }
  
  .sidebar.sidebar-collapsed {
    transform: translateX(0);
    box-shadow: 4px 0 15px rgba(199, 44, 65, 0.3);
  }
  
  .main-content {
    margin-left: 0;
  }
  
  .sidebar.sidebar-collapsed + .main-content {
    margin-left: 0;
  }
  
  .sidebar-toggle-btn {
    display: flex;
  }
  
  .top-header {
    padding: 0 15px;
  }
  
  .content-wrapper {
    padding: 15px;
  }
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 导航项淡入动画 */
.nav-item-btn,
.nav-submenu-item {
  animation: fadeIn 0.3s ease forwards;
}

/* 逐个延迟动画 */
.nav-group-item:nth-child(1) .nav-item-btn { animation-delay: 0.05s; }
.nav-group-item:nth-child(2) .nav-item-btn { animation-delay: 0.1s; }
.nav-group-item:nth-child(3) .nav-item-btn { animation-delay: 0.15s; }
.nav-group-item:nth-child(4) .nav-item-btn { animation-delay: 0.2s; }
.nav-group-item:nth-child(5) .nav-item-btn { animation-delay: 0.25s; }
.nav-group-item:nth-child(6) .nav-item-btn { animation-delay: 0.3s; }
.nav-group-item:nth-child(7) .nav-item-btn { animation-delay: 0.35s; }
.nav-group-item:nth-child(8) .nav-item-btn { animation-delay: 0.4s; }
</style>