package DataLayer;

import DTO.DriverDTO;
import DTO.EmployeeDTO;
import DataLayer.DAOs.DriverDAO;
import DataLayer.DAOs.EmployeeDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


public class DriverController {

    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    DriverDAO driverDAO;
    EmployeeDAO employeeDAO;

    public DriverController(EmployeeController employeeController) {
        String DB_URL = "drivers.db";
        DBConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
        this.driverDAO = new DriverDAO(connection);
        this.employeeDAO = employeeController.getEmployeeDAO();
    }

    public void addDriver(DriverDTO driver) {
        try {
            for (String LicenseType : driver.getLicenseTypes()) {
                driverDAO.addDriver(driver.getId(), LicenseType);
            }
        } catch (Exception e) {
            System.out.println("Error adding driver: " + e.getMessage());
        }
    }

    public void updateDriver(int id, String licenseType) {
        try {
            driverDAO.updateDriver(id, licenseType);
        } catch (Exception e) {
            System.out.println("Error updating driver: " + e.getMessage());
        }
    }

    public void deleteDriver(DriverDTO driver) {
        try {
            for (String LicenseType : driver.getLicenseTypes()) {
                driverDAO.deleteDriver(driver.getId(), LicenseType);
            }
        } catch (Exception e) {
            System.out.println("Error deleting driver: " + e.getMessage());
        }
    }

    public DriverDTO getDriver(int id) {
        try {
            ResultSet rs = driverDAO.getDriver(id);
            EmployeeDTO employee = employeeDAO.getEmployee(id);
            if (employee != null && rs.next()) {
                return new DriverDTO(rs.getInt("id"), employee.getName(), employee.getBranch(),
                        employee.getBankAccount(), employee.getSalary(), employee.getStartDate(),
                        employee.getVacationDays(), employee.getSickDays(), employee.getEducationFund(),
                        employee.getSocialBenefits(), employee.getPassword(), rs.getBoolean("isFinishedWorking"));
            }
        } catch (Exception e) {
            System.out.println("Error getting driver: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<DriverDTO> getAllDrivers() {
        ArrayList<DriverDTO> drivers = new ArrayList<>();
        try {
            ResultSet rs = driverDAO.getAllDrivers();
            while (rs.next()) {
                int id = rs.getInt("id");
                EmployeeDTO employee = employeeDAO.getEmployee(id);
                if (employee != null) {
                    drivers.add(new DriverDTO(rs.getInt("id"), employee.getName(), employee.getBranch(),
                            employee.getBankAccount(), employee.getSalary(), employee.getStartDate(),
                            employee.getVacationDays(), employee.getSickDays(), employee.getEducationFund(),
                            employee.getSocialBenefits(), employee.getPassword(), rs.getBoolean("isFinishedWorking")));
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting all drivers: " + e.getMessage());
        }
        return drivers;
    }

}
