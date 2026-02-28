-- 学习资源点赞表
CREATE TABLE IF NOT EXISTS resource_likes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    resource_id INT NOT NULL,
    user_id INT NOT NULL,
    like_time DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (resource_id) REFERENCES learning_resources(id) ON DELETE CASCADE
);

-- 学习资源评论表
CREATE TABLE IF NOT EXISTS resource_comments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    resource_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    parent_id INT,
    comment_time DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (resource_id) REFERENCES learning_resources(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES resource_comments(id) ON DELETE SET NULL
);

-- 学习资源分享表
CREATE TABLE IF NOT EXISTS resource_shares (
    id INT PRIMARY KEY AUTO_INCREMENT,
    resource_id INT NOT NULL,
    user_id INT NOT NULL,
    share_platform VARCHAR(50),
    share_link VARCHAR(500),
    share_time DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (resource_id) REFERENCES learning_resources(id) ON DELETE CASCADE
);