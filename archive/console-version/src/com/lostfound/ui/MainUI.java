package com.lostfound.ui;

import com.lostfound.model.*;
import com.lostfound.db.DataManager;
import com.lostfound.service.ItemMatchService;
import com.lostfound.callback.LoggingCallback;
import com.lostfound.callback.NotificationCallback;
import com.lostfound.api.OperationListener;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 主界面类 - 控制台交互界面
 * 实现OperationListener接口，展示接口回调和观察者模式
 */
public class MainUI implements OperationListener {
    private Scanner scanner;
    private DataManager dataManager;
    private User currentUser;
    private ItemMatchService matchService;

    public MainUI() {
        this.scanner = new Scanner(System.in);
        this.dataManager = DataManager.getInstance();
        this.matchService = new ItemMatchService();
    }

    // ==================== OperationListener接口实现 ====================

    @Override
    public void onOperationStart(String operation) {
        System.out.println("[系统] 开始执行: " + operation);
    }

    @Override
    public void onOperationComplete(String operation, boolean success, String message) {
        if (success) {
            System.out.println("[系统] " + operation + " 完成: " + message);
        } else {
            System.err.println("[系统] " + operation + " 失败: " + message);
        }
    }

    @Override
    public void onDataUpdate(String dataType, int count) {
        System.out.println("[系统] 数据更新: " + dataType + " 共 " + count + " 条");
    }

    // ==================== 主菜单 ====================

    public void start() {
        System.out.println("========================================");
        System.out.println("      失物招领管理系统 v1.0");
        System.out.println("========================================");

        while (true) {
            showMainMenu();
            int choice = readInt("请选择操作: ");

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 0:
                    System.out.println("感谢使用，再见！");
                    return;
                default:
                    System.out.println("无效选择，请重试");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- 主菜单 ---");
        System.out.println("1. 用户登录");
        System.out.println("2. 用户注册");
        System.out.println("0. 退出系统");
    }

    // ==================== 登录注册 ====================

    private void login() {
        System.out.println("\n--- 用户登录 ---");
        String username = readString("请输入用户名: ");
        String password = readString("请输入密码: ");

        User user = dataManager.login(username, password);
        if (user != null) {
            this.currentUser = user;
            System.out.println("登录成功！欢迎 " + user.getUsername() + " (" + user.getRole() + ")");
            onOperationComplete("用户登录", true, "欢迎回来");
            showUserMenu();
        } else {
            System.out.println("用户名或密码错误");
            onOperationComplete("用户登录", false, "用户名或密码错误");
        }
    }

    private void register() {
        System.out.println("\n--- 用户注册 ---");
        String username = readString("请输入用户名: ");

        if (dataManager.isUsernameExists(username)) {
            System.out.println("用户名已存在，请重新输入");
            return;
        }

        String password = readString("请输入密码: ");
        String phone = readString("请输入手机号: ");
        String email = readString("请输入邮箱: ");

        User newUser = new RegularUser(username, password, phone, email);
        dataManager.addUser(newUser, new LoggingCallback<User>("用户注册") {
            @Override
            public void onSuccess(User result) {
                System.out.println("注册成功！您的ID是: " + result.getId());
                MainUI.this.onOperationComplete("用户注册", true, "注册成功");
            }

            @Override
            public void onError(String errorMessage) {
                System.err.println("注册失败: " + errorMessage);
                MainUI.this.onOperationComplete("用户注册", false, errorMessage);
            }
        });
    }

    // ==================== 用户菜单 ====================

    private void showUserMenu() {
        while (currentUser != null) {
            System.out.println("\n--- " + currentUser.getUsername() + " 的菜单 (" + currentUser.getRole() + ") ---");
            System.out.println("1. 发布失物信息");
            System.out.println("2. 发布招领信息");
            System.out.println("3. 查看所有物品");
            System.out.println("4. 搜索物品");
            System.out.println("5. 物品智能匹配");
            System.out.println("6. 申请认领物品");
            System.out.println("7. 查看我的发布");
            System.out.println("8. 查看我的通知");
            System.out.println("9. 个人信息");

            if (currentUser instanceof Admin) {
                showAdminMenu();
            }

            System.out.println("0. 退出登录");

            int choice = readInt("请选择操作: ");

            switch (choice) {
                case 1: publishLostItem(); break;
                case 2: publishFoundItem(); break;
                case 3: viewAllItems(); break;
                case 4: searchItems(); break;
                case 5: smartMatch(); break;
                case 6: claimItem(); break;
                case 7: viewMyItems(); break;
                case 8: viewNotifications(); break;
                case 9: viewProfile(); break;
                case 0:
                    currentUser = null;
                    System.out.println("已退出登录");
                    return;
                default:
                    if (currentUser instanceof Admin) {
                        handleAdminChoice(choice);
                    } else {
                        System.out.println("无效选择，请重试");
                    }
            }
        }
    }

    private void showAdminMenu() {
        System.out.println("10. 管理用户");
        System.out.println("11. 管理所有物品");
        System.out.println("12. 审核认领申请");
        System.out.println("13. 系统统计");
    }

    private void handleAdminChoice(int choice) {
        if (!(currentUser instanceof Admin)) return;

        switch (choice) {
            case 10: manageUsers(); break;
            case 11: manageAllItems(); break;
            case 12: reviewClaims(); break;
            case 13: showStatistics(); break;
            default:
                System.out.println("无效选择，请重试");
        }
    }

    // ==================== 功能实现 ====================

    private void publishLostItem() {
        onOperationStart("发布失物信息");
        System.out.println("\n--- 发布失物信息 ---");

        String name = readString("物品名称: ");
        String description = readString("物品描述: ");
        String category = readString("物品类别(证件/电子/日用/其他): ");
        String location = readString("丢失地点: ");
        String lostTime = readString("丢失时间: ");
        double reward = readDouble("悬赏金额(元): ");

        LostItem item = new LostItem(name, description, category, location,
                currentUser.getId(), currentUser.getUsername(), lostTime, reward);

        dataManager.addItem(item, new LoggingCallback<Item>("发布失物") {
            @Override
            public void onSuccess(Item result) {
                currentUser.incrementPostCount();
                System.out.println("发布成功！物品ID: " + result.getId());
                MainUI.this.onOperationComplete("发布失物", true, "发布成功");
                MainUI.this.onDataUpdate("失物", 1);
            }

            @Override
            public void onError(String errorMessage) {
                System.err.println("发布失败: " + errorMessage);
                MainUI.this.onOperationComplete("发布失物", false, errorMessage);
            }
        });
    }

    private void publishFoundItem() {
        onOperationStart("发布招领信息");
        System.out.println("\n--- 发布招领信息 ---");

        String name = readString("物品名称: ");
        String description = readString("物品描述: ");
        String category = readString("物品类别(证件/电子/日用/其他): ");
        String location = readString("拾获地点: ");
        String foundTime = readString("拾获时间: ");
        String statusDesc = readString("物品状态(完好/有损/其他): ");

        FoundItem item = new FoundItem(name, description, category, location,
                currentUser.getId(), currentUser.getUsername(), foundTime, statusDesc);

        dataManager.addItem(item, new LoggingCallback<Item>("发布招领") {
            @Override
            public void onSuccess(Item result) {
                currentUser.incrementPostCount();
                System.out.println("发布成功！物品ID: " + result.getId());
                MainUI.this.onOperationComplete("发布招领", true, "发布成功");
                MainUI.this.onDataUpdate("招领", 1);
            }

            @Override
            public void onError(String errorMessage) {
                System.err.println("发布失败: " + errorMessage);
                MainUI.this.onOperationComplete("发布招领", false, errorMessage);
            }
        });
    }

    private void viewAllItems() {
        onOperationStart("查看所有物品");
        List<Item> items = dataManager.getAllItems();

        if (items.isEmpty()) {
            System.out.println("暂无物品信息");
            return;
        }

        System.out.println("\n========== 所有物品 ==========");
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.println("共 " + items.size() + " 条记录");
        System.out.println("================================");
    }

    private void searchItems() {
        onOperationStart("搜索物品");
        String keyword = readString("请输入搜索关键词: ");

        List<Item> items = dataManager.searchItems(keyword);
        if (items.isEmpty()) {
            System.out.println("未找到匹配的物品");
            return;
        }

        System.out.println("\n========== 搜索结果 ==========");
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.println("共 " + items.size() + " 条记录");
        System.out.println("================================");
    }

    private void smartMatch() {
        onOperationStart("智能匹配");
        System.out.println("\n--- 物品智能匹配 ---");
        System.out.println("1. 简单匹配策略");
        System.out.println("2. 高级匹配策略");

        int choice = readInt("选择匹配策略: ");
        if (choice == 2) {
            matchService.setMatcher(new com.lostfound.service.AdvancedItemMatcher());
        } else {
            matchService.setMatcher(new com.lostfound.service.SimpleItemMatcher());
        }

        List<com.lostfound.api.MatchResult> results = matchService.performMatch();
        matchService.printMatchResults(results);
    }

    private void claimItem() {
        onOperationStart("申请认领");
        System.out.println("\n--- 申请认领物品 ---");

        // 先显示所有待认领的物品
        List<Item> items = dataManager.getItemsByType("失物");
        List<Item> activeItems = items.stream()
                .filter(Item::isMatchable)
                .collect(Collectors.toList());

        if (activeItems.isEmpty()) {
            System.out.println("暂无可认领的物品");
            return;
        }

        System.out.println("可认领的失物:");
        for (Item item : activeItems) {
            System.out.println("  ID:" + item.getId() + " - " + item);
        }

        int itemId = readInt("请输入要认领的物品ID: ");
        String reason = readString("请输入认领理由: ");

        Item targetItem = null;
        for (Item item : activeItems) {
            if (item.getId() == itemId) {
                targetItem = item;
                break;
            }
        }

        if (targetItem == null) {
            System.out.println("物品不存在或不可认领");
            return;
        }

        ClaimRecord record = new ClaimRecord(itemId, targetItem.getName(),
                currentUser.getId(), currentUser.getUsername(), reason);

        dataManager.addClaimRecord(record, new LoggingCallback<ClaimRecord>("申请认领") {
            @Override
            public void onSuccess(ClaimRecord result) {
                System.out.println("认领申请已提交，等待审核！");
                MainUI.this.onOperationComplete("申请认领", true, "申请已提交");
            }

            @Override
            public void onError(String errorMessage) {
                System.err.println("申请失败: " + errorMessage);
                MainUI.this.onOperationComplete("申请认领", false, errorMessage);
            }
        });
    }

    private void viewMyItems() {
        onOperationStart("查看我的发布");
        List<Item> items = dataManager.getItemsByPublisher(currentUser.getId());

        if (items.isEmpty()) {
            System.out.println("您还没有发布任何物品");
            return;
        }

        System.out.println("\n========== 我的发布 ==========");
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.println("共 " + items.size() + " 条记录");
        System.out.println("================================");
    }

    private void viewNotifications() {
        onOperationStart("查看通知");
        List<Notification> notifications = dataManager.getUserNotifications(currentUser.getId());

        if (notifications.isEmpty()) {
            System.out.println("暂无通知");
            return;
        }

        System.out.println("\n========== 我的通知 ==========");
        for (Notification n : notifications) {
            System.out.println(n);
        }
        System.out.println("================================");
    }

    private void viewProfile() {
        System.out.println("\n========== 个人信息 ==========");
        System.out.println("ID: " + currentUser.getId());
        System.out.println("用户名: " + currentUser.getUsername());
        System.out.println("角色: " + currentUser.getRole());
        System.out.println("手机: " + currentUser.getPhone());
        System.out.println("邮箱: " + currentUser.getEmail());
        System.out.println("注册时间: " + currentUser.getFormattedCreateTime());
        System.out.println("发布数量: " + currentUser.getPostCount());
        System.out.println("权限: " + currentUser.getPermissionDescription());
        System.out.println("================================");
    }

    // ==================== 管理员功能 ====================

    private void manageUsers() {
        if (!(currentUser instanceof Admin)) return;
        onOperationStart("管理用户");

        List<User> users = dataManager.getAllUsers();
        System.out.println("\n========== 用户列表 ==========");
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("共 " + users.size() + " 个用户");
        System.out.println("================================");
    }

    private void manageAllItems() {
        if (!(currentUser instanceof Admin)) return;
        onOperationStart("管理所有物品");

        List<Item> items = dataManager.getAllItems();
        System.out.println("\n========== 所有物品 ==========");
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.println("共 " + items.size() + " 条记录");
        System.out.println("================================");

        System.out.println("1. 关闭物品");
        System.out.println("2. 删除物品");
        int choice = readInt("请选择操作(0返回): ");

        if (choice == 1) {
            int itemId = readInt("请输入要关闭的物品ID: ");
            if (dataManager.updateItemStatus(itemId, "CLOSED")) {
                System.out.println("物品已关闭");
            } else {
                System.out.println("操作失败");
            }
        } else if (choice == 2) {
            int itemId = readInt("请输入要删除的物品ID: ");
            if (dataManager.deleteItem(itemId)) {
                System.out.println("物品已删除");
            } else {
                System.out.println("操作失败");
            }
        }
    }

    private void reviewClaims() {
        if (!(currentUser instanceof Admin)) return;
        onOperationStart("审核认领申请");

        List<ClaimRecord> pendingClaims = dataManager.getPendingClaims();
        if (pendingClaims.isEmpty()) {
            System.out.println("暂无待审核的认领申请");
            return;
        }

        System.out.println("\n========== 待审核认领 ==========");
        for (ClaimRecord claim : pendingClaims) {
            System.out.println(claim);
        }
        System.out.println("================================");

        int claimId = readInt("请输入要审核的认领ID(0返回): ");
        if (claimId == 0) return;

        System.out.println("1. 通过");
        System.out.println("2. 拒绝");
        int choice = readInt("请选择操作: ");

        if (choice == 1) {
            if (dataManager.updateClaimStatus(claimId, "APPROVED")) {
                System.out.println("认领已通过");
            } else {
                System.out.println("操作失败");
            }
        } else if (choice == 2) {
            if (dataManager.updateClaimStatus(claimId, "REJECTED")) {
                System.out.println("认领已拒绝");
            } else {
                System.out.println("操作失败");
            }
        }
    }

    private void showStatistics() {
        if (!(currentUser instanceof Admin)) return;
        onOperationStart("系统统计");

        int totalUsers = dataManager.getTotalUsers();
        int totalItems = dataManager.getTotalItems();
        int activeItems = dataManager.getActiveItems();
        int pendingClaims = dataManager.getPendingClaimsCount();

        System.out.println("\n========== 系统统计 ==========");
        System.out.println("总用户数: " + totalUsers);
        System.out.println("总物品数: " + totalItems);
        System.out.println("活跃物品数: " + activeItems);
        System.out.println("待审核认领数: " + pendingClaims);
        System.out.println("================================");
    }

    // ==================== 工具方法 ====================

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字");
            }
        }
    }
}
