# 已实现接口汇总与前端对接规范

## 一、首页相关接口

### 1. 获取首页推荐内容
- **接口路径**：`GET /api/home/recommended`
- **请求参数**：无
- **返回格式**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "articles": [
      {
        "id": 1,
        "title": "文章标题",
        "imageUrl": "https://example.com/article1.jpg",
        "publishDate": "2025年12月11日"
      }
    ],
    "learningResources": [
      {
        "id": 1,
        "title": "学习资源标题",
        "imageUrl": "https://example.com/resource1.jpg",
        "type": "视频"
      }
    ],
    "activities": [
      {
        "id": 1,
        "title": "活动标题",
        "imageUrl": "https://example.com/activity1.jpg",
        "startDate": "2025年12月11日"
      }
    ]
  }
}
```
- **说明**：返回首页推荐的文章、学习资源和活动列表。

### 2. 获取头条新闻
- **接口路径**：`GET /api/home/headline-news`
- **请求参数**：无
- **返回格式**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 1,
      "title": "新闻标题1",
      "publishDate": "2025年12月11日",
      "imageUrl": "https://example.com/news1.jpg"
    },
    {
      "id": 2,
      "title": "新闻标题2",
      "publishDate": "2025年12月10日",
      "imageUrl": "https://example.com/news2.jpg"
    },
    {
      "id": 3,
      "title": "新闻标题3",
      "publishDate": "2025年12月09日",
      "imageUrl": "https://example.com/news3.jpg"
    }
  ]
}
```
- **说明**：返回最新的3条头条新闻。

## 二、新闻相关接口

### 1. 获取新闻列表
- **接口路径**：`GET /api/news`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 默认值 | 说明 |
  |--------|------|------|--------|------|
  | page | Integer | 否 | 1 | 页码 |
  | pageSize | Integer | 否 | 10 | 每页大小 |
  | category | String | 否 | 无 | 新闻分类，为空则获取所有分类 |
  | keyword | String | 否 | 无 | 搜索关键词，用于标题搜索 |
  | sortBy | String | 否 | publishDate | 排序字段 |
  | order | String | 否 | desc | 排序方式（asc/desc） |
- **返回格式**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "news": [
      {
        "id": 1,
        "title": "新闻标题",
        "category": "时政",
        "publishDate": "2025年12月11日",
        "source": "新华社",
        "imageUrl": "https://example.com/news1.jpg",
        "views": 100
      }
    ],
    "total": 100,
    "pages": 10,
    "page": 1,
    "pageSize": 10
  }
}
```
- **说明**：按条件获取新闻列表，支持分页和排序。

### 2. 获取新闻详情
- **接口路径**：`GET /api/news/{id}`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | id | Integer | 是 | 新闻ID |
- **返回格式**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "title": "新闻标题",
    "category": "时政",
    "publishDate": "2025年12月11日",
    "source": "新华社",
    "views": 100,
    "imageUrl": "https://example.com/news1.jpg",
    "content": "新闻内容...",
    "relatedNews": [
      {
        "id": 2,
        "title": "相关新闻标题",
        "publishDate": "2025年12月10日"
      }
    ]
  }
}
```
- **说明**：获取指定ID的新闻详情，包含相关新闻。

### 3. 获取新闻分类
- **接口路径**：`GET /api/news/categories`
- **请求参数**：无
- **返回格式**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": 835081,
      "name": "时政"
    },
    {
      "id": 891526,
      "name": "法治"
    },
    {
      "id": 1034335,
      "name": "经济"
    }
  ]
}
```
- **说明**：获取所有新闻分类。

### 4. 更新新闻浏览量
- **接口路径**：`POST /api/news/{id}/view`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | id | Integer | 是 | 新闻ID |
- **返回格式**：
```json
{
  "code": 200,
  "message": "浏览量增加成功",
  "data": {
    "id": 1,
    "views": 101
  }
}
```
- **说明**：更新指定新闻的浏览量（+1）。

## 三、前端对接规范

### 1. 请求格式规范
- **请求方式**：根据接口定义使用GET/POST请求
- **请求头**：
  - Content-Type: application/json
  - 如需认证，添加Authorization: Bearer {token}头
- **请求参数**：
  - GET请求：参数放在URL查询字符串中
  - POST请求：参数放在请求体中，使用JSON格式

### 2. 响应格式规范
- **统一响应结构**：所有接口返回统一的JSON格式
```json
{
  "code": 状态码,
  "message": "提示信息",
  "data": "返回数据"
}
```
- **状态码说明**：
  - 200：请求成功
  - 400：请求参数错误
  - 401：未授权
  - 403：禁止访问
  - 404：资源不存在
  - 500：服务器内部错误

### 3. 错误处理规范
- 前端需根据返回的code和message进行错误处理
- 对于401状态码，需跳转到登录页面
- 对于其他错误状态码，需在页面上显示友好的错误提示
- 网络错误时，需提示用户检查网络连接

### 4. 分页组件规范
- 页码从1开始
- 每页大小可配置，默认10条
- 分页组件需显示当前页码、总页数、总记录数
- 提供上一页、下一页、首页、末页按钮

### 5. 数据展示规范
- 日期时间格式：统一使用"yyyy年MM月dd日"格式
- 图片展示：使用懒加载优化性能
- 文本内容：超过指定长度时显示省略号
- 分类展示：使用统一的分类标签样式

### 6. 性能优化建议
- 使用缓存减少重复请求
- 图片使用CDN加速
- 分页加载数据，避免一次性加载过多数据
- 使用防抖和节流优化搜索和滚动事件

## 四、测试说明

所有接口均已通过测试，可以正常使用。测试环境：
- 服务器地址：http://localhost:8080
- 数据库：MySQL
- 测试工具：PowerShell Invoke-RestMethod

如需进一步测试或有其他问题，请联系后端开发人员。