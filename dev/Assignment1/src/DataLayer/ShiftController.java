package DataLayer;

import java.sql.ResultSet;

public class ShiftController {

    private ShiftDAO shiftDAO;

    public ShiftController(ShiftDAO shiftDAO) {
        this.shiftDAO = shiftDAO;
    }

    public void addShift(String date, String shiftType, int startTime, int endTime, int shiftManagerId) {
        try {
            shiftDAO.addShift(date, shiftType, startTime, endTime, shiftManagerId);
        } catch (Exception e) {
            System.out.println("Error adding shift: " + e.getMessage());
        }
    }

    public void deleteShift(String date, String shiftType) {
        try {
            shiftDAO.deleteShift(date, shiftType);
        } catch (Exception e) {
            System.out.println("Error deleting shift: " + e.getMessage());
        }
    }

    public ResultSet getShift(String date, String shiftType) {
        try {
            return shiftDAO.getShift(date, shiftType);
        } catch (Exception e) {
            System.out.println("Error getting shift: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getAllShifts() {
        try {
            return shiftDAO.getAllShifts();
        } catch (Exception e) {
            System.out.println("Error getting all shifts: " + e.getMessage());
            return null;
        }
    }

    public void getRole(String date, String shiftType, int employeeId) {
        try {
            shiftDAO.getRole(date, shiftType, employeeId);
        } catch (Exception e) {
            System.out.println("Error getting role: " + e.getMessage());
        }
    }

    public int getStartTime(String date, String shiftType) {
        try {
            return shiftDAO.getStartTime(date, shiftType);
        } catch (Exception e) {
            System.out.println("Error getting start time: " + e.getMessage());
            return -1;
        }
    }

    public int getEndTime(String date, String shiftType) {
        try {
            return shiftDAO.getEndTime(date, shiftType);
        } catch (Exception e) {
            System.out.println("Error getting end time: " + e.getMessage());
            return -1;
        }
    }

    public int getShiftManagerId(String date, String shiftType) {
        try {
            return shiftDAO.getShiftManagerId(date, shiftType);
        } catch (Exception e) {
            System.out.println("Error getting shift manager ID: " + e.getMessage());
            return -1;
        }
    }

    public void setStartTime(String date, String shiftType, int startTime) {
        try {
            shiftDAO.setStartTime(date, shiftType, startTime);
        } catch (Exception e) {
            System.out.println("Error setting start time: " + e.getMessage());
        }
    }

    public void setEndTime(String date, String shiftType, int endTime) {
        try {
            shiftDAO.setEndTime(date, shiftType, endTime);
        } catch (Exception e) {
            System.out.println("Error setting end time: " + e.getMessage());
        }
    }

    public void setShiftManagerId(String date, String shiftType, int shiftManagerId) {
        try {
            shiftDAO.setShiftManagerId(date, shiftType, shiftManagerId);
        } catch (Exception e) {
            System.out.println("Error setting shift manager ID: " + e.getMessage());
        }
    }
}
