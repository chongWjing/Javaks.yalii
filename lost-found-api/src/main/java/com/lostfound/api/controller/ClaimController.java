package com.lostfound.api.controller;

import com.lostfound.api.model.dto.ApiResponse;
import com.lostfound.api.model.dto.ClaimRequest;
import com.lostfound.api.model.entity.ClaimRecord;
import com.lostfound.api.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "http://localhost:3000")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClaimRecord>>> getAllClaims() {
        List<ClaimRecord> claims = claimService.getAllClaims();
        return ResponseEntity.ok(ApiResponse.success(claims));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClaimRecord>> getClaimById(@PathVariable Integer id) {
        ClaimRecord claim = claimService.getClaimById(id);
        return ResponseEntity.ok(ApiResponse.success(claim));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClaimRecord>> createClaim(
            @Valid @RequestBody ClaimRequest request,
            Authentication authentication) {
        ClaimRecord claim = claimService.createClaim(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(claim, "认领申请提交成功"));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<ClaimRecord>>> getPendingClaims() {
        List<ClaimRecord> claims = claimService.getPendingClaims();
        return ResponseEntity.ok(ApiResponse.success(claims));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ClaimRecord>>> getClaimsByUser(@PathVariable Integer userId) {
        List<ClaimRecord> claims = claimService.getClaimsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(claims));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ClaimRecord>> approveClaim(
            @PathVariable Integer id,
            Authentication authentication) {
        ClaimRecord claim = claimService.approveClaim(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(claim, "认领已批准"));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ClaimRecord>> rejectClaim(
            @PathVariable Integer id,
            Authentication authentication) {
        ClaimRecord claim = claimService.rejectClaim(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(claim, "认领已拒绝"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClaim(
            @PathVariable Integer id,
            Authentication authentication) {
        claimService.deleteClaim(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "认领记录删除成功"));
    }
}
