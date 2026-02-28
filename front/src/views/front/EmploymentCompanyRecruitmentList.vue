<template>
  <div class="employment-company-recruitment-list">
    <!-- 页面顶部导航 -->
    <div class="page-header">
      <div class="container">
        <h1>企业招聘信息</h1>
        <div class="breadcrumb">
          <a href="/">首页</a> &gt; <a href="/employment">就业服务</a> &gt; 企业招聘信息
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
              <input type="text" v-model="searchKeyword" placeholder="搜索职位或公司..." />
              <button @click="handleSearch">搜索</button>
            </div>
            <div class="filter-row">
              <div class="filter-group">
                <label>行业：</label>
                <select v-model="selectedIndustry" @change="handleFilter">
                  <option value="">全部行业</option>
                  <option value="互联网">互联网</option>
                  <option value="金融">金融</option>
                  <option value="教育">教育</option>
                  <option value="制造业">制造业</option>
                  <option value="医疗健康">医疗健康</option>
                  <option value="房地产">房地产</option>
                  <option value="零售">零售</option>
                </select>
              </div>
              <div class="filter-group">
                <label>地点：</label>
                <select v-model="selectedLocation" @change="handleFilter">
                  <option value="">全部地点</option>
                  <option value="北京">北京</option>
                  <option value="上海">上海</option>
                  <option value="广州">广州</option>
                  <option value="深圳">深圳</option>
                  <option value="杭州">杭州</option>
                  <option value="成都">成都</option>
                </select>
              </div>
              <div class="filter-group">
                <label>薪资：</label>
                <select v-model="selectedSalary" @change="handleFilter">
                  <option value="">全部薪资</option>
                  <option value="5k以下">5k以下</option>
                  <option value="5k-10k">5k-10k</option>
                  <option value="10k-20k">10k-20k</option>
                  <option value="20k-30k">20k-30k</option>
                  <option value="30k以上">30k以上</option>
                </select>
              </div>
            </div>
          </div>

          <!-- 招聘信息列表 -->
          <div class="recruitment-list">
            <div v-for="recruitment in recruitmentList" :key="recruitment.id" class="recruitment-item">
              <div class="company-info">
                <div class="company-logo">
                  <i class="icon-company"></i>
                </div>
                <div class="company-name">{{ recruitment.companyName }}</div>
                <div class="company-industry">{{ recruitment.industry }}</div>
              </div>
              <div class="position-info">
                <div class="position-title">
                  <a href="#" @click="goToDetail(recruitment.id)">{{ recruitment.positionName }}</a>
                </div>
                <div class="position-meta">
                  <span class="salary">{{ recruitment.salary }}</span>
                  <span class="location">{{ recruitment.location }}</span>
                  <span class="experience">{{ recruitment.experience }}</span>
                  <span class="education">{{ recruitment.education }}</span>
                </div>
                <div class="position-tags">
                  <span v-for="(tag, index) in recruitment.tags" :key="index" class="tag">{{ tag }}</span>
                </div>
              </div>
              <div class="recruitment-meta">
                <span class="publish-time">{{ recruitment.publishDate }}</span>
                <button @click="goToDetail(recruitment.id)" class="apply-btn">查看详情</button>
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
          <!-- 热门职位 -->
          <div class="sidebar-section">
            <h3>热门职位</h3>
            <div class="hot-positions">
              <div v-for="position in hotPositions" :key="position.id" class="hot-position-item">
                <a href="#" @click="goToDetail(position.id)">{{ position.positionName }}</a>
                <div class="position-info">
                  <span class="company">{{ position.companyName }}</span>
                  <span class="salary">{{ position.salary }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 最新招聘 -->
          <div class="sidebar-section">
            <h3>最新招聘</h3>
            <div class="latest-recruitments">
              <div v-for="recruitment in latestRecruitments" :key="recruitment.id" class="latest-item">
                <a href="#" @click="goToDetail(recruitment.id)">{{ recruitment.positionName }}</a>
                <span class="recruitment-time">{{ recruitment.publishDate }}</span>
              </div>
            </div>
          </div>

          <!-- 行业分类 -->
          <div class="sidebar-section">
            <h3>行业分类</h3>
            <div class="industry-categories">
              <div v-for="category in industryCategories" :key="category.name" class="category-item">
                <a href="#" @click="handleIndustryFilter(category.name)">
                  <span class="category-name">{{ category.name }}</span>
                  <span class="category-count">({{ category.count }})</span>
                </a>
              </div>
            </div>
          </div>

          <!-- 薪资分布 -->
          <div class="sidebar-section">
            <h3>薪资分布</h3>
            <div class="salary-distribution">
              <div v-for="salary in salaryRanges" :key="salary.range" class="salary-item">
                <a href="#" @click="handleSalaryFilter(salary.range)">
                  <span class="salary-range">{{ salary.range }}</span>
                  <span class="salary-count">({{ salary.count }})</span>
                </a>
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
import axios from 'axios'

const router = useRouter()
const recruitmentList = ref([])
const hotPositions = ref([])
const latestRecruitments = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const totalPages = ref(1)
const searchKeyword = ref('')
const selectedIndustry = ref('')
const selectedLocation = ref('')
const selectedSalary = ref('')

// 行业分类数据
const industryCategories = [
  { name: '互联网', count: 45 },
  { name: '金融', count: 32 },
  { name: '教育', count: 28 },
  { name: '制造业', count: 22 },
  { name: '医疗健康', count: 18 },
  { name: '房地产', count: 15 },
  { name: '零售', count: 12 }
]

// 薪资范围数据
const salaryRanges = [
  { range: '5k以下', count: 12 },
  { range: '5k-10k', count: 35 },
  { range: '10k-20k', count: 48 },
  { range: '20k-30k', count: 22 },
  { range: '30k以上', count: 15 }
]

// 加载数据
onMounted(() => {
  fetchRecruitmentList()
  fetchHotPositions()
  fetchLatestRecruitments()
})

// 获取招聘信息列表
const fetchRecruitmentList = async () => {
  try {
    // 打印环境变量信息
    console.log('=== 环境变量信息 ===')
    console.log('VITE_API_BASE_URL:', import.meta.env.VITE_API_BASE_URL)
    console.log('是否存在环境变量:', 'VITE_API_BASE_URL' in import.meta.env)
    
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchKeyword.value || undefined,
      industry: selectedIndustry.value || undefined,
      location: selectedLocation.value || undefined,
      salary: selectedSalary.value || undefined
    }
    
    console.log('=== 开始获取招聘信息列表 ===')
    console.log('请求参数:', params)
    
    // 使用employmentApi，查看完整响应
    console.log('准备调用employmentApi.getCompanyRecruitment')
    const data = await employmentApi.getCompanyRecruitment(params)
    console.log('调用employmentApi.getCompanyRecruitment成功')
    console.log('接口响应数据:', data)
    console.log('接口响应数据类型:', typeof data)
    console.log('接口响应数据结构:', Object.keys(data))
    
    // 处理可能的响应数据结构：code, message, data格式
    console.log('=== 处理响应数据结构 ===')
    console.log('  - 是否有code字段:', 'code' in data)
    console.log('  - code值:', data.code)
    console.log('  - 是否有message字段:', 'message' in data)
    console.log('  - 是否有data字段:', 'data' in data)
    console.log('  - data类型:', typeof data.data)
    if (data.data) {
      console.log('  - data结构:', Object.keys(data.data))
    }
    
    const responseData = data.code === 200 && data.data ? data.data : data
    console.log('处理后的响应数据:', responseData)
    console.log('处理后的响应数据类型:', typeof responseData)
    if (typeof responseData === 'object' && responseData !== null) {
      console.log('处理后的响应数据结构:', Object.keys(responseData))
    }
    
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    console.log('=== 解析数据列表 ===')
    console.log('  - responseData?.content:', responseData?.content)
    console.log('  - Array.isArray(responseData?.content):', Array.isArray(responseData?.content))
    if (Array.isArray(responseData?.content)) {
      console.log('  - content长度:', responseData.content.length)
    }
    
    console.log('  - responseData?.list:', responseData?.list)
    console.log('  - Array.isArray(responseData?.list):', Array.isArray(responseData?.list))
    if (Array.isArray(responseData?.list)) {
      console.log('  - list长度:', responseData.list.length)
    }
    
    console.log('  - Array.isArray(responseData):', Array.isArray(responseData))
    if (Array.isArray(responseData)) {
      console.log('  - responseData长度:', responseData.length)
    }
    
    recruitmentList.value = Array.isArray(responseData?.content) ? responseData.content : 
                           Array.isArray(responseData?.list) ? responseData.list : 
                           Array.isArray(responseData) ? responseData : []
    // 支持多种分页数据结构：total/totalElements, pages/totalPages
    total.value = responseData?.total || responseData?.totalElements || 0
    totalPages.value = responseData?.pages || responseData?.totalPages || 1
    
    console.log('=== 最终处理结果 ===')
    console.log('  - recruitmentList.value:', recruitmentList.value)
    console.log('  - recruitmentList.value.length:', recruitmentList.value.length)
    console.log('  - total.value:', total.value)
    console.log('  - totalPages.value:', totalPages.value)
    console.log('=== 获取招聘信息列表结束 ===')
  } catch (error) {
    console.error('=== 获取企业招聘信息列表失败 ===')
    console.error('错误类型:', error.name)
    console.error('错误消息:', error.message)
    console.error('错误堆栈:', error.stack)
    if (error.response) {
      console.error('响应错误:', error.response)
      console.error('响应状态码:', error.response.status)
      console.error('响应数据:', error.response.data)
    } else if (error.request) {
      console.error('请求错误:', error.request)
      console.error('请求错误详情:', error.request.status, error.request.statusText)
    } else {
      console.error('请求配置错误:', error.message)
    }
    // 模拟数据
    recruitmentList.value = [
      {
        id: 1,
        companyName: '阿里巴巴集团',
        industry: '互联网',
        positionName: '前端开发工程师',
        salary: '20k-40k',
        location: '杭州',
        experience: '3-5年',
        education: '本科',
        tags: ['前端', 'Vue', 'React', 'JavaScript'],
        publishDate: '2025-11-20'
      },
      {
        id: 2,
        companyName: '腾讯科技',
        industry: '互联网',
        positionName: 'Java后端开发工程师',
        salary: '25k-45k',
        location: '深圳',
        experience: '3-5年',
        education: '本科',
        tags: ['后端', 'Java', 'Spring', 'MySQL'],
        publishDate: '2025-11-19'
      },
      {
        id: 3,
        companyName: '字节跳动',
        industry: '互联网',
        positionName: '产品经理',
        salary: '15k-35k',
        location: '北京',
        experience: '1-3年',
        education: '本科',
        tags: ['产品', '需求分析', '用户研究'],
        publishDate: '2025-11-18'
      },
      {
        id: 4,
        companyName: '招商银行',
        industry: '金融',
        positionName: '金融科技岗',
        salary: '18k-30k',
        location: '上海',
        experience: '不限',
        education: '硕士',
        tags: ['金融', '科技', '数据分析'],
        publishDate: '2025-11-17'
      },
      {
        id: 5,
        companyName: '华为技术',
        industry: '制造业',
        positionName: '通信工程师',
        salary: '20k-40k',
        location: '深圳',
        experience: '3-5年',
        education: '本科',
        tags: ['通信', '5G', '网络'],
        publishDate: '2025-11-16'
      },
      {
        id: 6,
        companyName: '京东集团',
        industry: '互联网',
        positionName: '运营专员',
        salary: '10k-20k',
        location: '北京',
        experience: '1-3年',
        education: '本科',
        tags: ['运营', '电商', '用户运营'],
        publishDate: '2025-11-15'
      },
      {
        id: 7,
        companyName: '网易集团',
        industry: '互联网',
        positionName: '游戏开发工程师',
        salary: '25k-50k',
        location: '杭州',
        experience: '3-5年',
        education: '本科',
        tags: ['游戏', 'C++', 'Unity'],
        publishDate: '2025-11-14'
      },
      {
        id: 8,
        companyName: '百度在线',
        industry: '互联网',
        positionName: '人工智能工程师',
        salary: '30k-60k',
        location: '北京',
        experience: '3-5年',
        education: '硕士',
        tags: ['AI', '机器学习', 'Python'],
        publishDate: '2025-11-13'
      },
      {
        id: 9,
        companyName: '美团点评',
        industry: '互联网',
        positionName: '算法工程师',
        salary: '25k-50k',
        location: '北京',
        experience: '1-3年',
        education: '硕士',
        tags: ['算法', '数据挖掘', '推荐系统'],
        publishDate: '2025-11-12'
      },
      {
        id: 10,
        companyName: '滴滴出行',
        industry: '互联网',
        positionName: '测试工程师',
        salary: '15k-30k',
        location: '北京',
        experience: '1-3年',
        education: '本科',
        tags: ['测试', '自动化', '性能测试'],
        publishDate: '2025-11-11'
      }
    ]
    total.value = 68
    totalPages.value = 7
  }
}

// 获取热门职位
const fetchHotPositions = async () => {
  try {
    console.log('请求热门职位参数:', { page: 1, pageSize: 5, sortBy: 'popularity' })
    const data = await employmentApi.getCompanyRecruitment({ page: 1, pageSize: 5, sortBy: 'popularity' })
    console.log('热门职位接口响应:', data)
    
    // 处理可能的响应数据结构：code, message, data格式
    const responseData = data.code === 200 && data.data ? data.data : data
    console.log('处理后的热门职位响应数据:', responseData)
    
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    hotPositions.value = Array.isArray(responseData?.content) ? responseData.content : 
                        Array.isArray(responseData?.list) ? responseData.list : 
                        Array.isArray(responseData) ? responseData : []
    console.log('处理后的热门职位数据:', hotPositions.value)
  } catch (error) {
    console.error('获取热门职位失败:', error)
    hotPositions.value = [
      { id: 1, positionName: '前端开发工程师', companyName: '阿里巴巴集团', salary: '20k-40k' },
      { id: 2, positionName: 'Java后端开发工程师', companyName: '腾讯科技', salary: '25k-45k' },
      { id: 8, positionName: '人工智能工程师', companyName: '百度在线', salary: '30k-60k' },
      { id: 7, positionName: '游戏开发工程师', companyName: '网易集团', salary: '25k-50k' },
      { id: 9, positionName: '算法工程师', companyName: '美团点评', salary: '25k-50k' }
    ]
  }
}

// 获取最新招聘
const fetchLatestRecruitments = async () => {
  try {
    console.log('请求最新招聘参数:', { page: 1, pageSize: 5 })
    const data = await employmentApi.getCompanyRecruitment({ page: 1, pageSize: 5 })
    console.log('最新招聘接口响应:', data)
    
    // 处理可能的响应数据结构：code, message, data格式
    const responseData = data.code === 200 && data.data ? data.data : data
    console.log('处理后的最新招聘响应数据:', responseData)
    
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    latestRecruitments.value = Array.isArray(responseData?.content) ? responseData.content : 
                              Array.isArray(responseData?.list) ? responseData.list : 
                              Array.isArray(responseData) ? responseData : []
    console.log('处理后的最新招聘数据:', latestRecruitments.value)
  } catch (error) {
    console.error('获取最新招聘失败:', error)
    latestRecruitments.value = [
      { id: 1, positionName: '前端开发工程师', publishDate: '2025-11-20' },
      { id: 2, positionName: 'Java后端开发工程师', publishDate: '2025-11-19' },
      { id: 3, positionName: '产品经理', publishDate: '2025-11-18' },
      { id: 4, positionName: '金融科技岗', publishDate: '2025-11-17' },
      { id: 5, positionName: '通信工程师', publishDate: '2025-11-16' }
    ]
  }
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/employment/company-recruitment/${id}`)
}

// 处理分页
const handlePageChange = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
  fetchRecruitmentList()
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchRecruitmentList()
}

// 处理筛选
const handleFilter = () => {
  currentPage.value = 1
  fetchRecruitmentList()
}

// 处理行业筛选
const handleIndustryFilter = (industry) => {
  selectedIndustry.value = industry
  currentPage.value = 1
  fetchRecruitmentList()
}

// 处理薪资筛选
const handleSalaryFilter = (salary) => {
  selectedSalary.value = salary
  currentPage.value = 1
  fetchRecruitmentList()
}
</script>

<style scoped>
.employment-company-recruitment-list {
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
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.search-box {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
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

.filter-row {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-group label {
  color: #666;
  font-size: 14px;
}

.filter-group select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.recruitment-list {
  margin-bottom: 30px;
}

.recruitment-item {
  display: flex;
  gap: 20px;
  padding: 20px;
  border-bottom: 1px solid #eee;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 15px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.02);
}

.recruitment-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.company-info {
  width: 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.company-logo {
  width: 80px;
  height: 80px;
  background-color: #f0f0f0;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
}

.icon-company {
  font-size: 40px;
  color: #007bff;
}

.company-name {
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.company-industry {
  color: #666;
  font-size: 12px;
}

.position-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.position-title {
  font-size: 18px;
  font-weight: bold;
}

.position-title a {
  color: #333;
  text-decoration: none;
}

.position-title a:hover {
  color: #007bff;
  text-decoration: underline;
}

.position-meta {
  display: flex;
  gap: 20px;
  color: #666;
  font-size: 14px;
}

.salary {
  color: #dc3545;
  font-weight: bold;
}

.position-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  background-color: #e9ecef;
  color: #666;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.recruitment-meta {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-end;
  gap: 10px;
}

.publish-time {
  color: #999;
  font-size: 12px;
}

.apply-btn {
  padding: 8px 16px;
  background-color: #28a745;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.apply-btn:hover {
  background-color: #218838;
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

.hot-positions, .latest-recruitments {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.hot-position-item, .latest-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.hot-position-item:last-child, .latest-item:last-child {
  border-bottom: none;
}

.hot-position-item a, .latest-item a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.5;
}

.hot-position-item a:hover, .latest-item a:hover {
  color: #007bff;
}

.position-info {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #666;
}

.recruitment-time {
  font-size: 12px;
  color: #999;
}

.industry-categories, .salary-distribution {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.category-item, .salary-item {
  padding: 5px 0;
}

.category-item a, .salary-item a {
  color: #666;
  text-decoration: none;
  font-size: 14px;
  display: flex;
  justify-content: space-between;
}

.category-item a:hover, .salary-item a:hover {
  color: #007bff;
}

.category-count, .salary-count {
  color: #999;
}

@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
  }
  
  .recruitment-item {
    flex-direction: column;
    gap: 15px;
  }
  
  .company-info {
    width: 100%;
    flex-direction: row;
    align-items: center;
    gap: 15px;
    text-align: left;
  }
  
  .recruitment-meta {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
  
  .filter-row {
    flex-direction: column;
    gap: 15px;
  }
}
</style>