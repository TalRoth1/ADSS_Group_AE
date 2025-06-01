package DataLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;

import DTO.EmployeeDTO;
import DTO.ShiftDTO;
import DataLayer.DAOs.ShiftAssignedDAO;
import DataLayer.DAOs.ShiftDAO;
import DataLayer.DAOs.ShiftPreferredDAO;
import DataLayer.DAOs.ShiftReqRolesDAO;

public class ShiftController {

    private DBConnection dbConnection = new DBConnection();
    private DBConnection dbConnection2 = new DBConnection();
    private DBConnection dbConnection3 = new DBConnection();
    private DBConnection dbConnection4 = new DBConnection();
    private ShiftAssignedDAO shiftAssignedDAO;
    private ShiftDAO shiftDAO;
    private ShiftPreferredDAO shiftPreferredDAO;
    private ShiftReqRolesDAO shiftReqRolesDAO;

    public ShiftController() {
        this.dbConnection.connect("shift_assigned.db");
        this.dbConnection2.connect("shifts.db");
        this.dbConnection3.connect("preferred_shifts.db");
        this.dbConnection4.connect("shift_req_roles.db");
    }

    public void addShiftAssigned(EmployeeDTO employeeDTO, ShiftDTO shiftDTO, String role) {
        try {
            shiftAssignedDAO.addAssignedShift(shiftDTO.getId(), employeeDTO.getId(), role);
        } catch (Exception e) {
            System.out.println("Error adding shift assigned: " + e.getMessage());
        }
    }

    public void addShift(ShiftDTO shiftDTO) {
        try {
            shiftDAO.addShift(shiftDTO.getId(), shiftDTO.getDate().toString(), shiftDTO.getShiftType(), shiftDTO.getBranch().getId(),
                    shiftDTO.getStartTime(), shiftDTO.getEndTime(), shiftDTO.getShiftManagerId());
        } catch (Exception e) {
            System.out.println("Error adding shift: " + e.getMessage());
        }
    }

    public void addPreferredShift(EmployeeDTO employeeDTO, ShiftDTO shiftDTO, String role) {
        try {
            shiftPreferredDAO.addPreferredShift(shiftDTO.getId(), employeeDTO.getId(), role);
        } catch (Exception e) {
            System.out.println("Error adding preferred shift: " + e.getMessage());
        }
    }

    public void addShiftReqRoles(ShiftDTO shiftDTO, String role, int amount) {
        try {
            shiftReqRolesDAO.addRequiredRole(shiftDTO.getId(), role, amount);
        } catch (Exception e) {
            System.out.println("Error adding shift required roles: " + e.getMessage());
        }
    }

    public void updateShiftAssigned(EmployeeDTO employeeDTO, ShiftDTO shiftDTO, String role) {
        try {
            shiftAssignedDAO.addAssignedShift(shiftDTO.getId(), employeeDTO.getId(), role);
        } catch (Exception e) {
            System.out.println("Error updating shift assigned: " + e.getMessage());
        }
    }

    public void updateShift(ShiftDTO shiftDTO) {
        try {
            shiftDAO.updateShift(shiftDTO.getId(), shiftDTO.getDate().toString(), shiftDTO.getShiftType(),
                    shiftDTO.getBranch().getId(), shiftDTO.getStartTime(), shiftDTO.getEndTime(),
                    shiftDTO.getShiftManagerId());
        } catch (Exception e) {
            System.out.println("Error updating shift: " + e.getMessage());
        }
    }

    public void deleteShiftAssigned(EmployeeDTO employeeDTO, ShiftDTO shiftDTO) {
        try {
            int shiftId = shiftDTO.getId();
            int employeeId = employeeDTO.getId();
            shiftAssignedDAO.removeAssignedShift(shiftId, employeeId);
        } catch (Exception e) {
            System.out.println("Error deleting shift assigned: " + e.getMessage());
        }
    }

    public void deleteShift(ShiftDTO shiftDTO) {
        try {
            String date = shiftDTO.getDate().toString();
            String shiftType = shiftDTO.getShiftType();
            int locationId = shiftDTO.getBranch().getId();
            shiftDAO.deleteShift(date, shiftType, locationId);
        } catch (Exception e) {
            System.out.println("Error deleting shift: " + e.getMessage());
        }
    }

    public void deletePreferredShift(EmployeeDTO employeeDTO, ShiftDTO shiftDTO) {
        try {
            int shiftId = shiftDTO.getId();
            int employeeId = employeeDTO.getId();
            shiftPreferredDAO.removePreferredShift(shiftId, employeeId);
        } catch (Exception e) {
            System.out.println("Error deleting preferred shift: " + e.getMessage());
        }
    }

    public void deleteShiftReqRoles(ShiftDTO shiftDTO, String role) {
        try {
            int shiftId = shiftDTO.getId();
            shiftReqRolesDAO.removeRequiredRole(shiftId, role);
        } catch (Exception e) {
            System.out.println("Error deleting shift required roles: " + e.getMessage());
        }
    }

}
