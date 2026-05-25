package com.lostfound.api.model.dto;

import jakarta.validation.constraints.NotNull;

public class ClaimRequest {
    @NotNull(message = "物品ID不能为空")
    private Integer itemId;

    private String claimReason;

    public ClaimRequest() {}

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public String getClaimReason() { return claimReason; }
    public void setClaimReason(String claimReason) { this.claimReason = claimReason; }
}
