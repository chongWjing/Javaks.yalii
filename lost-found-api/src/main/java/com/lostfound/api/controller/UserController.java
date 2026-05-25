package com.lostfound.api.controller;

import com.lostfound.api.model.dto.ApiResponse;
import com.lostfound.api.model.dto.UserRequest;
import com.lostfound.api.model.entity.User;
import com.lostfound.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequest request,
            Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        if (!currentUser.getId().equals(id) && !currentUser.isAdmin()) {
            return ResponseEntity.status(403).body(ApiResponse.error("无权修改此用户信息"));
        }
        User user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(user, "用户信息更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Integer id,
            Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        if (!currentUser.isAdmin()) {
            return ResponseEntity.status(403).body(ApiResponse.error("仅管理员可删除用户"));
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "用户删除成功"));
    }
}
