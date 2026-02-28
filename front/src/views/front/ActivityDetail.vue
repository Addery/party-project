<template>
  <div class="activity-detail">
    <!-- 导航栏 -->
    <Navbar />
    
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="page-header-content">
        <button class="back-btn" @click="goBack">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="15 18 9 12 15 6"></polyline>
          </svg>
          返回列表
        </button>
        <h2>红芯炬事・党日华章</h2>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="isLoading" class="loading activity-container">
      加载中...
    </div>
    
    <!-- 错误提示 -->
    <div v-else-if="errorMessage" class="error-message activity-container">
      {{ errorMessage }}
      <button class="btn btn-primary" @click="loadActivityDetail">重试</button>
    </div>
    
    <!-- 活动详情内容 -->
    <div v-else-if="activity" class="activity-content activity-container">
      <!-- 活动基本信息 -->
      <div class="activity-header-section">
        <!-- 活动图片 -->
        <div v-if="activity.image" class="activity-image">
          <img :src="activity.image" :alt="activity.title" />
          <div class="activity-status" :class="activity.status">
            {{ getStatusText(activity.status) }}
          </div>
        </div>
        
        <!-- 活动标题和状态 -->
        <div class="activity-title-section">
          <h1 class="activity-title">{{ activity.title }}</h1>
          <div class="activity-meta">
            <span class="participants">{{ activity.participants }}人已参与</span>
          </div>
        </div>
      </div>
      
      <!-- 活动信息 -->
      <div class="activity-info-section">
        <div class="info-grid">
          <!-- 活动日期 -->
          <div class="info-item">
            <div class="info-label">活动日期</div>
            <div class="info-value">{{ activity.date }}</div>
          </div>
          
          <!-- 活动地点 -->
          <div class="info-item">
            <div class="info-label">活动地点</div>
            <div class="info-value">{{ activity.location }}</div>
          </div>
          
          <!-- 报名截止 -->
          <div v-if="activity.registerDeadline" class="info-item">
            <div class="info-label">报名截止</div>
            <div class="info-value">{{ activity.registerDeadline }}</div>
          </div>
          
          <!-- 活动容量 -->
          <div v-if="activity.capacity" class="info-item">
            <div class="info-label">活动容量</div>
            <div class="info-value">{{ activity.participants }}/{{ activity.capacity }}</div>
          </div>
        </div>
      </div>
      
      <!-- 活动描述 -->
      <div class="activity-description-section">
        <h3 class="section-title">活动简介</h3>
        <div class="description-content">{{ activity.description }}</div>
      </div>
      
      <!-- 活动详情 -->
      <div class="activity-content-section">
        <h3 class="section-title">活动详情</h3>
        <div class="content" v-html="processedContent"></div>
      </div>
      
      <!-- 活动标签 -->
      <div v-if="activity.tags && activity.tags.length > 0" class="activity-tags-section">
        <h3 class="section-title">活动标签</h3>
        <div class="tags">
          <span v-for="tag in activity.tags" :key="tag" class="tag">{{ tag }}</span>
        </div>
      </div>
      
      <!-- 活动附件 -->
      <div v-if="activity.attachments && activity.attachments.length > 0" class="activity-attachments-section">
        <h3 class="section-title">活动附件</h3>
        <div class="attachments">
          <a v-for="attachment in activity.attachments" :key="attachment.id" 
             :href="attachment.url" class="attachment" target="_blank" rel="noopener noreferrer">
            <span class="attachment-icon">📄</span>
            <span class="attachment-name">{{ attachment.name }}</span>
          </a>
        </div>
      </div>
      
      <!-- 操作按钮 -->
      <div class="activity-actions-section">
        <button 
          v-if="activity.status === 'published'" 
          class="btn btn-primary" 
          @click="registerActivity"
          :disabled="isRegistering"
        >
          {{ isRegistering ? '报名中...' : '立即报名' }}
        </button>
        <button v-else class="btn btn-secondary disabled">已截止报名</button>
        <button class="btn btn-secondary" @click="goBack">返回活动列表</button>
      </div>
    </div>
    
    <!-- 活动不存在 -->
    <div v-else class="activity-not-found activity-container">
      <h3>活动不存在或已被删除</h3>
      <button class="btn btn-primary" @click="goBack">返回活动列表</button>
    </div>
  </div>
    <!-- 智能助手 -->
    <ChatAssistant />
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Navbar from '@/components/front/Navbar.vue'
import activityApi from '@/api/activity.js'
import ChatAssistant from '@/components/front/ChatAssistant.vue'

const router = useRouter()
const route = useRoute()

// 活动详情数据
const activity = ref(null)

// 加载状态
const isLoading = ref(false)
const errorMessage = ref('')

// 报名状态
const isRegistering = ref(false)

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    'draft': '草稿',
    'published': '已发布',
    'ended': '已结束'
  }
  return statusMap[status] || status
}

// 加载活动详情
const loadActivityDetail = async () => {
  isLoading.value = true
  errorMessage.value = ''
  
  try {
    const activityId = route.params.id
    const response = await activityApi.getActivityDetail(activityId)
    
    if (response.code === 200) {
      activity.value = response.data
    } else {
      errorMessage.value = response.msg || response.message || '加载活动详情失败'
    }
  } catch (error) {
    console.error('加载活动详情失败:', error)
    errorMessage.value = '加载活动详情失败，请稍后重试'
  } finally {
    isLoading.value = false
  }
}

// 报名参加活动
const registerActivity = async () => {
  if (!activity.value) return
  
  isRegistering.value = true
  
  try {
    // 实际应用中，这些信息应该从用户登录信息中获取
    const registrationData = {
      userId: 1 // 示例用户ID
    }
    
    const response = await activityApi.registerActivity(activity.value.id, registrationData)
    if (response.code === 200) {
      alert('报名成功')
      // 更新本地活动数据
      activity.value.participants++
      activity.value.canRegister = false
    } else {
      alert(response.msg || response.message || '报名失败')
    }
  } catch (error) {
    console.error('报名失败:', error)
    alert('报名失败，请稍后重试')
  } finally {
    isRegistering.value = false
  }
}

// 处理HTML内容，确保规范显示
const contentProcessor = (htmlContent) => {
  if (!htmlContent) return ''
  
  // 解析HTML内容
  let parser = new DOMParser()
  let doc = parser.parseFromString(htmlContent, 'text/html')
  
  // 优化段落样式
  let paragraphs = doc.querySelectorAll('p')
  paragraphs.forEach(p => {
    if (!p.style.marginBottom) {
      p.style.marginBottom = '15px'
    }
  })
  
  // 优化标题样式
  let headings = doc.querySelectorAll('h1, h2, h3, h4, h5, h6')
  headings.forEach(heading => {
    if (!heading.style.marginTop) {
      heading.style.marginTop = '25px'
    }
    if (!heading.style.marginBottom) {
      heading.style.marginBottom = '15px'
    }
  })
  
  // 优化列表样式
  let lists = doc.querySelectorAll('ol, ul')
  lists.forEach(list => {
    if (!list.style.marginLeft) {
      list.style.marginLeft = '20px'
    }
    if (!list.style.paddingLeft) {
      list.style.paddingLeft = '30px'
    }
  })
  
  // 优化表格样式
  let tables = doc.querySelectorAll('table')
  tables.forEach(table => {
    table.style.width = '100%'
    table.style.borderCollapse = 'collapse'
    table.style.margin = '20px 0'
    table.style.overflowX = 'auto'
    
    let tableWrapper = doc.createElement('div')
    tableWrapper.style.overflowX = 'auto'
    tableWrapper.style.margin = '20px 0'
    table.parentElement.insertBefore(tableWrapper, table)
    tableWrapper.appendChild(table)
  })
  
  // 优化图片样式
  let images = doc.querySelectorAll('img')
  images.forEach(img => {
    img.style.maxWidth = '100%'
    img.style.height = 'auto'
    img.style.margin = '15px 0'
  })
  
  // 返回处理后的HTML
  return doc.body.innerHTML
}

// 计算处理后的内容
const processedContent = computed(() => {
  return contentProcessor(activity.value?.content || '')
})

// 返回活动列表
const goBack = () => {
  router.push('/activities')
}

// 组件挂载时加载数据
onMounted(() => {
  loadActivityDetail()
})
</script>

<style scoped>/* 活动详情容器 */
.activity-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 0;
}
.page-header {
  background-color: #c72c41;
  color: white;
  padding: 40px 0;
  width: 100%;
}

.page-header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.page-header h2 {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  color: white;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: 1px solid white;
  border-radius: 4px;
  background-color: transparent;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
}

.back-btn:hover {
  background-color: white;
  color: #c72c41;
}

/* 加载状态 */
.loading {
  text-align: center;
  padding: 50px;
  font-size: 16px;
  color: #666;
}

/* 错误提示 */
.error-message {
  text-align: center;
  padding: 20px;
  background-color: #f8d7da;
  color: #721c24;
  border-radius: 4px;
  margin-bottom: 20px;
}

/* 活动内容 */
.activity-container {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 30px auto;
  width: calc(100% - 40px);
}

.activity-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 100%;
}

/* 活动头部区域 */
.activity-header-section {
  width: 100%;
}

/* 活动头部 */
.activity-header-section {
  position: relative;
}
/* 活动图片 */
.activity-image {
  position: relative;
  height: 500px;
  overflow: hidden;
  width: 100%;
}

.activity-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.activity-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.activity-status {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  color: white;
}

.activity-status.draft {
  background-color: #95a5a6;
}

.activity-status.published {
  background-color: #2ecc71;
}

.activity-status.ended {
  background-color: #e74c3c;
}

.activity-title-section {
  padding: 30px;
  background-color: white;
}

.activity-title {
  font-size: 28px;
  color: #2c3e50;
  margin-bottom: 15px;
  line-height: 1.4;
}

.activity-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;
}

/* 活动信息 */
.activity-info-section {
  padding: 0 30px 30px;
  background-color: #f8f9fa;
  width: 100%;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 25px;
  max-width: 100%;
}

.info-item {
  background-color: white;
  padding: 15px;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.info-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
  font-weight: 500;
}

.info-value {
  font-size: 16px;
  color: #2c3e50;
  font-weight: bold;
}

/* 活动描述和内容 */
.activity-description-section,
.activity-content-section,
.activity-tags-section,
.activity-attachments-section {
  padding: 30px;
  border-bottom: 1px solid #eee;
}

.section-title {
  font-size: 20px;
  color: #2c3e50;
  margin-bottom: 20px;
  font-weight: bold;
}

.description-content {
  font-size: 16px;
  color: #555;
  line-height: 1.6;
}

.content {
  font-size: 16px;
  color: #555;
  line-height: 1.8;
}

.content h2 {
  font-size: 24px;
  margin: 30px 0 15px;
  color: #2c3e50;
}

.content h3 {
  font-size: 20px;
  margin: 25px 0 12px;
  color: #34495e;
}

.content p {
  margin-bottom: 15px;
}

.content ul, .content ol {
  margin-bottom: 15px;
  padding-left: 25px;
}

.content li {
  margin-bottom: 8px;
}

/* 活动标签 */
.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag {
  background-color: #e9ecef;
  color: #495057;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
}

/* 活动附件 */
.attachments {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.attachment {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  background-color: #f8f9fa;
  color: #007bff;
  text-decoration: none;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.attachment:hover {
  background-color: #e9ecef;
  color: #0056b3;
  transform: translateX(5px);
}

.attachment-icon {
  font-size: 20px;
}

.attachment-name {
  font-size: 15px;
}

/* 操作按钮 */
.activity-actions-section {
  padding: 30px;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: background-color 0.3s ease;
  text-decoration: none;
}

.btn-primary {
  background-color: #d93025;
  color: white;
}

.btn-primary:hover {
  background-color: #c12e23;
}

.btn-primary:disabled {
  background-color: #e57373;
  cursor: not-allowed;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background-color: #5a6268;
}

.btn-secondary.disabled {
  background-color: #adb5bd;
  cursor: not-allowed;
}

/* 活动不存在 */
.activity-not-found {
  text-align: center;
  padding: 100px 20px;
}

.activity-not-found h3 {
  font-size: 24px;
  color: #6c757d;
  margin-bottom: 30px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .activity-detail {
    padding: 0 15px 15px;
  }
  
  .activity-title {
    font-size: 24px;
  }
  
  .activity-title-section, 
  .activity-info-section, 
  .activity-description-section, 
  .activity-content-section, 
  .activity-tags-section, 
  .activity-attachments-section, 
  .activity-actions-section {
    padding: 20px;
  }
  
  .activity-image {
    height: 250px;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .activity-actions-section {
    flex-direction: column;
    align-items: center;
  }
  
  .btn {
    width: 100%;
    max-width: 300px;
  }
}
</style>
