package bank.dao;

import bank.model.Account;
import java.util.List;

public interface AccountDAO {
    void save(Account account, String customerId);
    Account findByAccountNumber(String accountNumber);   // returns null if not found
    List<Account> findByCustomerId(String customerId);
    void update(Account account);
}