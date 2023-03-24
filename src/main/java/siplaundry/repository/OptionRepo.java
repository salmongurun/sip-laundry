package siplaundry.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import siplaundry.entity.OptionEntity;
import siplaundry.util.DatabaseUtil;


public class OptionRepo extends Repo<OptionEntity> {
    final Connection conn = DatabaseUtil.getConnection();
    public static String tableName = "options";

    public Integer add(OptionEntity option) {
        String sql = "INSERT INTO " + tableName + " (`key`, `value`) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, option.getKey());
            stmt.setString(2, option.getValue());

            stmt.executeUpdate();

            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<OptionEntity> get() {
        return super.getAll(tableName);
    }

    public OptionEntity get(String key) {
        String sql = "SELECT * FROM " + tableName + " WHERE `key` = ?";
        OptionEntity keyOption = new OptionEntity();

        try (PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, key); 
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToEntity(rs);
            }
        } catch (SQLException e) {
        }

        return keyOption;
    }

    public boolean delete(String key) {
        String sql = "DELETE FROM " + tableName + " WHERE `key` = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, key);
            stmt.executeUpdate();

            return stmt.getUpdateCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public OptionEntity mapToEntity(ResultSet result) throws SQLException {
        OptionEntity account = new OptionEntity(
                result.getString("key"),
                result.getString("value"));

        return account;
    }

}
