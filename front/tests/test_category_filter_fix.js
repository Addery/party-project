// 测试分类过滤修复逻辑

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
  'policy': '政策法规',
  'party_building': '党建工作',
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

// 测试修复后的过滤逻辑
function testFilterLogic(selectedCategory) {
  let result = [...newsList]
  
  console.log('\n=== 测试分类过滤逻辑 ===')
  console.log('过滤前的新闻列表:', newsList)
  console.log('当前选中的分类:', selectedCategory)
  console.log('新闻分类与选中分类的对比:', result.map(news => ({ 
    category: news.category, 
    categoryName: categoryMap[news.category],
    match: news.category === selectedCategory || 
           news.category === Object.keys(categoryMap).find(key => categoryMap[key] === selectedCategory)
  })))
  
  // 按分类筛选
  if (selectedCategory) {
    result = result.filter(news => {
      // 同时支持中英文分类匹配
      return news.category === selectedCategory || 
             news.category === Object.keys(categoryMap).find(key => categoryMap[key] === selectedCategory)
    })
  }
  
  console.log('过滤后的新闻列表:', result)
  console.log('过滤结果数量:', result.length)
  return result
}

// 测试1：传入英文分类
console.log('\n--- 测试1：传入英文分类 "technology" ---')
const result1 = testFilterLogic('technology')

// 测试2：传入中文分类
console.log('\n--- 测试2：传入中文分类 "科技创新" ---')
const result2 = testFilterLogic('科技创新')

// 测试3：传入空分类
console.log('\n--- 测试3：传入空分类 "" ---')
const result3 = testFilterLogic('')

// 测试4：传入不存在的分类
console.log('\n--- 测试4：传入不存在的分类 "test" ---')
const result4 = testFilterLogic('test')

console.log('\n=== 测试完成 ===')
console.log('所有测试结果:')
console.log('测试1结果数量:', result1.length)
console.log('测试2结果数量:', result2.length)
console.log('测试3结果数量:', result3.length)
console.log('测试4结果数量:', result4.length)
