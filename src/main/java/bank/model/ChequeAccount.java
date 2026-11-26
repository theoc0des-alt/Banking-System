package bank.model;

import java.time.LocalDate;

public class ChequeAccount extends Account {

    private double overdraftLimit;

    public ChequeAccount(String accountNumber, String branch, double initialBalance, double overdraftLimit) {
        super(accountNumber, branch, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    // Used when loading an existing account from the database
    public ChequeAccount(String accountNumber, String branch, double balance, double overdraftLimit, LocalDate dateOpened) {
        super(accountNumber, branch, balance, dateOpened);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal must be more than 0");
        }
        if (amount > getBalance() + overdraftLimit) {
            throw new IllegalArgumentException("Overdraft limit exceeded");
        }
        setBalance(getBalance() - amount);   // balance can go negative, up to the limit
    }

    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }
}