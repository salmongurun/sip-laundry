package siplaundry.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mysql.cj.jdbc.Driver;

import siplaundry.data.Config;

public class DatabaseUtil {
    private static Connection conn;
    Config config = new Config();

    static {
      //  String jdbcUrl = "jdbc:mysql://" + Config.host + ":" + Config.port + "/" + Config.database;

        try {
            Properties properti = new Properties();
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("app.properties");
            properti.load(inputStream);

            String jdbcUrl = "jdbc:mysql://" +
                            properti.getProperty("db.host") + ":" +
                            properti.getProperty("db.port") + "/" +
                            properti.getProperty("db.database");


            Driver driver = new Driver();
            DriverManager.registerDriver(driver);

            conn = DriverManager.getConnection(jdbcUrl, properti.getProperty("db.username"), properti.getProperty("db.password"));
        } catch (SQLException | IOException e) {
            throw new Error(e.getMessage());
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void prepareStmt(PreparedStatement stmt, Map<String, Object> values) throws SQLException {
        int i = 1;

        for (String key : values.keySet()) {
            Object value = values.get(key);
            setValues(stmt, i++, value);
        }
    }

    public static void prepareStmt(PreparedStatement stmt, List<Object> values) throws SQLException {
        int i = 1;

        for (Object value : values) {
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
