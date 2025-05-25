package DataLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DTO.PreferredShiftDTO;

public class PreferredShiftDAO {

    private Connection connection;

    public PreferredShiftDAO(Connection connection) {
        this.connection = connection;
    }

    public void addPreferredShift(int employeeId, String shiftDate, String shiftType) throws SQLException {
        String sql = "INSERT INTO preferred_shifts (employeeId, shiftDate, shiftType) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, shiftDate);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        }
    }

    public void removePreferredShift(int employeeId, String shiftDate, String shiftType) throws SQLException {
        String sql = "DELETE FROM preferred_shifts WHERE employeeId=? AND shiftDate=? AND shiftType=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, shiftDate);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        }
    }

    //view all preferred shifts for an employee
    public List<PreferredShiftDTO> getPreferredShifts(int employeeId) throws SQLException {
        String sql = "SELECT * FROM preferred_shifts WHERE employeeId=?";
        List<PreferredShiftDTO> preferredShifts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PreferredShiftDTO shift = new PreferredShiftDTO(
                        rs.getInt("employeeId"),
                        rs.getString("shiftDate"),
                        rs.getString("shiftType")
                );
                preferredShifts.add(shift);
            }
        }
        return preferredShifts;
    }

    //view all employees that chose this preferred shift
    public List<PreferredShiftDTO> getPrefShiftEmployees(String shiftDate, String shiftType) throws SQLException {
        String sql = "SELECT * FROM preferred_shifts WHERE shiftDate=? AND shiftType=?";
        List<PreferredShiftDTO> preferredShifts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shiftDate);
            pstmt.setString(2, shiftType);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PreferredShiftDTO shift = new PreferredShiftDTO(
                        rs.getInt("employeeId"),
                        rs.getString("shiftDate"),
                        rs.getString("shiftType")
                );
                preferredShifts.add(shift);
            }
        }
        return preferredShifts;
    }

}
