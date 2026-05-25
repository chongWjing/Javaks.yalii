package com.lostfound.api.repository;

import com.lostfound.api.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdOrderByCreateTimeDesc(Integer userId);
    long countByUserIdAndIsReadFalse(Integer userId);
}
