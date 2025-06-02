package DAL;

import java.io.File;
import java.sql.*;

public class ProfitAmountsController {
    private final String tableName = "ProfitAmounts";
    String currentDir = System.getProperty("user.dir");
    String dbPath = currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;

    public ProfitAmountsController() {
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
                        CREATE TABLE IF NOT EXISTS ProfitAmounts (
                            productID INTEGER,
                            branchID INTEGER,
                            profitAmount INTEGER
                        );
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create ProfitAmounts table: " + e.getMessage());
        }
    }

    public void ensureProfitAmountExists(int productID, int branchID) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE productID = ? AND branchID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, productID);
                pstmt.setInt(2, branchID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertSql = "INSERT INTO " + tableName
                            + " (productID, branchID, profitAmount) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, productID);
                        insertStmt.setInt(2, branchID);
                        insertStmt.setInt(3, 0);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("ensureProfitAmountExists failed: " + e.getMessage());
        }
    }

    public void insert(int productID, int branchID, int profitAmount) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO " + tableName + " (productID, branchID, profitAmount) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, productID);
                pstmt.setInt(2, branchID);
                pstmt.setInt(3, profitAmount);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Insert to ProfitAmounts failed: " + e.getMessage());
        }
    }

    public void addProfit(int productID, int branchID, double amountToAdd) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String updateSql = "UPDATE " + tableName
                    + " SET profitAmount = profitAmount + ? WHERE productID = ? AND branchID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setDouble(1, amountToAdd);
                pstmt.setInt(2, productID);
                pstmt.setInt(3, branchID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("addProfit failed: " + e.getMessage());
        }
    }

    public void deleteByProductID(int productID) {
        String sql = "DELETE FROM ProfitAmounts WHERE productID = ?";
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productID);
            pstmt.executeUpdate();
            System.out.println("ProfitAmounts deleted for productID: " + productID);
        } catch (SQLException e) {
            System.out.println("Failed to delete ProfitAmounts for productID " + productID + ": " + e.getMessage());
        }
    }

}
