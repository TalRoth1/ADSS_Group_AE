package DataLayer.DAOs;

import DTO.ShiftDTO;
import java.sql.*;
import java.util.ArrayList;

public class ShiftDAO {

    private Connection connection;

    public ShiftDAO(Connection connection) {
        this.connection = connection;
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing shifts table: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS shifts (" +
                "id INT NOT NULL, " +        
                "date TEXT NOT NULL, " +
                "shiftType TEXT NOT NULL, " +
                "startTime INT NOT NULL, " +
                "endTime INT NOT NULL, " +
                "shiftManagerId INT NOT NULL, " +
                "isShipment INT NOT NULL, " + //0 false, 1 for true
                "locationId INT NOT NULL, " +
                "PRIMARY KEY (id), " +
                "FOREIGN KEY (shiftManagerId) REFERENCES employees(id), " +
                "FOREIGN KEY (locationId) REFERENCES locations(id)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shifts table: " + e.getMessage());
            throw e;
        }
    }

    public void addShift(String date, String shiftType, int locationId, int startTime, int endTime, int shiftManagerId)
            throws SQLException {
        String sql = "INSERT INTO shifts (date, shiftType, locationId, startTime, endTime, shiftManagerId) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            pstmt.setInt(4, startTime);
            pstmt.setInt(5, endTime);
            pstmt.setInt(6, shiftManagerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding shift: " + e.getMessage());
            throw e;
        }
    }

    public void deleteShift(String date, String shiftType, int locationId) throws SQLException {
        String sql = "DELETE FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting shift: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getShift(String date, String shiftType, int locationId) throws SQLException {
        String sql = "SELECT * FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getting shift: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getAllShifts() throws SQLException {
        String sql = "SELECT * FROM shifts";
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving all shifts: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getRole(String date, String shiftType, int employeeId) throws SQLException {
        String sql = "SELECT role FROM shift_assignments WHERE date = ? AND shiftType = ? AND employeeId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, employeeId);
            return pstmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving role: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getStartTime(String date, String shiftType, int locationId) throws SQLException {
        String sql = "SELECT startTime FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving start time: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getEndTime(String date, String shiftType, int locationId) throws SQLException {
        String sql = "SELECT endTime FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving end time: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getShiftManagerId(String date, String shiftType, int locationId) throws SQLException {
        String sql = "SELECT shiftManagerId FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving shift manager ID: " + e.getMessage());
            throw e;
        }
    }

    public void setStartTime(String date, String shiftType, int startTime, int locationId) throws SQLException {
        String sql = "UPDATE shifts SET startTime = ? WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, startTime);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.setInt(4, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating start time: " + e.getMessage());
            throw e;
        }
    }

    public void setEndTime(String date, String shiftType, int endTime, int locationId) throws SQLException {
        String sql = "UPDATE shifts SET endTime = ? WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, endTime);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.setInt(4, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating end time: " + e.getMessage());
            throw e;
        }
    }

    public void setShiftManagerId(String date, String shiftType, int shiftManagerId, int locationId) throws SQLException {
        String sql = "UPDATE shifts SET shiftManagerId = ? WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftManagerId);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.setInt(4, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating shift manager ID: " + e.getMessage());
            throw e;
        }
    }

    public void setNumOfRequiredcashiers(int numOfRequiredcashiers, String date, String shiftType, int locationId) throws SQLException {
        String sql = "UPDATE shifts SET numOfRequiredcashiers = ? WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, numOfRequiredcashiers);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.setInt(4, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating number of required cashiers: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getNumOfRequiredcashiers(String date, String shiftType, int locationId) throws SQLException {
        String sql = "SELECT numOfRequiredcashiers FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving number of required cashiers: " + e.getMessage());
            throw e;
        }
    }

    public void setNumOfRequireddrivers(int numOfRequireddrivers, String date, String shiftType, int locationId) throws SQLException {
        String sql = "UPDATE shifts SET numOfRequireddrivers = ? WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, numOfRequireddrivers);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.setInt(4, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating number of required drivers: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getNumOfRequireddrivers(String date, String shiftType, int locationId) throws SQLException {
        String sql = "SELECT numOfRequireddrivers FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving number of required drivers: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getNumOfRequiredstoreKeepers(String date, String shiftType, int locationId) throws SQLException {
        String sql = "SELECT numOfRequiredstoreKeepers FROM shifts WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, locationId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving number of required store keepers: " + e.getMessage());
            throw e;
        }
    }

    public void setNumOfRequiredstoreKeepers(int numOfRequiredstoreKeepers, String date, String shiftType, int locationId)
            throws SQLException {
        String sql = "UPDATE shifts SET numOfRequiredstoreKeepers = ? WHERE date = ? AND shiftType = ? AND locationId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, numOfRequiredstoreKeepers);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.setInt(4, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating number of required store keepers: " + e.getMessage());
            throw e;
        }
    }
    public void changeShiftManager(int oldid, int newid, String date, String shiftType, int locationId) throws SQLException {
        String sql = "UPDATE shifts SET shiftManagerId=? WHERE shiftManagerId=? AND date=? AND shiftType=? AND locationId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newid);
            pstmt.setInt(2, oldid);
            pstmt.setString(3, date);
            pstmt.setString(4, shiftType);
            pstmt.setInt(5, locationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error changing shift manager: " + e.getMessage());
            throw e;
        }
    }
}
