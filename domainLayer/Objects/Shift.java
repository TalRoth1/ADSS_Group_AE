import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Shift {
    private Date date;
    private ShiftType shiftType;
    private int duration; 
    private int shiftManagerId;
    private Map<Role, Integer> requiredRoles; // roles and number of employees required
    private List<Integer> assignedEmployeesID; 

    public Shift(Date date, ShiftType shiftType, int duration, int shiftManagerId, Map<Role, Integer> requiredRoles) {
        this.date = date;
        this.shiftType = shiftType;
        this.duration = duration;
        this.shiftManagerId = shiftManagerId;
        this.requiredRoles = requiredRoles;
        this.assignedEmployeesID = new ArrayList<>();
    }

}
