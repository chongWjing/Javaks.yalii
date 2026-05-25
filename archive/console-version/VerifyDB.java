import java.sql.*;

public class VerifyDB {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/lost_found_db?useSSL=false";
            Connection c = DriverManager.getConnection(url, "root", "123456");
            Statement s = c.createStatement();

            System.out.println("=== 数据库验证 ===\n");

            ResultSet rs = s.executeQuery("SHOW TABLES");
            System.out.println("数据库中的表:");
            while(rs.next()) {
                System.out.println("  ✓ " + rs.getString(1));
            }
            System.out.println();

            // 验证表结构
            String[] tables = {"users", "items", "claim_records", "notifications"};
            for(String table : tables) {
                rs = s.executeQuery("DESCRIBE " + table);
                System.out.println(table + " 表结构:");
                while(rs.next()) {
                    System.out.println("  - " + rs.getString(1) + ": " + rs.getString(2));
                }
                System.out.println();
            }

            c.close();
            System.out.println("✓ 数据库验证完成！");
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
        }
    }
}
