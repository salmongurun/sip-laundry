package siplaundry.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import siplaundry.entity.CustomerEntity;

public class CustomerRepo extends Repo<CustomerEntity> {

    private static String tableName = CustomerEntity.tableName;
    private static String getid = "customer_id";

    public Integer add(CustomerEntity cust) {
        String sql = "INSERT INTO " + tableName + " (`name`,`phone`) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cust.getname());
            stmt.setString(2, cust.getphone());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                System.out.println(stmt.toString());
                return rs.getInt(1);
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

    public boolean Update(CustomerEntity cust) {
        String sql = "UPDATE " + tableName + " SET name = ?, phone = ? WHERE customer_id = ?";
        System.out.println("jalan");
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cust.getname());
            stmt.setString(2, cust.getphone());
            stmt.setInt(3, cust.getid());

            stmt.executeUpdate();
            System.out.println(stmt.toString());
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
    public CustomerEntity mapToEntity(ResultSet result) throws SQLException {
        CustomerEntity customer = new CustomerEntity(
                result.getString("name"),
                result.getString("phone"));

        customer.setid(result.getInt("customer_id"));
        return customer;
    }

}
