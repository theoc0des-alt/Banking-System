package bank.dao;

import bank.model.Staff;

public interface StaffDAO {
    Staff findByUsername(String username);   // returns null if not found
    void save(Staff staff);
}