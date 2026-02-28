<template>
  <AdminLayout>
    <div class="data-analysis">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item><a href="#/back/dashboard">首页</a></el-breadcrumb-item>
          <el-breadcrumb-item>数据分析</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2>统计分析</h2>
      </div>

      <!-- 日期范围选择器 -->
      <div class="date-range-section">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          :default-time="['00:00:00', '23:59:59']"
          @change="handleDateRangeChange"
        >
        </el-date-picker>
      </div>



      <!-- 分析模块标签页 -->
      <div class="analysis-modules">
        <el-tabs v-model="activeTab" type="card">
          <!-- 党员结构分析 -->
          <el-tab-pane label="党员结构分析" name="member-structure">
            <div class="module-content">
              <div class="chart-row">
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>党员年龄分布</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-pie
                          :data="ageDistributionData"
                          :angle-field="'value'"
                          :color-field="'name'"
                          :radius="0.8"
                        >
                          <el-pie-label
                            :content="(datum) => `${datum.name}: ${datum.value}人`"
                          ></el-pie-label>
                        </el-pie>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>党员性别分布</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-pie
                          :data="genderDistributionData"
                          :angle-field="'value'"
                          :color-field="'name'"
                          :radius="0.8"
                        >
                          <el-pie-label
                            :content="(datum) => `${datum.name}: ${datum.value}人`"
                          ></el-pie-label>
                        </el-pie>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
              </div>

              <div class="chart-row">
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>党员学历分布</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-bar
                          :data="educationDistributionData"
                          :x-field="'name'"
                          :y-field="'value'"
                          :series-field="'name'"
                          :color="['#1890ff', '#52c41a', '#faad14', '#f5222d', '#722ed1']"
                        >
                          <el-bar-label />
                        </el-bar>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>党员职务分布</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-bar
                          :data="positionDistributionData"
                          :x-field="'name'"
                          :y-field="'value'"
                          :series-field="'name'"
                          :color="['#1890ff', '#52c41a', '#faad14', '#f5222d', '#722ed1']"
                          direction="horizontal"
                        >
                          <el-bar-label />
                        </el-bar>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
              </div>
            </div>
          </el-tab-pane>

          <!-- 活动参与分析 -->
          <el-tab-pane label="活动参与分析" name="activity-participation">
            <div class="module-content">
              <div class="chart-row">
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>活动类型参与统计</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-pie
                          :data="activityTypeData"
                          :angle-field="'value'"
                          :color-field="'name'"
                          :radius="0.8"
                        >
                          <el-pie-label
                            :content="(datum) => `${datum.name}: ${datum.value}次`"
                          ></el-pie-label>
                        </el-pie>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>活动参与率趋势</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-line
                          :data="activityParticipationTrend"
                          :x-field="'month'"
                          :y-field="'rate'"
                          :series-field="'type'"
                          :color="['#1890ff', '#52c41a']"
                        >
                          <el-line-point />
                          <el-line-label />
                          <el-tooltip />
                        </el-line>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
              </div>

              <div class="chart-row">
                <el-col :xs="24">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>各支部活动参与情况</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-bar
                          :data="branchActivityData"
                          :x-field="'name'"
                          :y-field="'participationRate'"
                          :series-field="'name'"
                          :color="['#1890ff']"
                        >
                          <el-bar-label />
                        </el-bar>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
              </div>
            </div>
          </el-tab-pane>

          <!-- 学习教育分析 -->
          <el-tab-pane label="学习教育分析" name="learning-analysis">
            <div class="module-content">
              <div class="chart-row">
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>学习资源类型分布</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-pie
                          :data="learningResourceTypeData"
                          :angle-field="'value'"
                          :color-field="'name'"
                          :radius="0.8"
                        >
                          <el-pie-label
                            :content="(datum) => `${datum.name}: ${datum.value}%`"
                          ></el-pie-label>
                        </el-pie>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>学习时长统计</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-bar
                          :data="learningTimeData"
                          :x-field="'name'"
                          :y-field="'hours'"
                          :series-field="'name'"
                          :color="['#52c41a', '#1890ff', '#faad14', '#f5222d', '#722ed1']"
                        >
                          <el-bar-label />
                        </el-bar>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
              </div>

              <div class="chart-row">
                <el-col :xs="24">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>考试通过率趋势</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-line
                          :data="examPassRateTrend"
                          :x-field="'month'"
                          :y-field="'rate'"
                          :series-field="'type'"
                          :color="['#52c41a']"
                        >
                          <el-line-point />
                          <el-line-label />
                          <el-tooltip />
                        </el-line>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
              </div>
            </div>
          </el-tab-pane>

          <!-- 党费缴纳分析 -->
          <el-tab-pane label="党费缴纳分析" name="dues-analysis">
            <div class="module-content">
              <div class="chart-row">
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>各支部党费缴纳情况</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-bar
                          :data="branchDuesData"
                          :x-field="'name'"
                          :y-field="'paymentRate'"
                          :series-field="'name'"
                          :color="['#1890ff']"
                        >
                          <el-bar-label />
                        </el-bar>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
                <el-col :xs="24" :md="12">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>月度党费缴纳趋势</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-line
                          :data="monthlyDuesTrend"
                          :x-field="'month'"
                          :y-field="'amount'"
                          :series-field="'type'"
                          :color="['#faad14']"
                        >
                          <el-line-point />
                          <el-line-label />
                          <el-tooltip />
                        </el-line>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
              </div>

              <div class="chart-row">
                <el-col :xs="24">
                  <el-card class="chart-card">
                    <template #header>
                      <div class="card-header">
                        <span>党员发展与党费增长关系</span>
                      </div>
                    </template>
                    <div class="chart-container">
                      <el-chart>
                        <el-line
                          :data="membershipDuesRelation"
                          :x-field="'quarter'"
                          :y-field="'value'"
                          :series-field="'type'"
                          :color="['#1890ff', '#faad14']"
                        >
                          <el-line-point />
                          <el-line-label />
                          <el-tooltip />
                        </el-line>
                      </el-chart>
                    </div>
                  </el-card>
                </el-col>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- 数据导出按钮 -->
      <div class="export-section">
        <el-button type="primary" @click="handleExportData">
          <el-icon><Download /></el-icon> 导出数据
        </el-button>
      </div>
    </div>
  </AdminLayout>
</template>

<script>
import { ref } from 'vue';
import AdminLayout from '@/components/AdminLayout.vue';
import { Download } from '@element-plus/icons-vue';

export default {
  name: 'DataAnalysis',
  components: {
    AdminLayout
  },
  setup() {
    // 日期范围
    const dateRange = ref([new Date(new Date().getFullYear(), 0, 1), new Date()]);
    
    // 活跃标签页
    const activeTab = ref('member-structure');



    // 党员年龄分布数据
    const ageDistributionData = ref([
      { name: '25岁以下', value: 32 },
      { name: '26-35岁', value: 85 },
      { name: '36-45岁', value: 68 },
      { name: '46-55岁', value: 42 },
      { name: '56岁以上', value: 29 }
    ]);

    // 党员性别分布数据
    const genderDistributionData = ref([
      { name: '男性', value: 168 },
      { name: '女性', value: 88 }
    ]);

    // 党员学历分布数据
    const educationDistributionData = ref([
      { name: '大专及以下', value: 42 },
      { name: '本科', value: 128 },
      { name: '硕士', value: 65 },
      { name: '博士', value: 21 }
    ]);

    // 确保所有数据都有值
    if (!ageDistributionData.value || ageDistributionData.value.length === 0) {
      ageDistributionData.value = [
        { name: '25岁以下', value: 0 },
        { name: '26-35岁', value: 0 },
        { name: '36-45岁', value: 0 },
        { name: '46-55岁', value: 0 },
        { name: '56岁以上', value: 0 }
      ];
    }

    // 党员职务分布数据
    const positionDistributionData = ref([
      { name: '普通党员', value: 182 },
      { name: '支委', value: 45 },
      { name: '支部书记', value: 12 },
      { name: '党委委员', value: 10 },
      { name: '党委书记', value: 7 }
    ]);

    // 活动类型参与数据
    const activityTypeData = ref([
      { name: '主题党日', value: 48 },
      { name: '三会一课', value: 72 },
      { name: '组织生活会', value: 12 },
      { name: '民主评议', value: 6 },
      { name: '其他活动', value: 24 }
    ]);

    // 活动参与率趋势数据
    const activityParticipationTrend = ref([
      { month: '1月', rate: 92, type: '参与率' },
      { month: '2月', rate: 95, type: '参与率' },
      { month: '3月', rate: 93, type: '参与率' },
      { month: '4月', rate: 96, type: '参与率' },
      { month: '5月', rate: 94, type: '参与率' },
      { month: '6月', rate: 97, type: '参与率' },
      { month: '7月', rate: 95, type: '参与率' },
      { month: '8月', rate: 98, type: '参与率' },
      { month: '9月', rate: 96, type: '参与率' },
      { month: '10月', rate: 94, type: '参与率' },
      { month: '11月', rate: 97, type: '参与率' },
      { month: '12月', rate: 99, type: '参与率' }
    ]);

    // 各支部活动参与情况数据
    const branchActivityData = ref([
      { name: '第一支部', participationRate: 98 },
      { name: '第二支部', participationRate: 96 },
      { name: '第三支部', participationRate: 94 },
      { name: '第四支部', participationRate: 99 },
      { name: '第五支部', participationRate: 95 },
      { name: '第六支部', participationRate: 97 }
    ]);

    // 学习资源类型分布数据
    const learningResourceTypeData = ref([
      { name: '文章', value: 45 },
      { name: '视频', value: 30 },
      { name: '音频', value: 10 },
      { name: '文档', value: 15 }
    ]);

    // 学习时长统计数据
    const learningTimeData = ref([
      { name: '第一支部', hours: 1250 },
      { name: '第二支部', hours: 1180 },
      { name: '第三支部', hours: 1320 },
      { name: '第四支部', hours: 1450 },
      { name: '第五支部', hours: 1280 }
    ]);

    // 考试通过率趋势数据
    const examPassRateTrend = ref([
      { month: '1月', rate: 88, type: '通过率' },
      { month: '2月', rate: 90, type: '通过率' },
      { month: '3月', rate: 85, type: '通过率' },
      { month: '4月', rate: 92, type: '通过率' },
      { month: '5月', rate: 94, type: '通过率' },
      { month: '6月', rate: 91, type: '通过率' },
      { month: '7月', rate: 93, type: '通过率' },
      { month: '8月', rate: 95, type: '通过率' },
      { month: '9月', rate: 92, type: '通过率' },
      { month: '10月', rate: 94, type: '通过率' },
      { month: '11月', rate: 96, type: '通过率' },
      { month: '12月', rate: 97, type: '通过率' }
    ]);

    // 各支部党费缴纳情况数据
    const branchDuesData = ref([
      { name: '第一支部', paymentRate: 99 },
      { name: '第二支部', paymentRate: 98 },
      { name: '第三支部', paymentRate: 97 },
      { name: '第四支部', paymentRate: 100 },
      { name: '第五支部', paymentRate: 96 },
      { name: '第六支部', paymentRate: 99 }
    ]);

    // 月度党费缴纳趋势数据
    const monthlyDuesTrend = ref([
      { month: '1月', amount: 12500, type: '金额' },
      { month: '2月', amount: 12800, type: '金额' },
      { month: '3月', amount: 13200, type: '金额' },
      { month: '4月', amount: 13500, type: '金额' },
      { month: '5月', amount: 13800, type: '金额' },
      { month: '6月', amount: 14200, type: '金额' },
      { month: '7月', amount: 14500, type: '金额' },
      { month: '8月', amount: 14800, type: '金额' },
      { month: '9月', amount: 15200, type: '金额' },
      { month: '10月', amount: 15500, type: '金额' },
      { month: '11月', amount: 15800, type: '金额' },
      { month: '12月', amount: 16200, type: '金额' }
    ]);

    // 党员发展与党费增长关系数据
    const membershipDuesRelation = ref([
      { quarter: '第一季度', value: 240, type: '党员数' },
      { quarter: '第二季度', value: 245, type: '党员数' },
      { quarter: '第三季度', value: 250, type: '党员数' },
      { quarter: '第四季度', value: 256, type: '党员数' },
      { quarter: '第一季度', value: 38000, type: '党费总额' },
      { quarter: '第二季度', value: 39500, type: '党费总额' },
      { quarter: '第三季度', value: 41000, type: '党费总额' },
      { quarter: '第四季度', value: 42500, type: '党费总额' }
    ]);

    // 日期范围变化处理
    const handleDateRangeChange = (value) => {
      console.log('日期范围变化:', value);
      // 这里可以添加根据日期范围更新数据的逻辑
    };

    // 导出数据处理
    const handleExportData = () => {
      // 这里可以添加导出数据的逻辑
      console.log('导出数据');
      alert('数据导出功能已触发');
    };

    return {
      dateRange,
      activeTab,
      ageDistributionData,
      genderDistributionData,
      educationDistributionData,
      positionDistributionData,
      activityTypeData,
      activityParticipationTrend,
      branchActivityData,
      learningResourceTypeData,
      learningTimeData,
      examPassRateTrend,
      branchDuesData,
      monthlyDuesTrend,
      membershipDuesRelation,
      handleDateRangeChange,
      handleExportData
    };
  }
};
</script>

<style scoped>
/* 现代化主题设计 - 统计分析界面 */

/* 主题变量定义 */
:root {
  /* 主色调 */
  --primary-color: #3b82f6;
  --primary-dark: #2563eb;
  --primary-light: #dbeafe;
  
  /* 辅助色 */
  --secondary-color: #8b5cf6;
  --success-color: #10b981;
  --warning-color: #f59e0b;
  --danger-color: #ef4444;
  --info-color: #06b6d4;
  
  /* 文本色 */
  --text-primary: #1f2937;
  --text-secondary: #6b7280;
  --text-muted: #9ca3af;
  
  /* 背景色 */
  --bg-primary: #ffffff;
  --bg-secondary: #f9fafb;
  --bg-tertiary: #f3f4f6;
  --bg-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  
  /* 边框和阴影 */
  --border-color: #e5e7eb;
  --border-radius: 12px;
  --border-radius-sm: 8px;
  --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  --shadow-2xl: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  
  /* 过渡动画 */
  --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  --transition-fast: all 0.2s ease;
}

/* 全局容器 */
.data-analysis {
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 确保analysis-modules占满剩余空间 */
.analysis-modules {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-bottom: 25px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.breadcrumb {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 10px 15px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

/* 面包屑样式 */
.breadcrumb {
  margin-bottom: 16px;
  font-size: 14px;
}

/* 页面头部样式 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e9ecef;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #212529;
}

.date-range-section {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.key-metrics {
  margin-bottom: 25px;
  width: 100%;
}

.metric-card {
  height: 120px;
  display: flex;
  align-items: center;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  border: none;
  overflow: hidden;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, var(--bg-primary) 0%, #fafbfc 100%);
  position: relative;
}

.metric-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, var(--primary-color), var(--secondary-color));
  opacity: 0;
  transition: var(--transition);
}

.metric-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-xl);
  border-color: var(--primary-color);
}

.metric-card:hover::before {
  opacity: 1;
}

.metric-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 15px;
}

.metric-value {
  font-size: 30px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.metric-label {
  font-size: 14px;
  color: #606266;
}

.metric-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  margin-left: auto;
  background-size: 30px;
  background-repeat: no-repeat;
  background-position: center;
}

.user-icon {
  background-color: #e6f7ff;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%231890ff"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>');
}

.add-user-icon {
  background-color: #f6ffed;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%2352c41a"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm5 11h-4v4h-2v-4H7v-2h4V7h2v4h4v2z"/></svg>');
}

.active-icon {
  background-color: #fff7e6;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23fa8c16"><path d="M12 7c-2.76 0-5 2.24-5 5s2.24 5 5 5 5-2.24 5-5-2.24-5-5-5zm0 9c-1.66 0-3-1.34-3-3s1.34-3 3-3 3 1.34 3 3-1.34 3-3 3zm-9-9c0-4.97 4.03-9 9-9s9 4.03 9 9c0 .46-.04.92-.1 1.36-.98 1.37-2.58 2.26-4.4 2.26h-4.2c-1.82 0-3.42-.89-4.4-2.26C3.04 11.92 3 11.46 3 11zm1.2 8.2c-.12.4-.19.83-.19 1.28 0 2.21 1.79 4 4 4h4c2.21 0 4-1.79 4-4 0-.45-.07-.88-.19-1.28C15.58 16.33 14.08 17 12.5 17h-1c-1.58 0-3.08-.67-4.3-2.8z"/></svg>');
}

.money-icon {
  background-color: #f9f0ff;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23722ed1"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"/></svg>');
}

.analysis-modules {
  margin-bottom: 25px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.el-tabs {
  border-top: none;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.el-tabs__content {
  flex: 1;
  overflow-y: auto;
}

.el-tabs__header {
  margin: 0;
  background-color: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
  padding: 0 20px;
}

.el-tabs__nav-wrap {
  border-bottom: none;
}

.el-tabs__item {
  font-size: 15px;
  font-weight: 500;
  padding: 15px 20px;
  margin-right: 20px;
  color: #6c757d;
  transition: all 0.3s ease;
  position: relative;
  border-radius: var(--border-radius-sm) var(--border-radius-sm) 0 0;
}

.el-tabs__item:hover {
  color: var(--primary-color);
  background-color: rgba(59, 130, 246, 0.05);
}

.el-tabs__item.is-active {
  color: var(--primary-color);
  font-weight: 600;
  background-color: var(--bg-primary);
  box-shadow: 0 -2px 0 0 var(--primary-color) inset;
}

.el-tabs__active-bar {
  background-color: #007bff;
  height: 2px;
}

.module-content {
  padding: 25px;
  background-color: #fff;
  max-width: 100%;
  margin: 0 auto;
}

/* 调整图表卡片和容器的布局 */
.el-col {
  padding: 0 10px;
}

.chart-row {
  margin-bottom: 20px;
  margin-left: -10px;
  margin-right: -10px;
}

.chart-card {
  min-height: 400px;
  display: flex;
  flex-direction: column;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  border: none;
  overflow: hidden;
  height: 100%;
  width: 100%;
  transition: all 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.chart-card .el-card__header {
  padding: 15px 20px;
  background-color: #fafafa;
  border-bottom: 1px solid #e4e7ed;
}

.card-header {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.chart-container {
  flex: 1;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  max-width: 100%;
  overflow: hidden;
}

.export-section {
  text-align: right;
  margin-top: 20px;
  background-color: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.export-section .el-button {
  border-radius: 4px;
  font-size: 14px;
  padding: 8px 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .data-analysis {
    padding: 10px;
  }

  .page-title {
    font-size: 18px;
    margin-bottom: 15px;
  }

  .date-range-section {
    padding: 10px 15px;
  }

  .metric-card {
    margin-bottom: 15px;
  }

  .metric-value {
    font-size: 24px;
  }

  .metric-icon {
    width: 50px;
    height: 50px;
    background-size: 25px;
  }

  .module-content {
    padding: 15px;
  }

  .chart-card {
    min-height: 300px;
    margin-bottom: 15px;
  }

  .chart-container {
    padding: 10px;
  }

  .export-section {
    text-align: center;
    padding: 10px 15px;
  }

  .export-section .el-button {
    width: 100%;
  }
}

/* 大屏幕优化 */
@media (min-width: 1200px) {
  .data-analysis {
    padding: 30px;
  }
  
  .module-content {
    padding: 30px;
  }
  
  .chart-card {
    min-height: 450px;
  }
  
  .chart-row {
    margin-bottom: 30px;
  }
}
</style>
