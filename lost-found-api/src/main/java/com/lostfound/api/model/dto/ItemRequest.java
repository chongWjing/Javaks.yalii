package com.lostfound.api.model.dto;

import jakarta.validation.constraints.NotBlank;

public class ItemRequest {
    @NotBlank(message = "物品名称不能为空")
    private String name;

    private String description;
    private String category;
    private String location;
    private String itemType;

    // For lost items
    private String lostTime;
    private Double reward;

    // For found items
    private String foundTime;
    private String statusDescription;

    public ItemRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getLostTime() { return lostTime; }
    public void setLostTime(String lostTime) { this.lostTime = lostTime; }

    public Double getReward() { return reward; }
    public void setReward(Double reward) { this.reward = reward; }

    public String getFoundTime() { return foundTime; }
    public void setFoundTime(String foundTime) { this.foundTime = foundTime; }

    public String getStatusDescription() { return statusDescription; }
    public void setStatusDescription(String statusDescription) { this.statusDescription = statusDescription; }
}
