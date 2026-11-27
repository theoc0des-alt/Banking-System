package bank.dao;

import bank.model.Staff;
import java.util.List;

public interface StaffDAO {
    void save(Staff staff);                           
    Staff findByUsername(String username);           
    List<Staff> findAll();                           
    void update(Staff staff);                        
    void delete(String staffId);                     
}