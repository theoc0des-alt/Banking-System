package bank.dao;

import bank.model.Account;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryAccountDAO implements AccountDAO {

    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, String> owners = new HashMap<>();   // accountNumber -> customerId

    @Override
    public void save(Account account, String customerId) {
        accounts.put(account.getAccountNumber(), account);
        owners.put(account.getAccountNumber(), customerId);
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public List<Account> findByCustomerId(String customerId) {
        List<Account> result = new ArrayList<>();
        for (Account a : accounts.values()) {
            if (customerId.equals(owners.get(a.getAccountNumber()))) {
                result.add(a);
            }
        }
        return result;
    }

    @Override
    public void update(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }
}