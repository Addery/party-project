<template>
  <AdminLayout>
    <div class="interaction-management theme-party-day">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item><a href="#/back/dashboard">首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>互动与民主管理</el-breadcrumb-item>
        <el-breadcrumb-item>互动管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 页面标题 -->
      <div class="page-header">
        <h2>互动与民主管理</h2>
      </div>

      <!-- 功能标签页 -->
      <el-tabs v-model="activeTab" class="interaction-tabs">
        <!-- 民主评议标签页 -->
        <el-tab-pane label="民主评议管理" name="democratic-evaluation">
          <div class="evaluation-section">
            <!-- 统计卡片 -->
            <el-row :gutter="20" class="stats-row">
              <el-col :xs="24" :sm="12" :md="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="card-content">
                    <div class="card-title">评议总数</div>
                    <div class="card-value">{{ totalEvaluations }}</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12" :md="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="card-content">
                    <div class="card-title">待处理</div>
                    <div class="card-value">{{ pendingEvaluations }}</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12" :md="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="card-content">
                    <div class="card-title">已完成</div>
                    <div class="card-value">{{ completedEvaluations }}</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12" :md="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="card-content">
                    <div class="card-title">参与率</div>
                    <div class="card-value">{{ participationRate }}%</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>

            <!-- 操作按钮 -->
            <div class="action-buttons">
              <el-button type="primary" @click="showCreateEvaluationDialog">
                <el-icon><Plus /></el-icon> 新建评议
              </el-button>
              <el-button @click="exportEvaluationData">导出数据</el-button>
              <el-button type="warning" @click="batchReview">批量审核</el-button>
            </div>

            <!-- 搜索筛选 -->
            <div class="filter-section">
              <el-row :gutter="20">
                <el-col :xs="24" :sm="12" :md="8">
                  <el-input
                    v-model="evaluationSearchQuery"
                    placeholder="搜索评议主题"
                    clearable
                    class="search-input"
                  >
                    <template #append>
                      <el-button @click="handleEvaluationSearch">
                        <el-icon><Search /></el-icon>
                      </el-button>
                    </template>
                  </el-input>
                </el-col>
                <el-col :xs="24" :sm="12" :md="6">
                  <el-select
                    v-model="evaluationStatusFilter"
                    placeholder="筛选状态"
                    clearable
                    class="filter-select"
                  >
                    <el-option label="全部状态" value=""></el-option>
                    <el-option label="待处理" value="pending"></el-option>
                    <el-option label="已完成" value="completed"></el-option>
                    <el-option label="已关闭" value="closed"></el-option>
                  </el-select>
                </el-col>
                <el-col :xs="24" :sm="12" :md="6">
                  <el-date-picker
                    v-model="evaluationDateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    class="filter-datepicker"
                  ></el-date-picker>
                </el-col>
              </el-row>
            </div>

            <!-- 评议列表 -->
            <div class="table-container">
              <el-table
                :data="paginatedEvaluations"
                border
                style="width: 100%"
                @selection-change="handleEvaluationSelectionChange"
                @row-dblclick="handleEvaluationRowDoubleClick"
              >
                <el-table-column type="selection" width="65"></el-table-column>
                <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
                <el-table-column prop="title" label="评议主题" min-width="200" show-overflow-tooltip></el-table-column>
                <el-table-column prop="type" label="评议类型" width="120" align="center">
                  <template #default="scope">
                    <el-tag :type="getEvaluationTypeTagType(scope.row.type)">
                      {{ getEvaluationTypeName(scope.row.type) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="participants" label="参与人数" width="120" align="center"></el-table-column>
                <el-table-column prop="responseRate" label="响应率" width="130" align="center">
                  <template #default="scope">
                    <el-progress
                      type="circle"
                      :percentage="scope.row.responseRate"
                      :width="80"
                      :stroke-width="6"
                      :show-text="true"
                      :stroke-color="getResponseRateColor(scope.row.responseRate)"
                    ></el-progress>
                  </template>
                </el-table-column>
                <el-table-column prop="startDate" label="开始日期" width="150" align="center"></el-table-column>
                <el-table-column prop="endDate" label="结束日期" width="150" align="center"></el-table-column>
                <el-table-column prop="status" label="状态" width="120" align="center">
                  <template #default="scope">
                    <el-tag :type="getEvaluationStatusTagType(scope.row.status)">
                      {{ getEvaluationStatusName(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="320" fixed="right">
                  <template #default="scope">
                    <el-space size="small">
                      <el-button size="small" @click="viewEvaluationDetail(scope.row)">
                        <el-icon><View /></el-icon> 查看
                      </el-button>
                      <el-button size="small" type="primary" @click="editEvaluation(scope.row)">
                        <el-icon><Edit /></el-icon> 编辑
                      </el-button>
                      <el-button size="small" type="success" @click="exportEvaluationResults(scope.row)">
                        <el-icon><Download /></el-icon> 导出结果
                      </el-button>
                      <el-button size="small" type="danger" @click="deleteEvaluation(scope.row)">
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
                v-model:current-page="evaluationCurrentPage"
                v-model:page-size="evaluationPageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="filteredEvaluations.length"
                @size-change="handleEvaluationSizeChange"
                @current-change="handleEvaluationCurrentChange"
              ></el-pagination>
            </div>
          </div>
        </el-tab-pane>

        <!-- 意见箱标签页 -->
        <el-tab-pane label="意见箱管理" name="suggestion-box">
          <div class="suggestion-section">
            <!-- 统计卡片 -->
            <el-row :gutter="20" class="stats-row">
              <el-col :xs="24" :sm="12" :md="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="card-content">
                    <div class="card-title">意见总数</div>
                    <div class="card-value">{{ totalSuggestions }}</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12" :md="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="card-content">
                    <div class="card-title">待处理</div>
                    <div class="card-value">{{ pendingSuggestions }}</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12" :md="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="card-content">
                    <div class="card-title">已处理</div>
                    <div class="card-value">{{ processedSuggestions }}</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12" :md="6">
                <el-card shadow="hover" class="stat-card">
                  <div class="card-content">
                    <div class="card-title">已回复</div>
                    <div class="card-value">{{ repliedSuggestions }}</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>

            <!-- 操作按钮 -->
            <div class="action-buttons">
              <el-button type="primary" @click="exportSuggestionData">导出数据</el-button>
              <el-button type="success" @click="batchReply">批量回复</el-button>
              <el-button type="warning" @click="batchProcess">批量处理</el-button>
            </div>

            <!-- 搜索筛选 -->
            <div class="filter-section">
              <el-row :gutter="20">
                <el-col :xs="24" :sm="12" :md="8">
                  <el-input
                    v-model="suggestionSearchQuery"
                    placeholder="搜索意见内容或关键词"
                    clearable
                    class="search-input"
                  >
                    <template #append>
                      <el-button @click="handleSuggestionSearch">
                        <el-icon><Search /></el-icon>
                      </el-button>
                    </template>
                  </el-input>
                </el-col>
                <el-col :xs="24" :sm="12" :md="6">
                  <el-select
                    v-model="suggestionStatusFilter"
                    placeholder="筛选状态"
                    clearable
                    class="filter-select"
                  >
                    <el-option label="全部状态" value=""></el-option>
                    <el-option label="待处理" value="pending"></el-option>
                    <el-option label="已处理" value="processed"></el-option>
                    <el-option label="已回复" value="replied"></el-option>
                  </el-select>
                </el-col>
                <el-col :xs="24" :sm="12" :md="6">
                  <el-select
                    v-model="suggestionTypeFilter"
                    placeholder="筛选类型"
                    clearable
                    class="filter-select"
                  >
                    <el-option label="全部类型" value=""></el-option>
                    <el-option label="意见建议" value="suggestion"></el-option>
                    <el-option label="投诉举报" value="complaint"></el-option>
                    <el-option label="咨询求助" value="inquiry"></el-option>
                  </el-select>
                </el-col>
              </el-row>
            </div>

            <!-- 意见列表 -->
            <div class="table-container">
              <el-table
                :data="paginatedSuggestions"
                border
                style="width: 100%"
                @selection-change="handleSuggestionSelectionChange"
                @row-dblclick="handleSuggestionRowDoubleClick"
              >
                <el-table-column type="selection" width="65"></el-table-column>
                <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
                <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip></el-table-column>
                <el-table-column prop="type" label="类型" width="120" align="center">
                  <template #default="scope">
                    <el-tag :type="getSuggestionTypeTagType(scope.row.type)">
                      {{ getSuggestionTypeName(scope.row.type) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="submitter" label="提交人" min-width="120" show-overflow-tooltip></el-table-column>
                <el-table-column prop="department" label="提交部门" min-width="150" show-overflow-tooltip></el-table-column>
                <el-table-column prop="submitDate" label="提交日期" width="150" align="center"></el-table-column>
                <el-table-column prop="status" label="状态" width="120" align="center">
                  <template #default="scope">
                    <el-tag :type="getSuggestionStatusTagType(scope.row.status)">
                      {{ getSuggestionStatusName(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="isAnonymous" label="是否匿名" width="120" align="center">
                  <template #default="scope">
                    <el-icon v-if="scope.row.isAnonymous" color="#909399"><QuestionFilled /></el-icon>
                    <span v-else>否</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="320" fixed="right">
                  <template #default="scope">
                    <el-space size="small">
                      <el-button size="small" @click="viewSuggestionDetail(scope.row)">
                        <el-icon><View /></el-icon> 查看
                      </el-button>
                      <el-button size="small" type="success" @click="replySuggestion(scope.row)" v-if="scope.row.status !== 'replied'">
                        <el-icon><Message /></el-icon> 回复
                      </el-button>
                      <el-button size="small" type="danger" @click="deleteSuggestion(scope.row)">
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
                v-model:current-page="suggestionCurrentPage"
                v-model:page-size="suggestionPageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="filteredSuggestions.length"
                @size-change="handleSuggestionSizeChange"
                @current-change="handleSuggestionCurrentChange"
              ></el-pagination>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <!-- 民主评议对话框 -->
      <el-dialog
        v-model="evaluationDialogVisible"
        :title="isEditingEvaluation ? '编辑评议' : '新建评议'"
        width="800px"
        :before-close="handleEvaluationDialogClose"
      >
        <el-form :model="currentEvaluation" label-width="100px" :rules="evaluationRules" ref="evaluationFormRef">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="评议主题" prop="title">
                <el-input v-model="currentEvaluation.title" placeholder="请输入评议主题"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="评议类型" prop="type">
                <el-select v-model="currentEvaluation.type" placeholder="请选择评议类型">
                  <el-option label="党员互评" value="mutual"></el-option>
                  <el-option label="群众评议" value="mass"></el-option>
                  <el-option label="领导评议" value="leader"></el-option>
                  <el-option label="综合评议" value="comprehensive"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开始日期" prop="startDate">
                <el-date-picker
                  v-model="currentEvaluation.startDate"
                  type="datetime"
                  placeholder="选择开始日期"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束日期" prop="endDate">
                <el-date-picker
                  v-model="currentEvaluation.endDate"
                  type="datetime"
                  placeholder="选择结束日期"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="参与人数">
                <el-input-number v-model="currentEvaluation.participants" :min="0" style="width: 100%"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="评议内容" prop="content">
                <el-input
                  v-model="currentEvaluation.content"
                  type="textarea"
                  :rows="6"
                  placeholder="请输入评议内容"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="handleEvaluationDialogClose">取消</el-button>
            <el-button type="primary" @click="handleSaveEvaluation">保存</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 评议详情对话框 -->
      <el-dialog
        v-model="evaluationDetailVisible"
        title="评议详情"
        width="800px"
      >
        <div v-if="currentEvaluation" class="evaluation-detail">
          <h3 class="detail-title">{{ currentEvaluation.title }}</h3>
          <div class="detail-meta">
            <span><strong>评议类型：</strong>{{ getEvaluationTypeName(currentEvaluation.type) }}</span>
            <span><strong>参与人数：</strong>{{ currentEvaluation.participants }}</span>
            <span><strong>响应率：</strong>{{ currentEvaluation.responseRate }}%</span>
            <span><strong>开始日期：</strong>{{ currentEvaluation.startDate }}</span>
            <span><strong>结束日期：</strong>{{ currentEvaluation.endDate }}</span>
            <span><strong>状态：</strong>{{ getEvaluationStatusName(currentEvaluation.status) }}</span>
          </div>
          <div class="detail-content">
            <strong>评议内容：</strong>
            <div v-html="currentEvaluation.content"></div>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="evaluationDetailVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 意见详情对话框 -->
      <el-dialog
        v-model="suggestionDetailVisible"
        title="意见详情"
        width="800px"
      >
        <div v-if="currentSuggestion" class="suggestion-detail">
          <h3 class="detail-title">{{ currentSuggestion.title }}</h3>
          <div class="detail-meta">
            <span><strong>意见类型：</strong>{{ getSuggestionTypeName(currentSuggestion.type) }}</span>
            <span><strong>提交人：</strong>{{ currentSuggestion.isAnonymous ? '匿名' : currentSuggestion.submitter }}</span>
            <span><strong>提交部门：</strong>{{ currentSuggestion.department }}</span>
            <span><strong>提交日期：</strong>{{ currentSuggestion.submitDate }}</span>
            <span><strong>状态：</strong>{{ getSuggestionStatusName(currentSuggestion.status) }}</span>
          </div>
          <div class="detail-content">
            <strong>意见内容：</strong>
            <div v-html="currentSuggestion.content"></div>
          </div>
          <div v-if="currentSuggestion.reply" class="reply-content">
            <h4>回复内容：</h4>
            <div v-html="currentSuggestion.reply"></div>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="suggestionDetailVisible = false">关闭</el-button>
            <el-button type="success" @click="replySuggestion(currentSuggestion)" v-if="currentSuggestion.status !== 'replied'">
              回复
            </el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 回复意见对话框 -->
      <el-dialog
        v-model="replyDialogVisible"
        title="回复意见"
        width="600px"
      >
        <el-form :model="replyForm" label-width="100px" ref="replyFormRef">
          <el-form-item label="回复内容" prop="reply">
            <el-input
              v-model="replyForm.reply"
              type="textarea"
              :rows="6"
              placeholder="请输入回复内容"
            ></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="replyDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSaveReply">保存回复</el-button>
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
import { Plus, QuestionFilled, View, Edit, Delete, Check, Download, Message, Search } from '@element-plus/icons-vue'

// 标签页状态
const activeTab = ref('democratic-evaluation')

// 民主评议相关状态
const evaluations = ref([
  {
    id: 1,
    title: '2022年度党员民主评议',
    type: 'comprehensive',
    participants: 120,
    responseRate: 95,
    startDate: '2023-01-05 09:00:00',
    endDate: '2023-01-20 23:59:59',
    content: '请对党员2022年度的工作表现、思想状况、学习情况等进行综合评议。',
    status: 'completed'
  },
  {
    id: 2,
    title: '2023年第一季度党员互评',
    type: 'mutual',
    participants: 85,
    responseRate: 88,
    startDate: '2023-04-01 09:00:00',
    endDate: '2023-04-15 23:59:59',
    content: '党员之间相互评议，重点关注工作态度、团队合作、服务意识等方面。',
    status: 'completed'
  },
  {
    id: 3,
    title: '党支部书记年度评议',
    type: 'leader',
    participants: 45,
    responseRate: 100,
    startDate: '2023-01-25 09:00:00',
    endDate: '2023-01-31 23:59:59',
    content: '对党支部书记2022年度的工作表现、领导能力、服务水平等进行评议。',
    status: 'completed'
  },
  {
    id: 4,
    title: '2023年第二季度群众评议',
    type: 'mass',
    participants: 200,
    responseRate: 75,
    startDate: '2023-07-01 09:00:00',
    endDate: '2023-07-15 23:59:59',
    content: '请群众代表对党员的工作表现、服务态度、廉洁自律等方面进行评议。',
    status: 'pending'
  },
  {
    id: 5,
    title: '党员先锋岗评选评议',
    type: 'comprehensive',
    participants: 150,
    responseRate: 92,
    startDate: '2023-03-10 09:00:00',
    endDate: '2023-03-25 23:59:59',
    content: '对党员先锋岗候选人的工作表现、先锋模范作用发挥情况等进行评议。',
    status: 'completed'
  },
  {
    id: 6,
    title: '2023年秋季学期党员评议',
    type: 'comprehensive',
    participants: 95,
    responseRate: 0,
    startDate: '2023-09-01 09:00:00',
    endDate: '2023-09-15 23:59:59',
    content: '请对党员2023年秋季学期的工作表现、学习情况等进行评议。',
    status: 'pending'
  },
  {
    id: 7,
    title: '党支部班子成员评议',
    type: 'leader',
    participants: 85,
    responseRate: 98,
    startDate: '2023-02-01 09:00:00',
    endDate: '2023-02-15 23:59:59',
    content: '对党支部班子成员的工作表现、团结协作、服务意识等方面进行评议。',
    status: 'completed'
  },
  {
    id: 8,
    title: '2023年上半年党员综合评议',
    type: 'comprehensive',
    participants: 120,
    responseRate: 0,
    startDate: '2023-07-10 09:00:00',
    endDate: '2023-07-25 23:59:59',
    content: '请对党员2023年上半年的工作表现、思想状况、学习情况等进行综合评议。',
    status: 'pending'
  },
  {
    id: 9,
    title: '2023年第三季度党员互评',
    type: 'mutual',
    participants: 110,
    responseRate: 82,
    startDate: '2023-10-01 09:00:00',
    endDate: '2023-10-15 23:59:59',
    content: '党员之间相互评议，重点关注季度工作目标完成情况和团队协作能力。',
    status: 'completed'
  },
  {
    id: 10,
    title: '优秀党员评选评议',
    type: 'comprehensive',
    participants: 200,
    responseRate: 90,
    startDate: '2023-11-01 09:00:00',
    endDate: '2023-11-15 23:59:59',
    content: '对2023年度优秀党员候选人进行评议，选出真正符合条件的优秀党员。',
    status: 'pending'
  },
  {
    id: 11,
    title: '新党员转正评议',
    type: 'leader',
    participants: 50,
    responseRate: 100,
    startDate: '2023-12-01 09:00:00',
    endDate: '2023-12-07 23:59:59',
    content: '对2023年度预备党员转正申请进行评议，决定是否同意转正。',
    status: 'pending'
  },
  {
    id: 12,
    title: '2023年度基层党建工作评议',
    type: 'mass',
    participants: 250,
    responseRate: 78,
    startDate: '2023-12-10 09:00:00',
    endDate: '2023-12-25 23:59:59',
    content: '请对2023年度基层党建工作的开展情况、成效等进行评议。',
    status: 'pending'
  }
])

const evaluationSearchQuery = ref('')
const evaluationStatusFilter = ref('')
const evaluationDateRange = ref('')
const evaluationCurrentPage = ref(1)
const evaluationPageSize = ref(10)
const evaluationDialogVisible = ref(false)
const evaluationDetailVisible = ref(false)
const isEditingEvaluation = ref(false)
const currentEvaluation = ref({})
const selectedEvaluations = ref([])
const evaluationFormRef = ref(null)

// 评议表单验证规则
const evaluationRules = {
  title: [{ required: true, message: '请输入评议主题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择评议类型', trigger: 'change' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
  content: [{ required: true, message: '请输入评议内容', trigger: 'blur' }]
}

// 过滤后的评议列表
const filteredEvaluations = computed(() => {
  return evaluations.value.filter(evaluation => {
    const matchesSearch = evaluation.title.toLowerCase().includes(evaluationSearchQuery.value.toLowerCase()) ||
                         evaluation.content.toLowerCase().includes(evaluationSearchQuery.value.toLowerCase())
    const matchesStatus = !evaluationStatusFilter.value || evaluation.status === evaluationStatusFilter.value
    // 日期范围过滤逻辑（简化处理）
    const matchesDateRange = !evaluationDateRange.value || true
    return matchesSearch && matchesStatus && matchesDateRange
  })
})

// 分页后的评议列表
const paginatedEvaluations = computed(() => {
  const start = (evaluationCurrentPage.value - 1) * evaluationPageSize.value
  const end = start + evaluationPageSize.value
  return filteredEvaluations.value.slice(start, end)
})

// 评议统计数据
const totalEvaluations = computed(() => evaluations.value.length)
const pendingEvaluations = computed(() => evaluations.value.filter(e => e.status === 'pending').length)
const completedEvaluations = computed(() => evaluations.value.filter(e => e.status === 'completed').length)
const participationRate = computed(() => {
  if (totalEvaluations.value === 0) return 0
  const completedRateSum = evaluations.value.reduce((sum, e) => sum + e.responseRate, 0)
  return Math.round(completedRateSum / totalEvaluations.value)
})

// 评议类型名称映射
const getEvaluationTypeName = (type) => {
  const typeMap = {
    mutual: '党员互评',
    mass: '群众评议',
    leader: '领导评议',
    comprehensive: '综合评议'
  }
  return typeMap[type] || type
}

// 评议类型标签样式映射
const getEvaluationTypeTagType = (type) => {
  const typeMap = {
    mutual: 'info',
    mass: 'success',
    leader: 'warning',
    comprehensive: 'primary'
  }
  return typeMap[type] || 'default'
}

// 评议状态名称映射
const getEvaluationStatusName = (status) => {
  const statusMap = {
    pending: '待处理',
    completed: '已完成',
    closed: '已关闭'
  }
  return statusMap[status] || status
}

// 评议状态标签样式映射
const getEvaluationStatusTagType = (status) => {
  const statusMap = {
    pending: 'warning',
    completed: 'success',
    closed: 'info'
  }
  return statusMap[status] || 'default'
}

// 响应率颜色映射
const getResponseRateColor = (rate) => {
  if (rate >= 90) return '#67c23a' // 绿色
  if (rate >= 70) return '#e6a23c' // 黄色
  if (rate >= 50) return '#f56c6c' // 橙色
  return '#909399' // 灰色
}

// 意见箱相关状态
const suggestions = ref([
  {
    id: 1,
    title: '关于改进党员学习方式的建议',
    type: 'suggestion',
    submitter: '张三',
    department: '宣传部',
    submitDate: '2023-05-10 14:30:00',
    content: '建议增加线上学习平台的互动功能，比如讨论区、直播课堂等，提高党员学习的积极性。',
    isAnonymous: false,
    status: 'processed'
  },
  {
    id: 2,
    title: '关于加强党支部组织生活的意见',
    type: 'suggestion',
    submitter: '',
    department: '组织部',
    submitDate: '2023-05-15 10:20:00',
    content: '希望党支部能增加一些实践活动，比如志愿服务、红色教育基地参观等，让组织生活更加丰富多彩。',
    isAnonymous: true,
    status: 'pending'
  },
  {
    id: 3,
    title: '关于党员发展工作的投诉',
    type: 'complaint',
    submitter: '李四',
    department: '学生处',
    submitDate: '2023-04-25 16:45:00',
    content: '投诉党员发展工作中存在不公平现象，希望有关部门能进行调查。',
    isAnonymous: false,
    status: 'replied',
    reply: '感谢您的反馈，我们已对党员发展工作进行全面检查，未发现不公平现象。如有具体问题，请提供更多细节。'
  },
  {
    id: 4,
    title: '关于党费收缴标准的咨询',
    type: 'inquiry',
    submitter: '',
    department: '财务部',
    submitDate: '2023-05-05 11:30:00',
    content: '请问最新的党费收缴标准是什么？如何计算？',
    isAnonymous: true,
    status: 'replied',
    reply: '根据最新规定，党费收缴标准为：每月工资收入(税后)在3000元以下(含3000元)者，交纳月工资收入的0.5%；3000元以上至5000元(含5000元)者，交纳1%；5000元以上至10000元(含10000元)者，交纳1.5%；10000元以上者，交纳2%。'
  },
  {
    id: 5,
    title: '关于改进党员服务的建议',
    type: 'suggestion',
    submitter: '王五',
    department: '办公室',
    submitDate: '2023-05-20 09:15:00',
    content: '建议建立党员服务热线或在线咨询平台，方便党员及时获取帮助和咨询问题。',
    isAnonymous: false,
    status: 'processed'
  },
  {
    id: 6,
    title: '关于主题党日活动的建议',
    type: 'suggestion',
    submitter: '',
    department: '宣传部',
    submitDate: '2023-05-25 15:40:00',
    content: '希望主题党日活动能更加贴近实际工作，结合业务开展，提高活动的针对性和实效性。',
    isAnonymous: true,
    status: 'pending'
  },
  {
    id: 7,
    title: '关于党员培训的建议',
    type: 'suggestion',
    submitter: '赵六',
    department: '组织部',
    submitDate: '2023-06-01 14:20:00',
    content: '建议增加党员培训的多样性，比如邀请专家学者授课、组织外出考察学习等，提高培训质量。',
    isAnonymous: false,
    status: 'processed'
  },
  {
    id: 8,
    title: '关于党员考核的建议',
    type: 'suggestion',
    submitter: '',
    department: '考核办',
    submitDate: '2023-06-05 10:30:00',
    content: '希望党员考核能更加注重实际表现和工作业绩，避免形式主义，让考核真正起到激励作用。',
    isAnonymous: true,
    status: 'pending'
  },
  {
    id: 9,
    title: '关于党员活动室建设的建议',
    type: 'suggestion',
    submitter: '孙七',
    department: '后勤处',
    submitDate: '2023-06-10 09:45:00',
    content: '建议加强党员活动室的建设，增加书籍、杂志、音像资料等，为党员学习提供更好的条件。',
    isAnonymous: false,
    status: 'processed'
  },
  {
    id: 10,
    title: '关于党员先锋岗的建议',
    type: 'suggestion',
    submitter: '',
    department: '宣传部',
    submitDate: '2023-06-15 16:20:00',
    content: '希望党员先锋岗能真正发挥先锋模范作用，定期评选和表彰，树立榜样，激励更多党员积极进取。',
    isAnonymous: true,
    status: 'pending'
  },
  {
    id: 11,
    title: '关于党内监督的建议',
    type: 'suggestion',
    submitter: '周八',
    department: '纪检部',
    submitDate: '2023-06-20 11:15:00',
    content: '建议加强党内监督，建立健全监督机制，及时发现和纠正党员干部存在的问题，保持党的先进性和纯洁性。',
    isAnonymous: false,
    status: 'processed'
  },
  {
    id: 12,
    title: '关于党员关怀的建议',
    type: 'suggestion',
    submitter: '',
    department: '组织部',
    submitDate: '2023-06-25 14:50:00',
    content: '希望加强对困难党员的关怀和帮助，定期走访慰问，解决他们的实际困难，让党员感受到组织的温暖。',
    isAnonymous: true,
    status: 'pending'
  },
  {
    id: 13,
    title: '关于加强党员理论学习的建议',
    type: 'suggestion',
    submitter: '吴九',
    department: '宣传部',
    submitDate: '2023-07-01 10:20:00',
    content: '建议增加党员理论学习的频率和深度，定期组织专题学习和研讨活动。',
    isAnonymous: false,
    status: 'pending'
  },
  {
    id: 14,
    title: '关于改进组织生活会形式的建议',
    type: 'suggestion',
    submitter: '',
    department: '组织部',
    submitDate: '2023-07-05 15:30:00',
    content: '希望组织生活会能更加开放和民主，鼓励党员畅所欲言，真正达到批评与自我批评的目的。',
    isAnonymous: true,
    status: 'processed'
  },
  {
    id: 15,
    title: '关于党员信息管理系统的咨询',
    type: 'inquiry',
    submitter: '郑十',
    department: '办公室',
    submitDate: '2023-07-10 14:45:00',
    content: '请问党员信息管理系统如何登录？如何修改个人信息？',
    isAnonymous: false,
    status: 'replied',
    reply: '党员信息管理系统登录地址为：http://xxxxx，使用党员ID和初始密码登录，登录后可在个人中心修改个人信息。如有问题，请联系组织部门。'
  },
  {
    id: 16,
    title: '关于党员志愿服务活动的建议',
    type: 'suggestion',
    submitter: '',
    department: '宣传部',
    submitDate: '2023-07-15 09:20:00',
    content: '建议增加党员志愿服务活动的机会和类型，让党员更好地服务群众、奉献社会。',
    isAnonymous: true,
    status: 'pending'
  },
  {
    id: 17,
    title: '关于党员评优评先工作的建议',
    type: 'suggestion',
    submitter: '钱十一',
    department: '组织部',
    submitDate: '2023-07-20 16:10:00',
    content: '希望党员评优评先工作能更加公开透明，严格按照标准评选，确保评选结果公正公平。',
    isAnonymous: false,
    status: 'processed'
  },
  {
    id: 18,
    title: '关于党费使用情况的咨询',
    type: 'inquiry',
    submitter: '',
    department: '财务部',
    submitDate: '2023-07-25 11:30:00',
    content: '请问党费的使用范围是什么？如何查询党费使用情况？',
    isAnonymous: true,
    status: 'replied',
    reply: '党费的使用范围主要包括：党员教育、党员培训、党内表彰、党内关怀、党组织活动等。党费使用情况每年会在党员大会上进行通报，党员也可向组织部门申请查询。'
  }
])

const suggestionSearchQuery = ref('')
const suggestionStatusFilter = ref('')
const suggestionTypeFilter = ref('')
const suggestionCurrentPage = ref(1)
const suggestionPageSize = ref(10)
const suggestionDetailVisible = ref(false)
const replyDialogVisible = ref(false)
const currentSuggestion = ref({})
const selectedSuggestions = ref([])
const replyForm = ref({ reply: '' })
const replyFormRef = ref(null)

// 过滤后的意见列表
const filteredSuggestions = computed(() => {
  return suggestions.value.filter(suggestion => {
    const matchesSearch = suggestion.title.toLowerCase().includes(suggestionSearchQuery.value.toLowerCase()) ||
                         suggestion.content.toLowerCase().includes(suggestionSearchQuery.value.toLowerCase())
    const matchesStatus = !suggestionStatusFilter.value || suggestion.status === suggestionStatusFilter.value
    const matchesType = !suggestionTypeFilter.value || suggestion.type === suggestionTypeFilter.value
    return matchesSearch && matchesStatus && matchesType
  })
})

// 分页后的意见列表
const paginatedSuggestions = computed(() => {
  const start = (suggestionCurrentPage.value - 1) * suggestionPageSize.value
  const end = start + suggestionPageSize.value
  return filteredSuggestions.value.slice(start, end)
})

// 意见统计数据
const totalSuggestions = computed(() => suggestions.value.length)
const pendingSuggestions = computed(() => suggestions.value.filter(s => s.status === 'pending').length)
const processedSuggestions = computed(() => suggestions.value.filter(s => s.status === 'processed').length)
const repliedSuggestions = computed(() => suggestions.value.filter(s => s.status === 'replied').length)

// 意见类型名称映射
const getSuggestionTypeName = (type) => {
  const typeMap = {
    suggestion: '意见建议',
    complaint: '投诉举报',
    inquiry: '咨询求助'
  }
  return typeMap[type] || type
}

// 意见类型标签样式映射
const getSuggestionTypeTagType = (type) => {
  const typeMap = {
    suggestion: 'info',
    complaint: 'danger',
    inquiry: 'warning'
  }
  return typeMap[type] || 'default'
}

// 意见状态名称映射
const getSuggestionStatusName = (status) => {
  const statusMap = {
    pending: '待处理',
    processed: '已处理',
    replied: '已回复'
  }
  return statusMap[status] || status
}

// 意见状态标签样式映射
const getSuggestionStatusTagType = (status) => {
  const statusMap = {
    pending: 'warning',
    processed: 'info',
    replied: 'success'
  }
  return statusMap[status] || 'default'
}

// 民主评议相关方法
const handleEvaluationSearch = () => {
  evaluationCurrentPage.value = 1
}

const handleEvaluationSizeChange = (val) => {
  evaluationPageSize.value = val
  evaluationCurrentPage.value = 1
}

const handleEvaluationCurrentChange = (val) => {
  evaluationCurrentPage.value = val
}

const showCreateEvaluationDialog = () => {
  isEditingEvaluation.value = false
  currentEvaluation.value = {
    title: '',
    type: 'comprehensive',
    participants: 0,
    responseRate: 0,
    startDate: new Date().toISOString().split('T')[0] + ' ' + new Date().toTimeString().split(' ')[0],
    endDate: '',
    content: '',
    status: 'pending'
  }
  evaluationDialogVisible.value = true
}

const editEvaluation = (evaluation) => {
  isEditingEvaluation.value = true
  currentEvaluation.value = JSON.parse(JSON.stringify(evaluation))
  evaluationDialogVisible.value = true
}

const viewEvaluationDetail = (evaluation) => {
  currentEvaluation.value = JSON.parse(JSON.stringify(evaluation))
  evaluationDetailVisible.value = true
}

const handleSaveEvaluation = () => {
  if (!evaluationFormRef.value) return
  evaluationFormRef.value.validate((valid) => {
    if (valid) {
      if (isEditingEvaluation.value) {
        // 编辑模式
        const index = evaluations.value.findIndex(e => e.id === currentEvaluation.value.id)
        if (index !== -1) {
          evaluations.value[index] = JSON.parse(JSON.stringify(currentEvaluation.value))
        }
      } else {
        // 添加模式
        const newId = Math.max(...evaluations.value.map(e => e.id)) + 1
        currentEvaluation.value.id = newId
        evaluations.value.unshift(JSON.parse(JSON.stringify(currentEvaluation.value)))
      }
      evaluationDialogVisible.value = false
      ElMessage.success(isEditingEvaluation.value ? '评议编辑成功' : '评议添加成功')
    } else {
      ElMessage.error('请填写必填字段')
      return false
    }
  })
}

const deleteEvaluation = (evaluation) => {
  ElMessageBox.confirm('确定要删除该评议吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = evaluations.value.findIndex(e => e.id === evaluation.id)
    if (index !== -1) {
      evaluations.value.splice(index, 1)
      ElMessage.success('评议删除成功')
    }
  }).catch(() => {
    // 取消删除
  })
}

const exportEvaluationData = () => {
  ElMessage.success('评议数据导出功能待实现')
}

const exportEvaluationResults = (evaluation) => {
  ElMessage.success(`评议结果导出功能待实现 - ${evaluation.title}`)
}

const batchReview = () => {
  if (selectedEvaluations.value.length === 0) {
    ElMessage.warning('请选择要审核的评议')
    return
  }
  ElMessage.success(`批量审核 ${selectedEvaluations.value.length} 条评议功能待实现`)
}

const handleEvaluationDialogClose = () => {
  evaluationDialogVisible.value = false
  if (evaluationFormRef.value) {
    evaluationFormRef.value.resetFields()
  }
}

const handleEvaluationSelectionChange = (selection) => {
  selectedEvaluations.value = selection
}

const handleEvaluationRowDoubleClick = (row) => {
  viewEvaluationDetail(row)
}

// 意见箱相关方法
const handleSuggestionSearch = () => {
  suggestionCurrentPage.value = 1
}

const handleSuggestionSizeChange = (val) => {
  suggestionPageSize.value = val
  suggestionCurrentPage.value = 1
}

const handleSuggestionCurrentChange = (val) => {
  suggestionCurrentPage.value = val
}

const viewSuggestionDetail = (suggestion) => {
  currentSuggestion.value = JSON.parse(JSON.stringify(suggestion))
  suggestionDetailVisible.value = true
}

const replySuggestion = (suggestion) => {
  currentSuggestion.value = JSON.parse(JSON.stringify(suggestion))
  replyForm.value = { reply: '' }
  replyDialogVisible.value = true
}

const handleSaveReply = () => {
  if (!currentSuggestion.value) return
  const index = suggestions.value.findIndex(s => s.id === currentSuggestion.value.id)
  if (index !== -1) {
    suggestions.value[index].reply = replyForm.value.reply
    suggestions.value[index].status = 'replied'
    replyDialogVisible.value = false
    suggestionDetailVisible.value = false
    ElMessage.success('意见回复成功')
  }
}

const deleteSuggestion = (suggestion) => {
  ElMessageBox.confirm('确定要删除该意见吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = suggestions.value.findIndex(s => s.id === suggestion.id)
    if (index !== -1) {
      suggestions.value.splice(index, 1)
      ElMessage.success('意见删除成功')
    }
  }).catch(() => {
    // 取消删除
  })
}

const exportSuggestionData = () => {
  ElMessage.success('意见数据导出功能待实现')
}

const batchReply = () => {
  if (selectedSuggestions.value.length === 0) {
    ElMessage.warning('请选择要回复的意见')
    return
  }
  ElMessage.success(`批量回复 ${selectedSuggestions.value.length} 条意见功能待实现`)
}

const batchProcess = () => {
  if (selectedSuggestions.value.length === 0) {
    ElMessage.warning('请选择要处理的意见')
    return
  }
  ElMessage.success(`批量处理 ${selectedSuggestions.value.length} 条意见功能待实现`)
}

const handleSuggestionSelectionChange = (selection) => {
  selectedSuggestions.value = selection
}

const handleSuggestionRowDoubleClick = (row) => {
  viewSuggestionDetail(row)
}
</script>

<style scoped>
.theme-party-day {
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
}

.interaction-management {
  padding: 20px;
}

.page-title {
  color: #2c3e50;
  margin-bottom: 20px;
  font-size: 24px;
}

.interaction-tabs {
  margin-top: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.card-content {
  text-align: center;
}

.card-title {
  font-size: 16px;
  color: #6c757d;
  margin-bottom: 10px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
}

.action-buttons {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.filter-section {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.search-input,
.filter-select,
.filter-datepicker {
  width: 100%;
}

.evaluation-table,
.suggestion-table {
  margin-bottom: 20px;
}

.pagination {
  text-align: right;
}

.evaluation-detail,
.suggestion-detail {
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

.reply-content {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #e9ecef;
}

/* 面包屑样式 */
.breadcrumb {
  margin-bottom: 16px;
  font-size: 14px;
}

/* 页面头部样式 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e9ecef;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #212529;
}

.header-actions {
  display: flex;
  gap: 10px;
}

/* 搜索筛选区域样式 */
.search-filter {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 4px;
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

/* 按钮样式 */
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

/* 响应式设计 */
@media (max-width: 1024px) {
  .theme-party-day,
  .interaction-management {
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
  
  .header-actions {
    flex-direction: column;
    width: 100%;
  }
  
  .detail-meta {
    flex-direction: column;
    gap: 10px;
  }
  
  .action-buttons {
    flex-direction: column;
  }
}
</style>