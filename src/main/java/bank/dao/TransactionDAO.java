package bank.dao;

import bank.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    void save(Transaction transaction);              
    List<Transaction> findByAccountNumber(String accountNumber);   
    List<Transaction> findAll();                     
}