package DataLayer.DAOs;

import java.sql.Connection;
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
                "itemID INT FOREIGN KEY REFERENCES items(id), " +
                "amount INT NOT NULL, " +
                "PRIMARY KEY (documentID, locationID, itemID)" +
                "FOREIGN KEY (documentID) REFERENCES documents(id)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating documents table: " + e.getMessage());
            throw e;
        }
    }

    public void addDocumentItem(int documentID, int locationID, int itemID, int amount) throws SQLException {
        String insertSQL = "INSERT INTO document_item (documentID, locationID, itemID, amount) VALUES (?, ?, ?, ?)";
        try (var preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, documentID);
            preparedStatement.setInt(2, locationID);
            preparedStatement.setInt(3, itemID);
            preparedStatement.setInt(4, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding document item: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getDocumentItems(int documentID) throws SQLException {
        String selectSQL = "SELECT * FROM document_item WHERE documentID = ?";
        try (var preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, documentID);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving document items: " + e.getMessage());
            throw e;
        }
    }

    public void deleteDocumentItem(int documentID, int locationID, int itemID) throws SQLException {
        String deleteSQL = "DELETE FROM document_item WHERE documentID = ? AND locationID = ? AND itemID = ?";
        try (var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, documentID);
            preparedStatement.setInt(2, locationID);
            preparedStatement.setInt(3, itemID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting document item: " + e.getMessage());
            throw e;
        }
    }

    public void updateDocumentItem(int documentID,int locationID, int itemID, int newAmount) throws SQLException {
        String updateSQL = "UPDATE document_item SET amount = ? WHERE documentID = ? AND locationID = ? AND itemID = ?";
        try (var preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, newAmount);
            preparedStatement.setInt(2, documentID);
            preparedStatement.setInt(3, locationID);
            preparedStatement.setInt(4, itemID);
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
}
