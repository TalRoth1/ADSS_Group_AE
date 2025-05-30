package DataLayer.DAOs;

import DTO.EmployeeShiftDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeShiftDAO {

    private Connection connection;

    public EmployeeShiftDAO(Connection connection) {
        this.connection = connection;
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employee_shifts (" +
                "employeeId INT NOT NULL, " +
                "shiftDate DATE NOT NULL, " +
                "shiftType TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "PRIMARY KEY (employeeId, shiftDate, shiftType), " +
                "FOREIGN KEY (employeeId) REFERENCES employees(id)" +

                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shifts table: " + e.getMessage());
            throw e;
        }
    }

    public void addEmployeeShift(int employeeId, String shiftDate, String shiftType, String role) throws SQLException {
        String sql = "INSERT INTO employee_shifts (employeeId, shiftDate, shiftType, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, shiftDate);
            pstmt.setString(3, shiftType);
            pstmt.setString(4, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding employee shift: " + e.getMessage());
            throw e;
        }
    }

    public void removeEmployeeShift(int employeeId, String shiftDate, String shiftType) throws SQLException {
        String sql = "DELETE FROM employee_shifts WHERE employeeId=? AND shiftDate=? AND shiftType=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, shiftDate);
            pstmt.setString(3, shiftType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing employee shift: " + e.getMessage());
            throw e;
        }
    }

    // view all shifts for an employee
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
                        rs.getString("role"));
                shifts.add(shift);
            }
            return shifts;
        } catch (SQLException e) {
            System.out.println("Error retrieving employee shifts: " + e.getMessage());
            throw e;
        }
    }

    // view all employees of a shift
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
                        rs.getString("role"));
                shifts.add(shift);
            }
            return shifts;
        } catch (SQLException e) {
            System.out.println("Error retrieving shift employees: " + e.getMessage());
            throw e;
        }
    }

    public void shiftReplacement(int oldemployeeId, int newEmployeeId, String shiftDate, String shiftType,
            String branch) throws SQLException {
        String sql = "UPDATE employee_shifts SET employeeId=? WHERE employeeId=? AND shiftDate=? AND shiftType=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newEmployeeId);
            pstmt.setInt(2, oldemployeeId);
            pstmt.setString(3, shiftDate);
            pstmt.setString(4, shiftType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error replacing shift: " + e.getMessage());
            throw e;
        }
    }

    public String getRole(int employeeId, String shiftDate, String shiftType) throws SQLException {
        String sql = "SELECT role FROM employee_shifts WHERE employeeId=? AND shiftDate=? AND shiftType=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, shiftDate);
            pstmt.setString(3, shiftType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            } else {
                return null; // No role found for the given parameters
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving role: " + e.getMessage());
            throw e;
        }
    }
}
