# 头条新闻后端接口文档

## 1. 接口概述

本文档定义了党建管理系统中头条新闻功能所需的后端接口，包括前台获取头条新闻列表、后台管理头条新闻（置顶、分类、发布等）功能。

## 2. 数据库结构

### 新闻表 (news)

```sql
CREATE TABLE IF NOT EXISTS news (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '新闻ID',
    title VARCHAR(200) NOT NULL COMMENT '新闻标题',
    category ENUM('时政要闻', '党建动态', '组织建设', '科技创新', '党风廉政', '基层党建') NOT NULL COMMENT '新闻分类',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    source VARCHAR(100) COMMENT '来源',
    publish_date DATETIME NOT NULL COMMENT '发布日期',
    summary TEXT COMMENT '新闻摘要',
    content LONGTEXT NOT NULL COMMENT '新闻内容',
    cover VARCHAR(255) COMMENT '封面图片',
    views INT DEFAULT 0 COMMENT '浏览量',
    status ENUM('draft', 'published') NOT NULL DEFAULT 'draft' COMMENT '状态',
    is_top BOOLEAN DEFAULT FALSE COMMENT '是否置顶',
    created_by INT COMMENT '创建人ID',
    updated_by INT COMMENT '更新人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '新闻表';
```

## 3. 前台接口

### 3.1 获取头条新闻列表

**功能**：获取首页展示的头条新闻列表

**请求方法**：`GET`

**请求路径**：`/api/news/headline`

**请求参数**：

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认3 |
| sortBy | String | 否 | 排序字段，默认publish_date |
| order | String | 否 | 排序方式，默认desc |

**返回格式**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "newsList": [
      {
        "id": 1,
        "title": "习近平主持召开中央全面深化改革委员会第五次会议",
        "category": "时政要闻",
        "author": "新华社",
        "source": "新华网",
        "publish_date": "2025-11-20T09:00:00Z",
        "summary": "习近平强调，要深入贯彻落实党的二十大精神，坚定不移全面深化改革...",
        "content": "<p>习近平主持召开中央全面深化改革委员会第五次会议...</p>",
        "cover": "https://example.com/cover.jpg",
        "views": 1234,
        "status": "published",
        "is_top": true
      },
      // 更多新闻...
    ],
    "total": 10,
    "page": 1,
    "pageSize": 3
  }
}
```

### 3.2 获取新闻详情

**功能**：获取单条新闻的详细信息

**请求方法**：`GET`

**请求路径**：`/api/news/:id`

**请求参数**：

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Integer | 是 | 新闻ID |

**返回格式**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "title": "习近平主持召开中央全面深化改革委员会第五次会议",
    "category": "时政要闻",
    "author": "新华社",
    "source": "新华网",
    "publish_date": "2025-11-20T09:00:00Z",
    "summary": "习近平强调，要深入贯彻落实党的二十大精神，坚定不移全面深化改革...",
    "content": "<p>习近平主持召开中央全面深化改革委员会第五次会议...</p>",
    "cover": "https://example.com/cover.jpg",
    "views": 1234,
    "status": "published",
    "is_top": true,
    "created_at": "2025-11-20T08:00:00Z",
    "updated_at": "2025-11-20T08:30:00Z"
  }
}
```

## 4. 后台接口

### 4.1 获取新闻列表（含头条筛选）

**功能**：获取新闻列表，支持按分类（包括头条新闻）、状态、置顶等条件筛选

**请求方法**：`GET`

**请求路径**：`/api/admin/news`

**请求参数**：

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| search | String | 否 | 搜索关键词（标题、内容） |
| category | String | 否 | 分类筛选 |
| status | String | 否 | 状态筛选（draft/published） |
| isTop | Boolean | 否 | 置顶筛选 |
| sortBy | String | 否 | 排序字段 |
| order | String | 否 | 排序方式 |

**返回格式**：

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "newsList": [
      {
        "id": 1,
        "title": "习近平主持召开中央全面深化改革委员会第五次会议",
        "category": "时政要闻",
        "author": "新华社",
        "source": "新华网",
        "publish_date": "2025-11-20T09:00:00Z",
        "views": 1234,
        "status": "published",
        "is_top": true
      },
      // 更多新闻...
    ],
    "total": 50,
    "page": 1,
    "pageSize": 10
  }
}
```

### 4.2 添加新闻

**功能**：添加一条新新闻

**请求方法**：`POST`

**请求路径**：`/api/admin/news`

**请求参数**：

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| title | String | 是 | 新闻标题 |
| category | String | 是 | 新闻分类（可以是头条新闻） |
| author | String | 是 | 作者 |
| source | String | 否 | 来源 |
| publish_date | String | 是 | 发布日期（ISO格式） |
| summary | String | 否 | 新闻摘要 |
| content | String | 是 | 新闻内容（HTML格式） |
| cover | String | 否 | 封面图片URL |
| status | String | 是 | 状态（draft/published） |
| is_top | Boolean | 否 | 是否置顶 |

**返回格式**：

```json
{
  "code": 200,
  "message": "添加成功",
  "data": {
    "id": 51
  }
}
```

### 4.3 编辑新闻

**功能**：编辑一条现有新闻

**请求方法**：`PUT`

**请求路径**：`/api/admin/news/:id`

**请求参数**：

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Integer | 是 | 新闻ID |
| title | String | 否 | 新闻标题 |
| category | String | 否 | 新闻分类 |
| author | String | 否 | 作者 |
| source | String | 否 | 来源 |
| publish_date | String | 否 | 发布日期（ISO格式） |
| summary | String | 否 | 新闻摘要 |
| content | String | 否 | 新闻内容（HTML格式） |
| cover | String | 否 | 封面图片URL |
| status | String | 否 | 状态（draft/published） |
| is_top | Boolean | 否 | 是否置顶 |

**返回格式**：

```json
{
  "code": 200,
  "message": "编辑成功",
  "data": null
}
```

### 4.4 删除新闻

**功能**：删除一条新闻

**请求方法**：`DELETE`

**请求路径**：`/api/admin/news/:id`

**请求参数**：

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Integer | 是 | 新闻ID |

**返回格式**：

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 4.5 批量操作

**功能**：批量发布、下架或删除新闻

**请求方法**：`POST`

**请求路径**：`/api/admin/news/batch`

**请求参数**：

```json
{
  "ids": [1, 2, 3],
  "action": "publish", // 可选值：publish, offline, delete
  "status": "published" // 当action为publish或offline时必填
}
```

**返回格式**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "successCount": 3,
    "failedCount": 0
  }
}
```

### 4.6 置顶/取消置顶新闻

**功能**：设置新闻的置顶状态

**请求方法**：`PUT`

**请求路径**：`/api/admin/news/:id/top`

**请求参数**：

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Integer | 是 | 新闻ID |
| is_top | Boolean | 是 | 是否置顶 |

**返回格式**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

### 4.7 更新新闻状态

**功能**：更新新闻的发布状态

**请求方法**：`PUT`

**请求路径**：`/api/admin/news/:id/status`

**请求参数**：

| 参数名 | 类型 | 必填 | 描述 |
|--------|------|------|------|
| id | Integer | 是 | 新闻ID |
| status | String | 是 | 新状态（published/offline） |

**返回格式**：

```json
{
  "code": 200,
  "message": "状态更新成功",
  "data": null
}
```

## 5. 数据模型

### 新闻对象 (News)

```json
{
  "id": 1,
  "title": "新闻标题",
  "category": "时政要闻",
  "author": "作者",
  "source": "来源",
  "publish_date": "2025-11-20T09:00:00Z",
  "summary": "新闻摘要",
  "content": "<p>新闻内容</p>",
  "cover": "https://example.com/cover.jpg",
  "views": 1234,
  "status": "published",
  "is_top": true,
  "created_by": 1,
  "updated_by": 1,
  "created_at": "2025-11-20T08:00:00Z",
  "updated_at": "2025-11-20T08:30:00Z"
}
```

## 6. 错误码

| 错误码 | 描述 |
|--------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 7. 注意事项

1. 头条新闻通常是指分类为"头条新闻"或被标记为"置顶"的新闻，建议优先显示置顶的头条新闻
2. 首页头条新闻板块默认显示3条新闻，可通过参数调整
3. 新闻内容支持HTML格式，便于富文本编辑
4. 图片上传功能需要额外的文件上传接口支持
5. 操作日志应记录所有对新闻的修改操作，包括发布、编辑、删除、置顶等
6. 浏览量统计需要在每次访问新闻详情时自动增加