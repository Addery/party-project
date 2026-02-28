<template>
  <div class="employment-detail-page">
    <!-- 导航栏 -->
    <div class="nav-bar">
      <div class="container">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/employment' }">就业服务</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/employment/company-recruitment/list' }">企业招聘</el-breadcrumb-item>
          <el-breadcrumb-item>招聘详情</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content container">
      <div class="row">
        <!-- 左侧详情内容 -->
        <div class="col-md-8">
          <div class="detail-card">
            <!-- 职位基本信息 -->
            <div class="job-header">
              <h1 class="job-title">{{ recruitmentDetail.title }}</h1>
              <div class="job-meta">
                <span class="salary">{{ recruitmentDetail.salary }}</span>
                <span class="location">{{ recruitmentDetail.location }}</span>
                <span class="experience">{{ recruitmentDetail.experience }}</span>
                <span class="education">{{ recruitmentDetail.education }}</span>
              </div>
              <div class="company-info">
                <h3 class="company-name">{{ recruitmentDetail.companyName }}</h3>
                <div class="company-tags">
                  <span class="tag" v-for="tag in recruitmentDetail.companyTags" :key="tag">{{ tag }}</span>
                </div>
              </div>
            </div>

            <!-- 职位详情 -->
            <div class="job-details">
              <div class="detail-section">
                <h4 class="section-title">职位描述</h4>
                <div class="content" v-html="recruitmentDetail.description"></div>
              </div>

              <div class="detail-section">
                <h4 class="section-title">任职要求</h4>
                <div class="content" v-html="recruitmentDetail.requirements"></div>
              </div>

              <div class="detail-section">
                <h4 class="section-title">福利待遇</h4>
                <div class="content" v-html="recruitmentDetail.welfare"></div>
              </div>

              <div class="detail-section">
                <h4 class="section-title">联系方式</h4>
                <div class="contact-info">
                  <div class="contact-item">
                    <span class="label">联系人：</span>
                    <span class="value">{{ recruitmentDetail.contactPerson }}</span>
                  </div>
                  <div class="contact-item">
                    <span class="label">联系电话：</span>
                    <span class="value">{{ recruitmentDetail.contactPhone }}</span>
                  </div>
                  <div class="contact-item">
                    <span class="label">邮箱：</span>
                    <span class="value">{{ recruitmentDetail.contactEmail }}</span>
                  </div>
                  <div class="contact-item">
                    <span class="label">公司地址：</span>
                    <span class="value">{{ recruitmentDetail.companyAddress }}</span>
                  </div>
                </div>
              </div>

              <div class="detail-section">
                <h4 class="section-title">公司介绍</h4>
                <div class="content" v-html="recruitmentDetail.companyIntroduction"></div>
              </div>
            </div>

            <!-- 发布信息 -->
            <div class="publish-info">
              <span>发布时间：{{ recruitmentDetail.publishDate }}</span>
              <span>浏览次数：{{ recruitmentDetail.viewCount }}</span>
            </div>
          </div>
        </div>

        <!-- 右侧侧边栏 -->
        <div class="col-md-4">
          <!-- 申请职位 -->
          <div class="sidebar-card">
            <el-button type="primary" size="large" class="apply-btn">立即申请</el-button>
            <el-button type="default" size="large" class="collect-btn">收藏职位</el-button>
          </div>

          <!-- 相关职位 -->
          <div class="sidebar-card">
            <h4 class="sidebar-title">相关职位</h4>
            <div class="related-jobs">
              <div class="related-job-item" v-for="job in relatedJobs" :key="job.id" @click="goToDetail(job.id)">
                <div class="job-info">
                  <h5 class="job-name">{{ job.title }}</h5>
                  <div class="job-meta-small">
                    <span class="salary">{{ job.salary }}</span>
                    <span class="location">{{ job.location }}</span>
                  </div>
                </div>
                <div class="company-name-small">{{ job.companyName }}</div>
              </div>
            </div>
          </div>

          <!-- 热门职位 -->
          <div class="sidebar-card">
            <h4 class="sidebar-title">热门职位</h4>
            <div class="hot-jobs">
              <div class="hot-job-item" v-for="job in hotJobs" :key="job.id" @click="goToDetail(job.id)">
                <div class="job-info">
                  <h5 class="job-name">{{ job.title }}</h5>
                  <div class="job-meta-small">
                    <span class="salary">{{ job.salary }}</span>
                    <span class="location">{{ job.location }}</span>
                  </div>
                </div>
                <div class="company-name-small">{{ job.companyName }}</div>
              </div>
            </div>
          </div>

          <!-- 企业招聘更多 -->
          <div class="sidebar-card">
            <h4 class="sidebar-title">该企业更多职位</h4>
            <div class="more-jobs">
              <div class="more-job-item" v-for="job in moreCompanyJobs" :key="job.id" @click="goToDetail(job.id)">
                <h5 class="job-name">{{ job.title }}</h5>
                <div class="job-meta-small">
                  <span class="salary">{{ job.salary }}</span>
                  <span class="location">{{ job.location }}</span>
                </div>
              </div>
            </div>
            <el-button type="text" class="more-btn" @click="goToCompanyJobs">查看更多</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import employmentApi from '@/api/employment'

export default {
  name: 'EmploymentCompanyRecruitmentDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const recruitmentDetail = ref({
      id: '',
      title: '前端开发工程师',
      salary: '10-15K',
      location: '北京',
      experience: '1-3年',
      education: '本科及以上',
      companyName: '科技有限公司',
      companyTags: ['互联网', '技术驱动', '初创企业'],
      description: '<p>1. 负责公司前端页面开发和维护</p><p>2. 与后端团队协作，实现前后端数据交互</p><p>3. 优化页面性能，提升用户体验</p>',
      requirements: '<p>1. 本科及以上学历，计算机相关专业</p><p>2. 1-3年前端开发经验</p><p>3. 熟悉Vue.js框架</p><p>4. 良好的代码风格和团队合作精神</p>',
      welfare: '<p>1. 五险一金</p><p>2. 带薪年假</p><p>3. 定期团建</p><p>4. 节日福利</p>',
      contactPerson: '人力资源部',
      contactPhone: '010-12345678',
      contactEmail: 'hr@example.com',
      companyAddress: '北京市朝阳区科技园',
      companyIntroduction: '<p>科技有限公司是一家专注于互联网技术的创新企业，致力于为用户提供优质的产品和服务。</p>',
      publishDate: '2023-12-15',
      viewCount: 1234
    })
    
    const relatedJobs = ref([
      { id: 1, title: '后端开发工程师', salary: '12-18K', location: '北京', companyName: '科技有限公司' },
      { id: 2, title: 'UI设计师', salary: '8-12K', location: '北京', companyName: '科技有限公司' },
      { id: 3, title: '产品经理', salary: '15-20K', location: '北京', companyName: '科技有限公司' }
    ])
    
    const hotJobs = ref([
      { id: 4, title: 'Java开发工程师', salary: '10-15K', location: '北京', companyName: '互联网公司' },
      { id: 5, title: '前端开发工程师', salary: '12-18K', location: '上海', companyName: '科技公司' },
      { id: 6, title: '测试工程师', salary: '8-12K', location: '广州', companyName: '软件公司' }
    ])
    
    const moreCompanyJobs = ref([
      { id: 7, title: '前端开发实习生', salary: '3-5K', location: '北京', companyName: '科技有限公司' },
      { id: 8, title: '运维工程师', salary: '10-15K', location: '北京', companyName: '科技有限公司' }
    ])

    const fetchRecruitmentDetail = async () => {
      try {
        const id = route.params.id
        const response = await employmentApi.getEmploymentCompanyRecruitmentDetail(id)
        recruitmentDetail.value = response.data
      } catch (error) {
        console.error('获取招聘详情失败:', error)
        // 使用模拟数据
      }
    }

    const goToDetail = (id) => {
      router.push(`/employment/company-recruitment/${id}`)
    }

    const goToCompanyJobs = () => {
      router.push('/employment/company-recruitment/list')
    }

    onMounted(() => {
      fetchRecruitmentDetail()
    })

    return {
      recruitmentDetail,
      relatedJobs,
      hotJobs,
      moreCompanyJobs,
      fetchRecruitmentDetail,
      goToDetail,
      goToCompanyJobs
    }
  }
}
</script>

<style scoped>
.employment-detail-page {
  background-color: #f5f7fa;
  min-height: 100vh;
}

.nav-bar {
  background-color: #fff;
  padding: 20px 0;
  border-bottom: 1px solid #e4e7ed;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.main-content {
  padding: 30px 0;
}

.detail-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
  margin-bottom: 30px;
}

.job-header {
  margin-bottom: 30px;
}

.job-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 15px;
}

.job-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
  font-size: 16px;
  color: #606266;
}

.salary {
  color: #f56c6c;
  font-weight: 500;
}

.company-info {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.company-name {
  font-size: 20px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 10px;
}

.company-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag {
  padding: 4px 12px;
  background-color: #ecf5ff;
  color: #409eff;
  border-radius: 4px;
  font-size: 14px;
}

.detail-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #e4e7ed;
}

.content {
  font-size: 16px;
  line-height: 1.8;
  color: #606266;
}

.content p {
  margin-bottom: 15px;
}

.content ul, .content ol {
  margin-bottom: 15px;
  padding-left: 20px;
}

.contact-info {
  font-size: 16px;
  line-height: 2;
  color: #606266;
}

.contact-item {
  display: flex;
  margin-bottom: 10px;
}

.label {
  width: 80px;
  font-weight: 500;
  color: #303133;
}

.publish-info {
  display: flex;
  justify-content: space-between;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
  color: #909399;
  font-size: 14px;
}

/* 侧边栏 */
.sidebar-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.sidebar-title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #e4e7ed;
}

.apply-btn, .collect-btn {
  width: 100%;
  margin-bottom: 10px;
}

.related-jobs, .hot-jobs, .more-jobs {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.related-job-item, .hot-job-item, .more-job-item {
  padding: 15px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e4e7ed;
}

.related-job-item:hover, .hot-job-item:hover, .more-job-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.15);
}

.job-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
}

.job-meta-small {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.company-name-small {
  font-size: 14px;
  color: #909399;
}

.more-btn {
  width: 100%;
  margin-top: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    padding: 20px 0;
  }
  
  .detail-card {
    padding: 20px;
  }
  
  .job-title {
    font-size: 24px;
  }
  
  .job-meta {
    gap: 10px;
    font-size: 14px;
  }
  
  .sidebar-card {
    padding: 15px;
  }
}
</style>