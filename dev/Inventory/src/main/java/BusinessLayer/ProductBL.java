package BusinessLayer;

import java.util.HashMap;

public class ProductBL {
    private int productID;
    private String name;
    private double costPrice;
    private double sellingPrice;
    private int discount;
    private int producerID;
    private String[] categories;

    private final HashMap<Integer, Integer> inventoryShelfQuantity; // branchID -> quantity
    private final HashMap<Integer, Long> minQuantity;               // branchID -> min quantity
    private final HashMap<Integer, Long> profitAmount;              // branchID -> profit

    private final Object nameLock = new Object();
    private final Object priceLock = new Object();

    public ProductBL(int productID, String name, double costPrice, double sellingPrice, int discount,
                     int producerID, String[] categories) {
        this.productID = productID;
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.producerID = producerID;
        this.categories = categories;

        this.inventoryShelfQuantity = new HashMap<>();
        this.minQuantity = new HashMap<>();
        this.profitAmount = new HashMap<>();
    }

    // ================== Getters ==================

    public int getProductID() {
        return productID;
    }

    public String getName() {
        synchronized (nameLock) {
            return name;
        }
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getSellingPrice() {
        synchronized (priceLock) {
            return sellingPrice;
        }
    }

    public int getDiscount() {
        return discount;
    }

    public int getProducerID() {
        return producerID;
    }

    public String[] getCategories() {
        return categories;
    }

    public int getInventoryQuantity(int branchID) {
        return inventoryShelfQuantity.get(branchID);
    }

    public long getMinQuantity(int branchID) {
        return minQuantity.get(branchID);
    }

    public long getProfit(int branchID) {
        return profitAmount.get(branchID);
    }

    // ================== Setters ==================

    public void setName(String newName) {
        synchronized (nameLock) {
            this.name = newName;
        }
    }

    public void setSellingPrice(double newPrice) {
        synchronized (priceLock) {
            this.sellingPrice = newPrice;
        }
    }

    public void setDiscount(int newDiscount) {
        this.discount = newDiscount;
    }

    public void setCategories(String[] newCategories) {
        this.categories = newCategories;
    }

}
