package DAL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Domain.DeliveryMethod;

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

    public List<ContractDAO> getSupplierContracts(int suppId)
    {
        try(Connection conn = DriverManager.getConnection(url)) {
            List<ContractDAO> contracts = new ArrayList<>();
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName + " WHERE supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, suppId);
                    try (var rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            contracts.add(new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", List.class), rs.getObject("billOfQuantities", List.class), DeliveryMethod.valueOf(rs.getString("deliveryMethod"))));
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Get supplier contracts failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
            return contracts;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ContractDAO> getAllContracts()
    {
        List<ContractDAO> contracts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName;
                try (var pstmt = conn.prepareStatement(sql); var rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        contracts.add(new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", List.class), rs.getObject("billOfQuantities", List.class), DeliveryMethod.valueOf(rs.getString("deliveryMethod"))));
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
