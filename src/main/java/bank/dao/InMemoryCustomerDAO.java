package bank.dao;

import bank.model.Customer;
import java.util.ArrayList;
import java.util.List;

public class InMemoryCustomerDAO implements CustomerDAO {

    private final List<Customer> customers = new ArrayList<>();

    @Override
    public Customer findByUsername(String username) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void save(Customer customer) {
        customers.add(customer);
    }
}