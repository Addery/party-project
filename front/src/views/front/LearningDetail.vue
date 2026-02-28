<template>
  <div class="learning-detail">
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
        <h2>红芯课堂・智慧党建</h2>
      </div>
    </div>

    <!-- 资源详情 -->
    <div class="detail-container">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>

      <!-- 错误信息 -->
      <div v-else-if="error" class="error">
        <p>{{ error }}</p>
        <button class="btn btn-primary" @click="loadResourceDetail">重新加载</button>
      </div>

      <!-- 资源详情内容 -->
      <div v-else-if="resource" class="resource-detail">
        <!-- 资源头部 -->
        <div class="resource-header">
          <!-- 资源图片 -->
          <div v-if="resource.image" class="resource-image">
            <img :src="resource.image" :alt="resource.title" />
            <div class="resource-type" :class="resource.type">
              {{ getResourceTypeText(resource.type) }}
            </div>
          </div>
          
          <!-- 资源基本信息 -->
          <div class="resource-basic-info">
            <h1 class="resource-title">{{ resource.title }}</h1>
            <div class="resource-meta">
              <span class="resource-author">{{ resource.author }}</span>
              <span class="resource-date">{{ resource.date }}</span>
              <span class="resource-duration">{{ resource.duration }}</span>
              <span class="resource-level">{{ resource.level }}</span>
            </div>
            <div class="resource-stats">
              <span class="stat-item"><i class="icon-play"></i> {{ resource.views }}次学习</span>
              <span class="stat-item"><i class="icon-star"></i> {{ resource.rating }}</span>
            </div>
            <div class="resource-actions">
              <button class="btn btn-primary" @click="startLearning">开始学习</button>
              <button v-if="userStore.isLoggedIn" class="btn btn-secondary" @click="toggleFavorite">
                <i class="icon-star"></i> {{ isFavorite ? '取消收藏' : '收藏资源' }}
              </button>
            </div>
          </div>
        </div>

        <!-- 资源详情内容 -->
        <div class="resource-content">
          <!-- 资源描述 -->
          <div class="section">
            <h3>资源介绍</h3>
            <p class="resource-description">{{ resource.description }}</p>
          </div>

          <!-- 资源内容 -->
          <div class="section">
            <h3>学习内容</h3>
            <div v-html="resource.content"></div>
          </div>

          <!-- 资源标签 -->
          <div class="section">
            <h3>标签</h3>
            <div class="resource-tags">
              <span v-for="tag in resource.tags" :key="tag" class="tag-item">
                {{ tag }}
              </span>
            </div>
          </div>
        </div>

        <!-- 相关推荐 -->
        <div class="related-resources">
          <h3>相关推荐</h3>
          <div class="related-list">
            <div v-for="item in relatedResources" :key="item.id" class="related-item">
              <div v-if="item.image" class="related-image">
                <img :src="item.image" :alt="item.title" />
              </div>
              <div class="related-info">
                <h4 class="related-title" @click="viewRelatedDetail(item.id)">{{ item.title }}</h4>
                <div class="related-meta">
                  <span>{{ item.author }}</span>
                  <span>{{ item.date }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
    <!-- 智能助手 -->
    <ChatAssistant />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Navbar from '@/components/front/Navbar.vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import { useUserStore } from '@/stores/user'
import learningApi from '@/api/learning.js'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 返回上一页
const goBack = () => {
  router.back()
}

// 资源ID
const resourceId = ref(route.params.id)

// 资源详情
const resource = ref(null)
// 相关资源
const relatedResources = ref([])
// 加载状态
const loading = ref(true)
// 错误信息
const error = ref('')
// 是否已收藏
const isFavorite = ref(false)

// 加载资源详情
const loadResourceDetail = async () => {
  loading.value = true
  error.value = ''
  try {
    // 获取资源详情
    const detailResponse = await learningApi.getResourceDetail(resourceId.value)
    if (detailResponse.code === 200) {
      // 处理tags字段，将字符串转换为数组
      const resourceData = detailResponse.data
      if (resourceData.tags && typeof resourceData.tags === 'string') {
        resourceData.tags = resourceData.tags.split(',').map(tag => tag.trim())
      } else if (!resourceData.tags) {
        resourceData.tags = []
      }
      resource.value = resourceData
    } else {
      error.value = detailResponse.message || '获取资源详情失败'
    }

    // 获取相关资源
    const relatedResponse = await learningApi.getRelatedResources(resourceId.value, 5)
    if (relatedResponse.code === 200) {
      relatedResources.value = relatedResponse.data
    }

    // 检查是否已收藏
    if (userStore.isLoggedIn) {
      const favoriteResponse = await learningApi.checkFavorite(resourceId.value)
      if (favoriteResponse.code === 200) {
        isFavorite.value = favoriteResponse.data.isFavorite
      }
    }
  } catch (err) {
    error.value = '网络错误，请稍后重试'
    console.error('加载资源详情失败:', err)
  } finally {
    loading.value = false
  }
}

// 开始学习
const startLearning = async () => {
  try {
    // 如果用户已登录，添加到浏览记录
    if (userStore.isLoggedIn) {
      userStore.addBrowsingHistory({
        id: resourceId.value,
        type: 'learning',
        title: resource.value.title,
        topic: resource.value.category,
        timestamp: new Date().toISOString()
      })
    }
    
    // 调用开始学习API
    const response = await learningApi.startResource(resourceId.value)
    if (response.code === 200) {
      // 根据资源类型跳转到不同的学习页面
      if (resource.value.type === 'quiz') {
        // 跳转到测试页面
        alert(`开始测试: ${resource.value.title}`)
        // router.push({ name: 'Quiz', params: { id: resourceId.value } })
      } else {
        // 跳转到学习播放页面
        alert(`开始学习: ${resource.value.title}`)
        // router.push({ name: 'LearningPlayer', params: { id: resourceId.value } })
      }
    } else {
      alert(response.message || '开始学习失败')
    }
  } catch (err) {
    console.error('开始学习失败:', err)
    alert('网络错误，请稍后重试')
  }
}

// 收藏/取消收藏
const toggleFavorite = async () => {
  try {
    if (isFavorite.value) {
      // 取消收藏
      const response = await learningApi.removeFavorite(resourceId.value)
      if (response.code === 200) {
        isFavorite.value = false
        alert('取消收藏成功')
      }
    } else {
      // 添加收藏
      const response = await learningApi.addFavorite(resourceId.value)
      if (response.code === 200) {
        isFavorite.value = true
        alert('收藏成功')
      }
    }
  } catch (err) {
    console.error('操作收藏失败:', err)
    alert('网络错误，请稍后重试')
  }
}

// 查看相关资源详情
const viewRelatedDetail = (id) => {
  router.push({ name: 'LearningDetail', params: { id } })
}

// 获取资源类型文本
const getResourceTypeText = (type) => {
  const typeMap = {
    'video': '视频',
    'document': '文档',
    'audio': '音频',
    'article': '文章',
    'case': '案例',
    'quiz': '测试'
  }
  return typeMap[type] || '未知'
}

// 初始加载数据
onMounted(() => {
  loadResourceDetail()
})
</script>

<style scoped>
.learning-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
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

.page-header h2 {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  color: white;
}

.detail-container {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 30px auto;
  width: calc(100% - 40px);
}

.loading {
  text-align: center;
  padding: 40px;
}

.loading-spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #d93025;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 0 auto 15px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error {
  text-align: center;
  padding: 40px;
  color: #e74c3c;
}

.resource-header {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.resource-image {
  width: 300px;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
  position: relative;
  flex-shrink: 0;
}

.resource-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.resource-type {
  position: absolute;
  bottom: 0;
  left: 0;
  padding: 6px 12px;
  color: white;
  font-size: 14px;
  font-weight: 500;
  border-radius: 0 8px 0 0;
}

.resource-type.video { background-color: #e74c3c; }
.resource-type.document { background-color: #3498db; }
.resource-type.audio { background-color: #9b59b6; }
.resource-type.article { background-color: #2ecc71; }
.resource-type.case { background-color: #f39c12; }
.resource-type.quiz { background-color: #1abc9c; }

.resource-basic-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.resource-title {
  font-size: 28px;
  color: #2c3e50;
  margin: 0 0 15px 0;
  line-height: 1.3;
}

.resource-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 15px;
  font-size: 14px;
  color: #7f8c8d;
}

.resource-level {
  background-color: #f5f5f5;
  padding: 4px 12px;
  border-radius: 15px;
  font-size: 12px;
}

.resource-stats {
  display: flex;
  gap: 25px;
  margin-bottom: 20px;
  font-size: 14px;
  color: #7f8c8d;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.icon-play::before { content: '▶'; color: #d93025; }
.icon-star::before { content: '★'; color: #f39c12; }

.resource-actions {
  display: flex;
  gap: 15px;
  margin-top: auto;
}

.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: #d93025;
  color: white;
}

.btn-primary:hover {
  background: #c0392b;
}

.btn-secondary {
  background: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-secondary:hover {
  background: #e0e0e0;
}

.resource-content {
  margin-bottom: 40px;
}

.section {
  margin-bottom: 30px;
}

.section h3 {
  font-size: 20px;
  color: #2c3e50;
  margin: 0 0 15px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.resource-description {
  color: #666;
  line-height: 1.6;
  font-size: 16px;
}

.resource-content :deep(h1),
.resource-content :deep(h2),
.resource-content :deep(h3),
.resource-content :deep(h4),
.resource-content :deep(h5),
.resource-content :deep(h6) {
  color: #2c3e50;
  margin-top: 20px;
  margin-bottom: 10px;
}

.resource-content :deep(p) {
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
}

.resource-content :deep(img) {
  max-width: 100%;
  height: auto;
  margin: 15px 0;
  border-radius: 4px;
}

.resource-content :deep(a) {
  color: #d93025;
  text-decoration: none;
}

.resource-content :deep(a:hover) {
  text-decoration: underline;
}

.resource-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag-item {
  background-color: #f5f5f5;
  padding: 6px 12px;
  border-radius: 15px;
  font-size: 14px;
  color: #7f8c8d;
}

.related-resources {
  margin-top: 40px;
  padding-top: 30px;
  border-top: 1px solid #eee;
}

.related-resources h3 {
  font-size: 20px;
  color: #2c3e50;
  margin: 0 0 20px 0;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.related-item {
  display: flex;
  gap: 15px;
  padding: 15px;
  border-radius: 8px;
  transition: all 0.3s;
  cursor: pointer;
}

.related-item:hover {
  background-color: #f8f9fa;
}

.related-image {
  width: 120px;
  height: 80px;
  overflow: hidden;
  border-radius: 4px;
  flex-shrink: 0;
}

.related-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.related-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.related-title {
  font-size: 16px;
  color: #2c3e50;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.related-title:hover {
  color: #d93025;
}

.related-meta {
  font-size: 12px;
  color: #7f8c8d;
  display: flex;
  gap: 15px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .resource-header {
    flex-direction: column;
  }
  
  .resource-image {
    width: 100%;
    height: 250px;
  }
  
  .resource-title {
    font-size: 24px;
  }
  
  .resource-actions {
    flex-direction: column;
  }
  
  .btn {
    width: 100%;
  }
}
</style>