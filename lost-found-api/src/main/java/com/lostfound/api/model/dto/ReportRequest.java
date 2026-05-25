package com.lostfound.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReportRequest {
    @NotNull(message = "物品ID不能为空")
    private Integer itemId;

    @NotBlank(message = "举报类型不能为空")
    private String reportType;

    @NotBlank(message = "举报原因不能为空")
    private String reason;

    public ReportRequest() {}

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
