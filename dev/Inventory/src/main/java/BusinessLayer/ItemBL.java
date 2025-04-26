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

    protected int getItemID() {
        return itemID;
    }

    protected int getProductID() {
        return productID;
    }

    protected String getName() {
        return name;
    }

    protected boolean isDef() {
        return isDef;
    }

    protected boolean isExpired() {
        return isExpired;
    }

    protected Date getExpirationDate() {
        return expirationDate;
    }

    protected int getBranchID() {
        return branchID;
    }

    protected String[] getLocation() {
        return location;
    }

    // ========== Setters ==========

    protected void setName(String name) {
        this.name = name;
    }

    protected void setDef(boolean def) {
        isDef = def;
    }

    protected void setExpired(boolean expired) {
        isExpired = expired;
    }

    protected void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    protected void setLocation(String[] location) {
        this.location = location;
    }

    // ========== Utility Methods ==========

    protected boolean checkIfExpired() {
        Date today = new Date();
        this.isExpired = expirationDate.before(today);
        return this.isExpired;
    }

    protected void markAsDefected() {
        this.isDef = true;
    }

    protected void updateLocation(String[] newLocation) {
        this.location = newLocation;
    }
}
