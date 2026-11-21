package bank.model;

public class CompanyCustomer extends Customer {

    private String companyName;
    private String registrationNumber;

    public CompanyCustomer(String customerId, String address, String phone, String username,
                           String password, String companyName, String registrationNumber) {
        super(customerId, address, phone, username, password);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String getDisplayName() {
        return companyName;
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRegistrationNumber() { return registrationNumber; }
}