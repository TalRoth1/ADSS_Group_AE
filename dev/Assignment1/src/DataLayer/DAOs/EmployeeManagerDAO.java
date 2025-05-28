package DataLayer.DAOs;

import DTO.EmployeeManagerDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManagerDAO {

    private Connection connection;

    public EmployeeManagerDAO(Connection connection) {
        this.connection = connection;
    }

    public void addManager(EmployeeManagerDTO manager) throws SQLException {
        String sql = "INSERT INTO employees (id, name, branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, manager.getId());
            pstmt.setString(2, manager.getName());
            pstmt.setString(3, manager.getBranch());
            pstmt.setString(4, manager.getBankAccount());
            pstmt.setInt(5, manager.getSalary());
            pstmt.setString(6, manager.getStartDate());
            pstmt.setInt(7, manager.getVacationDays());
            pstmt.setInt(8, manager.getSickDays());
            pstmt.setDouble(9, manager.getEducationFund());
            pstmt.setDouble(10, manager.getSocialBenefits());
            pstmt.setString(11, manager.getPassword());
            pstmt.executeUpdate();
        }
    }

    public EmployeeManagerDTO getManager(int id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id=? AND isManager=true";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new EmployeeManagerDTO(
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
                        rs.getString("password")
                );
            }
        }
        return null;
    }

    public List<EmployeeManagerDTO> getAllManagers() throws SQLException {
        String sql = "SELECT * FROM employees WHERE isManager=true";
        List<EmployeeManagerDTO> managers = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                EmployeeManagerDTO manager = new EmployeeManagerDTO(
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
                        rs.getString("password")
                );
                managers.add(manager);
            }
        }
        return managers;
    }

    public void removeManager(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id=? AND isManager=true";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Add update methods as needed
}
