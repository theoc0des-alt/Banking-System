package bank.dao;

import bank.model.Customer;
import java.util.List;

public interface CustomerDAO {
    void save(Customer customer);                    
    Customer findByUsername(String username);        
    Customer findById(String customerId);           
    List<Customer> findAll();                        
    void update(Customer customer);                 
    void delete(String customerId);                  
}