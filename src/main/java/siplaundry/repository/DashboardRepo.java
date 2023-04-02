package siplaundry.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import siplaundry.util.DatabaseUtil;

public class DashboardRepo {

    private static Connection conn = DatabaseUtil.getConnection();
    private static Calendar call = Calendar.getInstance();

    public int getIncomePerMonth(Date date){
        call.setTime(date);

        int total = 0;
        String sql = "SELECT SUM(amount) AS total FROM transaction WHERE MONTH(transaction_date) = ? AND YEAR(transaction_date) = ? AND payment_status = 'paid' ";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, call.get((Calendar.MONTH) + 1));
            stmt.setInt(2, call.get(Calendar.YEAR));

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                total = rs.getInt("total");
            }
        } catch( SQLException e){
            e.printStackTrace();
        }
        return total;
    }

    public int getCustomerTotal(){
        int total = 0;
        String sql = "SELECT COUNT(*) AS total FROM customers";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;

    }

    public int getCashierTotal(){
        int total = 0;
        String sql = "SELECT COUNT(*) AS TOTAL FROM users WHERE role = 'cashier' ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                total = rs.getInt("total");
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public int getTotalUnpaid(){
        int total = 0;
        String sql = "SELECT SUM(amount) AS TOTAL from transaction WHERE payment_status = 'unpaid' ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                total = rs.getInt("total");
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public int getProcessedTotal(){
        int total = 0;
        String sql = "SELECT COUNT(*) AS TOTAL from transaction WHERE status = 'process' ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                total = rs.getInt("total");
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getFinishTotal(){
        int total = 0;
        String sql = "SELECT COUNT(*) AS TOTAL from transaction WHERE status = 'finish' ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                total = rs.getInt("total");
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getTakenTotal(){
        int total = 0;
        String sql = "SELECT COUNT(*) AS TOTAL from transaction WHERE status = 'taken' ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                total = rs.getInt("total");
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
