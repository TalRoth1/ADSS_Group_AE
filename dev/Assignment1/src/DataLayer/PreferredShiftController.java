package DataLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.EmployeeDTO;
import DTO.ShiftDTO;
import DataLayer.DAOs.PreferredShiftDAO;
import DataLayer.DAOs.ShiftDAO;

public class PreferredShiftController {
    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private PreferredShiftDAO preferredShiftDAO;

    public PreferredShiftController() {
        String DB_URL = "PreferredShift.db";
        DBConnection.connect(DB_URL);
        this.connection = DBConnection.getConnection();
        this.preferredShiftDAO = new PreferredShiftDAO(connection);
    }

    public void addPreferredShift(int employeeId, String shiftDate, String shiftType) {
        try {
            preferredShiftDAO.addPreferredShift(employeeId, shiftDate, shiftType);
        } catch (SQLException e) {
            System.out.println("Error adding preferred shift: " + e.getMessage());
        }
    }

    public void removePreferredShift(int employeeId, String shiftDate, String shiftType) {
        try {
            preferredShiftDAO.removePreferredShift(employeeId, shiftDate, shiftType);
        } catch (SQLException e) {
            System.out.println("Error removing preferred shift: " + e.getMessage());
        }
    }

    public List<ShiftDTO> getPreferredShifts(int employeeId) {
        List<ShiftDTO> preferredShifts = new ArrayList<>();
        try {
            ResultSet rs = preferredShiftDAO.getPreferredShifts(employeeId);
            ShiftDAO shiftDAO = new ShiftDAO(connection);
            while (rs.next()) {
                String shiftDate = rs.getString("shiftDate");
                String shiftType = rs.getString("shiftType");
                ResultSet shiftRs = shiftDAO.getShift(shiftDate, shiftType);
                if (shiftRs.next()) {
                    ShiftDTO shift = new ShiftDTO(
                            shiftDate,
                            shiftType,
                            shiftRs.getInt("startTime"),
                            shiftRs.getInt("endTime"),
                            shiftRs.getInt("shiftManagerId"),
                            shiftRs.getInt("numOfRequiredcashiers"),
                            shiftRs.getInt("numOfRequireddrivers"),
                            shiftRs.getInt("numOfRequiredstoreKeepers"),
                            shiftRs.getInt("numOfRequiredshipmentManagers"),
                            shiftRs.getBoolean("isShipmentShift"),
                            shiftRs.getInt("branchid"));
                    preferredShifts.add(shift);
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting preferred shifts: " + e.getMessage());
        }
        return preferredShifts;
    }

    public List<EmployeeDTO> getPrefShiftEmployees(String shiftDate, String shiftType) { // all employees in this shift
        List<EmployeeDTO> employees = new ArrayList<>();
        try {
            ResultSet rs = preferredShiftDAO.getPrefShiftEmployees(shiftDate, shiftType);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int branchId = rs.getInt("branchid");
                String bankAccount = rs.getString("bankAccount");
                int salary = rs.getInt("salary");
                String startDate = rs.getString("startDate");
                int vacationDays = rs.getInt("vacationDays");
                int sickDays = rs.getInt("sickDays");
                double educationFund = rs.getDouble("educationFund");
                double socialBenefits = rs.getDouble("socialBenefits");
                String password = rs.getString("password");
                boolean isFinishedWorking = rs.getBoolean("isFinishedWorking");

                EmployeeDTO employee = new EmployeeDTO(id, name, branchId, bankAccount, salary, startDate,
                        vacationDays, sickDays, educationFund, socialBenefits, password, isFinishedWorking);
                employees.add(employee);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error getting preferred shift employees: " + e.getMessage());
        }
        return employees;
    }
}
