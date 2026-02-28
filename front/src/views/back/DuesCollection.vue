<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import AdminLayout from '@/components/AdminLayout.vue'
import { View, Edit, Check, Delete } from '@element-plus/icons-vue'

// 路由和状态管理
const router = useRouter()
const userStore = useUserStore()

// 总收缴金额
const totalDues = ref({
  amount: 12345.67,
  paidCount: 523,
  unpaidCount: 27
})

// 缴费记录列表
const duesList = ref([
  { id: 1, name: '王晓明', department: '机关第一党支部', amount: 12.00, date: '2025-05-15', status: '已缴纳', type: '教师', payMethod: '校园一卡通' },  { id: 2, name: '李婷婷', department: '机关第二党支部', amount: 10.00, date: '2025-05-14', status: '已缴纳', type: '教师', payMethod: '现金' },  { id: 3, name: '张伟东', department: '学生第一党支部', amount: 8.00, date: '', status: '未缴纳', type: '学生', payMethod: '' },  { id: 4, name: '陈思思', department: '学生第二党支部', amount: 10.00, date: '2025-05-16', status: '已缴纳', type: '学生', payMethod: '微信' },  { id: 5, name: '刘建国', department: '教师党支部', amount: 15.00, date: '', status: '未缴纳', type: '教师', payMethod: '' },  { id: 6, name: '吴子轩', department: '学生第三党支部', amount: 8.00, date: '2025-05-13', status: '已缴纳', type: '学生', payMethod: '支付宝' },  { id: 7, name: '林小雨', department: '机关第三党支部', amount: 12.00, date: '2025-05-12', status: '已缴纳', type: '教师', payMethod: '校园一卡通' },  { id: 8, name: '黄志远', department: '学生第一党支部', amount: 8.00, date: '2025-05-11', status: '已缴纳', type: '学生', payMethod: '微信' },
  { id: 9, name: '王十一', department: '教师党支部', amount: 15.00, date: '', status: '未缴纳', type: '教师', payMethod: '' },
  { id: 10, name: '李十二', department: '学生第二党支部', amount: 8.00, date: '2025-05-10', status: '已缴纳', type: '学生', payMethod: '支付宝' },
  { id: 11, name: '张十三', department: '机关第一党支部', amount: 12.00, date: '2025-05-09', status: '已缴纳', type: '教师', payMethod: '校园一卡通' },
  { id: 12, name: '刘十四', department: '学生第三党支部', amount: 8.00, date: '', status: '未缴纳', type: '学生', payMethod: '' },
  { id: 13, name: '陈十五', department: '教师党支部', amount: 15.00, date: '2025-05-08', status: '已缴纳', type: '教师', payMethod: '现金' },
  { id: 14, name: '杨十六', department: '机关第二党支部', amount: 12.00, date: '2025-05-07', status: '已缴纳', type: '教师', payMethod: '校园一卡通' },
  { id: 15, name: '赵十七', department: '学生第一党支部', amount: 8.00, date: '', status: '未缴纳', type: '学生', payMethod: '' },
  { id: 16, name: '钱十八', department: '学生第二党支部', amount: 8.00, date: '2025-05-06', status: '已缴纳', type: '学生', payMethod: '微信' },
  { id: 17, name: '孙十九', department: '机关第三党支部', amount: 12.00, date: '2025-05-05', status: '已缴纳', type: '教师', payMethod: '校园一卡通' },
  { id: 18, name: '周二十', department: '教师党支部', amount: 15.00, date: '', status: '未缴纳', type: '教师', payMethod: '' },
  { id: 19, name: '吴二十一', department: '学生第三党支部', amount: 8.00, date: '2025-05-04', status: '已缴纳', type: '学生', payMethod: '支付宝' },
  { id: 20, name: '郑二十二', department: '机关第一党支部', amount: 12.00, date: '2025-05-03', status: '已缴纳', type: '教师', payMethod: '校园一卡通' },
  { id: 21, name: '王二十三', department: '学生第一党支部', amount: 8.00, date: '2025-05-02', status: '已缴纳', type: '学生', payMethod: '微信' },
  { id: 22, name: '李二十四', department: '机关第二党支部', amount: 12.00, date: '2025-05-01', status: '已缴纳', type: '教师', payMethod: '校园一卡通' },
  { id: 23, name: '张二十五', department: '教师党支部', amount: 15.00, date: '', status: '未缴纳', type: '教师', payMethod: '' },
  { id: 24, name: '刘二十六', department: '学生第二党支部', amount: 8.00, date: '2025-04-30', status: '已缴纳', type: '学生', payMethod: '支付宝' },
  { id: 25, name: '陈二十七', department: '机关第三党支部', amount: 12.00, date: '2025-04-29', status: '已缴纳', type: '教师', payMethod: '校园一卡通' },
  { id: 26, name: '杨二十八', department: '学生第三党支部', amount: 8.00, date: '', status: '未缴纳', type: '学生', payMethod: '' },
  { id: 27, name: '赵二十九', department: '教师党支部', amount: 15.00, date: '2025-04-28', status: '已缴纳', type: '教师', payMethod: '现金' },
  { id: 28, name: '钱三十', department: '机关第一党支部', amount: 12.00, date: '2025-04-27', status: '已缴纳', type: '教师', payMethod: '校园一卡通' },
  { id: 29, name: '孙三十一', department: '学生第一党支部', amount: 8.00, date: '', status: '未缴纳', type: '学生', payMethod: '' },
  { id: 30, name: '周三十二', department: '学生第二党支部', amount: 8.00, date: '2025-04-26', status: '已缴纳', type: '学生', payMethod: '微信' }
])

// 批量导入模态框
const showBatchImportModal = ref(false)
// 缴费详情模态框
const showPaymentDetailModal = ref(false)
const currentPayment = ref(null)

// 筛选条件
const searchQuery = ref('')
const statusFilter = ref('all')
const departmentFilter = ref('all')
const typeFilter = ref('all')
const payMethodFilter = ref('all')

// 分页
const currentPage = ref(1)
const pageSize = ref(10)

// 计算筛选后的缴费记录
const filteredDuesList = computed(() => {
  return duesList.value.filter(item => {
    const matchesSearch = item.name.toLowerCase().includes(searchQuery.value.toLowerCase()) || 
                         item.department.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesStatus = statusFilter.value === 'all' || item.status === statusFilter.value
    const matchesDepartment = departmentFilter.value === 'all' || item.department === departmentFilter.value
    const matchesType = typeFilter.value === 'all' || item.type === typeFilter.value
    const matchesPayMethod = payMethodFilter.value === 'all' || item.payMethod === payMethodFilter.value
    
    return matchesSearch && matchesStatus && matchesDepartment && matchesType && matchesPayMethod
  })
})

// 计算分页后的缴费记录
const paginatedDuesList = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredDuesList.value.slice(start, end)
})

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(filteredDuesList.value.length / pageSize.value)
})

// 导出党费记录
const exportDuesList = () => {
  // 预留后端接口：/api/dues/export
  alert('导出功能开发中')
}

// 生成党费证
const generateDuesCard = () => {
  // 预留后端接口：/api/dues/generate-card
  alert('生成党费证功能开发中')
}

// 查看缴费详情
const viewPaymentDetail = (item) => {
  currentPayment.value = item
  showPaymentDetailModal.value = true
}

// 编辑缴费记录
const editPayment = (item) => {
  // 预留后端接口：/api/dues/edit/:id
  alert('编辑功能开发中')
}

// 设置缴费状态
const updatePaymentStatus = (item, status) => {
  // 预留后端接口：/api/dues/update-status/:id
  item.status = status
  if (status === '已缴纳') {
    item.date = new Date().toISOString().split('T')[0]
    item.payMethod = '手动登记'
  } else {
    item.date = ''
    item.payMethod = ''
  }
  alert('缴费状态更新成功')
}

// 获取所有党支部列表
const departments = computed(() => {
  const deptSet = new Set()
  duesList.value.forEach(item => deptSet.add(item.department))
  return Array.from(deptSet)
})

// 重置筛选条件
const resetFilters = () => {
  searchQuery.value = ''
  statusFilter.value = 'all'
  departmentFilter.value = 'all'
  typeFilter.value = 'all'
  payMethodFilter.value = 'all'
}
</script>

<template>
  <AdminLayout>
    <template #breadcrumb>
      <div class="breadcrumb">
        <span><a href="#">首页</a></span>
        <span class="breadcrumb-separator">/</span>
        <span>核心党务工作</span>
        <span class="breadcrumb-separator">/</span>
        <span>党费收缴管理</span>
      </div>
    </template>

    <div class="dues-collection">
      <!-- 页面标题和操作按钮 -->
      <div class="page-header">
        <div class="header-title">
          <h2>党费收缴管理</h2>
          <p>管理党员党费缴纳记录，支持批量导入、导出和生成电子党费证</p>
        </div>
        <div class="header-actions">
          <button class="btn btn-primary" @click="showBatchImportModal = true">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
              <polyline points="7 10 12 15 17 10"></polyline>
              <line x1="12" y1="15" x2="12" y2="3"></line>
            </svg>
            批量导入缴费记录
          </button>
          <button class="btn btn-secondary" @click="exportDuesList">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="16 18 22 12 16 6"></polyline>
              <polyline points="8 6 2 12 8 18"></polyline>
            </svg>
            导出党费记录
          </button>
          <button class="btn btn-secondary" @click="generateDuesCard">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
              <line x1="21" y1="10" x2="3" y2="10"></line>
            </svg>
            生成党费证
          </button>
        </div>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-info">
            <div class="stat-number">¥{{ totalDues.amount.toFixed(2) }}</div>
            <div class="stat-label">总收缴金额</div>
          </div>
          <div class="stat-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="12" y1="1" x2="12" y2="23"></line>
              <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
            </svg>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-info">
            <div class="stat-number">{{ totalDues.paidCount }}</div>
            <div class="stat-label">已缴费人数</div>
          </div>
          <div class="stat-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
              <polyline points="22 4 12 14.01 9 11.01"></polyline>
            </svg>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-info">
            <div class="stat-number">{{ totalDues.unpaidCount }}</div>
            <div class="stat-label">未缴费人数</div>
          </div>
          <div class="stat-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="10"></circle>
              <line x1="15" y1="9" x2="9" y2="15"></line>
              <line x1="9" y1="9" x2="15" y2="15"></line>
            </svg>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-info">
            <div class="stat-number">{{ ((totalDues.paidCount / (totalDues.paidCount + totalDues.unpaidCount)) * 100).toFixed(1) }}%</div>
            <div class="stat-label">缴费率</div>
          </div>
          <div class="stat-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10z"></path>
              <path d="M12 6v6l4 2"></path>
            </svg>
          </div>
        </div>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-section">
        <div class="filter-row">
          <div class="filter-group">
            <label>搜索：</label>
            <input 
              type="text" 
              v-model="searchQuery" 
              placeholder="输入姓名或党支部名称" 
              class="form-input"
            >
          </div>
          <div class="filter-group">
            <label>状态：</label>
            <select v-model="statusFilter" class="form-select">
              <option value="all">全部</option>
              <option value="已缴纳">已缴纳</option>
              <option value="未缴纳">未缴纳</option>
            </select>
          </div>
          <div class="filter-group">
            <label>党支部：</label>
            <select v-model="departmentFilter" class="form-select">
              <option value="all">全部</option>
              <option v-for="dept in departments" :key="dept" :value="dept">{{ dept }}</option>
            </select>
          </div>
          <div class="filter-group">
            <label>类型：</label>
            <select v-model="typeFilter" class="form-select">
              <option value="all">全部</option>
              <option value="教师">教师</option>
              <option value="学生">学生</option>
            </select>
          </div>
          <div class="filter-group">
            <label>缴费方式：</label>
            <select v-model="payMethodFilter" class="form-select">
              <option value="all">全部</option>
              <option value="校园一卡通">校园一卡通</option>
              <option value="微信">微信</option>
              <option value="支付宝">支付宝</option>
              <option value="现金">现金</option>
            </select>
          </div>
          <div class="filter-group">
            <button class="btn btn-secondary" @click="resetFilters">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8"></path>
                <path d="M3 3v5h5"></path>
              </svg>
              重置筛选
            </button>
          </div>
        </div>
      </div>

      <!-- 缴费记录表格 -->
      <div class="table-container">
        <el-table :data="paginatedDuesList" style="width: 100%" border>
          <el-table-column type="selection" min-width="60" align="center" />
          <el-table-column label="ID" min-width="70" align="center">
            <template #default="scope">
              {{ scope.row.id }}
            </template>
          </el-table-column>
          <el-table-column prop="name" label="姓名" min-width="100" align="center" />
          <el-table-column prop="type" label="类型" min-width="80" align="center">
            <template #default="scope">
              <span :class="{ 'type-tag': true, 'type-teacher': scope.row.type === '教师', 'type-student': scope.row.type === '学生' }">
                {{ scope.row.type }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="department" label="党支部" min-width="150" align="center" />
          <el-table-column prop="amount" label="缴费金额" min-width="100" align="center">
            <template #default="scope">
              ¥{{ scope.row.amount.toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="date" label="缴费日期" min-width="120" align="center">
            <template #default="scope">
              {{ scope.row.date || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="payMethod" label="缴费方式" min-width="100" align="center">
            <template #default="scope">
              {{ scope.row.payMethod || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" min-width="100" align="center">
            <template #default="scope">
              <span :class="{ 'status-tag': true, 'status-paid': scope.row.status === '已缴纳', 'status-unpaid': scope.row.status === '未缴纳' }">
                {{ scope.row.status }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="280" align="center">
            <template #default="scope">
              <el-space size="small">
                <el-button size="small" @click="viewPaymentDetail(scope.row)">
                  <el-icon><View /></el-icon> 查看
                </el-button>
                <el-button size="small" type="primary" @click="editPayment(scope.row)">
                  <el-icon><Edit /></el-icon> 编辑
                </el-button>
                <el-button
                  size="small"
                  v-if="scope.row.status === '未缴纳'"
                  type="success"
                  @click="updatePaymentStatus(scope.row, '已缴纳')"
                >
                  <el-icon><Check /></el-icon> 标记已缴
                </el-button>
                <el-button
                  size="small"
                  v-else
                  type="warning"
                  @click="updatePaymentStatus(scope.row, '未缴纳')"
                >
                  <el-icon><Delete /></el-icon> 标记未缴
                </el-button>
              </el-space>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <div class="pagination-info">
          共 {{ filteredDuesList.length }} 条记录，当前第 {{ currentPage }} / {{ totalPages }} 页
        </div>
        <div class="pagination-controls">
          <button 
            class="btn btn-secondary" 
            @click="currentPage = Math.max(1, currentPage - 1)" 
            :disabled="currentPage === 1"
          >
            上一页
          </button>
          <button 
            class="btn btn-secondary" 
            @click="currentPage = Math.min(totalPages, currentPage + 1)" 
            :disabled="currentPage === totalPages"
          >
            下一页
          </button>
          <select v-model="pageSize" class="form-select page-size-select">
            <option value="10">10条/页</option>
            <option value="20">20条/页</option>
            <option value="50">50条/页</option>
            <option value="100">100条/页</option>
          </select>
        </div>
      </div>

      <!-- 批量导入模态框 -->
      <div v-if="showBatchImportModal" class="modal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>批量导入缴费记录</h3>
            <button class="modal-close" @click="showBatchImportModal = false">×</button>
          </div>
          <div class="modal-body">
            <div class="import-section">
              <h4>导入说明</h4>
              <ul>
                <li>支持Excel文件格式 (.xlsx, .xls)</li>
                <li>请按照模板格式填写数据</li>
                <li>每次最多导入1000条记录</li>
              </ul>
              <button class="btn btn-secondary btn-download-template">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="16 18 22 12 16 6"></polyline>
                  <polyline points="8 6 2 12 8 18"></polyline>
                </svg>
                下载导入模板
              </button>
            </div>
            <div class="import-section">
              <h4>上传文件</h4>
              <div class="file-upload">
                <input type="file" id="file-input" accept=".xlsx, .xls">
                <label for="file-input" class="file-label">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                    <polyline points="7 10 12 15 17 10"></polyline>
                    <line x1="12" y1="15" x2="12" y2="3"></line>
                  </svg>
                  点击选择文件或拖拽文件到此处
                </label>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showBatchImportModal = false">取消</button>
            <button class="btn btn-primary">开始导入</button>
          </div>
        </div>
      </div>

      <!-- 缴费详情模态框 -->
      <div v-if="showPaymentDetailModal && currentPayment" class="modal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>缴费详情</h3>
            <button class="modal-close" @click="showPaymentDetailModal = false">×</button>
          </div>
          <div class="modal-body">
            <div class="detail-section">
              <div class="detail-row">
                <div class="detail-label">姓名</div>
                <div class="detail-value">{{ currentPayment.name }}</div>
              </div>
              <div class="detail-row">
                <div class="detail-label">类型</div>
                <div class="detail-value">
                  <span :class="{ 'type-tag': true, 'type-teacher': currentPayment.type === '教师', 'type-student': currentPayment.type === '学生' }">
                    {{ currentPayment.type }}
                  </span>
                </div>
              </div>
              <div class="detail-row">
                <div class="detail-label">党支部</div>
                <div class="detail-value">{{ currentPayment.department }}</div>
              </div>
              <div class="detail-row">
                <div class="detail-label">缴费金额</div>
                <div class="detail-value">¥{{ currentPayment.amount.toFixed(2) }}</div>
              </div>
              <div class="detail-row">
                <div class="detail-label">缴费状态</div>
                <div class="detail-value">
                  <span :class="{ 'status-tag': true, 'status-paid': currentPayment.status === '已缴纳', 'status-unpaid': currentPayment.status === '未缴纳' }">
                    {{ currentPayment.status }}
                  </span>
                </div>
              </div>
              <div class="detail-row">
                <div class="detail-label">缴费日期</div>
                <div class="detail-value">{{ currentPayment.date || '-' }}</div>
              </div>
              <div class="detail-row">
                <div class="detail-label">缴费方式</div>
                <div class="detail-value">{{ currentPayment.payMethod || '-' }}</div>
              </div>
              <div class="detail-row">
                <div class="detail-label">缴费周期</div>
                <div class="detail-value">2025年5月</div>
              </div>
              <div class="detail-row">
                <div class="detail-label">登记人</div>
                <div class="detail-value">系统管理员</div>
              </div>
              <div class="detail-row">
                <div class="detail-label">登记时间</div>
                <div class="detail-value">2025-05-17 10:30:00</div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showPaymentDetailModal = false">关闭</button>
          </div>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<style scoped>
/* 页面容器 */
.dues-collection {
  margin: 0 auto;
  padding: 24px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

/* 面包屑 */
.breadcrumb {
  font-size: 14px;
  color: #666;
  margin-bottom: 20px;
}

.breadcrumb a {
  color: #2c3e50;
  text-decoration: none;
}

.breadcrumb a:hover {
  color: #d93025;
}

.breadcrumb-separator {
  margin: 0 8px;
  color: #999;
}

/* 页面标题和操作按钮 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 20px;
}

.header-title {
  flex: 1;
}

.header-title h2 {
  margin: 0 0 5px 0;
  font-size: 24px;
  color: #2c3e50;
}

.header-title p {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.header-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

/* 统计卡片 */
.stats-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.stat-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex: 1;
  min-width: 200px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 24px;
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
  margin-left: 20px;
}

/* 筛选区域 */
.filter-section {
  background-color: #fafafa;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  border: 1px solid #f0f0f0;
}

.filter-row {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-group label {
  font-size: 14px;
  color: #666;
  white-space: nowrap;
}

.form-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  min-width: 200px;
}

.form-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  min-width: 150px;
  background-color: #fff;
}

/* 表格容器 */
.table-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow-x: auto;
  border: 1px solid #f0f0f0;
  position: relative;
}

/* 缴费记录表格 */
.dues-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.dues-table thead {
  background-color: #f5f5f5;
  color: #2c3e50;
}

.dues-table th, .dues-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.dues-table th {
  font-weight: 600;
  white-space: nowrap;
}

.dues-table tbody tr {
  transition: background-color 0.2s ease;
}

.dues-table tbody tr:hover {
  background-color: #fafafa;
}

/* 类型标签 */
.type-tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.type-teacher {
  background-color: #e3f2fd;
  color: #1976d2;
}

.type-student {
  background-color: #e8f5e9;
  color: #388e3c;
}

/* 状态标签 */
.status-tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.status-paid {
  background-color: #e8f5e9;
  color: #388e3c;
}

.status-unpaid {
  background-color: #ffebee;
  color: #d32f2f;
}

/* 按钮样式 */
.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  text-decoration: none;
}

.btn-primary {
  background: #d93025;
  color: white;
}

.btn-primary:hover {
  background: #c0392b;
}

.btn-secondary {
  background: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
}

.btn-secondary:hover {
  background: #eee;
}

.btn-warning {
  background: #ff9800;
  color: white;
}

.btn-warning:hover {
  background: #f57c00;
}

.btn-icon {
  padding: 6px 8px;
  height: 36px;
  justify-content: center;
  background: none;
  color: #666;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
}

.btn-view {
  color: #1976d2;
  border-color: #1976d2;
}

.btn-view:hover {
  background: #e3f2fd;
}

.btn-edit {
  color: #ff9800;
  border-color: #ff9800;
}

.btn-edit:hover {
  background: #fff3e0;
}

.action-buttons {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.btn-download-template {
  margin-top: 10px;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background-color: #fafafa;
  border-radius: 0 0 8px 8px;
  border-top: 1px solid #eee;
  margin-top: -1px;
  flex-wrap: wrap;
  gap: 20px;
}

.pagination-info {
  font-size: 14px;
  color: #666;
}

.pagination-controls {
  display: flex;
  gap: 10px;
  align-items: center;
}

.page-size-select {
  min-width: auto;
}

/* 模态框 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 600px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 20px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  border-top: 1px solid #eee;
}

/* 导入区域 */
.import-section {
  margin-bottom: 20px;
}

.import-section h4 {
  margin: 0 0 10px 0;
  font-size: 16px;
}

.import-section ul {
  margin: 0;
  padding-left: 20px;
}

.import-section li {
  margin-bottom: 5px;
  color: #666;
}

/* 文件上传 */
.file-upload {
  margin-top: 10px;
}

.file-upload input[type="file"] {
  display: none;
}

.file-label {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  border: 2px dashed #ddd;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  background-color: #fafafa;
}

.file-label:hover {
  border-color: #d93025;
  background-color: #f5f5f5;
}

/* 详情页面 */
.detail-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}

.detail-row {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-label {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.detail-value {
  font-size: 16px;
  color: #333;
}

/* 响应式设计 */
/* 表格样式 */
.table-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  overflow: hidden;
  margin-bottom: 20px;
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
  white-space: nowrap;
  line-height: normal;
  height: auto;
}

:deep(.el-table__header tr th .cell) {
  white-space: nowrap;
}

:deep(.el-table__body tr:hover) {
  background-color: #f8f9fa;
}

:deep(.el-table__body tr td) {
  padding: 12px 10px;
  border-bottom: 1px solid #f0f0f0;
  white-space: nowrap;
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

/* 选择框列居中 */
:deep(.el-table-column--selection .cell) {
  text-align: center;
}

/* 操作列按钮样式 */
:deep(.el-button) {
  border-radius: 4px;
}

:deep(.el-button--primary) {
  background-color: #c72c41;
  border-color: #c72c41;
}

:deep(.el-button--primary:hover),
:deep(.el-button--primary:focus) {
  background-color: #a82638;
  border-color: #a82638;
}

:deep(.el-button--danger) {
  background-color: #f56c6c;
  border-color: #f56c6c;
}

:deep(.el-button--danger:hover),
:deep(.el-button--danger:focus) {
  background-color: #f78989;
  border-color: #f78989;
}

@media (max-width: 768px) {
  .stats-cards {
    flex-direction: column;
  }

  .table-container {
    overflow-x: auto;
  }

  .header-actions {
    flex-direction: column;
  }

  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-group {
    flex-direction: column;
    align-items: stretch;
  }

  .form-input, .form-select {
    min-width: auto;
  }

  .pagination {
    flex-direction: column;
    align-items: stretch;
  }

  .pagination-controls {
    justify-content: center;
  }
}
</style>