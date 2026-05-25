package com.lostfound.model;

import java.time.LocalDateTime;

/**
 * 失物类 - 继承Item抽象类，展示继承和多态
 */
public class LostItem extends Item {
    private String lostTime;
    private double reward;

    public LostItem(String name, String description, String category, String location,
                    int publisherId, String publisherName, String lostTime, double reward) {
        super(name, description, category, location, publisherId, publisherName);
        this.lostTime = lostTime;
        this.reward = reward;
    }

    public LostItem(int id, String name, String description, String category, String location,
                    LocalDateTime time, int publisherId, String publisherName, ItemStatus status,
                    String lostTime, double reward) {
        super(id, name, description, category, location, time, publisherId, publisherName, status);
        this.lostTime = lostTime;
        this.reward = reward;
    }

    @Override
    public String getItemType() {
        return "失物";
    }

    @Override
    public String getDetailInfo() {
        return String.format("失物详情 - 名称:%s, 描述:%s, 类别:%s, 丢失地点:%s, " +
                        "丢失时间:%s, 悬赏金额:%.2f元, 发布者:%s, 状态:%s",
                name, description, category, location, lostTime, reward,
                publisherName, status.getDescription());
    }

    @Override
    public String toString() {
        return String.format("[失物] %s - %s | 地点:%s | 丢失时间:%s | 悬赏:%.2f元 | 状态:%s",
                name, description, location, lostTime, reward, status.getDescription());
    }

    // Getters and Setters
    public String getLostTime() { return lostTime; }
    public void setLostTime(String lostTime) { this.lostTime = lostTime; }
    public double getReward() { return reward; }
    public void setReward(double reward) { this.reward = reward; }
}
