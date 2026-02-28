# 通知公告接口说明

## 1. 获取首页最新通知公告

### 接口路径
```
GET /api/notices/home
```

### 请求参数
| 参数名 | 类型 | 是否必需 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- |
| limit | Integer | 否 | 5 | 返回的通知公告数量限制 |

### 返回数据结构
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 9,
      "title": "关于计算机学院实验室安全检查的通知",
      "date": "2025-12-04",
      "department": "计算机学院实验室管理中心"
    },
    {
      "id": 4,
      "title": "关于2025年12月学院党委理论学习中心组学习的通知",
      "date": "2025-12-01",
      "department": "党委办公室"
    }
  ]
}
```

### 前端调用示例（JavaScript fetch）
```javascript
fetch('/api/notices/home?limit=5')
  .then(response => response.json())
  .then(data => {
    if (data.code === 200) {
      console.log('首页通知公告列表:', data.data);
      // 处理通知公告列表数据
    } else {
      console.error('获取失败:', data.message);
    }
  })
  .catch(error => console.error('网络错误:', error));
```

## 2. 获取分页通知公告列表

### 接口路径
```
GET /api/notices
```

### 请求参数
| 参数名 | 类型 | 是否必需 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- |
| page | Integer | 否 | 1 | 当前页码 |
| pageSize | Integer | 否 | 10 | 每页显示数量 |
| keyword | String | 否 | - | 搜索关键词，用于模糊搜索标题和内容 |

### 返回数据结构
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 11,
    "page": 1,
    "pageSize": 10,
    "pages": 2,
    "notices": [
      {
        "id": 9,
        "title": "关于计算机学院实验室安全检查的通知",
        "publishDate": "2025-12-04",
        "author": "计算机学院实验室管理中心",
        "summary": "为加强实验室安全管理，确保师生人身和财产安全，学院将于近期组织开展实验室安全检查..."
      },
      {
        "id": 8,
        "title": "关于开展2025年冬季运动会的通知",
        "publishDate": "2025-11-30",
        "author": "团委",
        "summary": "为丰富师生校园文化生活，增强身体素质，学院将于2025年12月15日至17日举办冬季运动会..."
      }
    ]
  }
}
```

### 前端调用示例（JavaScript fetch）
```javascript
fetch('/api/notices?page=1&pageSize=10&keyword=安全')
  .then(response => response.json())
  .then(data => {
    if (data.code === 200) {
      console.log('通知公告分页列表:', data.data.notices);
      console.log('总条数:', data.data.total);
      console.log('总页数:', data.data.pages);
      // 处理分页列表数据
    } else {
      console.error('获取失败:', data.message);
    }
  })
  .catch(error => console.error('网络错误:', error));
```

## 3. 获取通知公告详情

### 接口路径
```
GET /api/notices/{id}
```

### 请求参数
| 参数名 | 类型 | 是否必需 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- |
| id | Integer | 是 | - | 通知公告ID |

### 返回数据结构
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 9,
    "title": "关于计算机学院实验室安全检查的通知",
    "publishDate": "2025-12-04",
    "author": "计算机学院实验室管理中心",
    "content": "<p>各实验室：</p><p>为加强实验室安全管理，确保师生人身和财产安全，学院将于近期组织开展实验室安全检查...</p>",
    "attachments": [
      {
        "name": "实验室安全检查标准.pdf",
        "url": "/uploads/20251204/lab-safety-standard.pdf"
      },
      {
        "name": "实验室安全自查表.xlsx",
        "url": "/uploads/20251204/lab-safety-checklist.xlsx"
      }
    ]
  }
}
```

### 前端调用示例（JavaScript fetch）
```javascript
const noticeId = 9;
fetch(`/api/notices/${noticeId}`)
  .then(response => response.json())
  .then(data => {
    if (data.code === 200) {
      console.log('通知公告详情:', data.data);
      console.log('通知内容:', data.data.content); // HTML格式
      console.log('附件列表:', data.data.attachments);
      // 处理通知公告详情数据
    } else {
      console.error('获取失败:', data.message);
    }
  })
  .catch(error => console.error('网络错误:', error));
```

## 通用响应说明

所有接口返回的数据都包含以下三个字段：
- `code`: 状态码，200表示成功，其他表示失败
- `message`: 响应消息，说明操作结果
- `data`: 响应数据，根据接口不同返回不同的结构

## 注意事项

1. 所有接口均支持跨域请求
2. 接口不需要认证，可以直接访问
3. 日期格式统一为YYYY-MM-DD
4. 内容字段（content）返回HTML格式，前端需使用innerHTML或类似方式渲染
5. 附件的url字段为相对路径，前端需拼接完整的域名访问