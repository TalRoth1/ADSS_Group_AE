package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DocumentItemDAO {
    private Connection connection;

    public DocumentItemDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing items table: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS document_item (" +
                "documentID INT NOT NULL, " +
                "locationID INT NOT NULL, " +
                "itemName TEXT NOT NULL, " +
                "amount INT NOT NULL, " +
                "PRIMARY KEY (documentID, locationID, itemName), " +
                "FOREIGN KEY (documentID) REFERENCES documents(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (itemName) REFERENCES items(name) ON DELETE CASCADE" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating document_item table: " + e.getMessage());
            throw e;
        }
    }

    public void addDocumentItem(int documentID, int locationID, String itemName, int amount) throws SQLException {
        String insertSQL = "INSERT INTO document_item (documentID, locationID, itemName, amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, documentID);
            preparedStatement.setInt(2, locationID);
            preparedStatement.setString(3, itemName);
            preparedStatement.setInt(4, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding document item: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getDocumentItems(int documentID) throws SQLException {
        String selectSQL = "SELECT * FROM document_item WHERE documentID = ?";
        var preparedStatement = connection.prepareStatement(selectSQL);
        preparedStatement.setInt(1, documentID);
        return preparedStatement.executeQuery();
    }

    public void deleteDocumentItem(int documentID, int locationID, String itemName) throws SQLException {
        String deleteSQL = "DELETE FROM document_item WHERE documentID = ? AND locationID = ? AND itemName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, documentID);
            preparedStatement.setInt(2, locationID);
            preparedStatement.setString(3, itemName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting document item: " + e.getMessage());
            throw e;
        }
    }

    public void updateDocumentItem(int documentID, int locationID, String itemName, int newAmount) throws SQLException {
        String updateSQL = "UPDATE document_item SET amount = ? WHERE documentID = ? AND locationID = ? AND itemName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, newAmount);
            preparedStatement.setInt(2, documentID);
            preparedStatement.setInt(3, locationID);
            preparedStatement.setString(4, itemName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating document item: " + e.getMessage());
            throw e;
        }
    }

    public void deleteAllDocumentItems(int documentID) throws SQLException {
        String deleteSQL = "DELETE FROM document_item WHERE documentID = ?";
        try (var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, documentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting all document items: " + e.getMessage());
            throw e;
        }
    }

    public void deleteDocumentLocation(int documentID, int locationID) throws SQLException {
        String deleteSQL = "DELETE FROM document_item WHERE documentID = ? AND locationID = ?";
        try (var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, documentID);
            preparedStatement.setInt(2, locationID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting document location: " + e.getMessage());
            throw e;
        }
    }

    public void clearTable() throws SQLException {
        String sql = "DELETE FROM document_item";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
