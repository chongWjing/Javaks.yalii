package com.lostfound.api.repository;

import com.lostfound.api.model.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Item> findByFilters(@Param("type") String type,
                             @Param("status") String status,
                             @Param("keyword") String keyword,
                             @Param("category") String category);

    @Query("SELECT i FROM Item i WHERE " +
           "(:type IS NULL OR i.itemType = :type) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:category IS NULL OR i.category = :category) AND " +
           "(:keyword IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Item> findByFiltersPaged(@Param("type") String type,
                                  @Param("status") String status,
                                  @Param("keyword") String keyword,
                                  @Param("category") String category,
                                  Pageable pageable);
}
