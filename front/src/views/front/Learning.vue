<template>
  <div class="learning">
    <!-- 导航栏 -->
    <Navbar />
    
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>红芯云课堂・炬光研习</h2>
    </div>

    <!-- 搜索和分类 -->
    <div class="learning-header">
      <!-- 学习资源分类 -->
      <div class="learning-categories">
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
      <div class="learning-search">
        <input 
          type="text" 
          v-model="searchKeyword" 
          @input="searchResources" 
          placeholder="搜索学习资源标题..." 
          class="search-input"
        />
        <button class="btn btn-search">搜索</button>
      </div>
    </div>

    <!-- 学习资源列表 -->
    <div class="learning-container">
      <div class="learning-item" v-for="resource in paginatedResources" :key="resource.id">
        <!-- 资源图片 -->
        <div v-if="resource.image" class="resource-image">
          <img :src="resource.image" :alt="resource.title" />
          <div class="resource-type" :class="resource.type">
            {{ getResourceTypeText(resource.type) }}
          </div>
        </div>
        
        <div class="resource-info">
          <h3 class="resource-title">{{ resource.title }}</h3>
          <div class="resource-meta">
            <span class="resource-author">{{ resource.author }}</span>
            <span class="resource-date">{{ resource.date }}</span>
            <span class="resource-duration">{{ resource.duration }}</span>
            <span class="resource-level">{{ resource.level }}</span>
          </div>
          <p class="resource-description">{{ resource.description }}</p>
          <div class="resource-stats">
            <span class="stat-item"><i class="icon-play"></i> {{ resource.views }}次学习</span>
            <span class="stat-item"><i class="icon-star"></i> {{ resource.rating }}</span>
          </div>
          <div class="resource-actions">
            <button class="btn btn-primary" @click="viewResourceDetail(resource.id)">查看详情</button>
            <button class="btn btn-secondary" @click="startLearning(resource.id)">开始学习</button>
          </div>
        </div>
      </div>
      <div v-if="paginatedResources.length === 0" class="no-resources">
        <p>没有找到符合条件的学习资源</p>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <button class="btn btn-page" :disabled="currentPage === 1" @click="prevPage">上一页</button>
      <div class="page-numbers">
        <button 
          v-for="page in totalPages" 
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
  </div>
    <!-- 智能助手 -->
    <ChatAssistant />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/front/Navbar.vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import { useUserStore } from '@/stores/user'
import learningApi from '@/api/learning.js'

const router = useRouter()
const userStore = useUserStore()

// 分类列表
const categories = ref([
  { id: 'all', name: '全部' }
])

// 加载分类列表
const loadCategories = async () => {
  try {
    const response = await learningApi.getResourceCategories()
    if (response.code === 200 && response.data) {
      categories.value = [{ id: 'all', name: '全部' }, ...response.data]
    }
  } catch (err) {
    console.error('加载分类列表失败:', err)
  }
}

// 选中的分类
const selectedCategory = ref('all')

// 搜索关键词
const searchKeyword = ref('')

// 学习资源数据
const learningResources = ref([])
// 加载状态
const loading = ref(false)
// 错误信息
const error = ref('')

// 分页参数
const currentPage = ref(1)
const pageSize = ref(6)

// 筛选后的资源列表
const filteredResources = computed(() => {
  let result = [...learningResources.value]
  
  // 按分类筛选
  if (selectedCategory.value !== 'all') {
    result = result.filter(resource => resource.category === selectedCategory.value)
  }
  
  // 按关键词搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(resource => resource.title.toLowerCase().includes(keyword) || resource.description.toLowerCase().includes(keyword))
  }
  
  return result
})

// 分页后的资源列表
const paginatedResources = computed(() => {
  // 学习资源数据直接存储在learningResources.value中
  return Array.isArray(learningResources.value) ? learningResources.value : []
})

// 总页数
const totalPages = computed(() => {
  if (learningResources.value.data) {
    // 支持total和totalElements两种属性名
    const total = learningResources.value.data.total || learningResources.value.data.totalElements || 0
    return Math.ceil(total / pageSize.value) || 1
  }
  return 1
})

// 分页操作
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

const goToPage = (page) => {
  currentPage.value = page
}

// 加载学习资源列表
const loadLearningResources = async () => {
  loading.value = true
  error.value = ''
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    // 添加分类筛选
    if (selectedCategory.value !== 'all') {
      params.category = selectedCategory.value
    }
    
    // 添加搜索关键词
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    
    const response = await learningApi.getLearningResources(params)
    if (response.code === 200 && response.data) {
      // 处理学习资源数据，支持content属性（分页结构）、resources属性和直接返回数组
      const resourcesData = response.data.content || response.data.resources || response.data
      learningResources.value = Array.isArray(resourcesData) ? resourcesData : []
      
      // 保存原始响应数据以便分页计算使用
      learningResources.value.data = response.data
    } else {
      error.value = response.message || '获取学习资源失败'
      console.error('获取学习资源失败:', response)
      learningResources.value = []
      learningResources.value.data = { total: 0, pages: 1 }
    }
  } catch (err) {
    error.value = '网络错误，请稍后重试'
    console.error('加载学习资源失败:', err)
  } finally {
    loading.value = false
  }
}

// 选择分类
const selectCategory = (categoryId) => {
  selectedCategory.value = categoryId
  currentPage.value = 1 // 筛选时重置到第一页
  searchKeyword.value = '' // 筛选时清空搜索关键词
  loadLearningResources() // 重新加载数据
}

// 搜索资源
const searchResources = () => {
  currentPage.value = 1 // 搜索时重置到第一页
  loadLearningResources() // 重新加载数据
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

// 查看资源详情
const viewResourceDetail = async (resourceId) => {
  // 如果用户已登录，添加到浏览记录
  if (userStore.isLoggedIn) {
    userStore.addBrowsingHistory({
      id: resourceId,
      type: 'learning',
      title: '', // 这里可以先传空，后续可以从API获取完整标题
      topic: '',
      timestamp: new Date().toISOString()
    })
  }
  // 跳转到资源详情页面
  router.push({ name: 'LearningDetail', params: { id: resourceId } })
}

// 开始学习
const startLearning = async (resourceId) => {
  try {
    // 如果用户已登录，添加到浏览记录
    if (userStore.isLoggedIn) {
      userStore.addBrowsingHistory({
        id: resourceId,
        type: 'learning',
        title: '', // 这里可以先传空，后续可以从API获取完整标题
        topic: '',
        timestamp: new Date().toISOString()
      })
    }
    
    // 调用开始学习API
    const response = await learningApi.startResource(resourceId)
    if (response.code === 200) {
      const resource = paginatedResources.value.find(r => r.id === resourceId)
      if (resource) {
        if (resource.type === 'quiz') {
          alert(`开始测试: ${resource.title}`)
          // router.push({ name: 'Quiz', params: { id: resourceId } })
        } else {
          alert(`开始学习: ${resource.title}`)
          // 预留跳转到学习页面的代码
          // router.push({ name: 'LearningPlayer', params: { id: resourceId } })
        }
      }
    } else {
      alert(response.message || '开始学习失败')
    }
  } catch (err) {
    console.error('开始学习失败:', err)
    alert('网络错误，请稍后重试')
  }
}

// 初始加载数据
onMounted(() => {
  loadCategories()
  loadLearningResources()
})
</script>

<style scoped>
.learning {
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

/* 搜索和分类 */
.learning-header {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 30px auto 30px;
}

.learning-categories {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: center;
}

.btn-category {
  padding: 10px 20px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background-color: white;
  color: #666;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-category:hover {
  border-color: #d93025;
  color: #d93025;
}

.btn-category.active {
  background-color: #d93025;
  color: white;
  border-color: #d93025;
}

.learning-search {
  display: flex;
  gap: 10px;
  justify-content: center;
  align-items: center;
}

.search-input {
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  width: 400px;
}

.search-input:focus {
  outline: none;
  border-color: #d93025;
}

/* 学习资源列表 */
.learning-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 0 auto 30px;
}

.learning-item {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  display: flex;
  gap: 20px;
}

.learning-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.resource-image {
  width: 200px;
  height: 130px;
  overflow: hidden;
  border-radius: 4px;
  position: relative;
  flex-shrink: 0;
}

.resource-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.resource-image:hover img {
  transform: scale(1.05);
}

.resource-type {
  position: absolute;
  bottom: 0;
  left: 0;
  padding: 4px 10px;
  color: white;
  font-size: 12px;
  font-weight: 500;
  border-radius: 0 4px 0 0;
}

.resource-type.video {
  background-color: #e74c3c;
}

.resource-type.document {
  background-color: #3498db;
}

.resource-type.audio {
  background-color: #9b59b6;
}

.resource-type.article {
  background-color: #2ecc71;
}

.resource-type.case {
  background-color: #f39c12;
}

.resource-type.quiz {
  background-color: #1abc9c;
}

.resource-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.resource-title {
  font-size: 20px;
  color: #2c3e50;
  margin: 0 0 10px 0;
  line-height: 1.4;
}

.resource-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #7f8c8d;
}

.resource-level {
  background-color: #f5f5f5;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.resource-description {
  color: #666;
  margin: 0 0 15px 0;
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.resource-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  font-size: 14px;
  color: #7f8c8d;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.icon-play::before {
  content: '▶';
  color: #d93025;
}

.icon-star::before {
  content: '★';
  color: #f39c12;
}

.resource-actions {
  display: flex;
  gap: 10px;
  margin-top: auto;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
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
}

.btn-secondary:hover {
  background: #e0e0e0;
}

.btn-search {
  background: #d93025;
  color: white;
}

.btn-search:hover {
  background: #c0392b;
}

.no-resources {
  text-align: center;
  padding: 40px;
  color: #7f8c8d;
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
  background: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
}

.btn-page:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-page-number {
  width: 36px;
  height: 36px;
  background: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
}

.btn-page-number.active {
  background: #d93025;
  color: white;
  border-color: #d93025;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .learning-item {
    flex-direction: column;
  }
  
  .resource-image {
    width: 100%;
    height: 200px;
  }
  
  .learning-search {
    flex-direction: column;
  }
  
  .search-input {
    width: 100%;
  }
}
</style>