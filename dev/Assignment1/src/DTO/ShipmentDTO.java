package DTO;

import java.util.List;
import java.util.Map;
import java.util.Date;

public class ShipmentDTO {
    private int id;
    private Date dateCreated;
    private Date dateSent;
    private TruckDTO truck;
    private DriverDTO driver;
    private String status;
    private DocumentDTO document;
    private String shiftType;

    public ShipmentDTO(int id, Date dateCreated, Date dateSent, TruckDTO truck, DriverDTO driver, String status,
                       DocumentDTO document, String shiftType) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.dateSent = dateSent;
        this.truck = truck;
        this.driver = driver;
        this.status = status;
        this.document = document;
        this.shiftType = shiftType;
    }
    public int getId() {
        return id;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public Date getDateSent() {
        return dateSent;
    }
    public TruckDTO getTruck() {
        return truck;
    }
    public DriverDTO getDriver() {
        return driver;
    }
    public String getStatus() {
        return status;
    }
    public DocumentDTO getDocument() {
        return document;
    }
    public String getShiftType() {
        return shiftType;
    }
    

}
