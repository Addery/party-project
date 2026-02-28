# 学习资源界面后端接口规范

## 1. 获取资源分类列表接口

### 接口说明
获取学习资源的分类列表，用于前端页面的分类筛选功能。

### 请求信息
- **URL**: `/api/learning/categories`
- **请求方法**: GET
- **请求参数**: 无

### 响应信息
- **响应格式**: JSON
- **响应状态码**: 200 OK

### 响应示例
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": "video",
      "name": "视频课程"
    },
    {
      "id": "document",
      "name": "文档资料"
    },
    {
      "id": "audio",
      "name": "音频资源"
    },
    {
      "id": "article",
      "name": "学习文章"
    },
    {
      "id": "case",
      "name": "案例分析"
    },
    {
      "id": "quiz",
      "name": "在线测试"
    }
  ]
}
```

## 2. 获取学习资源列表接口

### 接口说明
获取学习资源列表，支持分页、分类筛选和关键词搜索功能。

### 请求信息
- **URL**: `/api/learning/resources`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | page | Integer | 是 | 当前页码，从1开始 |
  | pageSize | Integer | 是 | 每页显示的资源数量 |
  | type | String | 否 | 资源类型筛选，对应分类列表中的id字段 |
  | keyword | String | 否 | 搜索关键词，支持对资源标题和描述的模糊搜索 |

### 响应信息
- **响应格式**: JSON
- **响应状态码**: 200 OK

### 响应示例
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "resources": [
      {
        "id": 1,
        "title": "习近平新时代中国特色社会主义思想概论",
        "author": "中共中央党校",
        "date": "2025-04-20",
        "duration": "6小时",
        "type": "video",
        "level": "初级",
        "category": "video",
        "description": "本课程系统阐述了习近平新时代中国特色社会主义思想的核心要义、精神实质、丰富内涵、实践要求，是党员干部学习贯彻党的创新理论的重要教材。",
        "image": "https://picsum.photos/id/10/300/200",
        "views": 12580,
        "rating": 4.8
      },
      {
        "id": 2,
        "title": "中国共产党章程（2022年修订）",
        "author": "中共中央",
        "date": "2025-03-15",
        "duration": "30分钟",
        "type": "document",
        "level": "初级",
        "category": "document",
        "description": "2022年10月22日中国共产党第二十次全国代表大会通过的《中国共产党章程》修订版全文，是全党必须遵循的总章程。",
        "image": "https://picsum.photos/id/20/300/200",
        "views": 8920,
        "rating": 4.9
      }
    ],
    "total": 12,
    "pages": 6,
    "page": 1,
    "pageSize": 2
  }
}
```

## 3. 获取资源详情接口

### 接口说明
根据资源ID获取资源的详细信息。

### 请求信息
- **URL**: `/api/learning/resources/{id}`
- **请求方法**: GET
- **路径参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 资源ID |

### 响应信息
- **响应格式**: JSON
- **响应状态码**: 200 OK

### 响应示例
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "title": "习近平新时代中国特色社会主义思想概论",
    "author": "中共中央党校",
    "date": "2025-04-20",
    "duration": "6小时",
    "type": "video",
    "level": "初级",
    "category": "video",
    "description": "本课程系统阐述了习近平新时代中国特色社会主义思想的核心要义、精神实质、丰富内涵、实践要求，是党员干部学习贯彻党的创新理论的重要教材。",
    "image": "https://picsum.photos/id/10/300/200",
    "views": 12580,
    "rating": 4.8,
    "content": "<h2>课程简介</h2><p>本课程共分为12个章节...</p>",
    "attachments": [
      {
        "name": "课程大纲.pdf",
        "url": "/api/attachments/1"
      }
    ]
  }
}
```

## 4. 开始学习资源接口

### 接口说明
记录用户开始学习某个资源的行为，可用于统计学习进度和学习记录。

### 请求信息
- **URL**: `/api/learning/resources/{id}/start`
- **请求方法**: POST
- **路径参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 资源ID |

### 响应信息
- **响应格式**: JSON
- **响应状态码**: 200 OK

### 响应示例
```json
{
  "code": 200,
  "message": "开始学习成功",
  "data": {
    "resourceId": 1,
    "userId": 123,
    "startTime": "2025-04-26T10:30:00Z"
  }
}
```

## 5. 通用响应格式说明

### 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": { /* 响应数据 */ }
}
```

### 错误响应
```json
{
  "code": 错误码,
  "message": "错误信息",
  "data": null
}
```

### 常见错误码
- 400: 请求参数错误
- 401: 未登录或登录过期
- 403: 没有权限访问该资源
- 404: 资源不存在
- 500: 服务器内部错误