<template>
  <AdminLayout>
    <div class="exam-management">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>核心党务工作</el-breadcrumb-item>
        <el-breadcrumb-item>在线考试</el-breadcrumb-item>
      </el-breadcrumb>

      <div class="page-header">
      <h2>考试与知识竞赛管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="createExam">
          <el-icon>
            <Plus />
          </el-icon>
          新建考试
        </el-button>
        <el-button @click="importExams">批量导入</el-button>
        <el-button @click="exportExams">导出考试</el-button>
        <el-button type="success" @click="publishExams">发布考试</el-button>
      </div>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-number">{{ stats.totalExams }}</div>
          <div class="stat-label">考试总数</div>
        </div>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-number">{{ stats.draftExams }}</div>
          <div class="stat-label">草稿考试</div>
        </div>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-number">{{ stats.publishedExams }}</div>
          <div class="stat-label">已发布考试</div>
        </div>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-number">{{ stats.ongoingExams }}</div>
          <div class="stat-label">进行中考试</div>
        </div>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-number">{{ stats.completedExams }}</div>
          <div class="stat-label">已完成考试</div>
        </div>
      </el-card>
      <el-card shadow="hover" class="stat-card">
        <div class="stat-item">
          <div class="stat-number">{{ stats.participantCount }}</div>
          <div class="stat-label">总参与人数</div>
        </div>
      </el-card>
    </div>
    
    <!-- 搜索和筛选 -->
    <div class="search-filter">
      <el-input
        v-model="searchKey"
        placeholder="搜索考试名称/关键词"
        prefix-icon="Search"
        clearable
        class="search-input"
      />
      
      <el-select
        v-model="filterStatus"
        placeholder="筛选状态"
        clearable
        class="filter-select"
      >
        <el-option label="所有状态" value="" />
        <el-option label="草稿" value="draft" />
        <el-option label="已发布" value="published" />
        <el-option label="进行中" value="ongoing" />
        <el-option label="已完成" value="completed" />
        <el-option label="已过期" value="expired" />
      </el-select>
      
      <el-select
        v-model="filterType"
        placeholder="筛选考试类型"
        clearable
        class="filter-select"
      >
        <el-option label="所有类型" value="" />
        <el-option label="在线考试" value="exam" />
        <el-option label="知识竞赛" value="competition" />
        <el-option label="练习模式" value="practice" />
      </el-select>
      
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        class="date-picker"
      />
      
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>
    
    <!-- 考试列表 -->
    <div class="table-container-horizontal-scroll">
      <el-table
        :data="filteredExams"
        border
        style="width: 100%"
        :header-row-class-name="'header-row'"
        :row-class-name="'data-row'"
      >
        <el-table-column type="selection" width="65" />
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="title" label="考试名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="考试类型" width="120">
          <template #default="scope">
            <el-tag :type="getExamTypeTagType(scope.row.type)">
              {{ getExamTypeName(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="考试分类" width="150">
          <template #default="scope">
            <el-tag size="small" :type="getCategoryTagType(scope.row.category)">
              {{ getCategoryName(scope.row.category) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="duration" label="考试时长" width="150">
          <template #default="scope">
            {{ formatDuration(scope.row.duration) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalQuestions" label="题目数量" width="120" />
        <el-table-column prop="totalPoints" label="总分" width="100" />
        <el-table-column prop="passScore" label="及格分数" width="100" />
        <el-table-column prop="description" label="考试描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="participantCount" label="参与人数" width="120">
          <template #default="scope">
            <el-link type="primary" @click="viewParticipants(scope.row)">
              {{ scope.row.participantCount || 0 }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="passRate" label="通过率" width="120">
          <template #default="scope">
            <el-progress 
              :percentage="scope.row.passRate || 0" 
              :stroke-width="8" 
              :color="getPassRateColor(scope.row.passRate || 0)"
              :format="(percentage) => `${percentage}%`"
            />
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="scope">
            <el-space size="small">
              <el-button size="small" @click="viewExamDetail(scope.row)">
                <el-icon><View /></el-icon> 查看
              </el-button>
              <el-button type="primary" size="small" @click="editExam(scope.row)">
                <el-icon><Edit /></el-icon> 编辑
              </el-button>
              <el-button type="primary" size="small" @click="manageQuestions(scope.row)" 
                v-if="scope.row.status === 'draft'">
                <el-icon><Edit /></el-icon> 题目管理
              </el-button>
              <el-button type="success" size="small" @click="publishExam(scope.row)"
                v-if="scope.row.status === 'draft'">
                <el-icon><Check /></el-icon> 发布
              </el-button>
              <el-button type="warning" size="small" @click="stopExam(scope.row)"
                v-if="scope.row.status === 'published' || scope.row.status === 'ongoing'">
                <el-icon><Check /></el-icon> 停止
              </el-button>
              <el-button type="danger" size="small" @click="deleteExam(scope.row)">
                <el-icon><Delete /></el-icon> 删除
              </el-button>
            </el-space>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="filteredExams.length"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    
    <!-- 考试详情对话框 -->
    <el-dialog
      v-model="examDetailVisible"
      :title="selectedExam.title || '考试详情'"
      width="80%"
    >
      <div class="exam-detail">
        <div class="detail-header">
          <h3>{{ selectedExam.title }}</h3>
          <div class="detail-meta">
            <el-tag :type="getExamTypeTagType(selectedExam.type)">
              {{ getExamTypeName(selectedExam.type) }}
            </el-tag>
            <el-tag :type="getCategoryTagType(selectedExam.category)">
              {{ getCategoryName(selectedExam.category) }}
            </el-tag>
            <el-tag :type="getStatusTagType(selectedExam.status)">
              {{ getStatusName(selectedExam.status) }}
            </el-tag>
          </div>
        </div>
        
        <div class="detail-content">
          <el-row :gutter="20">
            <el-col :span="16">
              <el-card shadow="hover" class="info-card">
                <template #header>
                  <div class="card-header">
                    <span>考试基本信息</span>
                  </div>
                </template>
                <div class="info-section">
                  <div class="info-item">
                    <label>考试描述：</label>
                    <p>{{ selectedExam.description || '无描述' }}</p>
                  </div>
                  <div class="info-item">
                    <label>考试规则：</label>
                    <div v-html="formatRules(selectedExam.rules)" />
                  </div>
                  <div class="info-item">
                    <label>考试须知：</label>
                    <p>{{ selectedExam.instructions || '无须知' }}</p>
                  </div>
                  <div class="info-item">
                    <label>备注信息：</label>
                    <p>{{ selectedExam.remark || '无备注' }}</p>
                  </div>
                </div>
              </el-card>
              
              <el-card shadow="hover" class="info-card">
                <template #header>
                  <div class="card-header">
                    <span>题目信息</span>
                  </div>
                </template>
                <div class="question-stats">
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.totalQuestions || 0 }}</div>
                    <div class="stat-label">总题目数</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.singleChoiceCount || 0 }}</div>
                    <div class="stat-label">单选题</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.multipleChoiceCount || 0 }}</div>
                    <div class="stat-label">多选题</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.judgementCount || 0 }}</div>
                    <div class="stat-label">判断题</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.shortAnswerCount || 0 }}</div>
                    <div class="stat-label">简答题</div>
                  </div>
                </div>
                <el-button type="primary" size="small" @click="manageQuestions(selectedExam)" 
                  v-if="selectedExam.status === 'draft'">管理题目</el-button>
              </el-card>
              
              <el-card shadow="hover" class="info-card">
                <template #header>
                  <div class="card-header">
                    <span>考试结果统计</span>
                  </div>
                </template>
                <div class="result-stats">
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.participantCount || 0 }}</div>
                    <div class="stat-label">参与人数</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.passCount || 0 }}</div>
                    <div class="stat-label">通过人数</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.failCount || 0 }}</div>
                    <div class="stat-label">未通过人数</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.averageScore || 0 }}</div>
                    <div class="stat-label">平均分</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.highestScore || 0 }}</div>
                    <div class="stat-label">最高分</div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-number">{{ selectedExam.lowestScore || 0 }}</div>
                    <div class="stat-label">最低分</div>
                  </div>
                </div>
                <div class="result-chart">
                  <el-skeleton :rows="3" animated v-if="loadingChart" />
                  <div v-else class="chart-placeholder">
                    <el-empty description="考试成绩分布图表（实际项目中集成图表库）" />
                  </div>
                </div>
              </el-card>
            </el-col>
            
            <el-col :span="8">
              <el-card shadow="hover" class="info-card">
                <template #header>
                  <div class="card-header">
                    <span>考试时间信息</span>
                  </div>
                </template>
                <div class="info-section">
                  <div class="info-item">
                    <label>创建时间：</label>
                    <span>{{ selectedExam.createTime }}</span>
                  </div>
                  <div class="info-item">
                    <label>开始时间：</label>
                    <span>{{ selectedExam.startTime }}</span>
                  </div>
                  <div class="info-item">
                    <label>结束时间：</label>
                    <span>{{ selectedExam.endTime }}</span>
                  </div>
                  <div class="info-item">
                    <label>考试时长：</label>
                    <span>{{ formatDuration(selectedExam.duration) }}</span>
                  </div>
                  <div class="info-item">
                    <label>发布时间：</label>
                    <span>{{ selectedExam.publishTime || '未发布' }}</span>
                  </div>
                  <div class="info-item">
                    <label>状态：</label>
                    <el-tag :type="getStatusTagType(selectedExam.status)">
                      {{ getStatusName(selectedExam.status) }}
                    </el-tag>
                  </div>
                </div>
              </el-card>
              
              <el-card shadow="hover" class="info-card">
                <template #header>
                  <div class="card-header">
                    <span>考试设置</span>
                  </div>
                </template>
                <div class="info-section">
                  <div class="info-item">
                    <label>总分：</label>
                    <span>{{ selectedExam.totalPoints }} 分</span>
                  </div>
                  <div class="info-item">
                    <label>及格分数：</label>
                    <span>{{ selectedExam.passScore }} 分</span>
                  </div>
                  <div class="info-item">
                    <label>考试类型：</label>
                    <el-tag :type="getExamTypeTagType(selectedExam.type)">
                      {{ getExamTypeName(selectedExam.type) }}
                    </el-tag>
                  </div>
                  <div class="info-item">
                    <label>分类：</label>
                    <el-tag :type="getCategoryTagType(selectedExam.category)">
                      {{ getCategoryName(selectedExam.category) }}
                    </el-tag>
                  </div>
                  <div class="info-item">
                    <label>是否允许重考：</label>
                    <el-tag :type="selectedExam.allowRetake ? 'success' : 'warning'">
                      {{ selectedExam.allowRetake ? '是' : '否' }}
                    </el-tag>
                  </div>
                  <div class="info-item">
                    <label>重考次数限制：</label>
                    <span>{{ selectedExam.maxRetakes || 0 }} 次</span>
                  </div>
                  <div class="info-item">
                    <label>是否随机出题：</label>
                    <el-tag :type="selectedExam.randomQuestions ? 'success' : 'warning'">
                      {{ selectedExam.randomQuestions ? '是' : '否' }}
                    </el-tag>
                  </div>
                  <div class="info-item">
                    <label>是否随机选项：</label>
                    <el-tag :type="selectedExam.randomOptions ? 'success' : 'warning'">
                      {{ selectedExam.randomOptions ? '是' : '否' }}
                    </el-tag>
                  </div>
                </div>
              </el-card>
              
              <el-card shadow="hover" class="info-card">
                <template #header>
                  <div class="card-header">
                    <span>目标人群</span>
                  </div>
                </template>
                <div class="target-section">
                  <div class="target-item">
                    <label>目标人群：</label>
                    <el-tag :type="getTargetTagType(selectedExam.target)">
                      {{ getTargetName(selectedExam.target) }}
                    </el-tag>
                  </div>
                  <div class="target-item" v-if="selectedExam.departments && selectedExam.departments.length > 0">
                    <label>适用院系：</label>
                    <div class="tag-list">
                      <el-tag v-for="dept in selectedExam.departments" :key="dept" size="small">
                        {{ dept }}
                      </el-tag>
                    </div>
                  </div>
                  <div class="target-item" v-if="selectedExam.grades && selectedExam.grades.length > 0">
                    <label>适用年级：</label>
                    <div class="tag-list">
                      <el-tag v-for="grade in selectedExam.grades" :key="grade" size="small">
                        {{ grade }}
                      </el-tag>
                    </div>
                  </div>
                  <div class="target-item" v-if="selectedExam.majors && selectedExam.majors.length > 0">
                    <label>适用专业：</label>
                    <div class="tag-list">
                      <el-tag v-for="major in selectedExam.majors" :key="major" size="small">
                        {{ major }}
                      </el-tag>
                    </div>
                  </div>
                </div>
              </el-card>
              
              <el-card shadow="hover" class="info-card">
                <template #header>
                  <div class="card-header">
                    <span>通知设置</span>
                  </div>
                </template>
                <div class="notification-section">
                  <div class="notification-item">
                    <label>发送通知：</label>
                    <el-switch v-model="selectedExam.sendNotification" disabled />
                  </div>
                  <div class="notification-item">
                    <label>通知方式：</label>
                    <div class="tag-list">
                      <el-tag v-for="method in selectedExam.notificationMethods" :key="method" size="small">
                        {{ getNotificationMethodName(method) }}
                      </el-tag>
                    </div>
                  </div>
                  <div class="notification-item">
                    <label>通知时间：</label>
                    <span>{{ selectedExam.notificationTime || '考试发布时' }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="examDetailVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 新增/编辑考试对话框 -->
    <el-dialog
      v-model="examModalVisible"
      :title="isEdit ? '编辑考试' : '新建考试'"
      width="80%"
      fullscreen
    >
      <el-tabs v-model="activeTab">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            :model="examForm"
            :rules="examRules"
            ref="examFormRef"
            label-width="120px"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="考试名称" prop="title">
                  <el-input
                    v-model="examForm.title"
                    placeholder="请输入考试名称"
                    maxlength="100"
                    show-word-limit
                  />
                </el-form-item>
                
                <el-form-item label="考试描述" prop="description">
                  <el-input
                    v-model="examForm.description"
                    type="textarea"
                    rows="4"
                    placeholder="请输入考试描述"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
                
                <el-form-item label="考试类型" prop="type">
                  <el-select v-model="examForm.type" placeholder="请选择考试类型">
                    <el-option label="在线考试" value="exam" />
                    <el-option label="知识竞赛" value="competition" />
                    <el-option label="练习模式" value="practice" />
                  </el-select>
                </el-form-item>
                
                <el-form-item label="考试分类" prop="category">
                  <el-select v-model="examForm.category" placeholder="请选择考试分类">
                    <el-option label="党的理论" value="theory" />
                    <el-option label="党章党规" value="party_rules" />
                    <el-option label="党史知识" value="party_history" />
                    <el-option label="校史校情" value="school_history" />
                    <el-option label="师德师风" value="teacher_ethics" />
                    <el-option label="课程思政" value="course_ideology" />
                    <el-option label="其他" value="other" />
                  </el-select>
                </el-form-item>
              </el-col>
              
              <el-col :span="12">
                <el-form-item label="开始时间" prop="startTime">
                  <el-date-picker
                    v-model="examForm.startTime"
                    type="datetime"
                    placeholder="选择开始时间"
                    style="width: 100%"
                  />
                </el-form-item>
                
                <el-form-item label="结束时间" prop="endTime">
                  <el-date-picker
                    v-model="examForm.endTime"
                    type="datetime"
                    placeholder="选择结束时间"
                    style="width: 100%"
                  />
                </el-form-item>
                
                <el-form-item label="考试时长" prop="duration">
                  <el-input-number
                    v-model="examForm.duration"
                    :min="10"
                    :max="300"
                    :step="5"
                    placeholder="请输入考试时长（分钟）"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="考试规则" prop="rules">
              <el-input
                v-model="examForm.rules"
                type="textarea"
                rows="6"
                placeholder="请输入考试规则，支持HTML格式"
                maxlength="1000"
                show-word-limit
              />
            </el-form-item>
            
            <el-form-item label="考试须知" prop="instructions">
              <el-input
                v-model="examForm.instructions"
                type="textarea"
                rows="4"
                placeholder="请输入考试须知"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <!-- 考试设置 -->
        <el-tab-pane label="考试设置" name="settings">
          <el-form
            :model="examForm"
            :rules="examRules"
            ref="examFormRef"
            label-width="120px"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="总分" prop="totalPoints">
                  <el-input-number
                    v-model="examForm.totalPoints"
                    :min="10"
                    :max="1000"
                    :step="10"
                    placeholder="请输入考试总分"
                    style="width: 100%"
                  />
                </el-form-item>
                
                <el-form-item label="及格分数" prop="passScore">
                  <el-input-number
                    v-model="examForm.passScore"
                    :min="1"
                    :max="examForm.totalPoints"
                    :step="5"
                    placeholder="请输入及格分数"
                    style="width: 100%"
                  />
                </el-form-item>
                
                <el-form-item label="是否允许重考" prop="allowRetake">
                  <el-switch v-model="examForm.allowRetake" />
                </el-form-item>
                
                <el-form-item 
                  label="重考次数限制" 
                  prop="maxRetakes"
                  v-if="examForm.allowRetake"
                >
                  <el-input-number
                    v-model="examForm.maxRetakes"
                    :min="1"
                    :max="10"
                    :step="1"
                    placeholder="请输入重考次数限制"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              
              <el-col :span="12">
                <el-form-item label="是否随机出题" prop="randomQuestions">
                  <el-switch v-model="examForm.randomQuestions" />
                </el-form-item>
                
                <el-form-item label="是否随机选项" prop="randomOptions">
                  <el-switch v-model="examForm.randomOptions" />
                </el-form-item>
                
                <el-form-item label="是否显示答案" prop="showAnswers">
                  <el-switch v-model="examForm.showAnswers" />
                </el-form-item>
                
                <el-form-item label="是否显示解析" prop="showExplanations">
                  <el-switch v-model="examForm.showExplanations" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        
        <!-- 目标人群 -->
        <el-tab-pane label="目标人群" name="target">
          <el-form
            :model="examForm"
            :rules="examRules"
            ref="examFormRef"
            label-width="120px"
          >
            <el-form-item label="目标人群" prop="target">
              <el-select v-model="examForm.target" placeholder="请选择目标人群">
                <el-option label="教师党员" value="teacher" />
                <el-option label="学生党员" value="student" />
                <el-option label="积极分子" value="active" />
                <el-option label="发展对象" value="development" />
                <el-option label="全体党员" value="all" />
              </el-select>
            </el-form-item>
            
            <el-divider content-position="left">细分人群</el-divider>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="适用院系" prop="departments">
                  <el-select
                    v-model="examForm.departments"
                    multiple
                    placeholder="请选择适用院系"
                  >
                    <el-option label="计算机学院" value="computer" />
                    <el-option label="电子工程学院" value="electronics" />
                    <el-option label="机械工程学院" value="mechanical" />
                    <el-option label="经济管理学院" value="management" />
                    <el-option label="人文学院" value="humanities" />
                  </el-select>
                </el-form-item>
              </el-col>
              
              <el-col :span="12">
                <el-form-item label="适用专业" prop="majors">
                  <el-select
                    v-model="examForm.majors"
                    multiple
                    placeholder="请选择适用专业"
                  >
                    <el-option label="计算机科学与技术" value="cs" />
                    <el-option label="软件工程" value="se" />
                    <el-option label="电子信息工程" value="ee" />
                    <el-option label="机械设计制造及其自动化" value="md" />
                    <el-option label="市场营销" value="marketing" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="适用年级" prop="grades">
                  <el-select
                    v-model="examForm.grades"
                    multiple
                    placeholder="请选择适用年级"
                  >
                    <el-option label="2020级" value="2020" />
                    <el-option label="2021级" value="2021" />
                    <el-option label="2022级" value="2022" />
                    <el-option label="2023级" value="2023" />
                    <el-option label="2024级" value="2024" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        
        <!-- 通知设置 -->
        <el-tab-pane label="通知设置" name="notification">
          <el-form
            :model="examForm"
            :rules="examRules"
            ref="examFormRef"
            label-width="120px"
          >
            <el-form-item label="发送通知" prop="sendNotification">
              <el-switch v-model="examForm.sendNotification" />
            </el-form-item>
            
            <el-form-item 
              label="通知方式" 
              prop="notificationMethods"
              v-if="examForm.sendNotification"
            >
              <el-select v-model="examForm.notificationMethods" placeholder="请选择通知方式" multiple>
                <el-option label="系统消息" value="system" />
                <el-option label="邮件" value="email" />
                <el-option label="短信" value="sms" />
              </el-select>
            </el-form-item>
            
            <el-form-item 
              label="通知时间" 
              prop="notificationTime"
              v-if="examForm.sendNotification"
            >
              <el-radio-group v-model="examForm.notificationTimeType">
                <el-radio label="publish">考试发布时</el-radio>
                <el-radio label="start">考试开始前</el-radio>
                <el-radio label="custom">自定义时间</el-radio>
              </el-radio-group>
              
              <el-select
                v-if="examForm.notificationTimeType === 'start'"
                v-model="examForm.notificationBeforeStart"
                placeholder="提前通知时间"
                style="margin-top: 10px; margin-right: 10px; width: 150px"
              >
                <el-option label="15分钟" value="15" />
                <el-option label="30分钟" value="30" />
                <el-option label="1小时" value="60" />
                <el-option label="2小时" value="120" />
                <el-option label="1天" value="1440" />
              </el-select>
              <el-date-picker
                v-if="examForm.notificationTimeType === 'custom'"
                v-model="examForm.notificationTime"
                type="datetime"
                placeholder="选择通知时间"
                style="margin-top: 10px; width: 100%"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="examModalVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveExam">保存</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 参与者列表对话框 -->
    <el-dialog
      v-model="participantsVisible"
      :title="participantsTitle"
      width="90%"
    >
      <div class="participant-list-container">
        <el-input
          v-model="participantSearchKey"
          placeholder="搜索用户姓名/学号/工号"
          prefix-icon="Search"
          clearable
          class="search-input"
        />
        
        <el-table
          :data="filteredParticipants"
          border
          stripe
          style="width: 100%"
        >
          <el-table-column type="selection" width="65" />
          <el-table-column prop="id" label="用户ID" width="100" />
          <el-table-column prop="name" label="姓名" width="120" />
          <el-table-column prop="userType" label="用户类型" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.userType === 'teacher' ? 'primary' : 'success'">
                {{ scope.row.userType === 'teacher' ? '教师' : '学生' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="department" label="院系" width="150" />
          <el-table-column prop="major" label="专业" width="150" />
          <el-table-column prop="grade" label="年级" width="100" />
          <el-table-column prop="studentId" label="学号/工号" width="150" />
          <el-table-column prop="examStatus" label="考试状态" width="120">
            <template #default="scope">
              <el-tag :type="getUserExamStatusTagType(scope.row.examStatus)">
                {{ getUserExamStatusName(scope.row.examStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="得分" width="100" />
          <el-table-column prop="rank" label="排名" width="100" />
          <el-table-column prop="examStartTime" label="开始时间" width="180" />
          <el-table-column prop="examEndTime" label="结束时间" width="180" />
          <el-table-column prop="duration" label="答题时长" width="120">
            <template #default="scope">
              {{ formatDuration(scope.row.duration) }}
            </template>
          </el-table-column>
          <el-table-column prop="retakeCount" label="重考次数" width="120" />
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button size="small" @click="viewAnswerSheet(scope.row)">查看答卷</el-button>
              <el-button type="primary" size="small" @click="sendReminder(scope.row)" 
                v-if="scope.row.examStatus !== 'completed'">发送提醒</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination">
          <el-pagination
            :current-page="participantCurrentPage"
            :page-size="participantPageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredParticipants.length"
            @size-change="handleParticipantSizeChange"
            @current-change="handleParticipantCurrentChange"
          />
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="participantsVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, computed, onMounted, onActivated, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import AdminLayout from '@/components/AdminLayout.vue'
import { ElMessage, ElMessageBox, ElSkeleton, ElEmpty, ElTabs, ElTabPane } from 'element-plus'
import { Plus, View, Edit, Delete, Check } from '@element-plus/icons-vue'
import * as examApi from '../../api/exam'
import { mockExams } from '../../mock/data'

// 路由实例
const router = useRouter()

// 组件数据
const exams = ref([])
const stats = ref({
  totalExams: 0,
  draftExams: 0,
  publishedExams: 0,
  ongoingExams: 0,
  completedExams: 0,
  participantCount: 0
})
const currentPage = ref(1)
const pageSize = ref(20)
const searchKey = ref('')
const filterStatus = ref('')
const filterType = ref('')
const dateRange = ref([])

// 模态框控制
const examDetailVisible = ref(false)
const examModalVisible = ref(false)
const participantsVisible = ref(false)
const participantsTitle = ref('')
const isEdit = ref(false)
const loadingChart = ref(false)
const activeTab = ref('basic')

// 表单引用
const examFormRef = ref(null)

// 选中的考试
const selectedExam = ref({})

// 参与者相关
const participants = ref([])
const participantSearchKey = ref('')
const participantCurrentPage = ref(1)
const participantPageSize = ref(20)

// 表单数据
const examForm = ref({
  id: null,
  title: '',
  description: '',
  type: 'exam',
  category: 'theory',
  status: 'draft',
  startTime: new Date(),
  endTime: new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000), // 默认7天后结束
  duration: 120, // 默认120分钟
  rules: '',
  instructions: '',
  totalPoints: 100,
  passScore: 60,
  totalQuestions: 0,
  singleChoiceCount: 0,
  multipleChoiceCount: 0,
  judgementCount: 0,
  shortAnswerCount: 0,
  allowRetake: false,
  maxRetakes: 0,
  randomQuestions: false,
  randomOptions: false,
  showAnswers: false,
  showExplanations: false,
  target: '',
  departments: [],
  grades: [],
  majors: [],
  sendNotification: true,
  notificationMethods: ['system'],
  notificationTimeType: 'publish',
  notificationBeforeStart: '60',
  notificationTime: null,
  creator: '当前用户',
  createTime: '',
  publishTime: null,
  participantCount: 0,
  passCount: 0,
  failCount: 0,
  passRate: 0,
  averageScore: 0,
  highestScore: 0,
  lowestScore: 0,
  remark: ''
})

// 表单验证规则
const examRules = {
  title: [
    { required: true, message: '请输入考试名称', trigger: 'blur' },
    { min: 5, max: 100, message: '标题长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入考试描述', trigger: 'blur' },
    { min: 10, max: 500, message: '描述长度在 10 到 500 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择考试类型', trigger: 'change' }
  ],
  category: [
    { required: true, message: '请选择考试分类', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  duration: [
    { required: true, message: '请输入考试时长', trigger: 'blur' },
    { min: 10, max: 300, message: '考试时长应在10-300分钟之间', trigger: 'blur' }
  ],
  totalPoints: [
    { required: true, message: '请输入考试总分', trigger: 'blur' },
    { min: 10, max: 1000, message: '总分应在10-1000分之间', trigger: 'blur' }
  ],
  passScore: [
    { required: true, message: '请输入及格分数', trigger: 'blur' },
    { max: examForm.value.totalPoints, message: '及格分数不能超过总分', trigger: 'blur' }
  ],
  target: [
    { required: true, message: '请选择目标人群', trigger: 'change' }
  ]
}

// 过滤后的考试列表
const filteredExams = computed(() => {
  let result = [...exams.value]
  
  // 按搜索关键词过滤
  if (searchKey.value) {
    const key = searchKey.value.toLowerCase()
    result = result.filter(item => 
      item.title.toLowerCase().includes(key) ||
      item.description.toLowerCase().includes(key)
    )
  }
  
  // 按状态过滤
  if (filterStatus.value) {
    result = result.filter(item => item.status === filterStatus.value)
  }
  
  // 按类型过滤
  if (filterType.value) {
    result = result.filter(item => item.type === filterType.value)
  }
  
  // 按日期范围过滤
  if (dateRange.value && dateRange.value.length === 2) {
    const startDate = dateRange.value[0]
    const endDate = dateRange.value[1]
    result = result.filter(item => {
      const createDate = new Date(item.createTime)
      return createDate >= startDate && createDate <= endDate
    })
  }
  
  // 按ID升序排序，确保ID列有序显示
  result.sort((a, b) => a.id - b.id)
  
  return result
})

// 过滤后的参与者列表
const filteredParticipants = computed(() => {
  let result = [...participants.value]
  
  // 按搜索关键词过滤
  if (participantSearchKey.value) {
    const key = participantSearchKey.value.toLowerCase()
    result = result.filter(item => 
      item.name.toLowerCase().includes(key) ||
      item.studentId.toLowerCase().includes(key)
    )
  }
  
  return result
})

// 获取考试类型名称
const getExamTypeName = (type) => {
  const typeMap = {
    'exam': '在线考试',
    'competition': '知识竞赛',
    'practice': '练习模式'
  }
  return typeMap[type] || type
}

// 获取考试类型标签类型
const getExamTypeTagType = (type) => {
  const typeMap = {
    'exam': 'primary',
    'competition': 'danger',
    'practice': 'success'
  }
  return typeMap[type] || 'info'
}

// 获取分类名称
const getCategoryName = (category) => {
  const categoryMap = {
    'theory': '党的理论',
    'party_rules': '党章党规',
    'party_history': '党史知识',
    'school_history': '校史校情',
    'teacher_ethics': '师德师风',
    'course_ideology': '课程思政',
    'other': '其他'
  }
  return categoryMap[category] || category
}

// 获取分类标签类型
const getCategoryTagType = (category) => {
  const categoryMap = {
    'theory': 'primary',
    'party_rules': 'success',
    'party_history': 'warning',
    'school_history': 'info',
    'teacher_ethics': 'danger',
    'course_ideology': 'purple',
    'other': 'info'
  }
  return categoryMap[category] || 'info'
}

// 获取状态名称
const getStatusName = (status) => {
  switch (status) {
    case 'draft': return '草稿'
    case 'published': return '已发布'
    case 'ongoing': return '进行中'
    case 'completed': return '已完成'
    case 'expired': return '已过期'
    default: return status
  }
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  switch (status) {
    case 'draft': return 'info'
    case 'published': return 'success'
    case 'ongoing': return 'primary'
    case 'completed': return 'success'
    case 'expired': return 'warning'
    default: return 'info'
  }
}

// 获取目标人群名称
const getTargetName = (target) => {
  const targetMap = {
    'teacher': '教师党员',
    'student': '学生党员',
    'active': '积极分子',
    'development': '发展对象',
    'all': '全体党员'
  }
  return targetMap[target] || target
}

// 获取目标人群标签类型
const getTargetTagType = (target) => {
  const targetMap = {
    'teacher': 'primary',
    'student': 'success',
    'active': 'warning',
    'development': 'info',
    'all': 'danger'
  }
  return targetMap[target] || 'info'
}

// 获取通知方式名称
const getNotificationMethodName = (method) => {
  const methodMap = {
    'system': '系统消息',
    'email': '邮件',
    'sms': '短信'
  }
  return methodMap[method] || method
}

// 获取用户考试状态名称
const getUserExamStatusName = (status) => {
  switch (status) {
    case 'not_started': return '未开始'
    case 'in_progress': return '进行中'
    case 'completed': return '已完成'
    case 'expired': return '已过期'
    case 'abnormal': return '异常'
    default: return status
  }
}

// 获取用户考试状态标签类型
const getUserExamStatusTagType = (status) => {
  switch (status) {
    case 'not_started': return 'info'
    case 'in_progress': return 'primary'
    case 'completed': return 'success'
    case 'expired': return 'warning'
    case 'abnormal': return 'danger'
    default: return 'info'
  }
}

// 获取通过率颜色
const getPassRateColor = (rate) => {
  if (rate >= 80) return '#67c23a'
  if (rate >= 60) return '#e6a23c'
  return '#f56c6c'
}

// 格式化时长
const formatDuration = (minutes) => {
  if (!minutes || minutes === 0) return '0分钟'
  if (minutes < 60) return `${minutes}分钟`
  const hours = Math.floor(minutes / 60)
  const mins = minutes % 60
  return `${hours}小时${mins}分钟`
}

// 格式化规则
const formatRules = (rules) => {
  if (!rules) return ''
  return rules.replace(/\n/g, '<br>')
}

// 加载考试列表
const loadExams = async () => {
  try {
    // 实际项目中调用API获取数据
    // const response = await examApi.getExams()
    // exams.value = response.data
    // stats.value = response.stats
    
    // 使用模拟数据
    exams.value = mockExams
    updateStats()
    
    console.log('Exams loaded:', exams.value)
    
    await nextTick()
  } catch (error) {
    console.error('Failed to load exams:', error)
    ElMessage.error('加载考试列表失败')
  }
}

// 更新容器宽度
// 更新统计数据
const updateStats = () => {
  const total = exams.value.length
  const draft = exams.value.filter(e => e.status === 'draft').length
  const published = exams.value.filter(e => e.status === 'published').length
  const ongoing = exams.value.filter(e => e.status === 'ongoing').length
  const completed = exams.value.filter(e => e.status === 'completed').length
  const participants = exams.value.reduce((sum, e) => sum + (e.participantCount || 0), 0)
  
  stats.value = {
    totalExams: total,
    draftExams: draft,
    publishedExams: published + ongoing,
    ongoingExams: ongoing,
    completedExams: completed,
    participantCount: participants
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  console.log('Searching with key:', searchKey.value)
}

// 重置
const handleReset = () => {
  searchKey.value = ''
  filterStatus.value = ''
  filterType.value = ''
  dateRange.value = []
  currentPage.value = 1
}

// 分页大小变化
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
}

// 当前页变化
const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

// 查看考试详情
const viewExamDetail = (exam) => {
  selectedExam.value = { ...exam }
  loadingChart.value = true
  
  // 模拟加载图表
  setTimeout(() => {
    loadingChart.value = false
  }, 1500)
  
  examDetailVisible.value = true
}

// 创建考试
const createExam = () => {
  isEdit.value = false
  activeTab.value = 'basic'
  examForm.value = {
    id: null,
    title: '',
    description: '',
    type: 'exam',
    category: 'theory',
    status: 'draft',
    startTime: new Date(),
    endTime: new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000),
    duration: 120,
    rules: '',
    instructions: '',
    totalPoints: 100,
    passScore: 60,
    totalQuestions: 0,
    singleChoiceCount: 0,
    multipleChoiceCount: 0,
    judgementCount: 0,
    shortAnswerCount: 0,
    allowRetake: false,
    maxRetakes: 0,
    randomQuestions: false,
    randomOptions: false,
    showAnswers: false,
    showExplanations: false,
    target: '',
    departments: [],
    grades: [],
    majors: [],
    sendNotification: true,
    notificationMethods: ['system'],
    notificationTimeType: 'publish',
    notificationBeforeStart: '60',
    notificationTime: null,
    creator: '当前用户',
    createTime: '',
    publishTime: null,
    participantCount: 0,
    passCount: 0,
    failCount: 0,
    passRate: 0,
    averageScore: 0,
    highestScore: 0,
    lowestScore: 0,
    remark: ''
  }
  examModalVisible.value = true
}

// 编辑考试
const editExam = (exam) => {
  isEdit.value = true
  activeTab.value = 'basic'
  examForm.value = { ...exam }
  examModalVisible.value = true
}

// 保存考试
const handleSaveExam = async () => {
  if (!examFormRef.value) return
  
  await examFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 实际项目中调用API保存数据
        // if (isEdit.value) {
        //   await examApi.updateExam(examForm.value)
        // } else {
        //   await examApi.createExam(examForm.value)
        // }
        
        // 模拟保存
        if (isEdit.value) {
          // 更新现有考试
          const index = exams.value.findIndex(e => e.id === examForm.value.id)
          if (index !== -1) {
            exams.value[index] = { ...examForm.value }
          }
        } else {
          // 新增考试
          const newExam = { 
            ...examForm.value, 
            id: Date.now(),
            createTime: new Date(),
            participantCount: Math.floor(Math.random() * 100) + 50,
            passCount: Math.floor(Math.random() * 50),
            failCount: Math.floor(Math.random() * 20),
            passRate: Math.floor(Math.random() * 100),
            averageScore: Math.floor(Math.random() * 40) + 60,
            highestScore: Math.floor(Math.random() * 20) + 80,
            lowestScore: Math.floor(Math.random() * 30) + 30
          }
          exams.value.push(newExam)
        }
        
        ElMessage.success(isEdit.value ? '考试更新成功' : '考试创建成功')
        examModalVisible.value = false
        updateStats()
      } catch (error) {
        console.error('Failed to save exam:', error)
        ElMessage.error(isEdit.value ? '更新考试失败' : '创建考试失败')
      }
    }
  })
}

// 删除考试
const deleteExam = (exam) => {
  ElMessageBox.confirm('确定要删除这条考试吗？删除后无法恢复。', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(async () => {
    try {
      // 实际项目中调用API删除数据
      // await examApi.deleteExam(exam.id)
      
      // 模拟删除
      const index = exams.value.findIndex(e => e.id === exam.id)
      if (index !== -1) {
        exams.value.splice(index, 1)
      }
      
      ElMessage.success('考试删除成功')
      updateStats()
    } catch (error) {
      console.error('Failed to delete exam:', error)
      ElMessage.error('删除考试失败')
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 发布考试
const publishExam = async (exam) => {
  ElMessageBox.confirm('确定要发布这条考试吗？发布后将无法修改题目和基本设置。', '发布确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 实际项目中调用API发布数据
      // await examApi.publishExam(exam.id)
      
      // 模拟发布
      const index = exams.value.findIndex(e => e.id === exam.id)
      if (index !== -1) {
        exams.value[index].status = 'published'
        exams.value[index].publishTime = new Date()
      }
      
      ElMessage.success('考试发布成功')
      updateStats()
    } catch (error) {
      console.error('Failed to publish exam:', error)
      ElMessage.error('发布考试失败')
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 停止考试
const stopExam = async (exam) => {
  ElMessageBox.confirm('确定要停止这条考试吗？停止后考生将无法继续答题。', '停止确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 实际项目中调用API停止数据
      // await examApi.stopExam(exam.id)
      
      // 模拟停止
      const index = exams.value.findIndex(e => e.id === exam.id)
      if (index !== -1) {
        exams.value[index].status = 'completed'
      }
      
      ElMessage.success('考试停止成功')
      updateStats()
    } catch (error) {
      console.error('Failed to stop exam:', error)
      ElMessage.error('停止考试失败')
    }
  }).catch(() => {
    // 用户取消操作
  })
}

// 查看参与者
const viewParticipants = (exam) => {
  selectedExam.value = { ...exam }
  participantsTitle.value = `${exam.title} - 参与者列表`
  
  // 模拟加载参与者列表
  participants.value = Array.from({ length: 50 }, (_, i) => ({
    id: `user_${i}`,
    name: `用户${i + 1}`,
    userType: i % 2 === 0 ? 'student' : 'teacher',
    department: '计算机学院',
    major: '计算机科学与技术',
    grade: '2022级',
    studentId: i % 2 === 0 ? `2022000${i + 1}` : `T202200${i + 1}`,
    examStatus: ['not_started', 'in_progress', 'completed', 'expired'][Math.floor(Math.random() * 4)],
    score: Math.floor(Math.random() * 100),
    rank: i + 1,
    examStartTime: new Date(),
    examEndTime: new Date(),
    duration: Math.floor(Math.random() * 120),
    retakeCount: Math.floor(Math.random() * 2)
  }))
  
  participantsVisible.value = true
}

// 管理题目
const manageQuestions = (exam) => {
  router.push({ path: '/admin/questions' })
}

// 查看答卷
const viewAnswerSheet = (user) => {
  ElMessage.info('查看答卷功能开发中')
}

// 发送提醒
const sendReminder = (user) => {
  ElMessage.success(`已向 ${user.name} 发送考试提醒`)
}

// 批量导入
const importExams = () => {
  ElMessage.info('批量导入功能开发中')
}

// 导出考试
const exportExams = () => {
  ElMessage.info('导出考试功能开发中')
}

// 发布考试（批量）
const publishExams = () => {
  ElMessage.info('批量发布功能开发中')
}

// 分页大小变化（参与者）
const handleParticipantSizeChange = (newSize) => {
  participantPageSize.value = newSize
  participantCurrentPage.value = 1
}

// 当前页变化（参与者）
const handleParticipantCurrentChange = (newPage) => {
  participantCurrentPage.value = newPage
}

// 页面加载时获取数据
onMounted(async () => {
  await loadExams()
})

// 路由切换时重新加载数据（处理组件缓存问题）
onActivated(async () => {
  await loadExams()
})
</script>

<style scoped>
.exam-management {
  margin: 0 auto;
  padding: 24px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  width: 100%;
}

/* 面包屑样式 */
.breadcrumb {
  margin-bottom: 16px;
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

.table-container {
  width: 100%;
  overflow-x: auto;
  margin-bottom: 16px;
  position: relative;
  min-width: 1200px;
  box-sizing: border-box;
}

.virtual-scroll-wrapper {
  position: relative;
  overflow: hidden;
}

.virtual-scroll-content {
  display: flex;
  flex-direction: column;
  transition: transform 0s;
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

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.stats-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.stat-card {
  flex: 1;
  min-width: 180px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  color: #606266;
}

.search-filter {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 20px;
  padding: 16px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
}

.search-input {
  width: 300px;
  margin-right: 10px;
}

.filter-select {
  width: 150px;
  margin-right: 10px;
}

.date-picker {
  width: 300px;
  margin-right: 10px;
}

.table-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.05);
  padding: 0;
  overflow-x: auto;
  margin-bottom: 16px;
  position: relative;
  width: 100%;
  -webkit-overflow-scrolling: touch;
  scroll-behavior: smooth;
  border: 1px solid #e9ecef;
  min-height: 200px;
  min-width: 1200px;
}

/* 优化滚动条样式 */
.table-container::-webkit-scrollbar {
  height: 10px;
}

.table-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 5px;
  margin: 0 8px;
}

.table-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 5px;
  border: 2px solid #f1f1f1;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 确保表格在容器内正确显示 */
.table-container :deep(.el-table) {
  width: auto;
  min-width: 100%;
  border: none;
}

/* 确保表格头部在滚动时保持可见 */
.table-container :deep(.el-table__header-wrapper) {
  position: sticky;
  top: 0;
  z-index: 10;
  background-color: #fff;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

/* 考试详情样式 */
.exam-detail {
  max-height: calc(100vh - 150px);
  overflow-y: auto;
  padding: 20px;
}

.detail-header {
  margin-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 20px;
}

.detail-header h3 {
  margin: 0 0 15px 0;
  font-size: 24px;
  color: #333;
}

.detail-meta {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item label {
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.info-item p,
.info-item span {
  color: #606266;
  line-height: 1.6;
}

.question-stats {
  margin-top: 20px;
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.question-stats .stat-item {
  display: inline-block;
  text-align: center;
}

.question-stats .stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.question-stats .stat-label {
  color: #606266;
  font-size: 14px;
}

.result-stats {
  margin-top: 20px;
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.result-stats .stat-item {
  display: inline-block;
  text-align: center;
}

.result-stats .stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #f56c6c;
}

.result-stats .stat-label {
  color: #606266;
  font-size: 14px;
}

.result-chart {
  margin-top: 20px;
  height: 300px;
  background-color: #f9f9f9;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
}

.chart-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 目标人群样式 */
.target-section {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.target-item {
  display: flex;
  flex-direction: column;
}

.target-item label {
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.target-item .tag-list {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

/* 通知设置样式 */
.notification-section {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.notification-item {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.notification-item label {
  font-weight: bold;
  color: #333;
  min-width: 80px;
}

/* 参与者列表样式 */
.participant-list-container {
  max-height: calc(100vh - 200px);
  overflow-y: auto;
  padding: 20px;
}

@media (max-width: 1200px) {
  .search-filter {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-input, .filter-select, .date-picker {
    width: 100% !important;
    margin-bottom: 10px;
  }
  
  .stats-cards {
    flex-direction: column;
  }
  
  .stat-card {
    min-width: auto;
  }
}



/* 水平虚拟滚动容器样式 */
.table-container-horizontal-scroll {
  width: 100%;
  overflow-x: auto;
  position: relative;
  max-width: 100%;
  scrollbar-width: thin;
  scrollbar-color: #409eff #f0f0f0;
  /* 固定最大宽度，确保容器有固定长度 */
  max-width: 1700px;
  /* 添加阴影效果，增强视觉层次感 */
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  /* 添加过渡效果，提升用户体验 */
  transition: box-shadow 0.3s ease;
}

/* 容器悬停效果 */
.table-container-horizontal-scroll:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

/* 滚动指示器样式 */
.table-container-horizontal-scroll::before,
.table-container-horizontal-scroll::after {
  content: '';
  position: absolute;
  top: 0;
  bottom: 0;
  width: 20px;
  pointer-events: none;
  z-index: 1;
}

/* 左侧阴影指示器 */
.table-container-horizontal-scroll::before {
  left: 0;
  background: linear-gradient(to right, rgba(255, 255, 255, 0.8), transparent);
}

/* 右侧阴影指示器 */
.table-container-horizontal-scroll::after {
  right: 0;
  background: linear-gradient(to left, rgba(255, 255, 255, 0.8), transparent);
}

/* 自定义滚动条样式 */
.table-container-horizontal-scroll::-webkit-scrollbar {
  height: 8px;
}

.table-container-horizontal-scroll::-webkit-scrollbar-track {
  background: #f0f0f0;
  border-radius: 4px;
}

.table-container-horizontal-scroll::-webkit-scrollbar-thumb {
  background: #409eff;
  border-radius: 4px;
}

.table-container-horizontal-scroll::-webkit-scrollbar-thumb:hover {
  background: #66b1ff;
}

/* 表格行样式 */
.header-row, .data-row {
  display: table;
  width: 100%;
  table-layout: fixed;
}

@media (max-width: 768px) {
  .exam-management {
    padding: 10px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .header-actions {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .question-stats, .result-stats {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .question-stats .stat-item, .result-stats .stat-item {
    text-align: left;
  }
}
</style>