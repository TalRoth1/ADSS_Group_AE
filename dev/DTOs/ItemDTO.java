package DTOs;

import java.util.Date;

public class ItemDTO {
    private final int itemID;
    private final int productID;
    private final String name;
    private final boolean isDef;
    private final boolean isExpired;
    private final Date expirationDate;
    private final int branchID;
    private final int buyingPrice;
    private final String[] location;

    public ItemDTO(int itemID, int productID, String name, boolean isDef, boolean isExpired,
            Date expirationDate, int branchID, int buyingPrice, String[] location) {
        this.itemID = itemID;
        this.productID = productID;
        this.name = name;
        this.isDef = isDef;
        this.isExpired = isExpired;
        this.expirationDate = expirationDate;
        this.branchID = branchID;
        this.buyingPrice = buyingPrice;
        this.location = location;
    }

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

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public String[] getLocation() {
        return location;
    }
}
