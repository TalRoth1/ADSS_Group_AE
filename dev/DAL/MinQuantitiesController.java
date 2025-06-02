package DAL;

import java.io.File;
import java.sql.*;

public class MinQuantitiesController {
    private final String tableName = "MinQuantities";
    String currentDir = System.getProperty("user.dir");
    String dbPath = currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;

    public MinQuantitiesController() {
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
                        CREATE TABLE IF NOT EXISTS MinQuantities (
                            productID INTEGER,
                            branchID INTEGER,
                            minQuantity INTEGER
                        );
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create MinQuantities table: " + e.getMessage());
        }
    }

    public void insert(int productID, int branchID, int minQuantity) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO " + tableName + " (productID, branchID, minQuantity) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, productID);
                pstmt.setInt(2, branchID);
                pstmt.setInt(3, minQuantity);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Insert to MinQuantities failed: " + e.getMessage());
        }
    }

    public void ensureMinQuantityExists(int productID, int branchID) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE productID = ? AND branchID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, productID);
                pstmt.setInt(2, branchID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertSql = "INSERT INTO " + tableName
                            + " (productID, branchID, minQuantity) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, productID);
                        insertStmt.setInt(2, branchID);
                        insertStmt.setInt(3, 0);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("ensureMinQuantityExists failed: " + e.getMessage());
        }
    }

    public void updateMinQuantity(int productID, int branchID, int newMin) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "UPDATE " + tableName + " SET minQuantity = ? WHERE productID = ? AND branchID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, newMin);
                pstmt.setInt(2, productID);
                pstmt.setInt(3, branchID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("updateMinQuantity failed: " + e.getMessage());
        }
    }

    public void deleteByProductID(int productID) {
        String sql = "DELETE FROM MinQuantities WHERE productID = ?";
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productID);
            pstmt.executeUpdate();
            System.out.println("MinQuantities deleted for productID: " + productID);
        } catch (SQLException e) {
            System.out.println("Failed to delete MinQuantities for productID " + productID + ": " + e.getMessage());
        }
    }

}