package com.lostfound.model;

import java.time.LocalDateTime;

/**
 * 招领物品类 - 继承Item抽象类，展示继承和多态
 */
public class FoundItem extends Item {
    private String foundTime;
    private String statusDescription;

    public FoundItem(String name, String description, String category, String location,
                     int publisherId, String publisherName, String foundTime, String statusDescription) {
        super(name, description, category, location, publisherId, publisherName);
        this.foundTime = foundTime;
        this.statusDescription = statusDescription;
    }

    public FoundItem(int id, String name, String description, String category, String location,
                     LocalDateTime time, int publisherId, String publisherName, ItemStatus status,
                     String foundTime, String statusDescription) {
        super(id, name, description, category, location, time, publisherId, publisherName, status);
        this.foundTime = foundTime;
        this.statusDescription = statusDescription;
    }

    @Override
    public String getItemType() {
        return "招领";
    }

    @Override
    public String getDetailInfo() {
        return String.format("招领详情 - 名称:%s, 描述:%s, 类别:%s, 拾获地点:%s, " +
                        "拾获时间:%s, 物品状态:%s, 发布者:%s, 状态:%s",
                name, description, category, location, foundTime,
                statusDescription, publisherName, status.getDescription());
    }

    @Override
    public String toString() {
        return String.format("[招领] %s - %s | 地点:%s | 拾获时间:%s | 状态:%s",
                name, description, location, foundTime, status.getDescription());
    }

    // Getters and Setters
    public String getFoundTime() { return foundTime; }
    public void setFoundTime(String foundTime) { this.foundTime = foundTime; }
    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
}
