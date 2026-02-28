<template>
  <AdminLayout>
    <div class="announcement-management">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item><a href="#/back/dashboard">首页</a></el-breadcrumb-item>
          <el-breadcrumb-item>信息发布与宣传阵地</el-breadcrumb-item>
          <el-breadcrumb-item>公告管理</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 页面标题 -->
      <h2 class="page-title">公告管理</h2>

      <!-- 搜索和筛选区域 -->
      <div class="filter-section">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-input
              v-model="searchQuery"
              placeholder="搜索公告标题或内容"
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
              v-model="filterPriority"
              placeholder="筛选优先级"
              clearable
              class="filter-select"
            >
              <el-option label="全部优先级" value=""></el-option>
              <el-option label="紧急" value="urgent"></el-option>
              <el-option label="重要" value="important"></el-option>
              <el-option label="一般" value="normal"></el-option>
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
              <el-option label="已过期" value="expired"></el-option>
            </el-select>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-button type="primary" @click="showAddAnnouncementDialog" class="add-btn">
              <el-icon><Plus /></el-icon> 添加公告
            </el-button>
            <el-button @click="batchPublish" class="batch-btn">批量发布</el-button>
            <el-button @click="batchDelete" class="batch-btn">批量删除</el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 公告列表 -->
      <div class="table-container">
        <el-table
          :data="paginatedAnnouncements"
          stripe
          border
          style="width: 100%"
          @selection-change="handleSelectionChange"
          @row-dblclick="handleRowDoubleClick"
        >
          <el-table-column type="selection" width="65"></el-table-column>
          <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
          <el-table-column prop="title" label="公告标题" min-width="200">
            <template #default="scope">
              <div class="title-container">
                <el-tag v-if="scope.row.priority === 'urgent'" type="danger" size="small">紧急</el-tag>
                <el-tag v-else-if="scope.row.priority === 'important'" type="warning" size="small">重要</el-tag>
                {{ scope.row.title }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="author" label="发布人" width="120"></el-table-column>
          <el-table-column prop="department" label="发布部门" width="150"></el-table-column>
          <el-table-column prop="priority" label="优先级" width="100" align="center">
            <template #default="scope">
              <el-tag :type="getPriorityTagType(scope.row.priority)">
                {{ getPriorityName(scope.row.priority) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="publishDate" label="发布日期" width="150" align="center"></el-table-column>
          <el-table-column prop="expiryDate" label="到期日期" width="150" align="center"></el-table-column>
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ getStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220" align="center">
            <template #default="scope">
              <el-button type="primary" size="small" @click="showAnnouncementDetail(scope.row)">
                查看
              </el-button>
              <el-button type="success" size="small" @click="showEditAnnouncementDialog(scope.row)">
                编辑
              </el-button>
              <el-button
                :type="scope.row.status === 'published' ? 'warning' : 'success'"
                size="small"
                @click="handleStatusChange(scope.row)"
              >
                {{ scope.row.status === 'published' ? '撤回' : '发布' }}
              </el-button>
              <el-button type="danger" size="small" @click="handleDeleteAnnouncement(scope.row)">
                删除
              </el-button>
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
          :total="filteredAnnouncements.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>

      <!-- 添加/编辑公告对话框 -->
      <el-dialog
        v-model="announcementDialogVisible"
        :title="isEditMode ? '编辑公告' : '添加公告'"
        width="800px"
        :before-close="handleDialogClose"
      >
        <el-form :model="currentAnnouncement" label-width="100px" :rules="announcementRules" ref="announcementFormRef">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="公告标题" prop="title">
                <el-input v-model="currentAnnouncement.title" placeholder="请输入公告标题"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发布人" prop="author">
                <el-input v-model="currentAnnouncement.author" placeholder="请输入发布人姓名"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发布部门" prop="department">
                <el-input v-model="currentAnnouncement.department" placeholder="请输入发布部门"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="优先级" prop="priority">
                <el-select v-model="currentAnnouncement.priority" placeholder="请选择优先级">
                  <el-option label="一般" value="normal"></el-option>
                  <el-option label="重要" value="important"></el-option>
                  <el-option label="紧急" value="urgent"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发布日期" prop="publishDate">
                <el-date-picker
                  v-model="currentAnnouncement.publishDate"
                  type="datetime"
                  placeholder="选择发布日期"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="到期日期" prop="expiryDate">
                <el-date-picker
                  v-model="currentAnnouncement.expiryDate"
                  type="datetime"
                  placeholder="选择到期日期"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="公告内容" prop="content">
                <el-input
                  v-model="currentAnnouncement.content"
                  type="textarea"
                  :rows="10"
                  placeholder="请输入公告内容"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否置顶">
                <el-switch v-model="currentAnnouncement.isTop"></el-switch>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态">
                <el-select v-model="currentAnnouncement.status">
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
            <el-button type="primary" @click="handleSaveAnnouncement">保存</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 公告详情对话框 -->
      <el-dialog
        v-model="announcementDetailVisible"
        title="公告详情"
        width="800px"
      >
        <div v-if="currentAnnouncement" class="announcement-detail">
          <h3 class="detail-title">{{ currentAnnouncement.title }}</h3>
          <div class="detail-meta">
            <span><strong>发布人：</strong>{{ currentAnnouncement.author }}</span>
            <span><strong>发布部门：</strong>{{ currentAnnouncement.department }}</span>
            <span><strong>优先级：</strong>{{ getPriorityName(currentAnnouncement.priority) }}</span>
            <span><strong>发布日期：</strong>{{ currentAnnouncement.publishDate }}</span>
            <span><strong>到期日期：</strong>{{ currentAnnouncement.expiryDate }}</span>
            <span><strong>状态：</strong>{{ getStatusName(currentAnnouncement.status) }}</span>
            <el-tag v-if="currentAnnouncement.isTop" type="success" size="small">置顶</el-tag>
          </div>
          <div class="detail-content">
            <strong>内容：</strong>
            <div v-html="currentAnnouncement.content"></div>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="announcementDetailVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import AdminLayout from '@/components/AdminLayout.vue'
import { ElMessage, ElMessageBox, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElDatePicker, ElIcon } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

// 状态管理
const searchQuery = ref('')
const filterPriority = ref('')
const filterStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const announcementDialogVisible = ref(false)
const announcementDetailVisible = ref(false)
const isEditMode = ref(false)
const currentAnnouncement = ref({})
const selectedAnnouncements = ref([])
const announcementFormRef = ref(null)

// 公告表单验证规则
const announcementRules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  author: [{ required: true, message: '请输入发布人', trigger: 'blur' }],
  department: [{ required: true, message: '请输入发布部门', trigger: 'blur' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
  publishDate: [{ required: true, message: '请选择发布日期', trigger: 'change' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }]
}

// 模拟公告数据 - 西安工业大学计算机学院2025年6-11月通知公告
const announcements = ref([
  // 2025年11月
  {
    id: 1,
    title: "关于计算机学院2025-2026学年第一学期期末考试党员先锋岗安排的通知",
    author: "张老师",
    department: "计算机学院党委",
    priority: "important",
    publishDate: "2025-11-28 09:00:00",
    expiryDate: "2025-12-31 23:59:59",
    content: "各党支部、全体党员：\n\n为确保2025-2026学年第一学期期末考试顺利进行，充分发挥党员先锋模范作用，维护良好的考试秩序，营造公平、公正的考试环境，现将期末考试党员先锋岗安排通知如下：\n\n一、工作时间：2025年12月15日至2025年12月25日\n二、工作岗位及职责：\n1. 考场监督岗：负责巡查考场纪律，维护考场秩序；\n2. 服务保障岗：负责考试期间的服务保障工作，解决突发问题；\n3. 宣传引导岗：负责考试诚信教育宣传，引导学生诚信考试。\n\n三、有关要求：\n1. 党员先锋岗人员要提前15分钟到岗，佩戴党徽和先锋岗标识；\n2. 要认真履行岗位职责，严格遵守工作纪律；\n3. 要热情服务学生，及时解决学生遇到的问题；\n4. 各党支部要加强对党员先锋岗人员的管理，确保工作落实到位。\n\n请各党支部于12月10日前将党员先锋岗人员名单报学院党委办公室。",
    isTop: true,
    status: "published"
  },
  {
    id: 2,
    title: "关于开展2025年秋季学期计算机学院党员民主生活会的通知",
    author: "李老师",
    department: "计算机学院党委",
    priority: "urgent",
    publishDate: "2025-11-15 14:00:00",
    expiryDate: "2025-11-30 23:59:59",
    content: "各党支部：\n\n根据学校党委《关于召开2025年秋季学期党员民主生活会的通知》要求，结合我院实际，现就开展2025年秋季学期党员民主生活会有关事项通知如下：\n\n一、会议主题：深入学习贯彻党的二十大精神和习近平总书记系列重要讲话精神\n二、会议时间：2025年11月25日（星期二）下午2:30\n三、会议地点：计算机学院会议室\n四、参会人员：全体党员\n五、会议要求：\n1. 各党支部要组织党员认真学习相关文件，做好会前准备；\n2. 党员要结合自身实际，撰写发言材料；\n3. 会议要严肃认真，开展批评与自我批评；\n4. 各党支部要做好会议记录，及时上报会议情况。\n\n请各党支部于11月20日前将参会人员名单报学院党委办公室。",
    isTop: false,
    status: "published"
  },
  // 2025年10月
  {
    id: 3,
    title: "关于举办2025年计算机学院创新创业大赛导师招募的通知",
    author: "王老师",
    department: "计算机学院团委",
    priority: "important",
    publishDate: "2025-10-20 10:00:00",
    expiryDate: "2025-11-10 23:59:59",
    content: "全体教师：\n\n为进一步推动我院创新创业教育，提升学生创新实践能力，现面向全院教师招募2025年计算机学院创新创业大赛导师，有关事项通知如下：\n\n一、招募对象：全院在职教师\n二、招募条件：\n1. 具有较强的专业知识和实践经验；\n2. 热心指导学生创新创业活动；\n3. 能够保证一定的指导时间。\n三、报名时间：2025年10月20日至2025年10月30日\n四、报名方式：填写《创新创业大赛导师申请表》，发送至邮箱：jisuanji@xatu.edu.cn\n\n请有意愿的教师积极报名参与，共同推动我院创新创业教育发展。",
    isTop: false,
    status: "published"
  },
  {
    id: 4,
    title: "关于组织观看党史教育影片《建党伟业》的通知",
    author: "刘老师",
    department: "计算机学院党委",
    priority: "normal",
    publishDate: "2025-10-10 09:00:00",
    expiryDate: "2025-10-15 23:59:59",
    content: "各党支部、全体党员：\n\n为深入开展党史学习教育，增强党员的党性修养，现组织观看党史教育影片《建党伟业》，有关事项通知如下：\n\n一、观影时间：2025年10月13日（星期一）晚上7:00\n二、观影地点：计算机学院报告厅\n三、参会人员：全体党员\n四、观影要求：\n1. 请提前15分钟到场，签到入场；\n2. 观影期间请保持安静，关闭手机或调至静音；\n3. 观影结束后，请各党支部组织党员进行讨论，撰写观影心得。\n\n请各党支部于10月12日前将参会人员名单报学院党委办公室。",
    isTop: false,
    status: "published"
  },
  // 2025年9月
  {
    id: 5,
    title: "关于开展2025级新生入党教育的通知",
    author: "陈老师",
    department: "计算机学院党委",
    priority: "urgent",
    publishDate: "2025-09-25 14:00:00",
    expiryDate: "2025-10-10 23:59:59",
    content: "2025级新生：\n\n为加强新生入党启蒙教育，引导新生树立正确的入党动机，现开展2025级新生入党教育，有关事项通知如下：\n\n一、教育时间：2025年9月28日（星期日）上午9:00\n二、教育地点：计算机学院报告厅\n三、教育内容：\n1. 党的基本知识讲解；\n2. 入党程序介绍；\n3. 优秀党员事迹报告。\n四、参会人员：2025级全体新生\n\n请2025级新生准时参加，不得缺席。",
    isTop: true,
    status: "published"
  },
  {
    id: 6,
    title: "关于印发《2025-2026学年计算机学院党支部工作计划》的通知",
    author: "赵老师",
    department: "计算机学院党委",
    priority: "important",
    publishDate: "2025-09-15 10:00:00",
    expiryDate: "2026-09-30 23:59:59",
    content: "各党支部：\n\n现将《2025-2026学年计算机学院党支部工作计划》印发给你们，请结合实际认真贯彻执行。\n\n一、指导思想：深入学习贯彻党的二十大精神和习近平总书记系列重要讲话精神\n二、工作目标：加强党支部建设，提高党员素质，推动学院事业发展\n三、主要工作：\n1. 加强理论学习，提高政治素养；\n2. 加强组织建设，严格党员管理；\n3. 加强作风建设，提高服务能力；\n4. 加强制度建设，规范工作流程。\n\n请各党支部结合实际，制定本支部的工作计划，并认真组织实施。",
    isTop: false,
    status: "published"
  },
  // 2025年8月
  {
    id: 7,
    title: "关于2025年暑期党员社会实践活动总结的通知",
    author: "孙老师",
    department: "计算机学院党委",
    priority: "normal",
    publishDate: "2025-08-25 09:00:00",
    expiryDate: "2025-09-30 23:59:59",
    content: "各党支部：\n\n为总结2025年暑期党员社会实践活动经验，展示活动成果，现将有关事项通知如下：\n\n一、总结内容：\n1. 活动开展情况；\n2. 取得的主要成果；\n3. 存在的问题及改进措施；\n4. 典型经验和做法。\n二、提交时间：2025年8月31日前\n三、提交方式：发送至邮箱：jisuanji@xatu.edu.cn\n\n请各党支部高度重视，认真总结，按时提交。",
    isTop: false,
    status: "published"
  },
  {
    id: 8,
    title: "关于2025年秋季学期党员志愿服务活动安排的通知",
    author: "周老师",
    department: "计算机学院团委",
    priority: "important",
    publishDate: "2025-08-15 14:00:00",
    expiryDate: "2025-09-10 23:59:59",
    content: "各党支部、全体党员：\n\n为进一步弘扬志愿服务精神，发挥党员先锋模范作用，现将2025年秋季学期党员志愿服务活动安排通知如下：\n\n一、活动时间：2025年9月至2026年1月\n二、活动内容：\n1. 校园文明劝导；\n2. 社区服务；\n3. 科技科普宣传；\n4. 扶危助困。\n三、报名方式：填写《党员志愿服务活动报名表》，交至学院团委办公室\n\n请全体党员积极参与，展现党员良好形象。",
    isTop: false,
    status: "published"
  },
  // 2025年7月
  {
    id: 9,
    title: "关于2025年暑假期间党支部工作安排的通知",
    author: "吴老师",
    department: "计算机学院党委",
    priority: "important",
    publishDate: "2025-07-20 10:00:00",
    expiryDate: "2025-08-31 23:59:59",
    content: "各党支部：\n\n现将2025年暑假期间党支部工作安排通知如下：\n\n一、学习安排：\n1. 组织党员学习党的最新理论成果；\n2. 开展党史学习教育；\n3. 学习专业知识和技能。\n二、安全工作：\n1. 做好假期安全宣传；\n2. 加强对党员的安全教育；\n3. 落实安全责任制。\n三、其他工作：\n1. 做好假期值班安排；\n2. 及时处理突发事件；\n3. 做好新学期工作准备。\n\n请各党支部认真落实各项工作安排，确保假期安全稳定。",
    isTop: false,
    status: "published"
  },
  {
    id: 10,
    title: "关于评选2025年上半年优秀共产党员的通知",
    author: "郑老师",
    department: "计算机学院党委",
    priority: "urgent",
    publishDate: "2025-07-10 14:00:00",
    expiryDate: "2025-07-20 23:59:59",
    content: "各党支部：\n\n为表彰先进，树立榜样，激励广大党员发挥先锋模范作用，现将评选2025年上半年优秀共产党员有关事项通知如下：\n\n一、评选对象：全院在职党员\n二、评选条件：\n1. 政治素质高，思想作风好；\n2. 工作业绩突出，先锋模范作用明显；\n3. 群众基础好，认可度高。\n三、评选程序：\n1. 个人申请；\n2. 支部推荐；\n3. 学院党委审批。\n四、报名时间：2025年7月10日至2025年7月15日\n\n请各党支部认真组织评选工作，确保公平、公正、公开。",
    isTop: true,
    status: "published"
  },
  // 2025年6月
  {
    id: 11,
    title: "关于2025年上半年党员发展工作的通知",
    author: "王老师",
    department: "计算机学院党委",
    priority: "urgent",
    publishDate: "2025-06-20 10:00:00",
    expiryDate: "2025-07-10 23:59:59",
    content: "各党支部：\n\n现将2025年上半年党员发展工作有关事项通知如下：\n\n一、发展计划：\n1. 拟发展预备党员X名；\n2. 拟转正预备党员X名。\n二、工作要求：\n1. 严格按照党员发展程序进行；\n2. 确保发展质量；\n3. 按时完成各项工作任务。\n三、时间安排：\n1. 6月20日至6月25日：支部讨论；\n2. 6月26日至6月30日：学院党委审批。\n\n请各党支部认真组织实施，确保党员发展工作顺利完成。",
    isTop: true,
    status: "published"
  },
  {
    id: 12,
    title: "关于2025年端午节放假安排及相关工作的通知",
    author: "李老师",
    department: "计算机学院办公室",
    priority: "normal",
    publishDate: "2025-06-10 09:00:00",
    expiryDate: "2025-06-25 23:59:59",
    content: "全体师生：\n\n根据学校统一安排，现将2025年端午节放假安排及相关工作通知如下：\n\n一、放假时间：6月12日至6月14日，共3天\n二、相关工作：\n1. 做好假期安全工作；\n2. 落实值班制度；\n3. 做好应急准备。\n三、注意事项：\n1. 假期外出请注意安全；\n2. 保持通讯畅通；\n3. 按时返校。\n\n祝全体师生端午节快乐！",
    isTop: false,
    status: "published"
  }
])

// 过滤后的公告列表
const filteredAnnouncements = computed(() => {
  return announcements.value.filter(announcement => {
    const matchesSearch = announcement.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                         announcement.content.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesPriority = !filterPriority.value || announcement.priority === filterPriority.value
    const matchesStatus = !filterStatus.value || announcement.status === filterStatus.value
    return matchesSearch && matchesPriority && matchesStatus
  })
})

// 分页后的公告列表
const paginatedAnnouncements = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredAnnouncements.value.slice(start, end)
})

// 优先级名称映射
const getPriorityName = (priority) => {
  const priorityMap = {
    normal: '一般',
    important: '重要',
    urgent: '紧急'
  }
  return priorityMap[priority] || priority
}

// 优先级标签样式映射
const getPriorityTagType = (priority) => {
  const typeMap = {
    normal: 'info',
    important: 'warning',
    urgent: 'danger'
  }
  return typeMap[priority] || 'default'
}

// 状态名称映射
const getStatusName = (status) => {
  const statusMap = {
    draft: '草稿',
    published: '已发布',
    expired: '已过期'
  }
  return statusMap[status] || status
}

// 状态标签样式映射
const getStatusTagType = (status) => {
  const typeMap = {
    draft: 'info',
    published: 'success',
    expired: 'warning'
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

// 显示添加公告对话框
const showAddAnnouncementDialog = () => {
  isEditMode.value = false
  currentAnnouncement.value = {
    title: '',
    author: '',
    department: '',
    priority: 'normal',
    publishDate: new Date().toISOString().split('T')[0] + ' ' + new Date().toTimeString().split(' ')[0],
    expiryDate: '',
    content: '',
    isTop: false,
    status: 'draft'
  }
  announcementDialogVisible.value = true
}

// 显示编辑公告对话框
const showEditAnnouncementDialog = (announcement) => {
  isEditMode.value = true
  currentAnnouncement.value = JSON.parse(JSON.stringify(announcement))
  announcementDialogVisible.value = true
}

// 显示公告详情
const showAnnouncementDetail = (announcement) => {
  currentAnnouncement.value = JSON.parse(JSON.stringify(announcement))
  announcementDetailVisible.value = true
}

// 保存公告
const handleSaveAnnouncement = () => {
  if (!announcementFormRef.value) return
  announcementFormRef.value.validate((valid) => {
    if (valid) {
      if (isEditMode.value) {
        // 编辑模式
        const index = announcements.value.findIndex(a => a.id === currentAnnouncement.value.id)
        if (index !== -1) {
          announcements.value[index] = JSON.parse(JSON.stringify(currentAnnouncement.value))
        }
      } else {
        // 添加模式
        const newId = Math.max(...announcements.value.map(a => a.id)) + 1
        currentAnnouncement.value.id = newId
        announcements.value.unshift(JSON.parse(JSON.stringify(currentAnnouncement.value)))
      }
      announcementDialogVisible.value = false
      ElMessage.success(isEditMode.value ? '公告编辑成功' : '公告添加成功')
    } else {
      ElMessage.error('请填写必填字段')
      return false
    }
  })
}

// 删除公告
const handleDeleteAnnouncement = (announcement) => {
  ElMessageBox.confirm('确定要删除该公告吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = announcements.value.findIndex(a => a.id === announcement.id)
    if (index !== -1) {
      announcements.value.splice(index, 1)
      ElMessage.success('公告删除成功')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 状态变化处理
const handleStatusChange = (announcement) => {
  if (announcement.status === 'published') {
    announcement.status = 'draft'
  } else {
    announcement.status = 'published'
  }
  ElMessage.success('公告状态已更新')
}

// 批量发布
const batchPublish = () => {
  if (selectedAnnouncements.value.length === 0) {
    ElMessage.warning('请选择要发布的公告')
    return
  }
  selectedAnnouncements.value.forEach(announcement => {
    announcement.status = 'published'
  })
  ElMessage.success(`已发布 ${selectedAnnouncements.value.length} 条公告`)
  selectedAnnouncements.value = []
}

// 批量删除
const batchDelete = () => {
  if (selectedAnnouncements.value.length === 0) {
    ElMessage.warning('请选择要删除的公告')
    return
  }
  ElMessageBox.confirm(`确定要删除选中的 ${selectedAnnouncements.value.length} 条公告吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    selectedAnnouncements.value.forEach(announcement => {
      const index = announcements.value.findIndex(a => a.id === announcement.id)
      if (index !== -1) {
        announcements.value.splice(index, 1)
      }
    })
    ElMessage.success(`已删除 ${selectedAnnouncements.value.length} 条公告`)
    selectedAnnouncements.value = []
  }).catch(() => {
    // 取消删除
  })
}

// 行双击事件
const handleRowDoubleClick = (row) => {
  showAnnouncementDetail(row)
}

// 选择事件
const handleSelectionChange = (selection) => {
  selectedAnnouncements.value = selection
}

// 关闭对话框
const handleDialogClose = () => {
  announcementDialogVisible.value = false
  if (announcementFormRef.value) {
    announcementFormRef.value.resetFields()
  }
}
</script>

<style scoped>
.announcement-management {
  padding: 20px;
}

.page-title {
  color: #2c3e50;
  margin-bottom: 20px;
  font-size: 24px;
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

.table-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin-bottom: 20px;
}

.table-container .el-table {
  border: none;
  margin: 0;
}

.table-container .el-table__header-wrapper th {
  background-color: #fafafa;
  font-weight: 600;
  color: #303133;
}

.table-container .el-table__body-wrapper {
  border-radius: 0 0 8px 8px;
}

.pagination {
  text-align: right;
}

.title-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.announcement-detail {
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

.detail-content {
  line-height: 1.8;
  color: #343a40;
}

.detail-content div {
  margin-top: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
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