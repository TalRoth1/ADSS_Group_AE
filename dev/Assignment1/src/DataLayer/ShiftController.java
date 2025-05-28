package DataLayer;

import java.sql.Connection;

import DataLayer.DAOs.ShiftDAO;

public class ShiftController {
    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private ShiftDAO shiftDAO;

    public ShiftController() {
        String DB_URL = "Shift.db";
        DBConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
        this.shiftDAO = new ShiftDAO(connection);
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

    public String getRole(String date, String shiftType, int employeeId) {
        try {
            return shiftDAO.getRole(date, shiftType, employeeId);
        } catch (Exception e) {
            System.out.println("Error getting role: " + e.getMessage());
            return null;
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

    public void setNumOfRequiredcashiers(int numOfRequiredcashiers, String date, String shiftType) {
        try {
            shiftDAO.setNumOfRequiredcashiers(numOfRequiredcashiers, date, shiftType);
        } catch (Exception e) {
            System.out.println("Error setting number of required cashiers: " + e.getMessage());
        }
    }

    public void setNumOfRequireddrivers(int numOfRequireddrivers, String date, String shiftType) {
        try {
            shiftDAO.setNumOfRequireddrivers(numOfRequireddrivers, date, shiftType);
        } catch (Exception e) {
            System.out.println("Error setting number of required drivers: " + e.getMessage());
        }
    }

    public void setNumOfRequiredstoreKeepers(int numOfRequiredstoreKeepers, String date, String shiftType) {
        try {
            shiftDAO.setNumOfRequiredstoreKeepers(numOfRequiredstoreKeepers, date, shiftType);
        } catch (Exception e) {
            System.out.println("Error setting number of required store keepers: " + e.getMessage());
        }
    }

    public int getNumOfRequiredcashiers(String date, String shiftType) {
        try {
            return shiftDAO.getNumOfRequiredcashiers(date, shiftType);
        } catch (Exception e) {
            System.out.println("Error getting number of required cashiers: " + e.getMessage());
            return -1;
        }
    }

    public int getNumOfRequireddrivers(String date, String shiftType) {
        try {
            return shiftDAO.getNumOfRequireddrivers(date, shiftType);
        } catch (Exception e) {
            System.out.println("Error getting number of required drivers: " + e.getMessage());
            return -1;
        }
    }

    public int getNumOfRequiredstoreKeepers(String date, String shiftType) {
        try {
            return shiftDAO.getNumOfRequiredstoreKeepers(date, shiftType);
        } catch (Exception e) {
            System.out.println("Error getting number of required store keepers: " + e.getMessage());
            return -1;
        }
    }

}
