package bank;

import bank.model.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Account[] accounts = {
            new SavingsAccount("S001", "Gaborone", 1000, 12),
            new SavingsAccount("S002", "Gaborone", 1000, 6, LocalDate.now().minusMonths(7)),
            new InvestmentAccount("I001", "Gaborone", 500),
            new ChequeAccount("C001", "Gaborone", 200, 500)
        };

        for (Account a : accounts) {
            try {
                a.withdraw(300);   // same call, different behaviour (polymorphism)
                System.out.println(a.getAccountNumber() + " withdrew 300, balance: " + a.getBalance());
            } catch (Exception e) {
                System.out.println(a.getAccountNumber() + " failed: " + e.getMessage());
            }
            if (a instanceof InterestBearing ib) {
                System.out.println("  interest due: " + ib.calculateInterest());
            }
        }
    }
}