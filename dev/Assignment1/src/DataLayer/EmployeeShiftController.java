package DataLayer;

import java.sql.Connection;
import java.sql.SQLException;

import DataLayer.DAOs.EmployeeShiftDAO;

public class EmployeeShiftController {
    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private EmployeeShiftDAO employeeShiftDAO;

    public EmployeeShiftController(EmployeeShiftDAO employeeShiftDAO) {
        String DB_URL = "EmployeesShift.db";
        DBConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
        this.employeeShiftDAO = new EmployeeShiftDAO(connection);
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

    public String getRole(int employeeId, String shiftDate, String shiftType) {
        try {
            return employeeShiftDAO.getRole(employeeId, shiftDate, shiftType);
        } catch (SQLException e) {
            System.out.println("Error getting role for shift: " + e.getMessage());
            return null;
        }
    }

    public void shiftReplacement(int employeeId, String shiftDate, String shiftType, String newRole) {
        try {
            employeeShiftDAO.removeEmployeeShift(employeeId, shiftDate, shiftType);
            employeeShiftDAO.addEmployeeShift(employeeId, shiftDate, shiftType, newRole);
        } catch (SQLException e) {
            System.out.println("Error replacing employee shift: " + e.getMessage());
        }
    }

    /*
     * public List<EmployeeShiftDTO> getShiftsForEmployee(int employeeId) {
     * try {
     * return employeeShiftDAO.getShiftsForEmployee(employeeId);
     * } catch (SQLException e) {
     * System.out.println("Error getting shifts for employee: " + e.getMessage());
     * return null;
     * }
     * }
     * 
     * public List<EmployeeShiftDTO> getEmployeesForShift(String shiftDate, String
     * shiftType) {
     * try {
     * return employeeShiftDAO.getEmployeesForShift(shiftDate, shiftType);
     * } catch (SQLException e) {
     * System.out.println("Error getting employees for shift: " + e.getMessage());
     * return null;
     * }
     * }
     */
}
