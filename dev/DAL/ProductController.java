package DAL;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Utils.Globals;

public class ProductController {
    private final String tableName = "Products";
    String currentDir = System.getProperty("user.dir");
    private final String dbPath = (Globals.useFakeData) ? currentDir + File.separator + "FakeData.db" : currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;
    InventoryShelfItemsController inventoryShelfItemsController = new InventoryShelfItemsController();
    MinQuantitiesController minQuantitiesController = new MinQuantitiesController();
    ProfitAmountsController profitAmountsController = new ProfitAmountsController();

    public ProductController() {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeTable();
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    private void initializeTable() {
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            String sql = """
                        CREATE TABLE IF NOT EXISTS Products (
                            productID INTEGER PRIMARY KEY,
                            name  TEXT NOT NULL,
                            sellingPrice REAL NOT NULL,
                            discount INTEGER,
                            producerID INTEGER,
                            categories TEXT
                        );
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create Products table: " + e.getMessage());
        }
    }

    public void insert(ProductDAO product) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO " + tableName
                    + " (productID, name, sellingPrice, discount, producerID, categories) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, product.getProductID());
                pstmt.setString(2, product.getName());
                pstmt.setDouble(3, product.getSellingPrice());
                pstmt.setInt(4, product.getDiscount());
                pstmt.setInt(5, product.getProducerID());
                pstmt.setString(6, String.join(",", product.getCategories()));

                pstmt.executeUpdate();

                // for (Map.Entry<Integer, List<Integer>> entry :
                // product.getInventoryShelfItems().entrySet()) {
                // int branchID = entry.getKey();
                // int quantity = entry.getValue().size();
                // inventoryShelfItemsController.insertInventoryShelfItem(product.getProductID(),
                // branchID, quantity);
                // }

                // for (Map.Entry<Integer, Integer> entry : product.getMinQuantity().entrySet())
                // {
                // minQuantitiesController.insert(product.getProductID(), entry.getKey(),
                // entry.getValue());
                // }

                // for (Map.Entry<Integer, Integer> entry :
                // product.getProfitAmount().entrySet()) {
                // profitAmountsController.insert(product.getProductID(), entry.getKey(),
                // entry.getValue());
                // }

                product.persist();
                System.out.println("Product inserted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    public void update(ProductDAO product) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "UPDATE " + tableName
                    + " SET name = ?, sellingPrice = ?, discount = ?, producerID = ?, categories = ? WHERE productID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, product.getName());
                pstmt.setDouble(2, product.getSellingPrice());
                pstmt.setInt(3, product.getDiscount());
                pstmt.setInt(4, product.getProducerID());
                pstmt.setString(5, String.join(",", product.getCategories()));
                pstmt.setInt(6, product.getProductID());
                pstmt.executeUpdate();

                // inventoryShelfItemsController.deleteByProductID(product.getProductID());
                // minQuantitiesController.deleteByProductID(product.getProductID());
                // profitAmountsController.deleteByProductID(product.getProductID());

                // for (Map.Entry<Integer, List<Integer>> entry :
                // product.getInventoryShelfItems().entrySet()) {
                // int branchID = entry.getKey();
                // int quantity = entry.getValue().size();
                // inventoryShelfItemsController.insert(product.getProductID(), branchID,
                // quantity);
                // }

                // for (Map.Entry<Integer, Integer> entry : product.getMinQuantity().entrySet())
                // {
                // minQuantitiesController.insert(product.getProductID(), entry.getKey(),
                // entry.getValue());
                // }

                // for (Map.Entry<Integer, Integer> entry :
                // product.getProfitAmount().entrySet()) {
                // profitAmountsController.insert(product.getProductID(), entry.getKey(),
                // entry.getValue());
                // }

                System.out.println("Product updated successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    public void delete(int productID) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "DELETE FROM " + tableName + " WHERE productID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, productID);
                pstmt.executeUpdate();

                inventoryShelfItemsController.deleteByProductID(productID);
                minQuantitiesController.deleteByProductID(productID);
                profitAmountsController.deleteByProductID(productID);

                System.out.println("Product deleted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    public ProductDAO getProduct(int id) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM " + tableName + " WHERE productID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    ProductDAO dao = new ProductDAO(
                            rs.getInt("productID"),
                            rs.getString("name"),
                            rs.getDouble("sellingPrice"),
                            rs.getInt("discount"),
                            rs.getInt("producerID"),
                            rs.getString("categories").split(","));
                    dao.persist();
                    return dao;
                }
            }
        } catch (SQLException e) {
            System.out.println("Get product failed: " + e.getMessage());
        }
        return null;
    }

    public List<ProductDAO> getAllProducts() {
        List<ProductDAO> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM " + tableName;
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ProductDAO dao = new ProductDAO(
                            rs.getInt("productID"),
                            rs.getString("name"),
                            rs.getDouble("sellingPrice"),
                            rs.getInt("discount"),
                            rs.getInt("producerID"),
                            rs.getString("categories").split(","));
                    dao.persist();
                    products.add(dao);
                }
            }
        } catch (SQLException e) {
            System.out.println("Get all products failed: " + e.getMessage());
        }
        return products;
    }

    public int getMaxProductID() {
        int maxID = 0;
        String sql = "SELECT MAX(productID) as maxID FROM Products";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                maxID = rs.getInt("maxID");
            }
        } catch (SQLException e) {
            System.out.println("getMaxProductID failed: " + e.getMessage());
        }

        return maxID;
    }

}
