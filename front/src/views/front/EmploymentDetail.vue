<template>
  <div class="employment-detail">
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
      
      <!-- 就业信息详情 -->
      <div v-else-if="employmentDetail" class="detail-content">
        <!-- 文章头部信息 -->
        <div class="article-header">
          <h1 class="article-title">{{ employmentDetail.title }}</h1>
          <div class="article-meta">
            <span class="meta-item">
              <i class="icon-date"></i>
              {{ employmentDetail.publishDate }}
            </span>
            <span class="meta-item">
              <i class="icon-source"></i>
              {{ employmentDetail.source }}
            </span>
            <span class="meta-item">
              <i class="icon-view"></i>
              {{ employmentDetail.viewCount || 0 }}次浏览
            </span>
          </div>
        </div>
        
        <!-- 文章内容 -->
        <div class="article-content" v-html="employmentDetail.content"></div>
        
        <!-- 相关推荐 -->
        <div class="related-content">
          <div class="section-header">
            <h3>相关推荐</h3>
          </div>
          <div class="related-list">
            <div v-for="item in relatedContentList" :key="item.id" class="related-item">
              <a href="#" @click="goToDetail(item.id)">{{ item.title }}</a>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧边栏 -->
      <div class="sidebar">
        <!-- 最新资讯 -->
        <div class="latest-news">
          <div class="section-header">
            <h3>最新资讯</h3>
          </div>
          <div class="news-list">
            <div v-for="item in latestNewsList" :key="item.id" class="news-item">
              <a href="#" @click="goToDetail(item.id)">{{ item.title }}</a>
            </div>
          </div>
        </div>
        
        <!-- 热门企业 -->
        <div class="hot-companies">
          <div class="section-header">
            <h3>热门企业</h3>
          </div>
          <div class="company-list">
            <div v-for="item in hotCompaniesList" :key="item.id" class="company-item">
              <a :href="item.website">{{ item.companyName }}</a>
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
const employmentDetail = ref(null)
const relatedList = ref([])
const latestNews = ref([])
const hotCompanies = ref([])

// 加载数据
onMounted(() => {
  fetchEmploymentDetail()
  fetchRelatedContent()
  fetchLatestNews()
  fetchHotCompanies()
})

// 获取就业资讯详情
const fetchEmploymentDetail = async () => {
  try {
    // 假设从路由参数获取ID
    const id = route.params.id || 1
    const data = await employmentApi.getEmploymentDetail(id)
    employmentDetail.value = data.data || {}
  } catch (error) {
    console.error('获取就业资讯详情失败:', error)
    // 模拟数据
    employmentDetail.value = {
      id: 1,
      title: '2025届毕业生春季校园招聘会成功举办',
      publishDate: '2025-03-15 00:00:00',
      source: '就业指导中心',
      category: '就业活动',
      views: 1234,
      content: `
        <p>3月15日，我校2025届毕业生春季校园招聘会在体育馆成功举办。本次招聘会吸引了来自全国各地的200余家企业参加，提供就业岗位5000余个。</p>
        <p>招聘会现场气氛热烈，毕业生们积极与企业代表交流，了解岗位需求和企业发展情况。学校相关部门也在现场提供了就业指导、简历修改等服务。</p>
        <p>据统计，本次招聘会共有3000余名毕业生参加，初步达成就业意向的有1200余人。</p>
      `
    }
  }
}

// 获取相关内容
const fetchRelatedContent = async () => {
  try {
    // 假设从路由参数获取ID
    const id = route.params.id || 1
    const data = await employmentApi.getEmploymentNews({ page: 1, pageSize: 5 })
    relatedContentList.value = data.data?.list || []
  } catch (error) {
    console.error('获取相关内容失败:', error)
    // 模拟数据
    relatedContentList.value = [
      { id: 2, title: '2025年毕业生就业质量报告发布', publishDate: '2025-03-10 00:00:00', source: '就业指导中心', category: '就业报告' },
      { id: 3, title: '如何撰写一份优秀的简历', publishDate: '2025-03-05 00:00:00', source: '就业指导中心', category: '简历指导' },
      { id: 4, title: '面试技巧分享会即将举行', publishDate: '2025-03-01 00:00:00', source: '就业指导中心', category: '面试指导' },
      { id: 5, title: '2025年就业政策解读', publishDate: '2025-02-25 00:00:00', source: '就业指导中心', category: '政策解读' },
      { id: 6, title: '职场新人必备技能培训', publishDate: '2025-02-20 00:00:00', source: '就业指导中心', category: '职场发展' }
    ]
  }
}

// 获取最新资讯
const fetchLatestNews = async () => {
  try {
    const data = await employmentApi.getEmploymentNews({ page: 1, pageSize: 5 })
    latestNewsList.value = data.data?.list || []
  } catch (error) {
    console.error('获取最新资讯失败:', error)
    // 模拟数据
    latestNewsList.value = [
      { id: 7, title: '夏季校园招聘会报名开始', publishDate: '2025-03-20 00:00:00', source: '就业指导中心', category: '招聘活动' },
      { id: 8, title: '企业用人需求变化趋势分析', publishDate: '2025-03-18 00:00:00', source: '就业指导中心', category: '就业分析' },
      { id: 9, title: '毕业生创业扶持政策详解', publishDate: '2025-03-16 00:00:00', source: '就业指导中心', category: '政策解读' },
      { id: 10, title: '职业规划专题讲座', publishDate: '2025-03-14 00:00:00', source: '就业指导中心', category: '职业规划' },
      { id: 11, title: '就业信息系统使用指南', publishDate: '2025-03-12 00:00:00', source: '就业指导中心', category: '就业服务' }
    ]
  }
}

// 获取热门企业
const fetchHotCompanies = async () => {
  try {
    const data = await employmentApi.getCompanyRecruitment({ page: 1, pageSize: 5 })
    const recruitmentList = data.data?.list || []
    // 从招聘信息中提取企业信息
    const hotCompanies = recruitmentList.map(item => ({
      id: item.id,
      companyName: item.companyName || '未知企业',
      website: item.source || '#'
    }))
    hotCompaniesList.value = hotCompanies
  } catch (error) {
    console.error('获取热门企业失败:', error)
    // 模拟数据
    hotCompaniesList.value = [
      { id: 1, companyName: '字节跳动', website: 'https://www.bytedance.com' },
      { id: 2, companyName: '阿里巴巴', website: 'https://www.alibaba.com' },
      { id: 3, companyName: '腾讯科技', website: 'https://www.tencent.com' },
      { id: 4, companyName: '华为技术', website: 'https://www.huawei.com' },
      { id: 5, companyName: '百度在线', website: 'https://www.baidu.com' }
    ]
  }
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/employment/${id}`)
}
</script>

<style scoped>
.employment-detail {
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

/* 文章头部信息 */
.article-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.article-title {
  margin: 0 0 20px 0;
  font-size: 28px;
  color: #333;
  line-height: 1.4;
}

.article-meta {
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

/* 文章内容 */
.article-content {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 30px;
}

.article-content p {
  margin-bottom: 20px;
}

.article-content h2 {
  font-size: 22px;
  margin: 30px 0 20px 0;
  color: #c72c41;
}

.article-content h3 {
  font-size: 20px;
  margin: 25px 0 15px 0;
  color: #c72c41;
}

/* 相关推荐 */
.related-content {
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

/* 最新资讯 */
.latest-news {
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

/* 热门企业 */
.hot-companies {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.company-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.company-item {
  font-size: 14px;
  color: #333;
  padding: 8px 15px;
  background-color: #f8f8f8;
  border-radius: 20px;
  display: inline-block;
  transition: all 0.3s ease;
}

.company-item:hover {
  background-color: #c72c41;
  color: white;
  transform: translateY(-2px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    grid-template-columns: 1fr;
  }
  
  .article-title {
    font-size: 24px;
  }
  
  .article-meta {
    flex-wrap: wrap;
    gap: 10px;
  }
}
</style>
