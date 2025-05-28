package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DocumentDAO {
    private Connection connection;

    public DocumentDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing items table: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS documents (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "originID INT FOREIGN KEY REFERENCES locations(id), " +
                "destinationID INT FOREIGN KEY REFERENCES locations(id), " +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating documents table: " + e.getMessage());
            throw e;
        }
    }

    public void createDocument(int docId, int originId, int destId) throws SQLException {
        String insertSQL = "INSERT INTO documents (id, originID, destinationID) VALUES (" + docId + ", " + originId
                + ", " + destId + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(insertSQL);
        }

    }

    public ArrayList<String> getDocument(int documentId) {
        String sql = "SELECT * FROM documents WHERE id = " + documentId;
        try (Statement stmt = connection.createStatement()) {
            var resultSet = stmt.executeQuery(sql);
            ArrayList<String> documentDetails = new ArrayList<>();
            while (resultSet.next()) {
                documentDetails.add("ID: " + resultSet.getInt("id"));
                documentDetails.add("Origin ID: " + resultSet.getInt("originID"));
                documentDetails.add("Destination ID: " + resultSet.getInt("destinationID"));
            }
            return documentDetails;
        } catch (SQLException e) {
            System.out.println("Error retrieving document: " + e.getMessage());
            return null;
        }

    }

    public void updateDocument(int documentId, String field, int newValue) {
        String sql = "UPDATE documents SET " + field + " = " + newValue + " WHERE id = " + documentId;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error updating document: " + e.getMessage());
        }
    }

    public void deleteDocument(int documentId) {
        String sql = "DELETE FROM documents WHERE id = " + documentId;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error deleting document: " + e.getMessage());
        }
    }
}