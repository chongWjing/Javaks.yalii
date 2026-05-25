package com.lostfound.db;

import com.lostfound.model.*;
import com.lostfound.api.DatabaseCallback;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据管理类 - 负责所有数据库操作
 * 使用接口回调机制处理操作结果
 */
public class DataManager {
    private static DataManager instance;
    private Connection connection;

    private DataManager() {
        this.connection = Database.getInstance().getConnection();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    // ==================== 用户操作 ====================

    /**
     * 添加用户 - 使用接口回调处理结果
     */
    public void addUser(User user, DatabaseCallback<User> callback) {
        try {
            String sql = "INSERT INTO users (username, password, phone, email, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user instanceof Admin ? "ADMIN" : "REGULAR");

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
                callback.onSuccess(user);
            } else {
                callback.onError("添加用户失败");
            }
        } catch (SQLException e) {
            callback.onError("数据库错误: " + e.getMessage());
        }
    }

    /**
     * 用户登录验证
     */
    public User login(String username, String password) {
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 登录查询失败: " + e.getMessage());
        }
        return null;
    }

    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String role = rs.getString("role");
        Timestamp createTime = rs.getTimestamp("create_time");
        int postCount = rs.getInt("post_count");
        int manageCount = rs.getInt("manage_count");

        LocalDateTime ct = createTime != null ? createTime.toLocalDateTime() : LocalDateTime.now();

        if ("ADMIN".equals(role)) {
            return new Admin(id, username, password, phone, email, ct, postCount, manageCount);
        } else {
            return new RegularUser(id, username, password, phone, email, ct, postCount);
        }
    }

    public boolean isUsernameExists(String username) {
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 检查用户名失败: " + e.getMessage());
        }
        return false;
    }

    public User getUserById(int userId) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 查询用户失败: " + e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM users ORDER BY create_time DESC";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(createUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 查询所有用户失败: " + e.getMessage());
        }
        return users;
    }

    // ==================== 物品操作 ====================

    /**
     * 添加物品 - 使用接口回调处理结果
     */
    public void addItem(Item item, DatabaseCallback<Item> callback) {
        try {
            String sql = "INSERT INTO items (name, description, category, location, publisher_id, " +
                    "publisher_name, item_type, lost_time, reward, found_time, status_description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setString(3, item.getCategory());
            pstmt.setString(4, item.getLocation());
            pstmt.setInt(5, item.getPublisherId());
            pstmt.setString(6, item.getPublisherName());
            pstmt.setString(7, item.getItemType());

            if (item instanceof LostItem) {
                LostItem lost = (LostItem) item;
                pstmt.setString(8, lost.getLostTime());
                pstmt.setDouble(9, lost.getReward());
                pstmt.setString(10, null);
                pstmt.setString(11, null);
            } else if (item instanceof FoundItem) {
                FoundItem found = (FoundItem) item;
                pstmt.setString(8, null);
                pstmt.setDouble(9, 0);
                pstmt.setString(10, found.getFoundTime());
                pstmt.setString(11, found.getStatusDescription());
            }

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    item.setId(keys.getInt(1));
                }
                callback.onSuccess(item);
            } else {
                callback.onError("添加物品失败");
            }
        } catch (SQLException e) {
            callback.onError("数据库错误: " + e.getMessage());
        }
    }

    /**
     * 获取所有物品
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        try {
            String sql = "SELECT * FROM items ORDER BY time DESC";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                items.add(createItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 查询所有物品失败: " + e.getMessage());
        }
        return items;
    }

    /**
     * 根据类型获取物品
     */
    public List<Item> getItemsByType(String type) {
        List<Item> items = new ArrayList<>();
        try {
            String sql = "SELECT * FROM items WHERE item_type = ? ORDER BY time DESC";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(createItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 查询物品失败: " + e.getMessage());
        }
        return items;
    }

    /**
     * 搜索物品
     */
    public List<Item> searchItems(String keyword) {
        List<Item> items = new ArrayList<>();
        try {
            String sql = "SELECT * FROM items WHERE name LIKE ? OR description LIKE ? " +
                    "OR category LIKE ? OR location LIKE ? ORDER BY time DESC";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            pstmt.setString(4, pattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(createItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 搜索物品失败: " + e.getMessage());
        }
        return items;
    }

    /**
     * 获取用户发布的物品
     */
    public List<Item> getItemsByPublisher(int publisherId) {
        List<Item> items = new ArrayList<>();
        try {
            String sql = "SELECT * FROM items WHERE publisher_id = ? ORDER BY time DESC";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, publisherId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(createItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 查询用户物品失败: " + e.getMessage());
        }
        return items;
    }

    /**
     * 更新物品状态
     */
    public boolean updateItemStatus(int itemId, String status) {
        try {
            String sql = "UPDATE items SET status = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, itemId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[数据库] 更新物品状态失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 删除物品
     */
    public boolean deleteItem(int itemId) {
        try {
            // 先删除相关的认领记录
            String deleteClaims = "DELETE FROM claim_records WHERE item_id = ?";
            PreparedStatement pstmt1 = connection.prepareStatement(deleteClaims);
            pstmt1.setInt(1, itemId);
            pstmt1.executeUpdate();

            // 再删除物品
            String sql = "DELETE FROM items WHERE id = ?";
            PreparedStatement pstmt2 = connection.prepareStatement(sql);
            pstmt2.setInt(1, itemId);
            return pstmt2.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[数据库] 删除物品失败: " + e.getMessage());
            return false;
        }
    }

    private Item createItemFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String category = rs.getString("category");
        String location = rs.getString("location");
        Timestamp time = rs.getTimestamp("time");
        int publisherId = rs.getInt("publisher_id");
        String publisherName = rs.getString("publisher_name");
        String status = rs.getString("status");
        String itemType = rs.getString("item_type");
        String lostTime = rs.getString("lost_time");
        double reward = rs.getDouble("reward");
        String foundTime = rs.getString("found_time");
        String statusDescription = rs.getString("status_description");

        LocalDateTime lt = time != null ? time.toLocalDateTime() : LocalDateTime.now();
        Item.ItemStatus itemStatus = Item.ItemStatus.valueOf(status);

        if ("失物".equals(itemType)) {
            return new LostItem(id, name, description, category, location, lt,
                    publisherId, publisherName, itemStatus, lostTime, reward);
        } else {
            return new FoundItem(id, name, description, category, location, lt,
                    publisherId, publisherName, itemStatus, foundTime, statusDescription);
        }
    }

    // ==================== 认领记录操作 ====================

    public void addClaimRecord(ClaimRecord record, DatabaseCallback<ClaimRecord> callback) {
        try {
            String sql = "INSERT INTO claim_records (item_id, item_name, claimer_id, claimer_name, claim_reason) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, record.getItemId());
            pstmt.setString(2, record.getItemName());
            pstmt.setInt(3, record.getClaimerId());
            pstmt.setString(4, record.getClaimerName());
            pstmt.setString(5, record.getClaimReason());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    record.setId(keys.getInt(1));
                }
                callback.onSuccess(record);
            } else {
                callback.onError("添加认领记录失败");
            }
        } catch (SQLException e) {
            callback.onError("数据库错误: " + e.getMessage());
        }
    }

    public List<ClaimRecord> getAllClaimRecords() {
        List<ClaimRecord> records = new ArrayList<>();
        try {
            String sql = "SELECT * FROM claim_records ORDER BY claim_time DESC";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                records.add(createClaimFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 查询认领记录失败: " + e.getMessage());
        }
        return records;
    }

    public List<ClaimRecord> getPendingClaims() {
        List<ClaimRecord> records = new ArrayList<>();
        try {
            String sql = "SELECT * FROM claim_records WHERE status = 'PENDING' ORDER BY claim_time DESC";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                records.add(createClaimFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 查询待审核认领失败: " + e.getMessage());
        }
        return records;
    }

    public boolean updateClaimStatus(int claimId, String status) {
        try {
            String sql = "UPDATE claim_records SET status = ?, process_time = CURRENT_TIMESTAMP WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, claimId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[数据库] 更新认领状态失败: " + e.getMessage());
            return false;
        }
    }

    private ClaimRecord createClaimFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int itemId = rs.getInt("item_id");
        String itemName = rs.getString("item_name");
        int claimerId = rs.getInt("claimer_id");
        String claimerName = rs.getString("claimer_name");
        String claimReason = rs.getString("claim_reason");
        String status = rs.getString("status");
        Timestamp claimTime = rs.getTimestamp("claim_time");
        Timestamp processTime = rs.getTimestamp("process_time");

        ClaimRecord.ClaimStatus claimStatus = ClaimRecord.ClaimStatus.valueOf(status);
        LocalDateTime ct = claimTime != null ? claimTime.toLocalDateTime() : LocalDateTime.now();
        LocalDateTime pt = processTime != null ? processTime.toLocalDateTime() : null;

        return new ClaimRecord(id, itemId, itemName, claimerId, claimerName,
                claimReason, claimStatus, ct, pt);
    }

    // ==================== 通知操作 ====================

    public void addNotification(Notification notification, DatabaseCallback<Notification> callback) {
        try {
            String sql = "INSERT INTO notifications (user_id, title, content, type) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, notification.getUserId());
            pstmt.setString(2, notification.getTitle());
            pstmt.setString(3, notification.getContent());
            pstmt.setString(4, notification.getType().name());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    notification.setId(keys.getInt(1));
                }
                callback.onSuccess(notification);
            } else {
                callback.onError("添加通知失败");
            }
        } catch (SQLException e) {
            callback.onError("数据库错误: " + e.getMessage());
        }
    }

    public List<Notification> getUserNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        try {
            String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY create_time DESC";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                notifications.add(createNotificationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 查询通知失败: " + e.getMessage());
        }
        return notifications;
    }

    private Notification createNotificationFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        String title = rs.getString("title");
        String content = rs.getString("content");
        String type = rs.getString("type");
        boolean isRead = rs.getBoolean("is_read");
        Timestamp createTime = rs.getTimestamp("create_time");

        Notification.NotificationType nType = Notification.NotificationType.valueOf(type);
        LocalDateTime ct = createTime != null ? createTime.toLocalDateTime() : LocalDateTime.now();

        return new Notification(id, userId, title, content, nType, isRead, ct);
    }

    // ==================== 统计操作 ====================

    public int getTotalUsers() {
        try {
            String sql = "SELECT COUNT(*) FROM users";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[数据库] 统计用户数失败: " + e.getMessage());
        }
        return 0;
    }

    public int getTotalItems() {
        try {
            String sql = "SELECT COUNT(*) FROM items";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[数据库] 统计物品数失败: " + e.getMessage());
        }
        return 0;
    }

    public int getActiveItems() {
        try {
            String sql = "SELECT COUNT(*) FROM items WHERE status = 'ACTIVE'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[数据库] 统计活跃物品失败: " + e.getMessage());
        }
        return 0;
    }

    public int getPendingClaimsCount() {
        try {
            String sql = "SELECT COUNT(*) FROM claim_records WHERE status = 'PENDING'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[数据库] 统计待审核认领失败: " + e.getMessage());
        }
        return 0;
    }
}
