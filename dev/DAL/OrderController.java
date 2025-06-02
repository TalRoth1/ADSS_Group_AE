package DAL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import Utils.OrderStatus;

public class OrderController {
    private String ordersTableName = "Orders";
    private String orderItemsTableName = "OrderItems";
    String currentDir = System.getProperty("user.dir");
    String dbPath = currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;

    public OrderController() {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeTableOrder();
            initializeTableOrderItem();
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    private void initializeTableOrder() {
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            String sql = """
                        CREATE TABLE IF NOT EXISTS Orders (
                            orderID INTEGER PRIMARY KEY,
                            supplierID INTEGER NOT NULL,
                            contractID INTEGER NOT NULL,
                            orderDate DATE NOT NULL,
                            destination TEXT NOT NULL,
                            orderStatus TEXT NOT NULL

                        );
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create Products table: " + e.getMessage());
        }
    }

    private void initializeTableOrderItem() {
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            String sql = """
                        CREATE TABLE IF NOT EXISTS OrderItems (
                            orderID INTEGER PRIMARY KEY,
                            itemID INTEGER PRIMARY KEY,
                            quantity INTEGER NOT NULL,
                            catalogID INTEGER NOT NULL,
                            totalPrice REAL NOT NULL,
                            FOREIGN KEY (orderID) REFERENCES Orders(orderID)
                            FOREIGN KEY (itemID) REFERENCES Items(itemID)


                        );
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create Products table: " + e.getMessage());
        }
    }

    public OrderDAO getOrder(int orderID) {
        try (var conn = java.sql.DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + ordersTableName + " WHERE orderID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, orderID);
                    try (var rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            int supplierID = rs.getInt("supplierID");
                            int contractID = rs.getInt("contractID");
                            java.util.Date orderDate = new java.util.Date(rs.getDate("orderDate").getTime());
                            String destination = rs.getString("destination");
                            String statusStr = rs.getString("orderStatus");
                            Utils.OrderStatus orderStatus = Utils.OrderStatus.valueOf(statusStr);
                            return new OrderDAO(orderID, supplierID, contractID, orderDate, destination, orderStatus,
                                    this);
                        } else {
                            System.out.println("Order not found.");
                        }
                    }
                } catch (java.sql.SQLException e) {
                    System.out.println("Query failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void insertOrder(OrderDAO order) {
        try (var conn = java.sql.DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "INSERT INTO " + ordersTableName
                        + " (orderID, supplierID, contractID, orderDate, destination, orderStatus) VALUES (?, ?, ?, ?, ?, ?)";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, order.getOrderID());
                    pstmt.setInt(2, order.getSupplierID());
                    pstmt.setInt(3, order.getContractID());
                    pstmt.setDate(4, new java.sql.Date(order.getOrderDate().getTime()));
                    pstmt.setString(5, order.getDestination());
                    pstmt.setString(6, order.getOrderStatus().name());
                    pstmt.executeUpdate();
                    System.out.println("Order inserted successfully.");
                } catch (java.sql.SQLException e) {
                    System.out.println("Insert failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateOrder(int orderID, String column, String value) {
        try (var conn = java.sql.DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "UPDATE " + ordersTableName + " SET " + column + " = ? WHERE orderID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, value);
                    pstmt.setInt(2, orderID);
                    pstmt.executeUpdate();
                    System.out.println("Order updated successfully.");
                } catch (java.sql.SQLException e) {
                    System.out.println("Update failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteOrder(int orderID) {
        try (var conn = java.sql.DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "DELETE FROM " + ordersTableName + " WHERE orderID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, orderID);
                    pstmt.executeUpdate();
                    System.out.println("Order deleted successfully.");
                } catch (java.sql.SQLException e) {
                    System.out.println("Delete failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<OrderDAO> getAllOrders() {
        List<OrderDAO> orders = new ArrayList<>();
        try (var conn = java.sql.DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + ordersTableName;
                try (var stmt = conn.createStatement();
                        var rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        int orderID = rs.getInt("orderID");
                        int supplierID = rs.getInt("supplierID");
                        int contractID = rs.getInt("contractID");
                        java.util.Date orderDate = new java.util.Date(rs.getDate("orderDate").getTime());
                        String destination = rs.getString("destination");
                        String orderStatusStr = rs.getString("orderStatus");
                        OrderStatus orderStatus = OrderStatus.valueOf(orderStatusStr);
                        orders.add(new OrderDAO(orderID, supplierID, contractID, orderDate, destination, orderStatus,
                                this));
                    }
                } catch (java.sql.SQLException e) {
                    System.out.println("Query failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    public void insertOrderItem(OrderItemDAO orderItem) {
        try (var conn = java.sql.DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "INSERT INTO " + orderItemsTableName
                        + " (orderID, itemID, quantity, catalogID, totalPrice) VALUES (?, ?, ?, ?, ?)";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, orderItem.getOrderID());
                    pstmt.setInt(2, orderItem.getItemID());
                    pstmt.setInt(3, orderItem.getQuantity());
                    pstmt.setInt(4, orderItem.getCatalogID());
                    pstmt.setDouble(5, orderItem.getTotalPrice());
                    pstmt.executeUpdate();
                    System.out.println("Order item inserted successfully.");
                } catch (java.sql.SQLException e) {
                    System.out.println("Insert failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteOrderItem(int orderID, int itemID) {
        try (var conn = java.sql.DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "DELETE FROM " + orderItemsTableName + " WHERE orderID = ? AND itemID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, orderID);
                    pstmt.setInt(2, itemID);
                    pstmt.executeUpdate();
                    System.out.println("Order item deleted successfully.");
                } catch (java.sql.SQLException e) {
                    System.out.println("Delete failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<OrderItemDAO> getOrderItems(int orderID) {
        List<OrderItemDAO> orderItems = new ArrayList<>();
        try (var conn = java.sql.DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + orderItemsTableName + " WHERE orderID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, orderID);
                    try (var rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            int itemID = rs.getInt("itemID");
                            int quantity = rs.getInt("quantity");
                            int catalogID = rs.getInt("catalogID");
                            double totalPrice = rs.getDouble("totalPrice");
                            orderItems.add(new OrderItemDAO(orderID, itemID, quantity, catalogID, totalPrice));
                        }
                    }
                } catch (java.sql.SQLException e) {
                    System.out.println("Query failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderItems;
    }

    public void updateOrderItems(int orderID, Map<Integer, OrderItemDAO> newItems) {
        try (var conn = java.sql.DriverManager.getConnection(url)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                try {
                    String deleteSql = "DELETE FROM " + orderItemsTableName + " WHERE orderID = ?";
                    var pstmt = conn.prepareStatement(deleteSql);
                    pstmt.setInt(1, orderID);
                    pstmt.executeUpdate();
                    for (OrderItemDAO item : newItems.values()) {
                        String insertSql = "INSERT INTO " + orderItemsTableName
                                + " (orderID, itemID, quantity, catalogID, totalPrice) VALUES (?, ?, ?, ?, ?)";
                        var pstmt2 = conn.prepareStatement(insertSql);
                        pstmt2.setInt(1, item.getOrderID());
                        pstmt2.setInt(2, item.getItemID());
                        pstmt2.setInt(3, item.getQuantity());
                        pstmt2.setInt(4, item.getCatalogID());
                        pstmt2.setDouble(5, item.getTotalPrice());
                        pstmt2.executeUpdate();
                        System.out.println("Order item inserted successfully.");
                    }
                    conn.commit();
                    System.out.println("Transaction committed successfully.");
                } catch (SQLException e) {
                    System.out.println("/*Rolling back transaction due to error: " + e.getMessage());
                    conn.rollback();
                } finally {
                    conn.setAutoCommit(true);
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
