# 前台系统后端接口汇总

## 1. 引言

本文档汇总了党建管理系统前台页面所需的所有后端接口，包括接口路径、请求方法、参数、响应格式等详细说明。所有接口遵循统一的响应格式，确保前后端交互的一致性和规范性。

## 2. 认证与用户管理接口

### 2.1 用户登录

**接口说明**：用户登录系统

**请求信息**
- URL: `/api/auth/login`
- 请求方法: POST
- 请求体:
  ```json
  {
    "username": "user123",
    "password": "password123"
  }
  ```

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "userInfo": {
        "id": 1,
        "username": "user123",
        "name": "张三",
        "avatar": "https://example.com/avatar.jpg",
        "role": "student"
      }
    }
  }
  ```

### 2.2 用户登出

**接口说明**：用户退出登录

**请求信息**
- URL: `/api/auth/logout`
- 请求方法: POST
- 请求头: 
  - Authorization: Bearer {token}

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "登出成功",
    "data": null
  }
  ```

### 2.3 用户注册

**接口说明**：用户注册新账号

**请求信息**
- URL: `/api/auth/register`
- 请求方法: POST
- 请求体:
  ```json
  {
    "username": "newuser",
    "password": "newpassword123",
    "name": "李四",
    "email": "lisi@example.com",
    "phone": "13800138000"
  }
  ```

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": {
      "id": 2,
      "username": "newuser",
      "name": "李四"
    }
  }
  ```

### 2.4 获取用户信息

**接口说明**：获取当前登录用户的详细信息

**请求信息**
- URL: `/api/user/profile`
- 请求方法: GET
- 请求头: 
  - Authorization: Bearer {token}

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "username": "user123",
      "name": "张三",
      "email": "zhangsan@example.com",
      "phone": "13800138000",
      "avatar": "https://example.com/avatar.jpg",
      "role": "student",
      "department": "计算机学院",
      "joinedAt": "2023-09-01"
    }
  }
  ```

### 2.5 更新用户信息

**接口说明**：更新当前登录用户的信息

**请求信息**
- URL: `/api/user/profile`
- 请求方法: PUT
- 请求头: 
  - Authorization: Bearer {token}
- 请求体:
  ```json
  {
    "name": "张三三",
    "email": "zhangsan3@example.com",
    "phone": "13900139000",
    "avatar": "https://example.com/new-avatar.jpg"
  }
  ```

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "username": "user123",
      "name": "张三三",
      "email": "zhangsan3@example.com",
      "phone": "13900139000",
      "avatar": "https://example.com/new-avatar.jpg"
    }
  }
  ```

## 3. 首页接口

### 3.1 获取推荐内容

**接口说明**：获取首页推荐的内容，包括新闻、学习资源和活动

**请求信息**
- URL: `/api/home/recommended`
- 请求方法: GET

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
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

### 3.2 获取头条新闻

**接口说明**：获取首页头条新闻

**请求信息**
- URL: `/api/home/headline-news`
- 请求方法: GET

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "title": "习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话",
        "date": "2025-07-01",
        "source": "新华网",
        "imageUrl": "https://example.com/news1.jpg"
      }
    ]
  }
  ```

## 4. 新闻接口

### 4.1 获取新闻列表

**接口说明**：获取新闻列表，支持分页、分类筛选和关键词搜索

**请求信息**
- URL: `/api/news`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | page | Integer | 是 | 当前页码，从1开始 |
  | pageSize | Integer | 是 | 每页显示的新闻数量 |
  | categoryId | Integer | 否 | 新闻分类ID |
  | keyword | String | 否 | 搜索关键词，支持对新闻标题和内容的模糊搜索 |
  | sortBy | String | 否 | 排序字段，可选值：`publishDate`（按发布时间）、`views`（按浏览量） |
  | order | String | 否 | 排序顺序，可选值：`asc`（升序）、`desc`（降序） |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "news": [
        {
          "id": 1,
          "title": "习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话",
          "category": "要闻",
          "publishDate": "2025-07-01T09:00:00Z",
          "source": "新华网",
          "views": 15800,
          "imageUrl": "https://example.com/news1.jpg"
        }
      ],
      "total": 100,
      "pages": 10,
      "page": 1,
      "pageSize": 10
    }
  }
  ```

### 4.2 获取新闻详情

**接口说明**：根据新闻ID获取新闻的详细信息

**请求信息**
- URL: `/api/news/{id}`
- 请求方法: GET
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 新闻ID |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "title": "习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话",
      "category": "要闻",
      "publishDate": "2025-07-01T09:00:00Z",
      "source": "新华网",
      "author": "新华社记者",
      "views": 15800,
      "imageUrl": "https://example.com/news1.jpg",
      "content": "<h2>讲话全文</h2><p>同志们，朋友们：..."",
      "relatedNews": [
        {
          "id": 2,
          "title": "各地各部门认真学习贯彻习近平总书记在庆祝中国共产党成立104周年大会上的重要讲话精神"
        }
      ]
    }
  }
  ```

### 4.3 获取新闻分类

**接口说明**：获取新闻分类列表

**请求信息**
- URL: `/api/news/categories`
- 请求方法: GET

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "要闻"
      },
      {
        "id": 2,
        "name": "党建动态"
      },
      {
        "id": 3,
        "name": "通知公告"
      }
    ]
  }
  ```

### 4.4 增加新闻浏览量

**接口说明**：增加新闻的浏览量

**请求信息**
- URL: `/api/news/{id}/view`
- 请求方法: POST
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 新闻ID |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "浏览量增加成功",
    "data": {
      "id": 1,
      "views": 15801
    }
  }
  ```

## 5. 通知公告接口

### 5.1 获取通知列表

**接口说明**：获取通知公告列表，支持分页和分类筛选

**请求信息**
- URL: `/api/notices`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | page | Integer | 是 | 当前页码，从1开始 |
  | pageSize | Integer | 是 | 每页显示的通知数量 |
  | categoryId | Integer | 否 | 通知分类ID |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "notices": [
        {
          "id": 1,
          "title": "关于开展2025年党员民主评议工作的通知",
          "category": "工作通知",
          "publishDate": "2025-06-15",
          "source": "党委组织部"
        }
      ],
      "total": 50,
      "pages": 5,
      "page": 1,
      "pageSize": 10
    }
  }
  ```

### 5.2 获取通知详情

**接口说明**：根据通知ID获取通知的详细信息

**请求信息**
- URL: `/api/notices/{id}`
- 请求方法: GET
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 通知ID |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "title": "关于开展2025年党员民主评议工作的通知",
      "category": "工作通知",
      "publishDate": "2025-06-15",
      "source": "党委组织部",
      "content": "各党支部：...",
      "attachments": [
        {
          "id": 1,
          "name": "党员民主评议表.docx",
          "url": "https://example.com/attachment1.docx"
        }
      ]
    }
  }
  ```

### 5.3 获取通知分类

**接口说明**：获取通知公告的分类列表

**请求信息**
- URL: `/api/notices/categories`
- 请求方法: GET

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "工作通知"
      },
      {
        "id": 2,
        "name": "会议通知"
      },
      {
        "id": 3,
        "name": "公示公告"
      }
    ]
  }
  ```

## 6. 学习资源接口

### 6.1 获取学习资源列表

**接口说明**：获取学习资源列表，支持分页、分类筛选和关键词搜索

**请求信息**
- URL: `/api/learning/resources`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | page | Integer | 是 | 当前页码，从1开始 |
  | pageSize | Integer | 是 | 每页显示的学习资源数量 |
  | category | String | 否 | 资源分类ID |
  | keyword | String | 否 | 搜索关键词，支持对资源标题和描述的模糊搜索 |
  | sortBy | String | 否 | 排序字段，可选值：`createTime`（按创建时间）、`views`（按浏览量） |
  | order | String | 否 | 排序顺序，可选值：`asc`（升序）、`desc`（降序） |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "resources": [
        {
          "id": 1,
          "title": "习近平新时代中国特色社会主义思想学习纲要（2025年版）",
          "type": "document",
          "category": "政治理论",
          "author": "中共中央宣传部",
          "createTime": "2025-05-01",
          "views": 2500,
          "likes": 1800,
          "comments": 200,
          "imageUrl": "https://example.com/book1.jpg"
        }
      ],
      "total": 200,
      "pages": 20,
      "page": 1,
      "pageSize": 10
    }
  }
  ```

### 6.2 获取学习资源详情

**接口说明**：根据资源ID获取学习资源的详细信息

**请求信息**
- URL: `/api/learning/resources/{id}`
- 请求方法: GET
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 学习资源ID |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "title": "习近平新时代中国特色社会主义思想学习纲要（2025年版）",
      "type": "document",
      "category": "政治理论",
      "author": "中共中央宣传部",
      "createTime": "2025-05-01",
      "views": 2500,
      "likes": 1800,
      "comments": 200,
      "imageUrl": "https://example.com/book1.jpg",
      "content": "<h2>学习纲要</h2><p>习近平新时代中国特色社会主义思想是当代中国马克思主义、二十一世纪马克思主义...",
      "fileUrl": "https://example.com/book1.pdf",
      "relatedResources": [
        {
          "id": 2,
          "title": "习近平谈治国理政（第四卷）"
        }
      ]
    }
  }
  ```

### 6.3 获取学习资源分类

**接口说明**：获取学习资源的分类列表

**请求信息**
- URL: `/api/learning/categories`
- 请求方法: GET

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": "politics",
        "name": "政治理论"
      },
      {
        "id": "party_history",
        "name": "党史学习"
      },
      {
        "id": "party_building",
        "name": "党建实务"
      },
      {
        "id": "moral_education",
        "name": "德育教育"
      }
    ]
  }
  ```

### 6.4 点赞学习资源

**接口说明**：点赞或取消点赞学习资源

**请求信息**
- URL: `/api/learning/resources/{id}/like`
- 请求方法: POST
- 请求头: 
  - Authorization: Bearer {token}
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 学习资源ID |
- 请求体:
  ```json
  {
    "action": "like" // 可选值: "like" (点赞), "unlike" (取消点赞)
  }
  ```

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": {
      "id": 1,
      "likes": 1801,
      "action": "like"
    }
  }
  ```

### 6.5 评论学习资源

**接口说明**：对学习资源进行评论

**请求信息**
- URL: `/api/learning/resources/{id}/comment`
- 请求方法: POST
- 请求头: 
  - Authorization: Bearer {token}
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 学习资源ID |
- 请求体:
  ```json
  {
    "content": "这是一本非常好的学习资料，值得推荐！"
  }
  ```

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "评论成功",
    "data": {
      "id": 101,
      "content": "这是一本非常好的学习资料，值得推荐！",
      "userId": 1,
      "userName": "张三",
      "createTime": "2025-11-10T14:30:00Z"
    }
  }
  ```

### 6.6 分享学习资源

**接口说明**：分享学习资源

**请求信息**
- URL: `/api/learning/resources/{id}/share`
- 请求方法: POST
- 请求头: 
  - Authorization: Bearer {token}
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 学习资源ID |
- 请求体:
  ```json
  {
    "platform": "wechat" // 可选值: "wechat", "weibo", "qq"
  }
  ```

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "分享成功",
    "data": {
      "id": 1,
      "shares": 501,
      "shareUrl": "https://example.com/share/learning/1"
    }
  }
  ```

## 7. 主题活动接口

### 7.1 获取活动分类列表

**接口说明**：获取主题活动的分类列表

**请求信息**
- URL: `/api/activities/categories`
- 请求方法: GET

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
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

### 7.2 获取活动列表

**接口说明**：获取主题活动列表，支持分页、分类筛选和关键词搜索

**请求信息**
- URL: `/api/activities`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | page | Integer | 是 | 当前页码，从1开始 |
  | pageSize | Integer | 是 | 每页显示的活动数量 |
  | category | String | 否 | 活动分类ID |
  | keyword | String | 否 | 搜索关键词，支持对活动标题和描述的模糊搜索 |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
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
          "description": "总结计算机学院习近平新时代中国特色社会主义思想主题教育开展情况...",
          "canRegister": true,
          "participants": 45,
          "views": 280,
          "image": "https://example.com/activity1.jpg"
        }
      ],
      "total": 10,
      "pages": 3,
      "page": 1,
      "pageSize": 4
    }
  }
  ```

### 7.3 获取活动详情

**接口说明**：根据活动ID获取活动的详细信息

**请求信息**
- URL: `/api/activities/{id}`
- 请求方法: GET
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 活动ID |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
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
      "description": "总结计算机学院习近平新时代中国特色社会主义思想主题教育开展情况...",
      "content": "<h2>活动详情</h2><p>一、活动背景...</p>",
      "canRegister": true,
      "registerDeadline": "2025-11-18",
      "participants": 45,
      "capacity": 100,
      "views": 280,
      "image": "https://example.com/activity1.jpg",
      "attachments": [
        {
          "name": "活动议程.pdf",
          "url": "/api/attachments/1"
        }
      ]
    }
  }
  ```

### 7.4 活动报名

**接口说明**：用户报名参加某个活动

**请求信息**
- URL: `/api/activities/{id}/register`
- 请求方法: POST
- 请求头: 
  - Authorization: Bearer {token}
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 活动ID |
- 请求体:
  ```json
  {
    "userId": 123, // 当前登录用户ID，可从Token中获取
    "name": "张三",
    "phone": "13800138000"
  }
  ```

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
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

## 8. 样板支部接口

### 8.1 获取样板支部列表

**接口说明**：获取样板支部列表，用于首页和详情页展示

**请求信息**
- URL: `/api/branches/model`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | limit | Integer | 否 | 每页显示的样板支部数量，默认6条 |
  | sortBy | String | 否 | 排序字段，可选值：`createdAt`（按创建时间）、`name`（按名称），默认`createdAt` |
  | order | String | 否 | 排序顺序，可选值：`asc`（升序）、`desc`（降序），默认`desc` |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "计算机学院学生第一党支部",
        "description": "全国样板党支部，连续三年被评为先进党支部",
        "image": "https://example.com/branch1.jpg",
        "level": "national",
        "createdAt": "2025-01-15T08:30:00Z"
      }
    ]
  }
  ```

### 8.2 获取样板支部详情

**接口说明**：根据ID获取样板支部的详细信息

**请求信息**
- URL: `/api/branches/model/{id}`
- 请求方法: GET
- 路径参数:
  | 参数名 | 类型 | 必填 | 描述 |
  |--------|------|------|------|
  | id | Integer | 是 | 样板支部ID |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "name": "计算机学院学生第一党支部",
      "description": "全国样板党支部，连续三年被评为先进党支部",
      "image": "https://example.com/branch1.jpg",
      "level": "national",
      "introduction": "计算机学院学生第一党支部成立于2018年，现有党员35名...",
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

## 9. 就业服务接口

### 9.1 获取就业统计数据

**接口说明**：获取就业相关统计数据

**请求信息**
- URL: `/api/employment/stats`
- 请求方法: GET

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "employmentRate": 95.2,           // 本科生就业率
      "employmentGraduateCount": 92.8,  // 研究生就业率
      "companyCount": 1200,             // 合作企业数量
      "recruitmentCount": 5600          // 招聘岗位数量
    }
  }
  ```

### 9.2 获取就业资讯列表

**接口说明**：获取就业资讯列表，支持分页、分类筛选和排序

**请求信息**
- URL: `/api/employment/list`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | page | int | 否 | 页码，默认1 |
  | pageSize | int | 否 | 每页数量，默认10 |
  | category | string | 否 | 分类（如news, analysis） |
  | sort | string | 否 | 排序方式（如latest, popular） |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "title": "2025年全国高校毕业生就业形势分析",
        "publishDate": "2025-11-20",
        "source": "教育部"
      }
    ],
    "total": 100 // 总数量
  }
  ```

### 9.3 获取就业资讯详情

**接口说明**：根据ID获取就业资讯的详细内容

**请求信息**
- URL: `/api/employment/detail/{id}`
- 请求方法: GET
- 路径参数:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | id | int | 是 | 就业资讯ID |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "title": "2025年全国高校毕业生就业形势分析",
      "publishDate": "2025-11-20",
      "source": "教育部",
      "viewCount": 5892,
      "content": "<p>2025年全国高校毕业生预计将达到1200万人，就业形势总体平稳但面临一定压力。</p>"
    }
  }
  ```

### 9.4 获取企业招聘信息

**接口说明**：获取企业招聘信息，支持分页、行业筛选和热门企业筛选

**请求信息**
- URL: `/api/employment/recruitment`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | page | int | 否 | 页码，默认1 |
  | pageSize | int | 否 | 每页数量，默认10 |
  | industry | string | 否 | 行业筛选 |
  | isHot | boolean | 否 | 是否热门企业 |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "companyName": "字节跳动",
        "industry": "互联网",
        "jobTitle": "前端开发工程师",
        "salary": "20-40K",
        "location": "北京"
      }
    ],
    "total": 500 // 总数量
  }
  ```

### 9.5 获取就业指导资源

**接口说明**：获取就业指导资源列表，支持分页

**请求信息**
- URL: `/api/employment/guidance`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | page | int | 否 | 页码，默认1 |
  | pageSize | int | 否 | 每页数量，默认10 |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "title": "简历制作指南",
        "type": "文档"
      },
      {
        "id": 2,
        "title": "面试技巧分享",
        "type": "视频"
      }
    ],
    "total": 30 // 总数量
  }
  ```

### 9.6 获取老师指导学生就业情况

**接口说明**：获取老师指导学生就业情况列表，支持按学生类型筛选

**请求信息**
- URL: `/api/employment/graduate`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | type | string | 是 | 学生类型（undergraduate: 本科生, graduate: 研究生） |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "name": "张老师",
        "studentCount": 45,
        "rankChange": 2
      }
    ]
  }
  ```

### 9.7 获取就业政策

**接口说明**：获取就业政策列表，支持分页

**请求信息**
- URL: `/api/employment/policies`
- 请求方法: GET
- 请求参数:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | page | int | 否 | 页码，默认1 |
  | pageSize | int | 否 | 每页数量，默认10 |

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "title": "关于进一步做好高校毕业生就业创业工作的通知"
      },
      {
        "id": 2,
        "title": "大学生就业创业扶持政策"
      }
    ],
    "total": 20 // 总数量
  }
  ```

### 9.8 获取就业服务

**接口说明**：获取就业服务列表

**请求信息**
- URL: `/api/employment/services`
- 请求方法: GET

**响应信息**
- 响应格式: JSON
- 响应状态码: 200 OK
- 响应示例:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "icon": "📅",
        "title": "校园招聘会"
      },
      {
        "id": 2,
        "icon": "💼",
        "title": "就业咨询"
      }
    ]
  }
  ```

## 10. 通用响应格式说明

### 10.1 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { /* 响应数据 */ }
}
```

### 10.2 错误响应

```json
{
  "code": 错误码,
  "message": "错误信息",
  "data": null
}
```

## 11. 常见错误码

| 错误码 | 描述 |
|-------|------|
| 400 | 请求参数错误 |
| 401 | 未登录或登录过期 |
| 403 | 没有权限访问该资源 |
| 404 | 资源不存在 |
| 409 | 已报名参加该活动 |
| 410 | 活动报名已截止 |
| 411 | 活动人数已满 |
| 500 | 服务器内部错误 |

## 12. 附录

### 12.1 API版本管理

- 当前版本: v1
- 版本管理: 通过URL路径中的版本号进行管理，如 `/api/v1/news`

### 12.2 数据格式说明

- 日期时间格式: ISO 8601，如 `2025-11-10T10:30:00Z`
- 文件大小单位: 字节 (Byte)
- 图片尺寸: 根据不同场景使用不同尺寸，建议后端提供多种尺寸的图片URL

### 12.3 安全要求

- 所有需要用户认证的接口必须使用Bearer Token进行身份验证
- 敏感数据必须进行加密传输
- 接口必须进行输入验证和输出过滤，防止注入攻击
- 对高频请求进行限流处理，防止恶意攻击