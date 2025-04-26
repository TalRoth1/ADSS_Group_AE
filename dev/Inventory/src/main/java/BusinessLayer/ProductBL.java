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
    private HashMap<Integer, Integer> minQuantity; // branchID -> minQuantity
    private HashMap<Integer, Integer> profitAmount; // branchID -> profitAmount

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

        this.inventoryShelfItems = new HashMap<>();
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
        return inventoryShelfItems.getOrDefault(branchID, new ArrayList<>()).size();
    }

    public int getMinQuantity(int branchID) {
        return minQuantity.getOrDefault(branchID, 0);
    }

    public int getProfit(int branchID) {
        return profitAmount.getOrDefault(branchID, 0);
    }

    public HashMap<Integer, Integer> getProfits() {
        return profitAmount;
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

    public void setMinQuantity(int branchID, int minQuantity) {
        if (minQuantity < 0) {
            throw new IllegalArgumentException("Minimum quantity cannot be negative.");
        }
        if (!inventoryShelfItems.containsKey(branchID)) {
            throw new IllegalArgumentException("Branch ID does not exist in the inventory.");
        }
        minQuantity = Math.max(minQuantity, 0);
        this.minQuantity.put(branchID, minQuantity);
    }

    // ================== Additional ==================

    public void addItemToBranch(int branchID, ItemBL item) {
        inventoryShelfItems.putIfAbsent(branchID, new ArrayList<>());
        inventoryShelfItems.get(branchID).add(item);
    }

    public void removeItemFromBranch(int branchID, int itemID) {
        List<ItemBL> items = inventoryShelfItems.get(branchID);
        if (items != null) {
            items.removeIf(item -> item.getItemID() == itemID);
        }
    }

    public List<ItemBL> getItemsInBranch(int branchID) {
        return inventoryShelfItems.getOrDefault(branchID, new ArrayList<>());
    }

    public void addProfit(int branchID, double amount) {
        int discountedAmount = (int) (amount * (100 - discount) / 100);
        int existingProfit = profitAmount.getOrDefault(branchID, 0);
        profitAmount.put(branchID, existingProfit + discountedAmount);
    }

    public boolean hasBranch(int branchID) {
        return inventoryShelfItems.containsKey(branchID);
    }

    public void initializeBranch(int branchID) {
        inventoryShelfItems.putIfAbsent(branchID, new ArrayList<>());
        minQuantity.putIfAbsent(branchID, 0);
        profitAmount.putIfAbsent(branchID, 0);
    }

}
