# 样板支部接口规范

## 1. 获取样板支部列表接口

### 接口说明
获取首页展示的样板支部列表，用于在首页的样板支部板块展示。

### 请求信息
- **URL**: `/api/branches/model`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | limit | Integer | 否 | 每页显示的样板支部数量，默认6条 |
  | sortBy | String | 否 | 排序字段，可选值：`createdAt`（按创建时间）、`name`（按名称），默认`createdAt` |
  | order | String | 否 | 排序顺序，可选值：`asc`（升序）、`desc`（降序），默认`desc` |

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
      "id": 1,
      "name": "计算机学院学生第一党支部",
      "description": "全国样板党支部，连续三年被评为先进党支部",
      "image": "https://picsum.photos/id/2/150/100",
      "level": "national",
      "createdAt": "2025-01-15T08:30:00Z"
    },
    {
      "id": 2,
      "name": "马克思主义学院教工党支部",
      "description": "省级样板党支部，党建与教学融合成效显著",
      "image": "https://picsum.photos/id/3/150/100",
      "level": "provincial",
      "createdAt": "2025-02-20T10:15:00Z"
    }
  ]
}
```

## 2. 获取样板支部详情接口

### 接口说明
根据ID获取样板支部的详细信息，用于查看样板支部的完整内容。

### 请求信息
- **URL**: `/api/branches/model/{id}`
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
  "data": {
    "id": 1,
    "name": "计算机学院学生第一党支部",
    "description": "全国样板党支部，连续三年被评为先进党支部",
    "image": "https://picsum.photos/id/2/300/200",
    "level": "national",
    "introduction": "计算机学院学生第一党支部成立于2018年，现有党员35名，其中正式党员28名，预备党员7名。党支部始终坚持以习近平新时代中国特色社会主义思想为指导，深入贯彻落实党的二十大精神，不断加强党的建设，取得了显著成效。",
    "achievements": [
      "2022年被评为全国样板党支部",
      "2023年被评为先进党支部",
      "2024年被评为先进党支部",
      "2025年被评为先进党支部"
    ],
    "activities": [
      {
        "title": "主题党日活动",
        "date": "2025-05-20",
        "content": "组织党员参观革命纪念馆，重温入党誓词"
      },
      {
        "title": "志愿服务活动",
        "date": "2025-06-10",
        "content": "组织党员到社区开展志愿服务，帮助困难群众"
      }
    ],
    "createdAt": "2025-01-15T08:30:00Z",
    "updatedAt": "2025-06-20T14:45:00Z"
  }
}
```

## 3. 字段说明

### 样板支部列表字段
| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Integer | 样板支部ID |
| name | String | 样板支部名称 |
| description | String | 样板支部简介 |
| image | String | 样板支部图片URL |
| level | String | 样板支部级别（national: 国家级, provincial: 省级, municipal: 市级） |
| createdAt | String | 创建时间 |

### 样板支部详情字段
| 字段名 | 类型 | 描述 |
|--------|------|------|
| id | Integer | 样板支部ID |
| name | String | 样板支部名称 |
| description | String | 样板支部简介 |
| image | String | 样板支部图片URL |
| level | String | 样板支部级别（national: 国家级, provincial: 省级, municipal: 市级） |
| introduction | String | 样板支部详细介绍 |
| achievements | Array | 样板支部成就列表 |
| activities | Array | 样板支部活动列表 |
| createdAt | String | 创建时间 |
| updatedAt | String | 更新时间 |
