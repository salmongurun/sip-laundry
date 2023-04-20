package siplaundry.service;

import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import siplaundry.util.DatabaseUtil;

public class StatusService {
    final Connection conn = DatabaseUtil.getConnection();
    Date date = new Date();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateString = sdf.format(date);

    public void ChangeToFinishAuto(){
        String sql = "UPDATE `transactions` INNER JOIN `transaction_details` ON `transactions`.`transaction_id` = `transaction_details`.`transaction_id` INNER JOIN `laundries` ON `transaction_details`.`laundry_id` = `laundries`.`laundry_id` SET `transactions`.`status` = 'finish' WHERE `transactions`.`status` = 'process' AND `transactions`.`transaction_date` <= (DATE_SUB(?, INTERVAL(CASE WHEN `laundries`.`IsExpress` = 1 THEN `transactions`.`retard` + 1 ELSE `transactions`.`retard` + 3 END)DAY))";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
           stmt.setString(1, dateString);
           stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void ChangeToFinishManual(int id){
        String sql = "UPDATE `transactions` SET `status` = 'finish' WHERE transaction_id = ? ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CancelToBeFinished(int id){  
        String sql = "UPDATE `transactions` SET `status` = 'process' WHERE transaction_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ChangeToPaid(int id){
        String sql = "UPDATE `transactions` SET `payment_status` = 'paid' WHERE `transaction_id` = ? ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            System.out.println(stmt.toString());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CancelTransaction(int id){  
        String sql = "UPDATE transactions SET status = 'cancelled' WHERE transaction_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
