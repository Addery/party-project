-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS party_management_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE party_management_system;

-- 创建就业资讯表
CREATE TABLE IF NOT EXISTS employment_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    publish_date DATE NOT NULL,
    source VARCHAR(100) NOT NULL,
    view_count INT DEFAULT 0,
    category VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建企业招聘表
CREATE TABLE IF NOT EXISTS company_recruitment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    industry VARCHAR(100) NOT NULL,
    job_title VARCHAR(255) NOT NULL,
    salary VARCHAR(50) NOT NULL,
    location VARCHAR(100) NOT NULL,
    is_hot BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建就业统计表
CREATE TABLE IF NOT EXISTS employment_stats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employment_rate DECIMAL(5,2) NOT NULL,
    graduate_employment_rate DECIMAL(5,2) NOT NULL,
    company_count INT NOT NULL,
    recruitment_count INT NOT NULL,
    year INT NOT NULL,
    semester INT NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_year_semester (year, semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建就业指导资源表
CREATE TABLE IF NOT EXISTS employment_guidance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建老师指导学生就业情况表
CREATE TABLE IF NOT EXISTS teacher_employment_guidance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    student_count INT NOT NULL,
    rank_change INT NOT NULL,
    type VARCHAR(50) NOT NULL COMMENT 'undergraduate: 本科生, graduate: 研究生',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建就业政策表
CREATE TABLE IF NOT EXISTS employment_policy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    publish_date DATE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建就业服务表
CREATE TABLE IF NOT EXISTS employment_service (
    id INT AUTO_INCREMENT PRIMARY KEY,
    icon VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入就业统计数据
INSERT INTO employment_stats (employment_rate, graduate_employment_rate, company_count, recruitment_count, year, semester)
VALUES (97.70, 98.62, 300, 1500, 2025, 1)
ON DUPLICATE KEY UPDATE
    employment_rate = VALUES(employment_rate),
    graduate_employment_rate = VALUES(graduate_employment_rate),
    company_count = VALUES(company_count),
    recruitment_count = VALUES(recruitment_count),
    updated_at = CURRENT_TIMESTAMP;

-- 插入就业资讯数据
INSERT INTO employment_info (title, content, publish_date, source, view_count, category)
VALUES 
('2025年全国高校毕业生就业形势分析', '<p>2025年全国高校毕业生预计将达到1200万人，就业形势总体平稳但面临一定压力。</p>', '2025-11-20', '教育部', 5892, 'news'),
('人工智能领域就业前景广阔', '<p>随着人工智能技术的快速发展，相关领域的就业岗位需求持续增长。</p>', '2025-11-18', '科技日报', 3245, 'analysis'),
('大学生就业创业扶持政策解读', '<p>国家出台多项政策支持大学生就业创业，包括税收优惠、贷款扶持等。</p>', '2025-11-15', '人力资源社会保障部', 4567, 'policy'),
('2025年热门就业行业TOP10', '<p>根据最新调查，互联网、人工智能、新能源等行业成为2025年热门就业行业。</p>', '2025-11-10', '就业时报', 6789, 'ranking');

-- 插入企业招聘数据
INSERT INTO company_recruitment (company_name, industry, job_title, salary, location, is_hot)
VALUES 
('字节跳动', '互联网', '前端开发工程师', '20-40K', '北京', TRUE),
('阿里巴巴', '互联网', '产品经理', '25-45K', '杭州', TRUE),
('腾讯', '互联网', '后端开发工程师', '22-42K', '深圳', TRUE),
('华为', '通信', '软件工程师', '18-35K', '深圳', TRUE),
('百度', '互联网', '算法工程师', '25-50K', '北京', TRUE),
('京东', '电子商务', '运营专员', '15-25K', '北京', FALSE),
('美团', '本地生活', '数据分析工程师', '18-30K', '北京', FALSE),
('滴滴出行', '交通出行', '移动端开发工程师', '20-40K', '北京', FALSE),
('小米', '智能硬件', '测试工程师', '15-28K', '北京', FALSE),
('网易', '互联网', 'UI设计师', '18-32K', '杭州', FALSE);

-- 插入就业指导资源数据
INSERT INTO employment_guidance (title, type)
VALUES 
('简历制作指南', '文档'),
('面试技巧分享', '视频'),
('职业规划方法论', '文档'),
('求职礼仪培训', '视频'),
('创业项目选择与评估', '文档');

-- 插入老师指导学生就业情况数据
INSERT INTO teacher_employment_guidance (name, student_count, rank_change, type)
VALUES 
('张老师', 45, 2, 'undergraduate'),
('李老师', 42, -1, 'undergraduate'),
('王老师', 38, 0, 'undergraduate'),
('赵老师', 35, 3, 'graduate'),
('刘老师', 32, -2, 'graduate');

-- 插入就业政策数据
INSERT INTO employment_policy (title, content, publish_date)
VALUES 
('关于进一步做好高校毕业生就业创业工作的通知', '<p>为进一步做好高校毕业生就业创业工作，现就有关事项通知如下...</p>', '2025-10-20'),
('大学生就业创业扶持政策', '<p>为鼓励大学生就业创业，国家出台了一系列扶持政策...</p>', '2025-09-15'),
('关于加强高校毕业生就业指导服务的意见', '<p>为加强高校毕业生就业指导服务，提高就业质量...</p>', '2025-08-10'),
('高校毕业生基层就业优惠政策', '<p>为引导高校毕业生到基层就业，国家制定了以下优惠政策...</p>', '2025-07-05');

-- 插入就业服务数据
INSERT INTO employment_service (icon, title, description)
VALUES 
('📅', '校园招聘会', '定期举办校园招聘会，为学生提供就业机会'),
('💼', '就业咨询', '提供一对一就业咨询服务'),
('📚', '就业指导课程', '开设就业指导相关课程'),
('📝', '简历批改', '提供专业简历批改服务'),
('🎯', '职业测评', '提供职业兴趣和能力测评服务');

-- 创建索引以提高查询性能
CREATE INDEX idx_employment_info_category ON employment_info(category);
CREATE INDEX idx_employment_info_publish_date ON employment_info(publish_date);
CREATE INDEX idx_company_recruitment_industry ON company_recruitment(industry);
CREATE INDEX idx_company_recruitment_is_hot ON company_recruitment(is_hot);
CREATE INDEX idx_employment_guidance_type ON employment_guidance(type);
CREATE INDEX idx_teacher_employment_guidance_type ON teacher_employment_guidance(type);
CREATE INDEX idx_employment_policy_publish_date ON employment_policy(publish_date);