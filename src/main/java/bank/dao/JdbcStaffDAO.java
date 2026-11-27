package bank.dao;

import bank.model.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcStaffDAO implements StaffDAO {

    @Override
    public void save(Staff staff) {
        String sql = "INSERT INTO staff (staff_id, staff_name, username, password_hash) VALUES (?, ?, ?, ?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, staff.getStaffId());
            ps.setString(2, staff.getDisplayName());
            ps.setString(3, staff.getUsername());
            ps.setString(4, staff.getPasswordHash());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save staff: " + e.getMessage(), e);
        }
    }

    @Override
    public Staff findByUsername(String username) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM staff WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read staff: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Staff> findAll() {
        List<Staff> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM staff ORDER BY staff_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read staff: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void update(Staff staff) {
        String sql = "UPDATE staff SET staff_name = ?, password_hash = ? WHERE staff_id = ?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, staff.getDisplayName());
            ps.setString(2, staff.getPasswordHash());
            ps.setString(3, staff.getStaffId());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Staff member not found");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not update staff: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String staffId) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM staff WHERE staff_id = ?")) {
            ps.setString(1, staffId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not delete staff: " + e.getMessage(), e);
        }
    }

    private Staff map(ResultSet rs) throws SQLException {
        Staff staff = new Staff(rs.getString("staff_id"), rs.getString("staff_name"),
                rs.getString("username"), "unused");
        staff.restorePasswordHash(rs.getString("password_hash"));
        return staff;
    }
}