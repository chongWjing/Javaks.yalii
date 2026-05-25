package com.lostfound.api.repository;

import com.lostfound.api.model.entity.ClaimRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRecordRepository extends JpaRepository<ClaimRecord, Integer> {
    List<ClaimRecord> findByClaimerId(Integer claimerId);
    List<ClaimRecord> findByItemId(Integer itemId);
    List<ClaimRecord> findByStatus(String status);
    long countByStatus(String status);
}
