package DAL;

import java.io.File;
import java.sql.*;

import Utils.Globals;

public class InventoryShelfItemsController {
    private final String tableName = "InventoryShelfItems";
    String currentDir = System.getProperty("user.dir");
    private final String dbPath = (Globals.useFakeData) ? currentDir + File.separator + "FakeData.db" : currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;

    public InventoryShelfItemsController() {
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
                        CREATE TABLE IF NOT EXISTS InventoryShelfItems (
                            productID INTEGER,
                            branchID INTEGER,
                            itemID INTEGER
                        );
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create InventoryShelfItems table: " + e.getMessage());
        }
    }

    public void insertInventoryShelfItem(int productID, int branchID, int itemID) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO " + tableName + " (productID, branchID, itemID) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, productID);
                pstmt.setInt(2, branchID);
                pstmt.setInt(3, itemID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Insert InventoryShelfItem failed: " + e.getMessage());
        }
    }

    public void deleteByProductID(int productID) {
        String sql = "DELETE FROM InventoryShelfItems WHERE productID = ?";
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productID);
            pstmt.executeUpdate();
            System.out.println("InventoryShelfItems deleted for productID: " + productID);
        } catch (SQLException e) {
            System.out
                    .println("Failed to delete InventoryShelfItems for productID " + productID + ": " + e.getMessage());
        }
    }

    public void deleteByItemID(int itemID) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "DELETE FROM " + tableName + " WHERE itemID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, itemID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Delete InventoryShelfItem failed: " + e.getMessage());
        }
    }
}