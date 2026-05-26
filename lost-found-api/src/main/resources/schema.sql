-- 失物招领管理系统数据库初始化脚本
-- 数据库名: javaks2

CREATE DATABASE IF NOT EXISTS javaks2 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE javaks2;

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

-- ============================================================
-- 样本数据
-- ============================================================

-- 用户数据（密码均为 "123456" 的 BCrypt 哈希）
INSERT INTO `users` (`id`, `create_time`, `email`, `manage_count`, `password`, `phone`, `post_count`, `role`, `username`) VALUES
(2, '2026-05-20 10:00:00', 'admin@lostfound.com', 10, '$2a$10$zDnPAL8XjzvMn2ua.5fLi.rSAhmY8NtgjX5CkIsuamsw0.QJTx8A2', '13900139000', 5, 'ADMIN', 'admin'),
(3, '2026-05-21 14:30:00', 'zhangsan@example.com', 0, '$2a$10$zDnPAL8XjzvMn2ua.5fLi.rSAhmY8NtgjX5CkIsuamsw0.QJTx8A2', '13800138001', 3, 'REGULAR', '张三'),
(4, '2026-05-22 09:15:00', 'lisi@example.com', 0, '$2a$10$zDnPAL8XjzvMn2ua.5fLi.rSAhmY8NtgjX5CkIsuamsw0.QJTx8A2', '13800138002', 2, 'REGULAR', '李四'),
(5, '2026-05-23 16:45:00', 'wangwu@example.com', 0, '$2a$10$zDnPAL8XjzvMn2ua.5fLi.rSAhmY8NtgjX5CkIsuamsw0.QJTx8A2', '13800138003', 1, 'REGULAR', '王五'),
(6, '2026-05-24 11:20:00', 'zhaoliu@example.com', 0, '$2a$10$zDnPAL8XjzvMn2ua.5fLi.rSAhmY8NtgjX5CkIsuamsw0.QJTx8A2', '13800138004', 2, 'REGULAR', '赵六'),
(7, '2026-05-25 09:28:26', 'admin2@lostfound.com', 0, '$2a$10$zDnPAL8XjzvMn2ua.5fLi.rSAhmY8NtgjX5CkIsuamsw0.QJTx8A2', '13900139001', 0, 'ADMIN', 'admin2');

-- 物品数据
INSERT INTO `items` (`id`, `category`, `description`, `found_time`, `item_type`, `location`, `lost_time`, `name`, `publisher_name`, `reward`, `status`, `status_description`, `time`, `publisher_id`, `image_urls`) VALUES
(1, '证件', '黑色皮质钱包，内有身份证和银行卡，姓名：张三', NULL, 'LOST', '图书馆三楼阅览室', '2026-05-22 09:00', '黑色钱包', '张三', 50, 'ACTIVE', NULL, '2026-05-22 10:30:00', 2, NULL),
(2, '电子', 'MacBook Pro 14寸，银色，机身上有贴纸', NULL, 'LOST', '教学楼A201教室', '2026-05-23 13:30', '苹果笔记本电脑', '张三', 200, 'ACTIVE', NULL, '2026-05-23 14:20:00', 2, NULL),
(3, '日用', '蓝色折叠雨伞，手柄有磨损', NULL, 'LOST', '食堂一楼入口', '2026-05-24 07:45', '蓝色雨伞', '李四', 0, 'ACTIVE', NULL, '2026-05-24 08:15:00', 3, NULL),
(4, '证件', '学生证，姓名：李四，学号：2024001', NULL, 'LOST', '操场跑道', '2026-05-24 15:00', '学生证', '李四', 30, 'ACTIVE', NULL, '2026-05-24 16:30:00', 3, NULL),
(5, '电子', 'AirPods Pro二代，白色充电盒', NULL, 'LOST', '图书馆二楼自习区', '2026-05-25 08:30', 'AirPods耳机', '王五', 100, 'ACTIVE', NULL, '2026-05-25 09:00:00', 4, NULL),
(6, '日用', '一串钥匙，共5把，带有蓝色钥匙扣', '2026-05-22 10:45', 'FOUND', '教学楼B102教室', NULL, '钥匙串', '赵六', NULL, 'ACTIVE', '完好', '2026-05-22 11:00:00', 5, NULL),
(7, '日用', '红色保温杯，品牌：膳魔师，有轻微凹痕', '2026-05-23 15:00', 'FOUND', '图书馆一楼饮水机旁', NULL, '红色水杯', '赵六', NULL, 'ACTIVE', '轻微损坏', '2026-05-23 15:30:00', 5, NULL),
(8, '日用', '黑色金属框近视眼镜，度数约400度', '2026-05-24 11:45', 'FOUND', '食堂二楼座位', NULL, '眼镜', '张三', NULL, 'ACTIVE', '完好', '2026-05-24 12:00:00', 2, NULL),
(9, '电子', '32GB U盘，品牌：金士顿，黑色', '2026-05-25 09:45', 'FOUND', '机房301门口', NULL, 'U盘', '李四', NULL, 'ACTIVE', '完好', '2026-05-25 10:00:00', 3, NULL),
(10, '其他', '《高等数学》第七版，上有笔记', '2026-05-25 13:30', 'FOUND', '教学楼A105教室', NULL, '课本', '王五', NULL, 'ACTIVE', '完好', '2026-05-25 14:00:00', 4, NULL);

-- 认领记录
INSERT INTO `claim_records` (`id`, `claim_reason`, `claim_time`, `claimer_name`, `item_name`, `process_time`, `status`, `claimer_id`, `item_id`, `contact_info`, `evidence_urls`, `review_note`, `reject_reason`) VALUES
(1, '这是我的钥匙串，蓝色钥匙扣是我女朋友送的，上面有5把钥匙', '2026-05-22 14:00:00', '张三', '钥匙串', '2026-05-22 15:30:00', 'APPROVED', 2, 6, NULL, NULL, NULL, NULL),
(2, '这是我的膳魔师杯子，上周在图书馆丢的', '2026-05-24 09:00:00', '李四', '红色水杯', NULL, 'PENDING', 3, 7, NULL, NULL, NULL, NULL),
(3, '我也有一个黑色钱包，想确认一下是不是我的', '2026-05-23 10:00:00', '王五', '黑色钱包', '2026-05-23 11:00:00', 'REJECTED', 4, 1, NULL, NULL, NULL, NULL),
(4, '我的眼镜度数是400度，黑色金属框，应该是我的', '2026-05-25 08:00:00', '赵六', '眼镜', NULL, 'PENDING', 5, 8, NULL, NULL, NULL, NULL);

-- 通知数据
INSERT INTO `notifications` (`id`, `content`, `create_time`, `is_read`, `title`, `type`, `user_id`) VALUES
(1, '您对物品「钥匙串」的认领申请已被批准，请尽快取回', '2026-05-22 15:30:00', 1, '认领申请已批准', 'CLAIM', 2),
(2, '用户 赵六 申请认领您的物品「眼镜」', '2026-05-25 08:00:00', 0, '新的认领申请', 'CLAIM', 3),
(3, '您对物品「黑色钱包」的认领申请已被拒绝', '2026-05-23 11:00:00', 1, '认领申请已拒绝', 'CLAIM', 4),
(4, '用户 李四 申请认领您的物品「红色水杯」', '2026-05-24 09:00:00', 0, '新的认领申请', 'CLAIM', 5),
(5, '欢迎使用失物招领系统！如有问题请联系管理员', '2026-05-20 10:00:00', 1, '系统通知', 'SYSTEM', 2),
(6, '欢迎使用失物招领系统！如有问题请联系管理员', '2026-05-22 09:15:00', 1, '系统通知', 'SYSTEM', 3),
(7, '欢迎使用失物招领系统！如有问题请联系管理员', '2026-05-23 16:45:00', 0, '系统通知', 'SYSTEM', 4),
(8, '欢迎使用失物招领系统！如有问题请联系管理员', '2026-05-24 11:20:00', 0, '系统通知', 'SYSTEM', 5);

-- 举报数据
INSERT INTO `reports` (`id`, `item_id`, `reporter_id`, `report_type`, `reason`, `status`, `admin_note`, `create_time`, `process_time`) VALUES
(1, 2, 3, '信息不实', '这台电脑看起来是偷的，价格远低于市场价', 'RESOLVED', '经核实，该物品为失主发布，信息属实', '2026-05-23 15:00:00', '2026-05-23 17:30:00'),
(2, 5, 5, '重复发布', '同一副耳机在另一个帖子也看到了', 'DISMISSED', '未发现重复发布，驳回', '2026-05-25 10:00:00', '2026-05-25 11:00:00'),
(3, 1, 6, '信息不实', '这个钱包我见过不是他的', 'PENDING', NULL, '2026-05-26 08:00:00', NULL),
(4, 3, 4, '其他', '雨伞图片和实物不符', 'PENDING', NULL, '2026-05-26 09:00:00', NULL);
