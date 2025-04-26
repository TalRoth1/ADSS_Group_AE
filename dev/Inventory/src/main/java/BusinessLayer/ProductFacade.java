package BusinessLayer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductFacade {
    private static ProductFacade instance = null;
    private Map<Integer, ProductBL> products;
    private Map<Integer, ItemBL> items;
    private int nextProductID;
    private int nextItemID;

    private ProductFacade() {
        this.products = new HashMap<>();
        this.items = new HashMap<>();
        this.nextProductID = 1;
        this.nextItemID = 1;
    }

    public static ProductFacade getInstance() {
        if (instance == null) {
            synchronized (ProductFacade.class) {
                if (instance == null) {
                    instance = new ProductFacade();
                }
            }
        }
        return instance;
    }

    // ================== Product Management ==================

    public synchronized int addProduct(String name, double costPrice, double sellingPrice, int discount,
                                       int producerID, String[] categories) {
        if (name == null)
            throw new IllegalArgumentException("Product name cannot be null");

        if (categories == null)
            throw new IllegalArgumentException("Categories cannot be null");

        if (costPrice <= 0)
            throw new IllegalArgumentException("Cost price must be positive");

        if (sellingPrice <= 0)
            throw new IllegalArgumentException("Selling price must be positive");

        if (discount < 0 || discount > 99)
            throw new IllegalArgumentException("Discount must be between 0 and 99");

        int id = nextProductID++;
        ProductBL product = new ProductBL(id, name, costPrice, sellingPrice, discount, producerID, categories);
        products.put(id, product);
        return id;
    }


    public void removeProduct(int productID) {
        ProductBL product = products.get(productID);
        if (product == null)
            throw new IllegalArgumentException("Product not found");

        items.values().removeIf(item -> item.getProductID() == productID);
        products.remove(productID);
    }


    public void updateProduct(int productID, String name, double costPrice, double sellingPrice, int discount,
                              int producerID, String[] categories) {
        ProductBL product = products.get(productID);
        if (product == null)
            throw new IllegalArgumentException("Product not found");

        if (name == null)
            throw new IllegalArgumentException("Product name cannot be null");

        if (categories == null)
            throw new IllegalArgumentException("Categories cannot be null");

        if (costPrice <= 0)
            throw new IllegalArgumentException("Cost price must be positive");

        if (sellingPrice <= 0)
            throw new IllegalArgumentException("Selling price must be positive");

        if (discount < 0 || discount > 99)
            throw new IllegalArgumentException("Discount must be between 0 and 99");

        product.setName(name);
        product.setSellingPrice(sellingPrice);
        product.setDiscount(discount);
        product.setCategories(categories);
    }

    // ================== Item Management ==================

    public long addItem(int productID, String name, boolean isDef,
                        Date expirationDate, int branchID, String[] location) {
        ProductBL product = products.get(productID);
        if (product == null)
            throw new IllegalArgumentException("Product not found");

        if (expirationDate.before(new Date()))
            throw new IllegalArgumentException("Cannot add item with expired date");

        int itemID = nextItemID++;
        ItemBL item = new ItemBL(itemID, productID, name, isDef, expirationDate, branchID, location);
        product.addItemToBranch(branchID, item);
        items.put(itemID, item);
        return itemID;
    }

    public void removeItem(int itemID) {
        ItemBL item = items.get(itemID);
        if (item == null)
            throw new IllegalArgumentException("Item not found");

        ProductBL product = products.get(item.getProductID());
        if (product == null)
            throw new IllegalArgumentException("Product not found for item");

        product.removeItemFromBranch(item.getBranchID(), itemID);
        items.remove(itemID);
    }

    public void purchaseItem(int itemID) {
        ItemBL item = items.get(itemID);
        if (item == null)
            throw new IllegalArgumentException("Item not found");

        ProductBL product = products.get(item.getProductID());
        if (product == null)
            throw new IllegalArgumentException("Product not found for item");

        product.addProfit(item.getBranchID(), (long) product.getSellingPrice());
        removeItem(itemID);
    }

    public void updateItem(int itemID, String name, boolean isDef,
                           int newBranchID, String[] location) {
        ItemBL item = items.get(itemID);
        if (item == null)
            throw new IllegalArgumentException("Item not found");

        ProductBL product = products.get(item.getProductID());
        if (product == null)
            throw new IllegalArgumentException("Product not found for item");

        int oldBranchID = item.getBranchID();

        if (oldBranchID != newBranchID) {
            product.removeItemFromBranch(oldBranchID, itemID);
            item.setBranchID(newBranchID);
            product.addItemToBranch(newBranchID, item);
        }

        item.setName(name);
        item.setDef(isDef);
        item.setLocation(location);
    }



    // ================== Reports ==================

    public ReportBL deficiencyReport() {
        // TODO: implement deficiencyReport
        return null;
    }

    public ReportBL salesReport() {
        // TODO: implement salesReport
        return null;
    }

    public ReportBL defectedReport() {
        // TODO: implement defectedReport
        return null;
    }

    public ReportBL expiredReport() {
        // TODO: implement expiredReport
        return null;
    }
}
