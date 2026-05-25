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
    image_urls TEXT,
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
    contact_info VARCHAR(200),
    evidence_urls TEXT,
    review_note TEXT,
    reject_reason VARCHAR(500),
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

-- 举报表
CREATE TABLE IF NOT EXISTS reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT,
    reporter_id INT,
    report_type VARCHAR(50) NOT NULL,
    reason TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    admin_note TEXT,
    create_time DATETIME,
    process_time DATETIME,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据库索引
CREATE INDEX idx_items_item_type ON items(item_type);
CREATE INDEX idx_items_status ON items(status);
CREATE INDEX idx_items_category ON items(category);
CREATE INDEX idx_items_time ON items(time);
CREATE INDEX idx_items_publisher_id ON items(publisher_id);
CREATE INDEX idx_claims_status ON claim_records(status);
CREATE INDEX idx_claims_claimer_id ON claim_records(claimer_id);
CREATE INDEX idx_claims_item_id ON claim_records(item_id);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_reports_item_id ON reports(item_id);
