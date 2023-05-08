package siplaundry.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import siplaundry.entity.UserEntity;
import siplaundry.util.DatabaseUtil;

public abstract class Repo<E> {
    final Connection conn = DatabaseUtil.getConnection();

    public List<E> getAll(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        List<E> table = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                table.add(mapToEntity(result));
            }
        } catch (Exception e) {
        }

        return table;
    }

    public List<E> get(String tableName, Map<String, Object> values) {
        int iterate = 0;
        String sql = "SELECT * FROM " + tableName + " WHERE ";
        List<E> table = new ArrayList<>();

        for (String valueKey : values.keySet()) {
            if (iterate > 0)
                sql += " AND ";
            sql += valueKey + " = ?";

            iterate++;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DatabaseUtil.prepareStmt(stmt, values, 0);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                table.add(mapToEntity(rs));
            }
        } catch (SQLException e) {
        }

        return table;
    }

    public E get(String tableName, String getid, Integer id) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + getid + " = ?";
        E customer = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return mapToEntity(rs);
        } catch (SQLException e) { e.printStackTrace(); }

        return customer;
    }

    public List<E> search(String tableName, Map<String, Object> values) {
        int iterate = 0;
        String sql = "SELECT * FROM " + tableName + " WHERE ";
        List<E> table = new ArrayList<>();

        for (String valueKey : values.keySet()) {
            if (iterate > 0)
                sql += " OR ";
            sql += (valueKey + " LIKE CONCAT( '%',?,'%')");

            iterate++;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            DatabaseUtil.prepareStmt(stmt, values, 0);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                table.add(mapToEntity(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return table;
    }
    public List<E> searchByUser(String tableName, UserEntity user, Map<String, Object> values) {
        int iterate = 0;
        String sql = "SELECT * FROM " + tableName + " WHERE (";
        List<E> table = new ArrayList<>();

        for (String valueKey : values.keySet()) {
            if (iterate > 0)
                sql += " OR ";
            sql += valueKey + " LIKE CONCAT( '%',?,'%')";

            iterate++;
        }

        sql += ") AND user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DatabaseUtil.prepareStmt(stmt, values, 0);
            stmt.setInt(values.keySet().size() + 1, user.getID());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                table.add(mapToEntity(rs));
            }
        } catch (SQLException e) {
        }

        return table;
    }

    public boolean delete(String tableName, String getid, int id) {
        String sql = "DELETE FROM " + tableName + " WHERE " + getid + " = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

            return stmt.getUpdateCount() > 0;
        } catch (SQLException e) { e.printStackTrace(); }

        return false;
    }

    public abstract E mapToEntity(ResultSet result) throws SQLException;
}
