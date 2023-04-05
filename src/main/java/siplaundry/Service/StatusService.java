package siplaundry.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import siplaundry.util.DatabaseUtil;

public class StatusService {
    final Connection conn = DatabaseUtil.getConnection();
    
    public void ChangeToFinish(){
        LocalDate deadline = LocalDate.now().minusDays(3);

        String sql = "UPDATE transactions SET status = 'finish' WHERE transaction_date <= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
           stmt.setString(1, deadline.toString());
           stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        }

        
    }

}
