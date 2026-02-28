# 首页主题活动与活动详情接口规范

## 一、首页主题活动展示接口

### 1. 获取首页推荐活动列表接口

#### 接口说明
获取首页展示的精选主题活动列表，用于在首页的推荐内容区域展示。

#### 请求信息
- **URL**: `/api/home/activities`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | limit | Integer | 否 | 每页显示的活动数量，默认6条 |
  | sortBy | String | 否 | 排序字段，可选值：`date`（按日期）、`participants`（按参与人数）、`views`（按浏览量），默认`date` |
  | order | String | 否 | 排序顺序，可选值：`asc`（升序）、`desc`（降序），默认`desc` |

#### 响应信息
- **响应格式**: JSON
- **响应状态码**: 200 OK

#### 响应示例
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 6,
      "title": "2025年秋季学期习近平新时代中国特色社会主义思想专题研讨班",
      "type": "活动",
      "date": "2025-10-20",
      "location": "计算机学院会议室",
      "participants": 120,
      "views": 500,
      "status": "upcoming",
      "image": "https://picsum.photos/id/130/300/200",
      "tags": ["习近平新时代中国特色社会主义思想", "专题研讨"]
    },
    {
      "id": 7,
      "title": "计算机学院2025年党员志愿服务月启动仪式",
      "type": "活动",
      "date": "2025-11-05",
      "location": "学校礼堂",
      "participants": 200,
      "views": 800,
      "status": "upcoming",
      "image": "https://picsum.photos/id/131/300/200",
      "tags": ["志愿服务", "党员活动"]
    }
  ]
}
```

## 二、活动详情接口

### 1. 获取活动详情接口

#### 接口说明
根据活动ID获取活动的详细信息，用于活动详情页面展示。

#### 请求信息
- **URL**: `/api/activities/{id}`
- **请求方法**: GET
- **路径参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 活动ID |

#### 响应信息
- **响应格式**: JSON
- **响应状态码**: 200 OK

#### 响应示例
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "title": "学习习近平新时代中国特色社会主义思想主题教育总结大会",
    "date": "2025-11-20",
    "time": "09:00 - 11:30",
    "location": "计算机学院党员活动室",
    "category": "theme",
    "status": "upcoming",
    "description": "总结计算机学院习近平新时代中国特色社会主义思想主题教育开展情况，表彰先进个人和集体，部署下一阶段学习任务。",
    "content": "<h2>活动详情</h2><p>一、活动背景：为深入贯彻落实党的二十大精神...</p>",
    "canRegister": true,
    "registerDeadline": "2025-11-18",
    "participants": 45,
    "capacity": 100,
    "views": 280,
    "image": "https://picsum.photos/id/130/300/200",
    "attachments": [
      {
        "name": "活动议程.pdf",
        "url": "/api/attachments/1"
      },
      {
        "name": "学习资料.zip",
        "url": "/api/attachments/2"
      }
    ],
    "tags": ["主题教育", "总结大会"],
    "createdAt": "2025-10-01T09:00:00Z",
    "updatedAt": "2025-10-10T14:30:00Z"
  }
}
```

### 2. 活动报名接口

#### 接口说明
用户报名参加某个活动（已在主题活动接口规范中定义，此处仅做引用）。

#### 请求信息
- **URL**: `/api/activities/{id}/register`
- **请求方法**: POST
- **路径参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 活动ID |

#### 请求体
```json
{
  "userId": 123, // 当前登录用户ID，可从Token中获取
  "name": "张三",
  "phone": "13800138000"
}
```

#### 响应信息
- **响应格式**: JSON
- **响应状态码**: 200 OK

#### 响应示例
```json
{
  "code": 200,
  "message": "报名成功",
  "data": {
    "activityId": 1,
    "userId": 123,
    "registerTime": "2025-11-10T10:30:00Z",
    "status": "registered"
  }
}
```

## 三、数据结构说明

### 1. 活动列表项数据结构（首页展示）

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Integer | 活动ID |
| title | String | 活动标题 |
| type | String | 内容类型，固定为"活动" |
| date | String | 活动日期，格式：YYYY-MM-DD |
| location | String | 活动地点 |
| participants | Integer | 参与人数 |
| views | Integer | 浏览量 |
| status | String | 活动状态，可选值：upcoming（即将开始）、ongoing（进行中）、completed（已结束） |
| image | String | 活动图片URL |
| tags | Array | 活动标签列表 |

### 2. 活动详情数据结构

| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Integer | 活动ID |
| title | String | 活动标题 |
| date | String | 活动日期，格式：YYYY-MM-DD |
| time | String | 活动时间，格式：HH:MM - HH:MM |
| location | String | 活动地点 |
| category | String | 活动分类ID |
| status | String | 活动状态 |
| description | String | 活动简短描述 |
| content | String | 活动详细内容（HTML格式） |
| canRegister | Boolean | 是否可报名 |
| registerDeadline | String | 报名截止日期，格式：YYYY-MM-DD |
| participants | Integer | 已报名人数 |
| capacity | Integer | 活动容量 |
| views | Integer | 浏览量 |
| image | String | 活动图片URL |
| attachments | Array | 活动附件列表 |
| tags | Array | 活动标签列表 |
| createdAt | String | 创建时间，格式：ISO 8601 |
| updatedAt | String | 更新时间，格式：ISO 8601 |

### 3. 附件数据结构

| 字段名 | 类型 | 描述 |
|--------|------|------|
| name | String | 附件名称 |
| url | String | 附件下载URL |

## 四、通用响应格式说明

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
- 404: 活动不存在
- 409: 已报名参加该活动
- 410: 活动报名已截止
- 411: 活动人数已满
- 500: 服务器内部错误

## 五、前端实现建议

### 1. 首页主题活动展示

```javascript
// 首页API调用示例
import axios from 'axios'

const getHomeActivities = async (params = {}) => {
  try {
    const response = await axios.get('/api/home/activities', { params })
    return response.data
  } catch (error) {
    console.error('获取首页活动失败:', error)
    throw error
  }
}

// 使用示例
const loadHomeActivities = async () => {
  try {
    const data = await getHomeActivities({
      limit: 6,
      sortBy: 'date',
      order: 'desc'
    })
    if (data.code === 200) {
      homeActivities.value = data.data
    }
  } catch (error) {
    errorMessage.value = '获取活动信息失败'
  }
}
```

### 2. 活动详情页面实现

```javascript
// 活动详情API调用示例
const getActivityDetail = async (id) => {
  try {
    const response = await axios.get(`/api/activities/${id}`)
    return response.data
  } catch (error) {
    console.error('获取活动详情失败:', error)
    throw error
  }
}

// 使用示例
const loadActivityDetail = async (id) => {
  try {
    const data = await getActivityDetail(id)
    if (data.code === 200) {
      activityDetail.value = data.data
    }
  } catch (error) {
    errorMessage.value = '获取活动详情失败'
  }
}
```

## 六、路由配置建议

```javascript
// 建议在router/index.js中添加活动详情路由
import ActivityDetail from '../views/front/ActivityDetail.vue'

const routes = [
  // ... 其他路由
  {
    path: '/activity/:id',
    name: 'ActivityDetail',
    component: ActivityDetail,
    meta: { requireAuth: false }
  }
]
```
