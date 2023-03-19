package siplaundry.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import siplaundry.Entity.VerivicationEntity;
import siplaundry.Util.DatabaseUtil;

public class VerivicationRepo extends Repo<VerivicationEntity> {
    final Connection conn = DatabaseUtil.getConnection();

    private final static String tableName = VerivicationEntity.tableName;
    private static String getid = "user_id";


    public Integer add(VerivicationEntity verify) {
        String sql = "INSERT INTO verivications (`user_id`, `code`) VALUES (?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, verify.getUserID().getID());
            stmt.setString(2, verify.getCode());
            stmt.executeUpdate();

            return 1;
        } catch(SQLException e) { e.printStackTrace(); }

        return 0;
    }
    
    public List<VerivicationEntity> get() {
       return super.getAll(tableName);
    }

    public List<VerivicationEntity> get(Map<String, Object> values) {
       return super.get(tableName, values);
    }


    public boolean Update(VerivicationEntity verify) {
        String sql = "UPDATE "+ tableName +" SET code = ? WHERE user_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, verify.getCode());
            stmt.setInt(2, verify.getUserID().getID());
            stmt.executeUpdate();

            return stmt.getUpdateCount() > 0;
        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    public boolean delete(VerivicationEntity verify) {
        String sql = "DELETE FROM "+ tableName +" WHERE user_id = ? AND code = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, verify.getUserID().getID());
            stmt.setString(2, verify.getCode());
            stmt.executeUpdate();

            return stmt.getUpdateCount() > 0;
        } catch(SQLException e) { e.printStackTrace(); }

        return false;
    }

    public boolean delete(int id) {
       return super.delete(tableName, getid, id);
    }

    public VerivicationEntity mapToEntity(ResultSet rs) throws SQLException {
        return new VerivicationEntity(
            new UsersRepo().get(rs.getInt("user_id")),
            rs.getString("code")
        );
    }


    
}
