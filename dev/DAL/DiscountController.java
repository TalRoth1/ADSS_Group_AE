package DAL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscountController
{
    private String tableName = "Discounts";
    String currentDir = System.getProperty("user.dir");
    String dbPath = currentDir + File.separator + "Data.db";
    String url = "jdbc:sqlite:" + dbPath;

    public DiscountController() 
    {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    public void insert(DiscountDAO discount) 
    {
        try(Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "INSERT INTO " + tableName + " (catalogID, minimumQuantity, discountPercentage) VALUES (?, ?, ?)";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, discount.getCatalogID());
                    pstmt.setInt(2, discount.getMinimumQuantity());
                    pstmt.setInt(3, discount.getDiscountPercentage());
                    pstmt.executeUpdate();
                    System.out.println("Discount inserted successfully.");
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

    public void update(int catalogID, String column, String value) 
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "UPDATE " + tableName + " SET " + column + " = ? WHERE catalogID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, value);
                    pstmt.setInt(2, catalogID);
                    pstmt.executeUpdate();
                    System.out.println("Discount updated successfully.");
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

    public void delete(int catalogID) 
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "DELETE FROM " + tableName + " WHERE catalogID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, catalogID);
                    pstmt.executeUpdate();
                    System.out.println("Discount deleted successfully.");
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

    public DiscountDAO getDiscount(int catalogID) 
    {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName + " WHERE catalogID = ?";
                try (var pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, catalogID);
                    var rs = pstmt.executeQuery();
                    if (rs.next()) {
                        int minimumQuantity = rs.getInt("minimumQuantity");
                        int discountPercentage = rs.getInt("discountPercentage");
                        return new DiscountDAO(catalogID, minimumQuantity, discountPercentage, this);
                    } else {
                        System.out.println("Discount not found.");
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

    public List<DiscountDAO> getAllDiscounts() 
    {
        List<DiscountDAO> discounts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String sql = "SELECT * FROM " + tableName;
                try (var pstmt = conn.prepareStatement(sql);
                     var rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int catalogID = rs.getInt("catalogID");
                        int minimumQuantity = rs.getInt("minimumQuantity");
                        int discountPercentage = rs.getInt("discountPercentage");
                        discounts.add(new DiscountDAO(catalogID, minimumQuantity, discountPercentage, this));
                    }
                } catch (SQLException e) {
                    System.out.println("Get all discounts failed: " + e.getMessage());
                }
            } else {
                System.out.println("Connection to database failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return discounts;
    }
}
