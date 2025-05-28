package DataLayer.DAOs;

import java.sql.Connection;
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
        String createTableSQL = "CREATE TABLE IF NOT EXISTS DocumentItem (" +
                "documentID INT AUTO_INCREMENT PRIMARY KEY, " +
                "itemID INT FOREIGN KEY REFERENCES locations(id), " +
                "amount INT NOT NULL, " +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating documents table: " + e.getMessage());
            throw e;
        }
    }

    public void addDocumentItem(int documentID, int itemID, int amount) throws SQLException {
        String insertSQL = "INSERT INTO documents (documentID, itemID, amount) VALUES (?, ?, ?)";
        try (var preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, documentID);
            preparedStatement.setInt(2, itemID);
            preparedStatement.setInt(3, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding document item: " + e.getMessage());
            throw e;
        }
    }

    public ArrayList<String> getDocumentItems(int documentID) throws SQLException {
        ArrayList<String> items = new ArrayList<>();
        String selectSQL = "SELECT itemID, amount FROM documents WHERE documentID = ?";
        try (var preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, documentID);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int itemID = resultSet.getInt("itemID");
                int amount = resultSet.getInt("amount");
                items.add("Item ID: " + itemID + ", Amount: " + amount);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving document items: " + e.getMessage());
            throw e;
        }
        return items;
    }

    public void deleteDocumentItem(int documentID, int itemID) throws SQLException {
        String deleteSQL = "DELETE FROM documents WHERE documentID = ? AND itemID = ?";
        try (var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, documentID);
            preparedStatement.setInt(2, itemID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting document item: " + e.getMessage());
            throw e;
        }
    }

    public void updateDocumentItem(int documentID, int itemID, int newAmount) throws SQLException {
        String updateSQL = "UPDATE documents SET amount = ? WHERE documentID = ? AND itemID = ?";
        try (var preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, newAmount);
            preparedStatement.setInt(2, documentID);
            preparedStatement.setInt(3, itemID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating document item: " + e.getMessage());
            throw e;
        }
    }

    public void deleteAllDocumentItems(int documentID) throws SQLException {
        String deleteSQL = "DELETE FROM documents WHERE documentID = ?";
        try (var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, documentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting all document items: " + e.getMessage());
            throw e;
        }
    }
}
