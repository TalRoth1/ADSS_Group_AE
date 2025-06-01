package DAL;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchController 
{
    private final String tableName = "Branches";
    private final String currentDir = System.getProperty("user.dir");
    private final String dbPath = currentDir + File.separator + "Data.db";
    private final String url = "jdbc:sqlite:" + dbPath;

    public BranchController() 
    {
        try 
        {
            Class.forName("org.sqlite.JDBC");
            initializeTable();
        } 
        catch (ClassNotFoundException e) 
        {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    private void initializeTable() 
    {
        String sql = """
                CREATE TABLE IF NOT EXISTS Branches (
                    branchID INTEGER PRIMARY KEY,
                    name TEXT NOT NULL,
                    address TEXT NOT NULL
                );
                """;
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) 
        {
            stmt.execute(sql);
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException("Failed to create Branches table", e);
        }
    }

    public void insert(BranchDAO branch)
    {
        String sql = "INSERT INTO " + tableName + " (branchID, name, address) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, branch.getBranchID());
            pstmt.setString(2, branch.getName());
            pstmt.setString(3, branch.getAddress());
            pstmt.executeUpdate();
            branch.persist();
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException("Insert failed for branchID: " + branch.getBranchID(), e);
        }
    }

    public void updateName(int id, String newName) 
    {
        String sql = "UPDATE " + tableName + " SET name = ? WHERE branchID = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) 
            {
                throw new IllegalArgumentException("No branch found with id: " + id);
            }
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException("Failed to update branch name for id: " + id, e);
        }
    }

    public void updateAddress(int id, String newAddress) {
        String sql = "UPDATE " + tableName + " SET address = ? WHERE branchID = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) 
            {
            pstmt.setString(1, newAddress);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) 
            {
                throw new IllegalArgumentException("No branch found with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update branch address for id: " + id, e);
        }
    }

    public void delete(int id) 
    {
        String sql = "DELETE FROM " + tableName + " WHERE branchID = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) 
            {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new IllegalArgumentException("No branch found with id: " + id);
            }
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException("Failed to delete branch with id: " + id, e);
        }
    }

    public BranchDAO getBranch(int id) 
    {
        String sql = "SELECT * FROM " + tableName + " WHERE branchID = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) 
            {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) 
            {
                BranchDAO branch = new BranchDAO(rs.getInt("branchID"), rs.getString("name"), rs.getString("address"));
                branch.persist();
                return branch;
            } 
            else 
            {
                throw new IllegalArgumentException("No branch found with id: " + id);
            }
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException("Failed to retrieve branch with id: " + id, e);
        }
    }

    public List<BranchDAO> getAllBranches() 
    {
        List<BranchDAO> branches = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) 
        {
            while (rs.next()) 
            {
                BranchDAO branch = new BranchDAO(rs.getInt("branchID"), rs.getString("name"), rs.getString("address"));
                branch.persist();
                branches.add(branch);
            }

        } 
        catch (SQLException e) 
        {
            throw new RuntimeException("Failed to retrieve all branches", e);
        }
        return branches;
    }

    public boolean isBranchExists(int id) 
    {
        String sql = "SELECT 1 FROM " + tableName + " WHERE branchID = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } 
        catch (SQLException e) 
        {
            throw new RuntimeException("Failed to check existence of branch with id: " + id, e);
        }
    }

    public int getMaxBranchID()
    {
        int maxID = 0;
        List<BranchDAO> allBranches = getAllBranches();
        for (BranchDAO branch : allBranches) 
        {
            if (branch.getBranchID() > maxID) 
            {
                maxID = branch.getBranchID();
            }
        }
        return maxID;
    }

}
