package bank.model;

import java.time.LocalDate;

public class SavingsAccount extends Account implements InterestBearing {

    private double interestRate = 0.0005;   // 0.05% per month
    private int lockPeriodMonths;           // 6 or 12

    public SavingsAccount(String accountNumber, String branch, double initialBalance, int lockPeriodMonths) {
        super(accountNumber, branch, initialBalance);
        this.lockPeriodMonths = checkLockPeriod(lockPeriodMonths);
    }

    public SavingsAccount(String accountNumber, String branch, double initialBalance,
                          int lockPeriodMonths, LocalDate dateOpened) {
        super(accountNumber, branch, initialBalance, dateOpened);
        this.lockPeriodMonths = checkLockPeriod(lockPeriodMonths);
    }

    private int checkLockPeriod(int months) {
        if (months != 6 && months != 12) {
            throw new IllegalArgumentException("Lock period must be 6 or 12 months");
        }
        return months;
    }

    public LocalDate getUnlockDate() {
        return getDateOpened().plusMonths(lockPeriodMonths);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal must be more than 0");
        }
        if (LocalDate.now().isBefore(getUnlockDate())) {
            throw new IllegalStateException("Withdrawals allowed from " + getUnlockDate());
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
    public int getLockPeriodMonths() { return lockPeriodMonths; }
}