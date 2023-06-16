package siplaundry.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import siplaundry.util.DatabaseUtil;

public class ReportRepository {
    final Connection conn = DatabaseUtil.getConnection();

    public ResultSet getResult(String query) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.executeQuery();
    }
}
