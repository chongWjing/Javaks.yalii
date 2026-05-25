import java.sql.*;

public class InitDB {
    public static void main(String[] args) {
        try {
            // 连接到MySQL（不指定数据库）
            String url = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String user = "root";
            String password = "123456";

            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            // 创建数据库（如果不存在）
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS lost_found_db");
            System.out.println("[初始化] 数据库 lost_found_db 创建成功");

            // 使用该数据库
            stmt.executeUpdate("USE lost_found_db");

            // 创建表
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
            System.out.println("[初始化] users 表创建成功");

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
            System.out.println("[初始化] items 表创建成功");

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
            System.out.println("[初始化] claim_records 表创建成功");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT, " +
                    "title VARCHAR(100), " +
                    "content TEXT, " +
                    "type VARCHAR(20), " +
                    "is_read BOOLEAN DEFAULT FALSE, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");
            System.out.println("[初始化] notifications 表创建成功");

            conn.close();
            System.out.println("[初始化] 数据库初始化完成！");
        } catch (Exception e) {
            System.err.println("[初始化] 错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
