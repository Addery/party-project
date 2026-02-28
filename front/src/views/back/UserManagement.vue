<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '../../stores/user'
import AdminLayout from '@/components/AdminLayout.vue'
import { Edit, View, Plus } from '@element-plus/icons-vue'

const userStore = useUserStore()

// 模拟用户数据
const users = ref([
  { id: 1, username: 'admin', name: '系统管理员', role: 'admin', department: '党委办公室', joinDate: '2025-01-01', status: 'active' },
  { id: 2, username: 'department_head', name: '部门负责人', role: 'department_head', department: '组织部', joinDate: '2025-01-01', status: 'active' },
  { id: 3, username: 'branch_secretary', name: '党支部书记', role: 'branch_secretary', department: '第一党支部', joinDate: '2025-01-01', status: 'active' },
  { id: 4, username: 'teacher1', name: '王晓明', role: 'member', department: '计算机学院', joinDate: '2025-02-15', status: 'active' },  { id: 5, username: 'student1', name: '李婷婷', role: 'member', department: '电子工程学院', joinDate: '2025-03-20', status: 'active' },  { id: 6, username: 'teacher2', name: '张伟东', role: 'member', department: '数学学院', joinDate: '2025-04-10', status: 'inactive' }
])

// 搜索和筛选
const searchKeyword = ref('')
const selectedRole = ref('')
const selectedStatus = ref('')

// 计算过滤后的用户列表
const filteredUsers = computed(() => {
  return users.value.filter(user => {
    const matchesKeyword = user.username.includes(searchKeyword.value) || 
                           user.name.includes(searchKeyword.value) ||
                           user.department.includes(searchKeyword.value)
    const matchesRole = selectedRole.value ? user.role === selectedRole.value : true
    const matchesStatus = selectedStatus.value ? user.status === selectedStatus.value : true
    return matchesKeyword && matchesRole && matchesStatus
  })
})

// 分页
const currentPage = ref(1)
const pageSize = ref(10)

// 计算分页后的数据
const paginatedUsers = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  return filteredUsers.value.slice(startIndex, startIndex + pageSize.value)
})

// 添加用户
const addUser = () => {
  // 这里应该跳转到添加用户页面或打开模态框
  console.log('添加用户')
}

// 编辑用户
const editUser = (user) => {
  // 这里应该跳转到编辑用户页面
  console.log('编辑用户', user.id)
}

// 查看用户详情
const viewUser = (user) => {
  // 这里应该跳转到用户详情页面
  console.log('查看用户详情', user.id)
}

// 方法：获取状态颜色
const getStatusColor = (status) => {
  switch (status) {
    case 'active': return 'success'
    case 'inactive': return 'danger'
    default: return ''
  }
}

// 模拟API调用：获取用户列表
const fetchUsers = () => {
  // 实际项目中这里会调用API获取数据
  // 模拟API延迟
  setTimeout(() => {
    console.log('用户数据加载完成')
  }, 300)
}

// 初始化数据
fetchUsers()

// 分页事件处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}
</script>

<template>
  <AdminLayout>
    <div class="user-management">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面头部 -->
      <div class="page-header">
        <h2>用户管理</h2>
        <el-button type="primary" @click="addUser">
          <el-icon><Plus /></el-icon> 添加用户
        </el-button>
      </div>

      <!-- 内容卡片 -->
      <div class="content-card">
        <!-- 搜索和筛选 -->
        <div class="filter-bar">
          <div class="search-box">
            <el-input 
              placeholder="搜索用户名、姓名或部门" 
              v-model="searchKeyword"
              style="width: 300px; margin-right: 10px;"
            />
          </div>
          <div class="filter-controls">
            <el-select 
              v-model="selectedRole" 
              placeholder="选择角色" 
              style="width: 150px; margin-right: 10px;"
            >
              <el-option value="" label="全部角色" />
              <el-option value="admin" label="系统管理员" />
              <el-option value="department_head" label="部门负责人" />
              <el-option value="branch_secretary" label="党支部书记" />
              <el-option value="member" label="普通党员" />
            </el-select>
            <el-select 
              v-model="selectedStatus" 
              placeholder="选择状态" 
              style="width: 150px; margin-right: 10px;"
            >
              <el-option value="" label="全部状态" />
              <el-option value="active" label="激活" />
              <el-option value="inactive" label="停用" />
            </el-select>
            <el-button type="primary">筛选</el-button>
          </div>
        </div>

        <!-- 用户表格 -->
        <div class="table-container">
            <el-table :data="paginatedUsers" style="width: 100%;">
            <el-table-column type="selection" width="65" />
            <el-table-column prop="id" label="ID" width="80" align="center" />
            <el-table-column prop="username" label="用户名" min-width="120" />
            <el-table-column prop="name" label="姓名" min-width="100" />
            <el-table-column prop="role" label="角色" min-width="140">
              <template #default="scope">
                {{ 
                  scope.row.role === 'admin' ? '系统管理员' : 
                  scope.row.role === 'department_head' ? '部门负责人' : 
                  scope.row.role === 'branch_secretary' ? '党支部书记' : 
                  '普通党员'
                }}
              </template>
            </el-table-column>
            <el-table-column prop="department" label="部门" min-width="160" />
            <el-table-column prop="joinDate" label="加入时间" min-width="140" />
            <el-table-column prop="status" label="状态" min-width="100">
              <template #default="scope">
                <el-tag :type="getStatusColor(scope.row.status)">
                  {{ scope.row.status === 'active' ? '激活' : '停用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160" fixed="right">
              <template #default="scope">
                <el-space size="small">
                  <el-button size="small" @click="viewUser(scope.row)">
                    <el-icon><View /></el-icon> 查看
                  </el-button>
                  <el-button size="small" type="primary" @click="editUser(scope.row)">
                    <el-icon><Edit /></el-icon> 编辑
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
            :total="filteredUsers.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<style scoped>
.user-management {
  margin: 0 auto;
  padding: 24px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 面包屑导航 */
.breadcrumb {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

.page-header {
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.page-header .el-button {
  background-color: #c72c41;
  border-color: #c72c41;
}

.page-header .el-button:hover {
  background-color: #a82336;
  border-color: #a82336;
}

.page-header h2 {
  margin: 0;
  color: #2c3e50;
  font-size: 24px;
  font-weight: 600;
  display: inline-block;
}

.content-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  border: 1px solid #f0f0f0;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 2px solid #f0f0f0;
  gap: 20px;
  flex-wrap: wrap;
}

.search-box {
  flex: 1;
  min-width: 200px;
}

.filter-controls {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

/* Element Plus 表格样式重置 */
.table-container {
  position: relative;
  overflow: hidden;
  width: 100%;
}

:deep(.el-table) {
  border: none;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  width: 100% !important;
  max-width: 100%;
  display: block;
}

:deep(.el-table__header-wrapper),
:deep(.el-table__body-wrapper) {
  overflow-x: auto;
  width: 100%;
}

:deep(.el-table__header),
:deep(.el-table__body) {
  width: 100% !important;
  min-width: auto !important;
}

:deep(.el-table) table {
  width: 100% !important;
  table-layout: auto;
  min-width: auto !important;
}

/* 重置所有列的宽度设置，让表格自动计算 */
:deep(.el-table__column),
:deep(.el-table__header-column) {
  width: auto !important;
  min-width: auto !important;
  max-width: none !important;
}

/* 确保表格内容正确显示 */
:deep(.el-table__cell) {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.table-container {
  padding: 24px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  overflow: hidden;
  width: 100%;
  box-sizing: border-box;
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

/* 移除了fixed-right相关样式 */

:deep(.el-table__body tr:last-child td) {
  border-bottom: none;
}



/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 24px;
  border-top: 2px solid #f0f0f0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-management {
    padding: 16px;
  }
  
  .filter-bar {
    flex-direction: column;
    align-items: stretch;
    padding: 16px;
  }
  
  .filter-controls {
    flex-wrap: wrap;
  }
  
  .table-container {
    padding: 16px;
  }
}
</style>