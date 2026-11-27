package bank.dao;

import bank.model.CompanyCustomer;
import bank.model.Customer;
import bank.model.IndividualCustomer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class JdbcCustomerDAO implements CustomerDAO {

    private static final String SELECT = "SELECT * FROM customers ";

    @Override
    public void save(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, customer_type, address, phone, username, password_hash, "
                + "first_name, surname, id_number, employer_name, employer_address, company_name, registration_number) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            setText(ps, 1, customer.getCustomerId());
            setText(ps, 3, customer.getAddress());
            setText(ps, 4, customer.getPhone());
            setText(ps, 5, customer.getUsername());
            setText(ps, 6, customer.getPasswordHash());
            if (customer instanceof IndividualCustomer ind) {
                setText(ps, 2, "INDIVIDUAL");
                setText(ps, 7, ind.getFirstName());
                setText(ps, 8, ind.getSurname());
                setText(ps, 9, ind.getIdNumber());
                setText(ps, 10, ind.getEmployerName());
                setText(ps, 11, ind.getEmployerAddress());
                setText(ps, 12, null);
                setText(ps, 13, null);
            } else if (customer instanceof CompanyCustomer comp) {
                setText(ps, 2, "COMPANY");
                setText(ps, 7, null);
                setText(ps, 8, null);
                setText(ps, 9, null);
                setText(ps, 10, null);
                setText(ps, 11, null);
                setText(ps, 12, comp.getCompanyName());
                setText(ps, 13, comp.getRegistrationNumber());
            } else {
                throw new IllegalArgumentException("Unknown customer type");
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save customer: " + e.getMessage(), e);
        }
    }

    @Override
    public Customer findByUsername(String username) {
        return findOne(SELECT + "WHERE username = ?", username);
    }

    @Override
    public Customer findById(String customerId) {
        return findOne(SELECT + "WHERE customer_id = ?", customerId);
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + "ORDER BY customer_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read customers: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void update(Customer customer) {
        String sql = "UPDATE customers SET address = ?, phone = ?, password_hash = ?, first_name = ?, surname = ?, "
                + "employer_name = ?, employer_address = ?, company_name = ? WHERE customer_id = ?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            setText(ps, 1, customer.getAddress());
            setText(ps, 2, customer.getPhone());
            setText(ps, 3, customer.getPasswordHash());
            if (customer instanceof IndividualCustomer ind) {
                setText(ps, 4, ind.getFirstName());
                setText(ps, 5, ind.getSurname());
                setText(ps, 6, ind.getEmployerName());
                setText(ps, 7, ind.getEmployerAddress());
                setText(ps, 8, null);
            } else if (customer instanceof CompanyCustomer comp) {
                setText(ps, 4, null);
                setText(ps, 5, null);
                setText(ps, 6, null);
                setText(ps, 7, null);
                setText(ps, 8, comp.getCompanyName());
            }
            setText(ps, 9, customer.getCustomerId());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Customer not found");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not update customer: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String customerId) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM customers WHERE customer_id = ?")) {
            ps.setString(1, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not delete customer (they may still hold accounts): " + e.getMessage(), e);
        }
    }

    private Customer findOne(String sql, String value) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read customer: " + e.getMessage(), e);
        }
    }

    // Rebuilds the right kind of Customer object from one database row
    private Customer map(ResultSet rs) throws SQLException {
        String type = rs.getString("customer_type");
        String id = rs.getString("customer_id");
        String address = rs.getString("address");
        String phone = rs.getString("phone");
        String username = rs.getString("username");
        Customer customer;
        if (type.equals("INDIVIDUAL")) {
            IndividualCustomer ind = new IndividualCustomer(id, address, phone, username, "unused",
                    rs.getString("first_name"), rs.getString("surname"), rs.getString("id_number"));
            String employer = rs.getString("employer_name");
            if (employer != null) {
                ind.setEmployer(employer, rs.getString("employer_address"));
            }
            customer = ind;
        } else {
            customer = new CompanyCustomer(id, address, phone, username, "unused",
                    rs.getString("company_name"), rs.getString("registration_number"));
        }
        customer.restorePasswordHash(rs.getString("password_hash"));
        return customer;
    }

    private static void setText(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value);
        }
    }
}