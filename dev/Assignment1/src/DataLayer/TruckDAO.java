package DataLayer;
import java.sql.*;
import DomainLayer.TruckDL;

public class TruckDAO {
    Connection connection;
    /*public int Number;
    public String Model;
    public String Type;
    public float Weight;
    public float MaxWeight;
    public boolean IsBusy = false;
     */

    public TruckDAO(Connection connection) {
        this.connection = connection;
    }

    public void addTruck(int number, String model, String type, float weight, float maxWeight) {
        Boolean isBusy = false;
        String sql = "INSERT INTO trucks (number, model, type, weight, maxWeight, isBusy) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, number);
            pstmt.setString(2, model);
            pstmt.setString(3, type);
            pstmt.setFloat(4, weight);
            pstmt.setFloat(5, maxWeight);
            pstmt.setBoolean(6, isBusy);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding truck: " + e.getMessage());
        }
    }

    public void updateTruck(int number, String model, String type, float weight, float maxWeight) {
        String sql = "UPDATE trucks SET model = ?, type = ?, weight = ?, maxWeight = ? WHERE number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, model);
            pstmt.setString(2, type);
            pstmt.setFloat(3, weight);
            pstmt.setFloat(4, maxWeight);
            pstmt.setInt(5, number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating truck: " + e.getMessage());
        }
    }

    public void deleteTruck(int number) {
        String sql = "DELETE FROM trucks WHERE number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting truck: " + e.getMessage());
        }
    }

    public TruckDL getTruck(int number) {
        String sql = "SELECT * FROM trucks WHERE number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, number);
            ResultSet rs = pstmt.executeQuery();
            TruckDL truck = null;
            if (rs.next()) {
                truck = new TruckDL(
                    rs.getInt("number"),
                    rs.getString("model"),
                    rs.getString("type"),
                    rs.getFloat("weight"),
                    rs.getFloat("maxWeight")
                );
            } 
            return truck;
        } catch (SQLException e) {
            System.out.println("Error retrieving truck: " + e.getMessage());
        }
    }

    public ResultSet getAllTrucks() {
        String sql = "SELECT * FROM trucks";
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error retrieving all trucks: " + e.getMessage());
            return null;
        }
    }
}
