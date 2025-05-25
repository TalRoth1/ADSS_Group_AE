package DataLayer;

import java.sql.SQLException;

public class PreferredShiftController {

    private PreferredShiftDAO preferredShiftDAO;

    public PreferredShiftController(PreferredShiftDAO preferredShiftDAO) {
        this.preferredShiftDAO = preferredShiftDAO;
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
