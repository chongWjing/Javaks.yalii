package com.lostfound.api.service;

import com.lostfound.api.model.entity.ClaimRecord;
import com.lostfound.api.model.entity.Item;
import com.lostfound.api.model.entity.User;
import com.lostfound.api.model.dto.ClaimRequest;
import com.lostfound.api.repository.ClaimRecordRepository;
import com.lostfound.api.repository.ItemRepository;
import com.lostfound.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimService {

    private final ClaimRecordRepository claimRecordRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ClaimService(ClaimRecordRepository claimRecordRepository,
                       ItemRepository itemRepository,
                       UserRepository userRepository,
                       NotificationService notificationService) {
        this.claimRecordRepository = claimRecordRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public ClaimRecord createClaim(ClaimRequest request, String username) {
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("物品不存在"));

        User claimer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (item.getPublisher().getId().equals(claimer.getId())) {
            throw new RuntimeException("不能认领自己发布的物品");
        }

        ClaimRecord claim = new ClaimRecord();
        claim.setItem(item);
        claim.setItemName(item.getName());
        claim.setClaimer(claimer);
        claim.setClaimerName(claimer.getUsername());
        claim.setClaimReason(request.getClaimReason());
        claim.setStatus("PENDING");
        claim.setClaimTime(LocalDateTime.now());

        ClaimRecord saved = claimRecordRepository.save(claim);

        notificationService.createNotification(
                item.getPublisher().getId(),
                "新的认领申请",
                "用户 " + claimer.getUsername() + " 申请认领您的物品「" + item.getName() + "」",
                "CLAIM"
        );

        return saved;
    }

    @Transactional(readOnly = true)
    public List<ClaimRecord> getAllClaims() {
        return claimRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ClaimRecord getClaimById(Integer id) {
        return claimRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("认领记录不存在"));
    }

    @Transactional(readOnly = true)
    public List<ClaimRecord> getClaimsByUser(Integer userId) {
        return claimRecordRepository.findByClaimerId(userId);
    }

    @Transactional(readOnly = true)
    public List<ClaimRecord> getPendingClaims() {
        return claimRecordRepository.findByStatus("PENDING");
    }

    @Transactional
    public ClaimRecord approveClaim(Integer id, String username) {
        ClaimRecord claim = getClaimById(id);
        User operator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Item item = claim.getItem();
        if (!item.getPublisher().getId().equals(operator.getId()) && !operator.isAdmin()) {
            throw new RuntimeException("无权审批此认领申请");
        }

        claim.setStatus("APPROVED");
        claim.setProcessTime(LocalDateTime.now());

        item.setStatus("CLAIMED");
        itemRepository.save(item);

        ClaimRecord saved = claimRecordRepository.save(claim);

        notificationService.createNotification(
                claim.getClaimer().getId(),
                "认领申请已批准",
                "您对物品「" + item.getName() + "」的认领申请已被批准",
                "CLAIM"
        );

        return saved;
    }

    @Transactional
    public ClaimRecord rejectClaim(Integer id, String username) {
        ClaimRecord claim = getClaimById(id);
        User operator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Item item = claim.getItem();
        if (!item.getPublisher().getId().equals(operator.getId()) && !operator.isAdmin()) {
            throw new RuntimeException("无权审批此认领申请");
        }

        claim.setStatus("REJECTED");
        claim.setProcessTime(LocalDateTime.now());

        ClaimRecord saved = claimRecordRepository.save(claim);

        notificationService.createNotification(
                claim.getClaimer().getId(),
                "认领申请已拒绝",
                "您对物品「" + item.getName() + "」的认领申请已被拒绝",
                "CLAIM"
        );

        return saved;
    }

    @Transactional
    public void deleteClaim(Integer id, String username) {
        ClaimRecord claim = getClaimById(id);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!claim.getClaimer().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new RuntimeException("无权删除此认领记录");
        }

        if ("PENDING".equals(claim.getStatus())) {
            claimRecordRepository.deleteById(id);
        } else {
            throw new RuntimeException("已处理的认领记录不能删除");
        }
    }
}
