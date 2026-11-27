package bank;

import bank.controller.AccountController;
import bank.controller.LoginController;
import bank.dao.*;
import bank.model.*;

public class Main {
    public static void main(String[] args) {

        // Set up the database and add the sample records the first time
        Database.createTables();
        CustomerDAO customerDao = new JdbcCustomerDAO();
        StaffDAO staffDao = new JdbcStaffDAO();
        AccountDAO accountDao = new JdbcAccountDAO();
        TransactionDAO transactionDao = new JdbcTransactionDAO();
        DataSeeder.seedIfEmpty(customerDao, staffDao, accountDao, transactionDao);

        System.out.println("--- Database records ---");
        System.out.println("Customers: " + customerDao.findAll().size());
        System.out.println("Staff: " + staffDao.findAll().size());
        System.out.println("Accounts: " + accountDao.findAll().size());
        System.out.println("Transactions: " + transactionDao.findAll().size());

        System.out.println("--- Login ---");
        LoginController login = new LoginController(customerDao, staffDao);
        System.out.println("Login succeeded with wrong password? " + login.login("kago", "nope"));
        System.out.println("Login succeeded with unknown user? " + login.login("ghost", "pass123"));
        System.out.println("Login succeeded with right details? " + login.login("kago", "pass123"));
        System.out.println("Logged in as: " + login.getCurrentCustomer().getDisplayName()
                + " (" + login.getCurrentRole() + ")");
        login.logout();
        login.login("neo", "staff123");
        System.out.println("Logged in as: " + login.getCurrentStaff().getDisplayName()
                + " (" + login.getCurrentRole() + ")");
        login.logout();

        LoginController lockTest = new LoginController(customerDao, staffDao);
        lockTest.login("kago", "x");
        lockTest.login("kago", "x");
        lockTest.login("kago", "x");
        try {
            lockTest.login("kago", "pass123");
        } catch (IllegalStateException e) {
            System.out.println("Locked: " + e.getMessage());
        }

        System.out.println("--- Deposit and withdraw ---");
        AccountController accounts = new AccountController(accountDao, transactionDao);
        System.out.println("Balance after depositing 250: " + accounts.deposit("S-1004", 250));
        System.out.println("Balance after withdrawing 100: " + accounts.withdraw("S-1004", 100));
        try {
            accounts.withdraw("S-1002", 50);
        } catch (Exception e) {
            System.out.println("Savings still locked: " + e.getMessage());
        }
        System.out.println("Cheque balance after withdrawing 900 (overdraft): " + accounts.withdraw("C-3002", 900));
        try {
            accounts.withdraw("C-3002", 600);
        } catch (Exception e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        System.out.println("Transactions on S-1004: " + accounts.getHistory("S-1004").size());

        System.out.println("--- Create, read, update, delete ---");
        Customer mpho = customerDao.findByUsername("mpho");
        mpho.setPhone("76000000");
        customerDao.update(mpho);
        System.out.println("Updated phone: " + customerDao.findByUsername("mpho").getPhone());
        customerDao.save(new IndividualCustomer("CU099", "Plot 1, Gaborone", "70000000", "temp", "pass123",
                "Temp", "User", "000000000"));
        System.out.println("Customers after create: " + customerDao.findAll().size());
        customerDao.delete("CU099");
        System.out.println("Customers after delete: " + customerDao.findAll().size());
    }
}