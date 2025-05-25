package DataLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DTO.EmployeeShiftDTO;

public class EmployeeShiftDAO {

    private Connection connection;

    public EmployeeShiftDAO(Connection connection) {
        this.connection = connection;
    }

    public void addEmployeeShift(int employeeId, String shiftDate, String shiftType, String role) throws SQLException {
        String sql = "INSERT INTO employee_shifts (employeeId, shiftDate, shiftType, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, shiftDate);
            pstmt.setString(3, shiftType);
            pstmt.setString(4, role);
            pstmt.executeUpdate();
        }
    }

    public void removeEmployeeShift(int employeeId, String shiftDate, String shiftType) throws SQLException {
        String sql = "DELETE FROM employee_shifts WHERE employeeId=? AND shiftDate=? AND shiftType=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, shiftDate);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        }
    }

    //view all shifts for an employee
    public List<EmployeeShiftDTO> getEmployeeShifts(int employeeId) throws SQLException {
        String sql = "SELECT * FROM employee_shifts WHERE employeeId=?";
        List<EmployeeShiftDTO> shifts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                EmployeeShiftDTO shift = new EmployeeShiftDTO(
                        rs.getInt("employeeId"),
                        rs.getString("shiftDate"),
                        rs.getString("shiftType"),
                        rs.getString("role")
                );
                shifts.add(shift);
            }
        }
        return shifts;
    }

    //view all employees of a shift
    public List<EmployeeShiftDTO> getShiftEmployees(String shiftDate, String shiftType) throws SQLException {
        String sql = "SELECT * FROM employee_shifts WHERE shiftDate=? AND shiftType=?";
        List<EmployeeShiftDTO> shifts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shiftDate);
            pstmt.setString(2, shiftType);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                EmployeeShiftDTO shift = new EmployeeShiftDTO(
                        rs.getInt("employeeId"),
                        rs.getString("shiftDate"),
                        rs.getString("shiftType"),
                        rs.getString("role")
                );
                shifts.add(shift);
            }
        }
        return shifts;
    }
}
