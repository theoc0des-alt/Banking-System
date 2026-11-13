package bank.model;

public class InvestmentAccount extends Account implements InterestBearing {

    private double interestRate = 0.05;     // 5% per month
    private double minDeposit = 500.00;     // BWP500 to open

    public InvestmentAccount(String accountNumber, String branch, double initialDeposit) {
        super(accountNumber, branch, initialDeposit);
        if (initialDeposit < minDeposit) {
            throw new IllegalArgumentException("Investment accounts need at least BWP500.00 to open");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal must be more than 0");
        }
        if (amount > getBalance()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        setBalance(getBalance() - amount);
    }

    @Override
    public double calculateInterest() {
        return Math.round(getBalance() * interestRate * 100.0) / 100.0;
    }

    @Override
    public void payInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            deposit(interest);
        }
    }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public double getMinDeposit() { return minDeposit; }
}