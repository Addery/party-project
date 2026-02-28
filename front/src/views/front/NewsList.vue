<template>
  <div class="news-list">
    <!-- 导航栏 -->
    <Navbar />
    
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>红芯资讯・薪火速递</h2>
    </div>

    <!-- 筛选和搜索 -->
    <div class="news-filter">
      <div class="filter-section">
        <label for="category">新闻分类：</label>
        <select id="category" v-model="selectedCategory" @change="filterNews">
          <option value="">全部</option>
          <option v-for="category in newsCategories" :key="category.id" :value="category.name">
            {{ categoryMap[category.name] || category.name }}
          </option>
        </select>
      </div>
      <div class="search-section">
        <input 
          type="text" 
          v-model="searchKeyword" 
          @input="filterNews" 
          placeholder="搜索新闻标题..." 
          class="search-input"
        />
        <button class="btn btn-search">搜索</button>
      </div>
    </div>

    <!-- 新闻列表 -->
    <div class="news-container">

      
      <div class="news-item" v-for="(news, index) in paginatedNews" :key="news.id">
        <div v-if="news.image" class="news-image">
          <img :src="news.image" :alt="news.title" />
        </div>
        <div class="news-info">
          <h3 class="news-title">{{ index + 1 }}. {{ news.title }}</h3>
          <div class="news-meta">
            <span class="news-date">{{ news.date }}</span>
            <span class="news-category">{{ categoryMap[news.category] || news.category }}</span>
          </div>
          <p class="news-summary">{{ news.summary }}</p>
          <div class="news-author">作者：{{ news.author }}</div>
          <button class="btn btn-primary" @click="viewDetail(news.id)">查看详情</button>
        </div>
      </div>
      <div v-if="paginatedNews.length === 0" class="no-news">
        <p>没有找到符合条件的新闻</p>
      </div>

    </div>

    <!-- 分页 -->
    <div class="pagination">
      <button class="btn btn-page" :disabled="currentPage === 1" @click="prevPage">上一页</button>
      <div class="page-numbers">
        <button 
          v-for="page in totalPages" 
          :key="page"
          class="btn btn-page-number" 
          :class="{ active: currentPage === page }"
          @click="goToPage(page)"
        >
          {{ page }}
        </button>
      </div>
      <button class="btn btn-page" :disabled="currentPage === totalPages" @click="nextPage">下一页</button>
    </div>
  </div>
    <!-- 智能助手 -->
    <ChatAssistant />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/front/Navbar.vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
import newsApi from '@/api/news.js'

const router = useRouter()

// 新闻列表数据
const newsList = ref([])

// 加载状态
const loading = ref(false)
// 错误信息
const error = ref(null)

// 筛选和搜索参数
const selectedCategory = ref('')
const searchKeyword = ref('')

// 新闻分类
const newsCategories = ref([])

// 英文到中文的分类映射表（与后端分类枚举类保持一致）
const categoryMap = {
  'policy': '时政要闻',
  'party_building': '党建动态',
  'organization': '组织建设',
  'technology': '科技创新',
  'party_discipline': '党风廉政',
  'grassroots_party': '基层党建',
  'law': '法律法规',
  'economy': '经济发展',
  'health': '卫生健康',
  'society': '社会民生',
  'environment': '环境保护',
  'education': '教育工作',
  'culture': '文化动态',
  'sports': '体育新闻',
  'safety': '安全警示'
}

// 分页参数
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)

// 过滤后的新闻列表
const filteredNews = computed(() => {
  let result = [...newsList.value]
  
  // 分类筛选
  if (selectedCategory.value) {
    result = result.filter(news => {
      // 同时支持中英文分类匹配
      return news.category === selectedCategory.value || 
             news.category === Object.keys(categoryMap).find(key => categoryMap[key] === selectedCategory.value)
    })
  }
  
  // 按关键词搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(news => news.title.toLowerCase().includes(keyword))
  }
  
  return result
})

// 分页后的新闻列表
const paginatedNews = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  const endIndex = startIndex + pageSize.value
  return filteredNews.value.slice(startIndex, endIndex)
})

// 总页数
const totalPages = computed(() => {
  return Math.ceil(filteredNews.value.length / pageSize.value) || 1
})

// 筛选新闻
const filterNews = async () => {
  currentPage.value = 1 // 筛选时重置到第一页
  await fetchNewsList()
}

// 获取新闻列表
const fetchNewsList = async () => {
  try {
    loading.value = true
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    // 如果有分类筛选，添加到请求参数中
    if (selectedCategory.value) {
      params.category = selectedCategory.value
    }
    
    // 如果有关键词搜索，添加到请求参数中
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    
    const response = await newsApi.getNewsList(params)
    
    // 检查API返回的数据格式是否正确
    if (!response) {
      error.value = '获取新闻列表失败，请稍后重试'
      return
    }
    
    if (response.code !== 200) {
      error.value = response.message || '获取新闻列表失败，请稍后重试'
      return
    }
    
    if (!response.data) {
      error.value = '获取新闻列表失败，请稍后重试'
      return
    }
    
    // 根据API实际返回结构，增加对response.data.data.newsList等字段的支持
    const newsData = response.data.data?.newsList || response.data.data?.content || response.data.data?.news || response.data.data?.list || response.data.data?.data || response.data.newsList || response.data.content || response.data.news || response.data.list || response.data.data
    
    if (!Array.isArray(newsData)) {
      // 如果newsData不是数组，尝试检查response.data本身是否是数组
      if (Array.isArray(response.data)) {
        console.log('发现response.data本身是数组，使用它作为新闻数据')
        newsList.value = response.data.map(news => ({
          id: news.id,
          title: news.title,
          date: news.publishDate || news.date,
          category: news.category,
          summary: news.description || news.summary,
          author: news.author,
          image: news.coverImage || news.cover || news.imageUrl || ''
        }))
        
        // 尝试从response或response.data中获取分页信息
        total.value = response.total || response.data.total || response.data.totalCount || 0
        currentPage.value = response.page || response.data.page || 1
        pageSize.value = response.pageSize || response.data.pageSize || 10
        
        return
      } else {
        error.value = '获取新闻列表失败，请稍后重试'
        return
      }
    }
    
    // 映射API返回数据到组件需要的格式
    const mappedNews = newsData.map(news => ({
      id: news.id,
      title: news.title,
      date: news.publishDate || news.date,
      category: news.category,
      summary: news.description || news.summary,
      author: news.author,
      image: news.coverImage || news.cover || news.imageUrl || ''
    }))
    newsList.value = mappedNews
    
    // 更新分页信息
    total.value = response.data.data?.total || response.data.data?.totalCount || response.data.total || response.data.totalCount || 0
    currentPage.value = response.data.data?.page || response.data.page || 1
    pageSize.value = response.data.data?.pageSize || response.data.pageSize || 10
  } catch (err) {
    error.value = '获取新闻列表失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 分页操作
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

// 查看详情
const viewDetail = (id) => {
  router.push({ name: 'NewsDetail', params: { id } })
}

// 获取新闻分类
const fetchNewsCategories = async () => {
  try {
    const response = await newsApi.getNewsCategories()
    console.log('新闻分类API返回数据:', response)
    
    if (response.code === 200 && Array.isArray(response.data)) {
      // API返回的是字符串数组，转换为对象数组（包含id和name属性）
      newsCategories.value = response.data.map((category, index) => ({
        id: index + 1,  // 生成ID
        name: category  // 使用分类名称
      }))
      console.log('处理后的新闻分类数据:', newsCategories.value)
    } else {
      console.error('获取新闻分类失败：返回数据格式不正确', response)
      // 如果API调用失败，使用与后端一致的默认英文分类数组
      newsCategories.value = [
        { id: 1, name: 'policy' },
        { id: 2, name: 'party_building' },
        { id: 3, name: 'organization' },
        { id: 4, name: 'technology' },
        { id: 5, name: 'party_discipline' },
        { id: 6, name: 'grassroots_party' },
        { id: 7, name: 'law' },
        { id: 8, name: 'economy' },
        { id: 9, name: 'health' },
        { id: 10, name: 'society' },
        { id: 11, name: 'environment' },
        { id: 12, name: 'education' },
        { id: 13, name: 'culture' },
        { id: 14, name: 'sports' },
        { id: 15, name: 'safety' }
      ]
    }
  } catch (err) {
    console.error('获取新闻分类失败:', err)
    // 出错时使用与后端一致的默认英文分类数组
    newsCategories.value = [
      { id: 1, name: 'policy' },
      { id: 2, name: 'party_building' },
      { id: 3, name: 'organization' },
      { id: 4, name: 'technology' },
      { id: 5, name: 'party_discipline' },
      { id: 6, name: 'grassroots_party' },
      { id: 7, name: 'law' },
      { id: 8, name: 'economy' },
      { id: 9, name: 'health' },
      { id: 10, name: 'society' },
      { id: 11, name: 'environment' },
      { id: 12, name: 'education' },
      { id: 13, name: 'culture' },
      { id: 14, name: 'sports' },
      { id: 15, name: 'safety' }
    ]
  }
}

// 页面加载时获取新闻列表和分类
onMounted(async () => {
  console.log('=== 页面加载开始 ===')
  await Promise.all([
    fetchNewsList(),
    fetchNewsCategories()
  ])
  console.log('=== 页面加载完成 ===')
  console.log('最终newsList:', newsList.value)
  console.log('最终filteredNews:', filteredNews.value)
  console.log('最终paginatedNews:', paginatedNews.value)
  console.log('最终totalPages:', totalPages.value)
})
</script>

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
  color: white;
}

/* 筛选和搜索 */
.news-filter {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 30px auto;
  padding: 20px;
}

.filter-section {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-section label {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.filter-section select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
}

.search-section {
  display: flex;
  gap: 10px;
}

.search-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  width: 300px;
}

.search-input:focus {
  outline: none;
  border-color: #d93025;
}

/* 新闻列表 */
.news-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 0 auto 30px;
}

.news-item {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  display: flex;
  gap: 20px;
}

.news-item:hover {
  transform: translateY(-5px);
}

.news-image {
  width: 200px;
  height: 130px;
  overflow: hidden;
  border-radius: 4px;
  flex-shrink: 0;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.news-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.news-title {
  font-size: 20px;
  color: #2c3e50;
  margin: 0 0 10px 0;
  line-height: 1.4;
}

.news-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #7f8c8d;
}

.news-summary {
  color: #666;
  margin: 0 0 10px 0;
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.news-author {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
}

.no-news {
  text-align: center;
  padding: 40px;
  color: #666;
  font-size: 16px;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  max-width: 1200px;
  margin: 0 auto 30px;
  padding: 0 20px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: #d93025;
  color: white;
  margin-top: auto;
  align-self: flex-start;
}

.btn-primary:hover {
  background: #c0392b;
}

.btn-search {
  background: #d93025;
  color: white;
}

.btn-search:hover {
  background: #c0392b;
}

.btn-page {
  background: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
}

.btn-page:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-page-number {
  width: 36px;
  height: 36px;
  background: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
}

.btn-page-number.active {
  background: #d93025;
  color: white;
  border-color: #d93025;
}

.btn-page-number.active {
  background: #d93025;
  color: white;
  border-color: #d93025;
}
</style>