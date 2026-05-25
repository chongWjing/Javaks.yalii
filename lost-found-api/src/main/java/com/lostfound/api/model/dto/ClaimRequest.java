package com.lostfound.api.model.dto;

import jakarta.validation.constraints.NotNull;

public class ClaimRequest {
    @NotNull(message = "物品ID不能为空")
    private Integer itemId;

    private String claimReason;
    private String contactInfo;
    private String evidenceUrls;

    public ClaimRequest() {}

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public String getClaimReason() { return claimReason; }
    public void setClaimReason(String claimReason) { this.claimReason = claimReason; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getEvidenceUrls() { return evidenceUrls; }
    public void setEvidenceUrls(String evidenceUrls) { this.evidenceUrls = evidenceUrls; }
}
