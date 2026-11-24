package bank.model;

public class Staff {

    private String staffId;
    private String name;
    private String username;
    private String passwordHash;

    public Staff(String staffId, String name, String username, String password) {
        this.staffId = staffId;
        this.name = name;
        this.username = username;
        this.passwordHash = PasswordUtil.hash(password);
    }

    public boolean checkPassword(String input) {
        return passwordHash.equals(PasswordUtil.hash(input));
    }

    public void setPassword(String password) {
        this.passwordHash = PasswordUtil.hash(password);
    }

    public String getDisplayName() { return name; }
    public String getStaffId() { return staffId; }
    public String getUsername() { return username; }
}