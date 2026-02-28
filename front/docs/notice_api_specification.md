# 首页通知公告后端接口规范

## 1. 接口概述

本文档定义了党员教育与管理系统中首页通知公告功能所需的后端API接口规范，仅包含前端展示所需的接口，不涉及后台管理系统功能。

## 2. 接口列表

| 接口名称 | 请求路径 | 请求方法 | 功能描述 |
|---------|---------|---------|---------|
| 获取首页通知公告列表 | `/api/notices/home` | GET | 获取首页显示的最新通知公告列表 |
| 获取通知公告列表 | `/api/notices` | GET | 获取分页的通知公告列表 |
| 获取通知公告详情 | `/api/notices/{id}` | GET | 获取指定通知公告的详细信息 |

## 3. 接口详情

### 3.1 获取首页通知公告列表

**URL**: `/api/notices/home`
**请求方法**: GET
**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| limit | Integer | 否 | 返回的通知公告数量，默认5 |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "title": "关于2025年11月计算机学院党委理论学习中心组学习的通知",
      "date": "2025-11-15",
      "department": "计算机学院党委"
    },
    {
      "id": 2,
      "title": "关于召开2025年计算机学院党员大会的通知",
      "date": "2025-11-10",
      "department": "计算机学院党委"
    },
    {
      "id": 3,
      "title": "关于2025年计算机学院创新创业大赛导师招募的通知",
      "date": "2025-10-20",
      "department": "计算机学院团委"
    }
  ]
}
```

### 3.2 获取通知公告列表

**URL**: `/api/notices`
**请求方法**: GET
**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| keyword | String | 否 | 搜索关键词，用于模糊搜索标题和内容 |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "total": 12,
    "page": 1,
    "pageSize": 10,
    "pages": 2,
    "notices": [
      {
        "id": 1,
        "title": "关于计算机学院2025-2026学年第一学期期末考试党员先锋岗安排的通知",
        "publishDate": "2025-11-28",
        "author": "计算机学院党委",
        "summary": "为确保期末考试顺利进行，充分发挥党员先锋模范作用，现将2025-2026学年第一学期期末考试党员先锋岗安排通知如下..."
      },
      {
        "id": 2,
        "title": "关于开展2025年秋季学期计算机学院党员民主生活会的通知",
        "publishDate": "2025-11-15",
        "author": "计算机学院党委",
        "summary": "根据学校党委统一部署，现就开展2025年秋季学期计算机学院党员民主生活会有关事项通知如下..."
      }
    ]
  }
}
```

### 3.3 获取通知公告详情

**URL**: `/api/notices/{id}`
**请求方法**: GET
**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| id | Integer | 是 | 通知公告ID |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "title": "关于计算机学院2025-2026学年第一学期期末考试党员先锋岗安排的通知",
    "publishDate": "2025-11-28",
    "author": "计算机学院党委",
    "content": "<p>各党支部、全体党员：</p><p>为确保2025-2026学年第一学期期末考试顺利进行，充分发挥党员先锋模范作用，维护良好的考试秩序，营造公平、公正的考试环境，现将期末考试党员先锋岗安排通知如下：</p><h3>一、工作时间</h3><p>2025年12月15日至2025年12月25日</p><h3>二、工作岗位及职责</h3><p>1. 考场监督岗：负责巡查考场纪律，维护考场秩序；</p><p>2. 服务保障岗：负责考试期间的后勤保障工作；</p><p>3. 应急处理岗：负责处理考试期间的突发情况。</p><p>请各党支部组织党员按时到岗，认真履行职责。</p><p>计算机学院党委</p><p>2025年11月28日</p>",
    "attachments": [
      {
        "name": "期末考试党员先锋岗安排表.xlsx",
        "url": "/uploads/attachments/20251128/123456.xlsx"
      }
    ]
  }
}
```

## 4. 数据字段说明

### 4.1 首页通知公告列表字段

| 字段名 | 类型 | 描述 |
|-------|------|------|
| id | Integer | 通知公告ID，唯一标识符 |
| title | String | 通知公告标题 |
| date | String | 发布日期，格式：YYYY-MM-DD |
| department | String | 发布部门 |

### 4.2 通知公告列表字段

| 字段名 | 类型 | 描述 |
|-------|------|------|
| id | Integer | 通知公告ID，唯一标识符 |
| title | String | 通知公告标题 |
| publishDate | String | 发布日期，格式：YYYY-MM-DD |
| author | String | 发布人或发布单位 |
| summary | String | 通知公告摘要 |

### 4.3 通知公告详情字段

| 字段名 | 类型 | 描述 |
|-------|------|------|
| id | Integer | 通知公告ID，唯一标识符 |
| title | String | 通知公告标题 |
| publishDate | String | 发布日期，格式：YYYY-MM-DD |
| author | String | 发布人或发布单位 |
| content | String | 通知公告内容（HTML格式） |
| attachments | Array | 附件列表 |

### 4.4 附件信息字段

| 字段名 | 类型 | 描述 |
|-------|------|------|
| name | String | 附件名称 |
| url | String | 附件下载URL |

## 5. 错误处理

所有API接口返回的错误格式统一为：

```json
{
  "code": 错误码,
  "message": "错误信息",
  "data": null
}
```

常见错误码：

| 错误码 | 描述 |
|-------|------|
| 400 | 请求参数错误 |
| 404 | 请求的资源不存在 |
| 500 | 服务器内部错误 |

## 6. 接口调用示例

### 6.1 获取首页通知公告列表

```javascript
// 前端调用示例
axios.get('/api/notices/home?limit=5')
  .then(response => {
    if (response.data.code === 200) {
      const notices = response.data.data;
      // 处理首页通知公告数据
    }
  })
  .catch(error => {
    console.error('获取首页通知公告失败:', error);
  });
```

### 6.2 获取完整通知公告列表（分页）

```javascript
// 前端调用示例
axios.get('/api/notices?page=1&pageSize=10&keyword=党员大会')
  .then(response => {
    if (response.data.code === 200) {
      const { total, page, pageSize, pages, notices } = response.data.data;
      // 处理通知公告列表数据
    }
  })
  .catch(error => {
    console.error('获取通知公告列表失败:', error);
  });
```

### 6.3 获取通知公告详情

```javascript
// 前端调用示例
const noticeId = 1;
axios.get(`/api/notices/${noticeId}`)
  .then(response => {
    if (response.data.code === 200) {
      const notice = response.data.data;
      // 处理通知公告详情数据
    }
  })
  .catch(error => {
    console.error('获取通知公告详情失败:', error);
  });
```

## 7. 性能要求

1. 首页通知公告列表接口响应时间不超过300ms
2. 通知公告详情接口响应时间不超过500ms
3. 支持至少100个并发请求
4. 考虑使用缓存机制提高热门通知公告的访问速度