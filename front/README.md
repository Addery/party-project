# 党员教育与管理系统 - 前台界面

## 项目说明

这是党员教育与管理系统的前台界面部分，基于Vue 3 + Vite开发，提供了新闻动态、学习资源、通知公告、主题活动等功能模块。

## 文档位置

所有项目文档已整理到 `docs/` 目录中，包括：
- 项目主说明文档：`docs/README.md`
- API接口规范：`docs/*.md`
- 需求文档：`docs/*.md`

请查看 `docs/` 目录获取完整的项目文档和接口规范。

## 快速启动

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

### 预览生产版本
```bash
npm run preview
```

## 技术栈

- Vue 3
- Vite
- Pinia (状态管理)
- Vue Router (路由管理)
- Axios (HTTP请求)
- Element Plus (UI组件库)

## 主要功能

- 新闻动态展示
- 学习资源管理
- 通知公告发布
- 主题活动展示
- 模范支部展示
- 用户个人中心

## 项目结构

```
├── src/              # 源代码目录
├── api/              # API接口定义
├── docs/             # 项目文档目录
├── public/           # 静态资源
├── index.html        # 入口HTML文件
├── vite.config.js    # Vite配置文件
├── package.json      # 项目依赖
└── README.md         # 项目说明（本文件）
```

## 开发说明

- 开发环境：Node.js 20.19+ 或 22.12+
- 代码规范：遵循ESLint和Prettier配置
- 提交规范：使用Conventional Commits

## 联系信息

如有问题，请联系项目开发团队。