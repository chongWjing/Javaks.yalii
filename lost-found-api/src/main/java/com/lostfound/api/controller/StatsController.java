package com.lostfound.api.controller;

import com.lostfound.api.model.dto.ApiResponse;
import com.lostfound.api.repository.ClaimRecordRepository;
import com.lostfound.api.repository.ItemRepository;
import com.lostfound.api.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "http://localhost:3000")
public class StatsController {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ClaimRecordRepository claimRecordRepository;

    public StatsController(UserRepository userRepository,
                          ItemRepository itemRepository,
                          ClaimRecordRepository claimRecordRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.claimRecordRepository = claimRecordRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalItems", itemRepository.count());
        stats.put("pendingClaims", claimRecordRepository.countByStatus("PENDING"));
        stats.put("approvedClaims", claimRecordRepository.countByStatus("APPROVED"));
        stats.put("totalClaims", claimRecordRepository.count());
        stats.put("lostItems", itemRepository.countByItemType("LOST"));
        stats.put("foundItems", itemRepository.countByItemType("FOUND"));
        stats.put("activeItems", itemRepository.countByStatus("ACTIVE"));
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
