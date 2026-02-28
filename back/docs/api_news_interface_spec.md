# 新闻相关接口对接规范

## 1. 接口概述

本文档描述了新闻相关的所有RESTful API接口规范，包括头条新闻、新闻详情、新闻列表、相关新闻和新闻分类等接口。

## 2. 通用接口规范

### 2.1 基本信息

- 接口协议：HTTP/HTTPS
- 接口前缀：`/api/news`
- 请求方法：GET/POST（新闻接口主要使用GET方法）
- 响应格式：JSON
- 编码方式：UTF-8

### 2.2 通用响应格式

所有接口返回统一的响应格式：

```json
{
  "code": 200,              // 响应状态码
  "message": "操作成功",     // 响应消息
  "data": {}                // 响应数据（具体格式根据接口而定）
}
```

### 2.3 状态码说明

| 状态码 | 说明               |
|--------|--------------------|
| 200    | 成功               |
| 400    | 参数错误           |
| 404    | 资源不存在         |
| 500    | 服务器内部错误     |

## 3. 具体接口规范

### 3.1 头条新闻接口

#### 接口描述

获取头条新闻列表，支持分页。

#### 请求信息

- URL：`/api/news/headline`
- 请求方法：GET
- 请求参数：
  | 参数名    | 类型    | 必填 | 默认值 | 说明         |
  |-----------|---------|------|--------|--------------|
  | page      | Integer | 否   | 1      | 页码         |
  | pageSize  | Integer | 否   | 10     | 每页条数     |

#### 响应信息

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 28,            // 总记录数
    "page": 1,              // 当前页码
    "pageSize": 10,         // 每页条数
    "pages": 3,             // 总页数
    "list": [
      {
        "id": 1,            // 新闻ID
        "title": "新闻标题",  // 新闻标题
        "content": "新闻内容", // 新闻内容
        "description": "新闻摘要", // 新闻摘要
        "category": "党建工作", // 新闻分类
        "author": "管理员",   // 作者
        "publishDate": "2023-10-15T08:30:00", // 发布日期
        "coverImage": "https://picsum.photos/id/101/800/400", // 封面图片
        "viewsCount": 123,  // 浏览量
        "createdAt": "2023-10-15T08:30:00", // 创建时间
        "updatedAt": "2023-10-15T08:30:00", // 更新时间
        "status": "published", // 状态
        "isTop": false,     // 是否置顶
        "sortOrder": 0      // 排序顺序
      }
    ]
  }
}
```

### 3.2 新闻详情接口

#### 接口描述

根据新闻ID获取新闻详情。

#### 请求信息

- URL：`/api/news/{id}`
- 请求方法：GET
- 请求参数：
  | 参数名 | 类型    | 必填 | 说明   |
  |--------|---------|------|--------|
  | id     | Integer | 是   | 新闻ID |

#### 响应信息

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,                // 新闻ID
    "title": "新闻标题",      // 新闻标题
    "category": "党建工作",  // 新闻分类
    "publishDate": "2023-10-15T08:30:00", // 发布日期
    "source": "管理员",       // 来源（对应author字段）
    "views": 123,           // 浏览量
    "imageUrl": "https://picsum.photos/id/101/800/400", // 封面图片
    "content": "新闻内容",     // 新闻内容
    "relatedNews": [        // 相关新闻列表
      {
        "id": 2,
        "title": "相关新闻标题",
        "publishDate": "2023-10-14T09:00:00",
        "category": "党建工作"
      }
    ]
  }
}
```

### 3.3 新闻列表接口

#### 接口描述

获取新闻列表，支持分类筛选、关键词搜索和分页。

#### 请求信息

- URL：`/api/news`
- 请求方法：GET
- 请求参数：
  | 参数名    | 类型    | 必填 | 默认值 | 说明         |
  |-----------|---------|------|--------|--------------|
  | page      | Integer | 否   | 1      | 页码         |
  | pageSize  | Integer | 否   | 10     | 每页条数     |
  | category  | String  | 否   | -      | 新闻分类     |
  | keyword   | String  | 否   | -      | 搜索关键词   |

#### 响应信息

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 28,
    "page": 1,
    "pageSize": 10,
    "pages": 3,
    "list": [
      {
        "id": 1,
        "title": "新闻标题",
        "content": "新闻内容",
        "description": "新闻摘要",
        "category": "党建工作",
        "author": "管理员",
        "publishDate": "2023-10-15T08:30:00",
        "coverImage": "https://picsum.photos/id/101/800/400",
        "viewsCount": 123,
        "createdAt": "2023-10-15T08:30:00",
        "updatedAt": "2023-10-15T08:30:00",
        "status": "published",
        "isTop": false,
        "sortOrder": 0
      }
    ]
  }
}
```

### 3.4 相关新闻接口

#### 接口描述

获取与指定新闻相关的新闻列表（同分类，排除当前新闻）。

#### 请求信息

- URL：`/api/news/related`
- 请求方法：GET
- 请求参数：
  | 参数名    | 类型    | 必填 | 说明                 |
  |-----------|---------|------|----------------------|
  | category  | String  | 是   | 新闻分类             |
  | excludeId | Integer | 是   | 排除的新闻ID（当前新闻ID） |

#### 响应信息

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 2,
      "title": "相关新闻标题",
      "publishDate": "2023-10-14T09:00:00",
      "category": "党建工作"
    }
  ]
}
```

### 3.5 新闻分类接口

#### 接口描述

获取所有新闻分类列表。

#### 请求信息

- URL：`/api/news/categories`
- 请求方法：GET
- 请求参数：无

#### 响应信息

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    "党建工作",
    "教学工作",
    "科研工作",
    "学生工作",
    "教师发展",
    "校园动态",
    "招生就业",
    "国际交流",
    "合作办学",
    "后勤服务",
    "工会活动",
    "其他"
  ]
}
```

## 4. 数据模型说明

### 4.1 News实体类

| 字段名        | 类型    | 说明                 |
|---------------|---------|----------------------|
| id            | Integer | 新闻ID（主键）       |
| title         | String  | 新闻标题             |
| content       | String  | 新闻内容             |
| description   | String  | 新闻摘要             |
| category      | String  | 新闻分类             |
| author        | String  | 作者                 |
| publishDate   | Date    | 发布日期             |
| coverImage    | String  | 封面图片URL          |
| viewsCount    | Integer | 浏览量               |
| createdAt     | Date    | 创建时间             |
| updatedAt     | Date    | 更新时间             |
| status        | String  | 状态（published/draft） |
| isTop         | Boolean | 是否置顶             |
| sortOrder     | Integer | 排序顺序             |

### 4.2 RelatedNewsDTO类

| 字段名        | 类型    | 说明                 |
|---------------|---------|----------------------|
| id            | Integer | 新闻ID               |
| title         | String  | 新闻标题             |
| publishDate   | Date    | 发布日期             |
| category      | String  | 新闻分类             |

## 5. 示例代码

### 5.1 前端调用示例（使用axios）

```javascript
// 获取头条新闻
axios.get('/api/news/headline', {
  params: {
    page: 1,
    pageSize: 10
  }
}).then(response => {
  if (response.data.code === 200) {
    const newsList = response.data.data.list;
    console.log(newsList);
  }
});

// 获取新闻详情
axios.get('/api/news/1').then(response => {
  if (response.data.code === 200) {
    const newsDetail = response.data.data;
    console.log(newsDetail);
  }
});

// 获取新闻分类
axios.get('/api/news/categories').then(response => {
  if (response.data.code === 200) {
    const categories = response.data.data;
    console.log(categories);
  }
});
```

## 6. 注意事项

1. 所有接口的请求参数和响应数据均采用UTF-8编码。
2. 图片URL使用`https://picsum.photos`提供的占位图片服务，实际项目中应替换为真实的图片存储服务URL。
3. 接口支持跨域请求（当前CORS已禁用，如需启用请在SecurityConfig中配置）。
4. 新闻浏览量会在用户访问详情页时自动更新。
5. 分页参数默认值：page=1，pageSize=10。

## 7. 版本历史

| 版本 | 日期       | 说明               |
|------|------------|--------------------|
| 1.0  | 2023-10-15 | 初始版本           |

---

**文档作者：** AI编程助手
**文档日期：** 2023-10-15