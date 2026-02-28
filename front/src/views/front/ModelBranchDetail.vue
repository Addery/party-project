<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Navbar from '@/components/front/Navbar.vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import branchApi from '@/api/branch.js'

const route = useRoute()
const branchId = route.params.id

const branch = ref(null)
const loading = ref(true)
const error = ref(null)

// 模拟数据，实际项目中应从API获取
const mockBranch = {
  id: branchId,
  name: '计算机学院学生第一党支部',
  description: '全国样板党支部，连续三年被评为先进党支部',
  image: 'https://picsum.photos/id/2/800/400',
  level: 'national',
  introduction: '计算机学院学生第一党支部成立于2018年，现有党员35名，其中正式党员28名，预备党员7名。党支部始终坚持以习近平新时代中国特色社会主义思想为指导，深入贯彻落实党的二十大精神，不断加强党的建设，取得了显著成效。',
  achievements: [
    '2022年被评为全国样板党支部',
    '2023年被评为先进党支部',
    '2024年被评为先进党支部',
    '2025年被评为先进党支部'
  ],
  activities: [
    {
      title: '主题党日活动',
      date: '2025-05-20',
      content: '组织党员参观革命纪念馆，重温入党誓词'
    },
    {
      title: '志愿服务活动',
      date: '2025-06-10',
      content: '组织党员到社区开展志愿服务，帮助困难群众'
    },
    {
      title: '理论学习活动',
      date: '2025-07-01',
      content: '组织党员学习习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话'
    }
  ],
  createdAt: '2025-01-15T08:30:00Z',
  updatedAt: '2025-06-20T14:45:00Z'
}

// 获取样板支部详情
const getBranchDetail = async () => {
  try {
    loading.value = true
    // 使用API获取数据
    const response = await branchApi.getModelBranchDetail(branchId)
    if (response.code === 200) {
      // 为achievements和activities添加默认值，确保页面能正常渲染
      const branchData = response.data
      branch.value = {
        ...branchData,
        achievements: Array.isArray(branchData.achievements) ? branchData.achievements : [],
        activities: Array.isArray(branchData.activities) ? branchData.activities : []
      }
    } else {
      console.error('获取样板支部详情失败：', response.message)
      error.value = response.message || '获取样板支部详情失败'
      // 出错时使用模拟数据
      await new Promise(resolve => setTimeout(resolve, 500))
      branch.value = mockBranch
    }
  } catch (err) {
    console.error('获取样板支部详情失败:', err)
    error.value = '获取样板支部详情失败，请稍后重试'
    // 出错时使用模拟数据
    await new Promise(resolve => setTimeout(resolve, 500))
    branch.value = mockBranch
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getBranchDetail()
})
</script>

<template>
  <div class="front-container">
    <!-- 顶部横幅 -->
    <div class="motto-banner">
      <img src="@/assets/images/head.jpg" alt="不忘初心 牢记使命" class="banner-image">
    </div>

    <!-- 导航栏 -->
    <Navbar />

    <!-- 主体内容 -->
    <main class="main-content">
      <div class="content-container">
        <div class="branch-detail-container">
          <div v-if="loading" class="loading">加载中...</div>
          <div v-else-if="error" class="error">{{ error }}</div>
          <div v-else-if="branch" class="branch-detail">
            <!-- 头部信息 -->
            <div class="branch-header">
              <div class="branch-image">
                <img :src="branch.image" :alt="branch.name">
              </div>
              <div class="branch-title-section">
                <h1 class="branch-name">{{ branch.name }}</h1>
                <div class="branch-level">
                  <span v-if="branch.level === 'national'" class="level-national">国家级</span>
                  <span v-else-if="branch.level === 'provincial'" class="level-provincial">省级</span>
                  <span v-else-if="branch.level === 'municipal'" class="level-municipal">市级</span>
                  <span v-else-if="branch.level === 'school'" class="level-school">校级</span>
                </div>
                <div class="branch-meta">
                  <span>创建时间：{{ new Date(branch.createdAt).toLocaleDateString() }}</span>
                  <span>最后更新：{{ new Date(branch.updatedAt).toLocaleDateString() }}</span>
                </div>
              </div>
            </div>

            <!-- 简介 -->
            <div class="branch-description">
              <h2>支部简介</h2>
              <p>{{ branch.introduction }}</p>
            </div>

            <!-- 成就 -->
            <div class="branch-achievements">
              <h2>支部成就</h2>
              <div v-if="branch.achievements && branch.achievements.length > 0">
                <ul>
                  <li v-for="(achievement, index) in branch.achievements" :key="index">
                    {{ achievement }}
                  </li>
                </ul>
              </div>
              <div v-else class="no-content">
                <p>暂无支部成就信息</p>
              </div>
            </div>

            <!-- 活动 -->
            <div class="branch-activities">
              <h2>支部活动</h2>
              <div v-if="branch.activities && branch.activities.length > 0">
                <div class="activities-list">
                  <div v-for="(activity, index) in branch.activities" :key="index" class="activity-item">
                    <div class="activity-header">
                      <h3>{{ activity.title }}</h3>
                      <span class="activity-date">{{ activity.date }}</span>
                    </div>
                    <p class="activity-content">{{ activity.content }}</p>
                  </div>
                </div>
              </div>
              <div v-else class="no-content">
                <p>暂无支部活动信息</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <p>党员教育与管理系统 © 2025 All Rights Reserved</p>
        <p>联系邮箱：contact@example.com</p>
      </div>
    </footer>

    <!-- 智能问答助手 -->
    <ChatAssistant />
  </div>
</template>

<style scoped>
.front-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.motto-banner {
  width: 100%;
  height: 150px;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.main-content {
  flex: 1;
  background-color: #f5f5f5;
  padding: 20px 0;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.branch-detail-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.loading, .error {
  text-align: center;
  padding: 50px 0;
  font-size: 18px;
}

.error {
  color: #d93025;
}

.branch-detail {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.branch-header {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.branch-image {
  width: 300px;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
  flex-shrink: 0;
}

.branch-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.branch-title-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.branch-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.branch-level {
  margin-bottom: 10px;
}

.level-national {
  background-color: #d93025;
  color: white;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 14px;
  margin-right: 10px;
}

.level-provincial {
  background-color: #f57c00;
  color: white;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 14px;
  margin-right: 10px;
}

.level-municipal {
  background-color: #1976d2;
  color: white;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 14px;
  margin-right: 10px;
}

.level-school {
  background-color: #43a047;
  color: white;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 14px;
  margin-right: 10px;
}



.branch-meta {
  font-size: 14px;
  color: #666;
  display: flex;
  gap: 20px;
}

.branch-description, .branch-achievements, .branch-activities {
  margin-bottom: 30px;
}

.branch-description h2, .branch-achievements h2, .branch-activities h2 {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
  border-bottom: 2px solid #d93025;
  padding-bottom: 5px;
  display: inline-block;
}

.branch-description p {
  font-size: 16px;
  line-height: 1.6;
  color: #666;
}

.branch-achievements ul {
  list-style-type: disc;
  padding-left: 20px;
}

.branch-achievements li {
  font-size: 16px;
  line-height: 1.6;
  color: #666;
  margin-bottom: 10px;
}

.no-content {
  background-color: #fafafa;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  color: #999;
  font-size: 16px;
}

.activities-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.activity-item {
  background-color: #fafafa;
  padding: 15px;
  border-radius: 8px;
}

.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.activity-header h3 {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.activity-date {
  font-size: 14px;
  color: #999;
}

.activity-content {
  font-size: 16px;
  line-height: 1.6;
  color: #666;
}

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

/* 响应式设计 */
@media (max-width: 768px) {
  .branch-header {
    flex-direction: column;
    align-items: center;
  }
  
  .branch-image {
    width: 100%;
    height: auto;
    max-height: 300px;
  }
  
  .branch-title-section {
    text-align: center;
  }
  
  .branch-meta {
    justify-content: center;
  }
}
</style>
