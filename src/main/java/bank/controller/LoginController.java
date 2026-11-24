package bank.controller;

import bank.dao.CustomerDAO;
import bank.dao.StaffDAO;
import bank.model.Customer;
import bank.model.Role;
import bank.model.Staff;

public class LoginController {

    private final CustomerDAO customerDAO;
    private final StaffDAO staffDAO;
    private Customer currentCustomer;
    private Staff currentStaff;
    private Role currentRole;
    private boolean loggedIn = false;
    private int failedAttempts = 0;
    private final int maxAttempts = 3;

    public LoginController(CustomerDAO customerDAO, StaffDAO staffDAO) {
        this.customerDAO = customerDAO;
        this.staffDAO = staffDAO;
    }

    public boolean login(String username, String password) {
        if (failedAttempts >= maxAttempts) {
            throw new IllegalStateException("Too many failed attempts. Login is locked.");
        }
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            failedAttempts++;
            return false;
        }

        Customer customer = customerDAO.findByUsername(username);
        if (customer != null && customer.checkPassword(password)) {
            currentCustomer = customer;
            currentRole = Role.CUSTOMER;
            loggedIn = true;
            failedAttempts = 0;
            return true;
        }

        Staff staff = staffDAO.findByUsername(username);
        if (staff != null && staff.checkPassword(password)) {
            currentStaff = staff;
            currentRole = Role.STAFF;
            loggedIn = true;
            failedAttempts = 0;
            return true;
        }

        failedAttempts++;
        return false;   // same answer whether the username or the password was wrong
    }

    public void logout() {
        currentCustomer = null;
        currentStaff = null;
        currentRole = null;
        loggedIn = false;
    }

    public boolean isLoggedIn() { return loggedIn; }
    public Role getCurrentRole() { return currentRole; }
    public Customer getCurrentCustomer() { return currentCustomer; }
    public Staff getCurrentStaff() { return currentStaff; }
    public int getRemainingAttempts() { return maxAttempts - failedAttempts; }
}