# 主题活动相关接口说明

## 接口概述
主题活动模块提供了活动分类查询、活动列表获取、活动详情查看以及活动报名等功能接口，支持分页查询、分类筛选和关键词搜索。

## 接口列表

### 1. 获取活动分类列表

**URL**: `/api/activities/categories`
**方法**: `GET`
**功能**: 获取所有活动分类
**权限**: 无需登录

#### 响应示例
```json
{
  "code": 200,
  "msg": "获取成功",
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

### 2. 获取活动列表

**URL**: `/api/activities`
**方法**: `GET`
**功能**: 获取活动列表，支持分页、分类筛选和关键词搜索
**权限**: 无需登录

#### 请求参数
| 参数名 | 类型 | 必填 | 描述 |
| ------ | ---- | ---- | ---- |
| page | Integer | 是 | 页码，从1开始 |
| pageSize | Integer | 是 | 每页数量，最大50 |
| category | String | 否 | 活动分类ID，如"theme" |
| keyword | String | 否 | 搜索关键词（标题或描述） |

#### 响应示例
```json
{
  "code": 200,
  "msg": "获取成功",
  "data": {
    "activities": [
      {
        "id": 1,
        "title": "主题党日活动",
        "date": "2023-10-15",
        "time": "09:00-11:30",
        "location": "党建活动室",
        "category": "theme",
        "status": "published",
        "description": "学习贯彻党的二十大精神",
        "canRegister": true,
        "participants": 15,
        "views": 120,
        "image": "/images/activity1.jpg"
      }
    ],
    "total": 6,
    "pages": 1,
    "page": 1,
    "pageSize": 10
  }
}
```

### 3. 获取活动详情

**URL**: `/api/activities/{id}`
**方法**: `GET`
**功能**: 获取活动详细信息
**权限**: 无需登录

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 |
| ------ | ---- | ---- | ---- |
| id | Integer | 是 | 活动ID |

#### 响应示例
```json
{
  "code": 200,
  "msg": "获取成功",
  "data": {
    "id": 1,
    "title": "主题党日活动",
    "date": "2023-10-15",
    "time": "09:00-11:30",
    "location": "党建活动室",
    "category": "theme",
    "status": "published",
    "description": "学习贯彻党的二十大精神",
    "content": "<p>活动内容详情...</p>",
    "canRegister": true,
    "registerDeadline": "2023-10-10",
    "participants": 15,
    "capacity": 50,
    "views": 120,
    "image": "/images/activity1.jpg",
    "attachments": [
      {
        "name": "活动议程.pdf",
        "url": "/attachments/agenda.pdf"
      }
    ]
  }
}
```

### 4. 活动报名

**URL**: `/api/activities/{id}/register`
**方法**: `POST`
**功能**: 报名参加活动
**权限**: 无需登录

#### 路径参数
| 参数名 | 类型 | 必填 | 描述 |
| ------ | ---- | ---- | ---- |
| id | Integer | 是 | 活动ID |

#### 请求体
```json
{
  "userId": 1,
  "name": "张三",
  "phone": "13800138000"
}
```

#### 请求参数说明
| 参数名 | 类型 | 必填 | 描述 |
| ------ | ---- | ---- | ---- |
| userId | Integer | 是 | 用户ID |
| name | String | 是 | 报名人姓名 |
| phone | String | 是 | 联系电话 |

#### 响应示例
```json
{
  "code": 200,
  "msg": "报名成功",
  "data": {
    "activityId": 1,
    "userId": 1,
    "registerTime": "2023-10-05T15:30:45",
    "status": "registered"
  }
}
```

## 数据结构说明

### ActivityCategoryDTO
| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| id | String | 分类ID |
| name | String | 分类名称 |

### ActivityItemDTO
| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| id | Integer | 活动ID |
| title | String | 活动标题 |
| date | String | 活动日期（格式：yyyy-MM-dd） |
| time | String | 活动时间 |
| location | String | 活动地点 |
| category | String | 活动分类ID |
| status | String | 活动状态 |
| description | String | 活动简介 |
| canRegister | Boolean | 是否可报名 |
| participants | Integer | 已报名人数 |
| views | Integer | 浏览量 |
| image | String | 活动图片URL |

### ActivityListDTO
| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| activities | List<ActivityItemDTO> | 活动列表 |
| total | Long | 总记录数 |
| pages | Integer | 总页数 |
| page | Integer | 当前页码 |
| pageSize | Integer | 每页数量 |

### ActivityDetailDTO
| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| id | Integer | 活动ID |
| title | String | 活动标题 |
| date | String | 活动日期（格式：yyyy-MM-dd） |
| time | String | 活动时间 |
| location | String | 活动地点 |
| category | String | 活动分类ID |
| status | String | 活动状态 |
| description | String | 活动简介 |
| content | String | 活动内容（富文本） |
| canRegister | Boolean | 是否可报名 |
| registerDeadline | String | 报名截止日期（格式：yyyy-MM-dd） |
| participants | Integer | 已报名人数 |
| capacity | Integer | 活动容量 |
| views | Integer | 浏览量 |
| image | String | 活动图片URL |
| attachments | List<AttachmentDTO> | 活动附件列表 |

### ActivityRegistrationRequest
| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| userId | Integer | 用户ID |
| name | String | 报名人姓名 |
| phone | String | 联系电话 |

### ActivityRegistrationResponse
| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| activityId | Integer | 活动ID |
| userId | Integer | 用户ID |
| registerTime | LocalDateTime | 报名时间 |
| status | String | 报名状态 |

### AttachmentDTO
| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| name | String | 附件名称 |
| url | String | 附件下载URL |

## 错误码说明

| 错误码 | 描述 |
| ------ | ---- |
| 200 | 成功 |
| 400 | 参数错误 |
| 404 | 活动不存在 |
| 409 | 已报名参加该活动 |
| 410 | 活动报名已截止 |
| 413 | 活动人数已满 |
| 500 | 服务器内部错误 |

## 业务逻辑说明

1. **活动列表查询**：
   - 支持分页查询，默认每页10条记录，最大50条
   - 支持按分类筛选
   - 支持按标题或描述关键词搜索

2. **活动报名**：
   - 验证活动是否存在
   - 验证用户是否已报名该活动
   - 验证报名是否已截止
   - 验证活动人数是否已满
   - 报名成功后更新活动参与人数

3. **活动状态管理**：
   - 活动状态包括：published（已发布）、draft（草稿）、ended（已结束）等
   - 只有已发布的活动才会在列表中显示
   - 超过报名截止日期或活动人数已满的活动将不可报名

## 接口测试说明

1. **活动列表接口**：需要提供page和pageSize参数，示例：
   ```
   GET /api/activities?page=1&pageSize=10
   ```

2. **活动报名接口**：需要提供完整的报名信息，示例：
   ```
   POST /api/activities/1/register
   Content-Type: application/json
   
   {
     "userId": 1,
     "name": "张三",
     "phone": "13800138000"
   }
   ```

3. **接口响应格式**：所有接口统一返回Response对象，包含code（状态码）、msg（消息）和data（数据）字段。