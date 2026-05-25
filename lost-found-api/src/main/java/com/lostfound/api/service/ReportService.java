package com.lostfound.api.service;

import com.lostfound.api.model.dto.ReportRequest;
import com.lostfound.api.model.entity.Item;
import com.lostfound.api.model.entity.Report;
import com.lostfound.api.model.entity.User;
import com.lostfound.api.repository.ItemRepository;
import com.lostfound.api.repository.ReportRepository;
import com.lostfound.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ReportService(ReportRepository reportRepository, ItemRepository itemRepository,
                         UserRepository userRepository, NotificationService notificationService) {
        this.reportRepository = reportRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Report createReport(ReportRequest request, String username) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("物品不存在"));
        User reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Report report = new Report();
        report.setItem(item);
        report.setReporter(reporter);
        report.setReportType(request.getReportType());
        report.setReason(request.getReason());

        return reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Report> getPendingReports() {
        return reportRepository.findByStatus("PENDING");
    }

    @Transactional(readOnly = true)
    public Report getReportById(Integer id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("举报记录不存在"));
    }

    @Transactional
    public Report resolveReport(Integer id, String adminNote, String username) {
        Report report = getReportById(id);
        User operator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (!operator.isAdmin()) throw new RuntimeException("仅管理员可处理举报");

        report.setStatus("RESOLVED");
        report.setAdminNote(adminNote);
        report.setProcessTime(LocalDateTime.now());

        // 通知举报人
        notificationService.createNotification(
                report.getReporter().getId(),
                "举报已处理",
                "您对物品「" + report.getItem().getName() + "」的举报已处理",
                "REPORT"
        );

        return reportRepository.save(report);
    }

    @Transactional
    public Report dismissReport(Integer id, String adminNote, String username) {
        Report report = getReportById(id);
        User operator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (!operator.isAdmin()) throw new RuntimeException("仅管理员可处理举报");

        report.setStatus("DISMISSED");
        report.setAdminNote(adminNote);
        report.setProcessTime(LocalDateTime.now());

        return reportRepository.save(report);
    }
}
