package bank.dao;

import bank.model.Account;
import bank.model.ChequeAccount;
import bank.model.CompanyCustomer;
import bank.model.IndividualCustomer;
import bank.model.InvestmentAccount;
import bank.model.SavingsAccount;
import bank.model.Staff;
import bank.model.Transaction;
import java.time.LocalDate;
import java.time.LocalTime;

// Puts the sample records into an empty database (10 customers, 2 staff, 16 accounts, 10 transactions).
// Every sample customer has the password pass123 and every staff member has staff123.
public class DataSeeder {

    private DataSeeder() { }

    public static void seedIfEmpty(CustomerDAO customerDao, StaffDAO staffDao,
                                   AccountDAO accountDao, TransactionDAO transactionDao) {
        if (!customerDao.findAll().isEmpty()) {
            return;   // already seeded
        }

        // Staff
        staffDao.save(new Staff("ST1", "Neo Phiri", "neo", "staff123"));
        staffDao.save(new Staff("ST2", "Lesedi Moyo", "lesedi", "staff123"));

        // Individual customers
        customerDao.save(person("CU001", "Thato", "Mokoena", "thato", "Plot 112, Gaborone", "71234567",
                "123456781", "Orange Botswana", "Main Mall, Gaborone"));
        customerDao.save(person("CU002", "Kago", "Dube", "kago", "Block 6, Francistown", "72345678",
                "123456782", null, null));
        customerDao.save(person("CU003", "Mpho", "Seretse", "mpho", "Plot 45, Gaborone West", "73456789",
                "123456783", "Choppies Enterprises", "Gaborone West"));
        customerDao.save(person("CU004", "Tshepo", "Ramotswe", "tshepo", "Ward 3, Maun", "74567890",
                "123456784", null, null));
        customerDao.save(person("CU005", "Boitumelo", "Kgosi", "boitumelo", "Orapa Township", "75678901",
                "123456785", "Debswana", "Orapa"));
        customerDao.save(person("CU006", "Lorato", "Mmusi", "lorato", "Plot 78, Gaborone", "76789012",
                "123456786", null, null));
        customerDao.save(person("CU007", "Onkarabile", "Tau", "onkarabile", "Block 9, Francistown", "77890123",
                "123456787", "Letshego", "Francistown"));

        // Company customers
        customerDao.save(new CompanyCustomer("CU008", "Main Mall, Gaborone", "3951234", "acme", "pass123",
                "Acme Ltd", "BW0001"));
        customerDao.save(new CompanyCustomer("CU009", "Industrial Area, Gaborone", "3952345", "kalahari", "pass123",
                "Kalahari Traders (Pty) Ltd", "BW0002"));
        customerDao.save(new CompanyCustomer("CU010", "Airport Road, Maun", "6861234", "okavango", "pass123",
                "Okavango Tours (Pty) Ltd", "BW0003"));

        // Accounts
        accountDao.save(savings("S-1001", "Gaborone", 5000, 6, 8), "CU001");
        accountDao.save(investment("I-2001", "Gaborone", 2500, 3), "CU001");
        accountDao.save(cheque("C-3001", "Gaborone", 1200, 1000, 5), "CU001");
        accountDao.save(savings("S-1002", "Francistown", 800, 12, 2), "CU002");
        accountDao.save(investment("I-2002", "Francistown", 600, 1), "CU002");
        accountDao.save(savings("S-1003", "Gaborone", 3000, 6, 7), "CU003");
        accountDao.save(cheque("C-3002", "Gaborone", 450, 500, 4), "CU003");
        accountDao.save(savings("S-1004", "Maun", 1500, 12, 13), "CU004");
        accountDao.save(investment("I-2003", "Orapa", 10000, 6), "CU005");
        accountDao.save(cheque("C-3003", "Orapa", 2200, 2000, 9), "CU005");
        accountDao.save(savings("S-1005", "Gaborone", 950, 6, 1), "CU006");
        accountDao.save(savings("S-1006", "Francistown", 4200, 6, 9), "CU007");
        accountDao.save(cheque("C-3004", "Francistown", 100, 500, 2), "CU007");
        accountDao.save(investment("I-2004", "Gaborone", 50000, 10), "CU008");
        accountDao.save(savings("S-1007", "Gaborone", 12000, 12, 14), "CU009");
        accountDao.save(investment("I-2005", "Maun", 7500, 4), "CU010");

        // Transactions
        transactionDao.save(txn("T-0001", "DEPOSIT", 5000, "S-1001", 8));
        transactionDao.save(txn("T-0002", "DEPOSIT", 2500, "I-2001", 3));
        transactionDao.save(txn("T-0003", "DEPOSIT", 1500, "C-3001", 5));
        transactionDao.save(txn("T-0004", "WITHDRAWAL", 300, "C-3001", 4));
        transactionDao.save(txn("T-0005", "DEPOSIT", 800, "S-1002", 2));
        transactionDao.save(txn("T-0006", "DEPOSIT", 3000, "S-1003", 7));
        transactionDao.save(txn("T-0007", "DEPOSIT", 450, "C-3002", 4));
        transactionDao.save(txn("T-0008", "DEPOSIT", 1500, "S-1004", 13));
        transactionDao.save(txn("T-0009", "DEPOSIT", 10000, "I-2003", 6));
        transactionDao.save(txn("T-0010", "DEPOSIT", 50000, "I-2004", 10));
    }

    private static IndividualCustomer person(String id, String first, String surname, String username,
                                             String address, String phone, String idNumber,
                                             String employer, String employerAddress) {
        IndividualCustomer c = new IndividualCustomer(id, address, phone, username, "pass123",
                first, surname, idNumber);
        if (employer != null) {
            c.setEmployer(employer, employerAddress);
        }
        return c;
    }

    private static Account savings(String number, String branch, double balance, int lockMonths, int monthsAgo) {
        return new SavingsAccount(number, branch, balance, lockMonths, LocalDate.now().minusMonths(monthsAgo));
    }

    private static Account investment(String number, String branch, double balance, int monthsAgo) {
        return new InvestmentAccount(number, branch, balance, LocalDate.now().minusMonths(monthsAgo));
    }

    private static Account cheque(String number, String branch, double balance, double overdraft, int monthsAgo) {
        return new ChequeAccount(number, branch, balance, overdraft, LocalDate.now().minusMonths(monthsAgo));
    }

    private static Transaction txn(String id, String type, double amount, String account, int monthsAgo) {
        return new Transaction(id, type, amount, account, LocalDate.now().minusMonths(monthsAgo), LocalTime.of(10, 30));
    }
}