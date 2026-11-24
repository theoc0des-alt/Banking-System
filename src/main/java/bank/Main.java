package bank;

import bank.model.*;
import java.time.LocalDate;
import bank.controller.LoginController;
import bank.dao.*;

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
                System.out.println("--- Login ---");
        CustomerDAO dao = new InMemoryCustomerDAO();
        dao.save(new IndividualCustomer("CU9", "Plot 5, Gaborone", "71111111",
                "kago", "pass123", "Kago", "Dube", "987654321"));
        LoginController login = new LoginController(dao);

        System.out.println("Login succeeded with wrong password? " + login.login("kago", "nope"));
        System.out.println("Login succeeded with unknown user? " + login.login("ghost", "pass123"));
        System.out.println("Login succeeded with right details? " + login.login("kago", "pass123"));
        System.out.println("Logged in as: " + login.getCurrentCustomer().getDisplayName());
        login.logout();
        System.out.println("Still logged in after logout? " + login.isLoggedIn());

        LoginController lockTest = new LoginController(dao);
        lockTest.login("kago", "x");
        lockTest.login("kago", "x");
        lockTest.login("kago", "x");
        try {
            lockTest.login("kago", "pass123");
        } catch (IllegalStateException e) {
            System.out.println("Locked: " + e.getMessage());
        }
        
    }
}