package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ShipmentDocDAO {
    private Connection connection;

    public ShipmentDocDAO(Connection connection) {
        this.connection = connection;
        try {
            initializeTable();
        } catch (SQLException e) {
            System.out.println("Error initializing items table: " + e.getMessage());
        }
    }

    private void initializeTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS documents (" +
                "shipmentID INT NOT NULL FOREIGN KEY REFERENCES shipments(id), " +
                "documentID INT NOT NULL PRIMARY KEY, " +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating documents table: " + e.getMessage());
            throw e;
        }
    }

    public void addDocument(int shipmentID, int documentID) throws SQLException {
        String insertSQL = "INSERT INTO documents (shipmentID, documentID) VALUES (?, ?)";
        try (var preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, shipmentID);
            preparedStatement.setInt(2, documentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding document: " + e.getMessage());
            throw e;
        }
    }

    public ArrayList<Integer> getDocumentsByShipmentID(int shipmentID) throws SQLException {
        ArrayList<Integer> documentIDs = new ArrayList<>();
        String selectSQL = "SELECT documentID FROM documents WHERE shipmentID = ?";
        try (var preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, shipmentID);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                documentIDs.add(resultSet.getInt("documentID"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving documents: " + e.getMessage());
            throw e;
        }
        return documentIDs;
    }

    public void deleteAllDocument(int shipmentID) throws SQLException {
        String deleteSQL = "DELETE FROM documents WHERE shipmentID = ?";
        try (var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, shipmentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting document: " + e.getMessage());
            throw e;
        }
    }

    public void deleteDocument(int shipmentID, int documentID) throws SQLException {
        String deleteSQL = "DELETE FROM documents WHERE shipmentID = ? AND documentID = ?";
        try (var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, shipmentID);
            preparedStatement.setInt(2, documentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting document: " + e.getMessage());
            throw e;
        }
    }
}
