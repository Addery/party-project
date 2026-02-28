<template>
  <AdminLayout>
    <div class="party-member-development">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>核心党务工作</el-breadcrumb-item>
        <el-breadcrumb-item>党员发展</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2>党员发展全周期管理</h2>
        <el-button type="primary" @click="showCreateDialog">
          <el-icon><Plus /></el-icon> 新增发展对象
        </el-button>
      </div>

      <!-- 搜索和筛选区域 -->
      <div class="search-filter">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索姓名/学号/工号"
          prefix-icon="Search"
          style="width: 300px; margin-right: 10px;"
        />
        <el-select
          v-model="filterStage"
          placeholder="发展阶段"
          style="width: 150px; margin-right: 10px;"
        >
          <el-option label="全部" value="" />
          <el-option label="申请入党" value="申请入党" />
          <el-option label="入党积极分子" value="入党积极分子" />
          <el-option label="发展对象" value="发展对象" />
          <el-option label="预备党员" value="预备党员" />
          <el-option label="正式党员" value="正式党员" />
        </el-select>
        <el-select
          v-model="filterDepartment"
          placeholder="所属部门"
          style="width: 150px; margin-right: 10px;"
        >
          <el-option label="全部" value="" />
          <el-option label="计算机学院" value="计算机学院" />
          <el-option label="软件学院" value="软件学院" />
          <el-option label="电子工程学院" value="电子工程学院" />
          <el-option label="经济管理学院" value="经济管理学院" />
          <el-option label="马克思主义学院" value="马克思主义学院" />
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

      <!-- 待办事项提醒 -->
      <el-card class="todo-card" shadow="hover" style="margin-bottom: 20px;">
        <template #header>
          <div class="card-header">
            <span>待办事项</span>
            <el-button type="text" size="small" @click="showAllTodos">查看全部</el-button>
          </div>
        </template>
        <div class="todo-list">
          <div v-for="todo in pendingTodos" :key="todo.id" class="todo-item">
            <el-alert
              :title="todo.title"
              :type="todo.type"
              :description="todo.description"
              show-icon
              :closable="false"
            >
              <template #default>
                <div class="todo-actions">
                  <el-button size="small" type="primary" @click="handleTodo(todo)">
                    处理
                  </el-button>
                </div>
              </template>
            </el-alert>
          </div>
          <div v-if="pendingTodos.length === 0" class="no-todos">
            <el-empty description="暂无待办事项" />
          </div>
        </div>
      </el-card>

      <!-- 发展对象列表 -->
      <div class="table-container">
        <el-table :data="paginatedDevelopments" style="width: 100%" border>
          <el-table-column type="selection" align="center" />
          <!-- ID到状态的列将作为滑动窗口的主体 -->
          <el-table-column prop="id" label="ID" align="center" />
          <el-table-column prop="name" label="姓名" min-width="150" show-overflow-tooltip />
          <el-table-column prop="type" label="类型">
            <template #default="scope">
              <el-tag :type="scope.row.type === '学生' ? 'primary' : 'success'">
                {{ scope.row.type }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="studentId" label="学号/工号" />
          <el-table-column prop="department" label="所属部门" min-width="120" show-overflow-tooltip />
          <el-table-column prop="stage" label="当前阶段">
            <template #default="scope">
              <el-tag :type="getStageColor(scope.row.stage)">
                {{ scope.row.stage }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="currentStep" label="当前步骤" min-width="120" show-overflow-tooltip />
          <el-table-column prop="createTime" label="申请时间" />
          <el-table-column prop="contactPerson" label="培养联系人" min-width="120" show-overflow-tooltip />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag :type="scope.row.status === '进行中' ? 'primary' : scope.row.status === '已完成' ? 'success' : 'warning'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <!-- 操作列固定在右侧 -->
          <el-table-column label="操作" min-width="250" fixed="right">
            <template #default="scope">
              <el-space size="small">
                <el-button size="small" @click="viewDevelopment(scope.row)">
                  <el-icon><View /></el-icon> 查看
                </el-button>
                <el-button size="small" type="primary" @click="editDevelopment(scope.row)">
                  <el-icon><Edit /></el-icon> 编辑
                </el-button>
                <el-button size="small" type="success" @click="progressDevelopment(scope.row)">
                  <el-icon><Check /></el-icon> 推进
                </el-button>
                <el-button size="small" type="danger" @click="deleteDevelopment(scope.row)">
                  <el-icon><Delete /></el-icon> 删除
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
          :total="filteredDevelopments.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 创建/编辑发展对象对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="800px"
        @close="resetForm"
      >
        <el-form :model="developmentForm" label-width="100px">
          <el-form-item label="姓名" required>
            <el-input v-model="developmentForm.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="类型" required>
            <el-select v-model="developmentForm.type" placeholder="请选择类型">
              <el-option label="学生" value="学生" />
              <el-option label="教师" value="教师" />
            </el-select>
          </el-form-item>
          <el-form-item label="学号/工号" required>
            <el-input v-model="developmentForm.studentId" placeholder="请输入学号或工号" />
          </el-form-item>
          <el-form-item label="所属部门" required>
            <el-select v-model="developmentForm.department" placeholder="请选择所属部门">
              <el-option label="计算机学院" value="计算机学院" />
              <el-option label="软件学院" value="软件学院" />
              <el-option label="电子工程学院" value="电子工程学院" />
              <el-option label="经济管理学院" value="经济管理学院" />
              <el-option label="马克思主义学院" value="马克思主义学院" />
            </el-select>
          </el-form-item>
          <el-form-item label="性别" required>
            <el-select v-model="developmentForm.gender" placeholder="请选择性别">
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期" required>
            <el-date-picker
              v-model="developmentForm.birthDate"
              type="date"
              placeholder="选择出生日期"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="联系电话" required>
            <el-input v-model="developmentForm.phone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="developmentForm.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="培养联系人" required>
            <el-input v-model="developmentForm.contactPerson" placeholder="请输入培养联系人" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input
              v-model="developmentForm.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveDevelopment">保存</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 发展详情对话框 -->
      <el-dialog
        v-model="detailDialogVisible"
        title="发展详情"
        width="900px"
      >
        <div v-if="currentDevelopment" class="development-detail">
          <div class="detail-header">
            <h3>{{ currentDevelopment.name }} - 发展详情</h3>
            <el-tag :type="getStageColor(currentDevelopment.stage)">
              {{ currentDevelopment.stage }} - {{ currentDevelopment.currentStep }}
            </el-tag>
          </div>
          
          <!-- 基本信息 -->
          <el-card class="detail-card" shadow="hover" style="margin-bottom: 20px;">
            <template #header>
              <div class="card-header">
                <span>基本信息</span>
              </div>
            </template>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">姓名：</span>
                <span class="info-value">{{ currentDevelopment.name }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">类型：</span>
                <span class="info-value">{{ currentDevelopment.type }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">学号/工号：</span>
                <span class="info-value">{{ currentDevelopment.studentId }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">所属部门：</span>
                <span class="info-value">{{ currentDevelopment.department }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">性别：</span>
                <span class="info-value">{{ currentDevelopment.gender }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">出生日期：</span>
                <span class="info-value">{{ currentDevelopment.birthDate }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">联系电话：</span>
                <span class="info-value">{{ currentDevelopment.phone }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">邮箱：</span>
                <span class="info-value">{{ currentDevelopment.email }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">培养联系人：</span>
                <span class="info-value">{{ currentDevelopment.contactPerson }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">申请时间：</span>
                <span class="info-value">{{ currentDevelopment.createTime }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">当前状态：</span>
                <span class="info-value">
                  <el-tag :type="currentDevelopment.status === '进行中' ? 'primary' : 'success'">
                    {{ currentDevelopment.status }}
                  </el-tag>
                </span>
              </div>
            </div>
            <div v-if="currentDevelopment.remark" class="info-remark">
              <span class="info-label">备注：</span>
              <span class="info-value">{{ currentDevelopment.remark }}</span>
            </div>
          </el-card>

          <!-- 发展流程 -->
          <el-card class="detail-card" shadow="hover" style="margin-bottom: 20px;">
            <template #header>
              <div class="card-header">
                <span>发展流程</span>
              </div>
            </template>
            <div class="process-flow">
              <el-steps :active="currentStageIndex" direction="vertical">
                <el-step
                  v-for="(stage, index) in developmentStages"
                  :key="stage.name"
                  :title="stage.name"
                  :description="stage.description"
                >
                  <template #description>
                    <div class="stage-steps">
                      <el-timeline>
                        <el-timeline-item
                          v-for="(step, stepIndex) in stage.steps"
                          :key="step.name"
                          :timestamp="step.time"
                          :type="step.status === 'completed' ? 'success' : step.status === 'pending' ? 'warning' : ''"
                          :size="step.status === 'completed' ? 'large' : 'normal'"
                          :icon="step.status === 'completed' ? 'Check' : ''"
                        >
                          <div class="step-content">
                            <h4>{{ step.name }}</h4>
                            <p>{{ step.description }}</p>
                            <div class="step-actions">
                              <el-button
                                v-if="step.status === 'pending' && index === currentStageIndex && stepIndex === currentStepIndex"
                                size="small"
                                type="primary"
                                @click="completeStep(step)"
                              >
                                完成此步骤
                              </el-button>
                              <el-button
                                v-if="step.materials && step.materials.length > 0"
                                size="small"
                                @click="viewMaterials(step)"
                              >
                                查看材料 ({{ step.materials.length }})
                              </el-button>
                              <el-button
                                v-if="step.status !== 'completed'"
                                size="small"
                                @click="uploadMaterials(step)"
                              >
                                上传材料
                              </el-button>
                            </div>
                          </div>
                        </el-timeline-item>
                      </el-timeline>
                    </div>
                  </template>
                </el-step>
              </el-steps>
            </div>
          </el-card>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="detailDialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 材料上传对话框 -->
      <el-dialog
        v-model="uploadDialogVisible"
        title="上传材料"
        width="600px"
      >
        <div class="upload-dialog">
          <h4>{{ currentStep?.name }} - 材料上传</h4>
          <el-upload
            class="upload-demo"
            action="#"
            :file-list="currentStepFiles"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-success="handleUploadSuccess"
            :auto-upload="false"
            list-type="text"
          >
            <el-button type="primary">
              <el-icon><Upload /></el-icon> 选择文件
            </el-button>
            <template #tip>
              <div class="el-upload__tip">
                支持上传PDF、Word、Excel等文件，单个文件不超过10MB
              </div>
            </template>
          </el-upload>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="uploadDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveUploadedFiles">保存</el-button>
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
import { Plus, Edit, Delete, View, Check, Upload } from '@element-plus/icons-vue'

// 响应式数据
const searchKeyword = ref('')
const filterStage = ref('')
const filterDepartment = ref('')
const dateRange = ref('')
const currentPage = ref(1)
const pageSize = ref(10)

// 对话框相关
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const uploadDialogVisible = ref(false)
const dialogTitle = ref('新增发展对象')
const currentDevelopment = ref(null)
const currentStep = ref(null)
const currentStepFiles = ref([])
const isEdit = ref(false)

// 待办事项
const todos = ref([
  {
    id: 1,
    title: '王晓明的入党申请书需要审批',
    description: '计算机学院王晓明同学的入党申请书已提交3天，请及时审批',
    type: 'warning',
    status: 'pending',
    relatedTo: 1
  },
  {
    id: 2,
    title: '李婷婷的发展对象考察期即将到期',
    description: '软件学院李婷婷同学的发展对象考察期将于3天后到期，请及时处理',
    type: 'error',
    status: 'pending',
    relatedTo: 2
  },
  {
    id: 3,
    title: '张伟东的预备党员转正申请需要审批',
    description: '电子工程学院张伟东同学的预备党员转正申请已提交，请及时审批',
    type: 'info',
    status: 'pending',
    relatedTo: 3
  }
])

// 表单数据
const developmentForm = ref({
  id: '',
  name: '',
  type: '学生',
  studentId: '',
  department: '',
  gender: '',
  birthDate: '',
  phone: '',
  email: '',
  contactPerson: '',
  stage: '申请入党',
  currentStep: '递交入党申请书',
  createTime: '',
  status: '进行中',
  remark: ''
})

// 发展阶段和步骤定义
const developmentStages = ref([
  {
    name: '申请入党',
    description: '入党积极分子的培养教育和考察',
    steps: [
      { name: '递交入党申请书', description: '申请人自愿向党组织递交入党申请书', status: 'completed', materials: [] },
      { name: '党组织派人谈话', description: '党组织在一个月内派人同申请人谈话', status: 'completed', materials: [] },
      { name: '确定入党积极分子', description: '经支部委员会讨论确定为入党积极分子', status: 'completed', materials: [] },
      { name: '上级党委备案', description: '将入党积极分子名单报上级党委备案', status: 'completed', materials: [] },
      { name: '指定培养联系人', description: '指定1-2名正式党员作为培养联系人', status: 'completed', materials: [] },
      { name: '培养教育考察', description: '进行一年以上的培养教育和考察', status: 'completed', materials: [] }
    ]
  },
  {
    name: '发展对象',
    description: '发展对象的确定和考察',
    steps: [
      { name: '确定发展对象', description: '经支部委员会讨论确定为发展对象', status: 'completed', materials: [] },
      { name: '上级党委备案', description: '将发展对象名单报上级党委备案', status: 'completed', materials: [] },
      { name: '确定入党介绍人', description: '确定2名正式党员作为入党介绍人', status: 'completed', materials: [] },
      { name: '进行政治审查', description: '对发展对象进行政治审查', status: 'completed', materials: [] },
      { name: '开展集中培训', description: '参加上级党组织的集中培训', status: 'completed', materials: [] }
    ]
  },
  {
    name: '预备党员',
    description: '预备党员的接收',
    steps: [
      { name: '支部委员会审查', description: '支部委员会对发展对象进行审查', status: 'completed', materials: [] },
      { name: '上级党委预审', description: '上级党委对发展对象进行预审', status: 'completed', materials: [] },
      { name: '填写入党志愿书', description: '发展对象填写《中国共产党入党志愿书》', status: 'completed', materials: [] },
      { name: '支部大会讨论', description: '召开支部大会讨论接收预备党员', status: 'completed', materials: [] },
      { name: '上级党委派人谈话', description: '上级党委在审批前派人同发展对象谈话', status: 'completed', materials: [] },
      { name: '上级党委审批', description: '上级党委对接收预备党员进行审批', status: 'completed', materials: [] },
      { name: '报再上一级党委组织部门备案', description: '将预备党员名单报再上一级党委组织部门备案', status: 'completed', materials: [] }
    ]
  },
  {
    name: '预备党员教育考察',
    description: '预备党员的教育考察和转正',
    steps: [
      { name: '编入党支部', description: '将预备党员编入党支部和党小组', status: 'completed', materials: [] },
      { name: '入党宣誓', description: '举行入党宣誓仪式', status: 'completed', materials: [] },
      { name: '继续教育考察', description: '进行一年的继续教育和考察', status: 'completed', materials: [] },
      { name: '提出转正申请', description: '预备党员在预备期满前提出书面转正申请', status: 'pending', materials: [] },
      { name: '支部大会讨论转正', description: '召开支部大会讨论预备党员转正', status: 'pending', materials: [] },
      { name: '上级党委审批', description: '上级党委对预备党员转正进行审批', status: 'pending', materials: [] },
      { name: '材料归档', description: '将入党材料存入本人人事档案', status: 'pending', materials: [] }
    ]
  },
  {
    name: '正式党员',
    description: '正式党员的教育管理',
    steps: [
      { name: '编入党支部', description: '正式编入党支部', status: 'pending', materials: [] },
      { name: '继续教育培养', description: '进行长期的教育培养', status: 'pending', materials: [] }
    ]
  }
])

// 模拟数据
const developments = ref([
  {    id: 1,    name: '王晓明',    type: '学生',    studentId: '20200001',    department: '计算机学院',    gender: '男',    birthDate: '2002-01-01',    phone: '13800138001',    email: 'wangxiaoming@example.com',    contactPerson: '李老师',    stage: '申请入党',    currentStep: '培养教育考察',    createTime: '2023-01-15',    status: '进行中',    remark: '表现优秀，积极向党组织靠拢'  },  {    id: 2,    name: '李婷婷',    type: '学生',    studentId: '20200002',    department: '软件学院',    gender: '女',    birthDate: '2002-03-15',    phone: '13800138002',    email: 'litingting@example.com',    contactPerson: '王老师',    stage: '发展对象',    currentStep: '开展集中培训',    createTime: '2023-02-20',    status: '进行中',    remark: '学习成绩优异，社会实践经验丰富'  },  {    id: 3,    name: '张伟东',    type: '教师',    studentId: 'T001',    department: '电子工程学院',    gender: '男',    birthDate: '1985-07-10',    phone: '13800138003',    email: 'zhangweidong@example.com',    contactPerson: '赵老师',    stage: '预备党员',    currentStep: '上级党委审批',    createTime: '2023-03-10',    status: '进行中',    remark: '教学科研能力强，政治素质过硬'  },  {    id: 4,    name: '陈思思',    type: '学生',    studentId: '20200003',    department: '经济管理学院',    gender: '女',    birthDate: '2002-05-20',    phone: '13800138004',    email: 'chensisi@example.com',    contactPerson: '钱老师',    stage: '申请入党',    currentStep: '党组织派人谈话',    createTime: '2023-04-01',    status: '进行中',    remark: '积极参与公益活动，有较强的组织能力'  },  {    id: 5,    name: '刘建国',    type: '教师',    studentId: 'T002',    department: '马克思主义学院',    gender: '男',    birthDate: '1990-09-15',    phone: '13800138005',    email: 'liuguojian@example.com',    contactPerson: '周老师',    stage: '预备党员教育考察',    currentStep: '提出转正申请',    createTime: '2023-05-05',    status: '进行中',    remark: '思想政治理论扎实，教学效果良好'  },  {    id: 6,    name: '吴子轩',    type: '学生',    studentId: '20200004',    department: '计算机学院',    gender: '男',    birthDate: '2002-11-30',    phone: '13800138006',    email: 'wuzixuan@example.com',    contactPerson: '吴老师',    stage: '发展对象',    currentStep: '进行政治审查',    createTime: '2023-06-15',    status: '进行中',    remark: '专业能力突出，多次获得竞赛奖项'  },  {    id: 7,    name: '林小雨',    type: '学生',    studentId: '20200005',    department: '软件学院',    gender: '女',    birthDate: '2002-08-25',    phone: '13800138007',    email: 'linxiaoyu@example.com',    contactPerson: '郑老师',    stage: '申请入党',    currentStep: '确定入党积极分子',    createTime: '2023-07-20',    status: '进行中',    remark: '团结同学，乐于助人，集体荣誉感强'  },  {    id: 8,    name: '黄志远',    type: '教师',    studentId: 'T003',    department: '电子工程学院',    gender: '男',    birthDate: '1988-04-12',    phone: '13800138008',    email: 'huangzhiyuan@example.com',
    contactPerson: '王老师',
    stage: '预备党员',
    currentStep: '支部大会讨论',
    createTime: '2023-08-10',
    status: '进行中',
    remark: '科研成果显著，具有团队合作精神'
  }
])

// 计算属性：筛选后的发展对象列表
const filteredDevelopments = computed(() => {
  let result = [...developments.value]
  
  // 按关键词筛选
  if (searchKeyword.value) {
    result = result.filter(item => 
      item.name.includes(searchKeyword.value) || 
      item.studentId.includes(searchKeyword.value)
    )
  }
  
  // 按阶段筛选
  if (filterStage.value) {
    result = result.filter(item => 
      item.stage === filterStage.value
    )
  }
  
  // 按部门筛选
  if (filterDepartment.value) {
    result = result.filter(item => 
      item.department === filterDepartment.value
    )
  }
  
  // 按日期范围筛选
  if (dateRange.value && dateRange.value.length === 2) {
    // 使用JavaScript原生Date方法处理日期范围
    const startDate = new Date(dateRange.value[0])
    const endDate = new Date(dateRange.value[1])
    
    // 检查日期是否有效
    if (!isNaN(startDate.getTime()) && !isNaN(endDate.getTime())) {
      // 格式化日期为YYYY-MM-DD格式
      const formatDate = (date) => {
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        return `${year}-${month}-${day}`
      }
      
      const formattedStartDate = formatDate(startDate)
      const formattedEndDate = formatDate(endDate)
      
      result = result.filter(item => 
        item && item.createTime && item.createTime >= formattedStartDate && item.createTime <= formattedEndDate
      )
    }
  }
  
  return result
})

// 分页数据
const paginatedDevelopments = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredDevelopments.value.slice(start, end)
})

// 计算属性：待处理的待办事项
const pendingTodos = computed(() => {
  return todos.value.filter(todo => todo.status === 'pending').slice(0, 3)
})

// 计算属性：当前阶段索引
const currentStageIndex = computed(() => {
  if (!currentDevelopment.value) return 0
  const stageIndex = developmentStages.value.findIndex(stage => 
    stage.name === currentDevelopment.value.stage
  )
  return stageIndex >= 0 ? stageIndex : 0
})

// 计算属性：当前步骤索引
const currentStepIndex = computed(() => {
  if (!currentDevelopment.value) return 0
  const stage = developmentStages.value.find(stage => 
    stage.name === currentDevelopment.value.stage
  )
  if (!stage) return 0
  const stepIndex = stage.steps.findIndex(step => 
    step.name === currentDevelopment.value.currentStep
  )
  return stepIndex >= 0 ? stepIndex : 0
})

// 方法：获取阶段颜色
const getStageColor = (stage) => {
  switch (stage) {
    case '申请入党': return 'info'
    case '发展对象': return 'warning'
    case '预备党员': return 'primary'
    case '预备党员教育考察': return 'success'
    case '正式党员': return 'success'
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
  filterStage.value = ''
  filterDepartment.value = ''
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
  dialogTitle.value = '新增发展对象'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 方法：编辑发展对象
const editDevelopment = (development) => {
  dialogTitle.value = '编辑发展对象'
  isEdit.value = true
  developmentForm.value = { ...development }
  dialogVisible.value = true
}

// 方法：查看发展详情
const viewDevelopment = (development) => {
  currentDevelopment.value = development
  detailDialogVisible.value = true
}

// 方法：推进发展流程
const progressDevelopment = (development) => {
  ElMessage.confirm('确定要推进该发展对象的流程吗？', '推进确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'primary'
  }).then(() => {
    // 这里应该实现推进流程的逻辑
    ElMessage.success('流程推进成功')
  }).catch(() => {
    // 取消操作
  })
}

// 方法：删除发展对象
const deleteDevelopment = (development) => {
  ElMessage.confirm('确定要删除该发展对象吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    const index = developments.value.findIndex(item => item.id === development.id)
    if (index !== -1) {
      developments.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 方法：重置表单
const resetForm = () => {
  developmentForm.value = {
    id: '',
    name: '',
    type: '学生',
    studentId: '',
    department: '',
    gender: '',
    birthDate: '',
    phone: '',
    email: '',
    contactPerson: '',
    stage: '申请入党',
    currentStep: '递交入党申请书',
    createTime: new Date().toISOString().split('T')[0],
    status: '进行中',
    remark: ''
  }
}

// 方法：保存发展对象
const saveDevelopment = () => {
  // 简单验证
  if (!developmentForm.value.name || !developmentForm.value.studentId || !developmentForm.value.department || !developmentForm.value.gender || !developmentForm.value.birthDate || !developmentForm.value.phone || !developmentForm.value.contactPerson) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  if (isEdit.value) {
    // 编辑模式
    const index = developments.value.findIndex(item => item.id === developmentForm.value.id)
    if (index !== -1) {
      developments.value[index] = { ...developmentForm.value }
      ElMessage.success('编辑成功')
    }
  } else {
    // 创建模式
    const newDevelopment = {
      ...developmentForm.value,
      id: Date.now(), // 简单生成ID
      createTime: new Date().toISOString().split('T')[0]
    }
    developments.value.unshift(newDevelopment)
    ElMessage.success('创建成功')
  }
  
  dialogVisible.value = false
  resetForm()
}

// 方法：处理待办事项
const handleTodo = (todo) => {
  // 这里应该实现处理待办事项的逻辑
  todo.status = 'completed'
  ElMessage.success('待办事项已处理')
}

// 方法：显示所有待办事项
const showAllTodos = () => {
  ElMessage.info('查看全部待办事项功能待实现')
}

// 方法：完成步骤
const completeStep = (step) => {
  step.status = 'completed'
  step.time = new Date().toISOString().split('T')[0]
  ElMessage.success('步骤完成')
}

// 方法：查看材料
const viewMaterials = (step) => {
  if (step.materials && step.materials.length > 0) {
    ElMessage.info('查看材料功能待实现')
  } else {
    ElMessage.warning('暂无材料')
  }
}

// 方法：上传材料
const uploadMaterials = (step) => {
  currentStep.value = step
  currentStepFiles.value = step.materials || []
  uploadDialogVisible.value = true
}

// 文件上传相关方法
const handlePreview = (file) => {
  console.log('Preview file:', file)
}

const handleRemove = (file) => {
  const index = currentStepFiles.value.indexOf(file)
  if (index !== -1) {
    currentStepFiles.value.splice(index, 1)
  }
}

const handleUploadSuccess = (response, file) => {
  console.log('Upload success:', response, file)
}

const saveUploadedFiles = () => {
  if (currentStep.value) {
    currentStep.value.materials = [...currentStepFiles.value]
    ElMessage.success('材料上传成功')
    uploadDialogVisible.value = false
  }
}

// 生命周期钩子
onMounted(() => {
  console.log('发展党员管理页面加载完成')
})
</script>

<style scoped>
.party-member-development {
  margin: 0 auto;
  padding: 24px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  max-width: 100%;
  overflow: hidden;
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

.todo-card {
  margin-bottom: 24px;
  border-radius: 8px;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.todo-list {
  max-height: 300px;
  overflow-y: auto;
}

.todo-item {
  margin-bottom: 10px;
  position: relative;
}

.todo-actions {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
}

.todo-item .el-alert {
  padding-right: 120px;
  margin: 0;
}

.no-todos {
  padding: 20px 0;
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

.development-detail {
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

.detail-card {
  margin-bottom: 24px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-label {
  font-weight: 500;
  color: #495057;
  margin-bottom: 5px;
}

.info-value {
  color: #212529;
}

.info-remark {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px dashed #e9ecef;
}

.process-flow {
  padding: 24px 0;
}

.stage-steps {
  margin-top: 10px;
  margin-left: 20px;
}

.step-content {
  padding: 16px;
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 8px;
}

.step-content h4 {
  margin: 0 0 8px 0;
  color: #212529;
  font-size: 16px;
  font-weight: 600;
}

.step-content p {
  margin: 0 0 12px 0;
  color: #495057;
  font-size: 14px;
}

.step-actions {
  margin-top: 10px;
}

.upload-dialog {
  padding: 24px 0;
}

.upload-dialog h4 {
  margin-bottom: 24px;
  color: #212529;
  font-weight: 600;
}

:deep(.el-table) {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  min-width: 100%;
  box-sizing: border-box;
}

.table-container {
  overflow-x: auto;
  margin-bottom: 16px;
  position: relative;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  padding: 1px;
  width: 100%;
  min-width: 800px;
  box-sizing: border-box;
}

/* 优化滚动条样式 */
.table-container::-webkit-scrollbar {
  height: 8px;
}

.table-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.table-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 确保表格在容器内正确显示 */
.table-container :deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

/* 优化表格头部样式 */
.table-container :deep(.el-table__header-wrapper) {
  position: sticky;
  top: 0;
  z-index: 10;
}

:deep(.el-table__header-wrapper) {
  background-color: #f8f9fa;
}

:deep(.el-table__header tr th) {
  background-color: #f8f9fa;
  font-weight: 500;
  color: #495057;
  border-bottom: 2px solid #e9ecef;
  white-space: nowrap;
  padding: 12px 10px;
}

:deep(.el-table__body tr td) {
  white-space: nowrap;
  padding: 12px 10px;
  border-bottom: 1px solid #f0f0f0;
}

:deep(.el-table__body tr:hover) {
  background-color: #f8f9fa;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #495057;
}

:deep(.el-form-item__content) {
  color: #212529;
}

:deep(.el-dialog) {
  border-radius: 4px;
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid #e9ecef;
}

:deep(.el-dialog__title) {
  font-weight: 600;
  color: #212529;
}

:deep(.el-dialog__footer) {
  border-top: 1px solid #e9ecef;
  padding-top: 16px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .party-member-development {
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