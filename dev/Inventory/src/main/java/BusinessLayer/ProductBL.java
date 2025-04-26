package BusinessLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductBL {
    private int productID;
    private String name;
    private double costPrice;
    private double sellingPrice;
    private int discount;
    private int producerID;
    private String[] categories;

    private HashMap<Integer, List<ItemBL>> inventoryShelfItems; // branchID -> list of ItemBL
    private HashMap<Integer, Integer> minQuantity;                 // branchID -> minQuantity
    private HashMap<Integer, Integer> profitAmount;                // branchID -> profitAmount

    private final Object nameLock = new Object();
    private final Object priceLock = new Object();

    protected ProductBL(int productID, String name, double costPrice, double sellingPrice, int discount,
                     int producerID, String[] categories) {
        this.productID = productID;
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.producerID = producerID;
        this.categories = categories;

        this.inventoryShelfItems = new HashMap<>();
        this.minQuantity = new HashMap<>();
        this.profitAmount = new HashMap<>();
    }

    // ================== Getters ==================

    protected int getProductID() {
        return productID;
    }

    protected String getName() {
        synchronized (nameLock) {
            return name;
        }
    }

    protected double getCostPrice() {
        return costPrice;
    }

    protected double getSellingPrice() {
        synchronized (priceLock) {
            return sellingPrice;
        }
    }

    protected int getDiscount() {
        return discount;
    }

    protected int getProducerID() {
        return producerID;
    }

    protected String[] getCategories() {
        return categories;
    }

    protected int getInventoryQuantity(int branchID) {
        return inventoryShelfItems.getOrDefault(branchID, new ArrayList<>()).size();
    }

    protected int getMinQuantity(int branchID) {
        return minQuantity.get(branchID);
    }

    protected int getProfit(int branchID) {
        return profitAmount.get(branchID);
    }

    protected HashMap<Integer, Integer> getProfits() {
        return profitAmount;
    }


    // ================== Setters ==================

    protected void setName(String newName) {
        synchronized (nameLock) {
            this.name = newName;
        }
    }

    protected void setSellingPrice(double newPrice) {
        synchronized (priceLock) {
            this.sellingPrice = newPrice;
        }
    }

    protected void setDiscount(int newDiscount) {
        this.discount = newDiscount;
    }

    protected void setCategories(String[] newCategories) {
        this.categories = newCategories;
    }
    // ================== Additional ==================

    protected void addItemToBranch(int branchID, ItemBL item) {
        inventoryShelfItems.putIfAbsent(branchID, new ArrayList<>());
        inventoryShelfItems.get(branchID).add(item);
    }

    protected void removeItemFromBranch(int branchID, int itemID) {
        List<ItemBL> items = inventoryShelfItems.get(branchID);
        if (items != null) {
            items.removeIf(item -> item.getItemID() == itemID);
        }
    }

    protected List<ItemBL> getItemsInBranch(int branchID) {
        return inventoryShelfItems.getOrDefault(branchID, new ArrayList<>());
    }

}
