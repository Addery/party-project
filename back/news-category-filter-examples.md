# 新闻分类筛选接口调用示例

## 接口基本信息
- **接口URL**: `http://localhost:8080/api/news`
- **请求方法**: `GET`
- **认证方式**: 无需认证（根据实际项目配置可能需要）

## 支持的分类

| 英文名称 | 中文显示名称 |
|---------|------------|
| policy | 时政要闻 |
| party_building | 党建动态 |
| economy | 经济发展 |
| culture | 文化动态 |
| education | 教育资讯 |
| technology | 科技创新 |
| sports | 体育新闻 |
| health | 健康生活 |
| environment | 环境保护 |
| safety | 安全警示 |
| law | 法律法规 |

## 调用示例

### 1. 通过英文分类名称筛选

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/news?category=technology&page=1&pageSize=10" -Method GET | ConvertTo-Json -Depth 4
```

#### cURL
```bash
curl -X GET "http://localhost:8080/api/news?category=technology&page=1&pageSize=10"
```

#### JavaScript (fetch)
```javascript
fetch('http://localhost:8080/api/news?category=technology&page=1&pageSize=10')
  .then(response => response.json())
  .then(data => console.log(data));
```

### 2. 通过中文分类名称筛选

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/news?category=科技创新&page=1&pageSize=10" -Method GET | ConvertTo-Json -Depth 4
```

#### cURL
```bash
curl -X GET "http://localhost:8080/api/news?category=科技创新&page=1&pageSize=10"
```

#### JavaScript (fetch)
```javascript
fetch('http://localhost:8080/api/news?category=科技创新&page=1&pageSize=10')
  .then(response => response.json())
  .then(data => console.log(data));
```

### 3. 结合分类和关键词筛选

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/news?category=education&keyword=学习&page=1&pageSize=10" -Method GET | ConvertTo-Json -Depth 4
```

#### cURL
```bash
curl -X GET "http://localhost:8080/api/news?category=education&keyword=学习&page=1&pageSize=10"
```

### 4. 结合分类、关键词和排序筛选

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/news?category=economy&keyword=发展&sortBy=publishDate&order=desc&page=1&pageSize=10" -Method GET | ConvertTo-Json -Depth 4
```

#### cURL
```bash
curl -X GET "http://localhost:8080/api/news?category=economy&keyword=发展&sortBy=publishDate&order=desc&page=1&pageSize=10"
```

### 5. 分页查询

#### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/news?category=sports&page=2&pageSize=5" -Method GET | ConvertTo-Json -Depth 4
```

#### cURL
```bash
curl -X GET "http://localhost:8080/api/news?category=sports&page=2&pageSize=5"
```

## 响应示例

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "newsList": [
      {
        "id": 1,
        "title": "科技创新大会在京召开",
        "content": "大会聚焦人工智能、量子计算等前沿科技领域...",
        "description": "科技创新大会在北京召开，探讨未来科技发展方向",
        "category": "technology",
        "author": "科技日报",
        "publishDate": "2025年11月15日",
        "coverImage": "https://picsum.photos/id/101/800/400",
        "viewsCount": 1234,
        "status": "published"
      },
      {
        "id": 2,
        "title": "5G技术在医疗领域的应用",
        "content": "5G技术的高速率和低延迟特性为远程医疗提供了可能...",
        "description": "5G技术正在改变医疗行业的服务模式",
        "category": "technology",
        "author": "健康时报",
        "publishDate": "2025年11月10日",
        "coverImage": "https://picsum.photos/id/102/800/400",
        "viewsCount": 897,
        "status": "published"
      }
    ],
    "total": 20,
    "pageSize": 10,
    "page": 1
  }
}
```

## 参数说明

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| category | string | 否 | 新闻分类（支持英文名称或中文显示名称） |
| keyword | string | 否 | 关键词搜索（标题中包含该关键词） |
| sortBy | string | 否 | 排序字段（publishDate、viewsCount等） |
| order | string | 否 | 排序方向（asc：升序，desc：降序） |
| page | integer | 否 | 页码（默认1） |
| pageSize | integer | 否 | 每页条数（默认10） |

## 注意事项

1. 分类参数支持中英文两种格式，使用中文时请注意URL编码
2. 所有参数均为可选，不提供任何参数时将返回所有已发布的新闻
3. 排序字段必须是新闻实体类中存在的字段
4. 接口返回结果包含分页信息，可用于前端分页展示