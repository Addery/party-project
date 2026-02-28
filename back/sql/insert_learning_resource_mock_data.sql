USE party_management_system;

-- 先更新所有记录的level字段
UPDATE learning_resources SET level='beginner' WHERE id % 3 = 1;
UPDATE learning_resources SET level='intermediate' WHERE id % 3 = 2;
UPDATE learning_resources SET level='advanced' WHERE id % 3 = 0;

-- 更新rating字段（1.0到5.0的随机值）
UPDATE learning_resources SET rating = ROUND(1 + RAND() * 4, 1);

-- 更新duration字段
UPDATE learning_resources SET duration = CONCAT(FLOOR(RAND() * 60 + 10), '分钟') WHERE type='video';
UPDATE learning_resources SET duration = CONCAT(FLOOR(RAND() * 30 + 5), '分钟') WHERE type='audio';
UPDATE learning_resources SET duration = CONCAT(FLOOR(RAND() * 20 + 5), '分钟阅读') WHERE type='article';
UPDATE learning_resources SET duration = CONCAT(FLOOR(RAND() * 120 + 30), '页') WHERE type='document';

-- 更新tags字段
UPDATE learning_resources SET tags='习近平新时代中国特色社会主义思想,理论学习,马克思主义' WHERE category='theory';
UPDATE learning_resources SET tags='党史学习,中国共产党历史,革命传统教育' WHERE category='history';
UPDATE learning_resources SET tags='政策解读,乡村振兴,改革开放' WHERE category='policy';
UPDATE learning_resources SET tags='先进典型,模范人物,榜样力量' WHERE category='model';
UPDATE learning_resources SET tags='党风廉政,反腐倡廉,纪律教育' WHERE category='integrity';

-- 验证结果
SELECT id, title, level, rating, duration, tags FROM learning_resources LIMIT 5;
