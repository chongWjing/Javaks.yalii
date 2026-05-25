package com.lostfound.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 通知消息类 - 用于系统通知和消息推送
 */
public class Notification {
    public enum NotificationType {
        SYSTEM("系统通知"), CLAIM("认领通知"), MATCH("匹配通知"), ADMIN("管理通知");

        private final String description;

        NotificationType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private int id;
    private int userId;
    private String title;
    private String content;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createTime;

    public Notification(int userId, String title, String content, NotificationType type) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.isRead = false;
        this.createTime = LocalDateTime.now();
    }

    public Notification(int id, int userId, String title, String content,
                        NotificationType type, boolean isRead, LocalDateTime createTime) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.isRead = isRead;
        this.createTime = createTime;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public String getFormattedTime() {
        return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s) %s",
                type.getDescription(), title, content, getFormattedTime(), isRead ? "已读" : "未读");
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public NotificationType getType() { return type; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreateTime() { return createTime; }
}
