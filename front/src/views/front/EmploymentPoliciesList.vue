<template>
  <div class="employment-policies-list">
    <!-- 页面顶部导航 -->
    <div class="page-header">
      <div class="container">
        <h1>就业政策</h1>
        <div class="breadcrumb">
          <a href="/">首页</a> &gt; <a href="/employment">就业服务</a> &gt; 就业政策
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
              <input type="text" v-model="searchKeyword" placeholder="搜索就业政策..." />
              <button @click="handleSearch">搜索</button>
            </div>
          </div>

          <!-- 政策列表 -->
          <div class="policies-list">
            <div v-for="policy in employmentPoliciesList" :key="policy.id" class="policy-item">
              <div class="policy-title">
                <a href="#" @click="goToDetail(policy.id)">{{ policy.title }}</a>
              </div>
              <div class="policy-meta">
                <span class="policy-date">{{ policy.publishDate }}</span>
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
          <!-- 最新政策 -->
          <div class="sidebar-section">
            <h3>最新政策</h3>
            <div class="latest-policies">
              <div v-for="policy in latestPolicies" :key="policy.id" class="latest-policy-item">
                <a href="#" @click="goToDetail(policy.id)">{{ policy.title }}</a>
              </div>
            </div>
          </div>

          <!-- 政策分类 -->
          <div class="sidebar-section">
            <h3>政策分类</h3>
            <div class="category-list">
              <div v-for="category in categories" :key="category" class="category-item">
                <a href="#" @click="handleCategoryFilter(category)">{{ category }}</a>
              </div>
            </div>
          </div>

          <!-- 政策解读 -->
          <div class="sidebar-section">
            <h3>政策解读</h3>
            <div class="interpretations">
              <div v-for="item in interpretations" :key="item.id" class="interpretation-item">
                <a href="#" @click="goToDetail(item.id)">{{ item.title }}</a>
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
const employmentPoliciesList = ref([])
const latestPolicies = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const totalPages = ref(1)
const searchKeyword = ref('')
const selectedCategory = ref('')
const categories = ['国家政策', '地方政策', '学校政策', '就业补贴', '创业扶持', '基层就业']
const interpretations = ref([])

// 加载数据
onMounted(() => {
  fetchEmploymentPolicies()
  fetchLatestPolicies()
  fetchInterpretations()
})

// 获取就业政策列表
const fetchEmploymentPolicies = async () => {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    const data = await employmentApi.getEmploymentPolicies(params)
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    employmentPoliciesList.value = Array.isArray(data.data?.content) ? data.data.content : 
                                   Array.isArray(data.data?.list) ? data.data.list : 
                                   Array.isArray(data.data) ? data.data : []
    // 支持多种分页数据结构：total/totalElements, pages/totalPages
    total.value = data.data?.total || data.data?.totalElements || 0
    totalPages.value = data.data?.pages || data.data?.totalPages || 1
  } catch (error) {
    console.error('获取就业政策列表失败:', error)
    // 模拟数据
    employmentPoliciesList.value = [
      { id: 1, title: '2025年大学生就业政策解读', publishDate: '2025-11-20 00:00:00' },
      { id: 2, title: '高校毕业生基层就业扶持政策', publishDate: '2025-11-15 00:00:00' },
      { id: 3, title: '大学生创业贷款贴息政策实施细则', publishDate: '2025-11-10 00:00:00' },
      { id: 4, title: '2025届毕业生就业服务工作方案', publishDate: '2025-11-05 00:00:00' },
      { id: 5, title: '大学生就业见习管理办法', publishDate: '2025-10-30 00:00:00' },
      { id: 6, title: '高校毕业生就业创业补贴申请指南', publishDate: '2025-10-25 00:00:00' },
      { id: 7, title: '研究生就业政策调整通知', publishDate: '2025-10-20 00:00:00' },
      { id: 8, title: '2025年征兵政策与就业优惠措施', publishDate: '2025-10-15 00:00:00' },
      { id: 9, title: '大学生就业援助计划实施方案', publishDate: '2025-10-10 00:00:00' },
      { id: 10, title: '高校就业指导服务规范', publishDate: '2025-10-05 00:00:00' }
    ]
    total.value = 35
    totalPages.value = 4
  }
}

// 获取最新政策
const fetchLatestPolicies = async () => {
  try {
    const data = await employmentApi.getEmploymentPolicies({ page: 1, pageSize: 5 })
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    latestPolicies.value = Array.isArray(data.data?.content) ? data.data.content : 
                           Array.isArray(data.data?.list) ? data.data.list : 
                           Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取最新政策失败:', error)
    latestPolicies.value = [
      { id: 1, title: '2025年大学生就业政策解读' },
      { id: 2, title: '高校毕业生基层就业扶持政策' },
      { id: 3, title: '大学生创业贷款贴息政策实施细则' },
      { id: 4, title: '2025届毕业生就业服务工作方案' },
      { id: 5, title: '大学生就业见习管理办法' }
    ]
  }
}

// 获取政策解读
const fetchInterpretations = async () => {
  try {
    const data = await employmentApi.getEmploymentNews({ page: 1, pageSize: 5, category: '政策解读' })
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    interpretations.value = Array.isArray(data.data?.content) ? data.data.content : 
                           Array.isArray(data.data?.list) ? data.data.list : 
                           Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取政策解读失败:', error)
    interpretations.value = [
      { id: 11, title: '如何申请大学生就业补贴？' },
      { id: 12, title: '基层就业项目有哪些优势？' },
      { id: 13, title: '创业贷款申请流程详解' },
      { id: 14, title: '就业见习与正式工作的区别' },
      { id: 15, title: '毕业生档案如何处理？' }
    ]
  }
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/employment/policy/${id}`)
}

// 处理分页
const handlePageChange = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
  fetchEmploymentPolicies()
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchEmploymentPolicies()
}

// 处理分类筛选
const handleCategoryFilter = (category) => {
  selectedCategory.value = category
  currentPage.value = 1
  fetchEmploymentPolicies()
}
</script>

<style scoped>
.employment-policies-list {
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

.policies-list {
  margin-bottom: 30px;
}

.policy-item {
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.policy-item:last-child {
  border-bottom: none;
}

.policy-title {
  font-size: 18px;
  margin-bottom: 10px;
}

.policy-title a {
  color: #333;
  text-decoration: none;
}

.policy-title a:hover {
  color: #007bff;
  text-decoration: underline;
}

.policy-meta {
  font-size: 14px;
  color: #666;
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

.latest-policy-item {
  margin-bottom: 10px;
}

.latest-policy-item a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.6;
}

.latest-policy-item a:hover {
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

.interpretations {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.interpretation-item a {
  color: #666;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.6;
}

.interpretation-item a:hover {
  color: #007bff;
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