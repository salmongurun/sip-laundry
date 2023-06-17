package siplaundry.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import siplaundry.data.AccountRole;
import siplaundry.data.SortingOrder;
import siplaundry.entity.CustomerEntity;
import siplaundry.entity.UserEntity;

public class UsersRepo extends Repo<UserEntity> {

    private static String tableName = UserEntity.tableName;
    private static String getid = "user_id";

    public Integer add(UserEntity cust) {
        String sql = "INSERT INTO " + tableName
                + " (`username`, `fullname`, `phone`, `password` , `address`, `role`) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cust.getUsername());
            stmt.setString(2, cust.getFullname());
            stmt.setString(3, cust.getPhone());
            stmt.setString(4, cust.getPassword());
            stmt.setString(5, cust.getAddress());
            stmt.setString(6, cust.getRole().toString());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<UserEntity> get() {
        return super.getAll(tableName);
    }

     public List<UserEntity> search(Map<String, Object> values, String column, SortingOrder ordering) {
        return super.search(tableName, values, column, ordering);
    }

    public List<UserEntity> sortBy(String column, String condition){
        return super.sortBy(tableName, column, condition);
    }

    public UserEntity get(Integer id) {
        return super.get(tableName, getid, id);
    }

    public List<UserEntity> get(Map<String, Object> values) {
        return super.get(tableName, values);
    }

    public List<UserEntity> search(Map<String, Object> values) {
        return super.search(tableName, values);
    }

    public boolean Update(UserEntity cust) {
        String sql = "UPDATE " + tableName
                + " SET username = ?, fullname = ?, phone = ?, password = ?, address = ? WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cust.getUsername());
            stmt.setString(2, cust.getFullname());
            stmt.setString(3, cust.getPhone());
            stmt.setString(4, cust.getPassword());
            stmt.setString(5, cust.getAddress());
            stmt.setInt(6, cust.getID());

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

    @Override
    public UserEntity mapToEntity(ResultSet result) throws SQLException {
        UserEntity user = new UserEntity(
                result.getString("username"),
                result.getString("fullname"),
                result.getString("phone"),
                result.getString("password"),
                result.getString("address"),
                AccountRole.valueOf(result.getString("role"))

        );

        user.setID(result.getInt("user_id"));
        return user;
    }

}
