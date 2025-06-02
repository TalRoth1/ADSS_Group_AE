// package DAL;

// public class ItemDAO {
//     private boolean isPersisted = false;
//     private int itemID;
//     private String itemName;
//     private double itemPrice;

//     public ItemDAO(int itemID, String itemName, double itemPrice) {
//         this.itemID = itemID;
//         this.itemName = itemName;
//         this.itemPrice = itemPrice;
//     }

//     public int getItemID() {
//         return itemID;
//     }

//     public String getItemName() {
//         return itemName;
//     }

//     public void setItemName(String itemName) {
//         if (isPersisted) {
//             this.itemName = itemName;
//         }
//     }

//     public double getItemPrice() {
//         return itemPrice;
//     }

//     public void setItemPrice(double itemPrice) {
//         if (isPersisted) {
//             this.itemPrice = itemPrice;
//         }
//     }

//     public boolean isPersisted() {
//         return isPersisted;
//     }

//     public void persist() {
//         this.isPersisted = true;
//     }
// }
package DAL;

import java.util.Date;

public class ItemDAO {
    private boolean isPersisted = false;

    private final int itemID;
    private final int productID;
    private String name;
    private boolean isDef;
    private boolean isExpired;
    private Date expirationDate;
    private int branchID;
    private int buyingPrice;
    private String[] location;

    public ItemDAO(int itemID, int productID, String name, boolean isDef, boolean isExpired,
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

    public void setName(String name) {
        if (isPersisted)
            this.name = name;
    }

    public void setDef(boolean def) {
        if (isPersisted)
            this.isDef = def;
    }

    public void setExpired(boolean expired) {
        if (isPersisted)
            this.isExpired = expired;
    }

    public void setBranchID(int branchID) {
        if (isPersisted)
            this.branchID = branchID;
    }

    public void setBuyingPrice(int buyingPrice) {
        if (isPersisted)
            this.buyingPrice = buyingPrice;
    }

    public void setLocation(String[] location) {
        if (isPersisted)
            this.location = location;
    }

    public void setExpirationDate(Date expirationDate) {
        if (isPersisted)
            this.expirationDate = expirationDate;
    }

    public void persist() {
        this.isPersisted = true;
    }

    public boolean isPersisted() {
        return isPersisted;
    }
}
