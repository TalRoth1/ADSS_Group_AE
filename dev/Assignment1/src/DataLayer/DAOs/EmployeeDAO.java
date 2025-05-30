package DataLayer.DAOs;

import DTO.EmployeeDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class EmployeeDAO {

    private Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employees (" +
                "id INT PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "locationId INT NOT NULL, " +
                "bankAccount TEXT NOT NULL, " +
                "salary INT NOT NULL, " +
                "startDate DATE NOT NULL, " +
                "vacationDays INT NOT NULL, " +
                "sickDays INT NOT NULL, " +
                "educationFund REAL NOT NULL, " +
                "socialBenefits REAL NOT NULL, " +
                "password TEXT NOT NULL" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shifts table: " + e.getMessage());
            throw e;
        }
    }

    public void addEmployee(int id, String name, int branchid, String bankAccount, int salary, String startDate,
            int vacationDays, int sickDays, double educationFund, double socialBenefits,
            String password) throws SQLException {
        String sql = "INSERT INTO employees (id, name, branch, bankAccount, salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, branchid);
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
        } catch (SQLException e) {
            System.out.println("Error removing employee: " + e.getMessage());
            throw e;
        }
    }

    public void fireEmployee(int employeeId) throws SQLException {
        String sql = "UPDATE employees SET isFinishedWorking=TRUE WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error firing employee: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getEmployee(int employeeId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, employeeId);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            throw new SQLException("Employee with ID " + employeeId + " does not exist.");
        }
        return rs;
    }

    public void updateBranch(int employeeId, String newBranch) throws SQLException {
        String sql = "UPDATE employees SET branch=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newBranch);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee branch: " + e.getMessage());
            throw e;
        }
    }

    public void updateSalary(int employeeId, int newSalary) throws SQLException {
        String sql = "UPDATE employees SET salary=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newSalary);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee salary: " + e.getMessage());
            throw e;
        }
    }

    public void updateBankAccount(int employeeId, String newBankAccount) throws SQLException {
        String sql = "UPDATE employees SET bankAccount=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newBankAccount);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee bank account: " + e.getMessage());
            throw e;
        }
    }

    public void updateVacationDays(int employeeId, int newVacationDays) throws SQLException {
        String sql = "UPDATE employees SET vacationDays=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newVacationDays);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee vacation days: " + e.getMessage());
            throw e;
        }
    }

    public void updateSickDays(int employeeId, int newSickDays) throws SQLException {
        String sql = "UPDATE employees SET sickDays=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newSickDays);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee sick days: " + e.getMessage());
            throw e;
        }
    }

    public void updateEducationFund(int employeeId, double newEducationFund) throws SQLException {
        String sql = "UPDATE employees SET educationFund=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newEducationFund);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee education fund: " + e.getMessage());
            throw e;
        }
    }

    public void updateSocialBenefits(int employeeId, double newSocialBenefits) throws SQLException {
        String sql = "UPDATE employees SET socialBenefits=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newSocialBenefits);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee social benefits: " + e.getMessage());
            throw e;
        }
    }

    public void updatePassword(int employeeId, String newPassword) throws SQLException {
        String sql = "UPDATE employees SET password=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee password: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getAllEmployees() throws SQLException {
        String sql = "SELECT * FROM employees";
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving all employees: " + e.getMessage());
            throw e;
        }
    }

    public void checkEmployee(int employeeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("Employee with ID " + employeeId + " does not exist.");
            }
        } catch (SQLException e) {
            System.out.println("Error checking employee: " + e.getMessage());
            throw e;
        }
    }

    public void setloginEmployee(int employeeId, boolean isLoggedIn) throws SQLException {
        String sql = "UPDATE employees SET isLoggedIn=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, isLoggedIn);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error setting employee login status: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getAllEmployeesInBranch(int branchid) throws SQLException {
        String sql = "SELECT name FROM employees WHERE branch=? AND isFinishedWorking=FALSE";
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving all employees in branch: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getEmployeeBranchId(int employeeId) throws SQLException {
        String sql = "SELECT branch FROM employees WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs;
            } else {
                throw new SQLException("Employee with ID " + employeeId + " does not exist.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving employee branch ID: " + e.getMessage());
            throw e;
        }
    }

}
