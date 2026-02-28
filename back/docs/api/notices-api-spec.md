# /api/notices 公告通知接口对接规范

## 1. 接口概述
- **接口URL**: `/api/notices`
- **请求方式**: `GET`
- **接口描述**: 获取公告通知列表，支持分页、分类筛选和关键词搜索

## 2. 请求参数

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
|-------|------|------|-------|------|
| categoryId | Integer | 否 | null | 公告分类ID，用于筛选特定分类的公告<br>1: 工作通知<br>2: 学习通知<br>3: 会议通知<br>4: 放假通知<br>5: 活动通知<br>6: 其他通知 |
| keyword | String | 否 | null | 搜索关键词，用于模糊匹配公告标题和内容 |
| page | Integer | 否 | 1 | 页码，从1开始 |
| pageSize | Integer | 否 | 10 | 每页条数，默认10条 |

## 3. 响应格式

### 3.1 成功响应
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 100,
    "page": 1,
    "pageSize": 10,
    "pages": 10,
    "notices": [
      {
        "id": 1,
        "title": "关于召开2025年度组织生活会的通知",
        "categoryId": 3,
        "publishDate": "2025-12-01",
        "source": "计算机学院教师第三党支部"
      },
      {
        "id": 2,
        "title": "关于2026年元旦放假安排的通知",
        "categoryId": 4,
        "publishDate": "2025-12-11",
        "source": "计算机学院教师第三党支部"
      }
    ]
  }
}
```

### 3.2 失败响应
```json
{
  "code": 500,
  "message": "服务器内部错误",
  "data": null
}
```

## 4. 响应字段说明

### 4.1 外层响应字段
| 字段名 | 类型 | 描述 |
|-------|------|------|
| code | Integer | 响应状态码，200表示成功 |
| message | String | 响应消息 |
| data | Object | 响应数据，成功时包含分页信息和公告列表 |

### 4.2 分页信息字段
| 字段名 | 类型 | 描述 |
|-------|------|------|
| total | Integer | 总记录数 |
| page | Integer | 当前页码 |
| pageSize | Integer | 每页条数 |
| pages | Integer | 总页数 |
| notices | Array | 公告列表 |

### 4.3 公告信息字段
| 字段名 | 类型 | 描述 |
|-------|------|------|
| id | Integer | 公告ID |
| title | String | 公告标题 |
| categoryId | Integer | 公告分类ID |
| publishDate | String | 发布日期，格式为YYYY-MM-DD |
| source | String | 发布来源 |

## 5. 分类ID与名称对应关系

| 分类ID | 分类名称 |
|-------|---------|
| 1 | 工作通知 |
| 2 | 学习通知 |
| 3 | 会议通知 |
| 4 | 放假通知 |
| 5 | 活动通知 |
| 6 | 其他通知 |

## 6. 请求示例

### 6.1 获取全部公告
```http
GET /api/notices HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

### 6.2 按分类筛选公告
```http
GET /api/notices?categoryId=3 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

### 6.3 关键词搜索公告
```http
GET /api/notices?keyword=会议 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

### 6.4 组合筛选和分页
```http
GET /api/notices?categoryId=1&keyword=通知&page=2&pageSize=20 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

## 7. 响应示例

### 7.1 成功响应示例
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 5,
    "page": 1,
    "pageSize": 10,
    "pages": 1,
    "notices": [
      {
        "id": 69,
        "title": "关于召开2025年度组织生活会和民主评议党员的通知",
        "categoryId": 3,
        "publishDate": "2025-12-01",
        "source": "计算机学院教师第三党支部"
      },
      {
        "id": 70,
        "title": "关于举办“研究生导师师德师风建设”专题培训的通知",
        "categoryId": 2,
        "publishDate": "2025-11-25",
        "source": "计算机学院教师第三党支部"
      },
      {
        "id": 72,
        "title": "关于2026年元旦放假安排的通知",
        "categoryId": 4,
        "publishDate": "2025-12-11",
        "source": "计算机学院教师第三党支部"
      },
      {
        "id": 73,
        "title": "关于组织申报2026年度陕西省教育厅科研计划项目的通知",
        "categoryId": 1,
        "publishDate": "2025-11-15",
        "source": "计算机学院教师第三党支部"
      },
      {
        "id": 74,
        "title": "关于开展支部共建活动的通知",
        "categoryId": 5,
        "publishDate": "2025-12-05",
        "source": "计算机学院教师第三党支部"
      }
    ]
  }
}
```

### 7.2 按分类筛选响应示例
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 1,
    "page": 1,
    "pageSize": 10,
    "pages": 1,
    "notices": [
      {
        "id": 69,
        "title": "关于召开2025年度组织生活会和民主评议党员的通知",
        "categoryId": 3,
        "publishDate": "2025-12-01",
        "source": "计算机学院教师第三党支部"
      }
    ]
  }
}
```

## 8. 错误码说明

| 错误码 | 描述 |
|-------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权，请先登录 |
| 500 | 服务器内部错误 |

## 9. 注意事项
1. 当categoryId参数值不在1-6范围内时，将返回空列表
2. 当数据库中存在未配置映射的分类名称时，categoryId字段将返回null
3. 关键词搜索支持模糊匹配标题和内容，使用LIKE查询
4. 接口默认按发布日期降序排列公告