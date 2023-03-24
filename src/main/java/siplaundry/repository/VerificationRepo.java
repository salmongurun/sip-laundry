package siplaundry.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import siplaundry.entity.VerificationEntity;
import siplaundry.util.DatabaseUtil;

public class VerificationRepo extends Repo<VerificationEntity> {
    final Connection conn = DatabaseUtil.getConnection();

    private final static String tableName = VerificationEntity.tableName;
    private static String getid = "user_id";

    public Integer add(VerificationEntity verify) {
        String sql = "INSERT INTO verifications (`user_id`, `code`) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, verify.getUserID().getID());
            stmt.setString(2, verify.getCode());
            stmt.executeUpdate();

            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<VerificationEntity> get() {
        return super.getAll(tableName);
    }

    public List<VerificationEntity> get(Map<String, Object> values) {
        return super.get(tableName, values);
    }

    public boolean Update(VerificationEntity verify) {
        String sql = "UPDATE " + tableName + " SET code = ? WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, verify.getCode());
            stmt.setInt(2, verify.getUserID().getID());
            stmt.executeUpdate();

            return stmt.getUpdateCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(VerificationEntity verify) {
        String sql = "DELETE FROM " + tableName + " WHERE user_id = ? AND code = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, verify.getUserID().getID());
            stmt.setString(2, verify.getCode());
            stmt.executeUpdate();

            return stmt.getUpdateCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int id) {
        return super.delete(tableName, getid, id);
    }

    public VerificationEntity mapToEntity(ResultSet rs) throws SQLException {
        return new VerificationEntity(
                new UsersRepo().get(rs.getInt("user_id")),
                rs.getString("code")
        );
    }

}
