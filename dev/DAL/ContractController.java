package DAL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Domain.DeliveryMethod;
import Domain.OnOrderDelivery;
import Domain.OrderItemDL; // Is this ok?
import Domain.PeriodicDelivery;
import Domain.PeriodicItem;
import Domain.PickupDelivery;

public class ContractController
{
    private String onOrder = "OnOrders";
    private String pickup = "Pickups";
    private String periodic = "Periodics";
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
                        String sql = "INSERT INTO " + onOrder + " (contractID, supplierID, itemCatalogID) VALUES (?, ?, ?)";
                        try (var pstmt = conn.prepareStatement(sql)) {
                            pstmt.setInt(1, contract.getContractID());
                            pstmt.setInt(2, contract.getSupplierID());
                            pstmt.setObject(3, contract.getItemCatalogID()); // Assuming itemCatalogID is serializable
                            pstmt.executeUpdate();
                            System.out.println("Contract inserted successfully.");
                        } catch (SQLException e) {
                            System.out.println("Insert failed: " + e.getMessage());
                        }
                        break;
                    case "Pickup Delivery":
                        sql = "INSERT INTO " + pickup + " (contractID, supplierID, itemCatalogID) VALUES (?, ?, ?)";
                        try (var pstmt = conn.prepareStatement(sql)) {
                            pstmt.setInt(1, contract.getContractID());
                            pstmt.setInt(2, contract.getSupplierID());
                            pstmt.setObject(3, contract.getItemCatalogID()); // Assuming itemCatalogID is serializable
                            pstmt.executeUpdate();
                            System.out.println("Contract inserted successfully.");
                        } catch (SQLException e) {
                            System.out.println("Insert failed: " + e.getMessage());
                        }
                        break;
                    case "Periodic Delivery":
                        sql = "INSERT INTO " + periodic + " (contractID, supplierID, itemCatalogID, interval) VALUES (?, ?, ?, ?)";
                        try (var pstmt = conn.prepareStatement(sql)) {
                            pstmt.setInt(1, contract.getContractID());
                            pstmt.setInt(2, contract.getSupplierID());
                            pstmt.setObject(3, contract.getItemCatalogID());
                            pstmt.setObject(4, ((PeriodicDelivery)contract.getDeliveryMethod()).getDeliveryInterval());
                            pstmt.executeUpdate();
                            System.out.println("Contract inserted successfully.");
                            
                            List <PeriodicItem> orderItems = ((PeriodicDelivery)contract.getDeliveryMethod()).getOrderItems();
                            sql = "INSERT INTO  Periods (contractID, productId, quantity, price) VALUES (?, ?, ?, ?)";
                            for (PeriodicItem item : orderItems) {
                                try (var pstmt2 = conn.prepareStatement(sql)) {
                                    pstmt2.setInt(1, contract.getContractID());
                                    pstmt2.setInt(2, item.getItemID());
                                    pstmt2.setInt(3, item.getQuantity());
                                    pstmt2.setDouble(4, item.getPrice());
                                    pstmt2.executeUpdate();
                                } catch (SQLException e) {
                                    System.out.println("Insert into Periods failed: " + e.getMessage());
                                }
                            }
                        } catch (SQLException e) {
                            System.out.println("Insert failed: " + e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Invalid delivery method.");
                        return;
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
                String sql = "UPDATE " + onOrder + " SET " + column + " = ? WHERE contractID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, value);
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                    System.out.println("Contract updated successfully.");
                } catch (SQLException e) {
                    System.out.println("Update failed: " + e.getMessage());
                }
                sql = "UPDATE " + pickup + " SET " + column + " = ? WHERE contractID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, value);
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                    System.out.println("Contract updated successfully.");
                } catch (SQLException e) {
                    System.out.println("Update failed: " + e.getMessage());
                }
                sql = "UPDATE " + periodic + " SET " + column + " = ? WHERE contractID = ?";
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
                String sql = "DELETE FROM " + onOrder + " WHERE contractID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    System.out.println("Contract deleted successfully.");
                } catch (SQLException e) {
                    System.out.println("Delete failed: " + e.getMessage());
                }
                sql = "DELETE FROM " + pickup + " WHERE contractID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    System.out.println("Contract deleted successfully.");
                } catch (SQLException e) {
                    System.out.println("Delete failed: " + e.getMessage());
                }
                sql = "DELETE FROM " + periodic + " WHERE contractID = ?";
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
                String sql = "SELECT * FROM " + onOrder + " WHERE supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, suppId);
                    try (var rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            contracts.add(new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", List.class), rs.getObject("billOfQuantities", List.class), new OnOrderDelivery()));
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Get supplier contracts failed: " + e.getMessage());
                }

                sql = "SELECT * FROM " + pickup + " WHERE supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, suppId);
                    try (var rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            contracts.add(new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", List.class), rs.getObject("billOfQuantities", List.class), new PickupDelivery()));
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Get supplier contracts failed: " + e.getMessage());
                }

                sql = "SELECT * FROM " + periodic + " WHERE supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, suppId);
                    try (var rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            String subsql = "SELECT * FROM Periods WHERE contractID = ?";
                            List<PeriodicItem> orderItems = new ArrayList<>();
                            try (var subpstmt = conn.prepareStatement(subsql)) {
                                subpstmt.setInt(1, rs.getInt("contractID"));
                                try (var subrs = subpstmt.executeQuery()) {
                                    while (subrs.next()) {
                                        orderItems.add(new PeriodicItem(subrs.getInt("itemID"), subrs.getInt("quantity"), subrs.getInt("price")));
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println("Get contract periods failed: " + e.getMessage());
                            }

                            contracts.add(new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", List.class), rs.getObject("billOfQuantities", List.class), new PeriodicDelivery(rs.getInt("interval"), orderItems)));
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
                String sql = "SELECT * FROM " + onOrder;
                try (var pstmt = conn.prepareStatement(sql); var rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        contracts.add(new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", List.class), rs.getObject("billOfQuantities", List.class), new OnOrderDelivery()));
                    }
                } catch (SQLException e) {
                    System.out.println("Get all contracts failed: " + e.getMessage());
                }

                sql = "SELECT * FROM " + pickup;
                try (var pstmt = conn.prepareStatement(sql); var rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        contracts.add(new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", List.class), rs.getObject("billOfQuantities", List.class), new PickupDelivery()));
                    }
                } catch (SQLException e) {
                    System.out.println("Get all contracts failed: " + e.getMessage());
                }

                sql = "SELECT * FROM " + periodic;
                try (var pstmt = conn.prepareStatement(sql); var rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String subsql = "SELECT * FROM Periods WHERE contractID = ?";
                        List<PeriodicItem> orderItems = new ArrayList<>();
                        try (var subpstmt = conn.prepareStatement(subsql)) {
                            subpstmt.setInt(1, rs.getInt("contractID"));
                            try (var subrs = subpstmt.executeQuery()) {
                                while (subrs.next()) {
                                    orderItems.add(new PeriodicItem(subrs.getInt("itemID"), subrs.getInt("quantity"), subrs.getInt("price")));
                                }
                            }
                        } catch (SQLException e) {
                            System.out.println("Get contract periods failed: " + e.getMessage());
                        }
                        contracts.add(new ContractDAO(rs.getInt("contractID"), rs.getInt("supplierID"), rs.getObject("itemCatalogID", List.class), rs.getObject("billOfQuantities", List.class), new PeriodicDelivery(rs.getInt("interval"), orderItems)));
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
