package bank.dao;

import bank.model.Customer;

public interface CustomerDAO {
    Customer findByUsername(String username);   // returns null if not found
    void save(Customer customer);
}