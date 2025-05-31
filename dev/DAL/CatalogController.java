package DAL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogController
{
    private String tableName = "Catalogs";
    String currentDir = System.getProperty("user.dir");
    String dbPath = currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;

    public CatalogController() 
    {
        // Ensure the database connection is established
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    public void insert(CatalogDAO catalog)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "INSERT INTO " + tableName + " (supplierID, contractID, productID, catalogID) VALUES (?, ?, ?, ?)";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, catalog.getSupplierID());
                    pstmt.setInt(2, catalog.getContractID());
                    pstmt.setInt(3, catalog.getProductID());
                    pstmt.setInt(4, catalog.getCatalogID());
                    pstmt.executeUpdate();
                    System.out.println("Catalog inserted successfully.");
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

    public void update(int supplierID, int contractID, int productID, String column, String value)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "UPDATE " + tableName + " SET " + column + " = ? WHERE supplierID = ? AND contractID = ? AND productID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, value);
                    pstmt.setInt(2, supplierID);
                    pstmt.setInt(3, contractID);
                    pstmt.setInt(4, productID);
                    pstmt.executeUpdate();
                    System.out.println("Catalog updated successfully.");
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

    public void delete(int supllierId, int contractId, int productID)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "DELETE FROM " + tableName + " WHERE supplierID = ? AND contractID = ? AND productID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, supllierId);
                    pstmt.setInt(2, contractId);
                    pstmt.setInt(3, productID);
                    pstmt.executeUpdate();
                    System.out.println("Catalog deleted successfully.");
                } catch (SQLException e) {
                    System.out.println("Delete failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public CatalogDAO getCatalog(int supplierId, int contractId, int productID)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName + " WHERE supplierID = ? AND contractID = ? AND productID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, supplierId);
                    pstmt.setInt(2, contractId);
                    pstmt.setInt(3, productID);
                    var rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return new CatalogDAO(rs.getInt("supplierID"), rs.getInt("contractID"), rs.getInt("productID"), rs.getInt("catalogID"));
                    } else {
                        System.out.println("Catalog not found.");
                        return null;
                    }
                } catch (SQLException e) {
                    System.out.println("Query failed: " + e.getMessage());
                    return null;
                }
            } else {
                System.out.println("Connection to database failed.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<CatalogDAO> getContractSupplierCatalogs(int supplierId, int contractId)
    {
        List<CatalogDAO> catalogs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName + " WHERE supplierID = ? AND contractID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, supplierId);
                    pstmt.setInt(2, contractId);
                    try (var rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            catalogs.add(new CatalogDAO(rs.getInt("supplierID"), rs.getInt("contractID"), rs.getInt("productID"), rs.getInt("catalogID")));
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Query failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return catalogs;
    }

    public List<CatalogDAO> getAllCatalogs()
    {
        List<CatalogDAO> catalogs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName;
                try (var pstmt = conn.prepareStatement(sql);
                     var rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        catalogs.add(new CatalogDAO(rs.getInt("supplierID"), rs.getInt("contractID"), rs.getInt("productID"), rs.getInt("catalogID")));
                    }
                } catch (SQLException e) {
                    System.out.println("Query failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return catalogs;
    }
}
