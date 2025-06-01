
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import DAL.ItemDAO;

public class ItemController {
    private final String tableName = "Items";
    String currentDir = System.getProperty("user.dir");
    String dbPath = currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;
    InventoryShelfItemsController inventoryController = new InventoryShelfItemsController();
    MinQuantitiesController minQuantitiesController = new MinQuantitiesController();
    ProfitAmountsController profitAmountsController = new ProfitAmountsController();

    public ItemController() {
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
                        CREATE TABLE IF NOT EXISTS Items (
                            itemID INTEGER PRIMARY KEY,
                            productID INTEGER,
                            name TEXT NOT NULL,
                            isDef BOOLEAN,
                            isExpired BOOLEAN,
                            expirationDate DATE,
                            branchID INTEGER,
                            buyingPrice INTEGER,
                            location TEXT
                        );
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create Items table: " + e.getMessage());
        }
    }

    public void insert(ItemDAO item) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO " + tableName
                    + " (itemID, productID, name, isDef, isExpired, expirationDate, branchID, buyingPrice, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, item.getItemID());
                pstmt.setInt(2, item.getProductID());
                pstmt.setString(3, item.getName());
                pstmt.setBoolean(4, item.isDef());
                pstmt.setBoolean(5, item.isExpired());
                pstmt.setDate(6, new java.sql.Date(item.getExpirationDate().getTime()));
                pstmt.setInt(7, item.getBranchID());
                pstmt.setInt(8, item.getBuyingPrice());
                pstmt.setString(9, String.join(",", item.getLocation()));
                pstmt.executeUpdate();
                item.persist();

                inventoryController.insertInventoryShelfItem(item.getProductID(), item.getBranchID(), item.getItemID());
                minQuantitiesController.ensureMinQuantityExists(item.getProductID(), item.getBranchID());
                profitAmountsController.ensureProfitAmountExists(item.getProductID(), item.getBranchID());

                System.out.println("Item inserted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    public void update(ItemDAO item) {
        if (!item.isPersisted())
            return;

        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "UPDATE " + tableName
                    + " SET name = ?, isDef = ?, isExpired = ?, expirationDate = ?, branchID = ?, location = ? WHERE itemID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, item.getName());
                pstmt.setBoolean(2, item.isDef());
                pstmt.setBoolean(3, item.isExpired());
                pstmt.setDate(4, new java.sql.Date(item.getExpirationDate().getTime()));
                pstmt.setInt(5, item.getBranchID());
                pstmt.setString(6, String.join(",", item.getLocation()));
                pstmt.setInt(7, item.getItemID());
                pstmt.executeUpdate();

                inventoryController.deleteByItemID(item.getItemID());
                inventoryController.insertInventoryShelfItem(item.getProductID(), item.getBranchID(), item.getItemID());
                minQuantitiesController.ensureMinQuantityExists(item.getProductID(), item.getBranchID());
                profitAmountsController.ensureProfitAmountExists(item.getProductID(), item.getBranchID());

                System.out.println("Item updated successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    public void delete(int itemID) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "DELETE FROM " + tableName + " WHERE itemID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, itemID);
                pstmt.executeUpdate();
                inventoryController.deleteByItemID(itemID);
                System.out.println("Item deleted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    public ItemDAO getItem(int itemID) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM " + tableName + " WHERE itemID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, itemID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    ItemDAO dao = new ItemDAO(
                            rs.getInt("itemID"),
                            rs.getInt("productID"),
                            rs.getString("name"),
                            rs.getBoolean("isDef"),
                            rs.getBoolean("isExpired"),
                            rs.getDate("expirationDate"),
                            rs.getInt("branchID"),
                            rs.getInt("buyingPrice"),
                            rs.getString("location").split(","));
                    dao.persist();
                    return dao;
                }
            }
        } catch (SQLException e) {
            System.out.println("Get item failed: " + e.getMessage());
        }
        return null;
    }

    public List<ItemDAO> getAllItems() {
        List<ItemDAO> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM " + tableName;
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ItemDAO dao = new ItemDAO(
                            rs.getInt("itemID"),
                            rs.getInt("productID"),
                            rs.getString("name"),
                            rs.getBoolean("isDef"),
                            rs.getBoolean("isExpired"),
                            rs.getDate("expirationDate"),
                            rs.getInt("branchID"),
                            rs.getInt("buyingPrice"),
                            rs.getString("location").split(","));
                    dao.persist();
                    items.add(dao);
                }
            }
        } catch (SQLException e) {
            System.out.println("Get all items failed: " + e.getMessage());
        }
        return items;
    }
}
