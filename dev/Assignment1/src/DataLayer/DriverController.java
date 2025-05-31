package DataLayer;

import DTO.DriverDTO;
import DTO.EmployeeDTO;
import DataLayer.DAOs.DriverDAO;
import DataLayer.DAOs.DriverLicenseDAO;
import DataLayer.DAOs.EmployeeDAO;
import DomainLayer.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DriverController {

    private DBConnection dbConnection = new DBConnection();
    private DBConnection dbConnection2 = new DBConnection();
    DriverLicenseDAO driverLicenseDAO;
    EmployeeController employeeController;
    DriverDAO driverDAO;

    public DriverController(EmployeeController employeeController) {
        this.employeeController = employeeController;
        String DB_URL = "drivers.db";
        dbConnection.connect(DB_URL);
        String DB_URL2 = "driver_license.db";
        dbConnection2.connect(DB_URL2);
        this.driverLicenseDAO = new DriverLicenseDAO(dbConnection2.getConnection());
        this.driverDAO = new DriverDAO(dbConnection.getConnection());
    }

    public void addDriver(DriverDTO driver) {
        try {
            // add driver
            driverDAO.addDriver(driver.getId(), driver.isBusy() ? 1 : 0);

            // add all license types
            for (String licenseType : driver.getLicenseTypes()) {
                driverLicenseDAO.addDriver(driver.getId(), licenseType);
            }
        } catch (Exception e) {
            System.out.println("Error adding driver: " + e.getMessage());
        }
    }

    public void updateDriver(DriverDTO driver) {
        try {
            // update driver
            driverDAO.updateDriver(driver.getId(), driver.isBusy() ? 1 : 0);

            // Update license types
            driverLicenseDAO.deleteAllLicenses(driver.getId());
            for (String licenseType : driver.getLicenseTypes()) {
                driverLicenseDAO.addDriver(driver.getId(), licenseType);
            }
        } catch (Exception e) {
            System.out.println("Error updating driver: " + e.getMessage());
        }
    }

    // assuming employee was deleted in emoployees beforehand
    public void deleteDriver(int id) {
        try {
            driverDAO.deleteDriver(id);
            driverLicenseDAO.deleteAllLicenses(id);
        } catch (Exception e) {
            System.out.println("Error deleting driver: " + e.getMessage());
        }
    }

    public DriverDTO getDriver(int id) {
        try {
            ResultSet rs = driverDAO.getDriver(id);
            if (rs.next()) {
                EmployeeDTO employee = employeeController.getEmployee(id);
                if (employee != null) {
                    ArrayList<String> licenseTypes = new ArrayList<>();
                    ResultSet licenseRs = driverLicenseDAO.getDriver(id);
                    while (licenseRs.next()) {
                        licenseTypes.add(licenseRs.getString("licenseType"));
                    }
                    return new DriverDTO(rs.getInt("id"), employee.getName(), employee.getBranchid(),
                            employee.getBankAccount(), employee.getSalary(), employee.getStartDate(),
                            employee.getVacationDays(), employee.getSickDays(), employee.getEducationFund(),
                            employee.getSocialBenefits(), employee.getPassword(), rs.getBoolean("isFinishedWorking"),
                            licenseTypes, rs.getInt("isBusy") == 1);
                }
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
                EmployeeDTO employee = employeeController.getEmployee(id);
                if (employee != null) {
                    ArrayList<String> licenseTypes = new ArrayList<>();
                    ResultSet licenseRs = driverLicenseDAO.getDriver(id);
                    while (licenseRs.next()) {
                        licenseTypes.add(licenseRs.getString("licenseType"));
                    }
                    drivers.add(new DriverDTO(rs.getInt("id"), employee.getName(), employee.getBranchid(),
                            employee.getBankAccount(), employee.getSalary(), employee.getStartDate(),
                            employee.getVacationDays(), employee.getSickDays(), employee.getEducationFund(),
                            employee.getSocialBenefits(), employee.getPassword(), rs.getBoolean("isFinishedWorking"),
                            licenseTypes, rs.getInt("isBusy") == 1));
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting all drivers: " + e.getMessage());
        }
        return drivers;
    }

}
