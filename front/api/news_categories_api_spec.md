# `/news/categories` 接口对接规范

## 1. 接口描述
该接口用于获取系统中所有新闻分类，主要应用于新闻列表页的分类筛选和新闻发布页的分类选择。

## 2. 接口信息
- **URL**: `/news/categories`
- **请求方法**: GET
- **请求协议**: HTTP/HTTPS
- **接口类型**: 数据查询接口

## 3. 请求参数
无请求参数

## 4. 响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    "头条新闻",
    "学校动态",
    "通知公告",
    "党建工作",
    "思政教育"
  ]
}
```

### 失败响应
```json
{
  "code": 500,
  "message": "服务器内部错误"
}
```

## 5. 数据字段说明

| 字段名 | 类型 | 描述 |
|-------|------|------|
| code | Integer | 响应状态码，200表示成功 |
| message | String | 响应消息 |
| data | Array<String> | 新闻分类列表，每个元素为分类名称 |

## 6. 业务逻辑

1. **分类来源**：从系统中的所有新闻数据中提取不重复的分类名称
2. **返回顺序**：默认按分类名称的自然顺序排列
3. **更新机制**：当有新分类的新闻发布时，接口会自动包含新分类

## 7. 错误处理

| 错误码 | 错误信息 | 处理方式 |
|-------|---------|---------|
| 500 | 服务器内部错误 | 记录错误日志，必要时联系后端开发人员 |

## 8. 前端调用示例

### 直接调用示例
```javascript
// 导入axios和配置
import axios from 'axios'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000
})

// 调用接口
service.get('/news/categories')
  .then(response => {
    console.log('新闻分类:', response.data)
  })
  .catch(error => {
    console.error('获取新闻分类失败:', error)
  })
```

### 使用API模块调用示例
```javascript
// 导入newsApi模块
import newsApi from '@/api/news.js'

// 在组件中调用
async function fetchNewsCategories() {
  try {
    const response = await newsApi.getNewsCategories()
    console.log('新闻分类:', response.data)
  } catch (error) {
    console.error('获取新闻分类失败:', error)
  }
}
```

## 9. 后端实现要点

```javascript
// 从新闻数据中提取所有不重复的分类
const categories = [...new Set(mockNews.map(news => news.category))];

res.json({
  code: 200,
  message: '获取成功',
  data: categories
});
```

## 10. 注意事项

1. 接口调用应在新闻列表加载前进行，以便展示完整的分类筛选
2. 当返回数据为空数组时，前端应提供友好提示
3. 分类名称应保持与后端数据的一致性，避免硬编码
4. 接口调用时应添加适当的错误处理和加载状态

## 11. 前端集成建议

1. **缓存策略**：考虑将分类数据缓存到本地，减少重复请求
2. **动态更新**：当有新闻发布或分类变更时，可选择重新调用接口更新分类列表
3. **用户体验**：提供"全部"选项，允许用户查看所有分类的新闻
4. **样式设计**：分类列表可采用横向滚动或下拉选择的方式展示，根据页面布局选择合适的展示形式

## 12. 版本变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| 1.0 | 2025-11-20 | 初始接口规范 | 系统开发团队 |