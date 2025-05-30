package DataLayer.Mappers;

import DTO.EmployeeDTO;
import DomainLayer.Employee;
import DomainLayer.LocationDL;

import java.time.LocalDate;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(Employee e) {
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

    public static Employee toDomain(EmployeeDTO e) {
        if (e == null) {
            return null;
        }
        return new Employee(e.getId(),
                e.getName(),
                LocationMapper.toDomain(e.getBranch()),
                e.getBankAccount(),
                e.getSalary(),
                LocalDate.parse(e.getStartDate()),
                e.getVacationDays(),
                e.getSickDays(),
                e.getEducationFund(),
                e.getSocialBenefits(),
                e.getPassword(),
                e.isFinishedWorking());
    }
}
