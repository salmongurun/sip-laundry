package siplaundry.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import siplaundry.data.SortingOrder;
import siplaundry.entity.UserEntity;
import siplaundry.util.DatabaseUtil;

public abstract class Repo<E> {
    final Connection conn = DatabaseUtil.getConnection();
    Date date = new Date();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateString = sdf.format(date);

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

    public List<E> sortBy(String tableName, String column, String condition){
        String sql = "SELECT * FROM " + tableName + " ORDER BY " + column + " " + condition;
        List<E> table = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                table.add(mapToEntity(result));
            }
        } catch (Exception e) { e.printStackTrace(); }

        return table;
    }

    public List<E> sortTable(String tableName, String show, String join, Map<String, Object> values, String column, SortingOrder sortOrder){
        int iterate = 0;
        String sql = "SELECT " + tableName + ".* , " + show + " FROM " + tableName + join + " WHERE ";
        List<E> table = new ArrayList<>();

        for (String valueKey : values.keySet()) {
            if (iterate > 0)
                sql += " OR ";
            sql += (valueKey + " LIKE CONCAT( '%',?,'%')");

            iterate++;
        }
        sql+= " ORDER BY "+ column + " " + sortOrder.toString();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            DatabaseUtil.prepareStmt(statement, values, 0);
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

    public List<E> search(String tableName, Map<String, Object> values, String columnName, SortingOrder sortOrder) {
        int iterate = 0;
        String sql = "SELECT * FROM " + tableName + " WHERE ";
        List<E> table = new ArrayList<>();

        for (String valueKey : values.keySet()) {
            if (iterate > 0)
                sql += " OR ";
            sql += (valueKey + " LIKE CONCAT( '%',?,'%')");

            iterate++;
        }

        sql+= " ORDER BY "+ columnName + " " + sortOrder.toString();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DatabaseUtil.prepareStmt(stmt, values, 0);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                table.add(mapToEntity(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return table;
    }

    public List<E> searchTable(String tableName, String show, String join, Map<String, Object> values){
        int iterate = 0;
        String sql = "SELECT " + tableName + ".* , " + show + " FROM " + tableName + join + " WHERE ";

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

    public String DashboardCount(String function, String count, String tableName, String condition,  Map<String, Object> values){
        int iterate = 0;
        String total = null;
        String sql = "SELECT " + function + "(" + count +") AS total FROM " + tableName + " WHERE " + condition + " >= DATE_SUB(?, INTERVAL 7 DAY)";

        if(values != null){
            sql += " AND ";
            for (String valueKey : values.keySet()) {
                if (iterate > 0)
                    sql += " OR ";
                sql += (valueKey + " = '" + values.get(valueKey) + "'");

                iterate++;
            }
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DatabaseUtil.prepareStmt(stmt, values, 0);
            stmt.setString(1, dateString);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                total = rs.getString("total");
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return total;
    }

    public String DashboardCount(String function, String count, String tableName, String condition){
        String total = null;
        String sql = "SELECT " + function + "(" + count +") AS total FROM " + tableName + " WHERE " + condition + " >= DATE_SUB(?, INTERVAL 7 DAY)";


        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dateString);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                total = rs.getString("total");
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return total;
    }

    public LinkedHashMap<String, Integer> chartCount(String tableName,String condition, String groupName,String function, String count, String duration, String time){
        LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
        // String sql = "SELECT " + groupName + "(STR_TO_DATE(" + count + ", '%Y-%m-%d %H:%i:%s')) AS Bulan," + function + " AS Total FROM " + tableName + " WHERE " + condition + " STR_TO_DATE(" + count + ", '%Y-%m-%d %H:%i:%s') >= DATE_SUB(?, INTERVAL " + duration + " " + time + ") GROUP BY Bulan ORDER BY " + time + "(STR_TO_DATE(" + count + ", '%Y-%m-%d %H:%i:%s')) ASC ";

        String sql;
        if(condition == null){
            sql = "SELECT " + groupName + "(STR_TO_DATE(" + count + ", '%Y-%m-%d %H:%i:%s')) AS Bulan," + function + " AS Total FROM " + tableName + " WHERE STR_TO_DATE(" + count + ", '%Y-%m-%d %H:%i:%s') >= DATE_SUB(?, INTERVAL " + duration + " " + time + ") GROUP BY Bulan ORDER BY " + time + "(STR_TO_DATE(" + count + ", '%Y-%m-%d %H:%i:%s')) ASC ";
        } else {
            sql = "SELECT " + groupName + "(STR_TO_DATE(" + count + ", '%Y-%m-%d %H:%i:%s')) AS Bulan," + function + " AS Total FROM " + tableName + " WHERE " + condition + " STR_TO_DATE(" + count + ", '%Y-%m-%d %H:%i:%s') >= DATE_SUB(?, INTERVAL " + duration + " " + time + ") GROUP BY Bulan ORDER BY " + time + "(STR_TO_DATE(" + count + ", '%Y-%m-%d %H:%i:%s')) ASC ";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, dateString);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                String bulan = rs.getString("Bulan");
                Integer total = rs.getInt("Total");
                data.put(bulan, total);
            }

        } catch (Exception e) {e.printStackTrace(); }

        return data;
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
