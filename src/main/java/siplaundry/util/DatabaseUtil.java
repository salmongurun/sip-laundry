package siplaundry.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mysql.cj.jdbc.Driver;

import siplaundry.data.Config;

public class DatabaseUtil {
    private static Connection conn;
    Config config = new Config();

    static {
        String dbhost = ConfigUtil.get("db.host");
        String dbport = ConfigUtil.get("db.port");
        String dbdatabase = ConfigUtil.get("db.database");
        String dbusername = ConfigUtil.get("db.username");
        String dbpassword = ConfigUtil.get("db.password");

        String jdbcUrl = "jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbdatabase;

        try {
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);

            conn = DriverManager.getConnection(jdbcUrl, dbusername, dbpassword);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void prepareStmt(PreparedStatement stmt, Map<String, Object> values, int addition) throws SQLException {
        int i = 1 + addition;

        for (String key : values.keySet()) {
            Object value = values.get(key);
            setValues(stmt, i++, value);
        }
    }

    private static void setValues(PreparedStatement stmt, int row, Object value) throws SQLException {
        if (value instanceof String)
            stmt.setString(row, String.valueOf(value));

        if (value instanceof Integer)
            stmt.setInt(row, (Integer) value);
    }
}
