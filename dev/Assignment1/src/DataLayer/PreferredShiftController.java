package DataLayer;

import java.sql.Connection;
import java.sql.SQLException;

import DataLayer.DAOs.PreferredShiftDAO;

public class PreferredShiftController {
    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private PreferredShiftDAO preferredShiftDAO;

    public PreferredShiftController() {
        String DB_URL = "PreferredShift.db";
        DBConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
        this.preferredShiftDAO = new PreferredShiftDAO(connection);
    }

    public void addPreferredShift(int employeeId, String shiftDate, String shiftType) {
        try {
            preferredShiftDAO.addPreferredShift(employeeId, shiftDate, shiftType);
        } catch (SQLException e) {
            System.out.println("Error adding preferred shift: " + e.getMessage());
        }
    }

    public void removePreferredShift(int employeeId, String shiftDate, String shiftType) {
        try {
            preferredShiftDAO.removePreferredShift(employeeId, shiftDate, shiftType);
        } catch (SQLException e) {
            System.out.println("Error removing preferred shift: " + e.getMessage());
        }
    }

    public void getPreferredShifts(int employeeId) {
        try {
            preferredShiftDAO.getPreferredShifts(employeeId);
        } catch (SQLException e) {
            System.out.println("Error getting preferred shifts: " + e.getMessage());
        }
    }

    public void getPrefShiftEmployees(String shiftDate, String shiftType) {
        try {
            preferredShiftDAO.getPrefShiftEmployees(shiftDate, shiftType);
        } catch (SQLException e) {
            System.out.println("Error getting preferred shift employees: " + e.getMessage());
        }
    }
}
