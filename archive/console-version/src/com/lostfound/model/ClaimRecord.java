package com.lostfound.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 认领记录类 - 展示类的组合和关联关系
 */
public class ClaimRecord {
    public enum ClaimStatus {
        PENDING("待审核"), APPROVED("已通过"), REJECTED("已拒绝");

        private final String description;

        ClaimStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private int id;
    private int itemId;
    private String itemName;
    private int claimerId;
    private String claimerName;
    private String claimReason;
    private ClaimStatus status;
    private LocalDateTime claimTime;
    private LocalDateTime processTime;

    public ClaimRecord(int itemId, String itemName, int claimerId, String claimerName, String claimReason) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.claimerId = claimerId;
        this.claimerName = claimerName;
        this.claimReason = claimReason;
        this.status = ClaimStatus.PENDING;
        this.claimTime = LocalDateTime.now();
    }

    public ClaimRecord(int id, int itemId, String itemName, int claimerId, String claimerName,
                       String claimReason, ClaimStatus status, LocalDateTime claimTime, LocalDateTime processTime) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.claimerId = claimerId;
        this.claimerName = claimerName;
        this.claimReason = claimReason;
        this.status = status;
        this.claimTime = claimTime;
        this.processTime = processTime;
    }

    public void approve() {
        this.status = ClaimStatus.APPROVED;
        this.processTime = LocalDateTime.now();
    }

    public void reject() {
        this.status = ClaimStatus.REJECTED;
        this.processTime = LocalDateTime.now();
    }

    public String getFormattedClaimTime() {
        return claimTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getFormattedProcessTime() {
        if (processTime == null) return "未处理";
        return processTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("认领记录{id=%d, 物品='%s', 认领者='%s', 理由='%s', 状态=%s, 时间=%s}",
                id, itemName, claimerName, claimReason, status.getDescription(), getFormattedClaimTime());
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getClaimerId() { return claimerId; }
    public String getClaimerName() { return claimerName; }
    public String getClaimReason() { return claimReason; }
    public ClaimStatus getStatus() { return status; }
    public LocalDateTime getClaimTime() { return claimTime; }
    public LocalDateTime getProcessTime() { return processTime; }
}
