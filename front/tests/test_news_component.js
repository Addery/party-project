// 测试新闻组件的核心功能

// 模拟Vue 3的ref和computed
function ref(value) {
  return { value }
}

function computed(fn) {
  return {
    get value() {
      return fn()
    }
  }
}

// 模拟数据
const newsList = ref([
  { id: 1, title: '嫦娥七号完成首次月球背面采样任务', category: 'technology', date: '2023-12-15', description: '这是一条关于嫦娥七号的新闻' },
  { id: 2, title: '量子通信技术取得重大突破', category: 'technology', date: '2023-12-10', description: '这是一条关于量子通信的新闻' },
  { id: 3, title: '中国5G网络覆盖超全国所有地级市', category: 'technology', date: '2023-12-05', description: '这是一条关于5G的新闻' },
  { id: 4, title: '人工智能在医疗诊断领域应用研究获进展', category: 'technology', date: '2023-12-01', description: '这是一条关于人工智能的新闻' },
  { id: 5, title: '我国新一代运载火箭长征十号完成关键测试', category: 'technology', date: '2023-11-28', description: '这是一条关于长征十号的新闻' }
])

// 分页参数
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(5)

// 筛选参数
const selectedCategory = ref('technology')
const searchKeyword = ref('')

// 分类映射
const categoryMap = {
  'policy': '时政要闻',
  'party_building': '党建动态',
  'organization': '组织建设',
  'technology': '科技创新',
  'party_discipline': '党风廉政',
  'grassroots_party': '基层党建',
  'law': '法治建设',
  'economy': '经济发展',
  'health': '健康医疗',
  'society': '社会民生',
  'environment': '环境保护',
  'education': '教育工作',
  'culture': '文化动态',
  'sports': '体育新闻',
  'safety': '安全警示'
}

// 过滤后的新闻列表
const filteredNews = computed(() => {
  let result = [...newsList.value]
  
  console.log('=== 过滤逻辑开始 ===')
  console.log('1. 原始新闻列表:', result)
  console.log('2. 当前selectedCategory:', selectedCategory.value)
  console.log('3. 当前selectedCategory类型:', typeof selectedCategory.value)
  console.log('4. categoryMap:', categoryMap)
  
  // 按分类筛选
  if (selectedCategory.value) {
    console.log('5. 开始分类筛选...')
    
    // 先检查selectedCategory是否已经是英文分类
    const isEnglish = Object.keys(categoryMap).includes(selectedCategory.value)
    console.log('6. selectedCategory是否为英文分类:', isEnglish)
    
    // 如果是中文分类，查找对应的英文键
    const englishCategory = isEnglish ? selectedCategory.value : Object.keys(categoryMap).find(key => categoryMap[key] === selectedCategory.value)
    console.log('7. 对应的英文分类:', englishCategory)
    
    // 检查新闻列表中的分类与目标分类的匹配情况
    const matchInfo = result.map(news => ({
      id: news.id,
      category: news.category,
      categoryName: categoryMap[news.category],
      matchesDirectly: news.category === selectedCategory.value,
      matchesEnglish: news.category === englishCategory
    }))
    console.log('8. 新闻分类匹配详情:', matchInfo)
    
    // 执行过滤
    result = result.filter(news => {
      return news.category === selectedCategory.value || news.category === englishCategory
    })
    console.log('9. 分类过滤后的结果:', result)
  }
  
  // 按关键词搜索
  if (searchKeyword.value) {
    console.log('10. 开始关键词搜索:', searchKeyword.value)
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(news => news.title.toLowerCase().includes(keyword))
    console.log('11. 关键词过滤后的结果:', result)
  }
  
  console.log('12. 最终过滤结果:', result)
  console.log('=== 过滤逻辑结束 ===')
  return result
})

// 分页后的新闻列表
const paginatedNews = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  const endIndex = startIndex + pageSize.value
  const result = filteredNews.value.slice(startIndex, endIndex)
  console.log('=== 分页逻辑 ===')
  console.log('1. 过滤后的新闻:', filteredNews.value)
  console.log('2. 当前页:', currentPage.value)
  console.log('3. 每页大小:', pageSize.value)
  console.log('4. 起始索引:', startIndex)
  console.log('5. 结束索引:', endIndex)
  console.log('6. 分页后的结果:', result)
  return result
})

// 总页数
const totalPages = computed(() => {
  return Math.ceil(total.value / pageSize.value) || 1
})

// 测试不同场景
console.log('=== 测试场景1：英文分类 technology ===')
selectedCategory.value = 'technology'
console.log('filteredNews长度:', filteredNews.value.length)
console.log('paginatedNews长度:', paginatedNews.value.length)
console.log('paginatedNews内容:', paginatedNews.value)

console.log('\n=== 测试场景2：中文分类 科技创新 ===')
selectedCategory.value = '科技创新'
console.log('filteredNews长度:', filteredNews.value.length)
console.log('paginatedNews长度:', paginatedNews.value.length)
console.log('paginatedNews内容:', paginatedNews.value)

console.log('\n=== 测试场景3：空分类 ===')
selectedCategory.value = ''
console.log('filteredNews长度:', filteredNews.value.length)
console.log('paginatedNews长度:', paginatedNews.value.length)
console.log('paginatedNews内容:', paginatedNews.value)

console.log('\n=== 测试场景4：禁用分类筛选 ===')
// 模拟禁用分类筛选的情况
const filteredNewsNoCategory = computed(() => {
  let result = [...newsList.value]
  
  // 按关键词搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(news => news.title.toLowerCase().includes(keyword))
  }
  
  return result
})

const paginatedNewsNoCategory = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize.value
  const endIndex = startIndex + pageSize.value
  return filteredNewsNoCategory.value.slice(startIndex, endIndex)
})

console.log('filteredNews长度:', filteredNewsNoCategory.value.length)
console.log('paginatedNews长度:', paginatedNewsNoCategory.value.length)
console.log('paginatedNews内容:', paginatedNewsNoCategory.value)
