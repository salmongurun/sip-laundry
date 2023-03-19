package siplaundry.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mysql.cj.jdbc.Driver;

import siplaundry.data.Config;

public class DatabaseUtil{
    private static Connection conn;
    Config config = new Config();

    static {

        String jdbcUrl = "jdbc:mysql://" + Config.host + ":" + Config.port + "/" + Config.database;

        try {
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);

            conn = DriverManager.getConnection(jdbcUrl, Config.username, Config.password);
        } catch (SQLException e) { throw new Error(e.getMessage()); }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void prepareStmt(PreparedStatement stmt, Map<String, Object> values) throws SQLException {
        int i = 1;

        for(String key: values.keySet()) {
            Object value = values.get(key);
            setValues(stmt, i++, value);
        }
    }

    public static void prepareStmt(PreparedStatement stmt, List<Object> values) throws SQLException {
        int i = 1;

        for(Object value: values) {
            setValues(stmt, i++, value);
        }
    }

    private static void setValues(PreparedStatement stmt, int row, Object value) throws SQLException {
        System.out.println(value);
        if(value instanceof String) stmt.setString(row, String.valueOf(value));
        if(value instanceof Integer) stmt.setInt(row, (Integer) value);
        // if(value instanceof TransactionStatus) stmt.setString(row, String.valueOf(value));
        // if(value instanceof Date) stmt.setDate(row, new java.sql.Date(((Date) value).getTime()));
    }

}
