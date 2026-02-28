// 简单测试分类过滤逻辑

// 模拟数据
const newsList = [
  { id: 1, title: '嫦娥七号完成首次月球背面采样任务', category: 'technology', date: '2023-12-15' },
  { id: 2, title: '量子通信技术取得重大突破', category: 'technology', date: '2023-12-10' },
  { id: 3, title: '中国5G网络覆盖超全国所有地级市', category: 'technology', date: '2023-12-05' },
  { id: 4, title: '人工智能在医疗诊断领域应用研究获进展', category: 'technology', date: '2023-12-01' },
  { id: 5, title: '我国新一代运载火箭长征十号完成关键测试', category: 'technology', date: '2023-11-28' }
]

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

// 测试场景：selectedCategory是中文"科技创新"
const selectedCategory = '科技创新'

console.log('=== 简单分类过滤测试 ===')
console.log('selectedCategory:', selectedCategory)
console.log('新闻列表数据:', newsList)

// 测试findKey函数
const findKey = () => {
  const key = Object.keys(categoryMap).find(key => categoryMap[key] === selectedCategory)
  console.log('通过中文查找英文键:', key)
  return key
}

const key = findKey()

// 测试过滤逻辑
const filterNews = () => {
  return newsList.filter(news => {
    const match1 = news.category === selectedCategory
    const match2 = news.category === key
    console.log(`新闻分类: ${news.category}, match1: ${match1}, match2: ${match2}`)
    return match1 || match2
  })
}

const filtered = filterNews()
console.log('过滤结果:', filtered)
console.log('过滤结果数量:', filtered.length)
