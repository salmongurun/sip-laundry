package siplaundry.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import siplaundry.data.SortingOrder;
import siplaundry.entity.CustomerEntity;

public class CustomerRepo extends Repo<CustomerEntity> {

    private static String tableName = CustomerEntity.tableName;
    private static String getid = "customer_id";

    public Integer add(CustomerEntity cust) {
        String sql = "INSERT INTO " + tableName + " (`name`,`phone`, `address`) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cust.getname());
            stmt.setString(2, cust.getphone());
            stmt.setString(3, cust.getAddress());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<CustomerEntity> get() {
        return super.getAll(tableName);
    }

    public CustomerEntity get(Integer id) {
        return super.get(tableName, getid, id);
    }

    public List<CustomerEntity> get(Map<String, Object> values) {
        return super.get(tableName, values);
    }

    public List<CustomerEntity> search(Map<String, Object> values) {
        return super.search(tableName, values);
    }

    public List<CustomerEntity> search(Map<String, Object> values, String column, SortingOrder ordering) {
        return super.search(tableName, values, column, ordering);
    }

    public boolean Update(CustomerEntity cust) {
        String sql = "UPDATE " + tableName + " SET name = ?, phone = ?, address = ? WHERE customer_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cust.getname());
            stmt.setString(2, cust.getphone());
            stmt.setString(3, cust.getAddress());
            stmt.setInt(4, cust.getid());

            stmt.executeUpdate();
            return stmt.getUpdateCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<CustomerEntity> sortBy(String column, String condition){
        return super.sortBy(tableName, column, condition);
    }

    public boolean delete(int id) {
        return super.delete(tableName, getid, id);
    }

    @Override
    public CustomerEntity mapToEntity(ResultSet result) throws SQLException {
        CustomerEntity customer = new CustomerEntity(
                result.getString("name"),
                result.getString("phone"),
                result.getString("address")
        );

        customer.setid(result.getInt("customer_id"));
        return customer;
    }

}
