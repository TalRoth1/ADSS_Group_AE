package DataLayer;
import DTO.ItemDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemsController {
    private Connection connection;
    private ItemsDAO itemsDAO;
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    public ItemsController(Connection connection) {
        this.connection = connection;
        this.itemsDAO = new ItemsDAO(connection);

    }

    private int generateId() {
        return idGenerator.getAndIncrement();
    }

    public void addItem(ItemDTO item) throws SQLException {
        itemCheck(item.getName(), item.getWeight());
        itemsDAO.addItem(generateId(), item.getName(), item.getWeight());
    }

    public void updateItem(ItemDTO item) throws SQLException {
        itemCheck(item.getName(), item.getWeight());
        itemsDAO.updateItem(item.getId(), item.getName(), item.getWeight());
    }

    public void deleteItem(ItemDTO item) throws SQLException {
        itemsDAO.deleteItem(item.getId());
    }

    public ItemDTO getItem(int id) throws SQLException {
        ResultSet rst =  itemsDAO.getItem(id);
        if (rst.next()) {
            return new ItemDTO(rst.getInt("id"), rst.getString("name"), rst.getDouble("weight"));
        } else {
            throw new SQLException("Item not found with id: " + id);
        }
    }

    public ArrayList<ItemDTO> getAllItems() throws SQLException {
        ArrayList<ItemDTO> items = new ArrayList<>();
        ResultSet rst = itemsDAO.getAllItems();
        while (rst.next()) {
            items.add(new ItemDTO(rst.getInt("id"), rst.getString("name"), rst.getDouble("weight")));
        }
        return items;
    }

    private void itemCheck(String name, double weight) {
        if (name == null || weight <= 0) {
            throw new IllegalArgumentException("Invalid item data provided.");
        }
    }

}