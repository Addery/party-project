<script setup>
import { ref, computed } from 'vue'
import AdminLayout from '@/components/AdminLayout.vue'

// 模拟党员数据
const partyMembers = ref([
  {
    id: 1,
    name: '王晓明',
    type: 'teacher', // teacher: 教师党员, student: 学生党员
    partyOrg: '计算机学院教工党支部',
    partyPost: '支部书记',
    joinDate: '2010-05-12',
    status: 'active', // active: 正式党员, probationary: 预备党员,流动党员: mobile
    department: '计算机学院',
    position: '教授',
    phone: '13800138000',
    email: 'wangxiaoming@example.com',
    avatar: 'https://via.placeholder.com/60'
  },
  {
    id: 2,
    name: '李婷婷',
    type: 'student',
    partyOrg: '计算机学院学生党支部',
    partyPost: '组织委员',
    joinDate: '2022-06-01',
    status: 'probationary',
    grade: '2020级',
    major: '计算机科学与技术',
    class: '计科1班',
    studentId: '2020001',
    phone: '13900139000',
    email: 'litingting@example.com',
    avatar: 'https://via.placeholder.com/60'
  },
  {
    id: 3,
    name: '张伟东',
    type: 'teacher',
    partyOrg: '电子工程学院教工党支部',
    partyPost: '宣传委员',
    joinDate: '2015-07-20',
    status: 'active',
    department: '电子工程学院',
    position: '副教授',
    phone: '13700137000',
    email: 'zhangweidong@example.com',
    avatar: 'https://via.placeholder.com/60'
  },
  {
    id: 4,
    name: '陈思思',
    type: 'student',
    partyOrg: '电子工程学院学生党支部',
    partyPost: '普通党员',
    joinDate: '2023-01-15',
    status: 'probationary',
    grade: '2019级',
    major: '电子信息工程',
    class: '电信2班',
    studentId: '2019002',
    phone: '13600136000',
    email: 'chensisi@example.com',
    avatar: 'https://via.placeholder.com/60'
  },
  {
    id: 5,
    name: '刘建国',
    type: 'teacher',
    partyOrg: '机关第一党总支',
    partyPost: '普通党员',
    joinDate: '2008-09-01',
    status: 'mobile', // 流动党员
    department: '教务处',
    position: '科长',
    phone: '13500135000',
    email: 'liuguojian@example.com',
    avatar: 'https://via.placeholder.com/60'
  },
  {
    id: 6,
    name: '吴子轩',
    type: 'student',
    partyOrg: '计算机学院学生党支部',
    partyPost: '普通党员',
    joinDate: '2022-12-01',
    status: 'active',
    grade: '2018级',
    major: '软件工程',
    class: '软工3班',
    studentId: '2018003',
    phone: '13400134000',
    email: 'zhouba@example.com',
    avatar: 'https://via.placeholder.com/60'
  }
])

// 筛选条件
const filters = ref({
  search: '',
  type: 'all', // all, teacher, student
  status: 'all', // all, active, probationary, mobile
  partyOrg: 'all'
})

// 分页参数
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: partyMembers.value.length
})

// 计算筛选后的党员列表
const filteredMembers = computed(() => {
  let result = [...partyMembers.value]
  
  // 搜索筛选
  if (filters.value.search) {
    const searchTerm = filters.value.search.toLowerCase()
    result = result.filter(member => 
      member.name.toLowerCase().includes(searchTerm) ||
      member.partyOrg.toLowerCase().includes(searchTerm) ||
      member.phone.includes(searchTerm) ||
      (member.email && member.email.toLowerCase().includes(searchTerm)) ||
      (member.studentId && member.studentId.includes(searchTerm))
    )
  }
  
  // 类型筛选
  if (filters.value.type !== 'all') {
    result = result.filter(member => member.type === filters.value.type)
  }
  
  // 状态筛选
  if (filters.value.status !== 'all') {
    result = result.filter(member => member.status === filters.value.status)
  }
  
  // 党组织筛选
  if (filters.value.partyOrg !== 'all') {
    result = result.filter(member => member.partyOrg === filters.value.partyOrg)
  }
  
  // 更新总条数
  pagination.value.total = result.length
  
  return result
})

// 计算当前页的党员列表
const currentPageMembers = computed(() => {
  const start = (pagination.value.currentPage - 1) * pagination.value.pageSize
  const end = start + pagination.value.pageSize
  return filteredMembers.value.slice(start, end)
})

// 获取所有党组织名称
const partyOrgs = computed(() => {
  const orgs = [...new Set(partyMembers.value.map(member => member.partyOrg))]
  return ['all', ...orgs]
})

// 模态框状态
const showAddModal = ref(false)
const showEditModal = ref(false)
const showDeleteModal = ref(false)

// 选中的党员
const selectedMember = ref(null)

// 新党员数据
const newMember = ref({
  name: '',
  type: 'student',
  partyOrg: '',
  partyPost: '普通党员',
  joinDate: '',
  status: 'probationary',
  department: '',
  position: '',
  grade: '',
  major: '',
  class: '',
  studentId: '',
  phone: '',
  email: ''
})

// 编辑党员数据
const editMember = ref({
  id: null,
  name: '',
  type: 'student',
  partyOrg: '',
  partyPost: '普通党员',
  joinDate: '',
  status: 'probationary',
  department: '',
  position: '',
  grade: '',
  major: '',
  class: '',
  studentId: '',
  phone: '',
  email: ''
})

// 打开添加模态框
const openAddModal = () => {
  newMember.value = {
    name: '',
    type: 'student',
    partyOrg: partyOrgs.value[1] || '',
    partyPost: '普通党员',
    joinDate: new Date().toISOString().split('T')[0],
    status: 'probationary',
    department: '',
    position: '',
    grade: '',
    major: '',
    class: '',
    studentId: '',
    phone: '',
    email: ''
  }
  showAddModal.value = true
}

// 打开编辑模态框
const openEditModal = (member) => {
  editMember.value = JSON.parse(JSON.stringify(member))
  showEditModal.value = true
}

// 打开删除模态框
const openDeleteModal = (member) => {
  selectedMember.value = member
  showDeleteModal.value = true
}

// 添加党员
const addMember = () => {
  // 生成新ID
  const newId = Math.max(...partyMembers.value.map(m => m.id), 0) + 1
  
  // 构建新党员
  const member = {
    id: newId,
    ...newMember.value,
    avatar: 'https://via.placeholder.com/60'
  }
  
  // 添加到列表
  partyMembers.value.push(member)
  
  // 关闭模态框
  showAddModal.value = false
}

// 编辑党员
const updateMember = () => {
  const index = partyMembers.value.findIndex(m => m.id === editMember.value.id)
  if (index > -1) {
    partyMembers.value[index] = {
      ...partyMembers.value[index],
      ...editMember.value
    }
  }
  showEditModal.value = false
}

// 删除党员
const deleteMember = () => {
  if (!selectedMember.value) return
  
  partyMembers.value = partyMembers.value.filter(m => m.id !== selectedMember.value.id)
  selectedMember.value = null
  showDeleteModal.value = false
}

// 页码变化
const handlePageChange = (page) => {
  pagination.value.currentPage = page
}

// 筛选条件变化
const handleFilterChange = () => {
  pagination.value.currentPage = 1 // 筛选时回到第一页
}

// 清除筛选
const clearFilters = () => {
  filters.value = {
    search: '',
    type: 'all',
    status: 'all',
    partyOrg: 'all'
  }
  pagination.value.currentPage = 1
}

// 模拟API调用：获取党员列表
const fetchMembers = () => {
  // 实际项目中这里会调用API获取数据
  console.log('获取党员列表数据')
}

// 初始化数据
fetchMembers()
</script>

<template>
  <AdminLayout>
    <div class="party-member-management">
      <!-- 页面头部 -->
      <div class="page-header">
        <h2>党员信息管理</h2>
        <button class="btn btn-primary" @click="openAddModal">添加党员</button>
      </div>

      <!-- 筛选区域 -->
      <div class="filter-section">
        <div class="filter-group">
          <input 
            type="text" 
            class="form-control" 
            placeholder="搜索姓名、电话、邮箱或学号" 
            v-model="filters.search"
            @input="handleFilterChange"
          />
        </div>
        <div class="filter-group">
          <select class="form-control" v-model="filters.type" @change="handleFilterChange">
            <option value="all">全部类型</option>
            <option value="teacher">教师党员</option>
            <option value="student">学生党员</option>
          </select>
        </div>
        <div class="filter-group">
          <select class="form-control" v-model="filters.status" @change="handleFilterChange">
            <option value="all">全部状态</option>
            <option value="active">正式党员</option>
            <option value="probationary">预备党员</option>
            <option value="mobile">流动党员</option>
          </select>
        </div>
        <div class="filter-group">
          <select class="form-control" v-model="filters.partyOrg" @change="handleFilterChange">
            <option value="all">全部党组织</option>
            <option v-for="org in partyOrgs.slice(1)" :key="org" :value="org">
              {{ org }}
            </option>
          </select>
        </div>
        <div class="filter-group">
          <button class="btn btn-secondary" @click="clearFilters">清除筛选</button>
        </div>
      </div>

      <!-- 党员列表 -->
      <div class="members-table">
        <table>
          <thead>
            <tr>
              <th>头像</th>
              <th>姓名</th>
              <th>类型</th>
              <th>党组织</th>
              <th>职务</th>
              <th>入党时间</th>
              <th>状态</th>
              <th>联系方式</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="member in currentPageMembers" :key="member.id">
              <td><img :src="member.avatar" :alt="member.name" class="avatar" /></td>
              <td>{{ member.name }}</td>
              <td>
                <span class="badge" :class="member.type === 'teacher' ? 'badge-teacher' : 'badge-student'">
                  {{ member.type === 'teacher' ? '教师党员' : '学生党员' }}
                </span>
              </td>
              <td>{{ member.partyOrg }}</td>
              <td>{{ member.partyPost }}</td>
              <td>{{ member.joinDate }}</td>
              <td>
                <span class="badge" :class="{
                  'badge-active': member.status === 'active',
                  'badge-probationary': member.status === 'probationary',
                  'badge-mobile': member.status === 'mobile'
                }">
                  {{ 
                    member.status === 'active' ? '正式党员' : 
                    member.status === 'probationary' ? '预备党员' : 
                    member.status === 'mobile' ? '流动党员' : '未知'
                  }}
                </span>
              </td>
              <td>
                <div class="contact-info">
                  <span>{{ member.phone }}</span>
                  <span v-if="member.email">{{ member.email }}</span>
                </div>
              </td>
              <td>
                <div class="action-buttons">
                  <button class="btn btn-small btn-primary" @click="openEditModal(member)">编辑</button>
                  <button class="btn btn-small btn-danger" @click="openDeleteModal(member)">删除</button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        
        <!-- 无数据提示 -->
        <div v-if="filteredMembers.length === 0" class="no-data">
          <p>没有找到符合条件的党员</p>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination" v-if="pagination.total > 0">
        <button 
          class="btn btn-small btn-secondary" 
          @click="handlePageChange(pagination.currentPage - 1)"
          :disabled="pagination.currentPage === 1"
        >
          上一页
        </button>
        <span class="page-info">
          第 {{ pagination.currentPage }} / {{ Math.ceil(pagination.total / pagination.pageSize) }} 页
        </span>
        <button 
          class="btn btn-small btn-secondary" 
          @click="handlePageChange(pagination.currentPage + 1)"
          :disabled="pagination.currentPage === Math.ceil(pagination.total / pagination.pageSize)"
        >
          下一页
        </button>
      </div>

      <!-- 添加党员模态框 -->
      <div class="modal" v-if="showAddModal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>添加党员</h3>
            <button class="close-btn" @click="showAddModal = false">×</button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>姓名：</label>
              <input type="text" class="form-control" v-model="newMember.name" required />
            </div>
            <div class="form-group">
              <label>类型：</label>
              <select class="form-control" v-model="newMember.type">
                <option value="teacher">教师党员</option>
                <option value="student">学生党员</option>
              </select>
            </div>
            <div class="form-group">
              <label>党组织：</label>
              <select class="form-control" v-model="newMember.partyOrg" required>
                <option value="">请选择党组织</option>
                <option v-for="org in partyOrgs.slice(1)" :key="org" :value="org">
                  {{ org }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>党内职务：</label>
              <select class="form-control" v-model="newMember.partyPost">
                <option value="普通党员">普通党员</option>
                <option value="支部书记">支部书记</option>
                <option value="组织委员">组织委员</option>
                <option value="宣传委员">宣传委员</option>
              </select>
            </div>
            <div class="form-group">
              <label>入党时间：</label>
              <input type="date" class="form-control" v-model="newMember.joinDate" required />
            </div>
            <div class="form-group">
              <label>党员状态：</label>
              <select class="form-control" v-model="newMember.status">
                <option value="active">正式党员</option>
                <option value="probationary">预备党员</option>
                <option value="mobile">流动党员</option>
              </select>
            </div>
            
            <!-- 教师党员特有字段 -->
            <div v-if="newMember.type === 'teacher'" class="type-specific-fields">
              <div class="form-group">
                <label>所在院系：</label>
                <input type="text" class="form-control" v-model="newMember.department" />
              </div>
              <div class="form-group">
                <label>职称：</label>
                <input type="text" class="form-control" v-model="newMember.position" />
              </div>
            </div>
            
            <!-- 学生党员特有字段 -->
            <div v-if="newMember.type === 'student'" class="type-specific-fields">
              <div class="form-group">
                <label>年级：</label>
                <input type="text" class="form-control" v-model="newMember.grade" />
              </div>
              <div class="form-group">
                <label>专业：</label>
                <input type="text" class="form-control" v-model="newMember.major" />
              </div>
              <div class="form-group">
                <label>班级：</label>
                <input type="text" class="form-control" v-model="newMember.class" />
              </div>
              <div class="form-group">
                <label>学号：</label>
                <input type="text" class="form-control" v-model="newMember.studentId" />
              </div>
            </div>
            
            <div class="form-group">
              <label>电话：</label>
              <input type="tel" class="form-control" v-model="newMember.phone" required />
            </div>
            <div class="form-group">
              <label>邮箱：</label>
              <input type="email" class="form-control" v-model="newMember.email" />
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showAddModal = false">取消</button>
            <button class="btn btn-primary" @click="addMember">添加</button>
          </div>
        </div>
      </div>

      <!-- 编辑党员模态框 -->
      <div class="modal" v-if="showEditModal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>编辑党员</h3>
            <button class="close-btn" @click="showEditModal = false">×</button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>姓名：</label>
              <input type="text" class="form-control" v-model="editMember.name" required />
            </div>
            <div class="form-group">
              <label>类型：</label>
              <select class="form-control" v-model="editMember.type">
                <option value="teacher">教师党员</option>
                <option value="student">学生党员</option>
              </select>
            </div>
            <div class="form-group">
              <label>党组织：</label>
              <select class="form-control" v-model="editMember.partyOrg" required>
                <option value="">请选择党组织</option>
                <option v-for="org in partyOrgs.slice(1)" :key="org" :value="org">
                  {{ org }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>党内职务：</label>
              <select class="form-control" v-model="editMember.partyPost">
                <option value="普通党员">普通党员</option>
                <option value="支部书记">支部书记</option>
                <option value="组织委员">组织委员</option>
                <option value="宣传委员">宣传委员</option>
              </select>
            </div>
            <div class="form-group">
              <label>入党时间：</label>
              <input type="date" class="form-control" v-model="editMember.joinDate" required />
            </div>
            <div class="form-group">
              <label>党员状态：</label>
              <select class="form-control" v-model="editMember.status">
                <option value="active">正式党员</option>
                <option value="probationary">预备党员</option>
                <option value="mobile">流动党员</option>
              </select>
            </div>
            
            <!-- 教师党员特有字段 -->
            <div v-if="editMember.type === 'teacher'" class="type-specific-fields">
              <div class="form-group">
                <label>所在院系：</label>
                <input type="text" class="form-control" v-model="editMember.department" />
              </div>
              <div class="form-group">
                <label>职称：</label>
                <input type="text" class="form-control" v-model="editMember.position" />
              </div>
            </div>
            
            <!-- 学生党员特有字段 -->
            <div v-if="editMember.type === 'student'" class="type-specific-fields">
              <div class="form-group">
                <label>年级：</label>
                <input type="text" class="form-control" v-model="editMember.grade" />
              </div>
              <div class="form-group">
                <label>专业：</label>
                <input type="text" class="form-control" v-model="editMember.major" />
              </div>
              <div class="form-group">
                <label>班级：</label>
                <input type="text" class="form-control" v-model="editMember.class" />
              </div>
              <div class="form-group">
                <label>学号：</label>
                <input type="text" class="form-control" v-model="editMember.studentId" />
              </div>
            </div>
            
            <div class="form-group">
              <label>电话：</label>
              <input type="tel" class="form-control" v-model="editMember.phone" required />
            </div>
            <div class="form-group">
              <label>邮箱：</label>
              <input type="email" class="form-control" v-model="editMember.email" />
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showEditModal = false">取消</button>
            <button class="btn btn-primary" @click="updateMember">保存</button>
          </div>
        </div>
      </div>

      <!-- 删除党员模态框 -->
      <div class="modal" v-if="showDeleteModal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>删除党员</h3>
            <button class="close-btn" @click="showDeleteModal = false">×</button>
          </div>
          <div class="modal-body">
            <p>确定要删除党员 "{{ selectedMember?.name }}" 吗？</p>
            <p class="warning-text">此操作将删除该党员的所有信息，且不可恢复！</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
            <button class="btn btn-danger" @click="deleteMember">删除</button>
          </div>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<style scoped>
.party-member-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-header h2 {
  margin: 0;
  color: #2c3e50;
  font-size: 24px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
}

.btn-primary {
  background-color: #d93025;
  color: white;
}

.btn-primary:hover {
  background-color: #b3241a;
}

.btn-secondary {
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
}

.btn-secondary:hover {
  background-color: #e5e5e5;
}

.btn-danger {
  background-color: #ea4335;
  color: white;
}

.btn-danger:hover {
  background-color: #c1352b;
}

.btn-small {
  padding: 4px 12px;
  font-size: 12px;
}

.filter-section {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.filter-group {
  flex: 1;
  min-width: 150px;
}

.form-control {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.members-table {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin-bottom: 20px;
}

.members-table table {
  width: 100%;
  border-collapse: collapse;
}

.members-table th,
.members-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.members-table th {
  background-color: #fafafa;
  font-weight: 600;
  color: #303133;
}

.members-table tr:hover {
  background-color: #f5f5f5;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.badge-teacher {
  background-color: #e3f2fd;
  color: #1565c0;
}

.badge-student {
  background-color: #f3e5f5;
  color: #7b1fa2;
}

.badge-active {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.badge-probationary {
  background-color: #fff3e0;
  color: #ef6c00;
}

.badge-mobile {
  background-color: #ffebee;
  color: #c62828;
}

.contact-info {
  font-size: 12px;
}

.contact-info span {
  display: block;
  color: #666;
}

.action-buttons {
  display: flex;
  gap: 5px;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 10px;
}

.page-info {
  font-size: 14px;
  color: #666;
}

/* 模态框样式 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
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
  color: #2c3e50;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
}

.close-btn:hover {
  color: #333;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #333;
}

.type-specific-fields {
  margin: 20px 0;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  border-top: 1px solid #eee;
}

.warning-text {
  color: #ea4335;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .filter-section {
    flex-direction: column;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .members-table table {
    font-size: 12px;
  }
  
  .members-table th,
  .members-table td {
    padding: 8px;
  }
}
</style>