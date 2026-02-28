<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import Navbar from '@/components/front/Navbar.vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import noticeApi from '@/api/notice.js'

const router = useRouter()
const userStore = useUserStore()

// 通知列表数据
const notices = ref([])
// 分页数据
const pagination = ref({
  page: 1,
  pageSize: 10,
  total: 0,
  pages: 0
})

// 搜索和筛选参数
const searchKeyword = ref('')
const selectedCategoryId = ref(null)

// 公告分类列表 (根据接口文档固定定义)
const noticeCategories = [
  { id: 1, name: '工作通知' },
  { id: 2, name: '学习通知' },
  { id: 3, name: '会议通知' },
  { id: 4, name: '放假通知' },
  { id: 5, name: '活动通知' },
  { id: 6, name: '其他通知' }
]

// 加载通知列表
const loadNotices = async () => {
  try {
    const params = {
      page: pagination.value.page,
      pageSize: pagination.value.pageSize
    }
    
    // 添加搜索关键词和分类筛选参数
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    
    // 添加分类筛选参数（categoryId）
    if (selectedCategoryId.value !== null && selectedCategoryId.value !== '') {
      params.categoryId = parseInt(selectedCategoryId.value)
    }
    
    const data = await noticeApi.getNoticeList(params)
    
    // 检查API返回的数据格式是否正确
    if (data && data.code === 200 && data.data) {
      // 支持分页数据结构（content）和直接返回数组的格式（notices）
      notices.value = Array.isArray(data.data.content) ? data.data.content : 
                      Array.isArray(data.data.notices) ? data.data.notices : []
      
      // 更新分页信息
      pagination.value.total = data.data.total || data.data.totalElements || 0
      pagination.value.pages = data.data.pages || data.data.totalPages || 0
    } else {
      console.error('获取通知列表失败：返回数据格式不正确', data)
      notices.value = [] // 确保notices始终是数组
    }
  } catch (error) {
    console.error('加载通知列表失败:', error)
  }
}

// 搜索通知
const searchNotices = () => {
  pagination.value.page = 1 // 重置到第一页
  loadNotices()
}

// 重置搜索
const resetSearch = () => {
  searchKeyword.value = ''
  selectedCategoryId.value = null
  pagination.value.page = 1 // 重置到第一页
  loadNotices()
}

// 查看通知详情
const viewNoticeDetail = (id) => {
  router.push(`/notice/${id}`)
}

// 页面加载时获取数据
onMounted(() => {
  loadNotices()
})
</script>

<template>
  <div class="news-list">
    <!-- 导航栏 -->
    <Navbar />
    
    <div class="page-header">
      <h2>红芯公告・炬令通传</h2>
    </div>
    
    <!-- 搜索和筛选 -->
    <div class="search-filter">
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchKeyword" 
          placeholder="搜索公告标题或内容"
          @keyup.enter="searchNotices"
        />
        <button @click="searchNotices">搜索</button>
        <button @click="resetSearch">重置</button>
      </div>
      <div class="category-filter">
        <select v-model="selectedCategoryId">
          <option value="">全部分类</option>
          <option v-for="category in noticeCategories" :key="category.id" :value="category.id">
            {{ category.name }}
          </option>
        </select>
      </div>
    </div>
    
    <div class="notice-list">
      <div 
        v-for="notice in notices" 
        :key="notice.id" 
        class="notice-item"
        @click="viewNoticeDetail(notice.id)"
      >
        <div class="notice-title">{{ notice.title }}</div>
        <div class="notice-meta">
          <span class="publish-date">{{ notice.publishDate }}</span>
          <span class="category">{{ notice.category }}</span>
          <span class="source">{{ notice.source }}</span>
        </div>
      </div>
      <div v-if="notices.length === 0" class="no-notices">
        <p>没有找到符合条件的公告</p>
      </div>
    </div>
    
    <!-- 分页组件 -->
    <div class="pagination" v-if="pagination.total > 0">
      <button 
        class="page-btn" 
        :disabled="pagination.page === 1"
        @click="pagination.page--; loadNotices()"
      >
        上一页
      </button>
      <span class="page-info">第 {{ pagination.page }} 页 / 共 {{ pagination.pages }} 页</span>
      <button 
        class="page-btn" 
        :disabled="pagination.page === pagination.pages"
        @click="pagination.page++; loadNotices()"
      >
        下一页
      </button>
    </div>
    
    <!-- 智能助手 -->
    <ChatAssistant />
  </div>
</template>

<style scoped>
.news-list {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.page-header {
  background-color: #c72c41;
  color: white;
  padding: 40px 0;
  text-align: center;
  width: 100%;
}

.page-header h2 {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 30px auto 30px;
}

.notice-item {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: white;
}

.notice-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
  border-color: #d93025;
}

.notice-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.notice-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #666;
}

.notice-meta .category {
  background-color: #f0f0f0;
  padding: 2px 8px;
  border-radius: 4px;
  color: #d93025;
}

/* 搜索筛选样式 */
.search-filter {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 30px auto;
  flex-wrap: wrap;
  gap: 20px;
}

.search-box {
  display: flex;
  gap: 10px;
  flex: 1;
  min-width: 300px;
}

.search-box input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.search-box button {
  padding: 10px 20px;
  border: 1px solid #d93025;
  border-radius: 4px;
  background-color: white;
  color: #d93025;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-box button:hover {
  background-color: #d93025;
  color: white;
}

.category-filter select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  background-color: white;
}

/* 无公告提示 */
.no-notices {
  text-align: center;
  padding: 40px 0;
  color: #666;
  font-size: 16px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 30px;
  max-width: 1200px;
  margin: 30px auto;
  padding: 0 20px;
}

.page-btn {
  padding: 8px 16px;
  border: 1px solid #d93025;
  border-radius: 4px;
  background-color: white;
  color: #d93025;
  cursor: pointer;
  transition: all 0.3s ease;
}

.page-btn:hover:not(:disabled) {
  background-color: #d93025;
  color: white;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #666;
}
</style>