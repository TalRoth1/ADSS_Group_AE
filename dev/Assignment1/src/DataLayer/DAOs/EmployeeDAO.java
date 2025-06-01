package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DataLayer.DBConnection;
import DomainLayer.LocationDL;

public class EmployeeDAO {

    private Connection connection;
    private Connection connectionLocation = DBConnection.getConnection();
    private LocationDAO locationDAO = new LocationDAO(connectionLocation);

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing employeesDAO: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employees ("
                + "id INT PRIMARY KEY, "
                + "name TEXT NOT NULL, "
                + "bankAccount TEXT NOT NULL, "
                + "salary INT NOT NULL, "
                + "startDate TEXT NOT NULL, "
                + "vacationDays INT NOT NULL, "
                + "sickDays INT NOT NULL, "
                + "educationFund FLOAT NOT NULL, "
                + "socialBenefits FLOAT NOT NULL, "
                + "password TEXT NOT NULL"
                + "isFired INT NOT NULL, " //false 0, true 1
                + "isloggedIn INT NOT NULL, " //false 0, true 1
                + "locationId INT NOT NULL, "
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating shifts table: " + e.getMessage());
            throw e;
        }
    }

    public void addEmployee(int id, String name, LocationDL branch, String bankAccount, int salary, String startDate,
            int vacationDays, int sickDays, double educationFund, double socialBenefits,
            String password) throws SQLException {
        String sql = "INSERT INTO employees (id, name, bankAccount, salary, startDate, vacationDays, sickDays, educationFund, socialBenefits, password, isFired, isLoggedIn, locationId), VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, bankAccount);
            pstmt.setInt(4, salary);
            pstmt.setString(5, startDate);
            pstmt.setInt(6, vacationDays);
            pstmt.setInt(7, sickDays);
            pstmt.setDouble(8, educationFund);
            pstmt.setDouble(9, socialBenefits);
            pstmt.setString(10, password);
            pstmt.setBoolean(11, false); // isFired
            pstmt.setBoolean(12, false); // isLoggedIn
            int locationId = locationDAO.getLocationId(branch.getStreet(), branch.getStreetNumber(), branch.getCity());
            pstmt.setInt(13, locationId);
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

    public void updateEmployeeByField(int employeeId, String fieldName, Object newValue) throws SQLException {
        String sql = "UPDATE employees SET " + fieldName + "=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, newValue);
            pstmt.setInt(2, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee field: " + e.getMessage());
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

    public ResultSet getAllEmployeesInBranch(int branchid) throws SQLException {
        String sql = "SELECT name FROM employees WHERE branch=? AND isFinishedWorking=FALSE";
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving all employees in branch: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getEmployeeField(int employeeId, String fieldName) throws SQLException {
        String sql = "SELECT " + fieldName + " FROM employees WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Employee with ID " + employeeId + " does not exist.");
            }
            return rs;
        } catch (SQLException e) {
            System.out.println("Error retrieving employee field: " + e.getMessage());
            throw e;
        }
    }

    public void clearTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM employees");
        } catch (SQLException e) {
            System.out.println("Error clearing employees table: " + e.getMessage());
            throw e;
        }
    }

}
