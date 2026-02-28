<template>
  <div class="employment-services-page">
    <!-- 导航栏 -->
    <div class="nav-bar">
      <div class="container">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/employment' }">就业服务</el-breadcrumb-item>
          <el-breadcrumb-item>就业服务列表</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">就业服务</h2>
        <p class="page-subtitle">提供全方位的就业服务支持</p>
      </div>

      <!-- 搜索筛选区 -->
      <div class="search-filter">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="服务名称">
            <el-input v-model="searchForm.serviceName" placeholder="请输入服务名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="服务类型">
            <el-select v-model="searchForm.serviceType" placeholder="请选择服务类型" clearable>
              <el-option label="职业指导" value="职业指导"></el-option>
              <el-option label="技能培训" value="技能培训"></el-option>
              <el-option label="创业支持" value="创业支持"></el-option>
              <el-option label="政策咨询" value="政策咨询"></el-option>
              <el-option label="招聘会" value="招聘会"></el-option>
              <el-option label="其他服务" value="其他服务"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="服务对象">
            <el-select v-model="searchForm.serviceObject" placeholder="请选择服务对象" clearable>
              <el-option label="毕业生" value="毕业生"></el-option>
              <el-option label="失业人员" value="失业人员"></el-option>
              <el-option label="下岗职工" value="下岗职工"></el-option>
              <el-option label="农民工" value="农民工"></el-option>
              <el-option label="残疾人" value="残疾人"></el-option>
              <el-option label="其他" value="其他"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="row">
        <!-- 左侧服务列表 -->
        <div class="col-md-8">
          <div class="services-list">
            <div class="service-item" v-for="service in servicesList" :key="service.id">
              <div class="service-content">
                <div class="service-header">
                  <h3 class="service-title">{{ service.title }}</h3>
                  <span class="service-type">{{ service.serviceType }}</span>
                </div>
                <div class="service-meta">
                  <span class="service-object">服务对象：{{ service.serviceObject }}</span>
                  <span class="service-provider">提供单位：{{ service.provider }}</span>
                  <span class="publish-date">发布日期：{{ service.publishDate }}</span>
                </div>
                <div class="service-description">{{ service.description }}</div>
                <div class="service-tags">
                  <span class="tag" v-for="tag in service.tags" :key="tag">{{ tag }}</span>
                </div>
                <div class="service-footer">
                  <el-button type="primary" size="small" @click="goToServiceDetail(service.id)">查看详情</el-button>
                  <span class="view-count">浏览：{{ service.viewCount }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 分页 -->
          <div class="pagination">
            <el-pagination
              v-model:current-page="pagination.currentPage"
              v-model:page-size="pagination.pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="pagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            ></el-pagination>
          </div>
        </div>

        <!-- 右侧侧边栏 -->
        <div class="col-md-4">
          <!-- 热门服务 -->
          <div class="sidebar-card">
            <h4 class="sidebar-title">热门服务</h4>
            <div class="hot-services">
              <div class="hot-service-item" v-for="service in hotServices" :key="service.id" @click="goToServiceDetail(service.id)">
                <div class="service-info">
                  <h5 class="service-name">{{ service.title }}</h5>
                  <div class="service-meta-small">
                    <span class="service-type">{{ service.serviceType }}</span>
                    <span class="view-count">{{ service.viewCount }}次浏览</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 服务分类 -->
          <div class="sidebar-card">
            <h4 class="sidebar-title">服务分类</h4>
            <div class="service-categories">
              <div class="category-item" v-for="category in serviceCategories" :key="category.id">
                <div class="category-info">
                  <span class="category-name">{{ category.name }}</span>
                  <span class="category-count">({{ category.count }})</span>
                </div>
                <el-progress :percentage="category.percentage" :stroke-width="6" :show-text="false"></el-progress>
              </div>
            </div>
          </div>

          <!-- 最新服务 -->
          <div class="sidebar-card">
            <h4 class="sidebar-title">最新服务</h4>
            <div class="latest-services">
              <div class="latest-service-item" v-for="service in latestServices" :key="service.id" @click="goToServiceDetail(service.id)">
                <h5 class="service-name">{{ service.title }}</h5>
                <div class="service-meta-small">
                  <span class="service-type">{{ service.serviceType }}</span>
                  <span class="publish-date">{{ service.publishDate }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 服务指南 -->
          <div class="sidebar-card">
            <h4 class="sidebar-title">服务指南</h4>
            <div class="guide-content">
              <p>就业服务是为了帮助求职者和用人单位解决就业和招聘问题而提供的各种服务。</p>
              <ul>
                <li>了解就业政策和法规</li>
                <li>参加职业技能培训</li>
                <li>获取就业信息和招聘会</li>
                <li>享受创业扶持政策</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import employmentApi from '@/api/employment'

export default {
  name: 'EmploymentServicesList',
  setup() {
    const router = useRouter()
    
    // 搜索表单
    const searchForm = ref({
      serviceName: '',
      serviceType: '',
      serviceObject: ''
    })
    
    // 服务列表
    const servicesList = ref([
      {
        id: 1,
        title: '2023年高校毕业生就业指导服务',
        serviceType: '职业指导',
        serviceObject: '毕业生',
        provider: '市人力资源和社会保障局',
        description: '为2023年高校毕业生提供全方位的就业指导服务，包括简历制作、面试技巧、职业规划等内容。',
        tags: ['毕业生', '就业指导', '免费'],
        publishDate: '2023-12-10',
        viewCount: 1234
      },
      {
        id: 2,
        title: '创业培训及扶持政策解读',
        serviceType: '创业支持',
        serviceObject: '创业者',
        provider: '市就业服务中心',
        description: '提供创业培训课程和创业扶持政策解读，帮助有意创业的人员了解创业流程和政策支持。',
        tags: ['创业', '培训', '政策'],
        publishDate: '2023-12-08',
        viewCount: 892
      },
      {
        id: 3,
        title: '职业技能提升培训课程',
        serviceType: '技能培训',
        serviceObject: '失业人员',
        provider: '市职业技能培训中心',
        description: '开设多种职业技能提升培训课程，包括计算机操作、电子商务、市场营销等，帮助失业人员提升就业能力。',
        tags: ['技能培训', '失业人员', '补贴'],
        publishDate: '2023-12-05',
        viewCount: 1567
      }
    ])
    
    // 分页信息
    const pagination = ref({
      currentPage: 1,
      pageSize: 10,
      total: 30
    })
    
    // 热门服务
    const hotServices = ref([
      {
        id: 1,
        title: '2023年高校毕业生就业指导服务',
        serviceType: '职业指导',
        viewCount: 1234
      },
      {
        id: 3,
        title: '职业技能提升培训课程',
        serviceType: '技能培训',
        viewCount: 1567
      },
      {
        id: 2,
        title: '创业培训及扶持政策解读',
        serviceType: '创业支持',
        viewCount: 892
      }
    ])
    
    // 服务分类
    const serviceCategories = ref([
      {
        id: 1,
        name: '职业指导',
        count: 12,
        percentage: 35
      },
      {
        id: 2,
        name: '技能培训',
        count: 10,
        percentage: 30
      },
      {
        id: 3,
        name: '创业支持',
        count: 5,
        percentage: 15
      },
      {
        id: 4,
        name: '政策咨询',
        count: 3,
        percentage: 10
      },
      {
        id: 5,
        name: '其他服务',
        count: 3,
        percentage: 10
      }
    ])
    
    // 最新服务
    const latestServices = ref([
      {
        id: 1,
        title: '2023年高校毕业生就业指导服务',
        serviceType: '职业指导',
        publishDate: '2023-12-10'
      },
      {
        id: 2,
        title: '创业培训及扶持政策解读',
        serviceType: '创业支持',
        publishDate: '2023-12-08'
      },
      {
        id: 3,
        title: '职业技能提升培训课程',
        serviceType: '技能培训',
        publishDate: '2023-12-05'
      }
    ])

    // 获取服务列表
    const fetchServicesList = async () => {
      try {
        const params = {
          page: pagination.value.currentPage,
          limit: pagination.value.pageSize,
          ...searchForm.value
        }
        const response = await employmentApi.getEmploymentServices(params)
        servicesList.value = response.data.list
        pagination.value.total = response.data.total
      } catch (error) {
        console.error('获取服务列表失败:', error)
        // 使用模拟数据
      }
    }

    // 搜索
    const handleSearch = () => {
      pagination.value.currentPage = 1
      fetchServicesList()
    }

    // 重置
    const handleReset = () => {
      searchForm.value = {
        serviceName: '',
        serviceType: '',
        serviceObject: ''
      }
      pagination.value.currentPage = 1
      fetchServicesList()
    }

    // 分页大小变化
    const handleSizeChange = (size) => {
      pagination.value.pageSize = size
      fetchServicesList()
    }

    // 当前页码变化
    const handleCurrentChange = (current) => {
      pagination.value.currentPage = current
      fetchServicesList()
    }

    // 跳转到服务详情
    const goToServiceDetail = (id) => {
      router.push(`/employment/services/${id}`)
    }

    onMounted(() => {
      fetchServicesList()
    })

    return {
      searchForm,
      servicesList,
      pagination,
      hotServices,
      serviceCategories,
      latestServices,
      fetchServicesList,
      handleSearch,
      handleReset,
      handleSizeChange,
      handleCurrentChange,
      goToServiceDetail
    }
  }
}
</script>

<style scoped>
.employment-services-page {
  background-color: #f5f7fa;
  min-height: 100vh;
}

.nav-bar {
  background-color: #fff;
  padding: 20px 0;
  border-bottom: 1px solid #e4e7ed;
}

.main-content {
  padding: 30px 0;
}

.page-header {
  margin-bottom: 30px;
  text-align: center;
}

.page-title {
  font-size: 32px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
}

.page-subtitle {
  font-size: 16px;
  color: #606266;
}

/* 搜索筛选区 */
.search-filter {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 30px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

/* 服务列表 */
.services-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.service-item {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 25px;
  transition: all 0.3s;
}

.service-item:hover {
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.service-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.service-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
  flex: 1;
}

.service-type {
  padding: 4px 12px;
  background-color: #ecf5ff;
  color: #409eff;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
}

.service-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 15px;
  font-size: 14px;
  color: #606266;
}

.service-description {
  font-size: 16px;
  line-height: 1.6;
  color: #606266;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.service-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 15px;
}

.tag {
  padding: 2px 8px;
  background-color: #f0f9eb;
  color: #67c23a;
  border-radius: 4px;
  font-size: 12px;
}

.service-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.view-count {
  font-size: 14px;
  color: #909399;
}

/* 分页 */
.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
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

.hot-services, .latest-services {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.hot-service-item, .latest-service-item {
  padding: 15px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e4e7ed;
}

.hot-service-item:hover, .latest-service-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.15);
}

.service-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 10px;
}

.service-meta-small {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #606266;
}

/* 服务分类 */
.service-categories {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.category-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.category-info {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #606266;
}

.category-name {
  font-weight: 500;
}

.category-count {
  color: #909399;
}

/* 服务指南 */
.guide-content {
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
}

.guide-content p {
  margin-bottom: 10px;
}

.guide-content ul {
  padding-left: 20px;
}

.guide-content li {
  margin-bottom: 5px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    padding: 20px 0;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .search-filter {
    padding: 15px;
  }
  
  .search-form {
    gap: 10px;
  }
  
  .service-item {
    padding: 20px;
  }
  
  .service-header {
    flex-direction: column;
    gap: 10px;
  }
  
  .service-meta {
    gap: 10px;
  }
  
  .sidebar-card {
    padding: 15px;
  }
}
</style>