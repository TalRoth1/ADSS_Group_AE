package DAL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class SupplierController {
    private final String tableName = "Suppliers";
    String currentDir = System.getProperty("user.dir");
    String dbPath = currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;

    public SupplierController() {
        // Ensure the database connection is established
        try {

            Class.forName("org.sqlite.JDBC");
            initializeTable();
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    private void initializeTable() {
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            String sql = """
                        CREATE TABLE IF NOT EXISTS Suppliers (
                            supplierID INTEGER PRIMARY KEY,
                            companyID INTEGER NOT NULL,
                            bankAccount INTEGER NOT NULL,
                            paymentMethod TEXT NOT NULL,
                            contactMail TEXT NOT NULL,
                            contactPhone TEXT NOT NULL

                        );
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create Products table: " + e.getMessage());
        }
    }

    public void insert(SupplierDAO supplier) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "INSERT INTO " + tableName
                        + " (supplierID, companyID, bankAccount, paymentMethod, contactMail, contactPhone) VALUES (?, ?, ?, ?, ?, ?)";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, supplier.getId());
                    pstmt.setInt(2, supplier.getCompanyID());
                    pstmt.setInt(3, supplier.getBankAccount());
                    pstmt.setString(4, supplier.getPaymentMethod());
                    pstmt.setString(5, supplier.getContactMail());
                    pstmt.setString(6, supplier.getContactPhone());
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

    public void Update(int id, String column, String value) {
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

    public void delete(int id) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "DELETE FROM " + tableName + " WHERE supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    System.out.println("Supplier deleted successfully.");
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

    public SupplierDAO getSupplier(int id) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName + " WHERE supplierID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    try (var rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            int companyID = rs.getInt("companyID");
                            int bankAccount = rs.getInt("bankAccount");
                            String paymentMethod = rs.getString("paymentMethod");
                            String contactMail = rs.getString("contactMail");
                            String contactPhone = rs.getString("contactPhone");
                            return new SupplierDAO(id, companyID, bankAccount, paymentMethod, contactMail, contactPhone,
                                    this);
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Get supplier failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Supplier not found
    }

    public List<SupplierDAO> getAllSuppliers() {
        List<SupplierDAO> suppliers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName;
                try (var pstmt = conn.prepareStatement(sql);
                        var rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("supplierID");
                        int companyID = rs.getInt("companyID");
                        int bankAccount = rs.getInt("bankAccount");
                        String paymentMethod = rs.getString("paymentMethod");
                        String contactMail = rs.getString("contactMail");
                        String contactPhone = rs.getString("contactPhone");
                        suppliers.add(new SupplierDAO(id, companyID, bankAccount, paymentMethod, contactMail,
                                contactPhone, this));
                    }
                } catch (SQLException e) {
                    System.out.println("Get all suppliers failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return suppliers;
    }

    public int getNextId() {
        String sql = "SELECT MAX(supplierID) AS maxID FROM " + tableName;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (var pstmt = conn.prepareStatement(sql);
                        var rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Math.max(1, rs.getInt("maxID") + 1); // Return the next ID
                    }
                } catch (SQLException e) {
                    System.out.println("Get next ID failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 1;
    }

    public void clearData() {
        String sql = "DELETE FROM " + tableName;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.executeUpdate();
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