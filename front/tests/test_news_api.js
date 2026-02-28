import http from 'http';

// 测试头条新闻API
http.get('http://localhost:8080/api/news/headline', (res) => {
  let data = '';

  res.on('data', (chunk) => {
    data += chunk;
  });

  res.on('end', () => {
    console.log('头条新闻API响应：');
    try {
      const result = JSON.parse(data);
      console.log('状态码:', result.code);
      console.log('消息:', result.message);
      console.log('数据:', JSON.stringify(result.data, null, 2));
    } catch (e) {
      console.error('解析响应失败:', e);
      console.log('原始响应:', data);
    }
  });

}).on('error', (err) => {
  console.error('请求失败:', err);
});

// 测试新闻详情API
setTimeout(() => {
  http.get('http://localhost:8080/api/news/1', (res) => {
    let data = '';

    res.on('data', (chunk) => {
      data += chunk;
    });

    res.on('end', () => {
      console.log('\n新闻详情API响应：');
      try {
        const result = JSON.parse(data);
        console.log('状态码:', result.code);
        console.log('消息:', result.message);
        console.log('数据:', JSON.stringify(result.data, null, 2));
      } catch (e) {
        console.error('解析响应失败:', e);
        console.log('原始响应:', data);
      }
    });

  }).on('error', (err) => {
    console.error('请求失败:', err);
  });
}, 1000);

// 测试新闻分类API
setTimeout(() => {
  http.get('http://localhost:8080/api/news/categories', (res) => {
    let data = '';

    res.on('data', (chunk) => {
      data += chunk;
    });

    res.on('end', () => {
      console.log('\n新闻分类API响应：');
      try {
        const result = JSON.parse(data);
        console.log('状态码:', result.code);
        console.log('消息:', result.message);
        console.log('数据:', JSON.stringify(result.data, null, 2));
      } catch (e) {
        console.error('解析响应失败:', e);
        console.log('原始响应:', data);
      }
    });

  }).on('error', (err) => {
    console.error('请求失败:', err);
  });
}, 2000);