<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Navbar from '@/components/front/Navbar.vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import noticeApi from '@/api/notice.js'

const router = useRouter()
const route = useRoute()
const noticeId = computed(() => parseInt(route.params.id))

// 通知详情数据
const notice = ref({
  id: null,
  title: '',
  category: '',
  publishDate: '',
  source: '',
  content: '',
  attachments: []
})

// 加载状态
const loading = ref(true)
const error = ref(null)

// 回到顶部按钮状态
const showBackToTop = ref(false)

// 监听滚动事件，控制回到顶部按钮的显示
const handleScroll = () => {
  showBackToTop.value = window.scrollY > 300
}

// 回到顶部
const backToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 优化content内容显示
const contentProcessor = (htmlContent) => {
  if (!htmlContent) return ''
  
  // 创建临时容器来处理HTML
  const tempContainer = document.createElement('div')
  tempContainer.innerHTML = htmlContent
  
  // 处理段落样式
  const paragraphs = tempContainer.querySelectorAll('p')
  paragraphs.forEach(p => {
    // 确保每个段落都有适当的边距
    if (!p.style.marginBottom) {
      p.style.marginBottom = '18px'
    }
    // 移除多余的空格
    p.innerHTML = p.innerHTML.trim()
  })
  
  // 处理标题样式
  const headings = tempContainer.querySelectorAll('h1, h2, h3, h4, h5, h6')
  headings.forEach(heading => {
    // 确保标题有适当的边距和样式
    if (!heading.style.marginTop) {
      heading.style.marginTop = '30px'
    }
    if (!heading.style.marginBottom) {
      heading.style.marginBottom = '15px'
    }
  })
  
  // 处理列表
  const lists = tempContainer.querySelectorAll('ul, ol')
  lists.forEach(list => {
    // 确保列表有适当的缩进和边距
    if (!list.style.marginLeft) {
      list.style.marginLeft = '20px'
    }
    if (!list.style.marginTop) {
      list.style.marginTop = '15px'
    }
    if (!list.style.marginBottom) {
      list.style.marginBottom = '15px'
    }
  })
  
  // 处理列表项
  const listItems = tempContainer.querySelectorAll('li')
  listItems.forEach(item => {
    if (!item.style.marginBottom) {
      item.style.marginBottom = '10px'
    }
  })
  
  // 处理图片
  const images = tempContainer.querySelectorAll('img')
  images.forEach(img => {
    // 确保图片响应式显示
    if (!img.style.maxWidth) {
      img.style.maxWidth = '100%'
    }
    if (!img.style.height) {
      img.style.height = 'auto'
    }
    if (!img.style.marginTop) {
      img.style.marginTop = '20px'
    }
    if (!img.style.marginBottom) {
      img.style.marginBottom = '20px'
    }
    // 添加图片alt属性（如果没有）
    if (!img.alt) {
      img.alt = '图片'
    }
  })
  
  // 处理表格
  const tables = tempContainer.querySelectorAll('table')
  tables.forEach(table => {
    // 添加表格容器，确保响应式显示
    const tableWrapper = document.createElement('div')
    tableWrapper.style.overflowX = 'auto'
    tableWrapper.style.marginTop = '20px'
    tableWrapper.style.marginBottom = '20px'
    table.parentNode.insertBefore(tableWrapper, table)
    tableWrapper.appendChild(table)
    
    // 确保表格有边框和样式
    if (!table.style.borderCollapse) {
      table.style.borderCollapse = 'collapse'
    }
    if (!table.style.width) {
      table.style.width = '100%'
    }
  })
  
  // 处理表格单元格
  const tableCells = tempContainer.querySelectorAll('th, td')
  tableCells.forEach(cell => {
    if (!cell.style.padding) {
      cell.style.padding = '12px 15px'
    }
    if (!cell.style.border) {
      cell.style.border = '1px solid #dee2e6'
    }
  })
  
  // 处理引用
  const blockquotes = tempContainer.querySelectorAll('blockquote')
  blockquotes.forEach(quote => {
    if (!quote.style.backgroundColor) {
      quote.style.backgroundColor = '#f8f9fa'
    }
    if (!quote.style.borderLeft) {
      quote.style.borderLeft = '4px solid #c72c41'
    }
    if (!quote.style.padding) {
      quote.style.padding = '15px 20px'
    }
    if (!quote.style.marginTop) {
      quote.style.marginTop = '20px'
    }
    if (!quote.style.marginBottom) {
      quote.style.marginBottom = '20px'
    }
  })
  
  // 处理代码块
  const codeBlocks = tempContainer.querySelectorAll('pre')
  codeBlocks.forEach(block => {
    if (!block.style.backgroundColor) {
      block.style.backgroundColor = '#f8f9fa'
    }
    if (!block.style.padding) {
      block.style.padding = '15px'
    }
    if (!block.style.overflowX) {
      block.style.overflowX = 'auto'
    }
    if (!block.style.marginTop) {
      block.style.marginTop = '20px'
    }
    if (!block.style.marginBottom) {
      block.style.marginBottom = '20px'
    }
  })
  
  // 返回处理后的HTML内容
  return tempContainer.innerHTML
}

// 处理后的content内容
const processedContent = computed(() => {
  return contentProcessor(notice.value.content)
})

// 加载通知详情
const loadNoticeDetail = async () => {
  try {
    loading.value = true
    error.value = null
    
    // 使用真实API调用获取通知详情
    const response = await noticeApi.getNoticeDetail(noticeId.value)
    
    if (response && response.code === 200 && response.data) {
      notice.value = response.data
    } else {
      error.value = '获取通知详情失败: ' + (response?.message || '返回数据格式不正确')
      console.error('获取通知详情失败:', error.value)
    }
  } catch (err) {
    error.value = '加载通知详情失败: ' + err.message
    console.error('加载通知详情失败:', err)
  } finally {
    loading.value = false
  }
}

// 返回通知列表
const goBackToList = () => {
  router.push('/notice')
}

// 页面加载时获取数据
onMounted(() => {
  loadNoticeDetail()
  window.addEventListener('scroll', handleScroll)
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<template>
  <div class="news-list">
    <!-- 导航栏 -->
    <Navbar />
    
    <div class="page-header">
      <div class="page-header-content">
        <button class="back-btn" @click="goBackToList">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="15 18 9 12 15 6"></polyline>
          </svg>
          返回列表
        </button>
        <h2>红芯公告・炬令通传</h2>
      </div>
    </div>
    
    <!-- 加载状态 -->
    <div class="loading-container" v-if="loading">
      <div class="loading-spinner"></div>
      <p>正在加载通知详情...</p>
    </div>
    
    <!-- 通知内容 -->
    <div class="notice-content" v-else-if="notice.id">
      <!-- 头部信息 -->
      <div class="notice-header">
        <h1 class="notice-title">{{ notice.title }}</h1>
        <div class="notice-meta">
          <span class="publish-date">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
              <line x1="16" y1="2" x2="16" y2="6"></line>
              <line x1="8" y1="2" x2="8" y2="6"></line>
              <line x1="3" y1="10" x2="21" y2="10"></line>
            </svg>
            {{ notice.publishDate }}
          </span>
          <span class="category">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M19 3H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2z"></path>
              <line x1="16" y1="3" x2="8" y2="3"></line>
              <line x1="16" y1="7" x2="8" y2="7"></line>
              <polyline points="16 11 11 11 11 17"></polyline>
            </svg>
            {{ notice.category }}
          </span>
          <span class="source">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
              <polyline points="7 10 12 15 17 10"></polyline>
              <line x1="12" y1="15" x2="12" y2="3"></line>
            </svg>
            {{ notice.source }}
          </span>
        </div>
      </div>
      
      <!-- 内容主体 -->
      <div class="notice-body" v-html="processedContent"></div>
      
      <!-- 附件列表 -->
      <div class="notice-attachments" v-if="notice.attachments && notice.attachments.length > 0">
        <h3 class="attachments-title">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
            <polyline points="7 10 12 15 17 10"></polyline>
            <line x1="12" y1="15" x2="12" y2="3"></line>
          </svg>
          附件列表
        </h3>
        <ul class="attachments-list">
          <li v-for="(attachment, index) in notice.attachments" :key="index" class="attachment-item">
            <a :href="attachment.url" :download="attachment.name" class="attachment-link">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                <polyline points="14 2 14 8 20 8"></polyline>
                <line x1="16" y1="13" x2="8" y2="13"></line>
                <line x1="16" y1="17" x2="8" y2="17"></line>
                <polyline points="10 9 9 9 8 9"></polyline>
              </svg>
              <span class="attachment-name">{{ attachment.name }}</span>
              <span class="attachment-size" v-if="attachment.size">({{ formatFileSize(attachment.size) }})</span>
            </a>
          </li>
        </ul>
      </div>
      
      <!-- 操作按钮 -->
      <div class="notice-actions">
        <button class="action-btn back-to-list" @click="goBackToList">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="15 18 9 12 15 6"></polyline>
          </svg>
          返回列表
        </button>
      </div>
    </div>
    
    <!-- 错误提示 -->
    <div class="error-message" v-else>
      <div class="error-icon">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="12" r="10"></circle>
          <line x1="15" y1="9" x2="9" y2="15"></line>
          <line x1="9" y1="9" x2="15" y2="15"></line>
        </svg>
      </div>
      <p>{{ error || '未找到该通知内容，请检查通知ID是否正确。' }}</p>
      <button class="back-btn" @click="goBackToList">返回通知列表</button>
    </div>
    
    <!-- 回到顶部按钮 -->
    <button class="back-to-top-btn" v-if="showBackToTop" @click="backToTop">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="18 15 12 9 6 15"></polyline>
      </svg>
    </button>
  </div>
    <!-- 智能助手 -->
    <ChatAssistant />
</template>

<style scoped>
/* 全局样式 */
.news-list {
  min-height: 100vh;
  background-color: #f8f9fa;
  position: relative;
}

/* 页面头部 */
.page-header {
  background-color: #c72c41;
  color: white;
  padding: 30px 0;
  width: 100%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.page-header-content {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.page-header h2 {
  font-size: 26px;
  font-weight: 700;
  margin: 0;
  color: white;
  letter-spacing: 0.5px;
}

/* 返回按钮 */
.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  border: 1px solid white;
  border-radius: 6px;
  background-color: transparent;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 15px;
  font-weight: 500;
}

.back-btn:hover {
  background-color: white;
  color: #c72c41;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 20px;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #c72c41;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 通知内容容器 */
.notice-content {
  background-color: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  max-width: 1000px;
  margin: 30px auto;
  width: calc(100% - 40px);
}

/* 通知头部 */
.notice-header {
  margin-bottom: 35px;
  padding-bottom: 25px;
  border-bottom: 1px solid #e8e8e8;
}

.notice-title {
  font-size: 28px;
  color: #2c3e50;
  margin-bottom: 20px;
  font-weight: 700;
  line-height: 1.3;
  text-align: center;
}

/* 通知元信息 */
.notice-meta {
  display: flex;
  justify-content: center;
  gap: 35px;
  font-size: 14px;
  color: #6c757d;
  flex-wrap: wrap;
}

.notice-meta span {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.notice-meta .category {
  background-color: #f8d7da;
  padding: 4px 12px;
  border-radius: 12px;
  color: #721c24;
  font-weight: 500;
}

.notice-meta .publish-date, .notice-meta .source {
  opacity: 0.9;
}

/* 通知正文内容 */
.notice-body {
  font-size: 16px;
  color: #495057;
  line-height: 2.0;
  word-break: break-word;
}

/* 正文排版规范 */
.notice-body h1 {
  font-size: 26px;
  font-weight: 700;
  color: #2c3e50;
  margin: 35px 0 20px 0;
  line-height: 1.4;
}

.notice-body h2 {
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
  margin: 30px 0 18px 0;
  line-height: 1.4;
}

.notice-body h3 {
  font-size: 22px;
  font-weight: 600;
  color: #2c3e50;
  margin: 25px 0 16px 0;
  line-height: 1.4;
}

.notice-body h4 {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
  margin: 20px 0 14px 0;
  line-height: 1.4;
}

.notice-body h5 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin: 18px 0 12px 0;
  line-height: 1.4;
}

.notice-body h6 {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin: 15px 0 10px 0;
  line-height: 1.4;
}

.notice-body p {
  margin-bottom: 18px;
  text-align: justify;
}

.notice-body strong {
  font-weight: 600;
  color: #2c3e50;
}

.notice-body em {
  font-style: italic;
  color: #6c757d;
}

/* 列表样式 */
.notice-body ol, .notice-body ul {
  margin: 0 0 20px 20px;
  padding-left: 30px;
}

/* 嵌套列表样式 */
.notice-body ol ol, .notice-body ol ul, .notice-body ul ol, .notice-body ul ul {
  margin: 10px 0 10px 0;
  padding-left: 25px;
}

.notice-body ol {
  list-style-type: decimal;
}

.notice-body ul {
  list-style-type: disc;
}

.notice-body li {
  margin-bottom: 10px;
  line-height: 1.8;
}

/* 图片样式 */
.notice-body img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 20px 0;
  display: block;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 表格样式 */
.notice-body table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
  font-size: 14px;
}

.notice-body table th,
.notice-body table td {
  padding: 12px 15px;
  border: 1px solid #dee2e6;
  text-align: left;
}

.notice-body table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #2c3e50;
}

.notice-body table tr:nth-child(even) {
  background-color: #f8f9fa;
}

/* 引用样式 */
.notice-body blockquote {
  margin: 20px 0;
  padding: 15px 20px;
  background-color: #f8f9fa;
  border-left: 4px solid #c72c41;
  color: #6c757d;
  font-style: italic;
}

/* 代码块样式 */
.notice-body pre {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 6px;
  overflow-x: auto;
  margin: 20px 0;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.6;
}

.notice-body code {
  background-color: #f8f9fa;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 14px;
  color: #d63384;
}

/* 附件列表 */
.notice-attachments {
  margin-top: 40px;
  padding-top: 25px;
  border-top: 1px solid #e8e8e8;
}

.attachments-title {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 18px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.attachments-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.attachment-item {
  margin-bottom: 12px;
}

.attachment-link {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
  color: #3498db;
  text-decoration: none;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.attachment-link:hover {
  background-color: #e9ecef;
  color: #2980b9;
  border-color: #dee2e6;
  transform: translateX(2px);
}

.attachment-name {
  flex: 1;
  font-weight: 500;
  font-size: 15px;
}

.attachment-size {
  font-size: 13px;
  color: #6c757d;
  opacity: 0.8;
}

/* 操作按钮 */
.notice-actions {
  margin-top: 35px;
  padding-top: 20px;
  border-top: 1px solid #e8e8e8;
  display: flex;
  justify-content: flex-end;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 15px;
  font-weight: 500;
  transition: all 0.3s ease;
  border: none;
}

.action-btn.back-to-list {
  background-color: #f8f9fa;
  color: #495057;
  border: 1px solid #dee2e6;
}

.action-btn.back-to-list:hover {
  background-color: #e9ecef;
  color: #2c3e50;
  transform: translateY(-1px);
}

/* 错误提示 */
.error-message {
  text-align: center;
  padding: 80px 20px;
  color: #721c24;
  background-color: white;
  max-width: 800px;
  margin: 30px auto;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.error-icon {
  margin-bottom: 20px;
  color: #dc3545;
}

.error-message p {
  font-size: 16px;
  margin-bottom: 25px;
  line-height: 1.5;
}

/* 回到顶部按钮 */
.back-to-top-btn {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #c72c41;
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  z-index: 1000;
  opacity: 0.9;
}

.back-to-top-btn:hover {
  background-color: #a82535;
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
  opacity: 1;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header-content {
    padding: 0 15px;
  }
  
  .page-header h2 {
    font-size: 22px;
  }
  
  .back-btn {
    padding: 8px 14px;
    font-size: 14px;
  }
  
  .notice-content {
    padding: 25px;
    width: calc(100% - 30px);
  }
  
  .notice-title {
    font-size: 22px;
  }
  
  .notice-meta {
    gap: 20px;
    font-size: 13px;
  }
  
  .notice-body {
    font-size: 15px;
    line-height: 1.8;
  }
  
  .notice-body h1 {
    font-size: 24px;
  }
  
  .notice-body h2 {
    font-size: 22px;
  }
  
  .notice-body h3 {
    font-size: 20px;
  }
  
  .notice-meta .category {
    padding: 3px 10px;
  }
  
  .back-to-top-btn {
    width: 45px;
    height: 45px;
    bottom: 20px;
    right: 20px;
  }
}

@media (max-width: 480px) {
  .page-header {
    padding: 20px 0;
  }
  
  .page-header-content {
    flex-direction: column;
    gap: 15px;
  }
  
  .notice-title {
    font-size: 20px;
  }
  
  .notice-meta {
    flex-direction: column;
    gap: 10px;
    align-items: center;
  }
  
  .notice-content {
    padding: 20px;
  }
  
  .notice-body {
    font-size: 14px;
  }
}
</style>