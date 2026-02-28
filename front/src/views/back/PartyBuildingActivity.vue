<template>
  <AdminLayout>
    <div class="party-building-activity">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>党建工作</el-breadcrumb-item>
        <el-breadcrumb-item>党建活动管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2>党建活动管理</h2>
        <el-button type="primary" @click="showCreateDialog">
          <el-icon><Plus /></el-icon> 创建活动
        </el-button>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ activities.length }}</div>
            <div class="stat-label">活动总数</div>
          </div>
          <div class="stat-icon">
            <el-icon class="icon-lg"><Calendar /></el-icon>
          </div>
        </el-card>
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ filteredActivities.filter(a => a.status === '进行中').length }}</div>
            <div class="stat-label">进行中</div>
          </div>
          <div class="stat-icon">
            <el-icon class="icon-lg"><Timer /></el-icon>
          </div>
        </el-card>
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ filteredActivities.filter(a => a.status === '已完成').length }}</div>
            <div class="stat-label">已完成</div>
          </div>
          <div class="stat-icon">
            <el-icon class="icon-lg"><Check /></el-icon>
          </div>
        </el-card>
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ filteredActivities.filter(a => a.status === '待审批').length }}</div>
            <div class="stat-label">待审批</div>
          </div>
          <div class="stat-icon">
            <el-icon class="icon-lg"><Document /></el-icon>
          </div>
        </el-card>
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ totalParticipants }}</div>
            <div class="stat-label">总参与人数</div>
          </div>
          <div class="stat-icon">
            <el-icon class="icon-lg"><User /></el-icon>
          </div>
        </el-card>
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ activityTypes.length }}</div>
            <div class="stat-label">活动类型</div>
          </div>
          <div class="stat-icon">
            <el-icon class="icon-lg"><Grid /></el-icon>
          </div>
        </el-card>
      </div>

      <!-- 搜索和筛选区域 -->
      <div class="search-filter">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索活动名称"
          prefix-icon="Search"
          style="width: 300px; margin-right: 10px;"
        />
        <el-select
          v-model="filterType"
          placeholder="活动类型"
          style="width: 150px; margin-right: 10px;"
        >
          <el-option label="全部" value="" />
          <el-option
            v-for="type in activityTypes"
            :key="type"
            :label="type"
            :value="type"
          />
        </el-select>
        <el-select
          v-model="filterStatus"
          placeholder="活动状态"
          style="width: 150px; margin-right: 10px;"
        >
          <el-option label="全部" value="" />
          <el-option label="待审批" value="待审批" />
          <el-option label="已审批" value="已审批" />
          <el-option label="已发布" value="已发布" />
          <el-option label="进行中" value="进行中" />
          <el-option label="已完成" value="已完成" />
          <el-option label="已取消" value="已取消" />
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px; margin-right: 10px;"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetFilter">重置</el-button>
      </div>

      <!-- 活动列表 -->
      <div class="table-container">
        <el-table :data="paginatedActivities" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="title" label="活动名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="活动类型" width="120" />
        <el-table-column prop="organization" label="发起组织" min-width="180" show-overflow-tooltip />
        <el-table-column prop="date" label="活动时间" width="180" />
        <el-table-column prop="location" label="活动地点" min-width="150" show-overflow-tooltip />
        <el-table-column prop="participantCount" label="参与人数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusColor(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-space>
              <el-button size="small" @click="viewActivity(scope.row)">
                <el-icon><View /></el-icon> 查看
              </el-button>
              <el-button size="small" type="primary" @click="editActivity(scope.row)">
                <el-icon><Edit /></el-icon> 编辑
              </el-button>
              <el-button
                size="small"
                :type="scope.row.status === '待审批' ? 'success' : 'warning'"
                @click="handleApprove(scope.row)"
                v-if="['待审批', '已审批'].includes(scope.row.status)"
              >
                <el-icon><Check /></el-icon>
                {{ scope.row.status === '待审批' ? '审批' : '发布' }}
              </el-button>
              <el-button
                size="small"
                type="danger"
                @click="cancelActivity(scope.row)"
                v-if="scope.row.status !== '已取消' && scope.row.status !== '已完成'"
              >
                <el-icon><Delete /></el-icon> 取消
              </el-button>
            </el-space>
          </template>
        </el-table-column>
      </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="filteredActivities.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 创建/编辑活动对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="800px"
        @close="resetForm"
      >
        <el-form :model="activityForm" label-width="100px">
          <el-form-item label="活动名称" required>
            <el-input v-model="activityForm.title" placeholder="请输入活动名称" />
          </el-form-item>
          <el-form-item label="活动类型" required>
            <el-select v-model="activityForm.type" placeholder="请选择活动类型">
              <el-option label="主题党日" value="主题党日" />
              <el-option label="三会一课" value="三会一课" />
              <el-option label="志愿服务" value="志愿服务" />
              <el-option label="理论学习" value="理论学习" />
              <el-option label="其他活动" value="其他活动" />
            </el-select>
          </el-form-item>
          <el-form-item label="发起组织" required>
            <el-select v-model="activityForm.organization" placeholder="请选择发起组织">
              <el-option label="校党委" value="校党委" />
              <el-option label="计算机学院党总支" value="计算机学院党总支" />
              <el-option label="软件学院党支部" value="软件学院党支部" />
              <el-option label="网络工程系党小组" value="网络工程系党小组" />
              <el-option label="电子工程系党支部" value="电子工程系党支部" />
              <el-option label="自动化系党小组" value="自动化系党小组" />
            </el-select>
          </el-form-item>
          <el-form-item label="活动时间" required>
            <el-date-picker
              v-model="activityForm.date"
              type="datetime"
              placeholder="选择活动时间"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="活动地点" required>
            <el-input v-model="activityForm.location" placeholder="请输入活动地点" />
          </el-form-item>
          <el-form-item label="活动负责人" required>
            <el-input v-model="activityForm.responsiblePerson" placeholder="请输入活动负责人" />
          </el-form-item>
          <el-form-item label="联系电话" required>
            <el-input v-model="activityForm.contactPhone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="活动内容" required>
            <el-input
              v-model="activityForm.content"
              type="textarea"
              :rows="6"
              placeholder="请输入活动内容"
            />
          </el-form-item>
          <el-form-item label="活动预算">
            <el-input v-model="activityForm.budget" placeholder="请输入活动预算" />
          </el-form-item>
          <el-form-item label="活动图片">
            <el-upload
              class="upload-demo"
              action="#"
              :file-list="fileList"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :on-success="handleSuccess"
              :auto-upload="false"
              list-type="picture-card"
            >
              <el-icon><Plus /></el-icon>
              <template #tip>
                <div class="el-upload__tip">
                  只能上传jpg/png文件，且不超过500kb
                </div>
              </template>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveActivity">保存</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 活动详情对话框 -->
      <el-dialog
        v-model="viewDialogVisible"
        title="活动详情"
        width="800px"
      >
        <div v-if="currentActivity" class="activity-detail">
          <div class="detail-row">
            <label>活动名称：</label>
            <span>{{ currentActivity.title }}</span>
          </div>
          <div class="detail-row">
            <label>活动类型：</label>
            <span>{{ currentActivity.type }}</span>
          </div>
          <div class="detail-row">
            <label>发起组织：</label>
            <span>{{ currentActivity.organization }}</span>
          </div>
          <div class="detail-row">
            <label>活动时间：</label>
            <span>{{ currentActivity.date }}</span>
          </div>
          <div class="detail-row">
            <label>活动地点：</label>
            <span>{{ currentActivity.location }}</span>
          </div>
          <div class="detail-row">
            <label>活动负责人：</label>
            <span>{{ currentActivity.responsiblePerson }}</span>
          </div>
          <div class="detail-row">
            <label>联系电话：</label>
            <span>{{ currentActivity.contactPhone }}</span>
          </div>
          <div class="detail-row">
            <label>活动内容：</label>
            <div class="content-display">{{ currentActivity.content }}</div>
          </div>
          <div class="detail-row">
            <label>活动预算：</label>
            <span>{{ currentActivity.budget || '未设置' }}</span>
          </div>
          <div class="detail-row">
            <label>参与人数：</label>
            <span>{{ currentActivity.participantCount }}</span>
          </div>
          <div class="detail-row">
            <label>状态：</label>
            <el-tag :type="getStatusColor(currentActivity.status)">
              {{ currentActivity.status }}
            </el-tag>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="viewDialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import AdminLayout from '@/components/AdminLayout.vue'
import { Plus, Edit, Delete, View, Check, Calendar, Timer, Document, User, Grid } from '@element-plus/icons-vue'

// 响应式数据
const searchKeyword = ref('')
const filterType = ref('')
const filterStatus = ref('')
const dateRange = ref([])
const currentPage = ref(1)
const pageSize = ref(10)

// 对话框相关
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('创建活动')
const currentActivity = ref(null)
const isEdit = ref(false)

// 表单数据
const activityForm = ref({
  id: '',
  title: '',
  type: '',
  organization: '',
  date: '',
  location: '',
  responsiblePerson: '',
  contactPhone: '',
  content: '',
  budget: '',
  participantCount: 0,
  status: '待审批'
})

// 文件上传
const fileList = ref([])

// 模拟数据
const activities = ref([
  {
    id: 1,
    title: '学习贯彻习近平新时代中国特色社会主义思想主题党日',
    type: '主题党日',
    organization: '校党委',
    date: '2023-10-20 09:00',
    location: '大礼堂',
    responsiblePerson: '张书记',
    contactPhone: '13800138001',
    content: '深入学习贯彻习近平新时代中国特色社会主义思想，推动党建工作高质量发展。',
    budget: '5000元',
    participantCount: 200,
    status: '已完成'
  },
  {
    id: 2,
    title: '支部委员会会议',
    type: '三会一课',
    organization: '计算机学院党总支',
    date: '2023-10-15 14:30',
    location: '计算机学院会议室',
    responsiblePerson: '李院长',
    contactPhone: '13800138002',
    content: '讨论近期党建工作安排和党员发展事宜。',
    budget: '0',
    participantCount: 12,
    status: '已完成'
  },
  {
    id: 3,
    title: '社区志愿服务活动',
    type: '志愿服务',
    organization: '软件学院党支部',
    date: '2023-10-25 08:30',
    location: '阳光社区',
    responsiblePerson: '王老师',
    contactPhone: '13800138003',
    content: '组织党员参与社区环境卫生整治和疫情防控宣传。',
    budget: '1000元',
    participantCount: 35,
    status: '进行中'
  },
  {
    id: 4,
    title: '学习党的二十大精神党课',
    type: '三会一课',
    organization: '校党委',
    date: '2023-11-05 14:30',
    location: '大礼堂',
    responsiblePerson: '张书记',
    contactPhone: '13800138001',
    content: '深入学习党的二十大精神，推动学校各项事业发展。',
    budget: '3000元',
    participantCount: 0,
    status: '待审批'
  },
  {
    id: 5,
    title: '党员先锋岗创建活动',
    type: '其他活动',
    organization: '电子工程系党支部',
    date: '2023-10-18 10:00',
    location: '电子工程系',
    responsiblePerson: '赵主任',
    contactPhone: '13800138004',
    content: '创建党员先锋岗，发挥党员先锋模范作用。',
    budget: '2000元',
    participantCount: 28,
    status: '已发布'
  },
  {
    id: 6,
    title: '党史学习教育专题组织生活会',
    type: '三会一课',
    organization: '网络工程系党小组',
    date: '2023-10-10 16:00',
    location: '网络工程系会议室',
    responsiblePerson: '刘组长',
    contactPhone: '13800138005',
    content: '召开党史学习教育专题组织生活会，开展批评与自我批评。',
    budget: '0',
    participantCount: 18,
    status: '已完成'
  },
  {
    id: 7,
    title: '乡村振兴调研活动',
    type: '社会实践',
    organization: '校党委',
    date: '2023-11-10 08:00',
    location: '对口帮扶村',
    responsiblePerson: '张书记',
    contactPhone: '13800138001',
    content: '组织党员干部到对口帮扶村开展乡村振兴调研和帮扶活动。',
    budget: '8000元',
    participantCount: 0,
    status: '待审批'
  },
  {
    id: 8,
    title: '入党积极分子培训班',
    type: '理论学习',
    organization: '校党委',
    date: '2023-10-22 09:00',
    location: '学术报告厅',
    responsiblePerson: '李老师',
    contactPhone: '13800138006',
    content: '对入党积极分子进行党的基本理论和基本知识培训。',
    budget: '4000元',
    participantCount: 50,
    status: '进行中'
  },
  {
    id: 9,
    title: '“我为群众办实事”主题党日',
    type: '主题党日',
    organization: '计算机学院党总支',
    date: '2023-10-28 09:00',
    location: '计算机学院',
    responsiblePerson: '李院长',
    contactPhone: '13800138002',
    content: '开展“我为群众办实事”主题党日活动，解决师生实际问题。',
    budget: '2000元',
    participantCount: 45,
    status: '已发布'
  },
  {
    id: 10,
    title: '支部党员大会',
    type: '三会一课',
    organization: '自动化系党小组',
    date: '2023-10-12 14:30',
    location: '自动化系会议室',
    responsiblePerson: '陈组长',
    contactPhone: '13800138007',
    content: '召开支部党员大会，讨论预备党员转正事宜。',
    budget: '0',
    participantCount: 22,
    status: '已完成'
  },
  {
    id: 11,
    title: '疫情防控志愿服务',
    type: '志愿服务',
    organization: '软件学院党支部',
    date: '2023-10-30 08:00',
    location: '学校大门',
    responsiblePerson: '王老师',
    contactPhone: '13800138003',
    content: '组织党员参与学校疫情防控志愿服务，测量体温、查验健康码。',
    budget: '500元',
    participantCount: 30,
    status: '进行中'
  },
  {
    id: 12,
    title: '党的章程学习研讨会',
    type: '理论学习',
    organization: '电子工程系党支部',
    date: '2023-11-02 16:00',
    location: '电子工程系会议室',
    responsiblePerson: '赵主任',
    contactPhone: '13800138004',
    content: '组织党员学习党的章程，研讨如何加强党性修养。',
    budget: '0',
    participantCount: 25,
    status: '已发布'
  }
])

// 计算属性：筛选后的活动列表
const filteredActivities = computed(() => {
  let result = [...activities.value]
  
  // 按关键词筛选
  if (searchKeyword.value) {
    result = result.filter(activity => 
      activity.title.includes(searchKeyword.value)
    )
  }
  
  // 按活动类型筛选
  if (filterType.value) {
    result = result.filter(activity => 
      activity.type === filterType.value
    )
  }
  
  // 按活动状态筛选
  if (filterStatus.value) {
    result = result.filter(activity => 
      activity.status === filterStatus.value
    )
  }
  
  // 按日期范围筛选
  if (dateRange.value && dateRange.value.length === 2) {
    const startDate = dateRange.value[0]
    const endDate = dateRange.value[1]
    result = result.filter(activity => {
      const activityDate = new Date(activity.date)
      return activityDate >= startDate && activityDate <= endDate
    })
  }
  
  return result
})

// 计算属性：分页数据
const paginatedActivities = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredActivities.value.slice(start, end)
})

// 计算属性：活动类型列表
const activityTypes = computed(() => {
  const types = new Set()
  activities.value.forEach(activity => {
    types.add(activity.type)
  })
  return Array.from(types)
})

// 计算属性：总参与人数
const totalParticipants = computed(() => {
  return activities.value.reduce((total, activity) => total + activity.participantCount, 0)
})

// 方法：获取状态颜色
const getStatusColor = (status) => {
  switch (status) {
    case '已完成': return 'success'
    case '进行中': return 'warning'
    case '待审批': return 'info'
    case '已审批': return 'primary'
    case '已发布': return 'primary'
    case '已取消': return 'danger'
    default: return ''
  }
}

// 方法：搜索
const handleSearch = () => {
  currentPage.value = 1
}

// 方法：重置筛选
const resetFilter = () => {
  searchKeyword.value = ''
  filterType.value = ''
  filterStatus.value = ''
  dateRange.value = []
  currentPage.value = 1
}

// 方法：分页
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 方法：显示创建对话框
const showCreateDialog = () => {
  dialogTitle.value = '创建活动'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 方法：编辑活动
const editActivity = (activity) => {
  dialogTitle.value = '编辑活动'
  isEdit.value = true
  activityForm.value = { ...activity }
  dialogVisible.value = true
}

// 方法：查看活动详情
const viewActivity = (activity) => {
  currentActivity.value = activity
  viewDialogVisible.value = true
}

// 方法：审批/发布活动
const handleApprove = (activity) => {
  if (activity.status === '待审批') {
    // 审批活动
    ElMessage.confirm('确定要审批该活动吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }).then(() => {
      const index = activities.value.findIndex(item => item.id === activity.id)
      if (index !== -1) {
        activities.value[index].status = '已审批'
        ElMessage.success('审批成功')
      }
    }).catch(() => {
      // 取消审批
    })
  } else if (activity.status === '已审批') {
    // 发布活动
    ElMessage.confirm('确定要发布该活动吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }).then(() => {
      const index = activities.value.findIndex(item => item.id === activity.id)
      if (index !== -1) {
        activities.value[index].status = '已发布'
        ElMessage.success('发布成功')
      }
    }).catch(() => {
      // 取消发布
    })
  }
}

// 方法：取消活动
const cancelActivity = (activity) => {
  ElMessage.confirm('确定要取消该活动吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = activities.value.findIndex(item => item.id === activity.id)
    if (index !== -1) {
      activities.value[index].status = '已取消'
      ElMessage.success('取消成功')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 方法：重置表单
const resetForm = () => {
  activityForm.value = {
    id: '',
    title: '',
    type: '',
    organization: '',
    date: '',
    location: '',
    responsiblePerson: '',
    contactPhone: '',
    content: '',
    budget: '',
    participantCount: 0,
    status: '待审批'
  }
  fileList.value = []
}

// 方法：保存活动
const saveActivity = () => {
  // 简单验证
  if (!activityForm.value.title || !activityForm.value.type || !activityForm.value.organization || !activityForm.value.date || !activityForm.value.location) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  if (isEdit.value) {
    // 编辑模式
    const index = activities.value.findIndex(activity => activity.id === activityForm.value.id)
    if (index !== -1) {
      activities.value[index] = { ...activityForm.value }
      ElMessage.success('编辑成功')
    }
  } else {
    // 创建模式
    const newActivity = {
      ...activityForm.value,
      id: Date.now(), // 简单生成ID
      participantCount: 0,
      status: '待审批'
    }
    activities.value.unshift(newActivity)
    ElMessage.success('创建成功')
  }
  
  dialogVisible.value = false
  resetForm()
}

// 文件上传相关方法
const handleRemove = (file) => {
  const index = fileList.value.indexOf(file)
  if (index !== -1) {
    fileList.value.splice(index, 1)
  }
}

const handlePreview = (file) => {
  console.log(file)
}

const handleSuccess = (response, file, fileList) => {
  console.log(response, file, fileList)
  ElMessage.success('文件上传成功')
}

// 生命周期钩子
onMounted(() => {
  // 模拟从后端获取数据
  console.log('党建活动数据加载完成')
})
</script>

<style scoped>
.party-building-activity {
  padding: 24px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

.breadcrumb {
  margin-bottom: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #2c3e50;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  border-left: 4px solid #409eff;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 100%;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

.stat-icon {
  position: absolute;
  top: 10px;
  right: 10px;
  color: #409eff;
  opacity: 0.3;
}

.icon-lg {
  font-size: 48px;
}

.search-filter {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.table-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  overflow: hidden;
  margin-bottom: 16px;
  position: relative;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .table-container {
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
  }
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.activity-detail {
  padding: 20px 0;
}

.detail-row {
  margin-bottom: 15px;
  display: flex;
  align-items: flex-start;
}

.detail-row label {
  width: 120px;
  font-weight: bold;
  color: #606266;
  margin-right: 20px;
}

.detail-row span {
  color: #303133;
}

.content-display {
  line-height: 1.6;
  white-space: pre-wrap;
}

.dialog-footer {
  text-align: right;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .search-filter {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-filter .el-input,
  .search-filter .el-select,
  .search-filter .el-date-picker {
    margin-right: 0 !important;
    margin-bottom: 10px;
  }
  
  .stats-cards {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
  
  .stat-number {
    font-size: 24px;
  }
  
  .icon-lg {
    font-size: 36px;
  }
}
</style>