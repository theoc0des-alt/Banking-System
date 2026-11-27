package bank.dao;

import bank.model.Account;
import bank.model.ChequeAccount;
import bank.model.InvestmentAccount;
import bank.model.SavingsAccount;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountDAO implements AccountDAO {

    @Override
    public void save(Account account, String customerId) {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, branch, balance, "
                + "date_opened, status, lock_period_months, overdraft_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, account.getAccountNumber());
            ps.setString(2, customerId);
            ps.setString(4, account.getBranch());
            ps.setDouble(5, account.getBalance());
            ps.setDate(6, Date.valueOf(account.getDateOpened()));
            ps.setString(7, account.getStatus());
            if (account instanceof SavingsAccount s) {
                ps.setString(3, "SAVINGS");
                ps.setInt(8, s.getLockPeriodMonths());
                ps.setNull(9, Types.DECIMAL);
            } else if (account instanceof InvestmentAccount) {
                ps.setString(3, "INVESTMENT");
                ps.setNull(8, Types.INTEGER);
                ps.setNull(9, Types.DECIMAL);
            } else if (account instanceof ChequeAccount ch) {
                ps.setString(3, "CHEQUE");
                ps.setNull(8, Types.INTEGER);
                ps.setDouble(9, ch.getOverdraftLimit());
            } else {
                throw new IllegalArgumentException("Unknown account type");
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save account: " + e.getMessage(), e);
        }
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM accounts WHERE account_number = ?")) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read account: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Account> findByCustomerId(String customerId) {
        List<Account> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT * FROM accounts WHERE customer_id = ? ORDER BY account_number")) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read accounts: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Account> findAll() {
        List<Account> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM accounts ORDER BY account_number");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read accounts: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void update(Account account) {
        String sql = "UPDATE accounts SET branch = ?, balance = ?, status = ?, overdraft_limit = ? "
                + "WHERE account_number = ?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, account.getBranch());
            ps.setDouble(2, account.getBalance());
            ps.setString(3, account.getStatus());
            if (account instanceof ChequeAccount ch) {
                ps.setDouble(4, ch.getOverdraftLimit());
            } else {
                ps.setNull(4, Types.DECIMAL);
            }
            ps.setString(5, account.getAccountNumber());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Account not found");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not update account: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String accountNumber) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM accounts WHERE account_number = ?")) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not delete account (it may have transactions): " + e.getMessage(), e);
        }
    }

    // Rebuilds the right kind of Account object from one database row
    private Account map(ResultSet rs) throws SQLException {
        String type = rs.getString("account_type");
        String number = rs.getString("account_number");
        String branch = rs.getString("branch");
        double balance = rs.getDouble("balance");
        LocalDate opened = rs.getDate("date_opened").toLocalDate();
        Account account;
        switch (type) {
            case "SAVINGS" -> account = new SavingsAccount(number, branch, balance,
                    rs.getInt("lock_period_months"), opened);
            case "INVESTMENT" -> account = new InvestmentAccount(number, branch, balance, opened);
            case "CHEQUE" -> account = new ChequeAccount(number, branch, balance,
                    rs.getDouble("overdraft_limit"), opened);
            default -> throw new IllegalStateException("Unknown account type: " + type);
        }
        account.setStatus(rs.getString("status"));
        return account;
    }
}