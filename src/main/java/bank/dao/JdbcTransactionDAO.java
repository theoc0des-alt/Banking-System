package bank.dao;

import bank.model.Transaction;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransactionDAO implements TransactionDAO {

    @Override
    public void save(Transaction t) {
        String sql = "INSERT INTO transactions (transaction_id, account_number, transaction_type, amount, "
                + "txn_date, txn_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getTransactionId());
            ps.setString(2, t.getAccountNumber());
            ps.setString(3, t.getType());
            ps.setDouble(4, t.getAmount());
            ps.setDate(5, Date.valueOf(t.getDate()));
            ps.setTime(6, Time.valueOf(t.getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save transaction: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        List<Transaction> result = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY txn_date, txn_time";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read transactions: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM transactions ORDER BY transaction_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read transactions: " + e.getMessage(), e);
        }
        return result;
    }

    private Transaction map(ResultSet rs) throws SQLException {
        return new Transaction(rs.getString("transaction_id"), rs.getString("transaction_type"),
                rs.getDouble("amount"), rs.getString("account_number"),
                rs.getDate("txn_date").toLocalDate(), rs.getTime("txn_time").toLocalTime());
    }
}