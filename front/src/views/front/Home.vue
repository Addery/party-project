<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import axios from 'axios'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import Navbar from '@/components/front/Navbar.vue'
import newsApi from '@/api/news.js'
import noticeApi from '@/api/notice.js'
import activityApi from '@/api/activity.js'
import learningApi from '@/api/learning.js'
import branchApi from '@/api/branch.js'

const router = useRouter()
const userStore = useUserStore()

// 新闻列表数据
const newsList = ref([])
// 加载状态
const loading = ref(false)
// 错误信息
const error = ref(null)

const noticesList = ref([
  {
    id: 1,
    title: "关于2025年11月计算机学院党委理论学习中心组学习的通知",
    date: "2025-11-15",
    department: "计算机学院党委"
  },
  {
    id: 2,
    title: "关于召开2025年计算机学院党员大会的通知",
    date: "2025-11-10",
    department: "计算机学院党委"
  },
  {
    id: 3,
    title: "关于2025年计算机学院创新创业大赛导师招募的通知",
    date: "2025-10-20",
    department: "计算机学院团委"
  },
  {
    id: 4,
    title: "关于开展2025年计算机学院党史观影活动的通知",
    date: "2025-10-15",
    department: "计算机学院党委"
  },
  {
    id: 5,
    title: "关于2025年计算机学院新生入党教育的通知",
    date: "2025-09-20",
    department: "计算机学院党委"
  }
])

// 生成推荐内容数据库 - 近期内容
const contentDatabase = [
  // 新闻
  {
    id: 1,
    title: '习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话',
    type: '新闻',
    date: '2025-07-01',
    tags: ['习近平新时代中国特色社会主义思想', '建党节']
  },
  {
    id: 2,
    title: '中共中央关于加强和改进新形势下党的建设的意见',
    type: '新闻',
    date: '2025-08-15',
    tags: ['党建', '政策文件']
  },
  {
    id: 3,
    title: '2025年全国党员教育培训工作规划解读',
    type: '新闻',
    date: '2025-09-01',
    tags: ['党员教育', '规划']
  },
  // 学习资源
  {
    id: 4,
    title: '习近平新时代中国特色社会主义思想学习纲要（2025年版）',
    type: '学习资源',
    date: '2025-10-01',
    views: 2500,
    tags: ['习近平新时代中国特色社会主义思想', '学习纲要']
  },
  {
    id: 5,
    title: '新时代党员干部必备能力学习手册',
    type: '学习资源',
    date: '2025-10-15',
    views: 1850,
    tags: ['党员干部', '能力提升']
  },
  // 活动 - 近期主题活动
  {
    id: 6,
    title: '2025年秋季学期习近平新时代中国特色社会主义思想专题研讨班',
    type: '活动',
    date: '2025-10-20',
    location: '计算机学院会议室',
    participants: 120,
    tags: ['习近平新时代中国特色社会主义思想', '专题研讨']
  },
  {
    id: 7,
    title: '计算机学院2025年党员志愿服务月启动仪式',
    type: '活动',
    date: '2025-11-05',
    location: '学校礼堂',
    participants: 200,
    tags: ['志愿服务', '党员活动']
  },
  {
    id: 8,
    title: '2025年计算机学院党委理论学习中心组学习扩大会',
    type: '活动',
    date: '2025-11-15',
    location: '计算机学院学术报告厅',
    participants: 80,
    tags: ['理论学习', '中心组学习']
  }
]

// 根据用户标签推荐的内容
const recommendedContent = ref([])

// 生成模拟数据，包含新闻、学习资源和活动
const generateRecommendedContent = () => {
  // 按分类组织推荐内容
  return {
    news: contentDatabase.filter(item => item.type === '新闻'),
    activities: contentDatabase.filter(item => item.type === '活动'),
    learningResources: contentDatabase.filter(item => item.type === '学习资源')
  }
}

// 初始化推荐内容
recommendedContent.value = generateRecommendedContent()

// 跳转到通知公告页面
const goToNotices = () => {
  router.push('/notice')
}

// 新增：样板支部专栏
const modelBranches = ref([
  {
    id: 1,
    name: '计算机学院学生第一党支部',
    description: '全国样板党支部，连续三年被评为先进党支部',
    image: 'https://picsum.photos/id/2/150/100'
  },
  {
    id: 2,
    name: '马克思主义学院教工党支部',
    description: '省级样板党支部，党建与教学融合成效显著',
    image: 'https://picsum.photos/id/3/150/100'
  }
])

// 访问人数数据
const visitCount = ref(0)





// API接口
const api = {
  // 获取头条新闻列表
  getNewsList: async () => {
    try {
      const data = await newsApi.getHeadlineNews({
        page: 1,
        pageSize: 10
      })
      
      // 添加详细的调试信息
      console.log('头条新闻API返回数据:', data)
      
      // 检查API返回的数据格式是否正确
      if (!data) {
        console.error('获取头条新闻失败：返回数据为空')
        error.value = '获取头条新闻失败，请稍后重试'
        return []
      }
      
      if (data.code !== 200) {
        console.error('获取头条新闻失败：状态码不为200', data.code)
        error.value = '获取头条新闻失败，请稍后重试'
        return []
      }
      
      if (!data.data) {
        console.error('获取头条新闻失败：data字段不存在')
        error.value = '获取头条新闻失败，请稍后重试'
        return []
      }
      
      // 检查数据是否在data.list或其他字段中
      const newsList = data.data.list || data.data.newsList || data.data
      
      if (!Array.isArray(newsList)) {
        console.error('获取头条新闻失败：新闻列表不是数组', newsList)
        error.value = '获取头条新闻失败，请稍后重试'
        return []
      }
      
      // 映射API返回数据到组件需要的格式
      return newsList.map(item => ({
        id: item.id,
        title: item.title,
        date: item.publishDate || item.date,
        content: item.description || item.summary,
        author: item.author,
        image: item.coverImage || item.cover || item.imageUrl || '',
        tags: Array.isArray(item.category) ? item.category : [item.category]
      }))
    } catch (err) {
      console.error('获取头条新闻失败:', err)
      error.value = '获取头条新闻失败，请稍后重试'
      // 出错时返回空数组，避免页面崩溃
      return []
    }
  },
  // 获取通知列表
  getNoticesList: async () => {
    try {
      const data = await noticeApi.getHomeNoticesList({
        limit: 10
      })
      
      // 检查API返回的数据格式是否正确
      if (!data || data.code !== 200 || !Array.isArray(data.data)) {
        console.error('获取通知公告失败：返回数据格式不正确', data)
        error.value = '获取通知公告失败，请稍后重试'
        return []
      }
      
      // 处理API返回数据，确保每个通知都有source字段
      const processedNotices = data.data.map(notice => {
        // 如果接口返回的数据中有source字段，则直接使用
        // 如果没有，则使用默认值或从其他字段转换
        return {
          ...notice,
          // 确保source字段存在
          source: notice.source || notice.department || notice.sourceDepartment || notice.departmentName || '未知来源'
        }
      })
      
      return processedNotices
    } catch (err) {
      console.error('获取通知公告失败:', err)
      error.value = '获取通知公告失败，请稍后重试'
      // 出错时返回空数组，避免页面崩溃
      return []
    }
  },
  // 获取推荐内容
  getRecommendedContent: async () => {
    try {
      // 并行获取首页推荐的新闻、活动和学习资源
      const [newsResponse, activitiesResponse, resourcesResponse] = await Promise.all([
        newsApi.getNewsList({
          page: 1,
          pageSize: 6
        }),
        // 改用getActivityList接口获取活动数据，因为getHomeActivities接口始终只返回4条数据
        activityApi.getActivityList({
          page: 1,
          pageSize: 6
        }),
        learningApi.getLearningResources({
          page: 1,
          pageSize: 6
        })
      ])
      
      // 添加调试信息
      console.log('推荐内容API返回数据:', {
        newsResponse,
        activitiesResponse,
        resourcesResponse
      })
      
      // 转换新闻数据格式
      let news = []
      if (newsResponse.code === 200 && newsResponse.data) {
        const newsData = newsResponse.data.list || newsResponse.data.newsList || newsResponse.data.news || newsResponse.data.content || newsResponse.data
        if (Array.isArray(newsData)) {
          news = newsData.map(item => ({
            id: item.id,
            title: item.title,
            type: '新闻',
            date: item.publishDate || item.date,
            views: item.viewsCount || item.views,
            tags: Array.isArray(item.category) ? item.category : [item.category]
          }))
        } else {
          console.error('新闻数据不是数组:', newsData)
        }
      } else {
        console.error('新闻API返回错误:', newsResponse)
      }
      
      // 转换活动数据格式
      let activities = []
      if (activitiesResponse.code === 200 && activitiesResponse.data) {
        const activitiesData = activitiesResponse.data.activities || activitiesResponse.data.list || activitiesResponse.data.content || activitiesResponse.data
        if (Array.isArray(activitiesData)) {
          activities = activitiesData
            .filter(activity => activity.status === 'published') // 只显示已发布的活动
            .map(activity => ({
            id: activity.id,
            title: activity.title,
            type: '活动',
            date: activity.date,
            location: activity.location,
            participants: activity.participants,
            status: activity.status,
            image: activity.image,
            tags: activity.tags || []
          }))
        } else {
          console.error('活动数据不是数组:', activitiesData)
        }
      } else {
        console.error('活动API返回错误:', activitiesResponse)
      }
      
      // 转换学习资源数据格式
      let learningResources = []
      if (resourcesResponse.code === 200 && resourcesResponse.data) {
        const resourcesData = resourcesResponse.data.resources || resourcesResponse.data.list || resourcesResponse.data.content || resourcesResponse.data
        if (Array.isArray(resourcesData)) {
          learningResources = resourcesData.map(resource => ({
            id: resource.id,
            title: resource.title,
            type: '学习资源',
            date: resource.createTime || resource.date,
            views: resource.views,
            tags: Array.isArray(resource.category) ? resource.category : [resource.category || '']
          }))
        } else {
          console.error('学习资源数据不是数组:', resourcesData)
        }
      } else {
        console.error('学习资源API返回错误:', resourcesResponse)
      }
      
      return {
        news: news,
        activities: activities,
        learningResources: learningResources
      }
    } catch (error) {
      console.error('获取推荐内容失败:', error)
      // 出错时返回模拟数据
      return recommendedContent.value
    }
  },
  // 获取样板支部
  getModelBranches: async () => {
    try {
      const data = await branchApi.getModelBranches({
        limit: 2, // 只获取2条数据
        sortBy: 'createdAt',
        order: 'desc'
      })
      
      // 检查API返回的数据格式是否正确
      if (!data || data.code !== 200 || !Array.isArray(data.data)) {
        console.error('获取样板支部列表失败：返回数据格式不正确', data)
        // 出错时返回模拟数据
        return modelBranches.value
      }
      
      // 返回API数据（已经是需要的格式）
      return data.data
    } catch (err) {
      console.error('获取样板支部列表失败:', err)
      // 出错时返回模拟数据
      return modelBranches.value
    }
  },
  
  // 获取访问人数数据
  getVisitCount: async () => {
    try {
      // 直接使用axios调用后端接口，因为这是一个简单的统计接口
      const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/session-stats/basic`)
      
      // 检查返回数据是否直接包含统计信息（没有外层code和data）
      if (response.data && typeof response.data.totalSessions === 'number') {
        return response.data.totalSessions
      } else if (response.data.code === 200 && response.data.data) {
        // 兼容可能的标准格式
        return response.data.data.totalSessions || 0
      } else {
        console.error('获取访问人数失败：返回数据格式不正确', response.data)
        return 0
      }
    } catch (err) {
      console.error('获取访问人数失败:', err)
      return 0
    }
  }
  


}

// 响应式调整元素数量
const screenWidth = ref(window.innerWidth)

// 根据屏幕宽度计算各板块显示的元素数量
const getDisplayCount = () => {
  if (screenWidth.value < 600) {
    return { news: 3, notices: 3, recommended: 2 }
  } else if (screenWidth.value < 900) {
    return { news: 3, notices: 5, recommended: 2 }
  } else {
    return { news: 3, notices: 5, recommended: 2 }
  }
}

// 监听窗口大小变化
const handleResize = () => {
  screenWidth.value = window.innerWidth
}

// 计算属性：显示的元素列表
const displayedNewsList = computed(() => {
  const count = getDisplayCount().news
  return newsList.value.slice(0, count)
})

const displayedNoticesList = computed(() => {
  const count = getDisplayCount().notices
  return noticesList.value.slice(0, count)
})

// 新增：主题活动数据处理
const displayedActivitiesList = computed(() => {
  const count = getDisplayCount().news
  return recommendedContent.value.activities.slice(0, count)
})

const displayedRecommendedContent = computed(() => {
  const count = getDisplayCount().recommended
  return {
    news: recommendedContent.value.news.slice(0, count),
    activities: recommendedContent.value.activities.slice(0, count),
    learningResources: recommendedContent.value.learningResources.slice(0, count)
  }
})

// 页面初始化
onMounted(async () => {
  try {
    loading.value = true
    // 调用接口获取真实数据
  const [news, notices, recommended, branches, count] = await Promise.all([
    api.getNewsList(),
    api.getNoticesList(),
    api.getRecommendedContent(),
    api.getModelBranches(),
    api.getVisitCount()
  ])
  
  newsList.value = news
  noticesList.value = notices
  recommendedContent.value = recommended
  modelBranches.value = branches
  visitCount.value = count
  } catch (err) {
    console.error('获取数据失败:', err)
    error.value = '获取数据失败，请稍后重试'
  } finally {
    loading.value = false
  }
  
  // 添加窗口大小变化监听
  window.addEventListener('resize', handleResize)
})

// 组件卸载时移除监听
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})

// 跳转到新闻详情
const goToNewsDetail = (id) => {
  router.push({ name: 'NewsDetail', params: { id } })
}

// 跳转到新闻列表
const goToNewsList = () => {
  router.push({ name: 'NewsList' })
}

// 跳转到学习页面
const goToLearning = () => {
  router.push({ name: 'Learning' })
}

// 跳转到活动页面
const goToActivities = () => {
  router.push({ name: 'Activities' })
}

// 跳转到通知详情
const goToNoticeDetail = (id) => {
  router.push(`/notice/${id}`)
}

// 跳转到样板支部详情页面
const goToModelBranchDetail = (id) => {
  router.push({ name: 'ModelBranchDetail', params: { id } })
}

// 跳转到活动详情页面
const goToActivityDetail = (id) => {
  router.push(`/activity/${id}`)
}



// 跳转到登录页面
const goToLogin = () => {
  router.push({ name: 'Login' })
}

// 退出登录
const logout = () => {
  userStore.logout()
  router.push({ name: 'Login' })
}
</script>

<template>
  <div class="front-container">
    <!-- 导航栏 -->
    <Navbar />

    <!-- 顶部横幅 -->
    <div class="motto-banner">
      <img src="@/assets/images/head.jpg" alt="不忘初心 牢记使命" class="banner-image">
    </div>



    <!-- 主体内容 -->
    <main class="main-content">
      <!-- 内容区域 -->
      <div class="content-container">
        <!-- 第一行：头条新闻和为您推荐 -->
        <div class="row-container">
          <!-- 头条新闻板块 -->
          <div class="news-section">
            <div class="section-header">
              <h2>头条新闻</h2>
              <a href="#" class="more-link" @click.prevent="goToNewsList">更多</a>
            </div>
            <div class="news-list">
              <div 
                v-for="(news, index) in displayedNewsList" 
                :key="news.id" 
                class="news-item"
                @click="goToNewsDetail(news.id)"
              >
                <div v-if="index === 0" class="news-number first">1</div>
                <div v-else class="news-number">{{ index + 1 }}</div>
                <div class="news-content-wrap">
                  <div class="news-date">{{ news.date }}</div>
                  <h3 class="news-title">{{ news.title }}</h3>
                  <div class="news-content">
                    <!-- 只有top1新闻在首页直接显示图片 -->
                    <div v-if="index === 0 && news.image" class="news-image">
                      <img :src="news.image" :alt="news.title" />
                    </div>
                    <p>{{ news.content }}</p>
                  </div>
                  <div class="news-author">{{ news.author }}</div>
                </div>
                <button class="news-read-more" @click.stop="goToNewsDetail(news.id)">阅读全文</button>
              </div>
            </div>
          </div>

          <!-- 为您推荐板块 -->
          <div class="left-recommend-section">
            <h3>为您推荐</h3>
            
            <!-- 新闻板块 -->
            <div class="recommend-subsection news-subsection">
              <h4 class="subsection-title">新闻</h4>
              <div 
                v-for="item in displayedRecommendedContent.news" 
                :key="item.id" 
                class="recommended-item"
              >
                <h4>{{ item.title }}</h4>
                <p>{{ item.date }}</p>
              </div>
            </div>
            
            <!-- 活动板块 -->
            <div class="recommend-subsection activity-subsection">
              <h4 class="subsection-title">活动</h4>
              <div 
                v-for="item in displayedRecommendedContent.activities" 
                :key="item.id" 
                class="recommended-item"
              >
                <h4>{{ item.title }}</h4>
                <p>{{ item.date }}</p>
              </div>
            </div>
            
            <!-- 学习资源板块 -->
            <div class="recommend-subsection resource-subsection">
              <h4 class="subsection-title">学习资源</h4>
              <div 
                v-for="item in displayedRecommendedContent.learningResources" 
                :key="item.id" 
                class="recommended-item"
              >
                <h4>{{ item.title }}</h4>
                <p>{{ item.date }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 第二行：通知公告 -->
        <div class="row-container">
          <!-- 通知公告板块 -->
          <div class="notice-section">
            <div class="section-header">
              <h2>通知公告</h2>
              <a href="#" class="more-link" @click.prevent="goToNotices">更多</a>
            </div>
            <div class="notice-list">
              <div 
                v-for="notice in displayedNoticesList" 
                :key="notice.id" 
                class="notice-item"
                @click="goToNoticeDetail(notice.id)"
              >
                <div class="notice-content">
                  <div class="notice-title">{{ notice.title }}</div>
                  <div class="notice-info">
                    <span class="notice-date">{{ notice.date }}</span>
                    <span class="notice-source">{{ notice.source }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 第三行：主题活动和样板支部 -->
        <div class="row-container">
          <!-- 主题活动板块 -->
          <div class="activity-section">
            <div class="section-header">
              <h2>主题活动</h2>
              <a href="#" class="more-link" @click.prevent="goToActivities">更多</a>
            </div>
            <div class="activity-list">
              <div 
                v-for="(activity, index) in displayedActivitiesList" 
                :key="activity.id" 
                class="activity-item"
                @click="goToActivityDetail(activity.id)"
              >
                <div class="activity-content">
                  <div class="activity-title">{{ activity.title }}</div>
                  <div class="activity-info">
                    <span class="activity-date">{{ activity.date }}</span>
                    <span class="activity-location">{{ activity.location }}</span>
                    <span class="activity-participants">参与人数: {{ activity.participants }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 样板支部板块 -->
          <div class="model-branches-section">
            <div class="section-header">
              <h2>样板支部</h2>
            </div>
            <div class="model-branches-list">
              <div v-if="modelBranches.length === 0" class="no-data">
                敬请期待
              </div>
              <div 
                v-else
                v-for="branch in modelBranches" 
                :key="branch.id" 
                class="model-branch-item"
                @click="goToModelBranchDetail(branch.id)"
              >
                <div class="branch-image">
                  <img :src="branch.image" :alt="branch.name">
                </div>
                <div class="branch-info">
                  <h3>{{ branch.name }}</h3>
                  <p>{{ branch.description }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        

      </div>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <p>红芯软炬党建教育平台 © 2025 All Rights Reserved</p>
        <p>联系邮箱：addery8023@163.com</p>
        <p>访问人数：{{ visitCount }}</p>
      </div>
    </footer>

    <!-- 智能问答助手 -->
    <ChatAssistant />
  </div>
</template>

<style scoped>
/* 全局容器 */
.front-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 */
.motto-banner {
  width: 100%;
  overflow: hidden;
  position: relative;
}

.banner-image {
  width: 100%;
  height: auto;
  display: block;
}



.motto-text {
  position: relative;
  z-index: 1;
  display: inline-block;
  color: gold;
  letter-spacing: 2px;
  font-family: 'Microsoft YaHei', sans-serif;
}

.motto-text::before {
  content: '◆';
  margin-right: 10px;
  color: gold;
}

.motto-text::after {
  content: '◆';
  margin-left: 10px;
  color: gold;
}



/* 主横幅 */
.main-banner {
  background-color: #d93025;
  color: white;
  padding: 40px 0;
  text-align: center;
}

.banner-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.banner-content h1 {
  font-size: 28px;
  margin-bottom: 10px;
}

.banner-content p {
  font-size: 16px;
}

/* 主体内容 */
.main-content {
  flex: 1;
  background-color: #f5f5f5;
  padding: 20px 0;
  position: relative;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 行容器 */
.row-container {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  width: 100%;
}

/* 板块宽度设置 */
.news-section {
  flex: 2;
  min-width: 300px;
}

.left-recommend-section {
  flex: 1;
  min-width: 250px;
}

.notice-section {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.activity-section {
  flex: 2;
  min-width: 300px;
}

.model-branches-section {
  flex: 1;
  min-width: 250px;
}

/* 通用板块样式 */
.news-section, .notice-section, .left-recommend-section, .activity-section, .model-branches-section {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #d93025;
}

.section-header h2 {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.more-link {
  color: #d93025;
  text-decoration: none;
  font-size: 14px;
}

.more-link:hover {
  text-decoration: underline;
}

/* 为您推荐板块 */
.left-recommend-section {
  flex: 1;
  min-width: 220px;
  max-width: 250px;
}

.left-recommend-section h3 {
  font-size: 18px;
  margin-bottom: 20px;
  color: #333;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.activity-item {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.activity-item:hover {
  background-color: #f8f8f8;
}

.activity-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
  line-height: 1.4;
}

.activity-info {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  font-size: 14px;
  color: #666;
}

.activity-date, .activity-location, .activity-participants {
  display: flex;
  align-items: center;
}

.activity-item:last-child {
  border-bottom: none;
}

.recommend-subsection {
  margin-bottom: 25px;
}

.subsection-title {
  font-size: 16px;
  margin-bottom: 15px;
  padding-left: 12px;
  position: relative;
}

/* 不同板块的左侧竖线颜色 */
.news-subsection .subsection-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  background-color: #d93025; /* 红色 */
  border-radius: 2px;
}

.activity-subsection .subsection-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  background-color: #1976d2; /* 蓝色 */
  border-radius: 2px;
}

.resource-subsection .subsection-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  background-color: #388e3c; /* 绿色 */
  border-radius: 2px;
}

.recommended-item {
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.recommended-item:last-child {
  border-bottom: none;
}

.recommended-item h4 {
  font-size: 14px;
  margin-bottom: 5px;
  color: #333;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recommended-item p {
  font-size: 12px;
  color: #999;
}

/* 头条新闻板块 */
.news-section {
  flex: 2;
  min-width: 350px;
}

.news-list {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.news-item {
  cursor: pointer;
  transition: transform 0.3s;
  padding: 15px;
  border-radius: 8px;
  background-color: #fafafa;
  margin-bottom: 15px;
  display: flex;
  position: relative;
}

.news-item:first-child {
  background-color: #fff3f3;
  border-left: 4px solid #d93025;
}

.news-item:last-child {
  margin-bottom: 0;
}

.news-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.news-number {
  width: 30px;
  height: 30px;
  background-color: #e0e0e0;
  color: #666;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
  margin-right: 15px;
  flex-shrink: 0;
}

.news-number.first {
  background-color: #d93025;
  color: white;
  width: 40px;
  height: 40px;
  font-size: 20px;
}

.news-content-wrap {
  flex: 1;
}

.news-date {
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
}

.news-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.news-content {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.news-content > div {
  display: flex;
  gap: 15px;
}

.news-image {
  flex-shrink: 0;
  width: 100px;
  height: 80px;
  overflow: hidden;
  border-radius: 4px;
}

/* 放大top1新闻的图片 */
.news-item:first-child .news-image {
  width: 200px;
  height: 160px;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.news-image + p {
  flex: 1;
  margin: 0;
}

.news-author {
  font-size: 12px;
  color: #999;
  text-align: right;
  margin-bottom: 10px;
}

.news-read-more {
  position: absolute;
  right: 15px;
  bottom: 15px;
  padding: 6px 12px;
  background-color: #d93025;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.news-read-more:hover {
  background-color: #c5221f;
}

/* 通知公告板块 */
.notice-section {
  flex: 2;
  min-width: 350px;
}

.notice-list {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.notice-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border-radius: 8px;
  background-color: #fafafa;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-bottom: 10px;
}

.notice-item:last-child {
  margin-bottom: 0;
}

.notice-item:hover {
  background-color: #f0f0f0;
}

.notice-content {
  flex: 1;
}

.notice-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notice-info {
  font-size: 12px;
  color: #666;
  display: flex;
  justify-content: space-between;
}

.notice-date {
  margin-right: 10px;
}

.notice-department {
  font-weight: 500;
  color: #d93025;
}

/* 推荐板块 */
.recommend-section {
  width: 25%;
  gap: 30px;
}

/* 样板支部专栏 */
.model-branches-section {
  /* 确保板块充满容器 */
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.model-branches-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  /* 确保列表充满板块 */
  flex-grow: 1;
  justify-content: stretch;
}

.model-branch-item {
  display: flex;
  gap: 15px;
  padding: 15px;
  background-color: #fafafa;
  border-radius: 8px;
  transition: background-color 0.3s;
  cursor: pointer;
  /* 让每条内容根据容器高度自动分配高度 */
  flex: 1;
  min-height: 100px;
}

.model-branch-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.no-data {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  width: 100%;
  background-color: #f8f8f8;
  color: #999;
  font-size: 16px;
  border-radius: 8px;
}

.branch-image {
  width: 100px;
  height: 80px;
  overflow: hidden;
  border-radius: 4px;
  flex-shrink: 0;
}

.branch-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.branch-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.branch-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.branch-description {
  font-size: 12px;
  color: #666;
  line-height: 1.4;
}





/* 响应式设计 */
@media (max-width: 900px) {
  .content-container {
    flex-direction: column;
  }
  
  .left-recommend-section {
    order: 1;
    max-width: 100%;
    position: static;
  }
  
  .news-section, .notice-section {
    order: 2;
    min-width: 100%;
  }
}

@media (max-width: 600px) {
  .content-container {
    padding: 0 10px;
  }
  
  .news-section, .notice-section, .left-recommend-section {
    padding: 15px;
  }
}

/* 页脚 */
.footer {
  background-color: #333;
  color: white;
  padding: 20px 0;
  text-align: center;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  font-size: 14px;
}
</style>
