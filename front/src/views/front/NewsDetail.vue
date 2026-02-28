<template>
  <div class="news-detail">
    <!-- 导航栏 -->
    <Navbar />
    
    <!-- 页面标题和返回按钮 -->
    <div class="page-header">
      <div class="page-header-content">
        <button class="btn btn-back" @click="goBack">返回列表</button>
        <h2>红芯资讯・薪火速递</h2>
      </div>
    </div>

    <!-- 新闻内容 -->
    <div class="news-content">
      <h1 class="news-title">{{ newsDetail.title }}</h1>
      <div class="news-meta">
        <span class="news-date">{{ newsDetail.date }}</span>
        <span class="news-category">{{ newsDetail.category }}</span>
        <span class="news-author">作者：{{ newsDetail.author }}</span>
      </div>
      
      <!-- 新闻图片 -->
      <div v-if="newsDetail.image" class="news-image">
        <img :src="newsDetail.image" :alt="newsDetail.title" />
      </div>
      
      <div class="news-body" v-html="newsDetail.content"></div>
      
      <!-- 相关新闻 -->
      <div class="related-news">
        <h3>相关新闻</h3>
        <div class="related-news-list">
          <div class="related-news-item" v-for="item in relatedNews" :key="item.id">
            <a @click="viewAnotherNews(item.id)">{{ item.title }}</a>
          </div>
          <div v-if="relatedNews.length === 0" class="no-related">
            <p>暂无相关新闻</p>
          </div>
        </div>
      </div>
    </div>
  </div>
    <!-- 智能助手 -->
    <ChatAssistant />
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Navbar from '@/components/front/Navbar.vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import { useUserStore } from '@/stores/user'
import newsApi from '@/api/news.js'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 新闻详情数据
const newsDetail = ref({
  id: null,
  title: '',
  date: '',
  category: '',
  author: '',
  image: '',
  content: ''
})

// 加载状态
const loading = ref(false)
// 错误信息
const error = ref(null)
// 相关新闻数据
const relatedNewsData = ref([])



// 获取新闻详情
const getNewsDetail = async (id) => {
  try {
    loading.value = true
    const response = await newsApi.getNewsDetail(id)
    
    // 检查API返回的数据格式是否正确
    if (!response || response.code !== 200 || !response.data) {
      console.error('获取新闻详情失败：返回数据格式不正确', response)
      error.value = '获取新闻详情失败，请稍后重试'
      // 如果找不到新闻，返回404或默认页面
      router.push('/404')
      return
    }
    
    const news = response.data
    // 映射API返回数据到组件需要的格式
    newsDetail.value = {
      id: news.id,
      title: news.title,
      date: news.publishDate || news.date,
      category: news.category,
      author: news.source || news.author, // 使用source或author作为作者
      image: news.imageUrl || news.coverImage || news.cover || '', // 获取图片URL，支持多种可能的字段名
      content: news.content // API返回的完整内容
    }
    
    // 如果用户已登录，添加到浏览记录
    if (userStore.isLoggedIn) {
      userStore.addBrowsingHistory({
        id: news.id,
        type: 'news',
        title: news.title,
        category: news.category,
        timestamp: new Date().toISOString()
      })
    }
    
    // 获取相关新闻
    getRelatedNews(news.category, news.id)
  } catch (err) {
    console.error('获取新闻详情失败:', err)
    error.value = '获取新闻详情失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 获取相关新闻
const getRelatedNews = async (category, excludeId) => {
  try {
    const response = await newsApi.getRelatedNews(category, excludeId)
    
    // 检查API返回的数据格式是否正确
    if (!response || response.code !== 200 || !Array.isArray(response.data)) {
      console.error('获取相关新闻失败：返回数据格式不正确', response)
      return
    }
    
    relatedNewsData.value = response.data.map(item => ({
      id: item.id,
      title: item.title,
      category: item.category
    }))
  } catch (err) {
    console.error('获取相关新闻失败:', err)
  }
}

// 返回列表
const goBack = () => {
  router.go(-1)
}

// 查看另一篇新闻
const viewAnotherNews = (id) => {
  // 更新URL参数
  router.push({ name: 'NewsDetail', params: { id } })
  // 获取新的新闻详情
  getNewsDetail(id)
}

// 相关新闻
const relatedNews = computed(() => {
  // 获取与当前新闻同一分类的其他新闻，最多显示3条
  return relatedNewsData.value.slice(0, 3)
})

// 页面加载时获取新闻详情
onMounted(async () => {
  await getNewsDetail(route.params.id)
})


</script>

<style scoped>
.news-detail {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background-color: #c72c41;
  color: white;
  padding: 40px 0;
  width: 100%;
}

.page-header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.btn-back {
  background-color: transparent;
  color: white;
  border: 1px solid white;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.btn-back:hover {
  background-color: white;
  color: #c72c41;
}

.news-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 30px auto;
  width: calc(100% - 40px);
}

.news-title {
  font-size: 28px;
  color: #2c3e50;
  margin-bottom: 20px;
  line-height: 1.4;
}

.news-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  font-size: 14px;
  color: #7f8c8d;
  border-bottom: 1px solid #eee;
  padding-bottom: 20px;
}

.news-image {
  margin-bottom: 30px;
  text-align: center;
}

.news-image img {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
}

.news-body {
  color: #333;
  line-height: 1.8;
  margin-bottom: 40px;
}

.news-body p {
  margin-bottom: 15px;
}

.news-body h4 {
  font-size: 18px;
  margin: 20px 0 10px 0;
  color: #2c3e50;
}

/* 相关新闻 */
.related-news {
  border-top: 1px solid #eee;
  padding-top: 30px;
}

.related-news h3 {
  font-size: 20px;
  margin-bottom: 20px;
  color: #2c3e50;
}

.related-news-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.related-news-item {
  padding: 10px 0;
  border-bottom: 1px dashed #eee;
}

.related-news-item:last-child {
  border-bottom: none;
}

.related-news-item a {
  color: #3498db;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.3s;
}

.related-news-item a:hover {
  color: #2980b9;
  text-decoration: underline;
}

.no-related {
  text-align: center;
  color: #7f8c8d;
  padding: 20px 0;
}
</style>