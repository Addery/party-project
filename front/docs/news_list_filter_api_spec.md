# 新闻界面通过列表筛选内容的后端接口规范

## 1. 接口描述
该接口用于获取新闻列表，支持按分类和关键词进行筛选，同时支持分页和排序功能。

## 2. 接口信息
- **接口URL**：`/api/news`
- **请求方法**：`GET`
- **响应数据格式**：`JSON`
- **认证方式**：无（公开接口）

## 3. 请求参数

| 参数名 | 类型 | 必填 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- |
| page | number | 否 | 1 | 页码，从1开始 |
| pageSize | number | 否 | 10 | 每页显示数量 |
| category | string | 否 | 无 | 新闻分类筛选，如"头条新闻"、"学校动态"等 |
| keyword | string | 否 | 无 | 关键词搜索，模糊匹配新闻标题 |
| sortBy | string | 否 | publishDate | 排序字段，支持：publishDate（发布日期）、views（浏览量） |
| order | string | 否 | desc | 排序方式：asc（升序）、desc（降序） |

## 4. 成功响应格式

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "newsList": [
      {
        "id": 1,
        "title": "习近平主持召开中央全面深化改革委员会第五次会议",
        "category": "头条新闻",
        "author": "新华社",
        "source": "新华网",
        "publishDate": "2025-11-20T09:00:00Z",
        "summary": "习近平强调，要深入贯彻落实党的二十大精神，坚定不移全面深化改革...",
        "content": "<p>新华社北京11月20日电 中共中央总书记、国家主席...",
        "cover": "",
        "views": 12345,
        "status": "published",
        "isTop": true,
        "createdAt": "2025-11-20T08:00:00Z",
        "updatedAt": "2025-11-20T08:30:00Z"
      },
      // 更多新闻...
    ],
    "total": 100,
    "page": 1,
    "pageSize": 10
  }
}
```

## 5. 失败响应格式

### 5.1 参数错误
```json
{
  "code": 400,
  "message": "请求参数错误",
  "data": null
}
```

### 5.2 服务器错误
```json
{
  "code": 500,
  "message": "服务器内部错误",
  "data": null
}
```

## 6. 数据字段说明

### 6.1 响应数据字段

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | number | 响应状态码，200表示成功 |
| message | string | 响应消息 |
| data | object | 响应数据主体 |
| data.newsList | array | 新闻列表数组 |
| data.total | number | 总新闻数量 |
| data.page | number | 当前页码 |
| data.pageSize | number | 每页显示数量 |

### 6.2 新闻列表项字段

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| id | number | 新闻ID |
| title | string | 新闻标题 |
| category | string | 新闻分类 |
| author | string | 作者 |
| source | string | 来源 |
| publishDate | string | 发布日期（ISO格式） |
| summary | string | 新闻摘要 |
| content | string | 新闻内容（HTML格式） |
| cover | string | 封面图片URL |
| views | number | 浏览量 |
| status | string | 状态（published：已发布，draft：草稿） |
| isTop | boolean | 是否置顶 |
| createdAt | string | 创建时间（ISO格式） |
| updatedAt | string | 更新时间（ISO格式） |

## 7. 业务逻辑说明

1. **数据筛选**：
   - 仅返回状态为"published"的新闻
   - 如果提供了category参数，则筛选出对应分类的新闻
   - 如果提供了keyword参数，则模糊匹配新闻标题

2. **排序逻辑**：
   - 默认按发布日期（publishDate）降序排序
   - 支持按publishDate或views字段排序
   - 支持升序（asc）或降序（desc）

3. **分页逻辑**：
   - 默认每页10条记录
   - 页码从1开始
   - 返回总记录数、当前页码和每页大小

## 8. 错误处理

1. **参数错误**：
   - 当page或pageSize不是有效的数字时，使用默认值
   - 当sortBy不是支持的字段时，使用默认值publishDate
   - 当order不是asc或desc时，使用默认值desc

2. **服务器错误**：
   - 当服务器内部出错时，返回500状态码和错误消息

## 9. 调用示例

### 9.1 获取全部新闻（第一页，每页10条）

**请求URL**：`/api/news`

**响应**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "newsList": [/* 新闻列表 */],
    "total": 100,
    "page": 1,
    "pageSize": 10
  }
}
```

### 9.2 按分类筛选（党建工作）

**请求URL**：`/api/news?category=党建工作`

**响应**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "newsList": [/* 党建工作分类的新闻列表 */],
    "total": 20,
    "page": 1,
    "pageSize": 10
  }
}
```

### 9.3 按关键词搜索

**请求URL**：`/api/news?keyword=改革`

**响应**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "newsList": [/* 标题包含"改革"的新闻列表 */],
    "total": 5,
    "page": 1,
    "pageSize": 10
  }
}
```

### 9.4 组合筛选（党建工作分类，关键词"会议"，按浏览量排序）

**请求URL**：`/api/news?category=党建工作&keyword=会议&sortBy=views&order=desc`

**响应**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "newsList": [/* 符合条件的新闻列表，按浏览量降序 */],
    "total": 3,
    "page": 1,
    "pageSize": 10
  }
}
```

## 10. 前端调用示例

```javascript
// 前端Vue组件中调用示例
const fetchNewsList = async () => {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value
    }
    
    // 如果有分类筛选
    if (selectedCategory.value) {
      params.category = selectedCategory.value
    }
    
    // 如果有关键词搜索
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    
    const response = await newsApi.getNewsList(params)
    
    if (response.code === 200) {
      newsList.value = response.data.newsList
      total.value = response.data.total
      // 更新分页信息等
    }
  } catch (error) {
    console.error('获取新闻列表失败:', error)
  }
}
```

## 11. 后端实现要点

1. **数据来源**：
   - 从数据库或缓存中获取新闻数据
   - 确保只返回已发布的新闻

2. **性能优化**：
   - 对分类和关键词查询添加索引
   - 使用分页查询，避免一次性返回大量数据
   - 考虑使用缓存机制提高响应速度

3. **扩展性**：
   - 支持添加更多筛选条件（如发布时间范围）
   - 支持更多排序字段
   - 支持自定义每页显示数量范围

## 12. 注意事项

1. **安全性**：
   - 对输入参数进行验证，防止SQL注入
   - 限制每页显示数量的最大值，防止恶意请求

2. **兼容性**：
   - 确保接口返回格式与前端期望一致
   - 考虑版本兼容问题

3. **文档更新**：
   - 当接口参数或返回格式发生变化时，及时更新文档

## 13. 版本历史

| 版本 | 日期 | 变更内容 |
| :--- | :--- | :--- |
| 1.0 | 2025-11-20 | 初始版本，支持分类、关键词筛选和分页排序 |