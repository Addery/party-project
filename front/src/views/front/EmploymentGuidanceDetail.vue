<template>
  <div class="employment-guidance-detail">
    <!-- 页面顶部导航 -->
    <div class="page-header">
      <div class="container">
        <h1>就业指导资源详情</h1>
        <div class="breadcrumb">
          <a href="/">首页</a> &gt; <a href="/employment">就业服务</a> &gt; <a href="/employment/guidance">就业指导资源</a> &gt; 详情
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="container">
      <div class="content-wrapper">
        <!-- 左侧详情内容区 -->
        <div class="main-content">
          <!-- 详情标题区域 -->
          <div class="detail-header">
            <h2 class="detail-title">{{ guidanceDetail.title }}</h2>
            <div class="detail-meta">
              <span class="guidance-type" :class="`type-${guidanceDetail.type}`">
                {{ guidanceDetail.type }}
              </span>
              <span class="publish-date">{{ guidanceDetail.publishDate }}</span>
              <span class="author">{{ guidanceDetail.author || '就业指导中心' }}</span>
              <span class="view-count">浏览: {{ guidanceDetail.viewCount || 0 }}</span>
            </div>
          </div>

          <!-- 详情内容 -->
          <div class="detail-content">
            <div v-html="guidanceDetail.content"></div>
          </div>

          <!-- 相关附件 -->
          <div class="attachments" v-if="guidanceDetail.attachments && guidanceDetail.attachments.length > 0">
            <h3>相关附件</h3>
            <div class="attachment-list">
              <div v-for="(attachment, index) in guidanceDetail.attachments" :key="index" class="attachment-item">
                <i class="icon-file"></i>
                <a href="#" @click.prevent>{{ attachment.name }}</a>
                <span class="file-size">{{ formatFileSize(attachment.size) }}</span>
              </div>
            </div>
          </div>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <button @click="goBackToList" class="btn-back">返回列表</button>
            <button @click="shareContent" class="btn-share">分享</button>
          </div>
        </div>

        <!-- 右侧边栏 -->
        <div class="sidebar">
          <!-- 相关推荐 -->
          <div class="sidebar-section">
            <h3>相关推荐</h3>
            <div class="related-guidance">
              <div v-for="item in relatedGuidance" :key="item.id" class="related-item">
                <a href="#" @click="goToDetail(item.id)">{{ item.title }}</a>
                <span class="item-type">{{ item.type }}</span>
              </div>
            </div>
          </div>

          <!-- 热门资源 -->
          <div class="sidebar-section">
            <h3>热门资源</h3>
            <div class="hot-guidance">
              <div v-for="item in hotGuidance" :key="item.id" class="hot-item">
                <a href="#" @click="goToDetail(item.id)">{{ item.title }}</a>
                <span class="item-view">{{ item.viewCount || 0 }} 浏览</span>
              </div>
            </div>
          </div>

          <!-- 资源分类导航 -->
          <div class="sidebar-section">
            <h3>资源分类</h3>
            <div class="category-nav">
              <div v-for="category in categories" :key="category" class="category-item">
                <a href="#" @click="goToCategory(category)">{{ category }}</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import employmentApi from '../../api/employment'

const router = useRouter()
const route = useRoute()
const guidanceDetail = ref({
  id: '',
  title: '',
  type: '',
  publishDate: '',
  author: '',
  viewCount: 0,
  content: '',
  attachments: []
})

const relatedGuidance = ref([])
const hotGuidance = ref([])
const categories = ['简历指导', '面试技巧', '职业规划', '职场适应', '创业指导', '政策解读']

// 加载数据
onMounted(() => {
  fetchGuidanceDetail()
  fetchRelatedGuidance()
  fetchHotGuidance()
})

// 获取指导资源详情
const fetchGuidanceDetail = async () => {
  try {
    const id = route.params.id
    const data = await employmentApi.getEmploymentGuidanceDetail(id)
    guidanceDetail.value = data.data || {}
  } catch (error) {
    console.error('获取就业指导资源详情失败:', error)
    // 使用模拟数据
    guidanceDetail.value = {
      id: route.params.id,
      title: '如何制作一份优秀的简历',
      type: '简历指导',
      publishDate: '2025-11-20 09:30:00',
      author: '就业指导中心李老师',
      viewCount: 523,
      content: `
        <p>简历是求职过程中最重要的文件之一，一份优秀的简历能够帮助你在众多求职者中脱颖而出。下面是一些制作优秀简历的关键技巧：</p>
        
        <h3>1. 简历结构要清晰</h3>
        <p>一个清晰的简历结构包括：个人信息、求职意向、教育背景、工作/实习经历、项目经验、技能证书、个人评价等部分。确保各部分之间有明确的区分，使用标题和适当的间距。</p>
        
        <h3>2. 突出核心能力和成就</h3>
        <p>在描述工作或实习经历时，不要只是简单地列出职责，要突出你的成就和贡献。使用具体的数据和例子来说明你的工作成果，比如："负责XX项目的市场推广，使产品销售额增长了30%"。</p>
        
        <h3>3. 针对性定制简历</h3>
        <p>针对不同的职位和公司，定制你的简历内容。重点突出与目标职位相关的技能和经验，删除不相关的信息，确保简历与职位要求高度匹配。</p>
        
        <h3>4. 语言简洁明了</h3>
        <p>使用简洁、专业的语言，避免使用模糊的词汇和冗长的句子。每段文字控制在3-5行以内，使用 bullet points 使内容更易于阅读。</p>
        
        <h3>5. 注意格式和排版</h3>
        <p>使用统一的字体和字号，建议使用 Arial、Calibri 等专业字体，字号在10-12pt之间。保持适当的行间距和页边距，确保简历整体美观大方。</p>
        
        <h3>6. 仔细检查和校对</h3>
        <p>简历完成后，仔细检查是否有拼写错误、语法错误和格式问题。可以请同学或老师帮忙校对，确保简历完美无缺。</p>
        
        <p>最后，记住简历是你的敲门砖，一份优秀的简历能够帮助你获得面试机会，但真正决定你能否获得工作的还是你的实际能力和表现。祝你求职顺利！</p>
      `,
      attachments: [
        { name: '优秀简历模板合集.zip', size: 2345000 },
        { name: '简历写作指南.pdf', size: 567000 }
      ]
    }
  }
}

// 获取相关推荐
const fetchRelatedGuidance = async () => {
  try {
    const data = await employmentApi.getEmploymentGuidance({ page: 1, pageSize: 5, type: guidanceDetail.value.type })
    relatedGuidance.value = data.data?.list || []
  } catch (error) {
    console.error('获取相关推荐失败:', error)
    relatedGuidance.value = [
      { id: 7, title: '简历中的实习经历如何突出', type: '简历指导', viewCount: 198 },
      { id: 2, title: '面试中常见问题及回答技巧', type: '面试技巧', viewCount: 486 },
      { id: 3, title: '大学生职业规划指南', type: '职业规划', viewCount: 342 },
      { id: 6, title: '2025年就业政策解读', type: '政策解读', viewCount: 412 },
      { id: 4, title: '初入职场的注意事项', type: '职场适应', viewCount: 298 }
    ]
  }
}

// 获取热门资源
const fetchHotGuidance = async () => {
  try {
    const data = await employmentApi.getEmploymentGuidance({ page: 1, pageSize: 5, sortBy: 'viewCount' })
    hotGuidance.value = data.data?.list || []
  } catch (error) {
    console.error('获取热门资源失败:', error)
    hotGuidance.value = [
      { id: 1, title: '如何制作一份优秀的简历', viewCount: 523 },
      { id: 2, title: '面试中常见问题及回答技巧', viewCount: 486 },
      { id: 6, title: '2025年就业政策解读', viewCount: 412 },
      { id: 8, title: '无领导小组讨论技巧', viewCount: 321 },
      { id: 3, title: '大学生职业规划指南', viewCount: 342 }
    ]
  }
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/employment/guidance/${id}`)
}

// 返回列表页
const goBackToList = () => {
  router.push('/employment/guidance/list')
}

// 分享内容
const shareContent = () => {
  // 这里可以实现分享功能
  alert('分享功能待实现')
}

// 跳转到分类页面
const goToCategory = (category) => {
  router.push({ path: '/employment/guidance', query: { type: category } })
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}
</script>

<style scoped>
.employment-guidance-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.page-header {
  background-color: #fff;
  padding: 30px 0;
  border-bottom: 1px solid #eee;
}

.page-header h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.breadcrumb {
  color: #666;
  font-size: 14px;
}

.breadcrumb a {
  color: #007bff;
  text-decoration: none;
}

.breadcrumb a:hover {
  text-decoration: underline;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.content-wrapper {
  display: flex;
  gap: 30px;
}

.main-content {
  flex: 1;
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.detail-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.detail-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 15px;
  line-height: 1.4;
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: center;
  font-size: 14px;
  color: #666;
}

.guidance-type {
  padding: 5px 10px;
  border-radius: 4px;
  color: #fff;
  font-weight: bold;
}

.type-简历指导 { background-color: #28a745; }
.type-面试技巧 { background-color: #ffc107; color: #333; }
.type-职业规划 { background-color: #17a2b8; }
.type-职场适应 { background-color: #6f42c1; }
.type-创业指导 { background-color: #dc3545; }
.type-政策解读 { background-color: #007bff; }

.detail-content {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 30px;
}

.detail-content h3 {
  font-size: 22px;
  color: #222;
  margin: 25px 0 15px;
}

.detail-content p {
  margin-bottom: 20px;
}

.detail-content ul, .detail-content ol {
  margin-bottom: 20px;
  padding-left: 25px;
}

.detail-content li {
  margin-bottom: 10px;
}

.attachments {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.attachments h3 {
  font-size: 18px;
  margin-bottom: 15px;
  color: #333;
}

.attachment-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.attachment-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background-color: #fff;
  border-radius: 4px;
  border: 1px solid #eee;
}

.icon-file {
  color: #007bff;
  font-size: 18px;
}

.attachment-item a {
  color: #007bff;
  text-decoration: none;
  flex: 1;
}

.attachment-item a:hover {
  text-decoration: underline;
}

.file-size {
  color: #666;
  font-size: 12px;
}

.action-buttons {
  display: flex;
  gap: 15px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.btn-back, .btn-share {
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  border: none;
}

.btn-back {
  background-color: #6c757d;
  color: #fff;
}

.btn-back:hover {
  background-color: #5a6268;
}

.btn-share {
  background-color: #17a2b8;
  color: #fff;
}

.btn-share:hover {
  background-color: #138496;
}

.sidebar {
  width: 300px;
}

.sidebar-section {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.sidebar-section h3 {
  font-size: 18px;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #007bff;
}

.related-guidance, .hot-guidance {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.related-item, .hot-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.related-item:last-child, .hot-item:last-child {
  border-bottom: none;
}

.related-item a, .hot-item a {
  color: #333;
  text-decoration: none;
  font-size: 14px;
  line-height: 1.5;
}

.related-item a:hover, .hot-item a:hover {
  color: #007bff;
}

.item-type, .item-view {
  font-size: 12px;
  color: #999;
}

.category-nav {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.category-item a {
  color: #666;
  text-decoration: none;
  font-size: 14px;
  display: block;
  padding: 8px 0;
  transition: padding-left 0.2s;
}

.category-item a:hover {
  color: #007bff;
  padding-left: 8px;
}

@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
  }
  
  .detail-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .btn-back, .btn-share {
    width: 100%;
  }
}
</style>