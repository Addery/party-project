<template>
  <AdminLayout>
    <div class="question-management">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/admin/exams' }">在线考试</el-breadcrumb-item>
        <el-breadcrumb-item>题目管理</el-breadcrumb-item>
      </el-breadcrumb>

      <div class="page-header">
        <h2>题目管理</h2>
        <div class="header-actions">
          <el-button type="primary" @click="createQuestion">
            <el-icon>
              <Plus />
            </el-icon>
            新建题目
          </el-button>
          <el-button @click="importQuestions">批量导入</el-button>
          <el-button @click="exportQuestions">导出题目</el-button>
        </div>
      </div>

      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <el-input
          v-model="searchKey"
          placeholder="搜索题目内容/关键词"
          prefix-icon="Search"
          clearable
          class="search-input"
        />
        
        <el-select
          v-model="filterType"
          placeholder="筛选题型"
          clearable
          class="filter-select"
        >
          <el-option label="所有题型" value="" />
          <el-option label="单选题" value="single_choice" />
          <el-option label="多选题" value="multiple_choice" />
          <el-option label="判断题" value="judgement" />
          <el-option label="简答题" value="short_answer" />
        </el-select>
        
        <el-select
          v-model="filterDifficulty"
          placeholder="筛选难度"
          clearable
          class="filter-select"
        >
          <el-option label="所有难度" value="" />
          <el-option label="简单" value="easy" />
          <el-option label="中等" value="medium" />
          <el-option label="困难" value="hard" />
        </el-select>
        
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>

      <!-- 自适应+虚拟滚动水平滑动窗口 -->
      <div 
        ref="containerRef"
        class="adaptive-table-container"
        @scroll="handleScroll"
      >
        <!-- 虚拟滚动容器 -->
        <div class="virtual-scroll-wrapper" :style="{ width: `${totalItemsWidth.value}px` }">
          <!-- 可视区域 -->
          <div 
            class="virtual-scroll-viewport"
            :style="{ transform: `translateX(${offsetLeft.value}px)` }"
          >
            <el-table
              v-loading="loading"
              :data="visibleQuestions"
              style="width: 100%"
              stripe
              border
              :header-cell-style="{ backgroundColor: '#f8f9fa', fontWeight: '500' }"
            >
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="type" label="题型" width="120">
                <template #default="scope">
                  <el-tag :type="getQuestionTypeTagType(scope.row.type)">
                    {{ getQuestionTypeName(scope.row.type) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="content" label="题目内容" min-width="300">
                <template #default="scope">
                  <div class="question-content" v-html="scope.row.content" />
                </template>
              </el-table-column>
              <el-table-column prop="options" label="选项" min-width="400">
                <template #default="scope">
                  <div v-if="scope.row.type !== 'short_answer'" class="question-options">
                    <div v-for="(option, index) in scope.row.options" :key="index" class="option-item">
                      <span class="option-label">{{ String.fromCharCode(65 + index) }}.</span>
                      <span class="option-content">{{ option }}</span>
                    </div>
                  </div>
                  <span v-else>无选项</span>
                </template>
              </el-table-column>
              <el-table-column prop="answer" label="正确答案" min-width="120">
                <template #default="scope">
                  <div class="question-answer">
                    <span v-if="scope.row.type === 'single_choice'">
                      {{ String.fromCharCode(65 + scope.row.answer) }}
                    </span>
                    <span v-else-if="scope.row.type === 'multiple_choice'">
                      {{ scope.row.answer.map(a => String.fromCharCode(65 + a)).join(', ') }}
                    </span>
                    <span v-else-if="scope.row.type === 'judgement'">
                      {{ scope.row.answer ? '正确' : '错误' }}
                    </span>
                    <span v-else>
                      {{ scope.row.answer }}
                    </span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="difficulty" label="难度" width="100">
                <template #default="scope">
                  <el-tag :type="getDifficultyTagType(scope.row.difficulty)">
                    {{ getDifficultyName(scope.row.difficulty) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="score" label="分值" width="80" />
              <el-table-column prop="category" label="分类" width="150">
                <template #default="scope">
                  <el-tag type="info">{{ scope.row.category }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createdTime" label="创建时间" width="180" />
              <el-table-column label="操作" width="200" fixed="right">
                <template #default="scope">
                  <el-space>
                    <el-button type="primary" size="small" @click="editQuestion(scope.row)">
                      <el-icon><Edit /></el-icon> 编辑
                    </el-button>
                    <el-button type="danger" size="small" @click="deleteQuestion(scope.row)">
                      <el-icon><Delete /></el-icon> 删除
                    </el-button>
                  </el-space>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="filteredQuestions.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 新建/编辑题目对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="70%"
        :destroy-on-close="true"
      >
        <el-form
          ref="questionFormRef"
          :model="questionForm"
          label-width="80px"
          :rules="questionRules"
        >
          <el-form-item label="题型" prop="type">
            <el-select v-model="questionForm.type" placeholder="请选择题型">
              <el-option label="单选题" value="single_choice" />
              <el-option label="多选题" value="multiple_choice" />
              <el-option label="判断题" value="judgement" />
              <el-option label="简答题" value="short_answer" />
            </el-select>
          </el-form-item>
          <el-form-item label="题目内容" prop="content">
            <el-input
              v-model="questionForm.content"
              type="textarea"
              :rows="4"
              placeholder="请输入题目内容"
            />
          </el-form-item>
          <el-form-item label="选项" prop="options" v-if="questionForm.type !== 'short_answer'">
            <div v-for="(option, index) in questionForm.options" :key="index" class="option-input-group">
              <span class="option-letter">{{ String.fromCharCode(65 + index) }}.</span>
              <el-input
                v-model="questionForm.options[index]"
                placeholder="请输入选项内容"
                :disabled="questionForm.type === 'judgement' && index > 1"
              />
              <el-button
                type="danger"
                size="small"
                icon="Delete"
                @click="removeOption(index)"
                v-if="questionForm.type !== 'judgement' && questionForm.options.length > 2"
              />
            </div>
            <el-button
              type="primary"
              size="small"
              icon="Plus"
              @click="addOption"
              v-if="questionForm.type !== 'judgement' && questionForm.options.length < 10"
            >
              添加选项
            </el-button>
          </el-form-item>
          <el-form-item label="正确答案" prop="answer">
            <el-select
              v-model="questionForm.answer"
              placeholder="请选择正确答案"
              multiple
              v-if="questionForm.type === 'multiple_choice'"
            >
              <el-option
                v-for="(option, index) in questionForm.options"
                :key="index"
                :label="String.fromCharCode(65 + index)"
                :value="index"
                :disabled="!option"
              />
            </el-select>
            <el-select
              v-model="questionForm.answer"
              placeholder="请选择正确答案"
              v-else-if="questionForm.type !== 'short_answer'"
            >
              <el-option
                v-for="(option, index) in questionForm.options"
                :key="index"
                :label="String.fromCharCode(65 + index)"
                :value="index"
                :disabled="!option"
              />
            </el-select>
            <el-input
              v-model="questionForm.answer"
              type="textarea"
              :rows="3"
              placeholder="请输入正确答案"
              v-else
            />
          </el-form-item>
          <el-form-item label="难度" prop="difficulty">
            <el-select v-model="questionForm.difficulty" placeholder="请选择难度">
              <el-option label="简单" value="easy" />
              <el-option label="中等" value="medium" />
              <el-option label="困难" value="hard" />
            </el-select>
          </el-form-item>
          <el-form-item label="分值" prop="score">
            <el-input-number v-model="questionForm.score" :min="1" :max="100" />
          </el-form-item>
          <el-form-item label="分类" prop="category">
            <el-input v-model="questionForm.category" placeholder="请输入分类" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit">确定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import AdminLayout from '@/components/AdminLayout.vue'
import { Plus, Edit, Delete, Search } from '@element-plus/icons-vue'

// 路由
const router = useRouter()

// 状态
const loading = ref(false)
const dialogVisible = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const searchKey = ref('')
const filterType = ref('')
const filterDifficulty = ref('')
const questionFormRef = ref(null)

// 题目数据
const questions = ref([])

// 虚拟滚动相关状态
const containerRef = ref(null)
const containerWidth = ref(0)
const scrollLeft = ref(0)
const itemWidth = ref(1000) // 预估每个表格项的宽度
const visibleCount = ref(10) // 可视区域显示的项目数量
const bufferSize = ref(5) // 缓冲区大小
const totalItemsWidth = ref(0)

// 模拟数据
const mockQuestions = Array.from({ length: 50 }, (_, index) => {
  const type = ['single_choice', 'multiple_choice', 'judgement', 'short_answer'][Math.floor(Math.random() * 4)];
  let answer;
  
  // 根据题型生成不同格式的答案
  if (type === 'multiple_choice') {
    // 多选题：生成1-3个正确选项的数组
    const answerCount = Math.floor(Math.random() * 3) + 1;
    answer = [];
    for (let i = 0; i < answerCount; i++) {
      const option = Math.floor(Math.random() * 4);
      if (!answer.includes(option)) {
        answer.push(option);
      }
    }
  } else if (type === 'short_answer') {
    // 简答题：生成一段文字
    answer = `这是第${index + 1}道简答题的参考答案，内容详细解释了题目涉及的知识点和解答思路。`;
  } else {
    // 单选题和判断题：生成单个数字
    answer = Math.floor(Math.random() * (type === 'judgement' ? 2 : 4));
  }
  
  return {
    id: index + 1,
    type,
    content: `这是第${index + 1}道测试题目，内容较长以测试自适应水平滑动窗口的效果。这道题目包含多个知识点，需要考生仔细阅读并理解题意。题目内容可能会涉及各种题型的测试，包括单选题、多选题、判断题和简答题等。`,
    options: [
      '选项A：这是第一个选项，内容可能比较长',
      '选项B：这是第二个选项，内容也可能比较长',
      '选项C：这是第三个选项，内容同样可能比较长',
      '选项D：这是第四个选项，内容可能会更长一些，用于测试表格的水平滚动效果'
    ],
    answer,
    difficulty: ['easy', 'medium', 'hard'][Math.floor(Math.random() * 3)],
    score: Math.floor(Math.random() * 10) + 1,
    category: `分类${Math.floor(Math.random() * 5) + 1}`,
    createdTime: `2023-12-${Math.floor(Math.random() * 28) + 1} ${Math.floor(Math.random() * 24)}:${Math.floor(Math.random() * 60)}:${Math.floor(Math.random() * 60)}`
  };
})

// 表单数据
const questionForm = ref({
  id: null,
  type: 'single_choice',
  content: '',
  options: ['', ''],
  answer: null,
  difficulty: 'medium',
  score: 5,
  category: ''
})

// 表单规则
const questionRules = {
  type: [{ required: true, message: '请选择题型', trigger: 'change' }],
  content: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
  options: [
    {
      validator: (rule, value, callback) => {
        if (questionForm.value.type !== 'short_answer') {
          const validOptions = value.filter(option => option.trim())
          if (validOptions.length < 2) {
            callback(new Error('至少需要两个有效选项'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  answer: [{ required: true, message: '请设置正确答案', trigger: 'change' }],
  difficulty: [{ required: true, message: '请选择难度', trigger: 'change' }],
  score: [{ required: true, message: '请设置分值', trigger: 'change' }],
  category: [{ required: true, message: '请输入分类', trigger: 'blur' }]
}

// 计算属性
const filteredQuestions = computed(() => {
  return questions.value.filter(question => {
    const matchesSearch = question.content.toLowerCase().includes(searchKey.value.toLowerCase())
    const matchesType = !filterType.value || question.type === filterType.value
    const matchesDifficulty = !filterDifficulty.value || question.difficulty === filterDifficulty.value
    return matchesSearch && matchesType && matchesDifficulty
  })
})

// 虚拟滚动计算属性
const visibleStartIndex = computed(() => {
  return Math.max(0, Math.floor(scrollLeft.value / itemWidth.value) - bufferSize.value)
})

const visibleEndIndex = computed(() => {
  return Math.min(
    filteredQuestions.value.length,
    Math.ceil((scrollLeft.value + containerWidth.value) / itemWidth.value) + bufferSize.value
  )
})

const visibleQuestions = computed(() => {
  return filteredQuestions.value.slice(visibleStartIndex.value, visibleEndIndex.value)
})

const offsetLeft = computed(() => {
  return visibleStartIndex.value * itemWidth.value
})

const dialogTitle = computed(() => {
  return questionForm.value.id ? '编辑题目' : '新建题目'
})

// 方法
const loadQuestions = async () => {
  try {
    loading.value = true
    // 实际项目中调用API获取数据
    // const response = await questionApi.getQuestions()
    // questions.value = response.data
    
    // 使用模拟数据
    questions.value = mockQuestions
    await nextTick()
    updateContainerWidth()
  } catch (error) {
    console.error('Failed to load questions:', error)
    ElMessage.error('加载题目列表失败')
  } finally {
    loading.value = false
  }
}

// 更新容器宽度
const updateContainerWidth = () => {
  if (containerRef.value) {
    containerWidth.value = containerRef.value.clientWidth
    // 估算总宽度
    totalItemsWidth.value = filteredQuestions.value.length * itemWidth.value
  }
}

// 处理滚动事件
const handleScroll = (event) => {
  scrollLeft.value = event.target.scrollLeft
}

// 处理窗口大小变化
const handleResize = () => {
  updateContainerWidth()
}

const getQuestionTypeTagType = (type) => {
  const typeMap = {
    single_choice: 'primary',
    multiple_choice: 'success',
    judgement: 'warning',
    short_answer: 'info'
  }
  return typeMap[type] || 'info'
}

const getQuestionTypeName = (type) => {
  const typeMap = {
    single_choice: '单选题',
    multiple_choice: '多选题',
    judgement: '判断题',
    short_answer: '简答题'
  }
  return typeMap[type] || type
}

const getDifficultyTagType = (difficulty) => {
  const difficultyMap = {
    easy: 'success',
    medium: 'warning',
    hard: 'danger'
  }
  return difficultyMap[difficulty] || 'info'
}

const getDifficultyName = (difficulty) => {
  const difficultyMap = {
    easy: '简单',
    medium: '中等',
    hard: '困难'
  }
  return difficultyMap[difficulty] || difficulty
}

const handleSearch = () => {
  currentPage.value = 1
}

const handleReset = () => {
  searchKey.value = ''
  filterType.value = ''
  filterDifficulty.value = ''
  currentPage.value = 1
}

const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

const createQuestion = () => {
  resetForm()
  dialogVisible.value = true
}

const editQuestion = (question) => {
  // 深拷贝
  questionForm.value = JSON.parse(JSON.stringify(question))
  dialogVisible.value = true
}

const deleteQuestion = (question) => {
  ElMessageBox.confirm(`确定要删除题目ID为${question.id}的题目吗？`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 实际项目中调用API删除
    questions.value = questions.value.filter(q => q.id !== question.id)
    ElMessage.success('删除成功')
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

const resetForm = () => {
  questionForm.value = {
    id: null,
    type: 'single_choice',
    content: '',
    options: ['', ''],
    answer: null,
    difficulty: 'medium',
    score: 5,
    category: ''
  }
  if (questionFormRef.value) {
    questionFormRef.value.resetFields()
  }
}

const addOption = () => {
  questionForm.value.options.push('')
}

const removeOption = (index) => {
  questionForm.value.options.splice(index, 1)
  // 如果删除的是当前选中的答案，重置答案
  if (questionForm.value.type === 'single_choice' && questionForm.value.answer === index) {
    questionForm.value.answer = null
  } else if (questionForm.value.type === 'multiple_choice') {
    questionForm.value.answer = questionForm.value.answer.filter(ans => ans !== index)
    // 调整答案索引
    questionForm.value.answer = questionForm.value.answer.map(ans => ans > index ? ans - 1 : ans)
  }
}

const handleSubmit = async () => {
  if (!questionFormRef.value) return
  try {
    await questionFormRef.value.validate()
    if (questionForm.value.id) {
      // 编辑
      const index = questions.value.findIndex(q => q.id === questionForm.value.id)
      if (index !== -1) {
        questions.value[index] = { ...questionForm.value }
        ElMessage.success('编辑成功')
      }
    } else {
      // 新建
      const newQuestion = {
        ...questionForm.value,
        id: questions.value.length + 1,
        createdTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
      }
      questions.value.unshift(newQuestion)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
  } catch (error) {
    console.error('Validation failed:', error)
  }
}

// 监听题型变化
watch(
  () => questionForm.value.type,
  (newType) => {
    if (newType === 'judgement') {
      questionForm.value.options = ['正确', '错误']
      questionForm.value.answer = 0
    } else if (newType === 'short_answer') {
      questionForm.value.options = []
      questionForm.value.answer = ''
    } else {
      if (questionForm.value.options.length < 2) {
        questionForm.value.options = ['', '']
      }
      questionForm.value.answer = questionForm.value.type === 'multiple_choice' ? [] : 0
    }
  }
)

// 页面加载
onMounted(() => {
  loadQuestions()
  updateContainerWidth()
  window.addEventListener('resize', handleResize)
})

// 页面卸载
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.question-management {
  padding: 24px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

/* 面包屑样式 */
.breadcrumb {
  margin-bottom: 16px;
}

/* 搜索筛选样式 */
.search-filter {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-input {
  width: 300px;
}

.filter-select {
  width: 150px;
}

/* 自适应+虚拟滚动水平滑动窗口 */
.adaptive-table-container {
  width: 100%;
  overflow-x: auto;
  margin-bottom: 16px;
  position: relative;
  /* 滚动优化 */
  scroll-behavior: smooth;
  -webkit-overflow-scrolling: touch;
  height: auto;
}

/* 虚拟滚动容器 */
.virtual-scroll-wrapper {
  position: relative;
}

/* 可视区域 */
.virtual-scroll-viewport {
  position: relative;
  transition: transform 0s;
}

/* 美化滚动条 */
.adaptive-table-container::-webkit-scrollbar {
  height: 8px;
}

.adaptive-table-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.adaptive-table-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.adaptive-table-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 表格样式 */
:deep(.el-table) {
  min-width: 1200px; /* 确保在小屏幕上也能显示水平滚动条 */
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table__header-wrapper) {
  position: sticky;
  top: 0;
  z-index: 10;
}

:deep(.el-table__header tr th) {
  white-space: nowrap;
  padding: 12px 10px;
  line-height: normal;
  height: auto;
  border-bottom: 2px solid #e9ecef;
}

:deep(.el-table__body tr td) {
  white-space: nowrap;
  padding: 12px 10px;
}

/* 题目内容样式 */
.question-content {
  word-break: break-word;
  white-space: normal;
  line-height: 1.5;
}

/* 选项样式 */
.question-options {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.option-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
}

.option-label {
  font-weight: 500;
  color: #409eff;
  min-width: 20px;
}

/* 表单样式 */
.option-input-group {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.option-letter {
  font-weight: 500;
  color: #409eff;
  min-width: 20px;
  text-align: center;
}

/* 分页样式 */
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>