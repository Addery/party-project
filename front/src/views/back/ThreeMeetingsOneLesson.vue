<template>
  <AdminLayout>
    <div class="three-meetings-one-lesson">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>核心党务工作</el-breadcrumb-item>
        <el-breadcrumb-item>三会一课</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题与操作区 -->
      <div class="page-header">
        <h2>三会一课管理</h2>
        <el-button type="primary" @click="showCreateDialog">
          <el-icon><Plus /></el-icon> +创建会议
        </el-button>
      </div>

      <!-- 搜索筛选区域 -->
      <div class="search-filter">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索会议名称"
          prefix-icon="Search"
          style="width: 300px; margin-right: 10px;"
        />
        <el-select
          v-model="filterType"
          placeholder="会议类型"
          style="width: 150px; margin-right: 10px;"
        >
          <el-option label="全部" value="" />
          <el-option label="支部党员大会" value="党员大会" />
          <el-option label="支部委员会" value="支部委员会" />
          <el-option label="党小组会" value="党小组会" />
          <el-option label="党课" value="党课" />
        </el-select>
        <el-select
          v-model="filterStatus"
          placeholder="会议状态"
          style="width: 150px; margin-right: 10px;"
        >
          <el-option label="全部" value="" />
          <el-option label="已完成" value="已完成" />
          <el-option label="进行中" value="进行中" />
          <el-option label="待开展" value="待开展" />
        </el-select>
        <el-date-picker
          v-model="filterDate"
          type="month"
          placeholder="选择月份"
          style="width: 150px; margin-right: 10px;"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetFilter">重置</el-button>
      </div>

      <!-- 会议列表 -->
      <div class="table-container">
        <el-table
          v-if="paginatedMeetings.length > 0"
          :data="paginatedMeetings"
          border
          style="width: 100%"
        >
          <el-table-column type="selection" width="65" />
          <el-table-column prop="id" label="ID" width="80" align="center" />
          <el-table-column prop="title" label="会议名称" min-width="200" show-overflow-tooltip>
            <template #default="scope">
              <span class="meeting-title">{{ scope.row.title }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="会议类型" width="130">
            <template #default="scope">
              <el-tag :type="getTypeColor(scope.row.type)">
                {{ scope.row.type }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="organization" label="所属组织" min-width="150" show-overflow-tooltip />
          <el-table-column prop="date" label="会议时间" width="180" />
          <el-table-column prop="location" label="会议地点" min-width="150" show-overflow-tooltip />
          <el-table-column prop="status" label="会议状态" width="110">
            <template #default="scope">
              <el-tag :type="getStatusColor(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="attendance" label="出勤率" width="100" />
          <el-table-column label="操作" width="320" fixed="right">
            <template #default="scope">
              <el-space size="small">
                <el-button size="small" @click="viewMeeting(scope.row)">
                  <el-icon><View /></el-icon> 查看
                </el-button>
                <el-button size="small" type="primary" @click="editMeeting(scope.row)">
                  <el-icon><Edit /></el-icon> 编辑
                </el-button>
                <el-button size="small" type="danger" @click="deleteMeeting(scope.row.id)">
                  <el-icon><Delete /></el-icon> 删除
                </el-button>
              </el-space>
            </template>
          </el-table-column>
        </el-table>
        <div v-else class="empty-state">
          <el-empty description="暂无会议数据" />
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="filteredMeetings.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 创建/编辑会议对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="700px"
        @close="resetForm"
      >
        <el-form :model="meetingForm" label-width="120px" class="meeting-form">
          <el-form-item label="会议名称" required>
            <el-input v-model="meetingForm.title" placeholder="请输入会议名称" />
          </el-form-item>
          <el-form-item label="会议类型" required>
            <el-select v-model="meetingForm.type" placeholder="请选择会议类型">
              <el-option label="支部党员大会" value="党员大会" />
              <el-option label="支部委员会" value="支部委员会" />
              <el-option label="党小组会" value="党小组会" />
              <el-option label="党课" value="党课" />
            </el-select>
          </el-form-item>
          <el-form-item label="所属组织" required>
            <el-select v-model="meetingForm.organization" placeholder="请选择所属组织">
              <el-option label="校党委" value="校党委" />
              <el-option label="计算机学院党总支" value="计算机学院党总支" />
              <el-option label="软件学院党支部" value="软件学院党支部" />
              <el-option label="网络工程系党小组" value="网络工程系党小组" />
            </el-select>
          </el-form-item>
          <el-form-item label="会议时间" required>
            <el-date-picker
              v-model="meetingForm.date"
              type="datetime"
              placeholder="选择会议时间"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="会议地点" required>
            <el-input v-model="meetingForm.location" placeholder="请输入会议地点" />
          </el-form-item>
          <el-form-item label="主持人">
            <el-input v-model="meetingForm.host" placeholder="请输入主持人" />
          </el-form-item>
          <el-form-item label="参会人员">
            <el-input
              v-model="meetingForm.participants"
              type="textarea"
              :rows="3"
              placeholder="请输入参会人员，用逗号分隔"
            />
          </el-form-item>
          <el-form-item label="会议内容">
            <el-input
              v-model="meetingForm.content"
              type="textarea"
              :rows="5"
              placeholder="请输入会议内容"
            />
          </el-form-item>
          <el-form-item label="出勤率">
            <el-input v-model="meetingForm.attendance" placeholder="请输入出勤率，例如：100%" />
          </el-form-item>
          <el-form-item label="会议状态">
            <el-select v-model="meetingForm.status" placeholder="请选择会议状态">
              <el-option label="已完成" value="已完成" />
              <el-option label="进行中" value="进行中" />
              <el-option label="待开展" value="待开展" />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveMeeting">保存</el-button>
          </div>
        </template>
      </el-dialog>

      <!-- 会议详情对话框 -->
      <el-dialog
        v-model="viewDialogVisible"
        title="会议详情"
        width="700px"
      >
        <div v-if="currentMeeting" class="meeting-detail">
          <div class="detail-section">
            <h3 class="detail-title">{{ currentMeeting.title }}</h3>
            <div class="detail-meta">
              <el-tag :type="getTypeColor(currentMeeting.type)">
                {{ currentMeeting.type }}
              </el-tag>
              <el-tag :type="getStatusColor(currentMeeting.status)">
                {{ currentMeeting.status }}
              </el-tag>
            </div>
          </div>
          <div class="detail-content">
            <div class="detail-item">
              <span class="detail-label">所属组织：</span>
              <span class="detail-value">{{ currentMeeting.organization }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">会议时间：</span>
              <span class="detail-value">{{ currentMeeting.date }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">会议地点：</span>
              <span class="detail-value">{{ currentMeeting.location }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">主持人：</span>
              <span class="detail-value">{{ currentMeeting.host || '未设置' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">参会人员：</span>
              <span class="detail-value">{{ currentMeeting.participants || '未设置' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">出勤率：</span>
              <span class="detail-value">{{ currentMeeting.attendance }}</span>
            </div>
            <div class="detail-item detail-content-full">
              <span class="detail-label">会议内容：</span>
              <div class="detail-value-full">{{ currentMeeting.content || '未设置' }}</div>
            </div>
          </div>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="viewDialogVisible = false">关闭</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import AdminLayout from '@/components/AdminLayout.vue'
import { View, Edit, Delete } from '@element-plus/icons-vue'

// 响应式数据
const searchKeyword = ref('')
const filterType = ref('')
const filterStatus = ref('')
const filterDate = ref('')
const currentPage = ref(1)
const pageSize = ref(20)

// 对话框相关
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('创建会议')
const currentMeeting = ref(null)
const isEdit = ref(false)

// 表单数据
const meetingForm = ref({
  id: '',
  title: '',
  type: '',
  organization: '',
  date: '',
  location: '',
  host: '',
  participants: '',
  content: '',
  attendance: '100%',
  status: '已完成'
})

// 模拟数据
const meetings = ref([
  {
    id: 1,
    title: '学习贯彻习近平新时代中国特色社会主义思想',
    type: '党课',
    organization: '校党委',
    date: '2023-10-15 14:30',
    location: '行政楼101会议室',
    host: '张书记',
    participants: '全体党员',
    content: '深入学习习近平新时代中国特色社会主义思想的核心要义，贯彻落实党的二十大精神，推动党建工作高质量发展。',
    attendance: '98%',
    status: '已完成'
  },
  {
    id: 2,
    title: '支部委员会会议',
    type: '支部委员会',
    organization: '计算机学院党总支',
    date: '2023-10-10 09:00',
    location: '计算机学院会议室',
    host: '李院长',
    participants: '支部委员',
    content: '讨论近期党建工作安排，研究发展新党员事宜，部署主题党日活动。',
    attendance: '100%',
    status: '已完成'
  },
  {
    id: 3,
    title: '党小组学习会',
    type: '党小组会',
    organization: '软件学院党支部',
    date: '2023-10-08 16:00',
    location: '软件学院教室',
    host: '王组长',
    participants: '软件学院党小组成员',
    content: '学习党的二十大报告精神，交流学习心得体会，讨论如何将理论学习转化为实际工作动力。',
    attendance: '95%',
    status: '已完成'
  },
  {
    id: 4,
    title: '支部党员大会',
    type: '党员大会',
    organization: '网络工程系党小组',
    date: '2023-10-05 15:00',
    location: '网络工程系会议室',
    host: '赵书记',
    participants: '网络工程系全体党员',
    content: '选举新的支部委员，讨论预备党员转正事宜，传达上级党委指示精神。',
    attendance: '92%',
    status: '已完成'
  },
  {
    id: 5,
    title: '学习贯彻党的二十大精神',
    type: '党课',
    organization: '校党委',
    date: '2023-09-28 14:30',
    location: '大礼堂',
    host: '张书记',
    participants: '全体党员',
    content: '深入学习党的二十大精神，解读报告主要内容，明确未来党建工作方向。',
    attendance: '96%',
    status: '已完成'
  },
  {
    id: 6,
    title: '新学期党建工作部署会',
    type: '支部委员会',
    organization: '计算机学院党总支',
    date: '2024-03-15 10:00',
    location: '计算机学院会议室',
    host: '李院长',
    participants: '支部委员、党小组组长',
    content: '部署新学期党建工作重点，讨论党员发展计划，安排主题教育活动。',
    attendance: '',
    status: '待开展'
  },
  {
    id: 7,
    title: '主题教育专题学习会',
    type: '党小组会',
    organization: '软件学院党支部',
    date: '2024-03-20 16:00',
    location: '软件学院教室',
    host: '王组长',
    participants: '软件学院党小组成员',
    content: '开展主题教育专题学习，深入学习习近平总书记重要讲话精神。',
    attendance: '',
    status: '待开展'
  },
  {
    id: 8,
    title: '2024年第一季度党课',
    type: '党课',
    organization: '校党委',
    date: '2024-03-25 14:30',
    location: '行政楼101会议室',
    host: '张书记',
    participants: '全体党员',
    content: '2024年第一季度党课，主题为《深入学习贯彻党的二十大精神，推动学校高质量发展》。',
    attendance: '',
    status: '待开展'
  }
])

// 计算属性：筛选后的会议列表
const filteredMeetings = computed(() => {
  let result = [...meetings.value]
  
  // 按关键词筛选
  if (searchKeyword.value) {
    result = result.filter(meeting => 
      meeting.title.includes(searchKeyword.value)
    )
  }
  
  // 按会议类型筛选
  if (filterType.value) {
    result = result.filter(meeting => 
      meeting.type === filterType.value
    )
  }
  
  // 按会议状态筛选
  if (filterStatus.value) {
    result = result.filter(meeting => 
      meeting.status === filterStatus.value
    )
  }
  
  // 按日期筛选
  if (filterDate.value) {
    const selectedDate = new Date(filterDate.value)
    const yearMonth = `${selectedDate.getFullYear()}-${String(selectedDate.getMonth() + 1).padStart(2, '0')}`
    result = result.filter(meeting => 
      meeting.date.startsWith(yearMonth)
    )
  }
  
  return result
})

// 分页数据
const paginatedMeetings = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredMeetings.value.slice(start, end)
})

// 统计数据
const totalMeetings = computed(() => meetings.value.length)
const completedMeetings = computed(() => 
  meetings.value.filter(m => m.status === '已完成').length
)
const ongoingMeetings = computed(() => 
  meetings.value.filter(m => m.status === '进行中').length
)
const upcomingMeetings = computed(() => 
  meetings.value.filter(m => m.status === '待开展').length
)

// 方法：获取类型颜色
const getTypeColor = (type) => {
  switch (type) {
    case '党课': return 'primary'
    case '支部委员会': return 'success'
    case '党小组会': return 'warning'
    case '党员大会': return 'info'
    default: return ''
  }
}

// 方法：获取状态颜色
const getStatusColor = (status) => {
  switch (status) {
    case '已完成': return 'success'
    case '进行中': return 'warning'
    case '待开展': return 'info'
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
  filterDate.value = ''
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
  dialogTitle.value = '创建会议'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 方法：编辑会议
const editMeeting = (meeting) => {
  dialogTitle.value = '编辑会议'
  isEdit.value = true
  meetingForm.value = { ...meeting }
  dialogVisible.value = true
}

// 方法：查看会议详情
const viewMeeting = (meeting) => {
  currentMeeting.value = meeting
  viewDialogVisible.value = true
}

// 方法：删除会议
const deleteMeeting = (id) => {
  ElMessage.confirm('确定要删除该会议吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = meetings.value.findIndex(meeting => meeting.id === id)
    if (index !== -1) {
      meetings.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 方法：重置表单
const resetForm = () => {
  meetingForm.value = {
    id: '',
    title: '',
    type: '',
    organization: '',
    date: '',
    location: '',
    host: '',
    participants: '',
    content: '',
    attendance: '100%',
    status: '已完成'
  }
}

// 方法：保存会议
const saveMeeting = () => {
  // 简单验证
  if (!meetingForm.value.title || !meetingForm.value.type || !meetingForm.value.organization || !meetingForm.value.date || !meetingForm.value.location) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  if (isEdit.value) {
    // 编辑模式
    const index = meetings.value.findIndex(meeting => meeting.id === meetingForm.value.id)
    if (index !== -1) {
      meetings.value[index] = { ...meetingForm.value }
      ElMessage.success('编辑成功')
    }
  } else {
    // 创建模式
    const newMeeting = {
      ...meetingForm.value,
      id: Date.now(), // 简单生成ID
    }
    meetings.value.unshift(newMeeting)
    ElMessage.success('创建成功')
  }
  
  dialogVisible.value = false
  resetForm()
}

// 生命周期钩子
onMounted(() => {
  console.log('三会一课页面加载完成')
  console.log('会议数据:', meetings.value)
  console.log('筛选后数据:', filteredMeetings.value)
  console.log('分页后数据:', paginatedMeetings.value)
})
</script>

<style scoped>
.three-meetings-one-lesson {
  margin: 0 auto;
  padding: 24px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

.breadcrumb {
  margin-bottom: 20px;
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

.empty-state {
  padding: 40px;
  text-align: center;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

/* 表格样式 */
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

:deep(.el-table) {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

:deep(.el-table__header-wrapper) {
  background-color: #f8f9fa;
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
  .three-meetings-one-lesson {
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
}
</style>