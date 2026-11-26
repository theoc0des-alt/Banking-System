package bank;

import bank.model.*;
import java.time.LocalDate;
import bank.controller.LoginController;
import bank.dao.*;
import bank.controller.AccountController;

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
                a.withdraw(300); // same call, different behaviour (polymorphism)
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
        StaffDAO staffDao = new InMemoryStaffDAO();
        staffDao.save(new Staff("ST1", "Neo Phiri", "neo", "staff123"));
        LoginController login = new LoginController(dao, staffDao);

        System.out.println("Login succeeded with wrong password? " + login.login("kago", "nope"));
        System.out.println("Login succeeded with unknown user? " + login.login("ghost", "pass123"));
        System.out.println("Login succeeded with right details? " + login.login("kago", "pass123"));
        System.out.println("Logged in as: " + login.getCurrentCustomer().getDisplayName()
                + " (" + login.getCurrentRole() + ")");
        login.logout();
        System.out.println("Still logged in after logout? " + login.isLoggedIn());

        login.login("neo", "staff123");
        System.out.println("Logged in as: " + login.getCurrentStaff().getDisplayName()
                + " (" + login.getCurrentRole() + ")");
        login.logout();

        LoginController lockTest = new LoginController(dao, staffDao);
        lockTest.login("kago", "x");
        lockTest.login("kago", "x");
        lockTest.login("kago", "x");
        try {
            lockTest.login("kago", "pass123");
        } catch (IllegalStateException e) {
            System.out.println("Locked: " + e.getMessage());
        }
                System.out.println("--- Deposit and withdraw ---");
        AccountDAO accountDao = new InMemoryAccountDAO();
        TransactionDAO transactionDao = new InMemoryTransactionDAO();
        accountDao.save(new SavingsAccount("S100", "Gaborone", 1000, 6, LocalDate.now().minusMonths(7)), "CU9");
        accountDao.save(new ChequeAccount("C100", "Gaborone", 200, 500), "CU9");
        AccountController accountController = new AccountController(accountDao, transactionDao);

        System.out.println("Balance after depositing 250: " + accountController.deposit("S100", 250));
        System.out.println("Balance after withdrawing 100: " + accountController.withdraw("S100", 100));
        try {
            accountController.deposit("S100", -5);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        try {
            accountController.withdraw("C100", 900);
        } catch (Exception e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        System.out.println("Cheque balance after withdrawing 600 (overdraft): " + accountController.withdraw("C100", 600));
        System.out.println("Transactions on S100: " + accountController.getHistory("S100").size());

    }
}