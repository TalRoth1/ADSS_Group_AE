package DataLayer.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                "id INTEGER PRIMARY KEY, " +
                "originID INT, " +
                "weight FLOAT, " +
                "FOREIGN KEY (originID) REFERENCES locations(id) ON DELETE CASCADE" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error creating documents table: " + e.getMessage());
            throw e;
        }
    }

    public void createDocument(int docId, int originId, float weight) throws SQLException {
        String sql = "INSERT INTO documents (id, originID, weight) VALUES (?, ? ,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, docId);
            preparedStatement.setInt(2, originId);
            preparedStatement.setFloat(3, weight);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating document: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet getDocument(int documentId) throws SQLException {
        String sql = "SELECT * FROM documents WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, documentId);
        return pstmt.executeQuery();
    }

    public void updateDocument(int documentId, int originId, float weight) {
        String sql = "UPDATE documents SET originID = ?, weight = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, originId);
            pstmt.setFloat(2, weight);
            pstmt.setInt(3, documentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating document: " + e.getMessage());
        }
    }

    public void updateDocumentByField(String fieldName, Object value, int documentId) {
        String sql = "UPDATE documents SET " + fieldName + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, value);
            pstmt.setInt(2, documentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating document by field: " + e.getMessage());
        }
    }

    public void deleteDocument(int documentId) {
        String sql = "DELETE FROM documents WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, documentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting document: " + e.getMessage());
        }
    }

    public ResultSet getAllDocuments() throws SQLException {
        String sql = "SELECT * FROM documents";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public void clearTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM documents"); // clear rows
            // stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='documents'"); //
            // reset AUTOINCREMENT
        } catch (SQLException e) {
            System.out.println("Error clearing documents table: " + e.getMessage());
            throw e;
        }
    }
}