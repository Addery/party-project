# 主题活动接口文档

## 1. 接口概述

本文档描述了主题活动相关的所有API接口，包括活动分类、活动列表、活动详情和活动报名等功能。

## 2. 基础信息

- 接口前缀：`/api/activities`
- 响应格式：JSON
- 状态码：所有接口均返回HTTP 200状态码，业务状态通过响应体中的`code`字段表示

## 3. 响应格式

所有接口返回统一的响应格式：

```json
{
  "code": 200,  // 业务状态码，200表示成功，其他表示失败
  "message": "操作成功",  // 响应消息
  "data": {}  // 响应数据，根据接口不同而变化
}
```

## 4. 接口详情

### 4.1 获取活动分类列表

**接口URL**：`/api/activities/categories`

**请求方法**：GET

**请求参数**：无

**响应数据**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": "theme",
      "name": "主题活动"
    },
    {
      "id": "learning",
      "name": "学习活动"
    },
    {
      "id": "volunteer",
      "name": "志愿活动"
    }
    // 更多分类...
  ]
}
```

**数据说明**：
- `data`：活动分类列表
  - `id`：分类ID
  - `name`：分类名称

### 4.2 获取活动列表

**接口URL**：`/api/activities`

**请求方法**：GET

**请求参数**：

| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 是 | - | 当前页码 |
| pageSize | Integer | 是 | - | 每页显示数量 |
| category | String | 否 | - | 活动分类ID |
| keyword | String | 否 | - | 搜索关键词（标题或描述） |

**响应数据**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "activities": [
      {
        "id": 1,
        "title": "党史学习教育主题活动",
        "date": "2025-12-10",
        "time": "14:00-16:00",
        "location": "会议室A",
        "category": "theme",
        "status": "published",
        "description": "深入学习党史，增强党性修养",
        "canRegister": true,
        "participants": 25,
        "views": 120,
        "image": "/images/activity1.jpg"
      }
      // 更多活动...
    ],
    "total": 100,  // 总记录数
    "pages": 25,   // 总页数
    "page": 1,     // 当前页码
    "pageSize": 4  // 每页显示数量
  }
}
```

**数据说明**：
- `data`：活动列表分页数据
  - `activities`：活动列表
    - `id`：活动ID
    - `title`：活动标题
    - `date`：活动日期
    - `time`：活动时间
    - `location`：活动地点
    - `category`：活动分类ID
    - `status`：活动状态
    - `description`：活动简介
    - `canRegister`：是否可报名
    - `participants`：已报名人数
    - `views`：浏览量
    - `image`：活动图片
  - `total`：总记录数
  - `pages`：总页数
  - `page`：当前页码
  - `pageSize`：每页显示数量

### 4.3 获取活动详情

**接口URL**：`/api/activities/{id}`

**请求方法**：GET

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Integer | 是 | 活动ID |

**响应数据**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "title": "党史学习教育主题活动",
    "date": "2025-12-10",
    "time": "14:00-16:00",
    "location": "会议室A",
    "category": "theme",
    "status": "published",
    "description": "深入学习党史，增强党性修养",
    "content": "<h1>活动详情内容</h1><p>...完整的活动内容...</p>",
    "canRegister": true,
    "registerDeadline": "2025-12-08",
    "participants": 25,
    "capacity": 50,
    "views": 120,
    "image": "/images/activity1.jpg",
    "attachments": [
      {
        "name": "活动议程.pdf",
        "url": "/attachments/agenda.pdf"
      }
      // 更多附件...
    ]
  }
}
```

**数据说明**：
- `data`：活动详情
  - `id`：活动ID
  - `title`：活动标题
  - `date`：活动日期
  - `time`：活动时间
  - `location`：活动地点
  - `category`：活动分类ID
  - `status`：活动状态
  - `description`：活动简介
  - `content`：活动详情内容（HTML格式）
  - `canRegister`：是否可报名
  - `registerDeadline`：报名截止日期
  - `participants`：已报名人数
  - `capacity`：活动容量
  - `views`：浏览量
  - `image`：活动图片
  - `attachments`：活动附件列表
    - `name`：附件名称
    - `url`：附件URL

**错误响应**：

```json
{
  "code": 404,
  "message": "未找到指定的活动",
  "data": null
}
```

### 4.4 活动报名

**接口URL**：`/api/activities/{id}/register`

**请求方法**：POST

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Integer | 是 | 活动ID |

**请求体**：

```json
{
  "userId": 1,
  "name": "张三",
  "phone": "13800138000"
}
```

**请求参数说明**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Integer | 是 | 用户ID |
| name | String | 是 | 参与者姓名 |
| phone | String | 是 | 联系电话 |

**响应数据**：

```json
{
  "code": 200,
  "message": "报名成功",
  "data": {
    "activityId": 1,
    "userId": 1,
    "registerTime": "2025-12-07T14:30:00",
    "status": "registered"
  }
}
```

**数据说明**：
- `data`：报名结果
  - `activityId`：活动ID
  - `userId`：用户ID
  - `registerTime`：报名时间
  - `status`：报名状态

**错误响应**：

```json
// 活动不存在
{
  "code": 404,
  "message": "活动不存在",
  "data": null
}

// 已报名参加该活动
{
  "code": 409,
  "message": "已报名参加该活动",
  "data": null
}

// 活动报名已截止
{
  "code": 410,
  "message": "活动报名已截止",
  "data": null
}

// 活动人数已满
{
  "code": 411,
  "message": "活动人数已满",
  "data": null
}
```

## 5. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 404 | 资源不存在 |
| 409 | 资源冲突（如已报名） |
| 410 | 资源已过期（如报名截止） |
| 411 | 资源已满（如活动人数已满） |
| 500 | 服务器内部错误 |

## 6. 使用示例

### 获取活动分类列表

```javascript
fetch('/api/activities/categories')
  .then(response => response.json())
  .then(data => {
    if (data.code === 200) {
      console.log('活动分类列表:', data.data);
    } else {
      console.error('获取失败:', data.message);
    }
  });
```

### 获取活动列表

```javascript
fetch('/api/activities?page=1&pageSize=4&category=theme')
  .then(response => response.json())
  .then(data => {
    if (data.code === 200) {
      console.log('活动列表:', data.data.activities);
      console.log('总页数:', data.data.pages);
    } else {
      console.error('获取失败:', data.message);
    }
  });
```

### 获取活动详情

```javascript
const activityId = 1;
fetch(`/api/activities/${activityId}`)
  .then(response => response.json())
  .then(data => {
    if (data.code === 200) {
      console.log('活动详情:', data.data);
    } else if (data.code === 404) {
      console.error('活动不存在');
    } else {
      console.error('获取失败:', data.message);
    }
  });
```

### 活动报名

```javascript
const activityId = 1;
const requestData = {
  userId: 1,
  name: '张三',
  phone: '13800138000'
};

fetch(`/api/activities/${activityId}/register`, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(requestData)
})
.then(response => response.json())
.then(data => {
  if (data.code === 200) {
    console.log('报名成功:', data.data);
  } else if (data.code === 404) {
    console.error('活动不存在');
  } else if (data.code === 409) {
    console.error('已报名参加该活动');
  } else if (data.code === 410) {
    console.error('活动报名已截止');
  } else if (data.code === 411) {
    console.error('活动人数已满');
  } else {
    console.error('报名失败:', data.message);
  }
});
```