package DAL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SupplierController
{
    private String tableName = "Suppliers";
    String currentDir = System.getProperty("user.dir");
    String dbPath = currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;

    public void insert(SupplierDAO supplier)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "INSERT INTO " + tableName + " (supplierId) VALUES (?)";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, "" + supplier.getId());
                    pstmt.executeUpdate();
                    System.out.println("Supplier inserted successfully.");
                } catch (SQLException e) {
                    System.out.println("Insert failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Update(int id, String column, String value)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "UPDATE " + tableName + " SET " + column + " = ? WHERE supplierId = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, value);
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                    System.out.println("Supplier updated successfully.");
                } catch (SQLException e) {
                    System.out.println("Update failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}