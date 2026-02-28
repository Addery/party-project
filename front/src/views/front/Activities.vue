<template>
  <div class="theme-activity">
    <!-- 导航栏 -->
    <Navbar />
    
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>红芯炬事・党日华章</h2>
    </div>

    <!-- 搜索和分类 -->
    <div class="activity-header">
      <!-- 活动分类 -->
      <div class="activity-categories">
        <button 
          v-for="category in categories" 
          :key="category.id"
          class="btn btn-category"
          :class="{ active: selectedCategory === category.id }"
          @click="selectCategory(category.id)"
        >
          {{ category.name }}
        </button>
      </div>
      
      <!-- 搜索功能 -->
      <div class="activity-search">
        <input 
          type="text" 
          v-model="searchKeyword" 
          @input="searchActivities" 
          placeholder="搜索活动标题或内容..." 
          class="search-input"
        />
        <button class="btn btn-search">搜索</button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="isLoading" class="loading">
      加载中...
    </div>
    
    <!-- 错误提示 -->
    <div v-else-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
    
    <!-- 活动列表 -->
    <div v-else class="activity-container">
      <div class="activity-item" v-for="activity in paginatedActivities" :key="activity.id">
        <!-- 活动图片 -->
        <div v-if="activity.image" class="activity-image">
          <img :src="activity.image" :alt="activity.title" />
          <div class="activity-status" :class="activity.status">
            {{ getStatusText(activity.status) }}
          </div>
        </div>
        
        <!-- 活动信息 -->
        <div class="activity-info">
          <h3 class="activity-title">{{ activity.title }}</h3>
          
          <!-- 活动日期 -->
          <div class="activity-date">
            <div class="date-item">
              <span class="date-label">日期：</span>
              <span class="date-value">{{ activity.date }}</span>
            </div>
          </div>
          
          <!-- 活动地点 -->
          <div class="activity-location">
            <span class="location-label">地点：</span>
            <span class="location-value">{{ activity.location }}</span>
          </div>
          
          <!-- 活动描述 -->
          <div class="activity-description">{{ activity.description }}</div>
          
          <!-- 活动参与者 -->
          <div class="activity-meta">
            <span class="participants">{{ activity.participants }}人已参与</span>
          </div>
          
          <!-- 操作按钮 -->
          <div class="activity-actions">
            <button class="btn btn-primary" @click="viewActivity(activity.id)">查看详情</button>
            <button 
              v-if="activity.status === 'published'" 
              class="btn btn-secondary" 
              @click="registerActivity(activity.id)"
            >
              立即报名
            </button>
          </div>
        </div>
      </div>
      
      <!-- 无活动提示 -->
      <div v-if="paginatedActivities.length === 0" class="no-activities">
        <p>暂无相关活动</p>
      </div>
    </div>

    <!-- 分页控件 -->
    <div class="pagination" v-if="totalPages > 1">
      <button class="btn btn-page" :disabled="currentPage === 1" @click="prevPage">上一页</button>
      <div class="page-numbers">
        <button 
          v-for="page in pagesToShow" 
          :key="page"
          class="btn btn-page-number"
          :class="{ active: currentPage === page }"
          @click="goToPage(page)"
        >
          {{ page }}
        </button>
      </div>
      <button class="btn btn-page" :disabled="currentPage === totalPages" @click="nextPage">下一页</button>
    </div>
    
    <!-- 智能助手 -->
    <ChatAssistant />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/front/Navbar.vue'
import activityApi from '@/api/activity.js'
import ChatAssistant from '@/components/front/ChatAssistant.vue'

const router = useRouter()

// 活动分类
const categories = ref([
  { id: 'all', name: '全部' }
])

// 加载状态
const isLoading = ref(false)
const errorMessage = ref('')

// 选中的分类
const selectedCategory = ref('all')

// 搜索关键词
const searchKeyword = ref('')

// 主题活动数据
const activities = ref([])
const totalActivities = ref(0)
const totalPagesFromApi = ref(0)

// 分页参数
const currentPage = ref(1)
const pageSize = ref(6)

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    'draft': '草稿',
    'published': '已发布',
    'ended': '已结束'
  }
  return statusMap[status] || status
}

// 选择分类
const selectCategory = (categoryId) => {
  selectedCategory.value = categoryId
  currentPage.value = 1
  loadActivities()
}

// 搜索活动
const searchActivities = () => {
  currentPage.value = 1
  loadActivities()
}

// 加载活动分类
const loadCategories = async () => {
  try {
    const response = await activityApi.getActivityCategories()
    if (response.code === 200) {
      // 添加'全部'分类到最前面
      categories.value = [
        { id: 'all', name: '全部' },
        ...response.data
      ]
    }
  } catch (error) {
    console.error('加载活动分类失败:', error)
    // 加载失败时使用硬编码分类数据
    categories.value = [
      { id: 'all', name: '全部' },
      { id: 'theme', name: '主题党日' },
      { id: 'study', name: '学习教育' },
      { id: 'volunteer', name: '志愿服务' },
      { id: 'democratic', name: '民主生活' },
      { id: 'social', name: '社会实践' },
      { id: 'training', name: '培训交流' }
    ]
  }
}

// 加载活动列表
const loadActivities = async () => {
  isLoading.value = true
  errorMessage.value = ''
  
  try {
    // 构建请求参数
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    // 添加分类筛选
    if (selectedCategory.value !== 'all') {
      params.category = selectedCategory.value
    }
    
    // 添加关键词搜索
    if (searchKeyword.value.trim()) {
      params.keyword = searchKeyword.value.trim()
    }
    
    const response = await activityApi.getActivityList(params)
    
    // 添加调试信息
    console.log('活动列表API返回数据:', response)
    
    if (response.code === 200 && response.data) {
      // 处理活动数据，支持content属性（分页结构）和activities属性
      const activitiesData = response.data.content || response.data.activities
      activities.value = Array.isArray(activitiesData) ? activitiesData : []
      
      // 处理总数和页数
      totalActivities.value = response.data.total || response.data.totalElements || 0
      totalPagesFromApi.value = response.data.pages || response.data.totalPages || 0
    } else {
      errorMessage.value = response.msg || response.message || '加载活动失败'
      console.error('获取活动列表失败:', response)
    }
  } catch (error) {
    console.error('加载活动列表失败:', error)
    errorMessage.value = '加载活动列表失败，请稍后重试'
  } finally {
    isLoading.value = false
  }
}

// 过滤活动
const filteredActivities = computed(() => {
  let result = activities.value
  
  // 按分类过滤
  if (selectedCategory.value !== 'all') {
    result = result.filter(activity => activity.category === selectedCategory.value)
  }
  
  // 按关键词搜索
  if (searchKeyword.value.trim()) {
    const keyword = searchKeyword.value.trim().toLowerCase()
    result = result.filter(activity => 
      activity.title.toLowerCase().includes(keyword) ||
      activity.description.toLowerCase().includes(keyword)
    )
  }
  
  return result
})

// 计算总页数
const totalPages = computed(() => {
  return totalPagesFromApi.value
})

// 计算当前页活动
const paginatedActivities = computed(() => {
  return activities.value
})

// 计算显示的页码
const pagesToShow = computed(() => {
  const pages = []
  const maxPagesToShow = 5
  
  let startPage = Math.max(1, currentPage.value - Math.floor(maxPagesToShow / 2))
  let endPage = Math.min(totalPages.value, startPage + maxPagesToShow - 1)
  
  // 调整页码范围
  if (endPage - startPage + 1 < maxPagesToShow) {
    startPage = Math.max(1, endPage - maxPagesToShow + 1)
  }
  
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i)
  }
  
  return pages
})

// 上一页
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    loadActivities()
  }
}

// 下一页
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    loadActivities()
  }
}

// 跳转到指定页面
const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    loadActivities()
  }
}

// 查看活动详情
const viewActivity = (activityId) => {
  router.push(`/activity/${activityId}`)
}

// 报名参加活动
const registerActivity = async (activityId) => {
  // 实际应用中，这些信息应该从用户登录信息中获取
  const registrationData = {
    userId: 1 // 示例用户ID
  }
  
  try {
    const response = await activityApi.registerActivity(activityId, registrationData)
    if (response.code === 200) {
      alert('报名成功')
      // 重新加载活动列表以更新报名人数
      loadActivities()
    } else {
      alert(response.msg || response.message || '报名失败')
    }
  } catch (error) {
    console.error('报名失败:', error)
    alert('报名失败，请稍后重试')
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadCategories()
  loadActivities()
})
</script>

<style scoped>
.theme-activity {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.page-header {
  background-color: #c72c41;
  color: white;
  padding: 40px 0;
  text-align: center;
  width: 100%;
}

.page-header h2 {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
}

/* 活动头部 */
.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 20px;
  padding: 20px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 0 auto 30px;
}

/* 活动分类 */
.activity-categories {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.btn-category {
  padding: 8px 16px;
  background-color: #f0f0f0;
  color: #333;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.btn-category:hover {
  background-color: #e0e0e0;
}

.btn-category.active {
  background-color: #d93025;
  color: white;
}

/* 搜索功能 */
.activity-search {
  display: flex;
  gap: 10px;
}

.search-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  width: 300px;
}

.btn-search {
  padding: 8px 16px;
  background-color: #333;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.btn-search:hover {
  background-color: #555;
}

/* 活动列表 */
.activity-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 0 auto 30px;
}

.activity-item {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.activity-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

/* 活动图片 */
.activity-image {
  position: relative;
  overflow: hidden;
  height: 200px;
}

.activity-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.activity-item:hover .activity-image img {
  transform: scale(1.05);
}

/* 活动状态 */
.activity-status {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
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

/* 活动信息 */
.activity-info {
  padding: 20px;
}

.activity-title {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 15px;
  line-height: 1.4;
}

/* 活动日期和时间 */
.activity-date {
  display: flex;
  gap: 20px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #666;
}

.date-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.date-label {
  font-weight: 500;
}

/* 活动地点 */
.activity-location {
  display: flex;
  align-items: center;
  gap: 5px;
  margin-bottom: 15px;
  font-size: 14px;
  color: #666;
}

.location-label {
  font-weight: 500;
}

/* 活动描述 */
.activity-description {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

/* 活动元数据 */
.activity-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  font-size: 13px;
  color: #999;
}

/* 活动操作 */
.activity-actions {
  display: flex;
  gap: 10px;
}

.btn-primary, .btn-secondary {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  text-decoration: none;
  display: inline-block;
  text-align: center;
}

.btn-primary {
  background-color: #d93025;
  color: white;
}

.btn-primary:hover {
  background-color: #c5221f;
}

.btn-secondary {
  background-color: #3367d6;
  color: white;
}

.btn-secondary:hover {
  background-color: #2a55cc;
}

/* 无活动提示 */
.no-activities {
  grid-column: 1 / -1;
  text-align: center;
  padding: 50px;
  color: #666;
  font-size: 16px;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  max-width: 1200px;
  margin: 30px auto;
  padding: 0 20px;
}

.btn-page {
  padding: 8px 12px;
  background-color: #f0f0f0;
  color: #333;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.btn-page:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.btn-page:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 5px;
}

.btn-page-number {
  padding: 8px 12px;
  background-color: #f0f0f0;
  color: #333;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
  min-width: 36px;
  text-align: center;
}

.btn-page-number:hover:not(.active) {
  background-color: #e0e0e0;
}

.btn-page-number.active {
  background-color: #d93025;
  color: white;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .activity-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .activity-categories {
    justify-content: center;
  }
  
  .activity-search {
    justify-content: center;
  }
  
  .search-input {
    width: 100%;
    max-width: 300px;
  }
  
  .activity-container {
    grid-template-columns: 1fr;
  }
  
  .activity-date {
    flex-direction: column;
    gap: 5px;
  }
}

@media (max-width: 480px) {
  .theme-activity {
    padding: 10px;
  }
  
  .activity-info {
    padding: 15px;
  }
  
  .activity-title {
    font-size: 16px;
  }
  
  .activity-actions {
    flex-direction: column;
  }
}
</style>