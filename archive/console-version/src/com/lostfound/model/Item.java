package com.lostfound.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 物品抽象类 - 展示抽象类和多态
 * 所有物品类型的基类
 */
public abstract class Item {
    protected int id;
    protected String name;
    protected String description;
    protected String category;
    protected String location;
    protected LocalDateTime time;
    protected int publisherId;
    protected String publisherName;
    protected ItemStatus status;

    public enum ItemStatus {
        ACTIVE("待认领"), CLAIMED("已认领"), CLOSED("已关闭");

        private final String description;

        ItemStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public Item(String name, String description, String category, String location,
                int publisherId, String publisherName) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.location = location;
        this.time = LocalDateTime.now();
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.status = ItemStatus.ACTIVE;
    }

    public Item(int id, String name, String description, String category, String location,
                LocalDateTime time, int publisherId, String publisherName, ItemStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.location = location;
        this.time = time;
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.status = status;
    }

    // 抽象方法 - 子类必须实现
    public abstract String getItemType();
    public abstract String getDetailInfo();

    // 普通方法 - 子类可选择性重写（多态）
    public boolean isMatchable() {
        return status == ItemStatus.ACTIVE;
    }

    public String getFormattedTime() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s | 类别:%s | 地点:%s | 状态:%s | 发布者:%s",
                getItemType(), name, description, category, location,
                status.getDescription(), publisherName);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getTime() { return time; }
    public int getPublisherId() { return publisherId; }
    public String getPublisherName() { return publisherName; }
    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }
}
