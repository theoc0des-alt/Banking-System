package bank.controller;

import bank.dao.CustomerDAO;
import bank.model.Customer;

public class LoginController {

    private final CustomerDAO customerDAO;
    private Customer currentCustomer;
    private boolean loggedIn = false;
    private int failedAttempts = 0;
    private final int maxAttempts = 3;

    public LoginController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public boolean login(String username, String password) {
        if (failedAttempts >= maxAttempts) {
            throw new IllegalStateException("Too many failed attempts. Login is locked.");
        }
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            failedAttempts++;
            return false;
        }

        Customer customer = customerDAO.findByUsername(username);   // step 1 in your sequence diagram
        if (customer != null && customer.checkPassword(password)) { // step 2: checkPassword
            currentCustomer = customer;
            loggedIn = true;
            failedAttempts = 0;
            return true;
        }

        failedAttempts++;
        return false;   // same answer whether the username or the password was wrong
    }

    public void logout() {
        currentCustomer = null;
        loggedIn = false;
    }

    public boolean isLoggedIn() { return loggedIn; }
    public Customer getCurrentCustomer() { return currentCustomer; }
    public int getRemainingAttempts() { return maxAttempts - failedAttempts; }
}