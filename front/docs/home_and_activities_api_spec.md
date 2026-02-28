# 前台首页与主题活动界面后端接口规范

## 一、首页接口

### 1. 获取推荐内容接口

#### 接口说明
获取首页推荐的内容，包括新闻、学习资源和活动。

#### 请求信息
- **URL**: `/api/home/recommended`
- **请求方法**: GET
- **请求参数**: 无

#### 响应信息
- **响应格式**: JSON
- **响应状态码**: 200 OK

#### 响应示例
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "news": [
      {
        "id": 1,
        "title": "习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话",
        "type": "新闻",
        "date": "2025-07-01",
        "tags": ["习近平新时代中国特色社会主义思想", "建党节"]
      },
      {
        "id": 2,
        "title": "中共中央关于加强和改进新形势下党的建设的意见",
        "type": "新闻",
        "date": "2025-08-15",
        "tags": ["党建", "政策文件"]
      }
    ],
    "activities": [
      {
        "id": 6,
        "title": "2025年秋季学期习近平新时代中国特色社会主义思想专题研讨班",
        "type": "活动",
        "date": "2025-10-20",
        "location": "计算机学院会议室",
        "participants": 120,
        "tags": ["习近平新时代中国特色社会主义思想", "专题研讨"]
      }
    ],
    "learningResources": [
      {
        "id": 4,
        "title": "习近平新时代中国特色社会主义思想学习纲要（2025年版）",
        "type": "学习资源",
        "date": "2025-10-01",
        "views": 2500,
        "tags": ["习近平新时代中国特色社会主义思想", "学习纲要"]
      }
    ]
  }
}
```

### 2. 获取样板支部接口

#### 接口说明
获取样板支部的信息，用于首页展示。

#### 请求信息
- **URL**: `/api/branches/model`
- **请求方法**: GET
- **请求参数**: 无

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
      "id": 1,
      "name": "计算机学院学生第一党支部",
      "description": "全国样板党支部，连续三年被评为先进党支部",
      "image": "https://picsum.photos/id/2/150/100"
    },
    {
      "id": 2,
      "name": "马克思主义学院教工党支部",
      "description": "省级样板党支部，党建与教学融合成效显著",
      "image": "https://picsum.photos/id/3/150/100"
    }
  ]
}
```

## 二、主题活动接口

### 1. 获取活动分类列表接口

#### 接口说明
获取主题活动的分类列表，用于活动页面的分类筛选功能。

#### 请求信息
- **URL**: `/api/activities/categories`
- **请求方法**: GET
- **请求参数**: 无

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
      "id": "theme",
      "name": "主题党日"
    },
    {
      "id": "study",
      "name": "学习教育"
    },
    {
      "id": "volunteer",
      "name": "志愿服务"
    },
    {
      "id": "democratic",
      "name": "民主生活"
    },
    {
      "id": "social",
      "name": "社会实践"
    },
    {
      "id": "training",
      "name": "培训交流"
    }
  ]
}
```

### 2. 获取活动列表接口

#### 接口说明
获取主题活动列表，支持分页、分类筛选和关键词搜索。

#### 请求信息
- **URL**: `/api/activities`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | page | Integer | 是 | 当前页码，从1开始 |
  | pageSize | Integer | 是 | 每页显示的活动数量 |
  | category | String | 否 | 活动分类ID，对应活动分类列表中的id字段 |
  | keyword | String | 否 | 搜索关键词，支持对活动标题和描述的模糊搜索 |

#### 响应信息
- **响应格式**: JSON
- **响应状态码**: 200 OK

#### 响应示例
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "activities": [
      {
        "id": 1,
        "title": "学习习近平新时代中国特色社会主义思想主题教育总结大会",
        "date": "2025-11-20",
        "time": "09:00 - 11:30",
        "location": "计算机学院党员活动室",
        "category": "theme",
        "status": "upcoming",
        "description": "总结计算机学院习近平新时代中国特色社会主义思想主题教育开展情况，表彰先进个人和集体，部署下一阶段学习任务。",
        "canRegister": true,
        "participants": 45,
        "views": 280,
        "image": "https://picsum.photos/id/130/300/200"
      },
      {
        "id": 2,
        "title": "党史学习教育专题讲座：改革开放史",
        "date": "2025-11-15",
        "time": "14:00 - 16:30",
        "location": "计算机学院会议室301",
        "category": "study",
        "status": "completed",
        "description": "邀请党史专家讲解改革开放史，深入学习改革开放以来党的理论创新和实践成果，增强党员的历史责任感和使命感。",
        "canRegister": false,
        "participants": 62,
        "views": 320,
        "image": "https://picsum.photos/id/132/300/200"
      }
    ],
    "total": 10,
    "pages": 3,
    "page": 1,
    "pageSize": 4
  }
}
```

### 3. 获取活动详情接口

#### 接口说明
根据活动ID获取活动的详细信息。

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
    "content": "<h2>活动详情</h2><p>一、活动背景...</p>",
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
      }
    ]
  }
}
```

### 4. 活动报名接口

#### 接口说明
用户报名参加某个活动。

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

## 三、通用响应格式说明

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
- 409: 已报名参加该活动
- 410: 活动报名已截止
- 411: 活动人数已满
- 500: 服务器内部错误