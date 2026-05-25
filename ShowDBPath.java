import java.sql.*;

public class ShowDBPath {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
            "root", "123456");
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SHOW VARIABLES LIKE 'datadir'");
        if (rs.next()) {
            System.out.println("MySQL Data Dir: " + rs.getString("Value"));
        }

        rs = stmt.executeQuery("SELECT @@datadir");
        if (rs.next()) {
            System.out.println("@@datadir: " + rs.getString(1));
        }

        rs = stmt.executeQuery("SHOW VARIABLES LIKE 'basedir'");
        if (rs.next()) {
            System.out.println("MySQL Base Dir: " + rs.getString("Value"));
        }

        conn.close();
    }
}
