<template>
  <div class="employment">
    <!-- 导航栏 -->
    <Navbar />
    
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>就业服务・职业发展</h2>
    </div>

    <!-- 就业统计数据 -->
    <div class="employment-stats">
      <div class="container">
        <div class="stats-year">
          {{ new Date().getFullYear() }}届毕业生数据
        </div>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-number">{{ stats.employmentRate || '0' }}%</div>
            <div class="stat-label">本科生就业率</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ stats.graduateEmploymentRate || '0' }}%</div>
            <div class="stat-label">研究生就业率</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ stats.companyCount || '0' }}+</div>
            <div class="stat-label">合作企业</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ stats.recruitmentCount || '0' }}+</div>
            <div class="stat-label">招聘岗位</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="container main-content">
      <!-- 左侧：就业信息列表 -->
      <div class="left-content">
        <div class="left-top-grid">
          <!-- 支部老师指导学生情况 -->
          <div class="teacher-guidance">
            <div class="section-header">
              <h3>指导学生就业情况</h3>
              <div class="rank-type-switch">
                <button 
                  @click="switchRankType('undergraduate')"
                  :class="{ active: currentRankType === 'undergraduate' }"
                >
                  本科生
                </button>
                <button 
                  @click="switchRankType('graduate')"
                  :class="{ active: currentRankType === 'graduate' }"
                >
                  研究生
                </button>
              </div>
            </div>
            <div class="guidance-ranking">
              <div class="ranking-list">
                <div v-for="(teacher, index) in teacherGuidanceList.slice(0, 10)" :key="teacher.id" class="ranking-item">
                  <div class="rank-number" :class="{
                    'rank-first': index === 0,
                    'rank-second': index === 1,
                    'rank-third': index === 2
                  }">{{ index + 1 }}</div>
                  <div class="teacher-info">
                    <div class="teacher-name">{{ teacher.name }}</div>
                    <div class="student-count-small">{{ teacher.studentCount }}人</div>
                  </div>
                  <div v-if="teacher.rankChange > 0" class="rank-change rank-up">
                    ↑{{ teacher.rankChange }}
                  </div>
                  <div v-else-if="teacher.rankChange < 0" class="rank-change rank-down">
                    ↓{{ Math.abs(teacher.rankChange) }}
                  </div>
                  <div v-else class="rank-change rank-stable">
                    →
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 就业资讯 -->
          <div class="employment-news">
            <div class="section-header">
              <h3>就业资讯</h3>
              <a href="#" class="more-link" @click.prevent="goToNewsList">更多</a>
            </div>
            <div class="news-list">
              <div v-for="item in employmentList" :key="item.id" class="news-item">
                <div class="news-title">
                  <a href="#" @click.prevent="goToDetail(item.id)">{{ item.title }}</a>
                </div>
                <div class="news-meta">
                  <span class="news-date">{{ item.publishDate }}</span>
                  <span class="news-source">{{ item.source }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 企业招聘信息 -->
        <div class="company-recruitment">
          <div class="section-header">
            <h3>企业招聘</h3>
            <a href="#" class="more-link" @click.prevent="goToRecruitmentList">更多</a>
          </div>
          <div class="recruitment-list">
            <div v-for="item in recruitmentList" :key="item.id" class="recruitment-item">
              <div class="recruitment-title">{{ item.title }}</div>
              <div class="recruitment-meta">
                <span class="meta-item">{{ item.publishDate }}</span>
                <span class="meta-item">{{ item.source }}</span>
                <span class="meta-item">{{ item.category }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：就业指导与服务 -->
      <div class="right-content">
        <!-- 就业指导资源 -->
        <div class="guidance-resources">
          <div class="section-header">
            <h3>就业指导</h3>
            <a href="#" class="more-link" @click.prevent="goToGuidanceList">更多</a>
          </div>
          <div class="resource-list">
            <div v-for="item in guidanceList" :key="item.id" class="resource-item" @click="goToGuidanceDetail(item.id)">
              <div class="resource-title">{{ item.title }}</div>
              <div class="resource-type">{{ item.type }}</div>
            </div>
          </div>
        </div>

        <!-- 就业政策 -->
        <div class="employment-policies">
          <div class="section-header">
            <h3>就业政策</h3>
            <a href="#" class="more-link" @click.prevent="goToPoliciesList">更多</a>
          </div>
          <div class="policy-list">
            <div v-for="item in policyList" :key="item.id" class="policy-item">
              <a href="#" @click.prevent="goToPolicyDetail(item.id)">{{ item.title }}</a>
            </div>
          </div>
        </div>

        <!-- 就业服务 -->
        <div class="employment-services">
          <div class="section-header">
            <h3>就业服务</h3>
            <a href="#" class="more-link" @click.prevent="goToServicesList">更多</a>
          </div>
          <div class="service-list">
            <div v-for="item in serviceList" :key="item.id" class="service-item">
              <div class="service-icon">{{ item.icon }}</div>
              <div class="service-title">{{ item.title }}</div>
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
import { useRouter } from 'vue-router'
import Navbar from '@/components/front/Navbar.vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import employmentApi from '@/api/employment.js'

const router = useRouter()

// 数据
const employmentList = ref([])
const recruitmentList = ref([])
const guidanceList = ref([])
const policyList = ref([])
const serviceList = ref([])
const teacherGuidanceList = ref([])
// 切换状态：undergraduate(本科生) / graduate(研究生)
const currentRankType = ref('undergraduate')
// 本科生和研究生的指导数据
const undergraduateGuidanceList = ref([])
const graduateGuidanceList = ref([])

// 加载数据
onMounted(() => {
  fetchEmploymentStats()
  fetchEmploymentNews()
  fetchCompanyRecruitment()
  fetchEmploymentGuidance()
  fetchEmploymentPolicies()
  fetchEmploymentServices()
  fetchTeacherGuidance()
})

// 就业统计数据（暂时使用模拟数据，接口规范中未定义该接口）
const stats = ref({
  employmentRate: 97.70,
  graduateEmploymentRate: 98.62,
  companyCount: 300,
  recruitmentCount: 1500
})

// 获取就业统计数据
const fetchEmploymentStats = async () => {
  try {
    const data = await employmentApi.getEmploymentStats()
    stats.value = data.data || {}
  } catch (error) {
    console.error('获取就业统计数据失败:', error)
    // 模拟数据
    stats.value = {
      employmentRate: 97.70,
      graduateEmploymentRate: 98.62,
      companyCount: 300,
      recruitmentCount: 1500
    }
  }
}

// 获取就业资讯列表
const fetchEmploymentNews = async () => {
  try {
    const data = await employmentApi.getEmploymentNews({ page: 1, pageSize: 6 })
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    employmentList.value = Array.isArray(data.data?.content) ? data.data.content : 
                          Array.isArray(data.data?.list) ? data.data.list : 
                          Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取就业资讯列表失败:', error)
    // 模拟数据
    employmentList.value = [
      { id: 1, title: '2025年全国高校毕业生就业形势分析', publishDate: '2025-11-20 00:00:00', source: '教育部', category: '就业分析' },
      { id: 2, title: '计算机学院2025届毕业生就业质量报告发布', publishDate: '2025-11-15 00:00:00', source: '计算机学院教师第三党支部', category: '就业报告' },
      { id: 3, title: '2025年秋季校园招聘会即将举行', publishDate: '2025-11-10 00:00:00', source: '就业指导中心', category: '招聘活动' },
      { id: 4, title: '教育部出台新政策支持高校毕业生就业创业', publishDate: '2025-10-28 00:00:00', source: '教育部', category: '政策解读' },
      { id: 5, title: '2025届毕业生就业指导系列讲座安排', publishDate: '2025-10-20 00:00:00', source: '就业指导中心', category: '就业指导' }
    ]
  }
}

// 获取企业招聘信息
const fetchCompanyRecruitment = async () => {
  try {
    const data = await employmentApi.getCompanyRecruitment({ page: 1, pageSize: 5 })
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    recruitmentList.value = Array.isArray(data.data?.content) ? data.data.content : 
                           Array.isArray(data.data?.list) ? data.data.list : 
                           Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取企业招聘信息失败:', error)
    // 模拟数据
    recruitmentList.value = [
      { id: 1, title: '前端开发工程师招聘', companyName: '字节跳动', publishDate: '2025-11-20 00:00:00', source: '字节跳动官网', category: 'IT互联网' },
      { id: 2, title: 'Java开发工程师招聘', companyName: '阿里巴巴', publishDate: '2025-11-19 00:00:00', source: '阿里巴巴官网', category: 'IT互联网' },
      { id: 3, title: '产品经理招聘', companyName: '腾讯科技', publishDate: '2025-11-18 00:00:00', source: '腾讯招聘官网', category: 'IT互联网' },
      { id: 4, title: '算法工程师招聘', companyName: '华为技术', publishDate: '2025-11-17 00:00:00', source: '华为招聘官网', category: '通信' },
      { id: 5, title: '后端开发工程师招聘', companyName: '百度在线', publishDate: '2025-11-16 00:00:00', source: '百度招聘官网', category: 'IT互联网' }
    ]
  }
}

// 获取就业指导资源
const fetchEmploymentGuidance = async () => {
  try {
    const data = await employmentApi.getEmploymentGuidance({ page: 1, pageSize: 5 })
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    guidanceList.value = Array.isArray(data.data?.content) ? data.data.content : 
                        Array.isArray(data.data?.list) ? data.data.list : 
                        Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取就业指导资源失败:', error)
    // 模拟数据
    guidanceList.value = [
      { id: 1, title: '简历制作指南', type: '简历指导', fileUrl: 'http://example.com/resume-guide.pdf' },
      { id: 2, title: '面试技巧培训视频', type: '面试指导', videoUrl: 'http://example.com/interview-skills.mp4' },
      { id: 3, title: '职场礼仪讲座', type: '职场发展', fileUrl: 'http://example.com/workplace-etiquette.pdf' },
      { id: 4, title: '职业规划咨询', type: '职业规划' },
      { id: 5, title: '公务员考试备考资料', type: '考试指导', fileUrl: 'http://example.com/civil-service-exam.pdf' }
    ]
  }
}

// 获取就业政策
const fetchEmploymentPolicies = async () => {
  try {
    const data = await employmentApi.getEmploymentPolicies({ page: 1, pageSize: 5 })
    // 支持多种数据结构：content(分页结构)、list(旧结构)或直接数据数组
    policyList.value = Array.isArray(data.data?.content) ? data.data.content : 
                       Array.isArray(data.data?.list) ? data.data.list : 
                       Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取就业政策失败:', error)
    // 模拟数据
    policyList.value = [
      { id: 1, title: '2023年大学生就业政策解读', publishDate: '2023-12-01' },
      { id: 2, title: '高校毕业生就业创业扶持政策', publishDate: '2023-11-15' },
      { id: 3, title: '基层就业项目实施办法', publishDate: '2023-11-01' },
      { id: 4, title: '大学生征兵政策解读', publishDate: '2023-10-15' },
      { id: 5, title: '留学归国人员就业服务政策', publishDate: '2023-10-01' }
    ]
  }
}

// 获取就业服务
const fetchEmploymentServices = async () => {
  try {
    const data = await employmentApi.getEmploymentServices()
    // 支持多种数据结构：content(分页结构)或直接数据数组
    serviceList.value = Array.isArray(data.data?.content) ? data.data.content : 
                       Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取就业服务失败:', error)
    // 模拟数据
    serviceList.value = [
      { id: 1, icon: '📋', title: '就业咨询', description: '提供专业的就业咨询服务' },
      { id: 2, icon: '🤝', title: '校企合作', description: '搭建校企合作平台' },
      { id: 3, icon: '🎓', title: '就业指导', description: '提供就业指导服务' },
      { id: 4, icon: '📊', title: '数据统计', description: '就业数据统计分析' }
    ]
  }
}

// 切换排行榜类型
const switchRankType = (type) => {
  currentRankType.value = type
  // 切换类型后重新获取数据
  fetchTeacherGuidance()
}

// 获取老师指导学生情况
const fetchTeacherGuidance = async () => {
  try {
    // 使用.value访问ref对象的实际值
    const data = await employmentApi.getTeacherGuidance({ type: currentRankType.value })
    // 支持多种数据结构：content(分页结构)或直接数据数组
    teacherGuidanceList.value = Array.isArray(data.data?.content) ? data.data.content : 
                               Array.isArray(data.data) ? data.data : []
  } catch (error) {
    console.error('获取老师指导学生情况失败:', error)
    // 模拟数据
    teacherGuidanceList.value = [
      { id: 1, name: '张三', studentCount: 25, rankChange: 1 },
      { id: 2, name: '李四', studentCount: 22, rankChange: 0 },
      { id: 3, name: '王五', studentCount: 19, rankChange: -1 },
      { id: 4, name: '赵六', studentCount: 18, rankChange: 2 },
      { id: 5, name: '钱七', studentCount: 17, rankChange: 0 },
      { id: 6, name: '孙八', studentCount: 16, rankChange: -2 },
      { id: 7, name: '周九', studentCount: 15, rankChange: 1 },
      { id: 8, name: '吴十', studentCount: 14, rankChange: 0 },
      { id: 9, name: '郑十一', studentCount: 13, rankChange: -1 },
      { id: 10, name: '王十二', studentCount: 12, rankChange: 1 }
    ]
  }
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/employment/${id}`)
}

// 跳转到政策详情页
const goToPolicyDetail = (id) => {
  router.push(`/employment/policy/${id}`)
}

// 跳转到就业资讯完整列表页
const goToNewsList = () => {
  router.push('/employment/news/list')
}

// 跳转到企业招聘信息完整列表页
const goToRecruitmentList = () => {
  router.push('/employment/company-recruitment/list')
}

// 跳转到就业指导资源完整列表页
const goToGuidanceList = () => {
  router.push('/employment/guidance/list')
}

// 跳转到就业政策完整列表页
const goToPoliciesList = () => {
  router.push('/employment/policies/list')
}

// 跳转到就业服务完整列表页
const goToServicesList = () => {
  router.push('/employment/services/list')
}

// 跳转到就业指导详情页
const goToGuidanceDetail = (id) => {
  router.push(`/employment/guidance/${id}`)
}
</script>

<style scoped>
.employment {
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

/* 就业统计数据 */
.employment-stats {
  background-color: white;
  padding: 30px 0;
  margin-bottom: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 当前年份届数据 */
.stats-year {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #c72c41;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

/* 支部老师指导学生情况 */
.teacher-guidance {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

/* 排行榜类型切换按钮 */
.rank-type-switch {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.rank-type-switch button {
  padding: 6px 12px;
  border: 1px solid #ddd;
  background-color: #f5f5f5;
  color: #333;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.rank-type-switch button:hover {
  background-color: #e0e0e0;
}

.rank-type-switch button.active {
  background-color: #c72c41;
  color: white;
  border-color: #c72c41;
}

.guidance-ranking {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px;
  background-color: white;
  border-radius: 6px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.ranking-item:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.rank-number {
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background-color: #e0e0e0; /* 灰色 */
  color: #333;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
}

.rank-number.rank-first {
  background-color: #c72c41; /* 红色 */
  color: white;
}

.rank-number.rank-second {
  background-color: #f39c12; /* 黄色 */
  color: white;
}

.rank-number.rank-third {
  background-color: #27ae60; /* 绿色 */
  color: white;
}

.teacher-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.rank-change {
  font-size: 14px;
  font-weight: bold;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 30px;
  text-align: center;
}

.rank-up {
  color: #27ae60;
  background-color: rgba(39, 174, 96, 0.1);
}

.rank-down {
  color: #c72c41;
  background-color: rgba(199, 44, 65, 0.1);
}

.rank-stable {
  color: #666;
  background-color: rgba(102, 102, 102, 0.1);
}

.teacher-name {
  font-weight: bold;
  color: #333;
  font-size: 14px;
}

.student-count-small {
  font-size: 14px;
  font-weight: bold;
  color: #c72c41;
  min-width: 50px;
  text-align: right;
}

.teacher-department {
  font-size: 12px;
  color: #666;
  margin-top: 3px;
}

.student-count {
  font-size: 16px;
  font-weight: bold;
  color: #c72c41;
  min-width: 60px;
  text-align: center;
}

.success-rate {
  font-size: 14px;
  font-weight: bold;
  color: #27ae60;
  min-width: 60px;
  text-align: center;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #c72c41;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 16px;
  color: #666;
}

/* 主要内容区域 */
.main-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 50px;
}

/* 左侧内容 */
.left-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.left-top-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-auto-rows: 1fr;
  gap: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .left-top-grid {
    grid-template-columns: 1fr;
  }
}

/* 右侧内容 */
.right-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 企业招聘信息 */
.company-recruitment {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 通用区块样式 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #c72c41;
}

.section-header h3 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.more-link {
  color: #c72c41;
  text-decoration: none;
  font-size: 14px;
}

.more-link:hover {
  text-decoration: underline;
}

/* 就业资讯 */
.employment-news {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  flex: 1;
}

.news-item {
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.news-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.news-title a {
  color: #333;
  text-decoration: none;
  font-size: 16px;
  line-height: 1.5;
}

.news-title a:hover {
  color: #c72c41;
  text-decoration: underline;
}

.news-meta {
  display: flex;
  gap: 15px;
  margin-top: 8px;
  font-size: 14px;
  color: #999;
}

/* 企业招聘信息 */
.company-recruitment {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.recruitment-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.recruitment-item {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 5px;
  transition: all 0.3s ease;
}

.recruitment-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-color: #c72c41;
}

.company-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.company-name {
  font-weight: bold;
  color: #333;
}

.company-industry {
  font-size: 14px;
  color: #666;
}

.job-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.job-title {
  font-weight: bold;
  color: #c72c41;
}

.job-salary {
  font-size: 14px;
  color: #f39c12;
}

.job-location {
  font-size: 14px;
  color: #666;
}

/* 就业指导资源 */
.guidance-resources {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.resource-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.resource-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.resource-item:last-child {
  border-bottom: none;
}

.resource-title {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.5;
}

.resource-title:hover {
  color: #c72c41;
  text-decoration: underline;
}

.resource-type {
  font-size: 12px;
  color: #999;
  background-color: #f5f5f5;
  padding: 3px 8px;
  border-radius: 10px;
}

/* 就业政策 */
.employment-policies {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.policy-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.policy-item a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.5;
  display: block;
  padding: 8px 0;
  position: relative;
}

.policy-item a::before {
  content: '▶';
  color: #c72c41;
  margin-right: 8px;
  font-size: 12px;
}

.policy-item a:hover {
  color: #c72c41;
  text-decoration: underline;
}

/* 就业服务 */
.employment-services {
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.service-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.service-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 20px;
  background-color: #f8f8f8;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.service-item:hover {
  background-color: #c72c41;
  color: white;
}

.service-icon {
  font-size: 32px;
  margin-bottom: 10px;
}

.service-title {
  font-size: 14px;
  font-weight: bold;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
  }
  
  .service-list {
    grid-template-columns: 1fr;
  }
}
</style>
