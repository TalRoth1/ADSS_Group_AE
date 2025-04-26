package BusinessLayer;

import java.util.*;

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

    public void deleteAllItemsForBranch(int branchID)
    {
        items.values().removeIf(item -> item.getBranchID() == branchID);

        for (ProductBL product : products.values())
        {
            if (product.hasBranch(branchID))
            {
                product.getItemsInBranch(branchID).clear();
                product.getProfits().remove(branchID);
            }
        }
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

    public String getListOfAllProducts() {
        StringBuilder sb = new StringBuilder();
        synchronized (products) {
            if (products.isEmpty()) {
                throw new IllegalArgumentException("No products found");
            }
            for (Map.Entry<Integer, ProductBL> entry : products.entrySet()) {
                int id = entry.getKey();
                ProductBL product = entry.getValue();
                sb.append("Product ID: ").append(id)
                        .append(", Name: ").append(product.getName())
                        .append(", Selling Price: ").append(product.getSellingPrice())
                        .append(", Discount: ").append(product.getDiscount()).append("%")
                        .append("\n");
            }
        }
        return sb.toString();
    }

    public void setMinQuantity(int productID, int branchID, int minQuantity) {
        ProductBL product = products.get(productID);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        product.setMinQuantity(branchID, minQuantity);
    }

    // ================== Item Management ==================

    public int addItem(int productID, String name, boolean isDef,
            Date expirationDate, int branchID, String[] location) {
        ProductBL product = products.get(productID);
        if (product == null)
            throw new IllegalArgumentException("Product not found");

        if (!BranchFacade.getInstance().isBranchExists(branchID)) {
            throw new IllegalArgumentException("Cannot add item to non-existent branch.");
        }

        product.initializeBranch(branchID);
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

        product.addProfit(item.getBranchID(), product.getSellingPrice());
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

    public String getListOfAllItems() {
        StringBuilder sb = new StringBuilder();
        synchronized (items) {
            if (items.isEmpty()) {
                throw new IllegalArgumentException("No items found");
            }
            for (Map.Entry<Integer, ItemBL> entry : items.entrySet()) {
                int id = entry.getKey();
                ItemBL item = entry.getValue();
                sb.append("Item ID: ").append(id)
                        .append(", Name: ").append(item.getName())
                        .append(", Product ID: ").append(item.getProductID())
                        .append(", Branch ID: ").append(item.getBranchID())
                        .append(", Defective: ").append(item.isDef())
                        .append(", Expired: ").append(item.isExpired())
                        .append("\n");
            }
        }
        return sb.toString();
    }

    // ================== Reports ==================

    public ReportBL deficiencyReport() {
        StringBuilder body = new StringBuilder();
        body.append("Deficiency Report\n");
        body.append("==================\n");

        List<Integer> branchIDs = new ArrayList<>(BranchFacade.getInstance().getAllBranchIDs());
        Collections.sort(branchIDs);

        for (Integer branchID : branchIDs) {
            String branchName = BranchFacade.getInstance().getBranchName(branchID);
            boolean hasDeficiency = false;

            for (ProductBL product : products.values()) {
                Integer minimalQuantityObj = product.getMinQuantity(branchID);
                if (minimalQuantityObj == null) {
                    // No minimal quantity defined for this branch and product
                    continue;
                }

                int minimalQuantity = minimalQuantityObj; // safe, because minimalQuantityObj is not null
                int currentQuantity = product.getInventoryQuantity(branchID);

                if (currentQuantity <= minimalQuantity) {
                    if (!hasDeficiency) {
                        body.append("Branch: ").append(branchName)
                                .append(" (ID: ").append(branchID).append(")\n");
                        hasDeficiency = true;
                    }
                    body.append("  Product: ").append(product.getName())
                            .append(" (Product ID: ").append(product.getProductID()).append(") ")
                            .append("Current Quantity: ").append(currentQuantity)
                            .append(", Minimal Required: ").append(minimalQuantity)
                            .append("\n");
                }
            }

            if (!hasDeficiency) {
                body.append("Branch: ").append(branchName)
                        .append(" (ID: ").append(branchID)
                        .append(") - No deficiencies.\n");
            }
        }

        return new ReportBL("Deficiency Report", body.toString());
    }

    public ReportBL expiredReport() {
        StringBuilder body = new StringBuilder();
        body.append("Expired Items Report\n");
        body.append("=====================\n");

        Map<Integer, List<ItemBL>> branchToExpiredItems = new HashMap<>();

        for (ItemBL item : items.values()) {
            if (item.checkIfExpired()) {
                branchToExpiredItems.computeIfAbsent(item.getBranchID(), k -> new ArrayList<>()).add(item);
            }
        }

        List<Integer> branchIDs = new ArrayList<>(branchToExpiredItems.keySet());
        Collections.sort(branchIDs);

        if (branchIDs.isEmpty()) {
            body.append("No expired items found.\n");
        } else {
            for (Integer branchID : branchIDs) {
                String branchName = BranchFacade.getInstance().getBranchName(branchID);
                body.append("Branch: ").append(branchName).append(" (ID: ").append(branchID).append(")\n");

                for (ItemBL item : branchToExpiredItems.get(branchID)) {
                    String productName = products.get(item.getProductID()).getName();
                    body.append("  Item ID: ").append(item.getItemID()).append(", Product: ").append(productName)
                            .append(", Expiration Date: ").append(item.getExpirationDate()).append(", Location: ")
                            .append(item.getLocation()).append("\n");
                }
            }
        }

        return new ReportBL("Expired Items Report", body.toString());
    }

    public ReportBL defectedReport() {
        StringBuilder body = new StringBuilder();
        body.append("Defected Items Report\n");
        body.append("======================\n");

        Map<Integer, List<ItemBL>> branchToDefectedItems = new HashMap<>();

        for (ItemBL item : items.values()) {
            if (item.isDef()) {
                branchToDefectedItems.computeIfAbsent(item.getBranchID(), k -> new ArrayList<>()).add(item);
            }
        }

        List<Integer> branchIDs = new ArrayList<>(branchToDefectedItems.keySet());
        Collections.sort(branchIDs);

        if (branchIDs.isEmpty()) {
            body.append("No defected items found.\n");
        } else {
            for (Integer branchID : branchIDs) {
                String branchName = BranchFacade.getInstance().getBranchName(branchID);
                body.append("Branch: ").append(branchName).append(" (ID: ").append(branchID).append(")\n");

                for (ItemBL item : branchToDefectedItems.get(branchID)) {
                    String productName = products.get(item.getProductID()).getName();
                    body.append("  Item ID: ").append(item.getItemID()).append(", Product: ").append(productName)
                            .append(", Location: ").append(item.getLocation()).append("\n");
                }
            }
        }

        return new ReportBL("Defected Items Report", body.toString());
    }

    public ReportBL salesReport() {
        StringBuilder body = new StringBuilder();
        body.append("Sales Report\n");
        body.append("============\n");

        Map<Integer, Map<String, Integer>> branchToProductProfits = new HashMap<>();

        for (ProductBL product : products.values()) {
            Map<Integer, Integer> productProfits = product.getProfits();

            for (Map.Entry<Integer, Integer> entry : productProfits.entrySet()) {
                int branchID = entry.getKey();
                int profit = entry.getValue();
                String productName = product.getName();

                branchToProductProfits.computeIfAbsent(branchID, k -> new HashMap<>()).merge(productName, profit,
                        Integer::sum);
            }
        }

        List<Integer> branchIDs = new ArrayList<>(branchToProductProfits.keySet());
        Collections.sort(branchIDs);

        if (branchIDs.isEmpty()) {
            body.append("No sales recorded.\n");
        } else {
            for (Integer branchID : branchIDs) {
                String branchName = BranchFacade.getInstance().getBranchName(branchID);
                body.append("Branch: ").append(branchName).append(" (ID: ").append(branchID).append(")\n");

                Map<String, Integer> productProfits = branchToProductProfits.get(branchID);
                List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productProfits.entrySet());

                sortedProducts.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

                for (Map.Entry<String, Integer> productEntry : sortedProducts) {
                    body.append("  Product: ").append(productEntry.getKey()).append(", Total Profit: ")
                            .append(productEntry.getValue()).append("\n");
                }
            }
        }

        return new ReportBL("Sales Report", body.toString());
    }

}
