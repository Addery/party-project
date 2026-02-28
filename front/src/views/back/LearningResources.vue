<template>
  <AdminLayout>
    <div class="learning-resources">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item><a href="#/back/dashboard">首页</a></el-breadcrumb-item>
          <el-breadcrumb-item>党员教育与思政融合</el-breadcrumb-item>
          <el-breadcrumb-item>学习资源库</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 页面标题区域 -->
      <div class="page-header">
        <h2>学习资源管理</h2>
        <el-button type="primary" @click="showAddResourceDialog" class="add-btn">
          <el-icon><Plus /></el-icon> 添加资源
        </el-button>
      </div>

      <!-- 搜索和筛选区域 -->
      <div class="filter-section">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-input
              v-model="searchQuery"
              placeholder="搜索资源名称或关键词"
              clearable
              class="search-input"
            >
              <template #append>
                <el-button icon="el-icon-search" @click="handleSearch"></el-button>
              </template>
            </el-input>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-select
              v-model="filterType"
              placeholder="筛选资源类型"
              clearable
              class="filter-select"
            >
              <el-option label="全部类型" value=""></el-option>
              <el-option label="文章" value="article"></el-option>
              <el-option label="视频" value="video"></el-option>
              <el-option label="音频" value="audio"></el-option>
              <el-option label="文档" value="document"></el-option>
            </el-select>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-select
              v-model="filterCategory"
              placeholder="筛选分类"
              clearable
              class="filter-select"
            >
              <el-option label="全部分类" value=""></el-option>
              <el-option label="理论学习" value="theory"></el-option>
              <el-option label="党史教育" value="history"></el-option>
              <el-option label="政策法规" value="policy"></el-option>
              <el-option label="先进典型" value="model"></el-option>
              <el-option label="廉政教育" value="integrity"></el-option>
            </el-select>
          </el-col>
        </el-row>
      </div>

      <!-- 学习资源列表 -->
      <div class="resources-table">
        <el-table
          :data="paginatedResources"
          stripe
          border
          style="width: 100%"
          @row-dblclick="handleRowDoubleClick"
        >
          <el-table-column type="selection" width="65" />
          <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
          <el-table-column prop="title" label="资源标题" min-width="200"></el-table-column>
          <el-table-column prop="type" label="资源类型" width="100" align="center">
            <template #default="scope">
              <el-tag :type="getResourceTypeTagType(scope.row.type)">
                {{ getResourceTypeName(scope.row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="category" label="分类" width="120" align="center">
            <template #default="scope">
              <el-tag>{{ getResourceCategoryName(scope.row.category) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="author" label="作者/来源" width="120"></el-table-column>
          <el-table-column prop="uploadDate" label="上传日期" width="150" align="center"></el-table-column>
          <el-table-column prop="views" label="浏览量" width="100" align="center"></el-table-column>
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="published"
                inactive-value="draft"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250" align="center">
            <template #default="scope">
              <el-space size="small">
                <el-button size="small" @click="showResourceDetail(scope.row)">
                  <el-icon><View /></el-icon> 查看
                </el-button>
                <el-button size="small" type="primary" @click="showEditResourceDialog(scope.row)">
                  <el-icon><Edit /></el-icon> 编辑
                </el-button>
                <el-button size="small" type="danger" @click="handleDeleteResource(scope.row)">
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
          :total="filteredResources.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>

      <!-- 添加/编辑资源对话框 -->
      <el-dialog
        v-model="resourceDialogVisible"
        :title="isEditMode ? '编辑资源' : '添加资源'"
        width="800px"
      >
        <el-form :model="currentResource" label-width="100px">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="资源标题" required>
                <el-input v-model="currentResource.title" placeholder="请输入资源标题"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="资源类型" required>
                <el-select v-model="currentResource.type" placeholder="请选择资源类型">
                  <el-option label="文章" value="article"></el-option>
                  <el-option label="视频" value="video"></el-option>
                  <el-option label="音频" value="audio"></el-option>
                  <el-option label="文档" value="document"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="分类" required>
                <el-select v-model="currentResource.category" placeholder="请选择分类">
                  <el-option label="理论学习" value="theory"></el-option>
                  <el-option label="党史教育" value="history"></el-option>
                  <el-option label="政策法规" value="policy"></el-option>
                  <el-option label="先进典型" value="model"></el-option>
                  <el-option label="廉政教育" value="integrity"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="作者/来源">
                <el-input v-model="currentResource.author" placeholder="请输入作者或来源"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="上传日期">
                <el-date-picker
                  v-model="currentResource.uploadDate"
                  type="date"
                  placeholder="选择上传日期"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="资源链接">
                <el-input v-model="currentResource.url" placeholder="请输入资源链接"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="简介">
                <el-input
                  v-model="currentResource.description"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入资源简介"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="内容">
                <el-input
                  v-model="currentResource.content"
                  type="textarea"
                  :rows="8"
                  placeholder="请输入资源内容（文章类型）"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态">
                <el-switch
                  v-model="currentResource.status"
                  active-value="published"
                  inactive-value="draft"
                ></el-switch>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="resourceDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSaveResource">保存</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 资源详情对话框 -->
      <el-dialog
        v-model="resourceDetailVisible"
        title="资源详情"
        width="800px"
      >
        <div v-if="currentResource" class="resource-detail">
          <h3>{{ currentResource.title }}</h3>
          <div class="detail-meta">
            <span><strong>类型：</strong>{{ getResourceTypeName(currentResource.type) }}</span>
            <span><strong>分类：</strong>{{ getResourceCategoryName(currentResource.category) }}</span>
            <span><strong>作者/来源：</strong>{{ currentResource.author }}</span>
            <span><strong>上传日期：</strong>{{ currentResource.uploadDate }}</span>
            <span><strong>浏览量：</strong>{{ currentResource.views }}</span>
            <span><strong>状态：</strong>{{ currentResource.status === 'published' ? '已发布' : '草稿' }}</span>
          </div>
          <div class="detail-description">
            <strong>简介：</strong>{{ currentResource.description }}
          </div>
          <div v-if="currentResource.content" class="detail-content">
            <strong>内容：</strong>
            <div v-html="currentResource.content"></div>
          </div>
          <div v-if="currentResource.url" class="detail-url">
            <strong>资源链接：</strong>
            <a :href="currentResource.url" target="_blank">{{ currentResource.url }}</a>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="resourceDetailVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import AdminLayout from '@/components/AdminLayout.vue'
import { View, Edit, Delete, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 状态管理
const searchQuery = ref('')
const filterType = ref('')
const filterCategory = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const resourceDialogVisible = ref(false)
const resourceDetailVisible = ref(false)
const isEditMode = ref(false)
const currentResource = ref({})

// 模拟学习资源数据
const resources = ref([
  {
    id: 1,
    title: "习近平新时代中国特色社会主义思想学习纲要",
    type: "article",
    category: "theory",
    author: "中共中央宣传部",
    uploadDate: "2023-09-15",
    views: 1258,
    status: "published",
    url: "#",
    description: "深入学习习近平新时代中国特色社会主义思想的重要辅导读物",
    content: "习近平新时代中国特色社会主义思想是当代中国马克思主义、二十一世纪马克思主义，是中华文化和中国精神的时代精华，实现了马克思主义中国化时代化新的飞跃。"
  },
  {
    id: 2,
    title: "中国共产党百年奋斗历程",
    type: "video",
    category: "history",
    author: "中央党校",
    uploadDate: "2023-08-20",
    views: 892,
    status: "published",
    url: "#",
    description: "全面回顾中国共产党百年奋斗的光辉历程和历史贡献"
  },
  {
    id: 3,
    title: "中国共产党章程（2022年修订）",
    type: "document",
    category: "policy",
    author: "中共中央",
    uploadDate: "2023-07-10",
    views: 1567,
    status: "published",
    url: "#",
    description: "2022年党的二十大修订通过的中国共产党章程"
  },
  {
    id: 4,
    title: "榜样的力量：张桂梅先进事迹报告",
    type: "audio",
    category: "model",
    author: "中央广播电视总台",
    uploadDate: "2023-06-05",
    views: 634,
    status: "published",
    url: "#",
    description: "时代楷模张桂梅的先进事迹报告"
  },
  {
    id: 5,
    title: "廉政教育警示片：零容忍",
    type: "video",
    category: "integrity",
    author: "中央纪委国家监委",
    uploadDate: "2023-05-18",
    views: 987,
    status: "published",
    url: "#",
    description: "全面从严治党专题片，展现反腐败斗争的成果"
  },
  {
    id: 6,
    title: "党的二十大报告全文学习",
    type: "article",
    category: "theory",
    author: "新华社",
    uploadDate: "2023-04-25",
    views: 2341,
    status: "published",
    url: "#",
    description: "中国共产党第二十次全国代表大会报告全文"
  },
  {
    id: 7,
    title: "中国共产党简史",
    type: "document",
    category: "history",
    author: "中共中央党史研究室",
    uploadDate: "2023-03-12",
    views: 1876,
    status: "published",
    url: "#",
    description: "简明扼要地叙述了中国共产党领导中国人民进行革命、建设和改革的光辉历程"
  },
  {
    id: 8,
    title: "党员权利保障条例解读",
    type: "article",
    category: "policy",
    author: "中央党校党建部",
    uploadDate: "2023-02-20",
    views: 756,
    status: "published",
    url: "#",
    description: "深入解读《中国共产党党员权利保障条例》的主要内容和精神实质"
  },
  {
    id: 9,
    title: "最美公务员先进事迹展播",
    type: "video",
    category: "model",
    author: "中央组织部",
    uploadDate: "2023-01-15",
    views: 543,
    status: "published",
    url: "#",
    description: "展示新时代公务员队伍的精神风貌和先进事迹"
  },
  {
    id: 10,
    title: "新时代党的建设总要求",
    type: "article",
    category: "theory",
    author: "《求是》杂志社",
    uploadDate: "2022-12-10",
    views: 1098,
    status: "published",
    url: "#",
    description: "深入阐释新时代党的建设总要求的内涵和意义"
  },
  {
    id: 11,
    title: "党史学习教育专题讲座",
    type: "audio",
    category: "history",
    author: "中央党校党史部",
    uploadDate: "2022-11-05",
    views: 432,
    status: "draft",
    url: "#",
    description: "党史学习教育专题讲座系列音频"
  },
  {
    id: 12,
    title: "廉洁自律准则解读",
    type: "article",
    category: "integrity",
    author: "中央纪委国家监委法规室",
    uploadDate: "2022-10-18",
    views: 678,
    status: "published",
    url: "#",
    description: "解读《中国共产党廉洁自律准则》的主要内容和要求"
  }
])

// 过滤后的资源列表
const filteredResources = computed(() => {
  return resources.value.filter(resource => {
    const matchesSearch = resource.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                         resource.description.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesType = !filterType.value || resource.type === filterType.value
    const matchesCategory = !filterCategory.value || resource.category === filterCategory.value
    return matchesSearch && matchesType && matchesCategory
  })
})

// 分页后的资源列表
const paginatedResources = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredResources.value.slice(start, end)
})

// 资源类型名称映射
const getResourceTypeName = (type) => {
  const typeMap = {
    article: '文章',
    video: '视频',
    audio: '音频',
    document: '文档'
  }
  return typeMap[type] || type
}

// 资源类型标签样式映射
const getResourceTypeTagType = (type) => {
  const typeMap = {
    article: 'primary',
    video: 'success',
    audio: 'warning',
    document: 'info'
  }
  return typeMap[type] || 'default'
}

// 资源分类名称映射
const getResourceCategoryName = (category) => {
  const categoryMap = {
    theory: '理论学习',
    history: '党史教育',
    policy: '政策法规',
    model: '先进典型',
    integrity: '廉政教育'
  }
  return categoryMap[category] || category
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
}

// 分页大小变化处理
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

// 页码变化处理
const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 显示添加资源对话框
const showAddResourceDialog = () => {
  isEditMode.value = false
  currentResource.value = {
    title: '',
    type: 'article',
    category: 'theory',
    author: '',
    uploadDate: new Date().toISOString().split('T')[0],
    views: 0,
    status: 'draft',
    url: '',
    description: '',
    content: ''
  }
  resourceDialogVisible.value = true
}

// 显示编辑资源对话框
const showEditResourceDialog = (resource) => {
  isEditMode.value = true
  currentResource.value = JSON.parse(JSON.stringify(resource))
  resourceDialogVisible.value = true
}

// 显示资源详情
const showResourceDetail = (resource) => {
  currentResource.value = JSON.parse(JSON.stringify(resource))
  // 模拟增加浏览量
  resource.views++
  resourceDetailVisible.value = true
}

// 保存资源
const handleSaveResource = () => {
  if (isEditMode.value) {
    // 编辑模式
    const index = resources.value.findIndex(r => r.id === currentResource.value.id)
    if (index !== -1) {
      resources.value[index] = JSON.parse(JSON.stringify(currentResource.value))
    }
  } else {
    // 添加模式
    const newId = Math.max(...resources.value.map(r => r.id)) + 1
    currentResource.value.id = newId
    resources.value.unshift(JSON.parse(JSON.stringify(currentResource.value)))
  }
  resourceDialogVisible.value = false
  ElMessage.success(isEditMode.value ? '资源编辑成功' : '资源添加成功')
}

// 删除资源
const handleDeleteResource = (resource) => {
  ElMessageBox.confirm('确定要删除该资源吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = resources.value.findIndex(r => r.id === resource.id)
    if (index !== -1) {
      resources.value.splice(index, 1)
      ElMessage.success('资源删除成功')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 状态变化处理
const handleStatusChange = (resource) => {
  ElMessage.success('资源状态已更新')
}

// 行双击事件
const handleRowDoubleClick = (row) => {
  showResourceDetail(row)
}

</script>

<style scoped>
/* 页面容器 */
.learning-resources {
  margin: 0 auto;
  padding: 24px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

/* 面包屑导航 */
.breadcrumb {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

/* 页面标题区域 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 15px;
}

.page-header h2 {
  margin: 0;
  color: #212529;
  font-size: 24px;
  font-weight: 600;
}

/* 主要按钮样式 */
.page-header .el-button {
  background-color: #c72c41;
  border-color: #c72c41;
}

.page-header .el-button:hover {
  background-color: #a82336;
  border-color: #a82336;
}

/* 搜索筛选区域 */
.filter-section {
  margin-bottom: 24px;
  padding: 20px;
  background-color: #ffffff;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

.filter-section .el-row {
  width: 100%;
}

.filter-section .el-col {
  margin-bottom: 15px;
}

.search-input,
.filter-select {
  width: 100%;
}

/* 资源列表表格 */
.resources-table {
  margin-bottom: 24px;
}

.resources-table :deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

.resources-table :deep(.el-table__header-wrapper th) {
  background-color: #f5f7fa;
  font-weight: 600;
}

/* 分页区域 */
.pagination {
  text-align: right;
  margin-top: 24px;
  padding: 16px 0;
}

/* 资源详情样式 */
.resource-detail {
  padding: 20px 0;
}

.resource-detail h3 {
  margin-bottom: 20px;
  color: #2c3e50;
  font-size: 20px;
  font-weight: 600;
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e9ecef;
}

.detail-meta span {
  color: #6c757d;
}

.detail-description,
.detail-content,
.detail-url {
  margin-bottom: 20px;
  line-height: 1.6;
}

.detail-url a {
  color: #409eff;
  text-decoration: none;
}

.detail-url a:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .learning-resources {
    padding: 20px 15px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .page-header h2 {
    font-size: 22px;
  }
  
  .filter-section {
    padding: 10px;
  }
  
  .filter-section .el-col {
    margin-bottom: 15px;
  }
  
  .detail-meta {
    flex-direction: column;
    gap: 10px;
  }
  
  .pagination {
    text-align: center;
  }
}
</style>