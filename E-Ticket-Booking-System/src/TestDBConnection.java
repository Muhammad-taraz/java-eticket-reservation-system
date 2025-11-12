import dao.JDBCUtil;
import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        Connection conn = JDBCUtil.getConnection();
        if (conn != null) {
            System.out.println("OK: Connected to DB");
            try { conn.close(); } catch (Exception ignored) {}
        } else {
            System.err.println("ERROR: Connection is null (see stacktrace above).");
        }
    }
}
