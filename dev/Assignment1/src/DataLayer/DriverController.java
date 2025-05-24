package DataLayer;

import DTO.DriverDTO;
import java.sql.ResultSet;
import java.util.ArrayList;

//TODO: move all of this to employeeController OwO

public class DriverController {
    DriverDAO driverDAO;
    
    public DriverController(DriverDAO driverDAO) {
        this.driverDAO = driverDAO;
    }

    public void addDriver(DriverDTO driver) {
        try {
            for(String LisenceType : driver.getLicenseTypes()) {
                driverDAO.addDriver(driver.getId(), LisenceType);
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
            for(String LisenceType : driver.getLicenseTypes()) {
                driverDAO.deleteDriver(driver.getId(), LisenceType);
            }
        } catch (Exception e) {
            System.out.println("Error deleting driver: " + e.getMessage());
        }
    }

    public DriverDTO getDriver(int id) {
        try {
            ResultSet rs = driverDAO.getDriver(id);
            if (rs.next()) {
                return new DriverDTO(rs.getInt("id"), rs.getString("name"), rs.getString("branch"),
                        rs.getString("bankAccount"), rs.getInt("salary"), rs.getString("startDate"),
                        rs.getInt("vacationDays"), rs.getInt("sickDays"), rs.getDouble("educationFund"),
                        rs.getDouble("socialBenefits"), rs.getString("password"), rs.getString("licenseNumber"),
                        rs.getString("vehicleType"), rs.getString("drivingExperience"));
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
                drivers.add(new DriverDTO(rs.getInt("id"), rs.getString("name"), rs.getString("branch"),
                        rs.getString("bankAccount"), rs.getInt("salary"), rs.getString("startDate"),
                        rs.getInt("vacationDays"), rs.getInt("sickDays"), rs.getDouble("educationFund"),
                        rs.getDouble("socialBenefits"), rs.getString("password"), rs.getString("licenseNumber"),
                        rs.getString("vehicleType"), rs.getString("drivingExperience")));
            }
        } catch (Exception e) {
            System.out.println("Error getting all drivers: " + e.getMessage());
        }
        return drivers;
    }

    

}
