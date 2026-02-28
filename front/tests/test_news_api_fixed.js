import http from 'http';

// 测试函数：发送GET请求并解析响应
function testApi(url, description, delay = 0) {
  setTimeout(() => {
    console.log(`\n=== ${description} ===`);
    http.get(url, (res) => {
      let data = '';

      res.on('data', (chunk) => {
        data += chunk;
      });

      res.on('end', () => {
        try {
          const result = JSON.parse(data);
          console.log('状态码:', result.code);
          console.log('消息:', result.message);
          console.log('总条数:', result.data?.total || 0);
          console.log('当前页:', result.data?.page || 1);
          console.log('每页大小:', result.data?.pageSize || 10);
          console.log('新闻列表长度:', result.data?.newsList?.length || 0);
          if (result.data?.newsList?.length > 0) {
            console.log('第一条新闻标题:', result.data.newsList[0].title);
            console.log('第一条新闻分类:', result.data.newsList[0].category);
          }
        } catch (e) {
          console.error('解析响应失败:', e);
          console.log('原始响应:', data);
        }
      });

    }).on('error', (err) => {
      console.error('请求失败:', err);
    });
  }, delay);
}

// 1. 测试获取全部新闻（默认分页）
testApi('http://localhost:8080/api/news', '1. 获取全部新闻（默认分页）');

// 2. 测试按分类筛选新闻（使用后端实际支持的分类值）
testApi('http://localhost:8080/api/news?category=policy', '2. 按分类筛选：政策', 1000);

testApi('http://localhost:8080/api/news?category=society', '3. 按分类筛选：社会', 2000);

// 4. 测试按关键词搜索
testApi('http://localhost:8080/api/news?keyword=习近平', '4. 按关键词搜索：习近平', 3000);

// 5. 测试组合筛选（分类+关键词）
testApi('http://localhost:8080/api/news?category=policy&keyword=会议', '5. 组合筛选：政策+会议', 4000);

// 6. 测试分页功能（第一页，每页5条）
testApi('http://localhost:8080/api/news?page=1&pageSize=5', '6. 分页测试：第1页，每页5条', 5000);

// 7. 测试分页功能（第二页，每页5条）
testApi('http://localhost:8080/api/news?page=2&pageSize=5', '7. 分页测试：第2页，每页5条', 6000);

// 8. 测试排序功能（按浏览量降序）
testApi('http://localhost:8080/api/news?sortBy=views&order=desc', '8. 排序测试：按浏览量降序', 7000);

// 9. 测试排序功能（按发布日期降序）
testApi('http://localhost:8080/api/news?sortBy=publishDate&order=desc', '9. 排序测试：按发布日期降序', 8000);

// 10. 测试边界情况：不存在的分类
testApi('http://localhost:8080/api/news?category=unknown', '10. 边界测试：不存在的分类', 9000);

// 11. 测试边界情况：不存在的关键词
testApi('http://localhost:8080/api/news?keyword=不存在的关键词12345', '11. 边界测试：不存在的关键词', 10000);

// 12. 测试边界情况：非常大的页码
testApi('http://localhost:8080/api/news?page=1000', '12. 边界测试：非常大的页码', 11000);
