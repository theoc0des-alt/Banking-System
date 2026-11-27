package bank.dao;

import bank.model.Account;
import java.util.List;

public interface AccountDAO {
    void save(Account account, String customerId);   
    Account findByAccountNumber(String accountNumber); 
    List<Account> findByCustomerId(String customerId); 
    List<Account> findAll();                         
    void update(Account account);                    
    void delete(String accountNumber);               
}