-- 党建管理系统数据库SQL脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS party_management_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE party_management_system;

-- 用户表（党员信息）
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名（登录账号）',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender ENUM('男', '女') NOT NULL COMMENT '性别',
    age INT COMMENT '年龄',
    party_status ENUM('入党申请人', '入党积极分子', '发展对象', '预备党员', '正式党员') NOT NULL COMMENT '党员身份',
    branch VARCHAR(100) NOT NULL COMMENT '所属支部',
    phone VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(100) COMMENT '邮箱',
    join_date DATE COMMENT '入党日期',
    position VARCHAR(100) COMMENT '职务',
    education ENUM('初中', '高中', '大专', '本科', '硕士研究生', '博士研究生') COMMENT '学历',
    work_years INT COMMENT '工作年限',
    avatar VARCHAR(255) COMMENT '头像路径',
    role ENUM('admin', 'manager', 'member') NOT NULL DEFAULT 'member' COMMENT '系统角色',
    status ENUM('active', 'inactive') NOT NULL DEFAULT 'active' COMMENT '账号状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户表（党员信息）';

-- 新闻表
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
) COMMENT '新闻表';

-- 学习资源表
CREATE TABLE IF NOT EXISTS learning_resources (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '资源ID',
    title VARCHAR(200) NOT NULL COMMENT '资源标题',
    type ENUM('article', 'video', 'audio', 'document') NOT NULL COMMENT '资源类型',
    category ENUM('theory', 'history', 'policy', 'model', 'integrity') NOT NULL COMMENT '资源分类',
    author VARCHAR(100) NOT NULL COMMENT '作者/来源',
    upload_date DATE NOT NULL COMMENT '上传日期',
    views INT DEFAULT 0 COMMENT '浏览量',
    status ENUM('draft', 'published') NOT NULL DEFAULT 'draft' COMMENT '状态',
    url VARCHAR(255) COMMENT '资源链接',
    description TEXT COMMENT '资源简介',
    content LONGTEXT COMMENT '资源内容（文章类型）',
    upload_by INT COMMENT '上传人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (upload_by) REFERENCES users(id) ON DELETE SET NULL
) COMMENT '学习资源表';

-- 党建活动表
CREATE TABLE IF NOT EXISTS party_activities (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    title VARCHAR(200) NOT NULL COMMENT '活动名称',
    type ENUM('主题党日', '三会一课', '志愿服务', '理论学习', '社会实践', '其他活动') NOT NULL COMMENT '活动类型',
    organization VARCHAR(100) NOT NULL COMMENT '发起组织',
    activity_date DATETIME NOT NULL COMMENT '活动时间',
    location VARCHAR(100) NOT NULL COMMENT '活动地点',
    responsible_person VARCHAR(50) NOT NULL COMMENT '负责人',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    content TEXT NOT NULL COMMENT '活动内容',
    budget VARCHAR(50) COMMENT '活动预算',
    participant_count INT DEFAULT 0 COMMENT '参与人数',
    status ENUM('待审批', '已审批', '已发布', '进行中', '已完成') NOT NULL DEFAULT '待审批' COMMENT '活动状态',
    created_by INT COMMENT '创建人ID',
    updated_by INT COMMENT '更新人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
) COMMENT '党建活动表';

-- 主题党日表
CREATE TABLE IF NOT EXISTS theme_party_days (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主题党日ID',
    title VARCHAR(200) NOT NULL COMMENT '活动名称',
    theme_type ENUM('理论学习', '志愿服务', '社会实践', '校园文化', '其他') NOT NULL COMMENT '主题类型',
    organization VARCHAR(100) NOT NULL COMMENT '发起组织',
    activity_date DATETIME NOT NULL COMMENT '活动时间',
    location VARCHAR(100) NOT NULL COMMENT '活动地点',
    responsible_person VARCHAR(50) NOT NULL COMMENT '负责人',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    content TEXT NOT NULL COMMENT '活动内容',
    budget VARCHAR(50) COMMENT '活动预算',
    participant_count INT DEFAULT 0 COMMENT '参与人数',
    status ENUM('待审批', '已审批', '进行中', '已结束') NOT NULL DEFAULT '待审批' COMMENT '活动状态',
    created_by INT COMMENT '创建人ID',
    updated_by INT COMMENT '更新人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
) COMMENT '主题党日表';

-- 党员发展流程表
CREATE TABLE IF NOT EXISTS member_development (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '发展记录ID',
    user_id INT NOT NULL COMMENT '关联用户ID',
    stage ENUM('申请入党', '入党积极分子', '发展对象', '预备党员', '正式党员') NOT NULL COMMENT '发展阶段',
    application_date DATE COMMENT '申请日期',
    active_member_date DATE COMMENT '确定积极分子日期',
    development_object_date DATE COMMENT '确定发展对象日期',
    probationary_date DATE COMMENT '预备党员接收日期',
    formal_date DATE COMMENT '转正日期',
    current_step VARCHAR(50) COMMENT '当前步骤',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) COMMENT '党员发展流程表';

-- 党员发展步骤表
CREATE TABLE IF NOT EXISTS development_steps (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '步骤ID',
    development_id INT NOT NULL COMMENT '发展记录ID',
    step_name VARCHAR(100) NOT NULL COMMENT '步骤名称',
    description TEXT COMMENT '步骤描述',
    status ENUM('pending', 'completed') NOT NULL DEFAULT 'pending' COMMENT '步骤状态',
    completed_date DATE COMMENT '完成日期',
    materials TEXT COMMENT '相关材料（JSON格式）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (development_id) REFERENCES member_development(id) ON DELETE CASCADE
) COMMENT '党员发展步骤表';

-- 用户浏览记录表
CREATE TABLE IF NOT EXISTS browsing_history (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id INT NOT NULL COMMENT '用户ID',
    item_id INT NOT NULL COMMENT '浏览项ID',
    item_type ENUM('news', 'learning', 'activity') NOT NULL COMMENT '浏览项类型',
    title VARCHAR(200) NOT NULL COMMENT '浏览项标题',
    category VARCHAR(50) COMMENT '分类（新闻）',
    topic VARCHAR(50) COMMENT '主题（学习资源）',
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) COMMENT '用户浏览记录表';

-- 用户画像表
CREATE TABLE IF NOT EXISTS user_profiles (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '画像ID',
    user_id INT UNIQUE NOT NULL COMMENT '用户ID',
    labels TEXT COMMENT '标签（JSON数组）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) COMMENT '用户画像表';

-- 用户偏好表
CREATE TABLE IF NOT EXISTS user_preferences (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '偏好ID',
    user_id INT NOT NULL COMMENT '用户ID',
    news_categories TEXT NOT NULL COMMENT '新闻分类偏好（JSON格式）',
    learning_topics TEXT NOT NULL COMMENT '学习主题偏好（JSON格式）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) COMMENT '用户偏好表';

-- 用户性格特征表
CREATE TABLE IF NOT EXISTS user_personality (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '性格特征ID',
    user_id INT NOT NULL COMMENT '用户ID',
    personality_traits TEXT NOT NULL COMMENT '性格特征（JSON格式）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) COMMENT '用户性格特征表';

-- 系统角色表
CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    name VARCHAR(50) UNIQUE NOT NULL COMMENT '角色名称',
    description TEXT COMMENT '角色描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '系统角色表';

-- 系统权限表
CREATE TABLE IF NOT EXISTS permissions (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    name VARCHAR(50) UNIQUE NOT NULL COMMENT '权限名称',
    description TEXT COMMENT '权限描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '系统权限表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INT NOT NULL COMMENT '角色ID',
    permission_id INT NOT NULL COMMENT '权限ID',
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
) COMMENT '角色权限关联表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT NOT NULL COMMENT '用户ID',
    role_id INT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) COMMENT '用户角色关联表';

-- 插入初始数据

-- 插入角色数据
INSERT INTO roles (name, description) VALUES
('admin', '系统管理员'),
('manager', '党建工作管理员'),
('member', '普通党员用户');

-- 插入权限数据
INSERT INTO permissions (name, description) VALUES
('user_management', '用户管理权限'),
('news_management', '新闻管理权限'),
('learning_resource_management', '学习资源管理权限'),
('activity_management', '活动管理权限'),
('member_development_management', '党员发展管理权限'),
('data_analysis', '数据分析权限'),
('system_setting', '系统设置权限');

-- 插入角色权限关联数据
-- 管理员拥有所有权限
INSERT INTO role_permissions (role_id, permission_id) SELECT r.id, p.id FROM roles r, permissions p WHERE r.name = 'admin';

-- 党建工作管理员拥有大部分权限
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'manager' AND p.name NOT IN ('system_setting');

-- 普通党员用户拥有查看权限（可根据实际需求调整）
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'member' AND p.name IN ('news_management', 'learning_resource_management', 'activity_management');

-- 插入测试用户数据
INSERT INTO users (username, password, name, gender, age, party_status, branch, phone, email, join_date, position, education, work_years, avatar, role) VALUES
('admin', 'admin123', '管理员', '男', 45, '正式党员', '校党委', '13800138000', 'admin@example.com', '2000-07-01', '党委书记', '硕士研究生', 20, '/src/assets/images/head.png', 'admin'),
('zhangsan', 'zhangsan123', '张三', '男', 35, '正式党员', '技术研发支部', '138****1234', 'zhangsan@example.com', '2015-07-01', '研发部经理', '硕士研究生', 10, '/src/assets/images/head.png', 'member'),
('lisi', 'lisi123', '李四', '女', 28, '预备党员', '软件学院党支部', '139****5678', 'lisi@example.com', '2022-05-15', '软件工程师', '本科', 5, '/src/assets/images/head.png', 'member');

-- 插入测试新闻数据
INSERT INTO news (title, category, author, source, publish_date, summary, content, views, status) VALUES
('学习贯彻习近平新时代中国特色社会主义思想主题党日', '党建动态', '校党委', '学校官网', '2023-10-20 09:00:00', '深入学习贯彻习近平新时代中国特色社会主义思想，推动党建工作高质量发展。', '深入学习贯彻习近平新时代中国特色社会主义思想，推动党建工作高质量发展。', 256, 'published'),
('我校新增两个国家级一流本科专业建设点', '党建动态', '教务处', '学校官网', '2023-07-25 16:00:00', '在教育部公布的2022年度国家级一流本科专业建设点名单中，我校计算机科学与技术、软件工程两个专业入选。', '日前，教育部公布了2022年度国家级一流本科专业建设点名单，我校计算机科学与技术、软件工程两个专业成功入选。截至目前，我校已有8个国家级一流本科专业建设点。', 3456, 'published');

-- 插入测试学习资源数据
INSERT INTO learning_resources (title, type, category, author, upload_date, views, status, description, content) VALUES
('习近平新时代中国特色社会主义思想学习纲要', 'article', 'theory', '中共中央宣传部', '2023-09-15', 1258, 'published', '深入学习习近平新时代中国特色社会主义思想的重要辅导读物', '习近平新时代中国特色社会主义思想是当代中国马克思主义、二十一世纪马克思主义，是中华文化和中国精神的时代精华，实现了马克思主义中国化时代化新的飞跃。'),
('中国共产党章程（2022年修订）', 'document', 'policy', '中共中央', '2023-07-10', 1567, 'published', '2022年党的二十大修订通过的中国共产党章程', '中国共产党是中国工人阶级的先锋队，同时是中国人民和中华民族的先锋队，是中国特色社会主义事业的领导核心，代表中国先进生产力的发展要求，代表中国先进文化的前进方向，代表中国最广大人民的根本利益。');

-- 插入测试党建活动数据
INSERT INTO party_activities (title, type, organization, activity_date, location, responsible_person, contact_phone, content, status) VALUES
('学习贯彻习近平新时代中国特色社会主义思想主题党日', '主题党日', '校党委', '2023-10-20 09:00:00', '大礼堂', '张书记', '13800138001', '深入学习贯彻习近平新时代中国特色社会主义思想，推动党建工作高质量发展。', '已完成'),
('支部委员会会议', '三会一课', '计算机学院党总支', '2023-10-15 14:30:00', '计算机学院会议室', '李院长', '13800138002', '讨论近期党建工作安排和党员发展事宜。', '已完成');

-- 插入测试主题党日数据
INSERT INTO theme_party_days (title, theme_type, organization, activity_date, location, responsible_person, contact_phone, content, status) VALUES
('学习习近平总书记重要讲话精神', '理论学习', '校党委', '2023-09-28 14:30:00', '行政楼101会议室', '张书记', '13800138001', '组织中层以上干部学习习近平总书记重要讲话精神...', '已结束'),
('关爱留守儿童主题党日活动', '志愿服务', '计算机学院党总支', '2023-09-20 09:00:00', '希望小学', '李院长', '13800138002', '组织党员志愿者到希望小学开展关爱留守儿童活动...', '已结束');

-- 插入测试浏览记录数据
INSERT INTO browsing_history (user_id, item_id, item_type, title, category, timestamp) VALUES
(2, 1, 'news', '学习贯彻习近平新时代中国特色社会主义思想主题党日', '党建动态', '2023-12-15 14:30:00'),
(2, 1, 'learning', '习近平新时代中国特色社会主义思想学习纲要', NULL, '2023-12-14 09:15:00');

-- 插入测试用户画像数据
INSERT INTO user_profiles (user_id, labels) VALUES
(2, '["时政要闻", "理论学习", "组织建设", "党章党规", "科技创新"]');

-- 插入测试用户偏好数据
INSERT INTO user_preferences (user_id, news_categories, learning_topics) VALUES
(2, '{"时政要闻": 15, "组织建设": 12, "科技创新": 8, "党风廉政": 6, "基层党建": 5}', '{"理论学习": 20, "党章党规": 18, "党史学习": 15, "业务知识": 10, "管理技能": 7}');

-- 插入测试用户性格特征数据
INSERT INTO user_personality (user_id, personality_traits) VALUES
(2, '{"资讯关注者": 0.9, "学习爱好者": 0.85, "活跃用户": 0.8, "时事敏锐": 0.75, "求知若渴": 0.7}');

-- 插入测试党员发展数据
INSERT INTO member_development (user_id, stage, application_date, current_step) VALUES
(3, '预备党员', '2021-03-15', '上级党委审批');

-- 插入测试党员发展步骤数据
INSERT INTO development_steps (development_id, step_name, description, status) VALUES
(1, '提交入党申请', '入党申请人提交书面入党申请', 'completed'),
(1, '确定入党积极分子', '党支部确定为入党积极分子', 'completed'),
(1, '确定发展对象', '党支部确定为发展对象', 'completed'),
(1, '上级党委审批', '上级党委审批预备党员', 'pending');

-- 插入用户角色关联数据
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- 管理员
(2, 3), -- 张三是普通用户
(3, 3); -- 李四是普通用户

COMMIT;
