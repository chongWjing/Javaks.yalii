package com.lostfound.api.repository;

import com.lostfound.api.model.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByStatus(String status);
    List<Report> findByItemId(Integer itemId);
    List<Report> findByReporterId(Integer reporterId);
    long countByStatus(String status);
}
