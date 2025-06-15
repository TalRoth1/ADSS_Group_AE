package DataLayer;

import DTO.ItemDTO;
import DataLayer.DAOs.ItemsDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemsController {
    private DBConnection dbConnection = new DBConnection();
    private Connection connection;
    private ItemsDAO itemsDAO;

    public ItemsController() {
        String DB_URL = "main.db";
        dbConnection.connect(DB_URL);
        this.connection = dbConnection.getConnection();
        this.itemsDAO = new ItemsDAO(connection);
    }

    public void addItem(ItemDTO item) throws SQLException {
        itemsDAO.addItem(item.getName(), item.getWeight());
    }

    public void updateItem(ItemDTO item) throws SQLException {
        itemsDAO.updateItem(item.getName(), item.getWeight());
    }

    public void deleteItem(ItemDTO item) throws SQLException {
        itemsDAO.deleteItem(item.getName());
    }

    public ItemDTO getItemByName(String name) throws SQLException {
        ResultSet rst = itemsDAO.getItem(name);
        if (rst.next()) {
            return new ItemDTO(rst.getString("name"), rst.getFloat("weight"));
        } else {
            throw new SQLException("Item not found with name: " + name);
        }
    }

    public ArrayList<ItemDTO> getAllItems() throws SQLException {
        ArrayList<ItemDTO> items = new ArrayList<>();
        ResultSet rst = itemsDAO.getAllItems();
        while (rst.next()) {
            items.add(new ItemDTO(rst.getString("name"), rst.getFloat("weight")));
        }
        return items;
    }

    public void clearAllItems() throws SQLException {
        itemsDAO.clearTable();
    }

    public void closeConnection() {
        dbConnection.close();
    }

    public void openConnection() {
        dbConnection.open("main.db");
        this.itemsDAO = new ItemsDAO(dbConnection.getConnection());
    }
}
