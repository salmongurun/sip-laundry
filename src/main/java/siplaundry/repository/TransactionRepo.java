package siplaundry.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import siplaundry.data.LaundryStatus;
import siplaundry.data.PaymentStatus;
import siplaundry.data.SortingOrder;
import siplaundry.entity.UserEntity;
import siplaundry.util.DatabaseUtil;
import siplaundry.entity.TransactionDashboardEntity;
import siplaundry.entity.TransactionEntity;

public class TransactionRepo extends Repo<TransactionEntity> {

    private final static String tableName = "transactions";
    private static String getid = "transaction_id";

    public Integer add(TransactionEntity trans) {
        String sql = "INSERT INTO " + tableName
                + " (`transaction_date`,`retard` ,`pickup_date`, `status`, `payment_status`, `amount`, `paid_off`, `user_id`, `customer_id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDate(1, new Date(trans.gettransactionDate().getTime()));
            stmt.setInt(2, trans.getRetard());
            stmt.setDate(3, new Date(trans.getpickupDate().getTime()));
            stmt.setString(4, trans.getstatus().toString());
            stmt.setString(5, trans.getPaymentStatus().toString());
            stmt.setInt(6, trans.getamount());
            stmt.setInt(7, trans.getPaid_off());
            stmt.setInt(8, trans.getUserID().getID());
            stmt.setInt(9, trans.getCustomerID().getid());
            
            if (trans.getCustomerID() == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, new Date(trans.getpickupDate().getTime()));
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<TransactionEntity> get() {
        return super.getAll(tableName);
    }

    public TransactionEntity get(Integer id) {
        return super.get(tableName, getid, id);
    }

    public List<TransactionEntity> sortTable(String show, String join, Map<String, Object> values,String column, SortingOrder sortOrder){
        return super.sortTable(tableName, show, join, values, column, sortOrder);
    }

    public List<TransactionEntity> get(Map<String, Object> values) {
        return super.get(tableName, values);
    }

    public List<TransactionEntity> search(Map<String, Object> values) {
        return super.search(tableName, values);
    }

    public List<TransactionEntity> searchByUser(UserEntity user, Map<String, Object> values) {
        return super.searchByUser(tableName, user, values);
    }

    public String DashboardCount(String function, String count,  Map<String, Object> values){
       return super.DashboardCount(function , count, tableName, "pickup_date", values);
    }

    public String DashboardCount(String function, String count){
        return super.DashboardCount(function, count, tableName, "pickup_date");
    }

    public List<TransactionEntity> searchTable(String show, String join, Map<String, Object> values){
        return super.searchTable(tableName, show, join, values);
    }

    public LinkedHashMap<String, Integer> chartCount(String condition, String groupName,String function, String count, String duration, String time){
        return super.chartCount(tableName, condition, groupName, function,count, duration, time);
    }
    
    public Map<String, String> detailCount(){
        String sql = "SELECT `transactions`.`transaction_id` AS id , COUNT(`transaction_details`.`qty`) AS items FROM `transactions` JOIN `transaction_details` ON `transactions`.`transaction_id` = `transaction_details`.`transaction_id` GROUP BY `transactions`.`transaction_id`;";
        Map<String, String> table = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                table.put(rs.getString(0), rs.getString(1));
            }
        } catch (SQLException e) { }
        return table;
    }

    public List<TransactionDashboardEntity> DashboardTable(){
        String sql = "SELECT `transactions`.`transaction_id` AS transaction_id, `customers`.`customer_id` AS customer_id, TIMESTAMPDIFF(HOUR, `transactions`.`transaction_date`, NOW()) AS diff_hours, COUNT(`transaction_details`.`transaction_id`) AS items_count FROM `transactions` JOIN `customers` ON `transactions`.`customer_id` = `customers`.`customer_id` JOIN `transaction_details` ON `transactions`.`transaction_id` = `transaction_details`.`transaction_id` GROUP BY `transaction_details`.`transaction_id` ORDER BY diff_hours ASC;";
        List<TransactionDashboardEntity> transactions = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                transactions.add(new TransactionDashboardEntity(
                    this.get(rs.getInt("transaction_id")),
                    (new CustomerRepo()).get(rs.getInt("customer_id")),
                    rs.getLong("diff_hours"),
                    rs.getInt("items_count"))
                );
            }
        } catch (SQLException e) { }

        return transactions;
    }

    public boolean Update(TransactionEntity trans) {
        String sql = "UPDATE " + tableName
                + " SET transaction_date = ?, retard = ?, pickup_date = ?, status = ?, payment_status = ?, amount = ?, paid_off = ?, user_id = ?, customer_id = ? WHERE transaction_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new Date(trans.gettransactionDate().getTime()));
            stmt.setInt(2, trans.getRetard());
            stmt.setDate(3, new Date(trans.getpickupDate().getTime()));
            stmt.setString(4, trans.getstatus().toString());
            stmt.setString(5, trans.getPaymentStatus().toString());
            stmt.setInt(6, trans.getamount());
            stmt.setInt(7, trans.getPaid_off());
            stmt.setInt(8, trans.getUserID().getID());
            stmt.setInt(9, trans.getCustomerID().getid());
            stmt.setInt(10, trans.getid());

            stmt.executeUpdate();

            return stmt.getUpdateCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean Update(Map<String, Object> setValues, Map<String, Object> whereValues){
        int setCount = 0;
        int whereCount = 0;
        String sql = "UPDATE " + tableName + " SET ";

        for (String setValueKey : setValues.keySet()) {
            if (setCount > 0)
                sql += ", ";
            sql += setValueKey + " = ?";
            setCount++;
        }

        if (whereValues != null && !whereValues.isEmpty()) {
            sql += " WHERE ";
            for (String whereValueKey : whereValues.keySet()) {
                if (whereCount > 0)
                    sql += " AND ";
                sql += whereValueKey + " = ?";


                whereCount++;
            }
        }

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            DatabaseUtil.prepareStmt(stmt, setValues, 0);
            DatabaseUtil.prepareStmt(stmt, whereValues, setCount);
            System.out.println(stmt.toString());
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

    public TransactionEntity mapToEntity(ResultSet result) throws SQLException {
        int custId = result.getInt("customer_id");
        int userId = result.getInt("user_id");

        TransactionEntity transaction = new TransactionEntity(
                result.getDate("transaction_date"),
                result.getInt("retard"),
                result.getDate("pickup_date"),
                LaundryStatus.valueOf(result.getString("status")),
                PaymentStatus.valueOf(result.getString("payment_status")),
                result.getInt("amount"),
                result.getInt("paid_off"),
                new UsersRepo().get(userId),
                new CustomerRepo().get(custId));

        transaction.setid(result.getInt("transaction_id"));
        return transaction;
    }
}
