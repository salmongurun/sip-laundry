package siplaundry.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import siplaundry.data.LaundryStatus;
import siplaundry.data.PaymentStatus;
import siplaundry.entity.TransactionEntity;
import siplaundry.util.DatabaseUtil;

public class ReportRepository extends Repo<TransactionEntity> {
    public List<TransactionEntity> result(Set<LaundryStatus> status, LocalDate startDate, LocalDate endDate) {
        List<TransactionEntity> transactions = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM transactions WHERE status IN ");

        sql.append(generateStatus(status));
        sql.append(generateBetween(startDate, endDate));
        sql.append("ORDER BY transaction_date DESC");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) transactions.add(mapToEntity(rs));

        } catch (SQLException e) { e.printStackTrace(); }

        return transactions;
    }

    private String generateStatus(Set<LaundryStatus> status) {
        StringBuilder generated = new StringBuilder("(");

        for (LaundryStatus sts: status) {
            generated.append("'")
                .append(sts.toString())
                .append("',");
        }

        if(status.isEmpty()) generated.append("null,");

        int genLength = generated.length();

        generated.delete((genLength - 1), genLength);
        generated.append(") ");

        return generated.toString();
    }

    private String generateBetween(LocalDate startDate, LocalDate endDate) {
        String generated = "AND transaction_date ";

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
            new CustomerRepo().get(custId)
        );

        transaction.setid(result.getInt("transaction_id"));
        return transaction;
    }
}
