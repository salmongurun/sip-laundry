package siplaundry.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import siplaundry.util.DatabaseUtil;

public class StatusService {
    final Connection conn = DatabaseUtil.getConnection();
    
    public void ChangeToFinishAuto(){
        LocalDate deadline = LocalDate.now();

        String sql = " UPDATE `transactions` JOIN `transaction_details` ON `transactions`.`transaction_id` = `transaction_details`.`transaction_id` JOIN `laundries` ON `laundries`.`laundry_id` = `transaction_details`.`laundry_id` SET `transactions`.`status` = 'finish' WHERE `transactions`.`status` = 'process' AND ( (`laundries`.`IsExpress` = 1 AND `transactions`.`transaction_date` <= DATE_SUB( ?, INTERVAL(`transactions`.`ritard` + 2)DAY)) OR (`laundries`.`IsExpress` = 0 AND `transactions`.`transaction_date` <= DATE_SUB( ?, INTERVAL(`transactions`.`ritard` + 2)DAY))); ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
           stmt.setString(1, deadline.toString());
           stmt.setString(2, deadline.toString());
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

}
