package DataLayer;

import DTO.EmployeeDTO;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeDAO {

    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public void addEmployee(int id, String name, String branch, String bankAccount, int salary, String startDate,
            int vacationDays, int sickDays, double educationFund, double socialBenefits,
            String password) throws SQLException {
        String sql = "INSERT INTO employees (id, name, branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, branch);
            pstmt.setString(4, bankAccount);
            pstmt.setInt(5, salary);
            pstmt.setString(6, startDate);
            pstmt.setInt(7, vacationDays);
            pstmt.setInt(8, sickDays);
            pstmt.setDouble(9, educationFund);
            pstmt.setDouble(10, socialBenefits);
            pstmt.setString(11, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
            throw e;
        }
    }

    public void removeEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void fireEmployee(int employeeId) throws SQLException {
        String sql = "UPDATE employees SET isFinishedWorking=TRUE WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        }
    }

    public EmployeeDTO getEmployee(int employeeId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new EmployeeDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("branch"),
                        rs.getString("bankAccount"),
                        rs.getInt("salary"),
                        rs.getString("startDate"),
                        rs.getInt("vacationDays"),
                        rs.getInt("sickDays"),
                        rs.getDouble("educationFund"),
                        rs.getDouble("socialBenefits"),
                        rs.getString("password"),
                        rs.getBoolean("isFinishedWorking")
                );
            } else {
                return null; // or throw an exception if preferred
            }
        }
    }

    public void updateBranch(int employeeId, String newBranch) throws SQLException {
        String sql = "UPDATE employees SET branch=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newBranch);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        }
    }

    public void updateSalary(int employeeId, int newSalary) throws SQLException {
        String sql = "UPDATE employees SET salary=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newSalary);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        }
    }

    public void updateBankAccount(int employeeId, String newBankAccount) throws SQLException {
        String sql = "UPDATE employees SET bankAccount=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newBankAccount);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        }
    }

    public void updateVacationDays(int employeeId, int newVacationDays) throws SQLException {
        String sql = "UPDATE employees SET vacationDays=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newVacationDays);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        }
    }

    public void updateSickDays(int employeeId, int newSickDays) throws SQLException {
        String sql = "UPDATE employees SET sickDays=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newSickDays);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        }
    }

    public void updateEducationFund(int employeeId, double newEducationFund) throws SQLException {
        String sql = "UPDATE employees SET educationFund=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newEducationFund);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        }
    }

    public void updateSocialBenefits(int employeeId, double newSocialBenefits) throws SQLException {
        String sql = "UPDATE employees SET socialBenefits=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newSocialBenefits);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        }
    }

    public void updatePassword(int employeeId, String newPassword) throws SQLException {
        String sql = "UPDATE employees SET password=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        }
    }

    public ArrayList<EmployeeDTO> getAllEmployees() throws SQLException {
        String sql = "SELECT * FROM employees";
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(new EmployeeDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("branch"),
                        rs.getString("bankAccount"),
                        rs.getInt("salary"),
                        rs.getString("startDate"),
                        rs.getInt("vacationDays"),
                        rs.getInt("sickDays"),
                        rs.getDouble("educationFund"),
                        rs.getDouble("socialBenefits"),
                        rs.getString("password"),
                        rs.getBoolean("isFinishedWorking")
                ));
            }
        }
        return employees;
    }

    public void checkEmployee(int employeeId) throws SQLException { //needed?
        String sql = "SELECT COUNT(*) FROM employees WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("Employee with ID " + employeeId + " does not exist.");
            }
        }
    }

}
