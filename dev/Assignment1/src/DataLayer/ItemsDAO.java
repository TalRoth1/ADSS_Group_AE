package DataLayer;
import java.sql.*;

public class ItemsDAO {
    private Connection connection;

    public ItemsDAO(Connection connection) {
        this.connection = connection;
    }


    public void addItem(int id, String name, int quantity, double weight) throws SQLException {
        String sql = "INSERT INTO items (id ,name, quantity, weight) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, weight);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding item: " + e.getMessage());
            throw e; 
        }
    }
    public void updateItem(int id, String name, int quantity, double weight) throws SQLException {
        String sql = "UPDATE items SET name = ?, quantity = ?, weight = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, weight);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating item: " + e.getMessage());
            throw e; 
        }
    }

    public void updateItem(String name, int quantity, double weight) throws SQLException {
        String sql = "UPDATE items SET name = ?, quantity = ?, weight = ? ? WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, weight);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating item: " + e.getMessage());
            throw e; 
        }
    }

    public void updateItemField(int id, String fieldName, String newValue) throws SQLException {
        String sql = "UPDATE items SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating item field: " + e.getMessage());
            throw e; 
        }
    }

    public void updateItemField(int id, String fieldName, int newValue) throws SQLException {
        String sql = "UPDATE items SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating item field: " + e.getMessage());
            throw e; 
        }
    }


    public void deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting item: " + e.getMessage());
            throw e; 
        }
    }
    public ResultSet getItem(int id) throws SQLException {
        String sql = "SELECT * FROM items WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt.executeQuery();
    }
    public ResultSet getAllItems() throws SQLException {
        String sql = "SELECT * FROM items";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }
}