# 相关新闻功能后端实现要求

## 1. 功能概述

新闻详情页中的相关新闻功能需要后端提供API接口，用于根据当前新闻的分类和ID获取相关新闻列表。

## 2. 前端实现逻辑

根据`NewsDetail.vue`文件的分析，前端实现相关新闻的逻辑如下：

1. 当用户查看新闻详情时，前端会先获取当前新闻的详细信息
2. 然后调用相关新闻API，传入当前新闻的分类(category)和ID(excludeId)
3. 后端返回相关新闻列表
4. 前端最多显示3条相关新闻
5. 用户可以点击相关新闻标题查看对应的新闻详情

## 3. 后端需要实现的接口

### 3.1 获取相关新闻接口

**URL**: `/api/news/related`
**请求方法**: GET
**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| category | String | 是 | 当前新闻的分类 |
| excludeId | Integer | 是 | 当前新闻的ID（需要排除） |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 2,
      "title": "相关新闻标题1",
      "publishDate": "2025-06-30",
      "category": "头条新闻"
    },
    {
      "id": 3,
      "title": "相关新闻标题2",
      "publishDate": "2025-06-29",
      "category": "头条新闻"
    },
    {
      "id": 4,
      "title": "相关新闻标题3",
      "publishDate": "2025-06-28",
      "category": "头条新闻"
    }
  ]
}
```

## 4. 后端实现要点

### 4.1 相关新闻的判定逻辑

后端需要根据一定的逻辑来判定哪些新闻是相关的，推荐的实现方式：

1. **基于分类匹配**：优先推荐与当前新闻同一分类的新闻
2. **基于发布时间**：推荐发布时间较近的新闻
3. **排除当前新闻**：确保不返回当前正在查看的新闻
4. **限制返回数量**：建议返回5-10条，前端会根据需要显示（最多3条）

### 4.2 数据排序

相关新闻应该按照一定的规则排序，推荐的排序方式：

1. 按发布时间倒序排列（最新的新闻排在前面）
2. 可以考虑添加其他排序权重（如点击量、相似度等）

### 4.3 性能考虑

1. 相关新闻查询应该高效，避免复杂的数据库操作
2. 可以考虑使用缓存来提高查询性能

## 5. 数据字段说明

| 字段名 | 类型 | 描述 |
|-------|------|------|
| id | Integer | 新闻ID，唯一标识符 |
| title | String | 新闻标题 |
| publishDate | String | 发布日期，格式：YYYY-MM-DD |
| category | String | 新闻分类 |

## 6. 错误处理

后端需要处理以下错误情况：

1. **参数错误**：当请求参数不完整或格式不正确时，返回400错误
2. **分类不存在**：当请求的分类不存在时，返回空数组
3. **服务器错误**：当服务器内部发生错误时，返回500错误

## 7. 示例实现（伪代码）

```sql
-- 假设新闻表结构
CREATE TABLE news (
  id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  category VARCHAR(50) NOT NULL,
  publish_date DATE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

```java
// 假设使用Spring Boot实现
@RestController
@RequestMapping("/api/news")
public class NewsController {
    
    @Autowired
    private NewsService newsService;
    
    @GetMapping("/related")
    public ResponseEntity<ApiResponse<List<News>>> getRelatedNews(
            @RequestParam String category,
            @RequestParam Integer excludeId) {
        
        List<News> relatedNews = newsService.getRelatedNews(category, excludeId);
        
        ApiResponse<List<News>> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("成功");
        response.setData(relatedNews);
        
        return ResponseEntity.ok(response);
    }
}

@Service
public class NewsServiceImpl implements NewsService {
    
    @Autowired
    private NewsRepository newsRepository;
    
    @Override
    public List<News> getRelatedNews(String category, Integer excludeId) {
        // 查询同一分类、排除当前新闻、按发布时间倒序排列、限制返回数量
        return newsRepository.findByCategoryAndIdNotOrderByPublishDateDesc(category, excludeId, PageRequest.of(0, 10));
    }
}
```

## 8. 总结

后端需要实现`/api/news/related`接口，根据当前新闻的分类和ID获取相关新闻列表。实现时需要注意：

1. 确保返回的数据格式符合前端要求
2. 实现合理的相关新闻判定逻辑
3. 保证接口性能和稳定性
4. 处理好各种错误情况

通过以上实现，后端可以为前端提供完整的相关新闻功能支持。