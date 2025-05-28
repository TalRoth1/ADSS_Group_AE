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
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Shifts (" +
                "date DATE NOT NULL, " +
                "shiftType TEXT NOT NULL, " +
                "locationId INT NOT NULL, " +
                "startTime INT NOT NULL, " +
                "endTime INT NOT NULL, " +
                "shiftManagerId INT NOT NULL, " +
                "PRIMARY KEY (date, shiftType, locationId), " +
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

    public void addShift(String date, String shiftType, int startTime, int endTime, int shiftManagerId)
            throws SQLException {
        String sql = "INSERT INTO shifts (date, shiftType, startTime, endTime, shiftManagerId) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, startTime);
            pstmt.setInt(4, endTime);
            pstmt.setInt(5, shiftManagerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding shift: " + e.getMessage());
            throw e;
        }
    }

    public void deleteShift(String date, String shiftType) throws SQLException {
        String sql = "DELETE FROM shifts WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting shift: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getShift(String date, String shiftType) throws SQLException {
        String sql = "SELECT * FROM shifts WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            ////?????????
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getting shift: " + e.getMessage());
            throw e;
        }
    }

    public ArrayList<ShiftDTO> getAllShifts() throws SQLException {
        String sql = "SELECT * FROM shifts";
        ArrayList<ShiftDTO> shifts = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ShiftDTO shift = new ShiftDTO(
                        rs.getString("date"),
                        rs.getString("shiftType"),
                        rs.getInt("startTime"),
                        rs.getInt("endTime"),
                        rs.getInt("shiftManagerId"),
                        0,
                        0,
                        0,
                        0,
                        false);
                shifts.add(shift);
            }
            return shifts;
        } catch (SQLException e) {
            System.out.println("Error retrieving all shifts: " + e.getMessage());
            throw e;
        }

    }

    public String getRole(String date, String shiftType, int employeeId) throws SQLException {
        String sql = "SELECT role FROM shift_assignments WHERE date = ? AND shiftType = ? AND employeeId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
            throw new SQLException("Role not found for employee in the specified shift.");
        }
    }

    public int getStartTime(String date, String shiftType) throws SQLException {
        String sql = "SELECT startTime FROM shifts WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("startTime");
            }
            throw new SQLException("Shift not found.");
        }
    }

    public int getEndTime(String date, String shiftType) throws SQLException {
        String sql = "SELECT endTime FROM shifts WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("endTime");
            }
            throw new SQLException("Shift not found.");
        }
    }

    public int getShiftManagerId(String date, String shiftType) throws SQLException {
        String sql = "SELECT shiftManagerId FROM shifts WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("shiftManagerId");
            }
            throw new SQLException("Shift not found.");
        }
    }

    public void setStartTime(String date, String shiftType, int startTime) throws SQLException {
        String sql = "UPDATE shifts SET startTime = ? WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, startTime);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        }
    }

    public void setEndTime(String date, String shiftType, int endTime) throws SQLException {
        String sql = "UPDATE shifts SET endTime = ? WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, endTime);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        }
    }

    public void setShiftManagerId(String date, String shiftType, int shiftManagerId) throws SQLException {
        String sql = "UPDATE shifts SET shiftManagerId = ? WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, shiftManagerId);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        }
    }

    public void setNumOfRequiredcashiers(int numOfRequiredcashiers, String date, String shiftType) throws SQLException {
        String sql = "UPDATE shifts SET numOfRequiredcashiers = ? WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, numOfRequiredcashiers);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        }
    }

    public int getNumOfRequiredcashiers(String date, String shiftType) throws SQLException {
        String sql = "SELECT numOfRequiredcashiers FROM shifts WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("numOfRequiredcashiers");
            }
            throw new SQLException("Shift not found.");
        }
    }

    public void setNumOfRequireddrivers(int numOfRequireddrivers, String date, String shiftType) throws SQLException {
        String sql = "UPDATE shifts SET numOfRequireddrivers = ? WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, numOfRequireddrivers);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        }
    }

    public int getNumOfRequireddrivers(String date, String shiftType) throws SQLException {
        String sql = "SELECT numOfRequireddrivers FROM shifts WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("numOfRequireddrivers");
            }
            throw new SQLException("Shift not found.");
        }
    }

    public int getNumOfRequiredstoreKeepers(String date, String shiftType) throws SQLException {
        String sql = "SELECT numOfRequiredstoreKeepers FROM shifts WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("numOfRequiredstoreKeepers");
            }
            throw new SQLException("Shift not found.");
        }
    }

    public void setNumOfRequiredstoreKeepers(int numOfRequiredstoreKeepers, String date, String shiftType)
            throws SQLException {
        String sql = "UPDATE shifts SET numOfRequiredstoreKeepers = ? WHERE date = ? AND shiftType = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, numOfRequiredstoreKeepers);
            pstmt.setString(2, date);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        }
    }

}
