import http from 'http';

// 辅助函数：发送HTTP GET请求
function httpGet(options) {
  return new Promise((resolve, reject) => {
    http.get(options, (res) => {
      let data = '';
      res.on('data', (chunk) => {
        data += chunk;
      });
      res.on('end', () => {
        try {
          resolve({ statusCode: res.statusCode, data: JSON.parse(data) });
        } catch (error) {
          resolve({ statusCode: res.statusCode, data: data });
        }
      });
    }).on('error', (err) => {
      reject(err);
    });
  });
}

// 测试科技创新分类筛选功能
async function testTechCategoryFilter() {
  console.log('开始测试科技创新(technology)分类筛选功能...\n');
  
  try {
    // 测试URL：科技创新分类对应后端technology
    const options = {
      hostname: 'localhost',
      port: 8080,
      path: '/api/news?page=1&pageSize=10&category=technology',
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      }
    };
    
    console.log('发送请求:', JSON.stringify(options, null, 2));
    
    const response = await httpGet(options);
    
    console.log('\n响应状态码:', response.statusCode);
    console.log('响应数据:', JSON.stringify(response.data, null, 2));
    
    // 检查返回的数据是否包含technology分类的新闻
    if (response.statusCode === 200 && response.data) {
      // 检查数据结构
      const newsData = response.data.content || response.data.news || response.data.list || response.data.newsList || response.data.data;
      
      if (Array.isArray(newsData)) {
        console.log('\n新闻列表长度:', newsData.length);
        
        // 检查是否有technology分类的新闻
        const techNews = newsData.filter(news => news.category === 'technology');
        console.log('technology分类新闻数量:', techNews.length);
        
        if (techNews.length > 0) {
          console.log('\n测试成功：获取到科技创新分类的新闻');
          console.log('示例新闻标题:', techNews[0].title);
        } else {
          console.log('\n警告：返回的数据中没有找到technology分类的新闻');
          // 显示所有新闻的分类
          console.log('所有新闻的分类:', newsData.map(news => news.category).join(', '));
        }
      } else {
        console.log('\n错误：返回的新闻数据不是数组');
      }
    } else {
      console.log('\n错误：请求失败，状态码:', response.statusCode);
    }
    
  } catch (error) {
    console.error('测试过程中出错：', error.message);
    console.error('错误堆栈:', error.stack);
  }
  
  console.log('\n科技创新分类筛选功能测试完成！');
}

// 执行测试
testTechCategoryFilter();