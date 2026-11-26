package bank.dao;

import bank.model.Transaction;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionDAO implements TransactionDAO {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAccountNumber().equals(accountNumber)) {
                result.add(t);
            }
        }
        return result;
    }
}