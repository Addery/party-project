<template>
  <AdminLayout>
    <div class="theme-party-day">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>核心党务工作</el-breadcrumb-item>
        <el-breadcrumb-item>主题党日</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2>主题党日活动管理</h2>
        <el-button type="primary" @click="showCreateDialog">
          <el-icon><Plus /></el-icon> 创建活动
        </el-button>
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
          v-model="filterStatus"
          placeholder="活动状态"
          style="width: 150px; margin-right: 10px;"
        >
          <el-option label="全部" value="" />
          <el-option label="待审批" value="待审批" />
          <el-option label="已审批" value="已审批" />
          <el-option label="已发布" value="已发布" />
          <el-option label="进行中" value="进行中" />
          <el-option label="已结束" value="已结束" />
          <el-option label="已取消" value="已取消" />
        </el-select>
        <el-select
          v-model="filterType"
          placeholder="活动类型"
          style="width: 150px; margin-right: 10px;"
        >
          <el-option label="全部" value="" />
          <el-option label="校园文化" value="校园文化" />
          <el-option label="志愿服务" value="志愿服务" />
          <el-option label="社会实践" value="社会实践" />
          <el-option label="理论学习" value="理论学习" />
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
        <el-table-column type="selection" width="65" />
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="title" label="活动名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="活动类型" width="120" />
        <el-table-column prop="organization" label="发起组织" min-width="180" />
        <el-table-column prop="date" label="活动时间" width="200" />
        <el-table-column prop="location" label="活动地点" min-width="160" />
        <el-table-column prop="participantCount" label="参与人数" width="120" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusColor(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="scope">
            <el-space size="small">
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
                v-if="scope.row.status !== '已取消' && scope.row.status !== '已结束'"
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
              <el-option label="校园文化" value="校园文化" />
              <el-option label="志愿服务" value="志愿服务" />
              <el-option label="社会实践" value="社会实践" />
              <el-option label="理论学习" value="理论学习" />
            </el-select>
          </el-form-item>
          <el-form-item label="发起组织" required>
            <el-select v-model="activityForm.organization" placeholder="请选择发起组织">
              <el-option label="校党委" value="校党委" />
              <el-option label="计算机学院党总支" value="计算机学院党总支" />
              <el-option label="软件学院党支部" value="软件学院党支部" />
              <el-option label="网络工程系党小组" value="网络工程系党小组" />
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
          <div class="detail-header">
            <h3>{{ currentActivity.title }}</h3>
            <el-tag :type="getStatusColor(currentActivity.status)">
              {{ currentActivity.status }}
            </el-tag>
          </div>
          <div class="detail-info">
            <div class="info-row">
              <span class="label">活动类型：</span>
              <span class="value">{{ currentActivity.type }}</span>
            </div>
            <div class="info-row">
              <span class="label">发起组织：</span>
              <span class="value">{{ currentActivity.organization }}</span>
            </div>
            <div class="info-row">
              <span class="label">活动时间：</span>
              <span class="value">{{ currentActivity.date }}</span>
            </div>
            <div class="info-row">
              <span class="label">活动地点：</span>
              <span class="value">{{ currentActivity.location }}</span>
            </div>
            <div class="info-row">
              <span class="label">活动负责人：</span>
              <span class="value">{{ currentActivity.responsiblePerson }}</span>
            </div>
            <div class="info-row">
              <span class="label">联系电话：</span>
              <span class="value">{{ currentActivity.contactPhone }}</span>
            </div>
            <div class="info-row">
              <span class="label">活动预算：</span>
              <span class="value">{{ currentActivity.budget || '无' }}</span>
            </div>
            <div class="info-row">
              <span class="label">参与人数：</span>
              <span class="value">{{ currentActivity.participantCount }}人</span>
            </div>
          </div>
          <div class="detail-content">
            <h4>活动内容：</h4>
            <div class="content-text">{{ currentActivity.content }}</div>
          </div>
          <div class="detail-actions" v-if="currentActivity.status === '进行中'">
            <el-button type="primary" @click="showSignInDialog">
              <el-icon><Check /></el-icon> 签到
            </el-button>
            <el-button type="success" @click="showSummaryDialog">
              <el-icon><EditPen /></el-icon> 提交总结
            </el-button>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="viewDialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 签到对话框 -->
      <el-dialog
        v-model="signInDialogVisible"
        title="活动签到"
        width="600px"
      >
        <div class="sign-in-dialog">
          <h4>{{ currentActivity?.title }} - 签到</h4>
          <div class="sign-in-form">
            <el-form-item label="党员姓名">
              <el-input v-model="signInForm.name" placeholder="请输入党员姓名" />
            </el-form-item>
            <el-form-item label="党员ID">
              <el-input v-model="signInForm.id" placeholder="请输入党员ID" />
            </el-form-item>
            <el-form-item label="签到方式">
              <el-radio-group v-model="signInForm.method">
                <el-radio label="扫码签到" />
                <el-radio label="手动签到" />
              </el-radio-group>
            </el-form-item>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="signInDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitSignIn">确认签到</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 总结对话框 -->
      <el-dialog
        v-model="summaryDialogVisible"
        title="提交活动总结"
        width="800px"
      >
        <div class="summary-dialog">
          <h4>{{ currentActivity?.title }} - 活动总结</h4>
          <el-form :model="summaryForm">
            <el-form-item label="活动总结" required>
              <el-input
                v-model="summaryForm.content"
                type="textarea"
                :rows="10"
                placeholder="请输入活动总结内容"
              />
            </el-form-item>
            <el-form-item label="活动图片">
              <el-upload
                class="upload-demo"
                action="#"
                :file-list="summaryFileList"
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
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="summaryDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitSummary">提交总结</el-button>
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
import { Plus, Edit, Delete, View, Check, EditPen } from '@element-plus/icons-vue'

// 响应式数据
const searchKeyword = ref('')
const filterStatus = ref('')
const filterType = ref('')
const dateRange = ref('')
const currentPage = ref(1)
const pageSize = ref(10)

// 对话框相关
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const signInDialogVisible = ref(false)
const summaryDialogVisible = ref(false)
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

// 签到表单
const signInForm = ref({
  name: '',
  id: '',
  method: '手动签到'
})

// 总结表单
const summaryForm = ref({
  content: ''
})

// 文件上传
const fileList = ref([])
const summaryFileList = ref([])

// 模拟数据
const activities = ref([
  {
    id: 1,
    title: '学习贯彻党的二十大精神主题党日活动',
    type: '理论学习',
    organization: '校党委',
    date: '2023-10-15 14:30',
    location: '大礼堂',
    responsiblePerson: '张书记',
    contactPhone: '13800138001',
    content: '组织全校党员学习贯彻党的二十大精神，深入领会报告的核心要义...',
    budget: '5000元',
    participantCount: 200,
    status: '已结束'
  },
  {
    id: 2,
    title: '志愿服务进社区主题党日活动',
    type: '志愿服务',
    organization: '计算机学院党总支',
    date: '2023-10-20 09:00',
    location: '阳光社区',
    responsiblePerson: '李院长',
    contactPhone: '13800138002',
    content: '组织党员志愿者到社区开展志愿服务活动，包括垃圾分类宣传、健康义诊等...',
    budget: '3000元',
    participantCount: 50,
    status: '进行中'
  },
  {
    id: 3,
    title: '红色教育基地参观主题党日活动',
    type: '社会实践',
    organization: '软件学院党支部',
    date: '2023-10-25 08:00',
    location: '革命纪念馆',
    responsiblePerson: '王书记',
    contactPhone: '13800138003',
    content: '组织党员参观革命纪念馆，接受红色教育，缅怀革命先烈...',
    budget: '4000元',
    participantCount: 30,
    status: '待审批'
  },
  {
    id: 4,
    title: '校园文化建设主题党日活动',
    type: '校园文化',
    organization: '网络工程系党小组',
    date: '2023-10-30 15:00',
    location: '校园广场',
    responsiblePerson: '赵组长',
    contactPhone: '13800138004',
    content: '组织党员参与校园文化建设，包括环境美化、文化展板布置等...',
    budget: '2000元',
    participantCount: 20,
    status: '已审批'
  },
  {
    id: 5,
    title: '学习习近平总书记重要讲话精神',
    type: '理论学习',
    organization: '校党委',
    date: '2023-09-28 14:30',
    location: '行政楼101会议室',
    responsiblePerson: '张书记',
    contactPhone: '13800138001',
    content: '组织中层以上干部学习习近平总书记重要讲话精神...',
    budget: '1000元',
    participantCount: 80,
    status: '已结束'
  },
  {
    id: 6,
    title: '关爱留守儿童主题党日活动',
    type: '志愿服务',
    organization: '计算机学院党总支',
    date: '2023-09-20 09:00',
    location: '希望小学',
    responsiblePerson: '李院长',
    contactPhone: '13800138002',
    content: '组织党员志愿者到希望小学开展关爱留守儿童活动...',
    budget: '2500元',
    participantCount: 40,
    status: '已结束'
  },
  {
    id: 7,
    title: '乡村振兴调研主题党日活动',
    type: '社会实践',
    organization: '软件学院党支部',
    date: '2023-09-15 08:00',
    location: '新农村建设示范村',
    responsiblePerson: '王书记',
    contactPhone: '13800138003',
    content: '组织党员到新农村建设示范村开展调研活动...',
    budget: '3500元',
    participantCount: 25,
    status: '已结束'
  },
  {
    id: 8,
    title: '校园义务劳动主题党日活动',
    type: '校园文化',
    organization: '网络工程系党小组',
    date: '2023-09-10 14:30',
    location: '校园绿地',
    responsiblePerson: '赵组长',
    contactPhone: '13800138004',
    content: '组织党员开展校园义务劳动，美化校园环境...',
    budget: '500元',
    participantCount: 15,
    status: '已结束'
  },
  {
    id: 9,
    title: '学习党章党规主题党日活动',
    type: '理论学习',
    organization: '校党委',
    date: '2023-08-28 14:30',
    location: '各支部会议室',
    responsiblePerson: '张书记',
    contactPhone: '13800138001',
    content: '组织各支部党员学习党章党规，增强党性修养...',
    budget: '800元',
    participantCount: 300,
    status: '已结束'
  },
  {
    id: 10,
    title: '疫情防控志愿服务主题党日活动',
    type: '志愿服务',
    organization: '计算机学院党总支',
    date: '2023-08-20 09:00',
    location: '校园门口',
    responsiblePerson: '李院长',
    contactPhone: '13800138002',
    content: '组织党员志愿者参与校园疫情防控工作...',
    budget: '1500元',
    participantCount: 30,
    status: '已结束'
  },
  {
    id: 11,
    title: '红色电影观影主题党日活动',
    type: '理论学习',
    organization: '软件学院党支部',
    date: '2023-08-15 19:00',
    location: '学术报告厅',
    responsiblePerson: '王书记',
    contactPhone: '13800138003',
    content: '组织党员观看红色电影，接受爱国主义教育...',
    budget: '1000元',
    participantCount: 60,
    status: '已结束'
  },
  {
    id: 12,
    title: '新生入学志愿服务主题党日活动',
    type: '志愿服务',
    organization: '网络工程系党小组',
    date: '2023-09-01 08:00',
    location: '校园各报到点',
    responsiblePerson: '赵组长',
    contactPhone: '13800138004',
    content: '组织党员志愿者为新生提供入学服务...',
    budget: '1200元',
    participantCount: 20,
    status: '已结束'
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
  
  // 按状态筛选
  if (filterStatus.value) {
    result = result.filter(activity => 
      activity.status === filterStatus.value
    )
  }
  
  // 按类型筛选
  if (filterType.value) {
    result = result.filter(activity => 
      activity.type === filterType.value
    )
  }
  
  // 按日期范围筛选
  if (dateRange.value && dateRange.value.length === 2) {
    // 使用JavaScript原生Date方法处理日期范围
    const startDate = new Date(dateRange.value[0])
    const endDate = new Date(dateRange.value[1])
    
    // 检查日期是否有效
    if (!isNaN(startDate.getTime()) && !isNaN(endDate.getTime())) {
      // 格式化日期为YYYY-MM-DD HH:mm格式
      const formatDate = (date) => {
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')
        return `${year}-${month}-${day} ${hours}:${minutes}`
      }
      
      const formattedStartDate = formatDate(startDate)
      const formattedEndDate = formatDate(endDate)
      
      result = result.filter(activity => 
        activity && activity.date && activity.date >= formattedStartDate && activity.date <= formattedEndDate
      )
    }
  }
  
  return result
})

// 分页数据
const paginatedActivities = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredActivities.value.slice(start, end)
})

// 方法：获取状态颜色
const getStatusColor = (status) => {
  switch (status) {
    case '待审批': return 'info'
    case '已审批': return 'warning'
    case '已发布': return 'success'
    case '进行中': return 'primary'
    case '已结束': return 'success'
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
  filterStatus.value = ''
  filterType.value = ''
  dateRange.value = ''
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
    ElMessage.confirm('确定要审批通过该活动吗？', '审批确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success'
    }).then(() => {
      const index = activities.value.findIndex(item => item.id === activity.id)
      if (index !== -1) {
        activities.value[index].status = '已审批'
        ElMessage.success('审批通过')
      }
    }).catch(() => {
      // 取消审批
    })
  } else if (activity.status === '已审批') {
    ElMessage.confirm('确定要发布该活动吗？', '发布确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      const index = activities.value.findIndex(item => item.id === activity.id)
      if (index !== -1) {
        activities.value[index].status = '进行中'
        ElMessage.success('活动已发布')
      }
    }).catch(() => {
      // 取消发布
    })
  }
}

// 方法：取消活动
const cancelActivity = (activity) => {
  ElMessage.confirm('确定要取消该活动吗？', '取消确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    const index = activities.value.findIndex(item => item.id === activity.id)
    if (index !== -1) {
      activities.value[index].status = '已取消'
      ElMessage.success('活动已取消')
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
  if (!activityForm.value.title || !activityForm.value.type || !activityForm.value.organization || !activityForm.value.date || !activityForm.value.location || !activityForm.value.responsiblePerson || !activityForm.value.contactPhone || !activityForm.value.content) {
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
      participantCount: 0
    }
    activities.value.unshift(newActivity)
    ElMessage.success('创建成功')
  }
  
  dialogVisible.value = false
  resetForm()
}

// 方法：显示签到对话框
const showSignInDialog = () => {
  signInDialogVisible.value = true
}

// 方法：提交签到
const submitSignIn = () => {
  if (!signInForm.value.name || !signInForm.value.id) {
    ElMessage.warning('请填写党员姓名和ID')
    return
  }
  
  ElMessage.success('签到成功')
  signInDialogVisible.value = false
  signInForm.value = {
    name: '',
    id: '',
    method: '手动签到'
  }
}

// 方法：显示总结对话框
const showSummaryDialog = () => {
  summaryDialogVisible.value = true
}

// 方法：提交总结
const submitSummary = () => {
  if (!summaryForm.value.content) {
    ElMessage.warning('请填写活动总结内容')
    return
  }
  
  ElMessage.success('总结提交成功')
  summaryDialogVisible.value = false
  summaryForm.value = {
    content: ''
  }
  summaryFileList.value = []
}

// 文件上传相关方法
const handleRemove = (file) => {
  const index = fileList.value.indexOf(file)
  if (index !== -1) {
    fileList.value.splice(index, 1)
  }
  
  const summaryIndex = summaryFileList.value.indexOf(file)
  if (summaryIndex !== -1) {
    summaryFileList.value.splice(summaryIndex, 1)
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
  console.log('主题党日活动数据加载完成')
})
</script>

<style scoped>
.theme-party-day {
  margin: 0 auto;
  padding: 24px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

.breadcrumb {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0;
  color: #212529;
  font-size: 24px;
  font-weight: 600;
}

.page-header .el-button {
  background-color: #c72c41;
  border-color: #c72c41;
}

.page-header .el-button:hover {
  background-color: #a82336;
  border-color: #a82336;
}

.search-filter {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
  padding: 20px;
  background-color: #ffffff;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

.search-filter > * {
  margin-bottom: 0;
}

.search-filter .el-button {
  border-radius: 4px;
}

.search-filter .el-button--primary {
  background-color: #c72c41;
  border-color: #c72c41;
}

.search-filter .el-button--primary:hover {
  background-color: #a82336;
  border-color: #a82336;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  padding: 16px 0;
}

.pagination .el-pagination__sizes {
  margin-right: 8px;
}

.pagination .el-pagination__total {
  color: #606266;
}

.activity-detail {
  padding: 24px 0;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

.detail-header h3 {
  margin: 0;
  color: #212529;
  font-size: 20px;
  font-weight: 600;
}

.detail-info {
  margin-bottom: 24px;
}

.info-row {
  margin-bottom: 20px;
  display: flex;
  align-items: flex-start;
}

.info-row .label {
  width: 120px;
  font-weight: 500;
  color: #495057;
  margin-right: 20px;
  padding-top: 4px;
}

.info-row .value {
  color: #212529;
  flex: 1;
}

.detail-content {
  margin-bottom: 24px;
}

.detail-content h4 {
  margin-bottom: 16px;
  color: #495057;
  font-size: 16px;
  font-weight: 600;
}

.content-text {
  line-height: 1.6;
  white-space: pre-wrap;
  color: #212529;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}

.detail-actions {
  display: flex;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid #e9ecef;
}

.sign-in-dialog h4,
.summary-dialog h4 {
  margin-bottom: 24px;
  color: #212529;
  font-weight: 600;
}

.sign-in-form {
  margin-bottom: 24px;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-table) {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

:deep(.el-table__header-wrapper) {
  background-color: #f8f9fa;
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

:deep(.el-table__header tr th) {
  background-color: #f8f9fa;
  font-weight: 500;
  color: #495057;
  border-bottom: 2px solid #e9ecef;
  padding: 12px 10px;
  white-space: nowrap; /* 强制表头单行显示 */
  line-height: normal;
  height: auto;
}

:deep(.el-table__header tr th .cell) {
  white-space: nowrap; /* 确保表头文字不换行 */
}

:deep(.el-table__body tr:hover) {
  background-color: #f8f9fa;
}

:deep(.el-table__body tr td) {
  padding: 12px 10px;
  border-bottom: 1px solid #f0f0f0;
  white-space: nowrap; /* 表格内容单行显示 */
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 确保固定列的层级高于滚动内容 */
:deep(.el-table__fixed-right) {
  z-index: 10;
}

:deep(.el-table__body tr:last-child td) {
  border-bottom: none;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #495057;
}

:deep(.el-form-item__content) {
  color: #212529;
}

:deep(.el-dialog) {
  border-radius: 8px;
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.15);
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid #e9ecef;
  padding: 20px 24px;
}

:deep(.el-dialog__title) {
  font-weight: 600;
  color: #212529;
  font-size: 18px;
}

:deep(.el-dialog__body) {
  padding: 24px;
}

:deep(.el-dialog__footer) {
  border-top: 1px solid #e9ecef;
  padding: 16px 24px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .theme-party-day {
    padding: 16px;
  }
  
  .search-filter {
    flex-direction: column;
    align-items: stretch;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .detail-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>