package DTO;

//import java.time.LocalDate;
import java.util.List;

public class ShiftDTO {

    private String date;
    private String shiftType;
    private LocationDTO branch;
    private int startTime;
    private int endTime;
    private int shiftManagerId;
    private int numOfRequiredcashiers;
    private int numOfRequireddrivers;
    private int numOfRequiredstoreKeepers;
    private int numOfRequiredshipmentManagers;
    private boolean isShipmentShift;
    private List<Integer> employees;

    public ShiftDTO(String date, String shiftType, int startTime, int endTime, int shiftManagerId,
            int numOfRequiredcashiers, int numOfRequireddrivers, int numOfRequiredstoreKeepers,
            int numOfRequiredshipmentManagers, boolean isShipmentShift, LocationDTO branch) {
        this.branch = branch;
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftManagerId = shiftManagerId;
        this.numOfRequiredcashiers = numOfRequiredcashiers;
        this.numOfRequireddrivers = numOfRequireddrivers;
        this.numOfRequiredstoreKeepers = numOfRequiredstoreKeepers;
        this.numOfRequiredshipmentManagers = numOfRequiredshipmentManagers;
        this.isShipmentShift = isShipmentShift;
    }

    public String getDate() {
        return date;
    }

    public String getShiftType() {
        return shiftType;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getShiftManagerId() {
        return shiftManagerId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setShiftManagerId(int shiftManagerId) {
        this.shiftManagerId = shiftManagerId;
    }

    public int getNumOfRequiredcashiers() {
        return numOfRequiredcashiers;
    }

    public int getNumOfRequireddrivers() {
        return numOfRequireddrivers;
    }

    public int getNumOfRequiredstoreKeepers() {
        return numOfRequiredstoreKeepers;
    }

    public int getNumOfRequiredshipmentManagers() {
        return numOfRequiredshipmentManagers;
    }

    public void setNumOfRequiredcashiers(int numOfRequiredcashiers) {
        this.numOfRequiredcashiers = numOfRequiredcashiers;
    }

    public void setNumOfRequireddrivers(int numOfRequireddrivers) {
        this.numOfRequireddrivers = numOfRequireddrivers;
    }

    public void setNumOfRequiredstoreKeepers(int numOfRequiredstoreKeepers) {
        this.numOfRequiredstoreKeepers = numOfRequiredstoreKeepers;
    }

    public void setNumOfRequiredshipmentManagers(int numOfRequiredshipmentManagers) {
        this.numOfRequiredshipmentManagers = numOfRequiredshipmentManagers;
    }

    public boolean isShipmentShift() {
        return isShipmentShift;
    }

    public void setBranch(LocationDTO branch) {
        this.branch = branch;
    }
    public LocationDTO getBranch() {
        return branch;
    }

}
