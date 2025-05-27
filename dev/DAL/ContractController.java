package DAL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContractController
{
    private String tableName = "Contracts";
    String currentDir = System.getProperty("user.dir");
    String dbPath = currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;

    public ContractController() 
    {
        // Ensure the database connection is established
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    public void insert(ContractDAO contract)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "INSERT INTO " + tableName + " (contractID, supplierID, itemCatalogID, deliveryMethod) VALUES (?, ?, ?, ?)";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, contract.getContractID());
                    pstmt.setInt(2, contract.getSupplierID());
                    pstmt.setObject(3, contract.getItemCatalogID()); // Assuming itemCatalogID is serializable
                    pstmt.setString(4, contract.getDeliveryMethod().toString());
                    pstmt.executeUpdate();
                    System.out.println("Contract inserted successfully.");
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

    public void update(int id, String column, String value)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "UPDATE " + tableName + " SET " + column + " = ? WHERE contractID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, value);
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                    System.out.println("Contract updated successfully.");
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

    public void delete(int id)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "DELETE FROM " + tableName + " WHERE contractID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    System.out.println("Contract deleted successfully.");
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

    public ContractDAO getContract(int id)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName + " WHERE contractID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    var rs = pstmt.executeQuery();
                    if (rs.next()) {
                        // Assuming the constructor of ContractDAO matches the columns in the database
                        return new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", Map.class), rs.getObject("billOfQuantities", List.class), DeliveryMethod.valueOf(rs.getString("deliveryMethod")));
                    }
                } catch (SQLException e) {
                    System.out.println("Get contract failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Return null if no contract found
    }

    public List<ContractDAO> getAllContracts()
    {
        List<ContractDAO> contracts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName;
                try (var pstmt = conn.prepareStatement(sql); var rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        contracts.add(new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", Map.class), rs.getObject("billOfQuantities", List.class), DeliveryMethod.valueOf(rs.getString("deliveryMethod"))));
                    }
                } catch (SQLException e) {
                    System.out.println("Get all contracts failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contracts;
    }
}
