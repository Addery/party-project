<template>
  <div class="employment-news-list">
    <!-- 页面顶部导航 -->
    <div class="page-header">
      <div class="container">
        <h1>就业资讯</h1>
        <div class="breadcrumb">
          <a href="/">首页</a> &gt; <a href="/employment">就业服务</a> &gt; 就业资讯
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
              <input type="text" v-model="searchKeyword" placeholder="搜索就业资讯..." />
              <button @click="handleSearch">搜索</button>
            </div>
            <div class="filter-box">
              <select v-model="selectedCategory">
                <option value="">全部分类</option>
                <option value="就业分析">就业分析</option>
                <option value="招聘信息">招聘信息</option>
                <option value="就业指导">就业指导</option>
                <option value="政策解读">政策解读</option>
              </select>
            </div>
          </div>

          <!-- 资讯列表 -->
          <div class="news-list">
            <div v-for="news in employmentNewsList" :key="news.id" class="news-item">
              <div class="news-title">
                <a href="#" @click="goToDetail(news.id)">{{ news.title }}</a>
              </div>
              <div class="news-meta">
                <span class="news-date">{{ news.publishDate }}</span>
                <span class="news-source">{{ news.source }}</span>
                <span class="news-category">{{ news.category }}</span>
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
          <!-- 最新资讯 -->
          <div class="sidebar-section">
            <h3>最新资讯</h3>
            <div class="latest-news">
              <div v-for="news in latestNews" :key="news.id" class="latest-news-item">
                <a href="#" @click="goToDetail(news.id)">{{ news.title }}</a>
              </div>
            </div>
          </div>

          <!-- 热门分类 -->
          <div class="sidebar-section">
            <h3>热门分类</h3>
            <div class="category-list">
              <div v-for="category in categories" :key="category" class="category-item">
                <a href="#" @click="handleCategoryFilter(category)">{{ category }}</a>
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
const employmentNewsList = ref([])
const latestNews = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const totalPages = ref(1)
const searchKeyword = ref('')
const selectedCategory = ref('')
const categories = ['就业分析', '招聘信息', '就业指导', '政策解读', '职场经验']

// 加载数据
onMounted(() => {
  fetchEmploymentNews()
  fetchLatestNews()
})

// 获取就业资讯列表
const fetchEmploymentNews = async () => {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      category: selectedCategory.value || undefined
    }
    
    const data = await employmentApi.getEmploymentNews(params)
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    employmentNewsList.value = Array.isArray(data.data?.content) ? data.data.content : 
                               Array.isArray(data.data?.list) ? data.data.list : 
                               Array.isArray(data.data) ? data.data : []
    // 支持多种分页数据结构：total/totalElements, pages/totalPages
    total.value = data.data?.total || data.data?.totalElements || 0
    totalPages.value = data.data?.pages || data.data?.totalPages || 1
  } catch (error) {
    console.error('获取就业资讯列表失败:', error)
    // 模拟数据
    employmentNewsList.value = [
      { id: 1, title: '2025年全国高校毕业生就业形势分析', publishDate: '2025-11-20 00:00:00', source: '教育部', category: '就业分析' },
      { id: 2, title: '春季校园招聘会即将举行', publishDate: '2025-11-18 00:00:00', source: '就业指导中心', category: '招聘信息' },
      { id: 3, title: '简历制作技巧与注意事项', publishDate: '2025-11-15 00:00:00', source: '就业指导中心', category: '就业指导' },
      { id: 4, title: '2025年大学生就业政策解读', publishDate: '2025-11-10 00:00:00', source: '人力资源社会保障部', category: '政策解读' },
      { id: 5, title: '面试技巧与礼仪', publishDate: '2025-11-05 00:00:00', source: '就业指导中心', category: '就业指导' }
    ]
    total.value = 15
    totalPages.value = 2
  }
}

// 获取最新资讯
const fetchLatestNews = async () => {
  try {
    const data = await employmentApi.getEmploymentNews({ page: 1, pageSize: 5 })
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    latestNews.value = Array.isArray(data.data?.content) ? data.data.content : 
                      Array.isArray(data.data?.list) ? data.data.list : 
                      Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取最新资讯失败:', error)
    latestNews.value = [
      { id: 1, title: '2025年全国高校毕业生就业形势分析' },
      { id: 2, title: '春季校园招聘会即将举行' },
      { id: 3, title: '简历制作技巧与注意事项' },
      { id: 4, title: '2025年大学生就业政策解读' },
      { id: 5, title: '面试技巧与礼仪' }
    ]
  }
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/employment/${id}`)
}

// 处理分页
const handlePageChange = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
  fetchEmploymentNews()
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchEmploymentNews()
}

// 处理分类筛选
const handleCategoryFilter = (category) => {
  selectedCategory.value = category
  currentPage.value = 1
  fetchEmploymentNews()
}
</script>

<style scoped>
.employment-news-list {
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

.news-list {
  margin-bottom: 30px;
}

.news-item {
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.news-item:last-child {
  border-bottom: none;
}

.news-title {
  font-size: 18px;
  margin-bottom: 10px;
}

.news-title a {
  color: #333;
  text-decoration: none;
}

.news-title a:hover {
  color: #007bff;
  text-decoration: underline;
}

.news-meta {
  font-size: 14px;
  color: #666;
  display: flex;
  gap: 20px;
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

.latest-news-item {
  margin-bottom: 10px;
}

.latest-news-item a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.6;
}

.latest-news-item a:hover {
  color: #007bff;
}

.category-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.category-item a {
  color: #666;
  text-decoration: none;
  font-size: 14px;
  display: block;
  padding: 5px 0;
}

.category-item a:hover {
  color: #007bff;
  padding-left: 5px;
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
}
</style>