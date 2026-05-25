package com.lostfound.callback;

import com.lostfound.api.DatabaseCallback;
import com.lostfound.model.Notification;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知回调类 - 实现DatabaseCallback接口，展示接口回调
 * 用于处理操作完成后发送通知
 */
public class NotificationCallback implements DatabaseCallback<String> {
    private int userId;
    private List<Notification> notifications;
    private Notification.NotificationType notificationType;

    public NotificationCallback(int userId, Notification.NotificationType notificationType) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.notifications = new ArrayList<>();
    }

    @Override
    public void onSuccess(String result) {
        Notification notification = new Notification(userId, "操作成功", result, notificationType);
        notifications.add(notification);
        System.out.println("[通知] " + notification);
    }

    @Override
    public void onError(String errorMessage) {
        Notification notification = new Notification(userId, "操作失败", errorMessage,
                Notification.NotificationType.SYSTEM);
        notifications.add(notification);
        System.err.println("[通知] " + notification);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
