# 新闻动态后端API接口规范

## 1. 接口概述

本文档定义了前台界面中新闻动态模块所需的后端API接口规范，包括接口URL、请求参数、响应格式等信息。

## 2. 接口列表

| 接口名称 | URL | 请求方法 | 功能描述 |
|---------|-----|---------|----------|
| 获取头条新闻列表 | /api/news/headline | GET | 获取头条新闻列表，支持分页和排序 |
| 获取新闻详情 | /api/news/{id} | GET | 根据ID获取新闻详情 |
| 获取新闻列表 | /api/news | GET | 获取通用新闻列表，支持分页、分类筛选和关键词搜索 |
| 获取相关新闻 | /api/news/related | GET | 获取与指定新闻相关的新闻列表 |
| 获取新闻分类 | /api/news/categories | GET | 获取所有新闻分类 |

## 3. 详细接口规范

### 3.1 获取头条新闻列表

**URL**: `/api/news/headline`
**请求方法**: GET
**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| page | Integer | 否 | 当前页码，默认1 |
| pageSize | Integer | 否 | 每页大小，默认10 |
| sortBy | String | 否 | 排序字段，默认publishDate |
| order | String | 否 | 排序方式，asc或desc，默认desc |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "newsList": [
      {
        "id": 1,
        "title": "习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话",
        "publishDate": "2025-07-01",
        "category": "头条新闻",
        "summary": "习近平总书记在庆祝中国共产党成立104周年大会上发表重要讲话...",
        "author": "新华社",
        "cover": "https://example.com/news-cover.jpg"
      },
      // 更多新闻...
    ],
    "total": 100,
    "page": 1,
    "pageSize": 10
  }
}
```

### 3.2 获取新闻详情

**URL**: `/api/news/{id}`
**请求方法**: GET
**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| id | Integer | 是 | 新闻ID，通过URL路径传递 |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "title": "习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话",
    "publishDate": "2025-07-01",
    "category": "头条新闻",
    "content": "<p>习近平总书记在庆祝中国共产党成立104周年大会上发表重要讲话...</p>",
    "author": "新华社",
    "cover": "https://example.com/news-cover.jpg",
    "views": 10000
  }
}
```

### 3.3 获取新闻列表

**URL**: `/api/news`
**请求方法**: GET
**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| page | Integer | 否 | 当前页码，默认1 |
| pageSize | Integer | 否 | 每页大小，默认10 |
| category | String | 否 | 新闻分类，为空则获取所有分类 |
| keyword | String | 否 | 搜索关键词，用于标题搜索 |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "newsList": [
      {
        "id": 1,
        "title": "习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话",
        "publishDate": "2025-07-01",
        "category": "头条新闻",
        "summary": "习近平总书记在庆祝中国共产党成立104周年大会上发表重要讲话...",
        "author": "新华社",
        "cover": "https://example.com/news-cover.jpg"
      },
      // 更多新闻...
    ],
    "total": 100,
    "page": 1,
    "pageSize": 10
  }
}
```

### 3.4 获取相关新闻

**URL**: `/api/news/related`
**请求方法**: GET
**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| category | String | 是 | 新闻分类 |
| excludeId | Integer | 是 | 排除的新闻ID（当前查看的新闻ID） |

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
    }
    // 更多相关新闻...
  ]
}
```

### 3.5 获取新闻分类

**URL**: `/api/news/categories`
**请求方法**: GET
**请求参数**: 无

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": [
    "头条新闻",
    "学校动态",
    "通知公告",
    "党建工作",
    "思政教育"
    // 更多分类...
  ]
}
```

## 4. 数据字段说明

### 4.1 新闻基本信息

| 字段名 | 类型 | 描述 |
|-------|------|------|
| id | Integer | 新闻ID，唯一标识符 |
| title | String | 新闻标题 |
| publishDate | String | 发布日期，格式：YYYY-MM-DD |
| category | String | 新闻分类 |
| summary | String | 新闻摘要 |
| content | String | 新闻内容（HTML格式） |
| author | String | 作者 |
| cover | String | 新闻封面图片URL |
| views | Integer | 浏览量 |

### 4.2 分页信息

| 字段名 | 类型 | 描述 |
|-------|------|------|
| total | Integer | 总记录数 |
| page | Integer | 当前页码 |
| pageSize | Integer | 每页大小 |

## 5. 错误响应格式

```json
{
  "code": 错误码,
  "message": "错误信息"
}
```

常见错误码：
- 400: 请求参数错误
- 404: 资源不存在
- 500: 服务器内部错误

## 6. 接口调用示例

### 6.1 获取头条新闻列表

```javascript
// 请求
axios.get('/api/news/headline', {
  params: {
    page: 1,
    pageSize: 10,
    sortBy: 'publishDate',
    order: 'desc'
  }
})

// 响应
{
  "code": 200,
  "message": "成功",
  "data": {
    "newsList": [
      {
        "id": 1,
        "title": "习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话",
        "publishDate": "2025-07-01",
        "category": "头条新闻",
        "summary": "习近平总书记在庆祝中国共产党成立104周年大会上发表重要讲话...",
        "author": "新华社",
        "cover": "https://example.com/news-cover.jpg"
      }
    ],
    "total": 1,
    "page": 1,
    "pageSize": 10
  }
}
```

### 6.2 获取新闻详情

```javascript
// 请求
axios.get('/api/news/1')

// 响应
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "title": "习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话",
    "publishDate": "2025-07-01",
    "category": "头条新闻",
    "content": "<p>习近平总书记在庆祝中国共产党成立104周年大会上发表重要讲话...</p>",
    "author": "新华社",
    "cover": "https://example.com/news-cover.jpg",
    "views": 10000
  }
}
```
