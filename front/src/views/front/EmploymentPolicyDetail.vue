<template>
  <div class="employment-policy-detail">
    <!-- 导航栏 -->
    <Navbar />
    
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>就业服务・职业发展</h2>
    </div>

    <!-- 主要内容区域 -->
    <div class="container main-content">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading">加载中...</div>
      
      <!-- 错误信息 -->
      <div v-else-if="error" class="error-message">{{ error }}</div>
      
      <!-- 政策详情 -->
      <div v-else-if="policyDetail" class="detail-content">
        <!-- 政策头部信息 -->
        <div class="policy-header">
          <h1 class="policy-title">{{ policyDetail.title }}</h1>
          <div class="policy-meta">
            <span class="meta-item">
              <i class="icon-date"></i>
              {{ policyDetail.publishDate }}
            </span>
            <span class="meta-item">
              <i class="icon-view"></i>
              {{ policyDetail.viewCount || 0 }}次浏览
            </span>
          </div>
        </div>
        
        <!-- 政策内容 -->
        <div class="policy-content" v-html="policyDetail.content"></div>
        
        <!-- 相关政策 -->
        <div class="related-policies">
          <div class="section-header">
            <h3>相关政策</h3>
          </div>
          <div class="related-list">
            <div v-for="item in relatedPoliciesList" :key="item.id" class="related-item">
              <a href="#" @click="goToPolicyDetail(item.id)">{{ item.title }}</a>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧边栏 -->
      <div class="sidebar">
        <!-- 最新政策 -->
        <div class="latest-policies">
          <div class="section-header">
            <h3>最新政策</h3>
          </div>
          <div class="policy-list">
            <div v-for="item in latestPoliciesList" :key="item.id" class="policy-item">
              <a href="#" @click="goToPolicyDetail(item.id)">{{ item.title }}</a>
            </div>
          </div>
        </div>
        
        <!-- 热门资讯 -->
        <div class="hot-news">
          <div class="section-header">
            <h3>热门资讯</h3>
          </div>
          <div class="news-list">
            <div v-for="item in hotNewsList" :key="item.id" class="news-item">
              <a href="#" @click="goToDetail(item.id)">{{ item.title }}</a>
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
import employmentApi from '@/api/employment.js'

const router = useRouter()
const route = useRoute()

// 数据
const loading = ref(true)
const error = ref(null)
const policyDetail = ref(null)
const relatedPoliciesList = ref([])
const latestPoliciesList = ref([])
const hotNewsList = ref([])

// 加载数据
onMounted(() => {
  fetchPolicyDetail()
  fetchRelatedPolicies()
  fetchLatestPolicies()
  fetchHotNews()
})

// 获取就业政策详情
const fetchPolicyDetail = async () => {
  try {
    // 假设从路由参数获取ID
    const id = route.params.id || 1
    const data = await employmentApi.getEmploymentPolicyDetail(id)
    policyDetail.value = data.data || {}
  } catch (error) {
    console.error('获取政策详情失败:', error)
    // 错误处理：可以显示错误提示或使用默认值
    policyDetail.value = {
      id: id,
      title: '政策详情获取失败',
      content: '<p>抱歉，无法获取该政策的详细信息。</p>'
    }
  } finally {
    loading.value = false
  }
}

// 获取相关政策
const fetchRelatedPolicies = async () => {
  try {
    const data = await employmentApi.getEmploymentPolicies({ page: 1, pageSize: 5 })
    relatedPoliciesList.value = data.data?.list || []
  } catch (error) {
    console.error('获取相关政策失败:', error)
    // 模拟数据
    relatedPoliciesList.value = [
      { id: 2, title: '高校毕业生就业创业扶持政策', publishDate: '2023-11-15 00:00:00' },
      { id: 3, title: '基层就业项目实施办法', publishDate: '2023-11-01 00:00:00' },
      { id: 4, title: '大学生征兵政策解读', publishDate: '2023-10-15 00:00:00' },
      { id: 5, title: '留学归国人员就业服务政策', publishDate: '2023-10-01 00:00:00' }
    ]
  }
}

// 获取最新政策
const fetchLatestPolicies = async () => {
  try {
    const data = await employmentApi.getEmploymentPolicies({ page: 1, pageSize: 5 })
    latestPoliciesList.value = data.data?.list || []
  } catch (error) {
    console.error('获取最新政策失败:', error)
    // 模拟数据
    latestPoliciesList.value = [
      { id: 1, title: '2023年大学生就业政策解读', publishDate: '2023-12-01 00:00:00' },
      { id: 2, title: '高校毕业生就业创业扶持政策', publishDate: '2023-11-15 00:00:00' },
      { id: 3, title: '基层就业项目实施办法', publishDate: '2023-11-01 00:00:00' },
      { id: 4, title: '大学生征兵政策解读', publishDate: '2023-10-15 00:00:00' },
      { id: 5, title: '留学归国人员就业服务政策', publishDate: '2023-10-01 00:00:00' }
    ]
  }
}

// 获取热门资讯
const fetchHotNews = async () => {
  try {
    const data = await employmentApi.getEmploymentNews({ page: 1, pageSize: 5 })
    hotNewsList.value = data.data?.list || []
  } catch (error) {
    console.error('获取热门资讯失败:', error)
    // 模拟数据
    hotNewsList.value = [
      { id: 1, title: '2025年全国高校毕业生就业形势分析', publishDate: '2025-11-20 00:00:00', source: '教育部', category: '就业分析' },
      { id: 2, title: '计算机学院2025届毕业生就业质量报告发布', publishDate: '2025-11-15 00:00:00', source: '计算机学院教师第三党支部', category: '就业报告' },
      { id: 3, title: '2025年秋季校园招聘会即将举行', publishDate: '2025-11-10 00:00:00', source: '就业指导中心', category: '招聘活动' },
      { id: 4, title: '教育部出台新政策支持高校毕业生就业创业', publishDate: '2025-10-28 00:00:00', source: '教育部', category: '政策解读' },
      { id: 5, title: '2025届毕业生就业指导系列讲座安排', publishDate: '2025-10-20 00:00:00', source: '就业指导中心', category: '就业指导' }
    ]
  }
}

// 跳转到政策详情页
const goToPolicyDetail = (id) => {
  router.push(`/employment/policy/${id}`)
}

// 跳转到资讯详情页
const goToDetail = (id) => {
  router.push(`/employment/${id}`)
}
</script>

<style scoped>
.employment-policy-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.page-header {
  background-color: #c72c41;
  color: white;
  padding: 40px 0;
  text-align: center;
}

.page-header h2 {
  margin: 0;
  font-size: 28px;
  font-weight: bold;
}

/* 主要内容区域 */
.main-content {
  display: grid;
  grid-template-columns: 3fr 1fr;
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px 50px;
}

/* 加载状态 */
.loading {
  text-align: center;
  padding: 50px 0;
  font-size: 18px;
  color: #666;
}

/* 错误信息 */
.error-message {
  text-align: center;
  padding: 50px 0;
  font-size: 18px;
  color: #c72c41;
}

/* 详情内容 */
.detail-content {
  background-color: white;
  padding: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 政策头部信息 */
.policy-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.policy-title {
  margin: 0 0 20px 0;
  font-size: 28px;
  color: #333;
  line-height: 1.4;
}

.policy-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #999;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* 政策内容 */
.policy-content {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 30px;
}

.policy-content p {
  margin-bottom: 20px;
}

.policy-content h2 {
  font-size: 22px;
  margin: 30px 0 20px 0;
  color: #c72c41;
}

.policy-content h3 {
  font-size: 20px;
  margin: 25px 0 15px 0;
  color: #c72c41;
}

/* 相关政策 */
.related-policies {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.section-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #c72c41;
}

.section-header h3 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.related-item a {
  color: #333;
  text-decoration: none;
  font-size: 16px;
  line-height: 1.5;
  display: block;
  padding: 5px 0;
  position: relative;
  padding-left: 20px;
}

.related-item a::before {
  content: '▶';
  color: #c72c41;
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  font-size: 12px;
}

.related-item a:hover {
  color: #c72c41;
  text-decoration: underline;
}

/* 右侧边栏 */
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

/* 最新政策 */
.latest-policies {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.policy-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.policy-item a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.5;
  display: block;
  padding: 5px 0;
  transition: all 0.3s ease;
}

.policy-item a:hover {
  color: #c72c41;
  text-decoration: underline;
  padding-left: 5px;
}

/* 热门资讯 */
.hot-news {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.news-item a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.5;
  display: block;
  padding: 5px 0;
  transition: all 0.3s ease;
}

.news-item a:hover {
  color: #c72c41;
  text-decoration: underline;
  padding-left: 5px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    grid-template-columns: 1fr;
  }
  
  .policy-title {
    font-size: 24px;
  }
  
  .policy-meta {
    flex-wrap: wrap;
    gap: 10px;
  }
}
</style>