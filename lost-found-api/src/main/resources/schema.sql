-- 失物招领管理系统数据库初始化脚本
-- 数据库名: javaks

CREATE DATABASE IF NOT EXISTS javaks DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE javaks;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    role VARCHAR(20) NOT NULL DEFAULT 'REGULAR',
    create_time DATETIME,
    post_count INT DEFAULT 0,
    manage_count INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 物品表
CREATE TABLE IF NOT EXISTS items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    location VARCHAR(200),
    time DATETIME,
    publisher_id INT,
    publisher_name VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    item_type VARCHAR(20),
    lost_time VARCHAR(100),
    reward DOUBLE DEFAULT 0,
    found_time VARCHAR(100),
    status_description VARCHAR(200),
    FOREIGN KEY (publisher_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 认领记录表
CREATE TABLE IF NOT EXISTS claim_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT,
    item_name VARCHAR(100),
    claimer_id INT,
    claimer_name VARCHAR(50),
    claim_reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    claim_time DATETIME,
    process_time DATETIME,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE SET NULL,
    FOREIGN KEY (claimer_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 通知表
CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    title VARCHAR(100),
    content TEXT,
    type VARCHAR(20),
    is_read BOOLEAN DEFAULT FALSE,
    create_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入默认管理员账户 (密码: admin123, BCrypt加密)
INSERT INTO users (username, password, phone, email, role, create_time, post_count, manage_count)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138000', 'admin@lostfound.com', 'ADMIN', NOW(), 0, 0)
ON DUPLICATE KEY UPDATE id=id;
