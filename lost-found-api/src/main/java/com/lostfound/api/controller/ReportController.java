package com.lostfound.api.controller;

import com.lostfound.api.model.dto.ApiResponse;
import com.lostfound.api.model.dto.ReportRequest;
import com.lostfound.api.model.entity.Report;
import com.lostfound.api.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Report>> createReport(
            @Valid @RequestBody ReportRequest request,
            Authentication authentication) {
        Report report = reportService.createReport(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(report, "举报提交成功"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Report>>> getAllReports() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getAllReports()));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<Report>>> getPendingReports() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getPendingReports()));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<Report>> resolveReport(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        Report report = reportService.resolveReport(id, body.get("adminNote"), authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(report, "举报已处理"));
    }

    @PutMapping("/{id}/dismiss")
    public ResponseEntity<ApiResponse<Report>> dismissReport(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        Report report = reportService.dismissReport(id, body.get("adminNote"), authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(report, "举报已驳回"));
    }
}
