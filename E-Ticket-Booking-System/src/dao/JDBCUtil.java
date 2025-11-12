package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/eTicketschema?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "haseeb66";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Throw a RuntimeException so application initialization fails clearly (you can't proceed without driver)
            throw new IllegalStateException("MySQL JDBC Driver not found on classpath. Add mysql-connector-j.jar to lib/ and classpath.", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to obtain DB connection. Check DB server, credentials and driver on classpath.", e);
        }
    }
}
