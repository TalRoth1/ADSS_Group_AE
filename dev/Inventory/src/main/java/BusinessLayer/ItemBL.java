package BusinessLayer;

import java.util.Date;

public class ItemBL {
    private int itemID;
    private int productID;
    private String name;
    private boolean isDef;
    private boolean isExpired;
    private Date expirationDate;
    private int branchID;
    private String[] location;

    public ItemBL(int itemID, int productID, String name, boolean isDef,
                  Date expirationDate, int branchID, String[] location) {
        this.itemID = itemID;
        this.productID = productID;
        this.name = name;
        this.isDef = isDef;
        this.expirationDate = expirationDate;
        this.branchID = branchID;
        this.location = location;
        this.isExpired = false;
    }

    // ========== Getters ==========

    public int getItemID() {
        return itemID;
    }

    public int getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public boolean isDef() {
        return isDef;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public int getBranchID() {
        return branchID;
    }

    public String[] getLocation() {
        return location;
    }

    // ========== Setters ==========

    public void setName(String name) {
        this.name = name;
    }

    public void setDef(boolean def) {
        isDef = def;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public void setLocation(String[] location) {
        this.location = location;
    }

    // ========== Utility Methods ==========

    public boolean checkIfExpired() {
        Date today = new Date();
        this.isExpired = expirationDate.before(today);
        return this.isExpired;
    }

    public void markAsDefected() {
        this.isDef = true;
    }

    public void updateLocation(String[] newLocation) {
        this.location = newLocation;
    }
}
