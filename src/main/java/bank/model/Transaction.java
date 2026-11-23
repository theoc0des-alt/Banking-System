package bank.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {

    private String transactionId;
    private String type;            
    private double amount;
    private LocalDate date;
    private LocalTime time;
    private String accountNumber;   

    public Transaction(String transactionId, String type, double amount, String accountNumber) {
        this(transactionId, type, amount, accountNumber, LocalDate.now(), LocalTime.now());
    }

    // Used later when loading saved transactions from the database
    public Transaction(String transactionId, String type, double amount, String accountNumber,
                       LocalDate date, LocalTime time) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.date = date;
        this.time = time;
    }

    // Getters only: a transaction can never be changed or reversed
    public String getTransactionId() { return transactionId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getAccountNumber() { return accountNumber; }
}