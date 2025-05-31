package DataLayer.Mappers;

import DTO.EmployeeDTO;
import DTO.EmployeeRoleDTO;
import DomainLayer.Employee;
import DomainLayer.LocationDL;
import DomainLayer.Role;
import DomainLayer.ShiftEmployee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(ShiftEmployee e) {
        if (e == null) {
            return null;
        }
        return new EmployeeDTO(e.getId(),
                e.getName(),
                e.getBranchid(),
                e.getBankAccount(),
                e.getSalary(),
                e.getStartDate().toString(),
                e.getVacationDays(),
                e.getSickDays(),
                e.getEducationFund(),
                e.getSocialBenefits(),
                e.getPassword(),
                e.isFinishWorking());
        // int id, String name, int branchid, String bankAccount, int salary, String
        // startDate,
        // int vacationDays, int sickDays, double educationFund, double socialBenefits,
        // String password, boolean isFinishedWorking

    }

    public static ShiftEmployee toDomain(EmployeeDTO e) {
        if (e == null) {
            return null;
        }
        return new Employee(e.getId(),
                e.getName(),
                LocationDL.getLocationById(e.getBranchid()), // Assuming LocationDL has a method to get location by ID
                e.getBankAccount(),
                e.getSalary(),
                LocalDate.parse(e.getStartDate()),
                e.getVacationDays(),
                e.getSickDays(),
                e.getEducationFund(),
                e.getSocialBenefits(),
                e.getPassword(),
                e.isFinishedWorking(),
              );

    }

    // for employeeController
    // currently not used
    // public static void StoreRolesToEmployees(List<EmployeeDTO> employees,
    // List<EmployeeRoleDTO> employeeRoles) {
    // // Map employeeId to list of roles
    // Map<Integer, List<Role>> rolesByEmployee = new HashMap<>();

    // for (EmployeeRoleDTO er : employeeRoles) {
    // int employeeId = er.getEmployeeId();
    // Role role = Role.valueOf(er.getRole()); // assumes string matches enum
    // rolesByEmployee.computeIfAbsent(employeeId, k -> new
    // ArrayList<>()).add(role);
    // }
    // for (EmployeeDTO employee : employees) {
    // List<Role> roles = rolesByEmployee.get(employee.getId());
    // employee.setRoles(roles != null ? roles : new ArrayList<>());
    // }
    // }
}
