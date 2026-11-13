package bank.model;

public class ChequeAccount extends Account {

    private double overdraftLimit;

    public ChequeAccount(String accountNumber, String branch, double initialBalance, double overdraftLimit) {
        super(accountNumber, branch, initialBalance);
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