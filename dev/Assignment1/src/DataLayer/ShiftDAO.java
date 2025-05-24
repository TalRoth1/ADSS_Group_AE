package DataLayer;

import java.sql.*;

public class ShiftDAO {

    private Connection connection;

    public ShiftDAO(Connection connection) {
        this.connection = connection;
    }

    public void addShift(String date, String shiftType, int startTime, int endTime, int shiftManagerId) throws SQLException {
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

    public ResultSet getAllShifts() throws SQLException {
        String sql = "SELECT * FROM shifts";
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error getting all shifts: " + e.getMessage());
            throw e;
        }
    }

    public void getRole(String date, String shiftType, int employeeId) throws SQLException {
        String sql = "SELECT role FROM shift_assignments WHERE date = ? AND shiftType = ? AND employeeId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, shiftType);
            pstmt.setInt(3, employeeId);
            ResultSet rst = pstmt.executeQuery();
            if (rst.next()) {
                System.out.println("Role: " + rst.getString("role"));
            } else {
                System.out.println("No role found for this employee in the specified shift.");
            }
        } catch (SQLException e) {
            System.out.println("Error getting role: " + e.getMessage());
            throw e;
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

}
