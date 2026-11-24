package bank.dao;

import bank.model.Staff;
import java.util.ArrayList;
import java.util.List;

public class InMemoryStaffDAO implements StaffDAO {

    private final List<Staff> staffList = new ArrayList<>();

    @Override
    public Staff findByUsername(String username) {
        for (Staff s : staffList) {
            if (s.getUsername().equals(username)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public void save(Staff staff) {
        staffList.add(staff);
    }
}