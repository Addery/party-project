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
        resolve({ statusCode: res.statusCode, data: JSON.parse(data) });
      });
    }).on('error', (err) => {
      reject(err);
    });
  });
}

// 测试分类筛选功能
async function testCategoryFilter() {
  console.log('开始测试新闻分类筛选功能...\n');
  
  // 测试分类列表
  const categories = ['policy', 'society', 'culture', 'technology', 'economy'];
  
  for (const category of categories) {
    try {
      const options = {
        hostname: 'localhost',
        port: 8080,
        path: `/api/news?page=1&pageSize=5&category=${category}`,
        method: 'GET'
      };
      
      const response = await httpGet(options);
      
      console.log(`测试分类：${category}`);
      console.log(`状态码：${response.statusCode}`);
      console.log(`返回数据：${JSON.stringify(response.data, null, 2)}`);
      console.log('---\n');
    } catch (error) {
      console.error(`测试分类 ${category} 时出错：`, error.message);
      console.log('---\n');
    }
  }
  
  // 测试默认分类
  try {
    const options = {
      hostname: 'localhost',
      port: 8080,
      path: '/api/news?page=1&pageSize=5',
      method: 'GET'
    };
    
    const response = await httpGet(options);
    
    console.log('测试默认分类（不指定分类）');
    console.log(`状态码：${response.statusCode}`);
    console.log(`返回数据：${JSON.stringify(response.data, null, 2)}`);
    console.log('---\n');
  } catch (error) {
    console.error('测试默认分类时出错：', error.message);
    console.log('---\n');
  }
  
  console.log('新闻分类筛选功能测试完成！');
}

// 执行测试
testCategoryFilter();