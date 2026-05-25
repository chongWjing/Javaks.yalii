package com.lostfound.api.service;

import com.lostfound.api.model.entity.Notification;
import com.lostfound.api.model.entity.User;
import com.lostfound.api.repository.NotificationRepository;
import com.lostfound.api.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Notification createNotification(Integer userId, String title, String content, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreateTime(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);

        // WebSocket 实时推送
        Map<String, Object> wsMessage = new HashMap<>();
        wsMessage.put("id", saved.getId());
        wsMessage.put("title", title);
        wsMessage.put("content", content);
        wsMessage.put("type", type);
        wsMessage.put("createTime", saved.getCreateTime());
        wsMessage.put("unreadCount", notificationRepository.countByUserIdAndIsReadFalse(userId));

        messagingTemplate.convertAndSendToUser(
                userId.toString(), "/notifications", wsMessage);

        return saved;
    }

    public List<Notification> getUserNotifications(Integer userId) {
        return notificationRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public long getUnreadCount(Integer userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    public Notification markAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知不存在"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Integer id, String username) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知不存在"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!notification.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new RuntimeException("无权删除此通知");
        }

        notificationRepository.deleteById(id);
    }

    public void markAllAsRead(Integer userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreateTimeDesc(userId);
        for (Notification n : notifications) {
            if (!n.getRead()) {
                n.setRead(true);
            }
        }
        notificationRepository.saveAll(notifications);
    }
}
