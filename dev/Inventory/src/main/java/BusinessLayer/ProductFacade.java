package BusinessLayer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductFacade {
    private static ProductFacade instance = null;
    private Map<Integer, ProductBL> products;
    private int nextProductID;

    private ProductFacade() {
        this.products = new HashMap<>();
        this.nextProductID = 1;
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
        if (!products.containsKey(productID))
            throw new IllegalArgumentException("Product not found");

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


    // ================================
    // Items & Reports
    // ================================

    public long addItem(int productID, int itemID, String name, boolean isDef,
                        Date expirationDate, int branchID, String[] location) {
        // TODO: implement addItem
        return 0;
    }

    public void removeItem(int itemID) {
        // TODO: implement removeItem
    }

    public void purchaseItem(int itemID) {
        // TODO: implement purchaseItem
    }

    public void updateItem(int itemID, String name, boolean isDef,
                           long branchID, String[] location) {
        // TODO: implement updateItem
    }

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
