package DataLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;

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

    public void addShift(String date, String shiftType, int locationId, int startTime, int endTime, int shiftManagerId) {
        try {
            shiftDAO.addShift(date, shiftType, locationId, startTime, endTime, shiftManagerId);
        } catch (Exception e) {
            System.out.println("Error adding shift: " + e.getMessage());
        }
    }

    public void deleteShift(String date, String shiftType, int locationId) {
        try {
            shiftDAO.deleteShift(date, shiftType, locationId);
        } catch (Exception e) {
            System.out.println("Error deleting shift: " + e.getMessage());
        }
    }

    public int getStartTime(String date, String shiftType, int locationId) {
        try {
            ResultSet resultSet = shiftDAO.getStartTime(date, shiftType, locationId);
            if (resultSet.next())
                return resultSet.getInt("startTime");
            return -1;
        } catch (Exception e) {
            System.out.println("Error getting start time: " + e.getMessage());
            return -1;
        }
    }

    public int getEndTime(String date, String shiftType, int locationId) {
        try {
            ResultSet resultSet = shiftDAO.getEndTime(date, shiftType, locationId);
            if (resultSet.next())
                return resultSet.getInt("endTime");
            return -1;
        } catch (Exception e) {
            System.out.println("Error getting end time: " + e.getMessage());
            return -1;
        }
    }

    public int getShiftManagerId(String date, String shiftType, int locationId) {
        try {
            ResultSet resultSet = shiftDAO.getShiftManagerId(date, shiftType, locationId);
            if (resultSet.next())
                return resultSet.getInt("shiftManagerId");
            return -1;
        } catch (Exception e) {
            System.out.println("Error getting shift manager ID: " + e.getMessage());
            return -1;
        }
    }

    public void setStartTime(String date, String shiftType, int startTime, int locationId) {
        try {
            shiftDAO.setStartTime(date, shiftType, startTime, locationId);
        } catch (Exception e) {
            System.out.println("Error setting start time: " + e.getMessage());
        }
    }

    public void setEndTime(String date, String shiftType, int endTime, int locationId) {
        try {
            shiftDAO.setEndTime(date, shiftType, endTime, locationId);
        } catch (Exception e) {
            System.out.println("Error setting end time: " + e.getMessage());
        }
    }

    public void setShiftManagerId(String date, String shiftType, int shiftManagerId, int locationId) {
        try {
            shiftDAO.setShiftManagerId(date, shiftType, shiftManagerId, locationId);
        } catch (Exception e) {
            System.out.println("Error setting shift manager ID: " + e.getMessage());
        }
    }

    public void setNumOfRequiredcashiers(int numOfRequiredcashiers, String date, String shiftType, int locationId) {
        try {
            shiftDAO.setNumOfRequiredcashiers(numOfRequiredcashiers, date, shiftType, locationId);
        } catch (Exception e) {
            System.out.println("Error setting number of required cashiers: " + e.getMessage());
        }
    }

    public void setNumOfRequireddrivers(int numOfRequireddrivers, String date, String shiftType, int locationId) {
        try {
            shiftDAO.setNumOfRequireddrivers(numOfRequireddrivers, date, shiftType, locationId);
        } catch (Exception e) {
            System.out.println("Error setting number of required drivers: " + e.getMessage());
        }
    }

    public void setNumOfRequiredstoreKeepers(int numOfRequiredstoreKeepers, String date, String shiftType, int locationId) {
        try {
            shiftDAO.setNumOfRequiredstoreKeepers(numOfRequiredstoreKeepers, date, shiftType, locationId);
        } catch (Exception e) {
            System.out.println("Error setting number of required store keepers: " + e.getMessage());
        }
    }

    public int getNumOfRequiredcashiers(String date, String shiftType, int locationId) {
        try {
            ResultSet resultSet = shiftDAO.getNumOfRequiredcashiers(date, shiftType, locationId);
            if (resultSet.next())
                return resultSet.getInt("numOfRequiredcashiers");
            return -1;
        } catch (Exception e) {
            System.out.println("Error getting number of required cashiers: " + e.getMessage());
            return -1;
        }
    }

    public int getNumOfRequireddrivers(String date, String shiftType, int locationId) {
        try {
            ResultSet resultSet = shiftDAO.getNumOfRequireddrivers(date, shiftType, locationId);
            if (resultSet.next()) {
                return resultSet.getInt("numOfRequireddrivers");
            }
            return -1;
        } catch (Exception e) {
            System.out.println("Error getting number of required drivers: " + e.getMessage());
            return -1;
        }
    }

    public int getNumOfRequiredstoreKeepers(String date, String shiftType, int locationId) {
        try {
            ResultSet resultSet = shiftDAO.getNumOfRequiredstoreKeepers(date, shiftType, locationId);
            if (resultSet.next())
                return resultSet.getInt("numOfRequiredstoreKeepers");
            return -1;
        } catch (Exception e) {
            System.out.println("Error getting number of required store keepers: " + e.getMessage());
            return -1;
        }
    }

    public void changeShiftManager(int oldId, int newId, String date, String shiftType, int locationId) {
        try {
            shiftDAO.changeShiftManager(oldId, newId, date, shiftType, locationId);
        } catch (Exception e) {
            System.out.println("Error changing shift manager: " + e.getMessage());
        }
    }

}
