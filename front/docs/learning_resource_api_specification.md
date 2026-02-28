# 前台学习资源后端接口规范

## 1. 接口概述

本文档定义了党员教育与管理系统中前台学习资源功能所需的后端API接口规范，仅包含前端展示所需的接口，不涉及后台管理系统功能。

## 2. 接口列表

| 接口名称 | 请求路径 | 请求方法 | 功能描述 |
|---------|---------|---------|---------|
| 获取学习资源分类列表 | `/api/learning/resources/categories` | GET | 获取学习资源的分类列表 |
| 获取学习资源列表 | `/api/learning/resources` | GET | 获取分页的学习资源列表 |
| 获取学习资源详情 | `/api/learning/resources/{id}` | GET | 获取指定学习资源的详细信息 |
| 开始学习资源 | `/api/learning/resources/{id}/start` | POST | 记录用户开始学习资源的行为 |

## 3. 接口详情

### 3.1 获取学习资源分类列表

**URL**: `/api/learning/resources/categories`
**请求方法**: GET
**请求参数**: 无

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": "video",
      "name": "视频课程"
    },
    {
      "id": "document",
      "name": "文档资料"
    },
    {
      "id": "audio",
      "name": "音频资源"
    },
    {
      "id": "article",
      "name": "学习文章"
    },
    {
      "id": "case",
      "name": "案例分析"
    },
    {
      "id": "quiz",
      "name": "在线测试"
    }
  ]
}
```

### 3.2 获取学习资源列表

**URL**: `/api/learning/resources`
**请求方法**: GET
**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认6 |
| type | String | 否 | 资源类型筛选（video/document/audio/article/case/quiz），不填表示全部 |
| keyword | String | 否 | 搜索关键词，用于模糊搜索标题和描述 |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "total": 12,
    "page": 1,
    "pageSize": 6,
    "pages": 2,
    "resources": [
      {
        "id": 1,
        "title": "习近平新时代中国特色社会主义思想概论",
        "author": "中共中央党校",
        "date": "2025-04-20",
        "duration": "6小时",
        "type": "video",
        "level": "初级",
        "category": "video",
        "description": "本课程系统阐述了习近平新时代中国特色社会主义思想的核心要义、精神实质、丰富内涵、实践要求，是党员干部学习贯彻党的创新理论的重要教材。",
        "image": "https://picsum.photos/id/10/300/200",
        "views": 12580,
        "rating": 4.8
      },
      {
        "id": 2,
        "title": "中国共产党章程（2022年修订）",
        "author": "中共中央",
        "date": "2025-03-15",
        "duration": "30分钟",
        "type": "document",
        "level": "初级",
        "category": "document",
        "description": "2022年10月22日中国共产党第二十次全国代表大会通过的《中国共产党章程》修订版全文，是全党必须遵循的总章程。",
        "image": "https://picsum.photos/id/20/300/200",
        "views": 8920,
        "rating": 4.9
      }
    ]
  }
}
```

### 3.3 获取学习资源详情

**URL**: `/api/learning/resources/{id}`
**请求方法**: GET
**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| id | Integer | 是 | 学习资源ID |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "title": "习近平新时代中国特色社会主义思想概论",
    "author": "中共中央党校",
    "date": "2025-04-20",
    "duration": "6小时",
    "type": "video",
    "level": "初级",
    "category": "video",
    "description": "本课程系统阐述了习近平新时代中国特色社会主义思想的核心要义、精神实质、丰富内涵、实践要求，是党员干部学习贯彻党的创新理论的重要教材。",
    "image": "https://picsum.photos/id/10/300/200",
    "views": 12580,
    "rating": 4.8,
    "content": "<p>课程详细内容...</p>",
    "url": "https://example.com/learning/videos/1",
    "attachments": [
      {
        "name": "课程大纲.docx",
        "url": "/uploads/attachments/20250420/123456.docx"
      }
    ],
    "chapters": [
      {
        "id": 1,
        "title": "第一章：习近平新时代中国特色社会主义思想的创立",
        "duration": "45分钟",
        "order": 1
      },
      {
        "id": 2,
        "title": "第二章：习近平新时代中国特色社会主义思想的核心要义",
        "duration": "40分钟",
        "order": 2
      }
    ]
  }
}
```

### 3.4 开始学习资源

**URL**: `/api/learning/resources/{id}/start`
**请求方法**: POST
**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| id | Integer | 是 | 学习资源ID |

**响应格式**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "success": true,
    "studyRecordId": 123,
    "message": "开始学习记录已创建"
  }
}
```

## 4. 数据结构定义

### 4.1 学习资源分类

```json
{
  "id": "video",
  "name": "视频课程"
}
```

**字段说明**:
- `id`: 分类ID
- `name`: 分类名称

### 4.2 学习资源列表项

```json
{
  "id": 1,
  "title": "习近平新时代中国特色社会主义思想概论",
  "author": "中共中央党校",
  "date": "2025-04-20",
  "duration": "6小时",
  "type": "video",
  "level": "初级",
  "category": "video",
  "description": "课程描述...",
  "image": "https://picsum.photos/id/10/300/200",
  "views": 12580,
  "rating": 4.8
}
```

**字段说明**:
- `id`: 资源ID
- `title`: 资源标题
- `author`: 作者/机构
- `date`: 发布日期
- `duration`: 时长
- `type`: 资源类型（video/document/audio/article/case/quiz）
- `level`: 难度等级（初级/中级/高级）
- `category`: 分类
- `description`: 资源描述
- `image`: 封面图片URL
- `views`: 学习次数
- `rating`: 评分（1-5分）

### 4.3 学习资源详情

```json
{
  "id": 1,
  "title": "习近平新时代中国特色社会主义思想概论",
  "author": "中共中央党校",
  "date": "2025-04-20",
  "duration": "6小时",
  "type": "video",
  "level": "初级",
  "category": "video",
  "description": "课程描述...",
  "image": "https://picsum.photos/id/10/300/200",
  "views": 12580,
  "rating": 4.8,
  "content": "<p>课程详细内容...</p>",
  "url": "https://example.com/learning/videos/1",
  "attachments": [
    {
      "name": "课程大纲.docx",
      "url": "/uploads/attachments/20250420/123456.docx"
    }
  ],
  "chapters": [
    {
      "id": 1,
      "title": "第一章：习近平新时代中国特色社会主义思想的创立",
      "duration": "45分钟",
      "order": 1
    }
  ]
}
```

**字段说明**:
- `id`: 资源ID
- `title`: 资源标题
- `author`: 作者/机构
- `date`: 发布日期
- `duration`: 时长
- `type`: 资源类型（video/document/audio/article/case/quiz）
- `level`: 难度等级（初级/中级/高级）
- `category`: 分类
- `description`: 资源描述
- `image`: 封面图片URL
- `views`: 学习次数
- `rating`: 评分（1-5分）
- `content`: 详细内容（HTML格式）
- `url`: 资源播放/访问URL
- `attachments`: 附件列表
- `chapters`: 章节列表（仅视频/音频资源）

### 4.4 附件

```json
{
  "name": "课程大纲.docx",
  "url": "/uploads/attachments/20250420/123456.docx"
}
```

**字段说明**:
- `name`: 附件名称
- `url`: 附件下载URL

### 4.5 章节

```json
{
  "id": 1,
  "title": "第一章：习近平新时代中国特色社会主义思想的创立",
  "duration": "45分钟",
  "order": 1
}
```

**字段说明**:
- `id`: 章节ID
- `title`: 章节标题
- `duration`: 章节时长
- `order`: 章节顺序

## 5. 错误码定义

| 错误码 | 错误信息 | 描述 |
|-------|---------|------|
| 200 | 成功 | 请求成功 |
| 400 | 请求参数错误 | 请求参数格式不正确或缺少必要参数 |
| 404 | 资源不存在 | 请求的学习资源不存在 |
| 500 | 服务器内部错误 | 服务器处理请求时发生内部错误 |