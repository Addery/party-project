<template>
  <AdminLayout>
    <div class="education-management">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item><a href="#/back/dashboard">首页</a></el-breadcrumb-item>
          <el-breadcrumb-item>党员教育与思政融合</el-breadcrumb-item>
          <el-breadcrumb-item>学习教育管理</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 页面标题 -->
      <h2 class="page-title">学习教育管理</h2>

      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ totalResources }}</div>
                <div class="stat-label">学习资源总数</div>
              </div>
              <div class="stat-icon resource-icon"></div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ totalExams }}</div>
                <div class="stat-label">考试总数</div>
              </div>
              <div class="stat-icon exam-icon"></div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ completedCourses }}</div>
                <div class="stat-label">已完成课程数</div>
              </div>
              <div class="stat-icon complete-icon"></div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ totalParticipants }}</div>
                <div class="stat-label">总参与人数</div>
              </div>
              <div class="stat-icon participant-icon"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 标签页 -->
      <div class="tabs-section">
        <el-tabs v-model="activeTab" type="card">
          <!-- 学习资源管理标签页 -->
          <el-tab-pane label="学习资源管理" name="resources">
            <div class="resources-tab">
              <!-- 搜索和筛选区域 -->
              <div class="filter-section">
                <el-row :gutter="20">
                  <el-col :xs="24" :sm="12" :md="8" :lg="6">
                    <el-input
                      v-model="resourceSearchQuery"
                      placeholder="搜索资源名称或关键词"
                      clearable
                      class="search-input"
                      @input="handleResourceSearch"
                    >
                      <template #append>
                        <el-button icon="el-icon-search" @click="handleResourceSearch"></el-button>
                      </template>
                    </el-input>
                  </el-col>
                  <el-col :xs="24" :sm="12" :md="8" :lg="6">
                    <el-select
                      v-model="resourceFilterType"
                      placeholder="筛选资源类型"
                      clearable
                      class="filter-select"
                      @change="handleResourceSearch"
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
                      v-model="resourceFilterCategory"
                      placeholder="筛选分类"
                      clearable
                      class="filter-select"
                      @change="handleResourceSearch"
                    >
                      <el-option label="全部分类" value=""></el-option>
                      <el-option label="理论学习" value="theory"></el-option>
                      <el-option label="党史教育" value="history"></el-option>
                      <el-option label="政策法规" value="policy"></el-option>
                      <el-option label="先进典型" value="model"></el-option>
                      <el-option label="廉政教育" value="integrity"></el-option>
                    </el-select>
                  </el-col>
                  <el-col :xs="24" :sm="12" :md="8" :lg="6">
                    <el-button type="primary" @click="showAddResourceDialog" class="add-btn">
                  <el-icon><Plus /></el-icon> 添加资源
                </el-button>
                  </el-col>
                </el-row>
              </div>

              <!-- 学习资源列表 -->
              <div class="resources-table">
                <div class="table-container">
                <el-table
                  :data="paginatedResources"
                  stripe
                  border
                  style="width: 100%"
                  @row-dblclick="handleResourceRowDoubleClick"
                >
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
                        @change="handleResourceStatusChange(scope.row)"
                      ></el-switch>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="200" align="center">
                    <template #default="scope">
                      <el-button type="primary" size="small" @click="showResourceDetail(scope.row)">
                        查看
                      </el-button>
                      <el-button type="success" size="small" @click="showEditResourceDialog(scope.row)">
                        编辑
                      </el-button>
                      <el-button type="danger" size="small" @click="handleDeleteResource(scope.row)">
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                </div>
              </div>

              <!-- 分页 -->
              <div class="pagination">
                <el-pagination
                  v-model:current-page="resourceCurrentPage"
                  v-model:page-size="resourcePageSize"
                  :page-sizes="[10, 20, 50, 100]"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="filteredResources.length"
                  @size-change="handleResourceSizeChange"
                  @current-change="handleResourceCurrentChange"
                ></el-pagination>
              </div>
            </div>
          </el-tab-pane>

          <!-- 考试管理标签页 -->
          <el-tab-pane label="考试管理" name="exams">
            <div class="exams-tab">
              <!-- 搜索和筛选区域 -->
              <div class="filter-section">
                <el-row :gutter="20">
                  <el-col :xs="24" :sm="12" :md="8" :lg="6">
                    <el-input
                      v-model="examSearchQuery"
                      placeholder="搜索考试名称"
                      clearable
                      class="search-input"
                      @input="handleExamSearch"
                    >
                      <template #append>
                        <el-button icon="el-icon-search" @click="handleExamSearch"></el-button>
                      </template>
                    </el-input>
                  </el-col>
                  <el-col :xs="24" :sm="12" :md="8" :lg="6">
                    <el-select
                      v-model="examFilterStatus"
                      placeholder="筛选考试状态"
                      clearable
                      class="filter-select"
                      @change="handleExamSearch"
                    >
                      <el-option label="全部状态" value=""></el-option>
                      <el-option label="未开始" value="not_started"></el-option>
                      <el-option label="进行中" value="in_progress"></el-option>
                      <el-option label="已结束" value="completed"></el-option>
                    </el-select>
                  </el-col>
                  <el-col :xs="24" :sm="12" :md="8" :lg="6">
                    <el-button type="primary" @click="showAddExamDialog" class="add-btn">
                  <el-icon><Plus /></el-icon> 创建考试
                </el-button>
                  </el-col>
                </el-row>
              </div>

              <!-- 考试列表 -->
              <div class="exams-table">
                <div class="table-container">
                <el-table
                  :data="paginatedExams"
                  stripe
                  border
                  style="width: 100%"
                  @row-dblclick="handleExamRowDoubleClick"
                >
                  <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
                  <el-table-column prop="title" label="考试名称" min-width="200"></el-table-column>
                  <el-table-column prop="category" label="考试分类" width="120" align="center">
                    <template #default="scope">
                      <el-tag>{{ scope.row.category }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="startTime" label="开始时间" width="180" align="center"></el-table-column>
                  <el-table-column prop="endTime" label="结束时间" width="180" align="center"></el-table-column>
                  <el-table-column prop="duration" label="考试时长" width="100" align="center">
                    <template #default="scope">
                      {{ scope.row.duration }}分钟
                    </template>
                  </el-table-column>
                  <el-table-column prop="passScore" label="及格分数" width="100" align="center">
                    <template #default="scope">
                      {{ scope.row.passScore }}分
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="状态" width="120" align="center">
                    <template #default="scope">
                      <el-tag :type="getExamStatusTagType(scope.row.status)">
                        {{ getExamStatusName(scope.row.status) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="200" align="center">
                    <template #default="scope">
                      <el-button type="primary" size="small" @click="showExamDetail(scope.row)">
                        查看
                      </el-button>
                      <el-button type="success" size="small" @click="showEditExamDialog(scope.row)">
                        编辑
                      </el-button>
                      <el-button type="danger" size="small" @click="handleDeleteExam(scope.row)">
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                </div>
              </div>

              <!-- 分页 -->
              <div class="pagination">
                <el-pagination
                  v-model:current-page="examCurrentPage"
                  v-model:page-size="examPageSize"
                  :page-sizes="[10, 20, 50, 100]"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="filteredExams.length"
                  @size-change="handleExamSizeChange"
                  @current-change="handleExamCurrentChange"
                ></el-pagination>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- 添加/编辑资源对话框 -->
      <el-dialog
        v-model="resourceDialogVisible"
        :title="isEditResourceMode ? '编辑资源' : '添加资源'"
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
        <div class="resource-detail">
          <h3>{{ currentResource.title }}</h3>
          <div class="resource-meta">
            <span class="meta-item">类型: {{ getResourceTypeName(currentResource.type) }}</span>
            <span class="meta-item">分类: {{ getResourceCategoryName(currentResource.category) }}</span>
            <span class="meta-item">作者/来源: {{ currentResource.author }}</span>
            <span class="meta-item">上传日期: {{ currentResource.uploadDate }}</span>
            <span class="meta-item">浏览量: {{ currentResource.views }}</span>
          </div>
          <div class="resource-description">
            <h4>简介</h4>
            <p>{{ currentResource.description }}</p>
          </div>
          <div class="resource-content">
            <h4>内容</h4>
            <div v-html="currentResource.content"></div>
          </div>
        </div>
      </el-dialog>

      <!-- 添加/编辑考试对话框 -->
      <el-dialog
        v-model="examDialogVisible"
        :title="isEditExamMode ? '编辑考试' : '创建考试'"
        width="800px"
      >
        <el-form :model="currentExam" label-width="100px">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="考试名称" required>
                <el-input v-model="currentExam.title" placeholder="请输入考试名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="考试分类" required>
                <el-select v-model="currentExam.category" placeholder="请选择考试分类">
                  <el-option label="理论知识" value="theory"></el-option>
                  <el-option label="党史知识" value="history"></el-option>
                  <el-option label="政策法规" value="policy"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="考试时长" required>
                <el-input-number v-model="currentExam.duration" :min="10" :max="180" placeholder="请输入考试时长（分钟）"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开始时间" required>
                <el-date-picker
                  v-model="currentExam.startTime"
                  type="datetime"
                  placeholder="选择开始时间"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间" required>
                <el-date-picker
                  v-model="currentExam.endTime"
                  type="datetime"
                  placeholder="选择结束时间"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="及格分数" required>
                <el-input-number v-model="currentExam.passScore" :min="0" :max="100" placeholder="请输入及格分数"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="考试说明">
                <el-input
                  v-model="currentExam.description"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入考试说明"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="examDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSaveExam">保存</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 考试详情对话框 -->
      <el-dialog
        v-model="examDetailVisible"
        title="考试详情"
        width="800px"
      >
        <div class="exam-detail">
          <h3>{{ currentExam.title }}</h3>
          <div class="exam-meta">
            <span class="meta-item">分类: {{ currentExam.category }}</span>
            <span class="meta-item">状态: {{ getExamStatusName(currentExam.status) }}</span>
            <span class="meta-item">开始时间: {{ currentExam.startTime }}</span>
            <span class="meta-item">结束时间: {{ currentExam.endTime }}</span>
            <span class="meta-item">时长: {{ currentExam.duration }}分钟</span>
            <span class="meta-item">及格分数: {{ currentExam.passScore }}分</span>
          </div>
          <div class="exam-description">
            <h4>考试说明</h4>
            <p>{{ currentExam.description }}</p>
          </div>
        </div>
      </el-dialog>
    </div>
  </AdminLayout>
</template>

<script>
import { ref, computed } from 'vue';
import AdminLayout from '@/components/AdminLayout.vue';
import { Plus } from '@element-plus/icons-vue';
import { ElIcon } from 'element-plus';

export default {
  name: 'EducationManagement',
  components: {
    AdminLayout,
    ElIcon,
    Plus
  },
  setup() {
    // 标签页状态
    const activeTab = ref('resources');

    // 学习资源数据
    const resources = ref([
      { id: 1, title: '习近平新时代中国特色社会主义思想学习纲要', type: 'article', category: 'theory', author: '中共中央宣传部', uploadDate: '2023-01-15', views: 1250, status: 'published', url: '#', description: '深入学习贯彻习近平新时代中国特色社会主义思想的重要辅助读物', content: '<p>这是一本深入阐述习近平新时代中国特色社会主义思想的重要著作...</p>' },
      { id: 2, title: '中国共产党章程（2022年修订）', type: 'document', category: 'policy', author: '中共中央', uploadDate: '2023-02-01', views: 980, status: 'published', url: '#', description: '中国共产党的根本大法，是全党必须遵循的总规矩', content: '<p>中国共产党章程规定了党的性质、宗旨、指导思想、奋斗目标...</p>' },
      { id: 3, title: '党史学习教育专题讲座', type: 'video', category: 'history', author: '中央党校', uploadDate: '2023-03-10', views: 850, status: 'published', url: '#', description: '党史学习教育专题讲座视频', content: '<p>讲座内容涵盖了中国共产党的百年奋斗历程...</p>' },
      { id: 4, title: '廉政教育警示片', type: 'video', category: 'integrity', author: '中央纪委国家监委', uploadDate: '2023-04-05', views: 720, status: 'published', url: '#', description: '廉政教育警示片，加强党员干部廉洁自律意识', content: '<p>警示片通过真实案例，教育党员干部要廉洁从政...</p>' },
      { id: 5, title: '榜样的力量', type: 'article', category: 'model', author: '人民日报', uploadDate: '2023-05-20', views: 680, status: 'published', url: '#', description: '先进典型人物事迹报道', content: '<p>本文介绍了多位优秀共产党员的先进事迹...</p>' },
      { id: 6, title: '党的二十大报告解读', type: 'audio', category: 'policy', author: '中央党校', uploadDate: '2023-06-15', views: 590, status: 'published', url: '#', description: '党的二十大报告解读音频', content: '<p>音频内容详细解读了党的二十大报告的主要内容...</p>' },
      { id: 7, title: '马克思主义基本原理概论', type: 'document', category: 'theory', author: '高等教育出版社', uploadDate: '2023-07-25', views: 480, status: 'published', url: '#', description: '马克思主义基本原理概论教材', content: '<p>本书系统阐述了马克思主义的基本原理...</p>' },
      { id: 8, title: '改革开放史专题学习', type: 'article', category: 'history', author: '中共中央党史和文献研究院', uploadDate: '2023-08-10', views: 420, status: 'published', url: '#', description: '改革开放史专题学习资料', content: '<p>本文回顾了中国改革开放的历史进程...</p>' },
      { id: 9, title: '新时代党的建设总要求', type: 'article', category: 'policy', author: '新华社', uploadDate: '2023-09-05', views: 390, status: 'published', url: '#', description: '新时代党的建设总要求解读', content: '<p>本文解读了新时代党的建设总要求的内涵...</p>' },
      { id: 10, title: '优秀党员事迹报告会', type: 'video', category: 'model', author: '中央组织部', uploadDate: '2023-10-15', views: 350, status: 'published', url: '#', description: '优秀党员事迹报告会视频', content: '<p>报告会邀请了多位优秀共产党员分享他们的事迹...</p>' },
      { id: 11, title: '中国共产党简史', type: 'document', category: 'history', author: '中共中央党史研究室', uploadDate: '2023-11-20', views: 320, status: 'published', url: '#', description: '中国共产党简史教材', content: '<p>本书简明扼要地介绍了中国共产党的发展历史...</p>' },
      { id: 12, title: '党风廉政建设责任制实施办法', type: 'document', category: 'integrity', author: '中共中央办公厅', uploadDate: '2023-12-05', views: 280, status: 'published', url: '#', description: '党风廉政建设责任制实施办法', content: '<p>本文规定了党风廉政建设责任制的实施细则...</p>' }
    ]);

    // 考试数据
    const exams = ref([
      { id: 1, title: '习近平新时代中国特色社会主义思想测试', category: 'theory', startTime: '2023-01-20 09:00', endTime: '2023-01-25 17:00', duration: 60, passScore: 60, status: 'completed', description: '测试党员对习近平新时代中国特色社会主义思想的掌握程度' },
      { id: 2, title: '党章知识竞赛', category: 'policy', startTime: '2023-02-15 09:00', endTime: '2023-02-20 17:00', duration: 45, passScore: 60, status: 'completed', description: '测试党员对党章的熟悉程度' },
      { id: 3, title: '党史知识测试', category: 'history', startTime: '2023-03-10 09:00', endTime: '2023-03-15 17:00', duration: 60, passScore: 60, status: 'completed', description: '测试党员对党史的了解程度' },
      { id: 4, title: '廉政知识测试', category: 'integrity', startTime: '2023-04-05 09:00', endTime: '2023-04-10 17:00', duration: 45, passScore: 60, status: 'completed', description: '测试党员对廉政知识的掌握程度' },
      { id: 5, title: '党的二十大精神测试', category: 'policy', startTime: '2023-05-20 09:00', endTime: '2023-05-25 17:00', duration: 60, passScore: 60, status: 'completed', description: '测试党员对党的二十大精神的理解程度' },
      { id: 6, title: '马克思主义基本原理测试', category: 'theory', startTime: '2023-06-15 09:00', endTime: '2023-06-20 17:00', duration: 60, passScore: 60, status: 'completed', description: '测试党员对马克思主义基本原理的掌握程度' },
      { id: 7, title: '改革开放史知识测试', category: 'history', startTime: '2023-07-10 09:00', endTime: '2023-07-15 17:00', duration: 45, passScore: 60, status: 'completed', description: '测试党员对改革开放史的了解程度' },
      { id: 8, title: '新时代党的建设测试', category: 'policy', startTime: '2023-08-05 09:00', endTime: '2023-08-10 17:00', duration: 60, passScore: 60, status: 'completed', description: '测试党员对新时代党的建设的认识程度' },
      { id: 9, title: '中国特色社会主义理论体系测试', category: 'theory', startTime: '2023-09-15 09:00', endTime: '2023-09-20 17:00', duration: 60, passScore: 60, status: 'completed', description: '测试党员对中国特色社会主义理论体系的掌握程度' },
      { id: 10, title: '党的纪律处分条例测试', category: 'policy', startTime: '2023-10-10 09:00', endTime: '2023-10-15 17:00', duration: 45, passScore: 60, status: 'completed', description: '测试党员对党的纪律处分条例的了解程度' },
      { id: 11, title: '新中国史知识测试', category: 'history', startTime: '2023-11-05 09:00', endTime: '2023-11-10 17:00', duration: 45, passScore: 60, status: 'completed', description: '测试党员对新中国史的了解程度' },
      { id: 12, title: '社会主义发展史知识测试', category: 'history', startTime: '2023-12-10 09:00', endTime: '2023-12-15 17:00', duration: 45, passScore: 60, status: 'completed', description: '测试党员对社会主义发展史的了解程度' }
    ]);

    // 学习资源相关状态
    const searchQuery = ref('');
    const filterType = ref('');
    const filterCategory = ref('');
    const currentPage = ref(1);
    const pageSize = ref(10);
    const resourceDialogVisible = ref(false);
    const resourceDetailVisible = ref(false);
    const isEditResourceMode = ref(false);
    const currentResource = ref({
      id: null,
      title: '',
      type: 'article',
      category: 'theory',
      author: '',
      uploadDate: new Date().toISOString().split('T')[0],
      views: 0,
      status: 'published',
      url: '',
      description: '',
      content: ''
    });

    // 考试相关状态
    const examSearchQuery = ref('');
    const examFilterStatus = ref('');
    const examCurrentPage = ref(1);
    const examPageSize = ref(10);
    const examDialogVisible = ref(false);
    const examDetailVisible = ref(false);
    const isEditExamMode = ref(false);
    const currentExam = ref({
      id: null,
      title: '',
      category: 'theory',
      startTime: '',
      endTime: '',
      duration: 60,
      passScore: 60,
      status: 'not_started',
      description: ''
    });

    // 计算属性 - 筛选后的学习资源
    const filteredResources = computed(() => {
      return resources.value.filter(resource => {
        const matchesSearch = resource.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
                            resource.description.toLowerCase().includes(searchQuery.value.toLowerCase());
        const matchesType = !filterType.value || resource.type === filterType.value;
        const matchesCategory = !filterCategory.value || resource.category === filterCategory.value;
        return matchesSearch && matchesType && matchesCategory;
      });
    });

    // 计算属性 - 分页后的学习资源
    const paginatedResources = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      return filteredResources.value.slice(start, end);
    });

    // 计算属性 - 筛选后的考试
    const filteredExams = computed(() => {
      return exams.value.filter(exam => {
        const matchesSearch = exam.title.toLowerCase().includes(examSearchQuery.value.toLowerCase());
        const matchesStatus = !examFilterStatus.value || exam.status === examFilterStatus.value;
        return matchesSearch && matchesStatus;
      });
    });

    // 计算属性 - 分页后的考试
    const paginatedExams = computed(() => {
      const start = (examCurrentPage.value - 1) * examPageSize.value;
      const end = start + examPageSize.value;
      return filteredExams.value.slice(start, end);
    });

    // 统计数据
    const totalResources = computed(() => resources.value.length);
    const totalExams = computed(() => exams.value.length);
    const completedCourses = computed(() => resources.value.filter(r => r.status === 'published').length);
    const totalParticipants = computed(() => 256); // 模拟数据

    // 学习资源相关方法
    const handleResourceSearch = () => {
      currentPage.value = 1;
    };

    const handleResourceSizeChange = (newSize) => {
      pageSize.value = newSize;
    };

    const handleResourceCurrentChange = (newPage) => {
      currentPage.value = newPage;
    };

    const getResourceTypeTagType = (type) => {
      const typeMap = {
        article: 'info',
        video: 'success',
        audio: 'warning',
        document: 'primary'
      };
      return typeMap[type] || 'info';
    };

    const getResourceTypeName = (type) => {
      const typeMap = {
        article: '文章',
        video: '视频',
        audio: '音频',
        document: '文档'
      };
      return typeMap[type] || type;
    };

    const getResourceCategoryName = (category) => {
      const categoryMap = {
        theory: '理论学习',
        history: '党史教育',
        policy: '政策法规',
        model: '先进典型',
        integrity: '廉政教育'
      };
      return categoryMap[category] || category;
    };

    const showAddResourceDialog = () => {
      isEditResourceMode.value = false;
      currentResource.value = {
        id: null,
        title: '',
        type: 'article',
        category: 'theory',
        author: '',
        uploadDate: new Date().toISOString().split('T')[0],
        views: 0,
        status: 'published',
        url: '',
        description: '',
        content: ''
      };
      resourceDialogVisible.value = true;
    };

    const showEditResourceDialog = (resource) => {
      isEditResourceMode.value = true;
      currentResource.value = { ...resource };
      resourceDialogVisible.value = true;
    };

    const showResourceDetail = (resource) => {
      currentResource.value = { ...resource };
      resourceDetailVisible.value = true;
    };

    const handleResourceRowDoubleClick = (resource) => {
      showResourceDetail(resource);
    };

    const handleResourceStatusChange = (resource) => {
      // 这里可以添加保存状态的逻辑
      console.log('资源状态已更新:', resource);
    };

    const handleDeleteResource = (resource) => {
      const index = resources.value.findIndex(r => r.id === resource.id);
      if (index !== -1) {
        resources.value.splice(index, 1);
      }
    };

    const handleSaveResource = () => {
      if (isEditResourceMode.value) {
        // 编辑模式
        const index = resources.value.findIndex(r => r.id === currentResource.value.id);
        if (index !== -1) {
          resources.value[index] = { ...currentResource.value };
        }
      } else {
        // 添加模式
        const newResource = { ...currentResource.value };
        newResource.id = resources.value.length + 1;
        resources.value.push(newResource);
      }
      resourceDialogVisible.value = false;
    };

    // 考试相关方法
    const handleExamSearch = () => {
      examCurrentPage.value = 1;
    };

    const handleExamSizeChange = (newSize) => {
      examPageSize.value = newSize;
    };

    const handleExamCurrentChange = (newPage) => {
      examCurrentPage.value = newPage;
    };

    const getExamStatusTagType = (status) => {
      const statusMap = {
        not_started: 'warning',
        in_progress: 'success',
        completed: 'info'
      };
      return statusMap[status] || 'info';
    };

    const getExamStatusName = (status) => {
      const statusMap = {
        not_started: '未开始',
        in_progress: '进行中',
        completed: '已结束'
      };
      return statusMap[status] || status;
    };

    const showAddExamDialog = () => {
      isEditExamMode.value = false;
      currentExam.value = {
        id: null,
        title: '',
        category: 'theory',
        startTime: '',
        endTime: '',
        duration: 60,
        passScore: 60,
        status: 'not_started',
        description: ''
      };
      examDialogVisible.value = true;
    };

    const showEditExamDialog = (exam) => {
      isEditExamMode.value = true;
      currentExam.value = { ...exam };
      examDialogVisible.value = true;
    };

    const showExamDetail = (exam) => {
      currentExam.value = { ...exam };
      examDetailVisible.value = true;
    };

    const handleExamRowDoubleClick = (exam) => {
      showExamDetail(exam);
    };

    const handleDeleteExam = (exam) => {
      const index = exams.value.findIndex(e => e.id === exam.id);
      if (index !== -1) {
        exams.value.splice(index, 1);
      }
    };

    const handleSaveExam = () => {
      if (isEditExamMode.value) {
        // 编辑模式
        const index = exams.value.findIndex(e => e.id === currentExam.value.id);
        if (index !== -1) {
          exams.value[index] = { ...currentExam.value };
        }
      } else {
        // 添加模式
        const newExam = { ...currentExam.value };
        newExam.id = exams.value.length + 1;
        exams.value.push(newExam);
      }
      examDialogVisible.value = false;
    };

    return {
      activeTab,
      // 学习资源相关
      searchQuery,
      filterType,
      filterCategory,
      currentPage,
      pageSize,
      resourceDialogVisible,
      resourceDetailVisible,
      isEditResourceMode,
      currentResource,
      filteredResources,
      paginatedResources,
      handleResourceSearch,
      handleResourceSizeChange,
      handleResourceCurrentChange,
      getResourceTypeTagType,
      getResourceTypeName,
      getResourceCategoryName,
      showAddResourceDialog,
      showEditResourceDialog,
      showResourceDetail,
      handleResourceRowDoubleClick,
      handleResourceStatusChange,
      handleDeleteResource,
      handleSaveResource,
      // 考试相关
      examSearchQuery,
      examFilterStatus,
      examCurrentPage,
      examPageSize,
      examDialogVisible,
      examDetailVisible,
      isEditExamMode,
      currentExam,
      filteredExams,
      paginatedExams,
      handleExamSearch,
      handleExamSizeChange,
      handleExamCurrentChange,
      getExamStatusTagType,
      getExamStatusName,
      showAddExamDialog,
      showEditExamDialog,
      showExamDetail,
      handleExamRowDoubleClick,
      handleDeleteExam,
      handleSaveExam,
      // 统计数据
      totalResources,
      totalExams,
      completedCourses,
      totalParticipants
    };
  }
};
</script>

<style scoped>
.education-management {
  max-width: 1400px;
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

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #212529;
}

.stats-cards {
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border: 1px solid #e9ecef;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-top: 5px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  margin-left: auto;
  background-size: 30px;
  background-repeat: no-repeat;
  background-position: center;
}

.resource-icon {
  background-color: #e6f7ff;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%231890ff"><path d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 1.99 2H18c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z"/></svg>');
}

.exam-icon {
  background-color: #f6ffed;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%2352c41a"><path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 14h-2v-2h2v2zm3-4H9v-2h6v2zm0-4H9V7h6v2z"/></svg>');
}

.complete-icon {
  background-color: #fff7e6;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23fa8c16"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>');
}

.participant-icon {
  background-color: #fff0f6;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23eb2f96"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>');
}

.tabs-section {
  background-color: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  border: 1px solid #e9ecef;
}

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

.search-input {
  width: 100%;
}

.filter-select {
  width: 100%;
}

.add-btn {
  background-color: #c72c41;
  border-color: #c72c41;
}

.add-btn:hover {
  background-color: #a82336;
  border-color: #a82336;
}

/* 表格样式 */
.resources-table,
.exams-table {
  margin-bottom: 24px;
}

/* 表格容器样式 */
.table-container {
  overflow-x: auto;
  margin-bottom: 16px;
  position: relative;
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

.pagination {
  text-align: right;
  margin-top: 24px;
  padding: 16px 0;
}

.resource-detail,
.exam-detail {
  line-height: 1.6;
}

.resource-detail h3,
.exam-detail h3 {
  margin-bottom: 20px;
  color: #303133;
}

.resource-meta,
.exam-meta {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.meta-item {
  margin-right: 20px;
  color: #606266;
}

.resource-description,
.exam-description {
  margin-bottom: 20px;
}

.resource-description h4,
.exam-description h4 {
  margin-bottom: 10px;
  color: #303133;
}

.resource-content h4 {
  margin-bottom: 10px;
  color: #303133;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .education-management {
    padding: 20px 15px;
  }

  .page-title {
    font-size: 22px;
  }

  .stat-card {
    margin-bottom: 15px;
  }

  .filter-section {
    padding: 10px;
  }

  .filter-section .el-col {
    margin-bottom: 15px;
  }

  .add-btn {
    width: 100%;
  }

  .pagination {
    text-align: center;
    justify-content: center;
  }
}
</style>
