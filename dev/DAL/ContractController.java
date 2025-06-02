package DAL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Domain.OnOrderDelivery;
import Domain.PeriodicDelivery;
import Domain.PeriodicItem;
import Domain.PickupDelivery;

public class ContractController
{
    private String onOrder = "OnOrders";
    private String pickup = "Pickups";
    private String periodic = "Periodics";
    private String periodicItem = "PeriodicItems";
    private String discount = "Discounts";
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
                switch (contract.getDeliveryMethod().toString()) {
                    case "On Order Delivery":
                        String sql = "INSERT INTO " + onOrder + " (contractID, supplierID, itemID, catalogID) VALUES (?, ?, ?, ?)";
                        try (var pstmt = conn.prepareStatement(sql)) {
                            for (Map.Entry<Integer, Integer> entry : contract.getItemCatalog().entrySet()) {
                                pstmt.setInt(1, contract.getContractID());
                                pstmt.setInt(2, contract.getSupplierID());
                                pstmt.setInt(3, entry.getKey());
                                pstmt.setInt(4, entry.getValue());
                                pstmt.executeUpdate();
                            }
                            System.out.println("Contract inserted successfully.");
                        } catch (SQLException e) {
                            System.out.println("Insert failed: " + e.getMessage());
                        }
                        break;
                    case "Pickup Delivery":
                        sql = "INSERT INTO " + pickup + " (contractID, supplierID, itemID, catalogID) VALUES (?, ?, ?, ?)";
                        try (var pstmt = conn.prepareStatement(sql)) {
                            for (Map.Entry<Integer, Integer> entry : contract.getItemCatalog().entrySet()) {
                                pstmt.setInt(1, contract.getContractID());
                                pstmt.setInt(2, contract.getSupplierID());
                                pstmt.setInt(3, entry.getKey());
                                pstmt.setInt(4, entry.getValue());
                                pstmt.executeUpdate();
                            }
                            System.out.println("Contract inserted successfully.");
                        } catch (SQLException e) {
                            System.out.println("Insert failed: " + e.getMessage());
                        }
                        break;
                    case "Periodic Delivery":
                        sql = "INSERT INTO " + periodic + " (contractID, supplierID, ItemID, catalogID, day) VALUES (?, ?, ?, ?, ?)";
                        try (var pstmt = conn.prepareStatement(sql)) {
                            for (Map.Entry<Integer, Integer> entry : contract.getItemCatalog().entrySet()) {
                                pstmt.setInt(1, contract.getContractID());
                                pstmt.setInt(2, contract.getSupplierID());
                                pstmt.setInt(3, entry.getKey());
                                pstmt.setInt(4, entry.getValue());
                                pstmt.setInt(5, ((PeriodicDelivery) contract.getDeliveryMethod()).getDay());
                                pstmt.executeUpdate();
                            }
                            List <PeriodicItem> orderItems = ((PeriodicDelivery)contract.getDeliveryMethod()).getOrderItems();
                            sql = "INSERT INTO " + periodicItem + " (contractID, supplierID, productId, quantity, price) VALUES (?, ?, ?, ?, ?)";
                            for (PeriodicItem item : orderItems) {
                                var pstmt2 = conn.prepareStatement(sql);
                                pstmt2.setInt(1, contract.getContractID());
                                pstmt2.setInt(2, contract.getSupplierID());
                                pstmt2.setInt(3, item.getItemID());
                                pstmt2.setInt(4, item.getQuantity());
                                pstmt2.setDouble(5, item.getPrice());
                                pstmt2.executeUpdate();
                            }
                        } catch (SQLException e) {
                            System.out.println("Insert failed: " + e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Invalid delivery method.");
                        return;
                }
                String discountSql = "INSERT INTO " + discount + " (contractID, supplierID, catalogID ,minimumQuantity, discountPercentage) VALUES (?, ?, ?, ?, ?)";
                try (var pstmt = conn.prepareStatement(discountSql)) {
                    for (DiscountDAO discount : contract.getBillOfQuantities()) {
                        pstmt.setInt(1, contract.getContractID());
                        pstmt.setInt(2, contract.getSupplierID());
                        pstmt.setInt(3, discount.getCatalogID());
                        pstmt.setInt(4, discount.getMinimumQuantity());
                        pstmt.setDouble(5, discount.getDiscountPercentage());
                        pstmt.executeUpdate();
                    }
                    System.out.println("Discounts inserted successfully.");
                } catch (SQLException e) {
                    System.out.println("Insert discounts failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePeriodic(int supplierId ,int contractId, List<PeriodicItem> orderItems)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                String sql = "DELETE FROM " + periodicItem + " WHERE contractID = ? AND supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, contractId);
                    pstmt.setInt(2, supplierId);
                    pstmt.executeUpdate();
                    sql = "INSERT INTO " + periodicItem + " (contractID, supplierID, itemID, quantity, price) VALUES (?, ?, ?, ?, ?)";
                    for (PeriodicItem item : orderItems) {
                        var pstmt2 = conn.prepareStatement(sql);
                        pstmt2.setInt(1, contractId);
                        pstmt2.setInt(2, supplierId);
                        pstmt2.setInt(3, item.getItemID());
                        pstmt2.setInt(4, item.getQuantity());
                        pstmt2.setDouble(5, item.getPrice());
                        pstmt2.executeUpdate();
                    }
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    System.out.println("Delete from " + periodicItem + " failed: " + e.getMessage());
                } finally{
                    conn.setAutoCommit(true);
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(int supplierId, int contractId)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "DELETE FROM " + onOrder + " WHERE contractID = ? AND supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, contractId);
                    pstmt.setInt(2, supplierId);
                    pstmt.executeUpdate();
                    System.out.println("Contract deleted successfully.");
                } catch (SQLException e) {
                    System.out.println("Delete failed: " + e.getMessage());
                }
                sql = "DELETE FROM " + pickup + " WHERE contractID = ? AND supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, contractId);
                    pstmt.setInt(2, supplierId);
                    pstmt.executeUpdate();
                    System.out.println("Contract deleted successfully.");
                } catch (SQLException e) {
                    System.out.println("Delete failed: " + e.getMessage());
                }
                sql = "DELETE FROM " + periodic + " WHERE contractID = ? AND supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, contractId);
                    pstmt.setInt(2, supplierId);
                    pstmt.executeUpdate();
                    String periodSql = "DELETE FROM " + periodicItem + " WHERE contractID = ? AND supplierID = ?";
                    var periodPstmt = conn.prepareStatement(periodSql);
                    periodPstmt.setInt(1, contractId);
                    periodPstmt.setInt(2, supplierId);
                    periodPstmt.executeUpdate();
                    System.out.println("Contract deleted successfully.");
                } catch (SQLException e) {
                    System.out.println("Delete failed: " + e.getMessage());
                }

                sql = "DELETE FROM " + discount + " WHERE contractID = ? AND supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, contractId);
                    pstmt.setInt(2, supplierId);
                    pstmt.executeUpdate();
                    System.out.println("Discounts deleted successfully.");
                } catch (SQLException e) {
                    System.out.println("Delete discounts failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateContact(int contractID, int supplierID, List<DiscountDAO> newBoq)
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "UPDATE " + discount + " SET minimumQuantity = ?, discountPercentage = ? WHERE contractID = ? AND supplierID = ?";
                var pstmt = conn.prepareStatement(sql);
                for (DiscountDAO discount : newBoq) {
                    pstmt.setInt(1, discount.getMinimumQuantity());
                    pstmt.setDouble(2, discount.getDiscountPercentage());
                    pstmt.setInt(3, contractID);
                    pstmt.setInt(4, supplierID);
                    pstmt.executeUpdate();
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
                String sql = "SELECT * FROM " + onOrder + " WHERE supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, suppId);
                    var rs = pstmt.executeQuery();
                    int contractId = rs.getInt("contractID");
                    int supplierId = rs.getInt("supplierID");
                    Map<Integer, Integer> items = new HashMap<>();
                    while (rs.next()) {
                        items.put(rs.getInt("itemID"), rs.getInt("catalogID"));
                    }
                    List<DiscountDAO> discounts = new ArrayList<>();
                    String discountSql = "SELECT * FROM " + discount + " WHERE contractID = ? AND supplierID = ?";
                    var discountPstmt = conn.prepareStatement(discountSql);
                    discountPstmt.setInt(1, contractId);
                    discountPstmt.setInt(2, supplierId);
                    var discountRs = discountPstmt.executeQuery();
                    while (discountRs.next()) {
                        discounts.add(new DiscountDAO(discountRs.getInt("CatalogID"), discountRs.getInt("minimumQuantity"), discountRs.getDouble("discountPercentage")));
                    }
                    contracts.add(new ContractDAO(contractId, supplierId, items, discounts, new OnOrderDelivery()));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return new ArrayList<>();
                }
                sql = "SELECT * FROM " + pickup + " WHERE supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, suppId);
                    var rs = pstmt.executeQuery();
                    int contractId = rs.getInt("contractID");
                    int supplierId = rs.getInt("supplierID");
                    Map<Integer, Integer> items2 = new HashMap<>();
                    while (rs.next()) {
                        items2.put(rs.getInt("itemID"), rs.getInt("catalogID"));
                    }
                    List<DiscountDAO> discounts = new ArrayList<>();
                    String discountSql = "SELECT * FROM " + discount + " WHERE contractID = ? AND supplierID = ?";
                    var discountPstmt = conn.prepareStatement(discountSql);
                    discountPstmt.setInt(1, contractId);
                    discountPstmt.setInt(2, supplierId);
                    var discountRs = discountPstmt.executeQuery();
                    while (discountRs.next()) {
                        discounts.add(new DiscountDAO(discountRs.getInt("CatalogID"), discountRs.getInt("minimumQuantity"), discountRs.getDouble("discountPercentage")));
                    }
                    contracts.add(new ContractDAO(contractId, supplierId, items2, discounts, new PickupDelivery()));
                } catch (SQLException e1) {
                    System.out.println("Get supplier contracts failed: " + e1.getMessage());
                }

                sql = "SELECT * FROM " + periodic + " WHERE supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, suppId);
                    var rs = pstmt.executeQuery();
                    int contractId = rs.getInt("contractID");
                    int supplierId = rs.getInt("supplierID");
                    Map<Integer, Integer> items3 = new HashMap<>();
                    List<PeriodicItem> orderItems = new ArrayList<>();
                    while (rs.next()) {
                        items3.put(rs.getInt("itemID"), rs.getInt("catalogID"));
                    }
                    List<DiscountDAO> discounts = new ArrayList<>();
                    String discountSql = "SELECT * FROM " + discount + " WHERE contractID = ? AND supplierID = ?";
                    var discountPstmt = conn.prepareStatement(discountSql);
                    discountPstmt.setInt(1, contractId);
                    discountPstmt.setInt(2, supplierId);
                    var discountRs = discountPstmt.executeQuery();
                    while (discountRs.next()) {
                        discounts.add(new DiscountDAO(discountRs.getInt("CatalogID"), discountRs.getInt("minimumQuantity"), discountRs.getDouble("discountPercentage")));
                    }
                    String subsql = "SELECT * FROM " + periodicItem + " WHERE supplierID = ? AND contractID = ?";
                    var subpstmt = conn.prepareStatement(subsql);
                    subpstmt.setInt(1, contractId);
                    var subrs = subpstmt.executeQuery();
                    while (subrs.next()) {
                        orderItems.add(new PeriodicItem(subrs.getInt("productID"), subrs.getInt("quantity"), subrs.getInt("price")));
                    }

                    contracts.add(new ContractDAO(contractId, supplierId, items3, discounts, new PeriodicDelivery(rs.getInt("day"), orderItems)));
                } catch (SQLException e) {
                    System.out.println("Get supplier contracts failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
            return contracts;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public void clearData()
    {
        String sql = "DELETE FROM " + onOrder + "; DELETE FROM " + pickup + "; DELETE FROM " + periodic + "; DELETE FROM " + periodicItem + "; DELETE FROM " + discount + ";";
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (var stmt = conn.createStatement()) {
                    stmt.executeUpdate(sql);
                    System.out.println("All contract data cleared successfully.");
                } catch (SQLException e) {
                    System.out.println("Clear data failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
