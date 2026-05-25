-- 数据库索引优化
CREATE INDEX idx_items_item_type ON items(item_type);
CREATE INDEX idx_items_status ON items(status);
CREATE INDEX idx_items_category ON items(category);
CREATE INDEX idx_items_time ON items(time);
CREATE INDEX idx_items_publisher_id ON items(publisher_id);
CREATE INDEX idx_items_location ON items(location(50));

CREATE INDEX idx_claims_status ON claim_records(status);
CREATE INDEX idx_claims_claimer_id ON claim_records(claimer_id);
CREATE INDEX idx_claims_item_id ON claim_records(item_id);
CREATE INDEX idx_claims_claim_time ON claim_records(claim_time);

CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_create_time ON notifications(create_time);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);

-- 认领流程增强字段
ALTER TABLE claim_records ADD COLUMN contact_info VARCHAR(200) COMMENT '联系方式';
ALTER TABLE claim_records ADD COLUMN evidence_urls TEXT COMMENT '证据图片URLs';
ALTER TABLE claim_records ADD COLUMN review_note TEXT COMMENT '审核备注';
ALTER TABLE claim_records ADD COLUMN reject_reason VARCHAR(500) COMMENT '拒绝原因';

-- 物品举报表
CREATE TABLE IF NOT EXISTS reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT,
    reporter_id INT,
    report_type VARCHAR(50) NOT NULL COMMENT '举报类型: FAKE/DUPLICATE/INAPPROPRIATE/OTHER',
    reason TEXT NOT NULL COMMENT '举报原因',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态: PENDING/RESOLVED/DISMISSED',
    admin_note TEXT COMMENT '管理员处理备注',
    create_time DATETIME,
    process_time DATETIME,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_reports_item_id ON reports(item_id);
