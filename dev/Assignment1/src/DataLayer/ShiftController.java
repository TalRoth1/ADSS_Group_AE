package DataLayer;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import DTO.EmployeeDTO;
import DTO.LocationDTO;
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
    LocationController locationController;

    public ShiftController(LocationController locationController) {
        this.dbConnection.connect("shift_assigned.db");
        this.dbConnection2.connect("shifts.db");
        this.dbConnection3.connect("preferred_shifts.db");
        this.dbConnection4.connect("shift_req_roles.db");
        this.locationController = locationController;
    }

    public void addShiftAssigned(EmployeeDTO employeeDTO, ShiftDTO shiftDTO, String role) throws SQLException {
        try {
            shiftAssignedDAO.addAssignedShift(shiftDTO.getId(), employeeDTO.getId(), role);
        } catch (Exception e) {
            System.out.println("Error adding shift assigned: " + e.getMessage());
        }
    }

    public void addShift(ShiftDTO shiftDTO) throws SQLException {
        try {
            shiftDAO.addShift(shiftDTO.getId(), shiftDTO.getDate().toString(), shiftDTO.getShiftType(),
                    shiftDTO.getBranch().getId(),
                    shiftDTO.getStartTime(), shiftDTO.getEndTime(), shiftDTO.getShiftManagerId());
        } catch (Exception e) {
            System.out.println("Error adding shift: " + e.getMessage());
        }
    }

    public void addPreferredShift(EmployeeDTO employeeDTO, ShiftDTO shiftDTO, String role) throws SQLException {
        try {
            shiftPreferredDAO.addPreferredShift(shiftDTO.getId(), employeeDTO.getId(), role);
        } catch (Exception e) {
            System.out.println("Error adding preferred shift: " + e.getMessage());
        }
    }

    public void addShiftReqRoles(ShiftDTO shiftDTO, String role, int amount) throws SQLException {
        try {
            shiftReqRolesDAO.addRequiredRole(shiftDTO.getId(), role, amount);
        } catch (Exception e) {
            System.out.println("Error adding shift required roles: " + e.getMessage());
        }
    }

    public void updateShiftAssigned(EmployeeDTO employeeDTO, ShiftDTO shiftDTO, String newRole) throws SQLException {
        try {
            shiftAssignedDAO.addAssignedShift(shiftDTO.getId(), employeeDTO.getId(), newRole);
        } catch (Exception e) {
            System.out.println("Error updating shift assigned: " + e.getMessage());
        }
    }

    public void updateShift(ShiftDTO shiftDTO, String fieldName, int newValue) throws SQLException {
        try {
            shiftDAO.setShiftField(shiftDTO.getId(), fieldName, newValue);
        } catch (Exception e) {
            System.out.println("Error updating shift: " + e.getMessage());
        }
    }

    public void updatePreferredShift(EmployeeDTO employeeDTO, ShiftDTO shiftDTO, String newRole) throws SQLException {
        try {
            shiftPreferredDAO.updatePreferredShiftRole(shiftDTO.getId(), employeeDTO.getId(), newRole);
        } catch (Exception e) {
            System.out.println("Error updating preferred shift: " + e.getMessage());
        }
    }

    public void updateShiftReqRoles(ShiftDTO shiftDTO, String role, int newAmount) throws SQLException {
        try {
            shiftReqRolesDAO.updateRequiredRoleAmount(shiftDTO.getId(), role, newAmount);
        } catch (Exception e) {
            System.out.println("Error updating shift required roles: " + e.getMessage());
        }
    }

    public void deleteShiftAssigned(EmployeeDTO employeeDTO, ShiftDTO shiftDTO) throws SQLException {
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

    public void deletePreferredShift(EmployeeDTO employeeDTO, ShiftDTO shiftDTO) throws SQLException {
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

    public ShiftDTO getShift(int id) throws SQLException {
        ResultSet rs = shiftDAO.getShift(id);
        if (rs.next()) {
            return buildShiftDTO(rs); //helper method to build ShiftDTO from ResultSet
        } else
            throw new SQLException("Shift not found with id: " + id);
    }

    public ArrayList<ShiftDTO> getAllShifts() throws SQLException {
        ArrayList<ShiftDTO> shifts = new ArrayList<>();
        ResultSet rs = shiftDAO.getAllShifts();
        while (rs.next()) {
            shifts.add(buildShiftDTO(rs)); //helper method to build ShiftDTO from ResultSet
        }
        return shifts;
    }

    public ArrayList<ShiftDTO> getAllPrefShifts(int id) {
        ArrayList<ShiftDTO> shifts = new ArrayList<>();
        try {
            ResultSet rs = shiftPreferredDAO.getPreferredShiftsForEmployee(id);
            while (rs.next()) {
                ResultSet shiftResult = shiftDAO.getShift(rs.getInt("shiftId"));
                shifts.add(buildShiftDTO(shiftResult)); //helper method to build ShiftDTO from ResultSet
            }
        } catch (SQLException e) {
            System.out.println("Error getting all preferred shifts: " + e.getMessage());
        }
        return shifts;
    }

    public Map<ShiftDTO, String> getAssignedShiftsForEmployee(int employeeId) {
        Map<ShiftDTO, String> assignedShifts = new HashMap<>();
        try {
            ResultSet rs = shiftAssignedDAO.getAssignedShifts(employeeId);
            while (rs.next()) {
                ResultSet shiftResult = shiftDAO.getShift(rs.getInt("shiftId"));
                ShiftDTO shiftDTO = buildShiftDTO(shiftResult); //helper method to build ShiftDTO from ResultSet
                String role = rs.getString("role");
                assignedShifts.put(shiftDTO, role);
            }
        } catch (SQLException e) {
            System.out.println("Error getting assigned shifts for employee: " + e.getMessage());
        }
        return assignedShifts;
    }


    //helper method to build ShiftDTO from ResultSet
    private ShiftDTO buildShiftDTO(ResultSet rst) throws SQLException {
        int id = rst.getInt("id");
        Date date = rst.getDate("date");
        String shiftType = rst.getString("shiftType");
        LocationDTO branch = locationController.getLocation(rst.getInt("locationId"));
        int startTime = rst.getInt("startTime");
        int endTime = rst.getInt("endTime");
        int shiftManagerId = rst.getInt("shiftManagerId");
        boolean isShipmentShift = rst.getInt("isShipment") == 1;

        Map<String, Integer> requiredRoles = new HashMap<>();
        ResultSet reqRolesResult = shiftReqRolesDAO.getRequiredRolesForShift(id);
        while (reqRolesResult.next()) {
            String role = reqRolesResult.getString("role");
            int amount = reqRolesResult.getInt("amount");
            requiredRoles.put(role, amount);
        }

        Map<Integer, String> assignedEmployeesID = new HashMap<>();
        ResultSet assignedResult = shiftAssignedDAO.getShiftEmployees(id);
        while (assignedResult.next()) {
            int employeeId = assignedResult.getInt("employeeId");
            String role = assignedResult.getString("role");
            assignedEmployeesID.put(employeeId, role);
        }

        Map<Integer, String> availableEmployeesID = new HashMap<>();
        ResultSet preferredResult = shiftPreferredDAO.getPreferredShiftsForShift(id);
        while (preferredResult.next()) {
            int employeeId = preferredResult.getInt("employeeId");
            String role = preferredResult.getString("role");
            availableEmployeesID.put(employeeId, role);
        }

        return new ShiftDTO(id, date, shiftType, startTime, endTime, shiftManagerId, isShipmentShift,
                branch, requiredRoles, assignedEmployeesID, availableEmployeesID);
    }

    public void clearAllShifts() {
        try {
            shiftAssignedDAO.clearTable();
            shiftPreferredDAO.clearTable();
            shiftReqRolesDAO.clearTable();
            shiftDAO.clearTable();
        } catch (SQLException e) {
            System.out.println("Error clearing tables: " + e.getMessage());
        }
    }

}
