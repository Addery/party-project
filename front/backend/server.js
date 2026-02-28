import express from 'express';
import cors from 'cors';
import { exec } from 'child_process';

// 新闻分类枚举
const NewsCategory = {
  policy: "时政要闻",
  party_building: "党建动态",
  organization: "组织建设",
  technology: "科技创新",
  party_discipline: "党风廉政",
  grassroots_party: "基层党建",
  law: "法律法规",
  economy: "经济发展",
  health: "卫生健康",
  society: "社会民生",
  environment: "环境保护",
  education: "教育工作",
  culture: "文化动态",
  sports: "体育新闻",
  safety: "安全警示"
};

const app = express();
const PORT = 8080;

// 启用CORS
app.use(cors());

// 解析JSON请求体
app.use(express.json());

// 模拟AI回复的函数
function generateAIResponse(message) {
  // 这里是模拟的AI回复逻辑
  // 在实际应用中，这里应该调用大模型API
  const responses = [
    '感谢您的提问！我正在学习如何更好地回答您的问题。',
    '这是一个很有趣的问题，让我思考一下...',
    '根据您的问题，我理解您需要关于这方面的信息。',
    '我很高兴能为您提供帮助，请问还有什么我可以解答的吗？',
    '感谢您的耐心等待，我正在处理您的请求。'
  ];
  
  // 随机选择一个回复
  const randomIndex = Math.floor(Math.random() * responses.length);
  return responses[randomIndex];
}

// 聊天接口
app.post('/api/chat/send', (req, res) => {
  const { message } = req.body;
  
  console.log('收到用户消息:', message);
  
  // 生成AI回复
  const response = generateAIResponse(message);
  
  // 模拟网络延迟
  setTimeout(() => {
    res.json({ response: response });
  }, 1000);
});

// 健康检查接口
app.get('/api/health', (req, res) => {
  res.json({ status: 'ok', message: '服务器正常运行' });
});

// 模拟用户数据（用于登录验证）
const mockUsers = {
  'Addery': {
    id: 6,
    username: 'Addery',
    password: 'password123',
    name: 'Addery'
  },
  'zhangsan': {
    id: 1,
    username: 'zhangsan',
    password: '123456',
    name: '张三'
  }
};

// 模拟白名单数据（对应registration_whitelist表）
const mockWhitelist = [
  { phone: '13800138001', id_card: '110101199001011234', remark: '测试用户1' },
  { phone: '13800138002', id_card: '110101199001011235', remark: '测试用户2' },
  { phone: '13800138003', id_card: '110101199001011236', remark: '测试用户3' },
  { phone: '13800138004', id_card: '110101199001011237', remark: '测试用户4' },
  { phone: '13800138005', id_card: '110101199001011238', remark: '测试用户5' }
];

// 模拟用户画像数据
const mockUserPortraits = {
  1: {
    id: 1,
    user: {
      id: 1,
      username: 'zhangsan',
      name: '张三'
    },
    learningTags: '["资讯关注者", "学习爱好者", "活跃用户", "时事敏锐", "求知若渴"]',
    learningInterests: '["时政要闻", "组织建设", "科技创新", "党风廉政", "基层党建"]',
    learningPreferences: '{"newsReading":{"时政要闻":15, "组织建设":12, "科技创新":8, "党风廉政":6, "基层党建":5}, "learningTopics":{"理论学习":20, "党章党规":18, "党史学习":15}}',
    updatedAt: new Date().toISOString(),
    createdAt: '2023-12-01T10:00:00'
  },
  6: {
    id: 2,
    user: {
      id: 6,
      username: 'Addery',
      name: 'Addery'
    },
    learningTags: '["学习达人", "组织管理者", "政策专家", "思想引领者", "理论功底深厚", "学习型领导"]',
    learningInterests: '["组织建设", "政策研究", "思想理论", "领导科学", "基层党建", "人才培养"]',
    learningPreferences: '{"newsReading":{"组织建设":30, "政策研究":25, "思想理论":22, "领导科学":20, "基层党建":18}, "learningTopics":{"党建工作":35, "领导艺术":28, "政策解读":20, "人才发展":18}}',
    updatedAt: new Date().toISOString(),
    createdAt: '2023-12-01T10:00:00'
  }
};

// 模拟新闻数据
const mockNews = [
  {
    id: 1,
    title: "习近平主持召开中央全面深化改革委员会第五次会议",
    category: "头条新闻",
    author: "新华社",
    source: "新华网",
    publishDate: "2025-11-20T09:00:00Z",
    summary: "习近平强调，要深入贯彻落实党的二十大精神，坚定不移全面深化改革，为新时代坚持和发展中国特色社会主义提供强大动力。",
    content: "<p>新华社北京11月20日电 中共中央总书记、国家主席、中央军委主席、中央全面深化改革委员会主任习近平11月20日下午主持召开中央全面深化改革委员会第五次会议。</p><p>会议强调，要把全面深化改革作为推进中国式现代化的根本动力，作为稳大局、应变局、开新局的重要抓手，把准方向、守正创新、真抓实干，在新征程上谱写改革开放新篇章。</p>",
    cover: "",
    views: 12345,
    status: "published",
    isTop: true,
    createdAt: "2025-11-20T08:00:00Z",
    updatedAt: "2025-11-20T08:30:00Z"
  },
  {
    id: 2,
    title: "学校召开2025年党委理论学习中心组学习扩大会",
    category: "学校动态",
    author: "党委宣传部",
    source: "学校官网",
    publishDate: "2025-11-19T14:30:00Z",
    summary: "学校召开党委理论学习中心组学习扩大会，深入学习贯彻习近平新时代中国特色社会主义思想，研究部署学校当前重点工作。",
    content: "<p>11月19日下午，学校在学术报告厅召开2025年党委理论学习中心组学习扩大会。全体校领导、中层干部参加了会议。</p><p>会议要求，全校上下要进一步提高政治站位，把学习贯彻习近平新时代中国特色社会主义思想作为首要政治任务，紧密结合学校实际，推动各项工作再上新台阶。</p>",
    cover: "",
    views: 5678,
    status: "published",
    isTop: false,
    createdAt: "2025-11-19T13:00:00Z",
    updatedAt: "2025-11-19T13:30:00Z"
  },
  {
    id: 3,
    title: "关于开展2025年党员教育培训工作的通知",
    category: "党建工作",
    author: "党委组织部",
    source: "学校官网",
    publishDate: "2025-11-18T09:00:00Z",
    summary: "为深入学习贯彻党的二十大精神，加强党员教育培训工作，现就开展2025年党员教育培训工作有关事项通知如下。",
    content: "<p>各党支部：</p><p>为深入学习贯彻党的二十大精神，加强党员教育培训工作，提高党员队伍素质，现就开展2025年党员教育培训工作有关事项通知如下：</p><p>一、培训对象：全体党员</p><p>二、培训内容：习近平新时代中国特色社会主义思想、党的二十大精神、党章党规等</p><p>三、培训时间：2025年11月至12月</p>",
    cover: "",
    views: 3456,
    status: "published",
    isTop: false,
    createdAt: "2025-11-18T08:00:00Z",
    updatedAt: "2025-11-18T08:30:00Z"
  },
  {
    id: 4,
    title: "我校在全国大学生党建工作案例评选中获奖",
    category: "党建工作",
    author: "党委组织部",
    source: "学校官网",
    publishDate: "2025-11-17T16:00:00Z",
    summary: "近日，在教育部思想政治工作司主办的全国大学生党建工作案例评选中，我校报送的《‘互联网+党建’工作模式探索与实践》案例荣获二等奖。",
    content: "<p>近日，教育部思想政治工作司公布了2025年全国大学生党建工作案例评选结果，我校报送的《‘互联网+党建’工作模式探索与实践》案例荣获二等奖。</p><p>该案例总结了我校近年来在推进‘互联网+党建’工作中的探索与实践，包括搭建智慧党建平台、开发党建APP、开展线上线下相结合的党建活动等方面的经验做法。</p>",
    cover: "",
    views: 2345,
    status: "published",
    isTop: false,
    createdAt: "2025-11-17T15:00:00Z",
    updatedAt: "2025-11-17T15:30:00Z"
  },
  {
    id: 5,
    title: "2025年秋季学期思政教育系列讲座预告",
    category: "思政教育",
    author: "马克思主义学院",
    source: "学校官网",
    publishDate: "2025-11-16T10:00:00Z",
    summary: "为加强学生思想政治教育，马克思主义学院将于11月至12月举办2025年秋季学期思政教育系列讲座。",
    content: "<p>为加强学生思想政治教育，提高学生的思想政治素质，马克思主义学院将于2025年11月至12月举办思政教育系列讲座。</p><p>讲座主题包括：习近平新时代中国特色社会主义思想的世界观和方法论、党的二十大精神解读、中国特色社会主义道路自信等。</p><p>欢迎广大师生积极参加！</p>",
    cover: "",
    views: 1890,
    status: "published",
    isTop: false,
    createdAt: "2025-11-16T09:00:00Z",
    updatedAt: "2025-11-16T09:30:00Z"
  },
  {
    id: 6,
    title: "学校举行2025年新生入党宣誓仪式",
    category: "党建工作",
    author: "党委组织部",
    source: "学校官网",
    publishDate: "2025-11-15T15:00:00Z",
    summary: "11月15日上午，学校在图书馆前广场举行2025年新生入党宣誓仪式。校党委副书记、纪委书记出席仪式并讲话。",
    content: "<p>11月15日上午，学校在图书馆前广场举行2025年新生入党宣誓仪式。校党委副书记、纪委书记出席仪式并讲话，全体新入党的学生党员参加了仪式。</p><p>仪式在庄严的国歌声中开始。校党委副书记、纪委书记带领新党员宣誓，并对新党员提出了希望和要求。</p>",
    cover: "",
    views: 2134,
    status: "published",
    isTop: false,
    createdAt: "2025-11-15T14:00:00Z",
    updatedAt: "2025-11-15T14:30:00Z"
  },
  {
    id: 7,
    title: "关于做好2025年冬季防火安全工作的通知",
    category: "通知公告",
    author: "保卫处",
    source: "学校官网",
    publishDate: "2025-11-14T09:00:00Z",
    summary: "为切实做好2025年冬季防火安全工作，预防和减少火灾事故的发生，保障师生员工的生命财产安全，现就有关事项通知如下。",
    content: "<p>各部门、各单位：</p><p>为切实做好2025年冬季防火安全工作，预防和减少火灾事故的发生，保障师生员工的生命财产安全，现就有关事项通知如下：</p><p>一、加强组织领导，落实消防安全责任</p><p>二、开展消防安全检查，消除火灾隐患</p><p>三、加强消防安全教育，提高消防安全意识</p><p>四、做好应急准备，提高应急处置能力</p>",
    cover: "",
    views: 1567,
    status: "published",
    isTop: false,
    createdAt: "2025-11-14T08:00:00Z",
    updatedAt: "2025-11-14T08:30:00Z"
  },
  {
    id: 8,
    title: "学校举办2025年教职工运动会",
    category: "学校动态",
    author: "工会",
    source: "学校官网",
    publishDate: "2025-11-13T16:00:00Z",
    summary: "11月13日，学校在田径场举办2025年教职工运动会。校领导、全体教职工参加了运动会。",
    content: "<p>11月13日，学校在田径场举办2025年教职工运动会。校领导、全体教职工参加了运动会。</p><p>运动会设置了跑步、跳远、铅球、拔河等多个比赛项目，教职工们积极参与，展现了良好的精神风貌。</p><p>经过一天的激烈角逐，各参赛队伍都取得了优异的成绩。</p>",
    cover: "",
    views: 2678,
    status: "published",
    isTop: false,
    createdAt: "2025-11-13T15:00:00Z",
    updatedAt: "2025-11-13T15:30:00Z"
  }
];

// 获取头条新闻列表
app.get('/api/news/headline', (req, res) => {
  const page = parseInt(req.query.page) || 1;
  const pageSize = parseInt(req.query.pageSize) || 3;
  const sortBy = req.query.sortBy || 'publishDate';
  const order = req.query.order || 'desc';
  
  // 筛选头条新闻
  let filteredNews = mockNews.filter(news => news.category === '头条新闻' && news.status === 'published');
  
  // 排序
  filteredNews.sort((a, b) => {
    if (order === 'asc') {
      return new Date(a[sortBy]) - new Date(b[sortBy]);
    } else {
      return new Date(b[sortBy]) - new Date(a[sortBy]);
    }
  });
  
  // 分页
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  const paginatedNews = filteredNews.slice(startIndex, endIndex);
  
  res.json({
    code: 200,
    message: '获取成功',
    data: {
      newsList: paginatedNews,
      total: filteredNews.length,
      page: page,
      pageSize: pageSize
    }
  });
});

// 获取新闻详情
app.get('/api/news/:id', (req, res) => {
  const id = parseInt(req.params.id);
  const news = mockNews.find(news => news.id === id && news.status === 'published');
  
  if (!news) {
    return res.status(404).json({
      code: 404,
      message: '新闻不存在',
      data: null
    });
  }
  
  res.json({
    code: 200,
    message: '获取成功',
    data: news
  });
});

// 获取相关新闻
app.get('/api/news/related', (req, res) => {
  const category = req.query.category;
  const excludeId = parseInt(req.query.excludeId);
  
  if (!category) {
    return res.status(400).json({
      code: 400,
      message: '分类参数不能为空',
      data: null
    });
  }
  
  // 筛选同一分类且不包含排除ID的新闻
  let relatedNews = mockNews.filter(news => 
    news.category === category && 
    news.id !== excludeId && 
    news.status === 'published'
  );
  
  // 按发布日期降序排序，最多返回3条
  relatedNews.sort((a, b) => new Date(b.publishDate) - new Date(a.publishDate));
  relatedNews = relatedNews.slice(0, 3);
  
  res.json({
    code: 200,
    message: '获取成功',
    data: relatedNews
  });
});

// 获取新闻列表（通用接口）
app.get('/api/news', (req, res) => {
  const page = parseInt(req.query.page) || 1;
  const pageSize = parseInt(req.query.pageSize) || 10;
  const category = req.query.category;
  const keyword = req.query.keyword;
  const sortBy = req.query.sortBy || 'publishDate';
  const order = req.query.order || 'desc';
  
  // 筛选
  let filteredNews = mockNews.filter(news => news.status === 'published');
  
  if (category) {
    filteredNews = filteredNews.filter(news => news.category === category);
  }
  
  if (keyword) {
    const lowerKeyword = keyword.toLowerCase();
    filteredNews = filteredNews.filter(news => 
      news.title.toLowerCase().includes(lowerKeyword)
    );
  }
  
  // 排序
  filteredNews.sort((a, b) => {
    if (order === 'asc') {
      return new Date(a[sortBy]) - new Date(b[sortBy]);
    } else {
      return new Date(b[sortBy]) - new Date(a[sortBy]);
    }
  });
  
  // 分页
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  const paginatedNews = filteredNews.slice(startIndex, endIndex);
  
  res.json({
    code: 200,
    message: '获取成功',
    data: {
      newsList: paginatedNews,
      total: filteredNews.length,
      page: page,
      pageSize: pageSize
    }
  });
});

// 获取新闻分类
app.get('/api/news/categories', (req, res) => {
  // 从新闻数据中提取所有不重复的分类
  const categories = [...new Set(mockNews.map(news => news.category))];
  
  res.json({
    code: 200,
    message: '获取成功',
    data: categories
  });
});

// 登录接口
app.post('/api/auth/login', (req, res) => {
  const { username, password } = req.body;
  
  // 验证用户名和密码
  if (!username || !password) {
    return res.status(500).json({ 
      code: 500, 
      message: '用户名或密码不能为空', 
      data: null 
    });
  }
  
  // 查找用户
  const user = mockUsers[username];
  if (!user || user.password !== password) {
    return res.status(500).json({ 
      code: 500, 
      message: '用户名或密码错误', 
      data: null 
    });
  }
  
  // 生成模拟token
  const token = `mock_token_${username}_${Date.now()}`;
  
  // 返回登录成功响应
  res.json({
    code: 200,
    message: '登录成功',
    data: {
      token: token,
      user: {
        id: user.id,
        username: user.username,
        name: user.name
      }
    }
  });
});

// 注册接口
app.post('/api/auth/register', (req, res) => {
  const { 
    username, 
    password, 
    name, 
    gender, 
    age, 
    partyStatus, 
    branch, 
    phone, 
    email, 
    idCard, 
    joinDate, 
    position, 
    education, 
    workYears, 
    avatar, 
    role 
  } = req.body;
  
  // 验证必填字段
  if (!username || !password || !name || !phone || !idCard) {
    return res.status(400).json({ 
      code: 400, 
      msg: '注册需要提供手机号和身份证号', 
      data: null 
    });
  }
  
  // 检查用户名是否已存在
  if (mockUsers[username]) {
    return res.status(400).json({ 
      code: 400, 
      msg: '用户名已存在', 
      data: null 
    });
  }
  
  // 检查邮箱是否已存在
  if (email && Object.values(mockUsers).some(user => user.email === email)) {
    return res.status(400).json({ 
      code: 400, 
      msg: '邮箱已被注册', 
      data: null 
    });
  }
  
  // 检查手机号是否已存在
  if (phone && Object.values(mockUsers).some(user => user.phone === phone)) {
    return res.status(400).json({ 
      code: 400, 
      msg: '手机号已被注册', 
      data: null 
    });
  }
  
  // 白名单验证：检查phone和idCard组合是否在白名单中
  const isInWhitelist = mockWhitelist.some(item => 
    item.phone === phone && item.id_card === idCard
  );
  
  if (!isInWhitelist) {
    return res.status(400).json({ 
      code: 400, 
      msg: '该手机号和身份证号组合不在注册白名单中', 
      data: null 
    });
  }
  
  // 生成模拟用户ID（简单递增）
  const newUserId = Object.keys(mockUsers).length + 1;
  
  // 创建新用户
  const newUser = {
    id: newUserId,
    username: username,
    password: password, // 注意：在实际应用中，密码应该加密存储
    name: name,
    gender: gender,
    age: age,
    partyStatus: partyStatus,
    branch: branch,
    phone: phone,
    email: email,
    idCard: idCard,
    joinDate: joinDate || new Date(),
    position: position,
    education: education,
    workYears: workYears || 0,
    avatar: avatar,
    role: role || 'member'
  };
  
  // 添加到模拟用户数据中
  mockUsers[username] = newUser;
  
  // 返回注册成功响应
  res.status(200).json({
    code: 200,
    msg: '注册成功',
    data: {
      id: newUser.id,
      username: newUser.username,
      name: newUser.name,
      gender: newUser.gender,
      age: newUser.age,
      partyStatus: newUser.partyStatus,
      branch: newUser.branch,
      phone: newUser.phone,
      email: newUser.email,
      idCard: newUser.idCard,
      joinDate: newUser.joinDate,
      position: newUser.position,
      education: newUser.education,
      workYears: newUser.workYears,
      avatar: newUser.avatar,
      role: newUser.role,
      createTime: new Date(),
      updateTime: new Date()
    }
  });
});

// 验证token的中间件
function verifyToken(req, res, next) {
  const authHeader = req.headers.authorization;
  
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(500).json({ 
      code: 500, 
      message: '未登录', 
      data: null 
    });
  }
  
  // 从Bearer头中提取token
  const token = authHeader.split(' ')[1];
  
  // 简化验证：只要token格式正确就认为有效
  if (!token || !token.startsWith('mock_token_')) {
    return res.status(500).json({ 
      code: 500, 
      message: '未登录', 
      data: null 
    });
  }
  
  // 将token存储到请求对象中，供后续使用
  req.token = token;
  
  // 从token中提取用户名
  const username = token.split('_')[2];
  const user = mockUsers[username];
  
  if (user) {
    req.currentUserId = user.id;
  }
  
  next();
}

// 获取当前登录用户的画像 - 需要认证
app.get('/api/user/portrait/current', verifyToken, (req, res) => {
  const userId = req.currentUserId;
  
  // 获取用户画像
  const portrait = mockUserPortraits[userId];
  
  if (!portrait) {
    return res.status(500).json({ 
      code: 500, 
      message: '用户画像不存在', 
      data: null 
    });
  }
  
  res.json({
    code: 200,
    message: '获取用户画像成功',
    data: portrait
  });
});

// 根据用户ID获取用户画像 - 需要认证
app.get('/api/user/portrait/:userId', verifyToken, (req, res) => {
  const userId = parseInt(req.params.userId);
  
  // 获取用户画像
  const portrait = mockUserPortraits[userId];
  
  if (!portrait) {
    return res.status(500).json({ 
      code: 500, 
      message: '用户画像不存在', 
      data: null 
    });
  }
  
  res.json({
    code: 200,
    message: '获取用户画像成功',
    data: portrait
  });
});

// 手动更新当前登录用户的画像 - 需要认证
app.post('/api/user/portrait/update', verifyToken, (req, res) => {
  const userId = req.currentUserId;
  
  // 获取用户画像
  const portrait = mockUserPortraits[userId];
  
  if (!portrait) {
    return res.status(500).json({ 
      code: 500, 
      message: '用户画像不存在', 
      data: null 
    });
  }
  
  // 更新画像的updatedAt时间
  const updatedPortrait = {
    ...portrait,
    updatedAt: new Date().toISOString()
  };
  
  // 更新模拟数据
  mockUserPortraits[userId] = updatedPortrait;
  
  res.json({
    code: 200,
    message: '更新用户画像成功',
    data: updatedPortrait
  });
});

// 就业服务相关接口

// 获取就业统计数据
app.get('/api/employment/stats', (req, res) => {
  res.json({
    code: 200,
    message: 'success',
    data: {
      employmentRate: 97.70,           // 本科生就业率
      graduateEmploymentRate: 98.62,   // 研究生就业率
      companyCount: 300,               // 合作企业数量
      recruitmentCount: 1500           // 招聘岗位数量
    }
  });
});

// 启动服务器
app.listen(PORT, () => {
  console.log(`服务器正在运行，端口: ${PORT}`);
  console.log('聊天API地址: http://localhost:8080/api/chat/send');
  console.log('用户画像API地址: http://localhost:8080/api/user/portrait/current');
  console.log('健康检查API地址: http://localhost:8080/api/health');
  console.log('就业统计数据API地址: http://localhost:8080/api/employment/stats');
});