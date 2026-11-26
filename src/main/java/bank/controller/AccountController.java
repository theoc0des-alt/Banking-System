package bank.controller;

import bank.dao.AccountDAO;
import bank.dao.TransactionDAO;
import bank.model.Account;
import bank.model.Transaction;
import java.util.List;
import java.util.UUID;
import bank.controller.AccountController;

public class AccountController {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public AccountController(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    public double deposit(String accountNumber, double amount) {
        checkAmount(amount);
        Account account = find(accountNumber);
        account.deposit(amount);
        accountDAO.update(account);
        transactionDAO.save(new Transaction(newId(), "DEPOSIT", amount, accountNumber));
        return account.getBalance();
    }

    public double withdraw(String accountNumber, double amount) {
        checkAmount(amount);
        Account account = find(accountNumber);
        account.withdraw(amount);              // each account type enforces its own rules
        accountDAO.update(account);
        transactionDAO.save(new Transaction(newId(), "WITHDRAWAL", amount, accountNumber));
        return account.getBalance();
    }

    public List<Account> getAccounts(String customerId) {
        return accountDAO.findByCustomerId(customerId);
    }

    public double getBalance(String accountNumber) {
        return find(accountNumber).getBalance();
    }

    public List<Transaction> getHistory(String accountNumber) {
        return transactionDAO.findByAccountNumber(accountNumber);
    }

    private void checkAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Enter a valid amount");
        }
    }

    private Account find(String accountNumber) {
        Account account = accountDAO.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }

    private String newId() {
        return "T" + UUID.randomUUID().toString().substring(0, 8);
    }
}