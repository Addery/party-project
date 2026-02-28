<template>
  <AdminLayout>
    <div class="news-management">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item><a href="#/back/dashboard">首页</a></el-breadcrumb-item>
          <el-breadcrumb-item>信息发布与宣传阵地</el-breadcrumb-item>
          <el-breadcrumb-item>新闻管理</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 页面标题 -->
      <h2 class="page-title">新闻管理</h2>

      <!-- 搜索和筛选区域 -->
      <div class="filter-section">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-input
              v-model="searchQuery"
              placeholder="搜索新闻标题或内容"
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
              v-model="filterCategory"
              placeholder="筛选新闻分类"
              clearable
              class="filter-select"
            >
              <el-option label="全部分类" value=""></el-option>
              <el-option label="头条新闻" value="headline"></el-option>
              <el-option label="党建动态" value="party_dynamic"></el-option>
              <el-option label="通知公告" value="notification"></el-option>
              <el-option label="先进典型" value="model"></el-option>
              <el-option label="学习资料" value="learning_material"></el-option>
            </el-select>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-select
              v-model="filterStatus"
              placeholder="筛选状态"
              clearable
              class="filter-select"
            >
              <el-option label="全部状态" value=""></el-option>
              <el-option label="草稿" value="draft"></el-option>
              <el-option label="已发布" value="published"></el-option>
              <el-option label="已下架" value="offline"></el-option>
            </el-select>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-button type="primary" @click="showAddNewsDialog" class="add-btn">
              <el-icon><Plus /></el-icon> 添加新闻
            </el-button>
            <el-button @click="batchPublish" class="batch-btn">批量发布</el-button>
            <el-button @click="batchDelete" class="batch-btn">批量删除</el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 新闻列表 -->
      <div class="table-container">
        <el-table
          :data="paginatedNews"
          border
          style="width: 100%"
          @selection-change="handleSelectionChange"
          @row-dblclick="handleRowDoubleClick"
        >
          <el-table-column type="selection" width="65" align="center"></el-table-column>
          <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
          <el-table-column prop="title" label="新闻标题" min-width="200">
            <template #default="scope">
              <div class="title-container">
                <el-tag v-if="scope.row.isTop" type="danger" size="small">置顶</el-tag>
                {{ scope.row.title }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="category" label="分类" width="120" align="center">
            <template #default="scope">
              <el-tag>{{ getCategoryName(scope.row.category) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="author" label="作者" width="120"></el-table-column>
          <el-table-column prop="source" label="来源" width="120"></el-table-column>
          <el-table-column prop="publishDate" label="发布日期" width="150" align="center"></el-table-column>
          <el-table-column prop="views" label="浏览量" width="100" align="center"></el-table-column>
          <el-table-column prop="status" label="状态" width="105" align="center">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ getStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="350" align="center" fixed="right">
            <template #default="scope">
              <el-space size="small">
                <el-button size="small" @click="showNewsDetail(scope.row)">
                  <el-icon><View /></el-icon> 查看
                </el-button>
                <el-button type="primary" size="small" @click="showEditNewsDialog(scope.row)">
                  <el-icon><Edit /></el-icon> 编辑
                </el-button>
                <el-button
                  :type="scope.row.status === 'published' ? 'warning' : 'success'"
                  size="small"
                  @click="handleStatusChange(scope.row)"
                >
                  <el-icon><Document /></el-icon> {{ scope.row.status === 'published' ? '下架' : '发布' }}
                </el-button>
                <el-button type="danger" size="small" @click="handleDeleteNews(scope.row)">
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
          :total="filteredNews.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>

      <!-- 添加/编辑新闻对话框 -->
      <el-dialog
        v-model="newsDialogVisible"
        :title="isEditMode ? '编辑新闻' : '添加新闻'"
        width="800px"
        :before-close="handleDialogClose"
      >
        <el-form :model="currentNews" label-width="100px" :rules="newsRules" ref="newsFormRef">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="新闻标题" prop="title">
                <el-input v-model="currentNews.title" placeholder="请输入新闻标题"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="新闻分类" prop="category">
                <el-select v-model="currentNews.category" placeholder="请选择新闻分类">
                  <el-option label="头条新闻" value="headline"></el-option>
                  <el-option label="党建动态" value="party_dynamic"></el-option>
                  <el-option label="通知公告" value="notification"></el-option>
                  <el-option label="先进典型" value="model"></el-option>
                  <el-option label="学习资料" value="learning_material"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="作者">
                <el-input v-model="currentNews.author" placeholder="请输入作者名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="来源">
                <el-input v-model="currentNews.source" placeholder="请输入新闻来源"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发布日期">
                <el-date-picker
                  v-model="currentNews.publishDate"
                  type="datetime"
                  placeholder="选择发布日期"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="新闻摘要" prop="summary">
                <el-input
                  v-model="currentNews.summary"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入新闻摘要"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="新闻内容" prop="content">
                <el-input
                  v-model="currentNews.content"
                  type="textarea"
                  :rows="10"
                  placeholder="请输入新闻内容"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="新闻封面">
                <el-upload
                  class="avatar-uploader"
                  action="#"
                  :show-file-list="false"
                  :on-success="handleCoverUpload"
                  :before-upload="beforeCoverUpload"
                >
                  <img v-if="currentNews.cover" :src="currentNews.cover" class="avatar" />
                  <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否置顶">
                <el-switch v-model="currentNews.isTop"></el-switch>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态">
                <el-select v-model="currentNews.status">
                  <el-option label="草稿" value="draft"></el-option>
                  <el-option label="已发布" value="published"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="handleDialogClose">取消</el-button>
            <el-button type="primary" @click="handleSaveNews">保存</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 新闻详情对话框 -->
      <el-dialog
        v-model="newsDetailVisible"
        title="新闻详情"
        width="800px"
      >
        <div v-if="currentNews" class="news-detail">
          <h3 class="detail-title">{{ currentNews.title }}</h3>
          <div class="detail-meta">
            <span><strong>分类：</strong>{{ getCategoryName(currentNews.category) }}</span>
            <span><strong>作者：</strong>{{ currentNews.author }}</span>
            <span><strong>来源：</strong>{{ currentNews.source }}</span>
            <span><strong>发布日期：</strong>{{ currentNews.publishDate }}</span>
            <span><strong>浏览量：</strong>{{ currentNews.views }}</span>
            <span><strong>状态：</strong>{{ getStatusName(currentNews.status) }}</span>
            <el-tag v-if="currentNews.isTop" type="danger" size="small">置顶</el-tag>
          </div>
          <div v-if="currentNews.cover" class="detail-cover">
            <img :src="currentNews.cover" alt="新闻封面" />
          </div>
          <div class="detail-summary">
            <strong>摘要：</strong>{{ currentNews.summary }}
          </div>
          <div class="detail-content">
            <strong>内容：</strong>
            <div v-html="currentNews.content"></div>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="newsDetailVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import AdminLayout from '@/components/AdminLayout.vue'
import { ElMessage, ElMessageBox, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElDatePicker, ElUpload, ElIcon } from 'element-plus'
import { Plus, View, Edit, Document, Delete } from '@element-plus/icons-vue'

// 状态管理
const searchQuery = ref('')
const filterCategory = ref('')
const filterStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const newsDialogVisible = ref(false)
const newsDetailVisible = ref(false)
const isEditMode = ref(false)
const currentNews = ref({})
const selectedNews = ref([])
const newsFormRef = ref(null)

// 新闻表单验证规则
const newsRules = {
  title: [{ required: true, message: '请输入新闻标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择新闻分类', trigger: 'change' }],
  summary: [{ required: true, message: '请输入新闻摘要', trigger: 'blur' }],
  content: [{ required: true, message: '请输入新闻内容', trigger: 'blur' }]
}

// 模拟新闻数据
const newsList = ref([
  {
    id: 1,
    title: "习近平总书记在全国组织工作会议上发表重要讲话",
    category: "headline",
    author: "新华社",
    source: "新华网",
    publishDate: "2023-09-20 09:00:00",
    summary: "习近平总书记在全国组织工作会议上强调，要坚持和加强党的全面领导，坚持党要管党、全面从严治党，坚持德才兼备、以德为先、五湖四海、任人唯贤，坚持事业为上、公道正派，坚持面向基层、服务群众，坚持解放思想、改革创新。",
    content: "习近平总书记在全国组织工作会议上发表重要讲话，强调要坚持和加强党的全面领导，坚持党要管党、全面从严治党，坚持德才兼备、以德为先、五湖四海、任人唯贤，坚持事业为上、公道正派，坚持面向基层、服务群众，坚持解放思想、改革创新，为新时代党的组织路线指明了方向。",
    cover: "https://via.placeholder.com/800x400",
    views: 5678,
    status: "published",
    isTop: true
  },
  {
    id: 2,
    title: "学校党委召开2023年秋季学期党建工作会议",
    category: "party_dynamic",
    author: "党委办公室",
    source: "学校官网",
    publishDate: "2023-09-15 14:30:00",
    summary: "学校党委召开2023年秋季学期党建工作会议，总结上半年党建工作，部署下半年党建任务。",
    content: "9月15日，学校党委召开2023年秋季学期党建工作会议，全体校领导、中层干部参加会议。会议总结了上半年党建工作，部署了下半年党建任务，强调要深入学习贯彻习近平新时代中国特色社会主义思想，全面加强党的建设，为学校高质量发展提供坚强政治保障。",
    cover: "https://via.placeholder.com/800x400",
    views: 1234,
    status: "published",
    isTop: false
  },
  {
    id: 3,
    title: "关于开展2023年党员民主评议工作的通知",
    category: "notification",
    author: "组织部",
    source: "学校官网",
    publishDate: "2023-09-10 10:00:00",
    summary: "为进一步加强党员队伍建设，提高党员素质，学校党委决定开展2023年党员民主评议工作。",
    content: "各党支部：为进一步加强党员队伍建设，提高党员素质，根据上级党组织要求和学校党委工作安排，决定开展2023年党员民主评议工作。现将有关事项通知如下：一、评议时间；二、评议内容；三、评议步骤；四、有关要求。",
    cover: "",
    views: 890,
    status: "published",
    isTop: false
  },
  {
    id: 4,
    title: "我校教师获全国优秀共产党员称号",
    category: "model",
    author: "宣传部",
    source: "学校官网",
    publishDate: "2023-09-05 16:00:00",
    summary: "在全国优秀共产党员表彰大会上，我校教师张明同志获得全国优秀共产党员称号。",
    content: "在日前召开的全国优秀共产党员表彰大会上，我校计算机学院教师张明同志获得全国优秀共产党员称号。张明同志在教学科研一线默默奉献，为学校发展做出了突出贡献。",
    cover: "https://via.placeholder.com/800x400",
    views: 2345,
    status: "published",
    isTop: false
  },
  {
    id: 5,
    title: "习近平新时代中国特色社会主义思想学习资料汇编",
    category: "learning_material",
    author: "党委宣传部",
    source: "学校党委",
    publishDate: "2023-09-01 09:00:00",
    summary: "为方便广大党员干部学习习近平新时代中国特色社会主义思想，学校党委汇编了相关学习资料。",
    content: "为深入学习贯彻习近平新时代中国特色社会主义思想，学校党委宣传部汇编了《习近平新时代中国特色社会主义思想学习资料汇编》，供广大党员干部学习参考。",
    cover: "https://via.placeholder.com/800x400",
    views: 1678,
    status: "published",
    isTop: false
  },
  {
    id: 6,
    title: "学校召开全面从严治党工作会议",
    category: "party_dynamic",
    author: "纪委办公室",
    source: "学校官网",
    publishDate: "2023-08-25 14:00:00",
    summary: "学校召开全面从严治党工作会议，部署2023年全面从严治党工作任务。",
    content: "8月25日，学校召开全面从严治党工作会议，全体校领导、中层干部、党支部书记参加会议。会议传达了上级纪委全会精神，总结了学校2022年全面从严治党工作，部署了2023年工作任务。",
    cover: "https://via.placeholder.com/800x400",
    views: 987,
    status: "published",
    isTop: false
  },
  {
    id: 7,
    title: "关于做好2023年秋季学期入党积极分子培训工作的通知",
    category: "notification",
    author: "组织部",
    source: "学校官网",
    publishDate: "2023-08-20 10:30:00",
    summary: "为进一步加强入党积极分子培养教育，学校党委决定举办2023年秋季学期入党积极分子培训班。",
    content: "各党支部：为进一步加强入党积极分子培养教育，提高入党积极分子的政治素质和理论水平，学校党委决定举办2023年秋季学期入党积极分子培训班。现将有关事项通知如下：一、培训对象；二、培训时间；三、培训内容；四、培训要求。",
    cover: "",
    views: 765,
    status: "published",
    isTop: false
  },
  {
    id: 8,
    title: "我校学生在全国大学生党建知识竞赛中获佳绩",
    category: "model",
    author: "学工部",
    source: "学校官网",
    publishDate: "2023-08-15 15:00:00",
    summary: "在全国大学生党建知识竞赛中，我校学生代表队获得团体一等奖。",
    content: "在日前举办的全国大学生党建知识竞赛中，我校学生代表队经过激烈角逐，获得团体一等奖，充分展示了我校学生的理论水平和精神风貌。",
    cover: "https://via.placeholder.com/800x400",
    views: 1456,
    status: "published",
    isTop: false
  },
  {
    id: 9,
    title: "学习贯彻党的二十大精神专题辅导报告",
    category: "learning_material",
    author: "党委宣传部",
    source: "学校党委",
    publishDate: "2023-08-10 14:00:00",
    summary: "学校邀请专家作学习贯彻党的二十大精神专题辅导报告。",
    content: "为深入学习贯彻党的二十大精神，学校邀请省委党校教授作专题辅导报告，全体党员干部参加学习。报告从党的二十大的重大意义、主要内容和贯彻落实等方面进行了深入解读。",
    cover: "https://via.placeholder.com/800x400",
    views: 1890,
    status: "published",
    isTop: false
  },
  {
    id: 10,
    title: "学校召开新学期工作会议",
    category: "party_dynamic",
    author: "校长办公室",
    source: "学校官网",
    publishDate: "2023-08-05 09:00:00",
    summary: "学校召开新学期工作会议，部署2023年秋季学期工作任务。",
    content: "8月5日，学校召开新学期工作会议，全体校领导、中层干部参加会议。会议总结了2023年春季学期工作，部署了秋季学期工作任务，强调要以习近平新时代中国特色社会主义思想为指导，全面贯彻党的二十大精神，推动学校各项事业高质量发展。",
    cover: "https://via.placeholder.com/800x400",
    views: 1234,
    status: "published",
    isTop: false
  },
  {
    id: 11,
    title: "关于开展2023年秋季学期主题党日活动的通知",
    category: "notification",
    author: "组织部",
    source: "学校官网",
    publishDate: "2023-08-01 10:00:00",
    summary: "为加强党的基层组织建设，丰富党员活动内容，学校党委决定开展2023年秋季学期主题党日活动。",
    content: "各党支部：为加强党的基层组织建设，丰富党员活动内容，增强党组织的凝聚力和战斗力，学校党委决定开展2023年秋季学期主题党日活动。现将有关事项通知如下：一、活动主题；二、活动时间；三、活动内容；四、有关要求。",
    cover: "",
    views: 654,
    status: "published",
    isTop: false
  },
  {
    id: 12,
    title: "我校新增两个国家级一流本科专业建设点",
    category: "party_dynamic",
    author: "教务处",
    source: "学校官网",
    publishDate: "2023-07-25 16:00:00",
    summary: "在教育部公布的2022年度国家级一流本科专业建设点名单中，我校计算机科学与技术、软件工程两个专业入选。",
    content: "日前，教育部公布了2022年度国家级一流本科专业建设点名单，我校计算机科学与技术、软件工程两个专业成功入选。截至目前，我校已有8个国家级一流本科专业建设点。",
    cover: "https://via.placeholder.com/800x400",
    views: 3456,
    status: "published",
    isTop: false
  }
])

// 过滤后的新闻列表
const filteredNews = computed(() => {
  return newsList.value.filter(news => {
    const matchesSearch = news.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                         news.content.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                         news.summary.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesCategory = !filterCategory.value || news.category === filterCategory.value
    const matchesStatus = !filterStatus.value || news.status === filterStatus.value
    return matchesSearch && matchesCategory && matchesStatus
  })
})

// 分页后的新闻列表
const paginatedNews = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredNews.value.slice(start, end)
})

// 新闻分类名称映射
const getCategoryName = (category) => {
  const categoryMap = {
    headline: '头条新闻',
    party_dynamic: '党建动态',
    notification: '通知公告',
    model: '先进典型',
    learning_material: '学习资料'
  }
  return categoryMap[category] || category
}

// 新闻状态名称映射
const getStatusName = (status) => {
  const statusMap = {
    draft: '草稿',
    published: '已发布',
    offline: '已下架'
  }
  return statusMap[status] || status
}

// 新闻状态标签样式映射
const getStatusTagType = (status) => {
  const typeMap = {
    draft: 'info',
    published: 'success',
    offline: 'warning'
  }
  return typeMap[status] || 'default'
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

// 显示添加新闻对话框
const showAddNewsDialog = () => {
  isEditMode.value = false
  currentNews.value = {
    title: '',
    category: 'party_dynamic',
    author: '',
    source: '',
    publishDate: new Date().toISOString().split('T')[0] + ' ' + new Date().toTimeString().split(' ')[0],
    summary: '',
    content: '',
    cover: '',
    views: 0,
    status: 'draft',
    isTop: false
  }
  newsDialogVisible.value = true
}

// 显示编辑新闻对话框
const showEditNewsDialog = (news) => {
  isEditMode.value = true
  currentNews.value = JSON.parse(JSON.stringify(news))
  newsDialogVisible.value = true
}

// 显示新闻详情
const showNewsDetail = (news) => {
  currentNews.value = JSON.parse(JSON.stringify(news))
  // 模拟增加浏览量
  news.views++
  newsDetailVisible.value = true
}

// 保存新闻
const handleSaveNews = () => {
  if (!newsFormRef.value) return
  newsFormRef.value.validate((valid) => {
    if (valid) {
      if (isEditMode.value) {
        // 编辑模式
        const index = newsList.value.findIndex(n => n.id === currentNews.value.id)
        if (index !== -1) {
          newsList.value[index] = JSON.parse(JSON.stringify(currentNews.value))
        }
      } else {
        // 添加模式
        const newId = Math.max(...newsList.value.map(n => n.id)) + 1
        currentNews.value.id = newId
        newsList.value.unshift(JSON.parse(JSON.stringify(currentNews.value)))
      }
      newsDialogVisible.value = false
      ElMessage.success(isEditMode.value ? '新闻编辑成功' : '新闻添加成功')
    } else {
      ElMessage.error('请填写必填字段')
      return false
    }
  })
}

// 删除新闻
const handleDeleteNews = (news) => {
  ElMessageBox.confirm('确定要删除该新闻吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = newsList.value.findIndex(n => n.id === news.id)
    if (index !== -1) {
      newsList.value.splice(index, 1)
      ElMessage.success('新闻删除成功')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 状态变化处理
const handleStatusChange = (news) => {
  const newStatus = news.status === 'published' ? 'offline' : 'published'
  news.status = newStatus
  ElMessage.success('新闻状态已更新')
}

// 批量发布
const batchPublish = () => {
  if (selectedNews.value.length === 0) {
    ElMessage.warning('请选择要发布的新闻')
    return
  }
  selectedNews.value.forEach(news => {
    news.status = 'published'
  })
  ElMessage.success(`已发布 ${selectedNews.value.length} 条新闻`)
  selectedNews.value = []
}

// 批量删除
const batchDelete = () => {
  if (selectedNews.value.length === 0) {
    ElMessage.warning('请选择要删除的新闻')
    return
  }
  ElMessageBox.confirm(`确定要删除选中的 ${selectedNews.value.length} 条新闻吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    selectedNews.value.forEach(news => {
      const index = newsList.value.findIndex(n => n.id === news.id)
      if (index !== -1) {
        newsList.value.splice(index, 1)
      }
    })
    ElMessage.success(`已删除 ${selectedNews.value.length} 条新闻`)
    selectedNews.value = []
  }).catch(() => {
    // 取消删除
  })
}

// 行双击事件
const handleRowDoubleClick = (row) => {
  showNewsDetail(row)
}

// 选择事件
const handleSelectionChange = (selection) => {
  selectedNews.value = selection
}

// 关闭对话框
const handleDialogClose = () => {
  newsDialogVisible.value = false
  if (newsFormRef.value) {
    newsFormRef.value.resetFields()
  }
}

// 封面上传前验证
const beforeCoverUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImage) {
    ElMessage.error('只能上传图片格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isImage && isLt2M
}

// 封面上传成功处理
const handleCoverUpload = (response, file, fileList) => {
  // 模拟上传成功，实际项目中应该使用服务器返回的图片地址
  currentNews.value.cover = URL.createObjectURL(file.raw)
  ElMessage.success('封面上传成功')
}
</script>

<style scoped>
/* 面包屑样式 */
.breadcrumb {
  margin-bottom: 16px;
  font-size: 14px;
}

/* news-management 主容器样式 */
.news-management {
  background-color: #ffffff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

.page-title {
  color: #2c3e50;
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: 600;
}

.filter-section {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.search-input,
.filter-select {
  width: 100%;
}

.batch-btn {
  margin-left: 10px;
}

.news-table {
  margin-bottom: 20px;
}

.pagination {
  text-align: right;
}

.title-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.news-detail {
  padding: 20px 0;
}

.detail-title {
  margin-bottom: 20px;
  color: #2c3e50;
  font-size: 20px;
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e9ecef;
  color: #6c757d;
}

.detail-cover {
  margin-bottom: 20px;
  text-align: center;
}

.detail-cover img {
  max-width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 4px;
}

.detail-summary {
  margin-bottom: 20px;
  line-height: 1.6;
  color: #6c757d;
}

.detail-content {
  line-height: 1.8;
  color: #343a40;
}

.detail-content div {
  margin-top: 10px;
}

/* 表格容器样式 */
.table-container {
  overflow-x: auto;
  margin-bottom: 16px;
  position: relative;
}

/* 表格样式 */
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

/* 表单样式 */
:deep(.el-form-item__label) {
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

/* 上传组件样式 */
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .news-management {
    padding: 16px;
  }
  
  .filter-section {
    padding: 10px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .detail-meta {
    flex-direction: column;
    gap: 10px;
  }
  
  .batch-btn {
    margin-left: 0;
    margin-top: 10px;
    width: 100%;
  }
}
</style>