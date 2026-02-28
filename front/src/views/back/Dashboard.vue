<script setup>
import { ref } from 'vue'
import AdminLayout from '@/components/AdminLayout.vue'
import { useUserStore } from '../../stores/user'

// 状态管理
const userStore = useUserStore()

// 模拟数据：党员统计
const partyStatistics = ref({
  total: 1250,
  active: 1235,
  new: 25,
  branches: 42
})

// 模拟数据：学习情况
const learningStats = ref([
  { name: '习近平新时代中国特色社会主义思想', completion: 95 },
  { name: '党章党规', completion: 92 },
  { name: '党史学习', completion: 88 },
  { name: '时政学习', completion: 85 }
])

// 模拟数据：最近活动
const recentActivities = ref([
  {
    title: '主题党日活动：学习贯彻二十大精神',
    branch: '机关第一党支部',
    time: '5月20日 14:30'
  },
  {
    title: '党员发展大会',
    branch: '学生第一党支部',
    time: '5月18日 16:00'
  },
  {
    title: '党费收缴工作会议',
    branch: '党委办公室',
    time: '5月15日 09:00'
  }
])

// 模拟数据：系统通知
const systemNotifications = ref([
  {
    title: '系统升级通知',
    desc: '系统将于5月25日22:00-24:00进行升级维护',
    time: '5月20日'
  },
  {
    title: '学习任务提醒',
    desc: '本月学习任务已发布，请及时完成',
    time: '5月15日'
  }
])
</script>

<template>
  <AdminLayout>
    <div class="dashboard-content">
      <!-- 欢迎信息 -->
      <div class="welcome-section">
        <h2>欢迎回来，{{ userStore.userInfo.name || '管理员' }}！</h2>
        <p>今天是 {{ new Date().toLocaleDateString() }}，祝您工作愉快！</p>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-section">
        <div class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ partyStatistics.total }}</div>
            <div class="stat-label">党员总数</div>
          </div>
          <div class="stat-icon">👥</div>
        </div>
        <div class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ partyStatistics.active }}</div>
            <div class="stat-label">活跃党员</div>
          </div>
          <div class="stat-icon">💚</div>
        </div>
        <div class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ partyStatistics.new }}</div>
            <div class="stat-label">本月新增</div>
          </div>
          <div class="stat-icon">🆕</div>
        </div>
        <div class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ partyStatistics.branches }}</div>
            <div class="stat-label">党支部数</div>
          </div>
          <div class="stat-icon">🏛️</div>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="content-section">
        <!-- 学习情况 -->
        <div class="content-card">
          <div class="card-header">
            <h3>学习情况统计</h3>
          </div>
          <div class="card-body">
            <div class="learning-item" v-for="item in learningStats" :key="item.name">
              <div class="learning-info">
                <div class="learning-name">{{ item.name }}</div>
                <div class="learning-percentage">{{ item.completion }}%</div>
              </div>
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: `${item.completion}%` }"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 最近活动 -->
        <div class="content-card">
          <div class="card-header">
            <h3>最近活动</h3>
            <a href="/admin/activities" class="view-more">查看全部</a>
          </div>
          <div class="card-body">
            <div class="activity-list">
              <div class="activity-item" v-for="(activity, index) in recentActivities" :key="index">
                <div class="activity-icon">📅</div>
                <div class="activity-content">
                  <div class="activity-title">{{ activity.title }}</div>
                  <div class="activity-meta">
                    <span class="activity-branch">{{ activity.branch }}</span>
                    <span class="activity-time">{{ activity.time }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 系统通知 -->
        <div class="content-card">
          <div class="card-header">
            <h3>系统通知</h3>
            <a href="/admin/notifications" class="view-more">查看全部</a>
          </div>
          <div class="card-body">
            <div class="notification-list">
              <div class="notification-item" v-for="(notification, index) in systemNotifications" :key="index">
                <div class="notification-content">
                  <div class="notification-title">{{ notification.title }}</div>
                  <div class="notification-desc">{{ notification.desc }}</div>
                </div>
                <div class="notification-time">{{ notification.time }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<style scoped>
.dashboard-content {
  padding: 20px;
}

/* 欢迎信息 */
.welcome-section {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.welcome-section h2 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.welcome-section p {
  margin: 0;
  color: #7f8c8d;
}

/* 统计卡片 */
.stats-section {
  padding: 20px 0;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.stat-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  border-left: 4px solid #d93025;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #7f8c8d;
}

.stat-icon {
  font-size: 32px;
  color: #d93025;
}

/* 内容区域 */
.content-section {
  padding: 20px 0;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 20px;
}

.content-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-header {
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.view-more {
  font-size: 14px;
  color: #1890ff;
  text-decoration: none;
}

.view-more:hover {
  text-decoration: underline;
}

.card-body {
  padding: 20px;
}

/* 学习情况 */
.learning-item {
  margin-bottom: 15px;
}

.learning-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.learning-name {
  font-size: 14px;
  color: #333;
}

.learning-percentage {
  font-size: 14px;
  font-weight: bold;
  color: #d93025;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background-color: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: #d93025;
  border-radius: 4px;
  transition: width 0.3s ease;
}

/* 最近活动 */
.activity-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f5f5f5;
}

.activity-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.activity-icon {
  font-size: 20px;
  margin-right: 15px;
  margin-top: 3px;
  color: #d93025;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
}

.activity-meta {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #7f8c8d;
}

/* 系统通知 */
.notification-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f5f5f5;
}

.notification-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.notification-content {
  flex: 1;
}

.notification-title {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
}

.notification-desc {
  font-size: 13px;
  color: #7f8c8d;
  line-height: 1.4;
}

.notification-time {
  font-size: 12px;
  color: #bdc3c7;
  white-space: nowrap;
  margin-left: 15px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .content-section {
    grid-template-columns: 1fr;
  }
  
  .stats-section {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .stats-section {
    grid-template-columns: 1fr;
  }
}
</style>