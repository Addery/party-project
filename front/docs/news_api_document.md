# 新闻模块后端接口文档

## 1. 接口概述

本文档定义了新闻模块的后端API接口，包括前台新闻展示和后台新闻管理所需的所有接口。

## 2. 基础URL

所有接口的基础URL为：`/api/news`

## 3. 接口列表

### 3.1 前台接口

#### 3.1.1 获取新闻列表

- **请求方法**：GET
- **请求路径**：`/`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | page | int | 否 | 当前页码，默认1 |
  | pageSize | int | 否 | 每页条数，默认10 |
  | category | string | 否 | 新闻分类，可选 |
  | keyword | string | 否 | 搜索关键词，标题或内容 |

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "list": [
        {
          "id": 1,
          "title": "新闻标题",
          "date": "2025-11-28",
          "category": "头条新闻",
          "summary": "新闻摘要",
          "author": "作者",
          "image": "https://example.com/image.jpg"
        }
      ],
      "total": 100,
      "page": 1,
      "pageSize": 10
    }
  }
  ```

#### 3.1.2 获取新闻详情

- **请求方法**：GET
- **请求路径**：`/:id`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | int | 是 | 新闻ID |

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "title": "新闻标题",
      "date": "2025-11-28",
      "category": "头条新闻",
      "author": "作者",
      "image": "https://example.com/image.jpg",
      "content": "<p>新闻内容</p>"
    }
  }
  ```

#### 3.1.3 获取相关新闻

- **请求方法**：GET
- **请求路径**：`/related`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | category | string | 是 | 新闻分类 |
  | excludeId | int | 是 | 排除的新闻ID |
  | limit | int | 否 | 返回数量，默认3 |

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 2,
        "title": "相关新闻标题",
        "date": "2025-11-27",
        "category": "头条新闻",
        "summary": "相关新闻摘要"
      }
    ]
  }
  ```

#### 3.1.4 获取新闻分类

- **请求方法**：GET
- **请求路径**：`/categories`
- **请求参数**：无

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      "头条新闻",
      "学校动态",
      "通知公告",
      "党建工作",
      "思政教育"
    ]
  }
  ```

### 3.2 后台接口

#### 3.2.1 获取新闻列表（带分页和筛选）

- **请求方法**：GET
- **请求路径**：`/admin/list`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | page | int | 否 | 当前页码，默认1 |
  | pageSize | int | 否 | 每页条数，默认10 |
  | category | string | 否 | 新闻分类，可选 |
  | status | string | 否 | 新闻状态，可选：draft/published/offline |
  | keyword | string | 否 | 搜索关键词，标题或内容 |

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "list": [
        {
          "id": 1,
          "title": "新闻标题",
          "category": "headline",
          "author": "作者",
          "source": "来源",
          "publishDate": "2025-11-28 14:30:00",
          "views": 100,
          "status": "published",
          "isTop": true,
          "summary": "新闻摘要",
          "cover": "https://example.com/cover.jpg"
        }
      ],
      "total": 100,
      "page": 1,
      "pageSize": 10
    }
  }
  ```

#### 3.2.2 添加新闻

- **请求方法**：POST
- **请求路径**：`/admin`
- **请求参数**：
  ```json
  {
    "title": "新闻标题",
    "category": "headline",
    "author": "作者",
    "source": "来源",
    "publishDate": "2025-11-28 14:30:00",
    "summary": "新闻摘要",
    "content": "<p>新闻内容</p>",
    "cover": "https://example.com/cover.jpg",
    "status": "draft",
    "isTop": false
  }
  ```

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "新闻添加成功",
    "data": {
      "id": 1
    }
  }
  ```

#### 3.2.3 编辑新闻

- **请求方法**：PUT
- **请求路径**：`/admin/:id`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | int | 是 | 新闻ID |
  | title | string | 是 | 新闻标题 |
  | category | string | 是 | 新闻分类 |
  | author | string | 是 | 作者 |
  | source | string | 是 | 来源 |
  | publishDate | string | 是 | 发布日期 |
  | summary | string | 是 | 新闻摘要 |
  | content | string | 是 | 新闻内容 |
  | cover | string | 否 | 封面图片URL |
  | status | string | 是 | 新闻状态 |
  | isTop | boolean | 是 | 是否置顶 |

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "新闻编辑成功",
    "data": null
  }
  ```

#### 3.2.4 删除新闻

- **请求方法**：DELETE
- **请求路径**：`/admin/:id`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | int | 是 | 新闻ID |

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "新闻删除成功",
    "data": null
  }
  ```

#### 3.2.5 批量发布新闻

- **请求方法**：POST
- **请求路径**：`/admin/batch-publish`
- **请求参数**：
  ```json
  {
    "ids": [1, 2, 3]
  }
  ```

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "新闻批量发布成功",
    "data": null
  }
  ```

#### 3.2.6 批量删除新闻

- **请求方法**：POST
- **请求路径**：`/admin/batch-delete`
- **请求参数**：
  ```json
  {
    "ids": [1, 2, 3]
  }
  ```

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "新闻批量删除成功",
    "data": null
  }
  ```

#### 3.2.7 更新新闻状态

- **请求方法**：PUT
- **请求路径**：`/admin/:id/status`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | int | 是 | 新闻ID |
  | status | string | 是 | 新闻状态：draft/published/offline |

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "新闻状态更新成功",
    "data": null
  }
  ```

#### 3.2.8 上传封面图片

- **请求方法**：POST
- **请求路径**：`/admin/upload-cover`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | file | file | 是 | 封面图片文件 |

- **返回参数**：
  ```json
  {
    "code": 200,
    "message": "封面上传成功",
    "data": {
      "url": "https://example.com/cover.jpg"
    }
  }
  ```

## 4. 数据模型

### 4.1 新闻表（news）

| 字段名 | 数据类型 | 长度 | 允许空 | 主键 | 描述 |
| ------ | ------- | ---- | ------ | ---- | ---- |
| id | int | 11 | 否 | 是 | 新闻ID |
| title | varchar | 255 | 否 | 否 | 新闻标题 |
| category | varchar | 50 | 否 | 否 | 新闻分类 |
| author | varchar | 100 | 否 | 否 | 作者 |
| source | varchar | 100 | 是 | 否 | 来源 |
| publish_date | datetime | | 否 | 否 | 发布日期 |
| summary | varchar | 500 | 是 | 否 | 新闻摘要 |
| content | text | | 否 | 否 | 新闻内容 |
| cover | varchar | 255 | 是 | 否 | 封面图片URL |
| views | int | 11 | 否 | 否 | 浏览量 |
| status | varchar | 20 | 否 | 否 | 新闻状态：draft/published/offline |
| is_top | tinyint | 1 | 否 | 否 | 是否置顶：0/1 |
| created_at | datetime | | 否 | 否 | 创建时间 |
| updated_at | datetime | | 否 | 否 | 更新时间 |

## 5. 错误码

| 错误码 | 描述 |
| ------ | ---- |
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 6. 注意事项

1. 所有接口返回统一的JSON格式，包含code、message和data字段
2. 后台接口需要进行权限验证
3. 文件上传接口需要支持multipart/form-data格式
4. 分页接口需要返回总数、当前页和每页条数信息
5. 新闻内容支持HTML格式