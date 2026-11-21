package bank.model;

public class IndividualCustomer extends Customer {

    private String firstName;
    private String surname;
    private String idNumber;
    private String employerName;
    private String employerAddress;

    public IndividualCustomer(String customerId, String address, String phone, String username,
                              String password, String firstName, String surname, String idNumber) {
        super(customerId, address, phone, username, password);
        this.firstName = firstName;
        this.surname = surname;
        this.idNumber = idNumber;
    }

    public void setEmployer(String employerName, String employerAddress) {
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    public boolean isEmployed() {
        return employerName != null && !employerName.isBlank();
    }

    @Override
    public boolean canOpenChequeAccount() {
        return isEmployed();                     
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + surname;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getIdNumber() { return idNumber; }
    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
}
