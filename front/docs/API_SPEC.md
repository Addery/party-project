# 红芯云课堂 - 后端API接口规范

## 1. 接口概述

本文档定义了红芯云课堂模块的后端API接口规范，包括学习资源的获取、详情展示、相关推荐、收藏管理和学习记录等功能。

## 2. 接口列表

| 接口名称 | URL | 请求方法 | 功能描述 |
|---------|-----|---------|--------|
| 获取资源分类 | /api/learning/categories | GET | 获取学习资源分类列表 |
| 获取资源列表 | /api/learning/resources | GET | 获取学习资源列表，支持分页和筛选 |
| 获取资源详情 | /api/learning/resources/{id} | GET | 获取指定学习资源的详细信息 |
| 获取相关资源 | /api/learning/resources/{id}/related | GET | 获取与指定资源相关的学习资源 |
| 检查收藏状态 | /api/learning/resources/{id}/favorite | GET | 检查当前用户是否收藏了指定资源 |
| 添加收藏 | /api/learning/resources/{id}/favorite | POST | 当前用户收藏指定资源 |
| 取消收藏 | /api/learning/resources/{id}/favorite | DELETE | 当前用户取消收藏指定资源 |
| 开始学习 | /api/learning/resources/{id}/start | POST | 记录用户开始学习指定资源 |

## 3. 接口详细规范

### 3.1 获取资源分类

**URL**: `/api/learning/categories`
**请求方法**: GET
**请求参数**: 无

**响应格式**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": "theory",
      "name": "理论学习"
    },
    {
      "id": "history",
      "name": "党史教育"
    },
    {
      "id": "policy",
      "name": "政策法规"
    },
    {
      "id": "model",
      "name": "先进典型"
    },
    {
      "id": "integrity",
      "name": "廉政教育"
    }
  ]
}
```

### 3.2 获取资源列表

**URL**: `/api/learning/resources`
**请求方法**: GET
**请求参数**:
| 参数名 | 类型 | 必填 | 描述 |
|-------|-----|-----|-----|
| page | int | 否 | 当前页码，默认为1 |
| pageSize | int | 否 | 每页大小，默认为10 |
| category | string | 否 | 资源分类ID |
| keyword | string | 否 | 搜索关键词，支持标题和描述搜索 |

**响应格式**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "list": [
      {
        "id": 1,
        "title": "习近平新时代中国特色社会主义思想学习纲要",
        "type": "article",
        "category": "theory",
        "author": "中共中央宣传部",
        "date": "2023-09-15",
        "duration": "约45分钟",
        "level": "初级",
        "views": 1258,
        "rating": 4.8,
        "image": "https://example.com/image.jpg",
        "description": "深入学习习近平新时代中国特色社会主义思想的重要辅导读物"
      }
    ]
  }
}
```

### 3.3 获取资源详情

**URL**: `/api/learning/resources/{id}`
**请求方法**: GET
**请求参数**:
| 参数名 | 类型 | 必填 | 描述 |
|-------|-----|-----|-----|
| id | int | 是 | 资源ID |

**响应格式**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "习近平新时代中国特色社会主义思想学习纲要",
    "type": "article",
    "category": "theory",
    "author": "中共中央宣传部",
    "date": "2023-09-15",
    "duration": "约45分钟",
    "level": "初级",
    "views": 1258,
    "rating": 4.8,
    "image": "https://example.com/image.jpg",
    "description": "深入学习习近平新时代中国特色社会主义思想的重要辅导读物",
    "content": "<p>习近平新时代中国特色社会主义思想是当代中国马克思主义、二十一世纪马克思主义...</p>",
    "tags": ["习近平新时代中国特色社会主义思想", "理论学习", "党的建设"]
  }
}
```

### 3.4 获取相关资源

**URL**: `/api/learning/resources/{id}/related`
**请求方法**: GET
**请求参数**:
| 参数名 | 类型 | 必填 | 描述 |
|-------|-----|-----|-----|
| id | int | 是 | 资源ID |
| limit | int | 否 | 返回数量，默认为3 |

**响应格式**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 2,
      "title": "中国共产党百年奋斗历程",
      "type": "video",
      "author": "中央党校",
      "date": "2023-08-20",
      "image": "https://example.com/image2.jpg"
    }
  ]
}
```

### 3.5 检查收藏状态

**URL**: `/api/learning/resources/{id}/favorite`
**请求方法**: GET
**请求参数**:
| 参数名 | 类型 | 必填 | 描述 |
|-------|-----|-----|-----|
| id | int | 是 | 资源ID |

**响应格式**:
```json
{
  "code": 200,
  "message": "success",
  "data": true
}
```

### 3.6 添加收藏

**URL**: `/api/learning/resources/{id}/favorite`
**请求方法**: POST
**请求参数**:
| 参数名 | 类型 | 必填 | 描述 |
|-------|-----|-----|-----|
| id | int | 是 | 资源ID |

**响应格式**:
```json
{
  "code": 200,
  "message": "收藏成功",
  "data": null
}
```

### 3.7 取消收藏

**URL**: `/api/learning/resources/{id}/favorite`
**请求方法**: DELETE
**请求参数**:
| 参数名 | 类型 | 必填 | 描述 |
|-------|-----|-----|-----|
| id | int | 是 | 资源ID |

**响应格式**:
```json
{
  "code": 200,
  "message": "取消收藏成功",
  "data": null
}
```

### 3.8 开始学习

**URL**: `/api/learning/resources/{id}/start`
**请求方法**: POST
**请求参数**:
| 参数名 | 类型 | 必填 | 描述 |
|-------|-----|-----|-----|
| id | int | 是 | 资源ID |

**响应格式**:
```json
{
  "code": 200,
  "message": "开始学习成功",
  "data": {
    "recordId": 123
  }
}
```

## 4. 数据模型

### 4.1 学习资源模型

```json
{
  "id": 1,
  "title": "习近平新时代中国特色社会主义思想学习纲要",
  "type": "article", // article, video, audio, document, case, quiz
  "category": "theory", // theory, history, policy, model, integrity
  "author": "中共中央宣传部",
  "date": "2023-09-15",
  "duration": "约45分钟",
  "level": "初级", // 初级, 中级, 高级
  "views": 1258,
  "rating": 4.8,
  "image": "https://example.com/image.jpg",
  "description": "深入学习习近平新时代中国特色社会主义思想的重要辅导读物",
  "content": "<p>习近平新时代中国特色社会主义思想是当代中国马克思主义、二十一世纪马克思主义...</p>",
  "tags": ["习近平新时代中国特色社会主义思想", "理论学习", "党的建设"],
  "createTime": "2023-09-15 10:00:00",
  "updateTime": "2023-09-15 10:00:00"
}
```

### 4.2 学习资源分类模型

```json
{
  "id": "theory",
  "name": "理论学习",
  "order": 1
}
```

## 5. 错误码定义

| 错误码 | 描述 |
|-------|-----|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 6. 注意事项

1. 所有接口返回数据格式统一为JSON
2. 接口调用需要携带有效的用户认证信息（如JWT token）
3. 分页接口默认每页返回10条数据
4. 资源内容支持HTML格式，用于富文本展示
5. 相关资源推荐基于资源分类和标签进行匹配