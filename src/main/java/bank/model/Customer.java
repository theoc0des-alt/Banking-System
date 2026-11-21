package bank.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

public abstract class Customer {

    private String customerId;
    private String address;
    private String phone;
    private String username;
    private String passwordHash;
    private List<Account> accounts = new ArrayList();

    public Customer (String customerId, String address, String phone, String username, String password) {
        this.customerId = customerId;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.passwordHash = hash(password);
}

public abstract String getDisplayName();

public boolean canOpenChequeAccount() {
    return false;
}

public void addAccount(Account account) {
    if (account instanceof ChequeAccount && !canOpenChequeAccount()) {
        throw new IllegalStateException("Only employed individual customers can open a cheque account");
    }
    accounts.add(account);
}

public List<Account> geAccounts() {
    return new ArrayList<>(accounts);
}

public boolean checkPassword(String input) {
        return passwordHash.equals(hash(input));
}

public void setPassword(String password) {
        this.passwordHash = hash(password);
}

private static String hash(String text) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return HexFormat.of().formatHex(md.digest(text.getBytes(StandardCharsets.UTF_8)));
    } 
    catch (Exception e) {
        throw new IllegalStateException("Could not hash password");
    }
}

    public String getCustomerId() { return customerId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getUsername() { return username; }
}