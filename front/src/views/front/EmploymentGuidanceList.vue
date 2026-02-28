<template>
  <div class="employment-guidance-list">
    <!-- 页面顶部导航 -->
    <div class="page-header">
      <div class="container">
        <h1>就业指导资源</h1>
        <div class="breadcrumb">
          <a href="/">首页</a> &gt; <a href="/employment">就业服务</a> &gt; 就业指导资源
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="container">
      <div class="content-wrapper">
        <!-- 左侧内容区 -->
        <div class="main-content">
          <!-- 搜索和筛选 -->
          <div class="search-filter">
            <div class="search-box">
              <input type="text" v-model="searchKeyword" placeholder="搜索就业指导资源..." />
              <button @click="handleSearch">搜索</button>
            </div>
            <div class="filter-box">
              <select v-model="selectedType" @change="handleFilter">
                <option value="">全部类型</option>
                <option value="简历指导">简历指导</option>
                <option value="面试技巧">面试技巧</option>
                <option value="职业规划">职业规划</option>
                <option value="职场适应">职场适应</option>
                <option value="创业指导">创业指导</option>
                <option value="政策解读">政策解读</option>
              </select>
            </div>
          </div>

          <!-- 指导资源列表 -->
          <div class="guidance-list">
            <div v-for="guidance in guidanceList" :key="guidance.id" class="guidance-item">
              <div class="guidance-type-badge" :class="`type-${guidance.type}`">
                {{ guidance.type }}
              </div>
              <div class="guidance-content">
                <div class="guidance-title">
                  <a href="#" @click.prevent="goToDetail(guidance.id)">{{ guidance.title }}</a>
                </div>
                <div class="guidance-meta">
                  <span class="guidance-date">{{ guidance.publishDate }}</span>
                  <span class="guidance-author">{{ guidance.author || '就业指导中心' }}</span>
                </div>
                <div class="guidance-preview">
                  {{ guidance.content?.substring(0, 100) || '' }}...
                </div>
                <div class="guidance-actions">
                  <a href="#" @click.prevent="goToDetail(guidance.id)" class="read-more">阅读全文</a>
                  <span class="view-count">浏览: {{ guidance.viewCount || 0 }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 分页控件 -->
          <div class="pagination">
            <button @click="handlePageChange(1)" :disabled="currentPage === 1">首页</button>
            <button @click="handlePageChange(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
            <span class="page-info">第 {{ currentPage }} / {{ totalPages }} 页</span>
            <button @click="handlePageChange(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
            <button @click="handlePageChange(totalPages)" :disabled="currentPage === totalPages">末页</button>
          </div>
        </div>

        <!-- 右侧边栏 -->
        <div class="sidebar">
          <!-- 热门资源 -->
          <div class="sidebar-section">
            <h3>热门资源</h3>
            <div class="hot-resources">
              <div v-for="resource in hotResources" :key="resource.id" class="hot-resource-item">
                <div class="resource-rank">{{ resource.rank }}</div>
                <div class="resource-title">
                  <a href="#" @click.prevent="goToDetail(resource.id)">{{ resource.title }}</a>
                </div>
              </div>
            </div>
          </div>

          <!-- 资源分类 -->
          <div class="sidebar-section">
            <h3>资源分类</h3>
            <div class="resource-categories">
              <div v-for="category in resourceCategories" :key="category.name" class="category-item">
                <a href="#" @click.prevent="handleCategoryFilter(category.name)">
                  <span class="category-name">{{ category.name }}</span>
                  <span class="category-count">({{ category.count }})</span>
                </a>
              </div>
            </div>
          </div>

          <!-- 最新上传 -->
          <div class="sidebar-section">
            <h3>最新上传</h3>
            <div class="latest-resources">
              <div v-for="resource in latestResources" :key="resource.id" class="latest-resource-item">
                <a href="#" @click.prevent="goToDetail(resource.id)">{{ resource.title }}</a>
                <span class="resource-date">{{ resource.publishDate }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import employmentApi from '../../api/employment'

const router = useRouter()
const guidanceList = ref([])
const hotResources = ref([])
const latestResources = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const totalPages = ref(1)
const searchKeyword = ref('')
const selectedType = ref('')

// 资源分类数据
const resourceCategories = [
  { name: '简历指导', count: 25 },
  { name: '面试技巧', count: 32 },
  { name: '职业规划', count: 18 },
  { name: '职场适应', count: 15 },
  { name: '创业指导', count: 12 },
  { name: '政策解读', count: 20 }
]

// 加载数据
onMounted(() => {
  fetchGuidanceList()
  fetchHotResources()
  fetchLatestResources()
})

// 获取就业指导资源列表
const fetchGuidanceList = async () => {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      type: selectedType.value || undefined
    }
    
    const data = await employmentApi.getEmploymentGuidance(params)
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    guidanceList.value = Array.isArray(data.data?.content) ? data.data.content : 
                        Array.isArray(data.data?.list) ? data.data.list : 
                        Array.isArray(data.data) ? data.data : []
    // 支持多种分页数据结构：total/totalElements, pages/totalPages
    total.value = data.data?.total || data.data?.totalElements || 0
    totalPages.value = data.data?.pages || data.data?.totalPages || 1
  } catch (error) {
    console.error('获取就业指导资源列表失败:', error)
    // 模拟数据
    guidanceList.value = [
      { id: 1, title: '如何制作一份优秀的简历', type: '简历指导', publishDate: '2025-11-20 00:00:00', viewCount: 523 },
      { id: 2, title: '面试中常见问题及回答技巧', type: '面试技巧', publishDate: '2025-11-18 00:00:00', viewCount: 486 },
      { id: 3, title: '大学生职业规划指南', type: '职业规划', publishDate: '2025-11-15 00:00:00', viewCount: 342 },
      { id: 4, title: '初入职场的注意事项', type: '职场适应', publishDate: '2025-11-12 00:00:00', viewCount: 298 },
      { id: 5, title: '大学生创业项目选择与评估', type: '创业指导', publishDate: '2025-11-10 00:00:00', viewCount: 265 },
      { id: 6, title: '2025年就业政策解读', type: '政策解读', publishDate: '2025-11-08 00:00:00', viewCount: 412 },
      { id: 7, title: '简历中的实习经历如何突出', type: '简历指导', publishDate: '2025-11-05 00:00:00', viewCount: 198 },
      { id: 8, title: '无领导小组讨论技巧', type: '面试技巧', publishDate: '2025-11-03 00:00:00', viewCount: 321 },
      { id: 9, title: '如何找到适合自己的职业方向', type: '职业规划', publishDate: '2025-11-01 00:00:00', viewCount: 278 },
      { id: 10, title: '职场沟通技巧与人际关系', type: '职场适应', publishDate: '2025-10-28 00:00:00', viewCount: 245 }
    ]
    total.value = 48
    totalPages.value = 5
  }
}

// 获取热门资源
const fetchHotResources = async () => {
  try {
    const data = await employmentApi.getEmploymentGuidance({ page: 1, pageSize: 5, sortBy: 'viewCount' })
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    const list = Array.isArray(data.data?.content) ? data.data.content : 
                Array.isArray(data.data?.list) ? data.data.list : 
                Array.isArray(data.data) ? data.data : []
    hotResources.value = list.map((item, index) => ({ ...item, rank: index + 1 }))
  } catch (error) {
    console.error('获取热门资源失败:', error)
    hotResources.value = [
      { id: 1, title: '如何制作一份优秀的简历', rank: 1 },
      { id: 2, title: '面试中常见问题及回答技巧', rank: 2 },
      { id: 6, title: '2025年就业政策解读', rank: 3 },
      { id: 8, title: '无领导小组讨论技巧', rank: 4 },
      { id: 3, title: '大学生职业规划指南', rank: 5 }
    ]
  }
}

// 获取最新资源
const fetchLatestResources = async () => {
  try {
    const data = await employmentApi.getEmploymentGuidance({ page: 1, pageSize: 5 })
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    latestResources.value = Array.isArray(data.data?.content) ? data.data.content : 
                           Array.isArray(data.data?.list) ? data.data.list : 
                           Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取最新资源失败:', error)
    latestResources.value = [
      { id: 1, title: '如何制作一份优秀的简历', publishDate: '2025-11-20' },
      { id: 2, title: '面试中常见问题及回答技巧', publishDate: '2025-11-18' },
      { id: 3, title: '大学生职业规划指南', publishDate: '2025-11-15' },
      { id: 4, title: '初入职场的注意事项', publishDate: '2025-11-12' },
      { id: 5, title: '大学生创业项目选择与评估', publishDate: '2025-11-10' }
    ]
  }
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/employment/guidance/${id}`)
}

// 处理分页
const handlePageChange = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
  fetchGuidanceList()
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchGuidanceList()
}

// 处理类型筛选
const handleFilter = () => {
  currentPage.value = 1
  fetchGuidanceList()
}

// 处理分类筛选
const handleCategoryFilter = (category) => {
  selectedType.value = category
  currentPage.value = 1
  fetchGuidanceList()
}
</script>

<style scoped>
.employment-guidance-list {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.page-header {
  background-color: #fff;
  padding: 30px 0;
  border-bottom: 1px solid #eee;
}

.page-header h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.breadcrumb {
  color: #666;
  font-size: 14px;
}

.breadcrumb a {
  color: #007bff;
  text-decoration: none;
}

.breadcrumb a:hover {
  text-decoration: underline;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.content-wrapper {
  display: flex;
  gap: 30px;
}

.main-content {
  flex: 1;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.search-filter {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.search-box {
  flex: 1;
  display: flex;
  gap: 10px;
}

.search-box input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.search-box button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-box button:hover {
  background-color: #0056b3;
}

.filter-box select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.guidance-list {
  margin-bottom: 30px;
}

.guidance-item {
  display: flex;
  gap: 15px;
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.guidance-item:last-child {
  border-bottom: none;
}

.guidance-type-badge {
  padding: 5px 10px;
  border-radius: 4px;
  color: #fff;
  font-size: 12px;
  font-weight: bold;
  min-width: 80px;
  text-align: center;
  align-self: flex-start;
}

.type-简历指导 { background-color: #28a745; }
.type-面试技巧 { background-color: #ffc107; color: #333; }
.type-职业规划 { background-color: #17a2b8; }
.type-职场适应 { background-color: #6f42c1; }
.type-创业指导 { background-color: #dc3545; }
.type-政策解读 { background-color: #007bff; }

.guidance-content {
  flex: 1;
}

.guidance-title {
  font-size: 18px;
  margin-bottom: 10px;
}

.guidance-title a {
  color: #333;
  text-decoration: none;
}

.guidance-title a:hover {
  color: #007bff;
  text-decoration: underline;
}

.guidance-meta {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.guidance-date, .guidance-author {
  margin-right: 20px;
}

.guidance-preview {
  font-size: 14px;
  color: #555;
  line-height: 1.6;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.guidance-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.read-more {
  color: #007bff;
  text-decoration: none;
  font-size: 14px;
}

.read-more:hover {
  text-decoration: underline;
}

.view-count {
  color: #999;
  font-size: 13px;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 20px 0;
}

.pagination button {
  padding: 8px 16px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:hover:not(:disabled) {
  background-color: #f5f5f5;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #666;
}

.sidebar {
  width: 300px;
}

.sidebar-section {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.sidebar-section h3 {
  font-size: 18px;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #007bff;
}

.hot-resources {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.hot-resource-item {
  display: flex;
  gap: 10px;
  align-items: flex-start;
}

.resource-rank {
  width: 24px;
  height: 24px;
  background-color: #007bff;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  flex-shrink: 0;
}

.hot-resource-item:nth-child(1) .resource-rank {
  background-color: #dc3545;
}

.hot-resource-item:nth-child(2) .resource-rank {
  background-color: #ffc107;
}

.hot-resource-item:nth-child(3) .resource-rank {
  background-color: #28a745;
}

.resource-title a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.6;
}

.resource-title a:hover {
  color: #007bff;
}

.resource-categories {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.category-item a {
  color: #666;
  text-decoration: none;
  font-size: 14px;
  display: flex;
  justify-content: space-between;
  padding: 5px 0;
}

.category-item a:hover {
  color: #007bff;
}

.category-count {
  color: #999;
}

.latest-resources {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.latest-resource-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.latest-resource-item a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
}

.latest-resource-item a:hover {
  color: #007bff;
}

.resource-date {
  font-size: 12px;
  color: #999;
}

@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
  }
  
  .search-filter {
    flex-direction: column;
  }
  
  .guidance-item {
    flex-direction: column;
  }
  
  .guidance-type-badge {
    align-self: flex-start;
  }
}
</style>