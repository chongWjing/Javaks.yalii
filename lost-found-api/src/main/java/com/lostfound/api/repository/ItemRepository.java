package com.lostfound.api.repository;

import com.lostfound.api.model.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByItemType(String itemType);
    List<Item> findByStatus(String status);
    List<Item> findByPublisherId(Integer publisherId);
    long countByItemType(String itemType);
    long countByStatus(String status);

    @Query("SELECT i FROM Item i WHERE " +
           "(:type IS NULL OR i.itemType = :type) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:category IS NULL OR i.category = :category) AND " +
           "(:keyword IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:startTime IS NULL OR i.time >= :startTime) AND " +
           "(:endTime IS NULL OR i.time <= :endTime)")
    List<Item> findByFilters(@Param("type") String type,
                             @Param("status") String status,
                             @Param("keyword") String keyword,
                             @Param("category") String category,
                             @Param("startTime") LocalDateTime startTime,
                             @Param("endTime") LocalDateTime endTime);

    @Query("SELECT i FROM Item i WHERE " +
           "(:type IS NULL OR i.itemType = :type) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:category IS NULL OR i.category = :category) AND " +
           "(:keyword IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:startTime IS NULL OR i.time >= :startTime) AND " +
           "(:endTime IS NULL OR i.time <= :endTime)")
    Page<Item> findByFiltersPaged(@Param("type") String type,
                                  @Param("status") String status,
                                  @Param("keyword") String keyword,
                                  @Param("category") String category,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime,
                                  Pageable pageable);

    // 热门地点统计
    @Query("SELECT i.location, COUNT(i) as cnt FROM Item i WHERE i.location IS NOT NULL GROUP BY i.location ORDER BY cnt DESC")
    List<Object[]> findTopLocations(org.springframework.data.domain.Pageable pageable);

    // 按月统计趋势
    @Query("SELECT FUNCTION('DATE_FORMAT', i.time, '%Y-%m') as month, COUNT(i) FROM Item i GROUP BY month ORDER BY month DESC")
    List<Object[]> findMonthlyTrend();

    // 平均找回时长（已认领的物品）
    @Query("SELECT AVG(TIMESTAMPDIFF(HOUR, i.time, c.processTime)) FROM Item i JOIN ClaimRecord c ON c.item = i WHERE c.status = 'APPROVED' AND c.processTime IS NOT NULL")
    Double findAverageRecoveryHours();
}
