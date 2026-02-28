# 就业服务后端接口设计

## 1. 概述

本文档基于就业服务前端界面功能需求，分析并列出后端需要提供的API接口，包括接口路径、请求方法、参数、响应格式等详细说明。

## 2. 接口列表

| 接口名称 | 接口路径 | 请求方法 | 功能描述 |
|---------|---------|---------|---------|
| 获取就业统计数据 | `/api/employment/stats` | GET | 获取就业相关统计数据 |
| 获取就业资讯列表 | `/api/employment/list` | GET | 获取就业资讯列表 |
| 获取就业资讯详情 | `/api/employment/detail/{id}` | GET | 获取就业资讯详情 |
| 获取企业招聘信息 | `/api/employment/recruitment` | GET | 获取企业招聘信息 |
| 获取就业指导资源 | `/api/employment/guidance` | GET | 获取就业指导资源 |
| 获取老师指导学生就业情况 | `/api/employment/graduate` | GET | 获取老师指导学生就业情况 |
| 获取就业政策 | `/api/employment/policies` | GET | 获取就业政策列表 |
| 获取就业服务 | `/api/employment/services` | GET | 获取就业服务列表 |
| 获取热门企业 | `/api/employment/recruitment` | GET | 获取热门企业列表（通过参数筛选） |

## 3. 接口详细说明

### 3.1 获取就业统计数据

- **接口路径**: `/api/employment/stats`
- **请求方法**: GET
- **请求参数**: 无
- **响应数据格式**:
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
- **功能描述**: 获取就业相关统计数据，包括本科生就业率、研究生就业率、合作企业数量和招聘岗位数量。

### 3.2 获取就业资讯列表

- **接口路径**: `/api/employment/list`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | page | int | 否 | 页码，默认1 |
  | pageSize | int | 否 | 每页数量，默认10 |
  | category | string | 否 | 分类（如news, analysis） |
  | sort | string | 否 | 排序方式（如latest, popular） |
- **响应数据格式**:
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
      },
      // 更多就业资讯...
    ],
    "total": 100 // 总数量
  }
  ```
- **功能描述**: 获取就业资讯列表，支持分页、分类筛选和排序。

### 3.3 获取就业资讯详情

- **接口路径**: `/api/employment/detail/{id}`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | id | int | 是 | 就业资讯ID |
- **响应数据格式**:
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
- **功能描述**: 根据ID获取就业资讯的详细内容。

### 3.4 获取企业招聘信息

- **接口路径**: `/api/employment/recruitment`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | page | int | 否 | 页码，默认1 |
  | pageSize | int | 否 | 每页数量，默认10 |
  | industry | string | 否 | 行业筛选 |
  | isHot | boolean | 否 | 是否热门企业 |
- **响应数据格式**:
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
      },
      // 更多招聘信息...
    ],
    "total": 500 // 总数量
  }
  ```
- **功能描述**: 获取企业招聘信息，支持分页、行业筛选和热门企业筛选。

### 3.5 获取就业指导资源

- **接口路径**: `/api/employment/guidance`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | page | int | 否 | 页码，默认1 |
  | pageSize | int | 否 | 每页数量，默认10 |
- **响应数据格式**:
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
      },
      // 更多指导资源...
    ],
    "total": 30 // 总数量
  }
  ```
- **功能描述**: 获取就业指导资源列表，支持分页。

### 3.6 获取老师指导学生就业情况

- **接口路径**: `/api/employment/graduate`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | type | string | 是 | 学生类型（undergraduate: 本科生, graduate: 研究生） |
- **响应数据格式**:
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
      },
      {
        "id": 2,
        "name": "李老师",
        "studentCount": 42,
        "rankChange": -1
      },
      // 更多老师指导情况...
    ]
  }
  ```
- **功能描述**: 获取老师指导学生就业情况列表，支持按学生类型筛选。

### 3.7 获取就业政策

- **接口路径**: `/api/employment/policies`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |-------|------|------|------|
  | page | int | 否 | 页码，默认1 |
  | pageSize | int | 否 | 每页数量，默认10 |
- **响应数据格式**:
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
      },
      // 更多政策...
    ],
    "total": 20 // 总数量
  }
  ```
- **功能描述**: 获取就业政策列表，支持分页。

### 3.8 获取就业服务

- **接口路径**: `/api/employment/services`
- **请求方法**: GET
- **请求参数**: 无
- **响应数据格式**:
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
      },
      // 更多服务...
    ]
  }
  ```
- **功能描述**: 获取就业服务列表。

## 4. 数据结构设计建议

### 4.1 就业资讯表 (employment_info)

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| id | INT | 主键ID |
| title | VARCHAR(255) | 资讯标题 |
| content | TEXT | 资讯内容 |
| publish_date | DATE | 发布日期 |
| source | VARCHAR(100) | 资讯来源 |
| view_count | INT | 浏览次数 |
| category | VARCHAR(50) | 资讯分类 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

### 4.2 企业招聘表 (company_recruitment)

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| id | INT | 主键ID |
| company_name | VARCHAR(255) | 企业名称 |
| industry | VARCHAR(100) | 行业 |
| job_title | VARCHAR(255) | 职位名称 |
| salary | VARCHAR(50) | 薪资范围 |
| location | VARCHAR(100) | 工作地点 |
| is_hot | BOOLEAN | 是否热门企业 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

### 4.3 就业统计表 (employment_stats)

| 字段名 | 数据类型 | 描述 |
|-------|---------|------|
| id | INT | 主键ID |
| employment_rate | DECIMAL(5,2) | 本科生就业率 |
| employment_graduate_count | DECIMAL(5,2) | 研究生就业率 |
| company_count | INT | 合作企业数量 |
| recruitment_count | INT | 招聘岗位数量 |
| year | INT | 统计年份 |
| semester | INT | 统计学期 |
| updated_at | DATETIME | 更新时间 |

## 5. 接口使用场景

| 页面组件 | 使用接口 | 功能描述 |
|---------|---------|---------|
| 就业统计数据模块 | `/api/employment/stats` | 展示就业相关统计数据 |
| 指导学生就业排名 | `/api/employment/graduate` | 展示老师指导学生就业情况排名 |
| 就业资讯列表 | `/api/employment/list` | 展示就业资讯列表 |
| 就业资讯详情 | `/api/employment/detail/{id}` | 展示就业资讯详情内容 |
| 企业招聘信息 | `/api/employment/recruitment` | 展示企业招聘信息 |
| 就业指导资源 | `/api/employment/guidance` | 展示就业指导资源 |
| 就业政策列表 | `/api/employment/policies` | 展示就业政策列表 |
| 就业服务列表 | `/api/employment/services` | 展示就业服务列表 |
| 热门企业列表 | `/api/employment/recruitment?ishot=true` | 展示热门企业列表 |

## 6. 注意事项

1. 所有接口应统一返回格式，包含code、message和data字段
2. 接口应支持适当的分页和筛选功能，以提高性能和用户体验
3. 敏感数据应进行适当的权限控制和加密处理
4. 接口应具备良好的错误处理机制，返回清晰的错误信息
5. 考虑到前端页面展示的需要，部分接口数据可能需要提前进行格式化处理