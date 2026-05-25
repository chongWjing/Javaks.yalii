package com.lostfound.db;

import java.sql.*;

/**
 * 数据库连接管理类 - 单例模式
 * 提供数据库连接和基础操作
 */
public class Database {
    private static Database instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lost_found_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("[数据库] 连接成功");
        } catch (ClassNotFoundException e) {
            System.err.println("[数据库] MySQL驱动未找到: " + e.getMessage());
            initH2Fallback();
        } catch (SQLException e) {
            System.err.println("[数据库] 连接MySQL失败: " + e.getMessage());
            initH2Fallback();
        }
    }

    private void initH2Fallback() {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection("jdbc:h2:mem:lostfound;DB_CLOSE_DELAY=-1");
            System.out.println("[数据库] H2内存数据库初始化成功");
            initTables();
        } catch (Exception e) {
            System.err.println("[数据库] H2初始化失败: " + e.getMessage());
        }
    }

    private void initTables() {
        try {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "phone VARCHAR(20), " +
                    "email VARCHAR(100), " +
                    "role VARCHAR(20) DEFAULT 'REGULAR', " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "post_count INT DEFAULT 0, " +
                    "manage_count INT DEFAULT 0)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "description TEXT, " +
                    "category VARCHAR(50), " +
                    "location VARCHAR(200), " +
                    "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "publisher_id INT, " +
                    "publisher_name VARCHAR(50), " +
                    "status VARCHAR(20) DEFAULT 'ACTIVE', " +
                    "item_type VARCHAR(20), " +
                    "lost_time VARCHAR(100), " +
                    "reward DOUBLE DEFAULT 0, " +
                    "found_time VARCHAR(100), " +
                    "status_description VARCHAR(200), " +
                    "FOREIGN KEY (publisher_id) REFERENCES users(id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS claim_records (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "item_id INT, " +
                    "item_name VARCHAR(100), " +
                    "claimer_id INT, " +
                    "claimer_name VARCHAR(50), " +
                    "claim_reason TEXT, " +
                    "status VARCHAR(20) DEFAULT 'PENDING', " +
                    "claim_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "process_time TIMESTAMP, " +
                    "FOREIGN KEY (item_id) REFERENCES items(id), " +
                    "FOREIGN KEY (claimer_id) REFERENCES users(id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT, " +
                    "title VARCHAR(100), " +
                    "content TEXT, " +
                    "type VARCHAR(20), " +
                    "is_read BOOLEAN DEFAULT FALSE, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            System.out.println("[数据库] 数据表创建成功");
        } catch (SQLException e) {
            System.err.println("[数据库] 创建表失败: " + e.getMessage());
        }
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[数据库] 连接已关闭");
            }
        } catch (SQLException e) {
            System.err.println("[数据库] 关闭连接失败: " + e.getMessage());
        }
    }
}
