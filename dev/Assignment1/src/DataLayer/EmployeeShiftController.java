package DataLayer;

import java.sql.SQLException;
import java.util.List;
import DTO.EmployeeShiftDTO;

public class EmployeeShiftController {

    private EmployeeShiftDAO employeeShiftDAO;

    public EmployeeShiftController(EmployeeShiftDAO employeeShiftDAO) {
        this.employeeShiftDAO = employeeShiftDAO;
    }

    public void addEmployeeShift(int employeeId, String shiftDate, String shiftType, String role) {
        try {
            employeeShiftDAO.addEmployeeShift(employeeId, shiftDate, shiftType, role);
        } catch (SQLException e) {
            System.out.println("Error adding employee shift: " + e.getMessage());
        }
    }

    public void removeEmployeeShift(int employeeId, String shiftDate, String shiftType) {
        try {
            employeeShiftDAO.removeEmployeeShift(employeeId, shiftDate, shiftType);
        } catch (SQLException e) {
            System.out.println("Error removing employee shift: " + e.getMessage());
        }
    }

    /*public List<EmployeeShiftDTO> getShiftsForEmployee(int employeeId) {
        try {
            return employeeShiftDAO.getShiftsForEmployee(employeeId);
        } catch (SQLException e) {
            System.out.println("Error getting shifts for employee: " + e.getMessage());
            return null;
        }
    }

    public List<EmployeeShiftDTO> getEmployeesForShift(String shiftDate, String shiftType) {
        try {
            return employeeShiftDAO.getEmployeesForShift(shiftDate, shiftType);
        } catch (SQLException e) {
            System.out.println("Error getting employees for shift: " + e.getMessage());
            return null;
        }
    }*/
}
