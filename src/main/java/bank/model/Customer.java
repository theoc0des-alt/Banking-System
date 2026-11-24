package bank.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer {

    private String customerId;
    private String address;
    private String phone;
    private String username;
    private String passwordHash;                 // never store the plain password
    private List<Account> accounts = new ArrayList<>();

    public Customer(String customerId, String address, String phone, String username, String password) {
        this.customerId = customerId;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.passwordHash = PasswordUtil.hash(password);
    }

    // Abstract: each customer type decides how its name is shown
    public abstract String getDisplayName();

    // Default: customers cannot open a Cheque account. Individuals override this.
    public boolean canOpenChequeAccount() {
        return false;
    }

    public void addAccount(Account account) {
        if (account instanceof ChequeAccount && !canOpenChequeAccount()) {
            throw new IllegalStateException("Only employed individual customers can open a Cheque account");
        }
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);        // a copy, so outsiders can't change the list
    }

    public boolean checkPassword(String input) {
        return passwordHash.equals(PasswordUtil.hash(input));
    }

    public void setPassword(String password) {
        this.passwordHash = PasswordUtil.hash(password);
    }

    public String getCustomerId() { return customerId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getUsername() { return username; }
}