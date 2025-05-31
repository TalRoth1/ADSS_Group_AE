package DataLayer.Mappers;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import DTO.DriverDTO;
import DTO.LocationDTO;
import DomainLayer.DriverDL;

public class DriverMapper {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DriverDTO toDTO(DriverDL dl) {
        return new DriverDTO(
                dl.getId(),
                dl.getName(),
                LocationMapper.toDTO(dl.getBranch()), // assuming LocationDL has getId() IMPORTANT
                dl.getBankAccount(),
                dl.getSalary(),
                dl.getStartDate().format(dateFormatter),
                dl.getVacationDays(),
                dl.getSickDays(),
                dl.getEducationFund(),
                dl.getSocialBenefits(),
                dl.getPassword(),
                dl.isFinishWorking(), // Assuming this exists in ShiftEmployee or DriverDL
                dl.getLicenceType(),
                dl.isBusy
        );
    }

    public static DriverDL toDL(DriverDTO dto) {// this should get the branch from the dto
        return new DriverDL(
                dto.getId(),
                dto.getName(),
                LocationMapper.toDomain(dto.getBranchid()), // Convert LocationDTO to LocationDL
                dto.getBankAccount(),
                dto.getSalary(),
                LocalDate.parse(dto.getStartDate(), dateFormatter),
                dto.getVacationDays(),
                dto.getSickDays(),
                dto.getEducationFund(),
                dto.getSocialBenefits(),
                dto.getPassword(),
                dto.getLicenseTypes()
        );
    }
    
}
