-- 为数据库中的用户画像添加模拟数据
-- 每个用户将获得更丰富的学习标签、兴趣和偏好

-- 更新用户ID为2的画像（zhangsan）
UPDATE user_portrait SET 
  learning_tags = '["资讯关注者", "学习爱好者", "活跃用户", "时事敏锐", "求知若渴", "理论研究者", "政策解读达人"]',
  learning_interests = '["时政要闻", "组织建设", "科技创新", "党风廉政", "基层党建", "政策解读", "思想理论"]',
  learning_preferences = '{"newsReading":{"时政要闻":25, "组织建设":20, "科技创新":18, "党风廉政":15, "基层党建":22}, "learningTopics":{"理论学习":30, "党章党规":25, "政策解读":28, "思想建设":22}}'
WHERE user_id = 2;

-- 更新用户ID为3的画像（lisi）
UPDATE user_portrait SET 
  learning_tags = '["科技爱好者", "创新思维", "数据驱动", "学习型人才", "技术专家", "政策关注者"]',
  learning_interests = '["科技创新", "数字经济", "人工智能", "大数据应用", "信息技术", "政策法规"]',
  learning_preferences = '{"newsReading":{"科技创新":30, "数字经济":25, "人工智能":28, "大数据应用":22, "信息技术":20}, "learningTopics":{"技术前沿":35, "政策解读":15, "数字治理":25, "创新案例":20}}'
WHERE user_id = 3;

-- 更新用户ID为5的画像（newuser）
UPDATE user_portrait SET 
  learning_tags = '["新党员", "学习新人", "积极分子", "思想进步", "求知若渴", "组织参与者"]',
  learning_interests = '["入党知识", "思想理论", "党章党规", "党史学习", "组织生活", "先进事迹"]',
  learning_preferences = '{"newsReading":{"党史学习":25, "党章党规":28, "思想理论":22, "入党知识":30, "先进事迹":20}, "learningTopics":{"基础理论":35, "党性修养":28, "组织纪律":20, "党员义务":18}}'
WHERE user_id = 5;

-- 更新用户ID为6的画像（Addery）
UPDATE user_portrait SET 
  learning_tags = '["学习达人", "组织管理者", "政策专家", "思想引领者", "理论功底深厚", "学习型领导"]',
  learning_interests = '["组织建设", "政策研究", "思想理论", "领导科学", "基层党建", "人才培养"]',
  learning_preferences = '{"newsReading":{"组织建设":30, "政策研究":25, "思想理论":22, "领导科学":20, "基层党建":18}, "learningTopics":{"党建工作":35, "领导艺术":28, "政策解读":20, "人才发展":18}}'
WHERE user_id = 6;

-- 更新用户ID为7的画像（test）
UPDATE user_portrait SET 
  learning_tags = '["普通党员", "岗位能手", "学习进取", "工作认真", "组织纪律性强", "业务骨干"]',
  learning_interests = '["业务知识", "岗位技能", "政策法规", "工作经验", "职业发展", "行业动态"]',
  learning_preferences = '{"newsReading":{"业务知识":28, "岗位技能":25, "政策法规":20, "行业动态":18, "职业发展":15}, "learningTopics":{"专业技能":35, "工作方法":25, "政策应用":20, "职业规划":15}}'
WHERE user_id = 7;

-- 更新用户ID为8的画像（testtest）
UPDATE user_portrait SET 
  learning_tags = '["青年党员", "活力四射", "创新思维", "学习能力强", "社交达人", "网络活跃"]',
  learning_interests = '["青年工作", "网络党建", "创新实践", "志愿服务", "文化活动", "社交网络"]',
  learning_preferences = '{"newsReading":{"青年工作":25, "网络党建":30, "创新实践":22, "志愿服务":18, "文化活动":15}, "learningTopics":{"青年发展":35, "网络技术":25, "创新方法":20, "志愿服务":15}}'
WHERE user_id = 8;

-- 更新用户ID为9的画像
UPDATE user_portrait SET 
  learning_tags = '["资深党员", "经验丰富", "理论扎实", "思想成熟", "组织协调", "传帮带能手"]',
  learning_interests = '["党史研究", "传统教育", "经验分享", "思想工作", "组织建设", "人才培养"]',
  learning_preferences = '{"newsReading":{"党史研究":30, "传统教育":25, "经验分享":22, "组织建设":18, "人才培养":15}, "learningTopics":{"党史学习":35, "传统教育":25, "经验传承":20, "思想工作":15}}'
WHERE user_id = 9;

-- 更新用户ID为10的画像（admin）
UPDATE user_portrait SET 
  learning_tags = '["系统管理员", "平台维护者", "技术支持", "安全专家", "数据管理者", "服务保障"]',
  learning_interests = '["系统安全", "技术维护", "数据管理", "平台优化", "用户体验", "安全策略"]',
  learning_preferences = '{"newsReading":{"系统安全":30, "技术维护":28, "数据管理":22, "平台优化":20, "安全策略":18}, "learningTopics":{"技术维护":35, "系统安全":30, "数据管理":20, "平台优化":15}}'
WHERE user_id = 10;

SELECT '模拟数据添加完成！' AS message;
