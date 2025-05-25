package DTO;

public class EmployeeRoleDTO {

    private int employeeId;
    private String role;

    public EmployeeRoleDTO(int employeeId, String role) {
        this.employeeId = employeeId;
        this.role = role;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getRole() {
        return role;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
