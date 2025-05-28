package DataLayer.Mappers;

import DTO.EmployeeManagerDTO;
import DomainLayer.EmployeeManager;

import java.time.LocalDate;

public class EmployeeManagerMapper {

    public static EmployeeManagerDTO toDTO(EmployeeManager em) {
        if (em == null) {
            return null;
        }
        return new EmployeeManagerDTO(
            em.getId(),
            em.getName(),
            em.getBankAccount(),
            em.getSalary(),
            em.getStartDate().toString(),
            em.getVacationDays(),
            em.getSickDays(),
            em.getEducationFund(),
            em.getSocialBenefits(),
            em.getPassword(),
            em.isFinishWorking()
        );
    }

    public static EmployeeManager toDomain(EmployeeManagerDTO dto) {
        if (dto == null) {
            return null;
        }
        return new EmployeeManager(
            dto.getId(),
            dto.getName(),
            dto.getBankAccount(),
            dto.getSalary(),
            LocalDate.parse(dto.getStartDate()),
            dto.getVacationDays(),
            dto.getSickDays(),
            dto.getEducationFund(),
            dto.getSocialBenefits(),
            dto.getPassword()
            // Add other fields as needed
        );
    }
}