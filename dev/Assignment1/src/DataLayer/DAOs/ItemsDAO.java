package DataLayer.DAOs;

import java.sql.*;

import javax.naming.spi.DirStateFactory.Result;

public class ItemsDAO {
    private Connection connection;

    public ItemsDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing items table: " + e.getMessage());
        }
    }

    public void initializeTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS items (" +
                "name TEXT PRIMARY KEY, " +
                "weight REAL NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addItem(String name, double weight) throws SQLException {
        String sql = "INSERT INTO items (name, weight) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, weight);
            pstmt.executeUpdate();
        }
    }

    public void updateItem(String name, double weight) throws SQLException {
        String sql = "UPDATE items SET weight = ? WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, weight);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        }
    }

    public void deleteItem(String name) throws SQLException {
        String sql = "DELETE FROM items WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    public ResultSet getItem(String name) throws SQLException {
        String sql = "SELECT * FROM items WHERE name = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, name);
        return pstmt.executeQuery();
    }

    public ResultSet getAllItems() throws SQLException {
        String sql = "SELECT * FROM items";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public void clearTable() throws SQLException {
        String sql = "DELETE FROM items";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error clearing items table: " + e.getMessage());
            throw e;
        }
    }
}