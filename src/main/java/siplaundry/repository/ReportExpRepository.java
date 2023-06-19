package siplaundry.repository;

import siplaundry.entity.ExpenseEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportExpRepository extends Repo<ExpenseEntity>{

    public List<ExpenseEntity> resultExp(LocalDate startDate, LocalDate endDate) {
        List<ExpenseEntity> expense = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM expense WHERE ");

        sql.append(generateBetween(startDate, endDate));
        sql.append(" ORDER BY expense_date DESC");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) expense.add(mapToEntity(rs));

        } catch (SQLException e) { e.printStackTrace(); }

        return expense;
    }

    private String generateBetween(LocalDate startDate, LocalDate endDate) {
        String generated = "expense_date ";

       if(startDate != null && endDate != null) {
           generated += "BETWEEN '" + startDate.toString() + "' AND '" + endDate.toString() + "' ";
           return generated;
       }

       if(startDate != null) {
           generated += "BETWEEN '" + startDate.toString() + "' AND NOW() ";
           return generated;
       }
       return " ";
    }


    @Override
    public ExpenseEntity mapToEntity(ResultSet result) throws SQLException {
    int userId = result.getInt("user_id");
        ExpenseEntity expense = new ExpenseEntity(
                result.getString("name"),
                result.getDate("expense_date"),
                result.getInt("qty"),
                result.getInt("subtotal"),
                result.getInt("amount"),
                result.getString("optional"),
                new UsersRepo().get(userId)
        );

    expense.setid(result.getInt("expense_id"));
        return null;
    }
}
