<script setup>
import { ref, computed, onMounted } from 'vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 编辑模式状态
const isEditing = ref(false)
const editForm = ref({})

// 计算属性获取个人信息和画像数据
const userInfo = computed(() => userStore.userInfo)
// 直接从userStore获取用户画像数据，不通过getters
const userProfile = computed(() => userStore.userPortrait)
// 从userProfile中直接提取学习兴趣标签和党员特质标签
const preferenceLabels = computed(() => userProfile.value.learningTags || [])
const personalityLabels = computed(() => userProfile.value.personalityTraits || [])

// 检查是否有画像数据
const hasProfileData = computed(() => {
  return preferenceLabels.value.length > 0 || 
         personalityLabels.value.length > 0 || 
         (userProfile.value.learningPreferences && 
          (Object.keys(userProfile.value.learningPreferences.newsReading || {}).length > 0 || 
           Object.keys(userProfile.value.learningPreferences.learningTopics || {}).length > 0))
})

// 初始化编辑表单
const initEditForm = () => {
  editForm.value = { ...userInfo.value }
  isEditing.value = true
}

// 保存编辑的用户信息
const saveUserInfo = () => {
  userStore.updateUserInfo(editForm.value)
  isEditing.value = false
}

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false
}

// 返回上一页
const goBack = () => {
  router.back()
}



// 组件挂载时自动获取用户画像
onMounted(async () => {
  console.log('Profile.vue的onMounted钩子被调用，开始获取用户画像');
  try {
    const result = await userStore.fetchUserProfile()
    console.log('Profile.vue获取用户画像成功，结果:', JSON.stringify(result, null, 2));
  } catch (error) {
    console.error('获取用户画像失败:', error)
  }
})
</script>

<template>
  <div class="profile-container">
    <!-- 消息提示组件已移除 -->
    <!-- 顶部党建主题横幅 -->
    <div class="party-banner">
      <div class="banner-content">
        <button class="back-btn party-btn-back" @click="goBack">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M19 12H5M12 19l-7-7 7-7"/>
          </svg>
          返回
        </button>
        <div class="party-icon">
          <img src="/src/assets/images/dangwei.png" alt="党徽" width="40" height="40">
        </div>
        <h1 class="banner-title">个人中心</h1>
        <div class="banner-subtitle">不忘初心 牢记使命</div>
      </div>
    </div>
    
    <div class="profile-content">

      <!-- 个人信息卡片 -->
      <div class="profile-card party-card">
        <div class="card-header party-header">
          <h2 class="card-title">个人基本信息</h2>
          <button class="btn-edit party-btn" @click="initEditForm" v-if="!isEditing">编辑</button>
          <div class="edit-actions" v-else>
            <button class="btn-save party-btn-primary" @click="saveUserInfo">保存</button>
            <button class="btn-cancel party-btn-secondary" @click="cancelEdit">取消</button>
          </div>
        </div>
        <div class="card-body">
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">姓名</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.name }}</div>
              <input type="text" v-model="editForm.name" class="form-input party-input" v-else>
            </div>
            
            <div class="info-item">
              <div class="info-label">性别</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.gender || '未填写' }}</div>
              <select v-model="editForm.gender" class="form-select party-select" v-else>
                <option value="">请选择</option>
                <option value="男">男</option>
                <option value="女">女</option>
              </select>
            </div>
            
            <div class="info-item">
              <div class="info-label">年龄</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.age || '未填写' }}</div>
              <input type="number" v-model="editForm.age" class="form-input party-input" v-else>
            </div>
            
            <div class="info-item">
              <div class="info-label">党员身份</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.partyStatus || '未填写' }}</div>
              <input type="text" v-model="editForm.partyStatus" class="form-input party-input" v-else>
            </div>
            
            <div class="info-item">
              <div class="info-label">所在支部</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.branch || '未填写' }}</div>
              <input type="text" v-model="editForm.branch" class="form-input party-input" v-else>
            </div>
            
            <div class="info-item">
              <div class="info-label">联系方式</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.phone || '未填写' }}</div>
              <input type="tel" v-model="editForm.phone" class="form-input party-input" v-else>
            </div>
            
            <div class="info-item">
              <div class="info-label">电子邮箱</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.email || '未填写' }}</div>
              <input type="email" v-model="editForm.email" class="form-input party-input" v-else>
            </div>
            
            <div class="info-item">
              <div class="info-label">入党日期</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.joinDate || '未填写' }}</div>
              <input type="date" v-model="editForm.joinDate" class="form-input party-input" v-else>
            </div>
            
            <div class="info-item">
              <div class="info-label">职位</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.position || '未填写' }}</div>
              <input type="text" v-model="editForm.position" class="form-input party-input" v-else>
            </div>
            
            <div class="info-item">
              <div class="info-label">学历</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.education || '未填写' }}</div>
              <input type="text" v-model="editForm.education" class="form-input party-input" v-else>
            </div>
            
            <div class="info-item">
              <div class="info-label">工作年限</div>
              <div class="info-value" v-if="!isEditing">{{ userInfo.workYears || '未填写' }}</div>
              <input type="number" v-model="editForm.workYears" class="form-input party-input" v-else>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 个人画像卡片 -->
      <div class="profile-card party-card">
        <div class="card-header party-header">
          <h2 class="card-title">党员画像</h2>
        </div>
        <div class="card-body">
          <!-- 只有当有画像数据时才显示内容 -->
          <template v-if="hasProfileData">
            <!-- 党建风格用户画像展示 -->
            <div class="party-profile-visual">
              <!-- 中央头像区域 -->
              <div class="profile-avatar-container">
                <div class="avatar-frame">
                  <div class="avatar-glow party-glow"></div>
                  <div class="profile-avatar">
                    <svg width="120" height="120" viewBox="0 0 120 120">
                      <!-- 党建背景圆环 -->
                      <circle cx="60" cy="60" r="55" fill="none" stroke="url(#partyAvatarGradient)" stroke-width="3" />
                      <!-- 党徽元素 -->
                      <g class="party-emblem">
                        <path d="M60,25 L60,95" stroke="#e74c3c" stroke-width="2" opacity="0.8"/>
                        <path d="M25,60 L95,60" stroke="#e74c3c" stroke-width="2" opacity="0.8"/>
                      </g>
                      <!-- 核心人像轮廓 -->
                      <g class="avatar-core">
                        <circle cx="60" cy="50" r="20" fill="url(#partyCoreGradient)" />
                        <path d="M45,70 C45,90 75,90 75,70 Z" fill="url(#partyBodyGradient)" />
                        <!-- 党员徽章 -->
                        <circle cx="75" cy="40" r="5" fill="#e74c3c" opacity="0.9">
                          <animate attributeName="opacity" values="0.6;1;0.6" dur="2s" repeatCount="indefinite" />
                        </circle>
                      </g>
                      <!-- 渐变定义 -->
                      <defs>
                        <linearGradient id="partyAvatarGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                          <stop offset="0%" style="stop-color:#e74c3c;stop-opacity:0.8" />
                          <stop offset="100%" style="stop-color:#c0392b;stop-opacity:0.8" />
                        </linearGradient>
                        <linearGradient id="partyCoreGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                          <stop offset="0%" style="stop-color:#e74c3c;stop-opacity:1" />
                          <stop offset="100%" style="stop-color:#c0392b;stop-opacity:1" />
                        </linearGradient>
                        <linearGradient id="partyBodyGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                          <stop offset="0%" style="stop-color:#c0392b;stop-opacity:0.8" />
                          <stop offset="100%" style="stop-color:#a93226;stop-opacity:0.8" />
                        </linearGradient>
                      </defs>
                    </svg>
                  </div>
                </div>
                <div class="avatar-name party-name">
                  <span class="name-text">{{ userInfo.name || '未知姓名' }}同志</span>
                </div>
                <div class="avatar-stats party-stats">
                  <div class="stat-item">
                    <span class="stat-value">{{ preferenceLabels.length }}</span>
                    <span class="stat-label">学习标签</span>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 学习兴趣标签 -->
            <div class="profile-section" v-if="preferenceLabels.length > 0">
              <h3 class="section-title">学习兴趣</h3>
              <div class="labels-container party-labels">
                <span class="profile-label party-label" v-for="(label, index) in preferenceLabels" :key="'pref-' + index">
                  <span class="label-icon">📚</span>
                  {{ label }}
                </span>
              </div>
            </div>
            
            <!-- 党员特质标签 -->
            <div class="profile-section" v-if="personalityLabels.length > 0">
              <h3 class="section-title">党员特质</h3>
              <div class="labels-container party-labels">
                <span class="profile-label party-personality-label" v-for="(label, index) in personalityLabels" :key="'pers-' + index">
                  <span class="label-icon">⭐</span>
                  {{ label }}
                </span>
              </div>
            </div>
            
            <!-- 学习偏好分析 -->
            <div class="profile-section" v-if="userProfile.learningPreferences && (Object.keys(userProfile.learningPreferences.newsReading || {}).length > 0 || Object.keys(userProfile.learningPreferences.learningTopics || {}).length > 0)">
              <h3 class="section-title">学习偏好分析</h3>
              <div class="analysis-content party-analysis">
                <div class="analysis-item" v-if="userProfile.learningPreferences.newsReading && Object.keys(userProfile.learningPreferences.newsReading).length > 0">
                  <h4 class="analysis-subtitle">新闻阅读偏好</h4>
                  <div class="progress-container party-progress">
                    <div class="progress-item" v-for="(count, category) in userProfile.learningPreferences.newsReading" :key="category">
                      <span class="progress-label">{{ category }}</span>
                      <div class="progress-bar">
                        <div class="progress-fill party-progress-fill" :style="{ width: (count / Math.max(...Object.values(userProfile.learningPreferences.newsReading))) * 100 + '%' }"></div>
                      </div>
                      <span class="progress-value">{{ count }}次</span>
                    </div>
                  </div>
                </div>
                
                <div class="analysis-item" v-if="userProfile.learningPreferences.learningTopics && Object.keys(userProfile.learningPreferences.learningTopics).length > 0">
                  <h4 class="analysis-subtitle">学习主题偏好</h4>
                  <div class="progress-container party-progress">
                    <div class="progress-item" v-for="(count, topic) in userProfile.learningPreferences.learningTopics" :key="topic">
                      <span class="progress-label">{{ topic }}</span>
                      <div class="progress-bar">
                        <div class="progress-fill party-progress-fill learning" :style="{ width: (count / Math.max(...Object.values(userProfile.learningPreferences.learningTopics))) * 100 + '%' }"></div>
                      </div>
                      <span class="progress-value">{{ count }}次</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </template>
          <div v-else class="no-profile-data">
            <p>暂无党员画像数据</p>
          </div>
        </div>
      </div>
      
      <!-- 党员承诺区域 -->
      <div class="profile-card party-card commitment-card">
        <div class="card-header party-header">
          <h2 class="card-title">党员承诺</h2>
        </div>
        <div class="card-body">
          <div class="commitment-content">
            <div class="commitment-icon">
              <svg width="50" height="50" viewBox="0 0 100 100" fill="none">
                <circle cx="50" cy="50" r="45" stroke="#e74c3c" stroke-width="3" fill="#fef2f2"/>
                <path d="M35,50 L45,60 L65,40" stroke="#e74c3c" stroke-width="5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="commitment-text">
              <p>我承诺：</p>
              <ul>
                <li>认真学习贯彻习近平新时代中国特色社会主义思想</li>
                <li>严守党的纪律，践行党的宗旨</li>
                <li>积极参加党组织活动，发挥党员先锋模范作用</li>
                <li>不忘初心，牢记使命，为共产主义事业奋斗终身</li>
              </ul>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
    <!-- 智能助手 -->
    <ChatAssistant />
</template>

<style scoped>
/* 消息提示样式 */
.message {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 12px 20px;
  border-radius: 4px;
  color: white;
  font-size: 14px;
  z-index: 1000;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s;
}

.message-success {
  background-color: #52c41a;
}

.message-error {
  background-color: #ff4d4f;
}

.message-info {
  background-color: #1890ff;
}

/* 基础样式重置 */
* {
  box-sizing: border-box;
}

/* 主容器样式 */
.profile-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
  font-family: 'Microsoft YaHei', sans-serif;
  background-color: #f8f9fa;
}

/* 党建主题横幅 */
.party-banner {
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
  color: white;
  padding: 30px 20px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

.party-banner::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: repeating-linear-gradient(
    45deg,
    transparent,
    transparent 10px,
    rgba(255, 255, 255, 0.05) 10px,
    rgba(255, 255, 255, 0.05) 20px
  );
  animation: bannerMove 20s linear infinite;
}

@keyframes bannerMove {
  0% { transform: translate(0, 0); }
  100% { transform: translate(50px, 50px); }
}

.banner-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  position: relative;
}

.back-btn {
  position: absolute;
  left: 20px;
}

.party-icon {
  color: white;
  margin-bottom: 10px;
}

.banner-title {
  font-size: 32px;
  color: white;
  margin: 0;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* 返回按钮样式 */
.back-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.party-btn-back {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.party-btn-back:hover {
  background-color: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateX(-2px);
}

.party-btn-back svg {
  stroke: white;
}

.banner-subtitle {
  font-size: 16px;
  opacity: 0.9;
  font-style: italic;
}

/* 内容区域样式 */
.profile-content {
  display: grid;
  gap: 25px;
  padding: 25px;
  grid-template-columns: 1fr;
}

/* 卡片样式 */
.profile-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.profile-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

/* 党建卡片样式 */
.party-card {
  border-left: 4px solid #e74c3c;
}

/* 卡片头部 */
.card-header {
  padding: 20px 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
}

.party-header {
  background-color: #fef2f2;
  border-bottom-color: #fecaca;
}

.card-title {
  margin: 0;
  font-size: 20px;
  color: #c0392b;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-title::before {
  content: "★";
  color: #e74c3c;
}

/* 卡片内容 */
.card-body {
  padding: 25px;
}

/* 个人信息网格 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-label {
  font-weight: bold;
  color: #555;
  font-size: 14px;
}

.info-value {
  color: #333;
  font-size: 16px;
  padding: 8px 12px;
  background-color: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}

/* 表单样式 */
.form-input, .form-select {
  padding: 10px 12px;
  border: 1px solid #e74c3c;
  border-radius: 4px;
  font-size: 14px;
  width: 100%;
  background-color: white;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.form-input:focus, .form-select:focus {
  outline: none;
  border-color: #c0392b;
  box-shadow: 0 0 0 3px rgba(231, 76, 60, 0.1);
}

/* 党建表单样式 */
.party-input, .party-select {
  border-color: #e74c3c;
}

/* 按钮样式 */
.btn-edit, .btn-save, .btn-cancel {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
}

/* 党建按钮样式 */
.party-btn {
  background-color: #e74c3c;
  color: white;
}

.party-btn:hover {
  background-color: #c0392b;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(231, 76, 60, 0.3);
}

.party-btn-primary {
  background-color: #e74c3c;
  color: white;
  margin-right: 10px;
}

.party-btn-primary:hover {
  background-color: #c0392b;
}

.party-btn-secondary {
  background-color: #f8f9fa;
  color: #6c757d;
  border: 1px solid #dee2e6;
}

.party-btn-secondary:hover {
  background-color: #e9ecef;
  color: #495057;
}

/* 编辑操作区域 */
.edit-actions {
  display: flex;
  gap: 10px;
}

/* 党建风格个人画像样式 */
.party-profile-visual {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.profile-avatar-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.avatar-frame {
  position: relative;
  padding: 10px;
  background: radial-gradient(circle, rgba(231, 76, 60, 0.1) 0%, rgba(231, 76, 60, 0.05) 70%, transparent 100%);
  border-radius: 50%;
}

.profile-avatar {
  position: relative;
  width: 120px;
  height: 120px;
  z-index: 2;
}

/* 党建发光效果 */
.avatar-glow.party-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 140px;
  height: 140px;
  background: radial-gradient(circle, rgba(231, 76, 60, 0.3) 0%, rgba(231, 76, 60, 0.1) 70%, transparent 100%);
  border-radius: 50%;
  animation: glow-pulse 3s ease-in-out infinite;
  z-index: 1;
}

@keyframes glow-pulse {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.6; }
  50% { transform: translate(-50%, -50%) scale(1.1); opacity: 0.8; }
}

/* 统计信息 */
.avatar-name.party-name {
  margin-top: 15px;
  text-align: center;
}

.name-text {
  font-size: 24px;
  font-weight: bold;
  color: #c72c41;
  font-family: 'Microsoft YaHei', sans-serif;
}

.avatar-stats.party-stats {
  display: flex;
  gap: 30px;
  margin-top: 15px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: #e74c3c;
}

.stat-label {
  display: block;
  font-size: 14px;
  color: #666;
  margin-top: 5px;
  font-weight: 500;
}

/* 个人画像部分样式 */
.profile-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.profile-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.section-title {
  margin: 0 0 18px 0;
  font-size: 18px;
  color: #c0392b;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 10px;
}

.section-title::before {
  content: "📋";
  font-size: 20px;
}

/* 标签容器 */
.labels-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

/* 党建标签样式 */
.party-labels .profile-label {
  background-color: #fef2f2;
  color: #c0392b;
  border: 1px solid #fecaca;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.party-labels .profile-label:hover {
  background-color: #fee2e2;
  border-color: #fca5a5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(231, 76, 60, 0.15);
}

.party-labels .personality-label {
  background-color: #f0fdf4;
  color: #166534;
  border-color: #bbf7d0;
}

.party-labels .personality-label:hover {
  background-color: #dcfce7;
  border-color: #86efac;
}

.label-icon {
  margin-right: 6px;
  font-size: 14px;
}

.empty-label {
  color: #999;
  font-style: italic;
  padding: 8px 16px;
}

/* 分析内容样式 */
.analysis-content {
  display: grid;
  gap: 25px;
}

/* 党建分析样式 */
.party-analysis {
  background-color: #fef2f2;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #fecaca;
}

.analysis-item h4 {
  margin: 0 0 18px 0;
  font-size: 16px;
  color: #c0392b;
  font-weight: bold;
}

/* 进度条样式 */
.progress-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.progress-item {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.progress-label {
  width: 120px;
  font-size: 14px;
  color: #555;
  font-weight: 500;
  flex-shrink: 0;
}

.progress-bar {
  flex: 1;
  height: 12px;
  background-color: #f3f4f6;
  border-radius: 6px;
  overflow: hidden;
  min-width: 150px;
}

.progress-fill {
  height: 100%;
  border-radius: 6px;
  transition: width 1s ease-out;
  position: relative;
  overflow: hidden;
}

/* 党建进度条样式 */
.party-progress-fill {
  background: linear-gradient(90deg, #e74c3c 0%, #c0392b 100%);
}

.party-progress-fill::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
  animation: progress-shine 2s infinite;
}

@keyframes progress-shine {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.progress-value {
  width: 60px;
  font-size: 13px;
  color: #666;
  text-align: right;
  font-weight: 500;
  flex-shrink: 0;
}

/* 党员承诺卡片 */
.commitment-card {
  background: linear-gradient(135deg, #fef2f2 0%, #fee2e2 100%);
  border-left-color: #e74c3c;
}

.commitment-content {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.commitment-icon {
  flex-shrink: 0;
  margin-top: 5px;
}

.commitment-text p {
  margin: 0 0 15px 0;
  font-size: 18px;
  font-weight: bold;
  color: #c0392b;
}

.commitment-text ul {
  margin: 0;
  padding-left: 20px;
  color: #555;
}

.commitment-text li {
  margin-bottom: 8px;
  line-height: 1.6;
}

.commitment-text li::marker {
  color: #e74c3c;
  font-weight: bold;
}

/* 空状态样式 */
.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #999;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-content {
    padding: 15px;
    gap: 20px;
  }
  
  .banner-content {
    padding: 0 15px;
  }
  
  .back-btn {
    left: 15px;
    padding: 6px 12px;
    font-size: 13px;
  }
  
  .party-icon img {
    width: 35px;
    height: 35px;
  }
  
  .banner-title {
    font-size: 24px;
  }
  
  .banner-subtitle {
    font-size: 14px;
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    padding: 15px 20px;
  }
  
  .card-body {
    padding: 20px;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
    gap: 18px;
  }
  
  .progress-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .progress-label {
    width: auto;
  }
  
  .progress-value {
    width: auto;
  }
  
  .progress-bar {
    width: 100%;
  }
  
  .commitment-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .commitment-text ul {
    text-align: left;
  }
  
  .avatar-stats.party-stats {
    flex-direction: column;
    gap: 15px;
  }
}

@media (min-width: 769px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
}
</style>