package com.lostfound.api.controller;

import com.lostfound.api.model.dto.ApiResponse;
import com.lostfound.api.model.dto.ItemRequest;
import com.lostfound.api.model.entity.Item;
import com.lostfound.api.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:3000")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Item>>> getAllItems(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        List<Item> items = itemService.getAllItems(type, status, keyword, category);
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllItemsPaged(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Item> itemPage = itemService.getAllItemsPaged(type, status, keyword, category, page, size);
        Map<String, Object> result = new HashMap<>();
        result.put("content", itemPage.getContent());
        result.put("totalElements", itemPage.getTotalElements());
        result.put("totalPages", itemPage.getTotalPages());
        result.put("currentPage", itemPage.getNumber());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Item>> getItemById(@PathVariable Integer id) {
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Item>> createItem(
            @Valid @RequestBody ItemRequest request,
            Authentication authentication) {
        Item item = itemService.createItem(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(item, "物品发布成功"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Item>> updateItem(
            @PathVariable Integer id,
            @Valid @RequestBody ItemRequest request,
            Authentication authentication) {
        Item item = itemService.updateItem(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(item, "物品更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable Integer id,
            Authentication authentication) {
        itemService.deleteItem(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "物品删除成功"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Item>>> getItemsByUser(@PathVariable Integer userId) {
        List<Item> items = itemService.getItemsByPublisher(userId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Item>> updateItemStatus(
            @PathVariable Integer id,
            @RequestParam String status,
            Authentication authentication) {
        Item item = itemService.updateItemStatus(id, status, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(item, "状态更新成功"));
    }
}
