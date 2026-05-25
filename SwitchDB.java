import java.sql.*;

public class SwitchDB {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        Connection conn = DriverManager.getConnection(url, "root", "123456");
        Statement stmt = conn.createStatement();

        // 1. Create @javaks database
        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `@javaks`");
        System.out.println("[OK] Database @javaks ready");

        // 2. Use @javaks
        stmt.executeUpdate("USE `@javaks`");

        // 3. Create tables
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "username VARCHAR(50) UNIQUE NOT NULL, " +
            "password VARCHAR(100) NOT NULL, " +
            "phone VARCHAR(20), email VARCHAR(100), " +
            "role VARCHAR(20) DEFAULT 'REGULAR', " +
            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "post_count INT DEFAULT 0, manage_count INT DEFAULT 0)");
        System.out.println("[OK] Table users created");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS items (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(100) NOT NULL, description TEXT, " +
            "category VARCHAR(50), location VARCHAR(200), " +
            "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "publisher_id INT, publisher_name VARCHAR(50), " +
            "status VARCHAR(20) DEFAULT 'ACTIVE', " +
            "item_type VARCHAR(20), lost_time VARCHAR(100), " +
            "reward DOUBLE DEFAULT 0, found_time VARCHAR(100), " +
            "status_description VARCHAR(200), " +
            "FOREIGN KEY (publisher_id) REFERENCES users(id))");
        System.out.println("[OK] Table items created");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS claim_records (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "item_id INT, item_name VARCHAR(100), " +
            "claimer_id INT, claimer_name VARCHAR(50), " +
            "claim_reason TEXT, status VARCHAR(20) DEFAULT 'PENDING', " +
            "claim_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "process_time TIMESTAMP, " +
            "FOREIGN KEY (item_id) REFERENCES items(id), " +
            "FOREIGN KEY (claimer_id) REFERENCES users(id))");
        System.out.println("[OK] Table claim_records created");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "user_id INT, title VARCHAR(100), " +
            "content TEXT, type VARCHAR(20), " +
            "is_read BOOLEAN DEFAULT FALSE, " +
            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (user_id) REFERENCES users(id))");
        System.out.println("[OK] Table notifications created");

        // 4. Drop old database
        stmt.executeUpdate("DROP DATABASE IF EXISTS `lost_found_db`");
        System.out.println("[OK] Old database lost_found_db deleted");

        conn.close();
        System.out.println("[DONE] Switched to @javaks database");
    }
}
