package siplaundry.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import siplaundry.entity.ExpenseEntity;
import siplaundry.entity.UserEntity;

public class ExpanseRepo extends Repo<ExpenseEntity> {
    private final static String tableName = "expense";
    private static String getid = "expense_id";

    public Integer add(ExpenseEntity exp) {
        String sql = "INSERT INTO " + tableName
                + " (`name`, `expense_date`, `amount`, `user_id`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, exp.getName().toString());
            stmt.setDate(2, new Date(exp.getExpanse_date().getTime()));
            stmt.setInt(3, exp.getAmount());
            stmt.setInt(4, exp.getUser_id().getID());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<ExpenseEntity> get() {
        return super.getAll(tableName);
    }

    public ExpenseEntity get(Integer id) {
        return super.get(tableName, getid, id);
    }

    public List<ExpenseEntity> get(Map<String, Object> values) {
        return super.get(tableName, values);
    }

    public List<ExpenseEntity> search(Map<String, Object> values) {
        return super.search(tableName, values);
    }

    public List<ExpenseEntity> searchByUser(UserEntity user, Map<String, Object> values) {
        return super.searchByUser(tableName, user, values);
    }

    public boolean Update(ExpenseEntity exp) {
        String sql = "UPDATE " + tableName
                + " SET name = ?, expense_date = ?, amount = ?, user_id = ? WHERE expense_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, exp.getName().toString());
            stmt.setDate(2, new Date(exp.getExpanse_date().getTime()));
            stmt.setInt(3, exp.getAmount());
            stmt.setInt(4, exp.getUser_id().getID());
            stmt.setInt(5, exp.getExpanse_id());

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
    public ExpenseEntity mapToEntity(ResultSet result) throws SQLException {
        int userId = result.getInt("user_id");

        ExpenseEntity exp = new ExpenseEntity(
                result.getString("name"),
                result.getDate("expense_date"),
                result.getInt("amount"),
                new UsersRepo().get(userId)
        );
        

        exp.setid(result.getInt("expense_id"));;
        return exp;
    
    }}
