package bank.model;

import java.time.LocalDate;

public abstract class Account {

    private String accountNumber;
    private double balance;
    private String branch;
    private LocalDate dateOpened;
    private String status;

    public Account(String accountNumber, String branch, double initialBalance) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.balance = initialBalance;
        this.dateOpened = LocalDate.now();
        this.status = "Active";
    }

    // Overloaded deposit methods (same name, different parameters)
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit must be more than 0");
        }
        balance += amount;
    }

    public void deposit(double amount, String note) {
        deposit(amount);   // note is recorded later as a Transaction
    }

    // Abstract: every subclass must write its own version
    public abstract void withdraw(double amount);

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    protected void setBalance(double amount) { this.balance = amount; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public LocalDate getDateOpened() { return dateOpened; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}