package DataLayer;
import java.sql.*;
import DomainLayer.ItemsDL;
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

    public void addItem(String name, int quantity, double weight) throws SQLException {
        itemCheck(name, quantity, weight);
        itemsDAO.addItem(generateId() ,name, quantity, weight);
    }

    public void updateItem(int id, String name, int quantity, double weight) throws SQLException {
        itemCheck(name, quantity, weight);
        itemsDAO.updateItem(id ,name, quantity, weight);
    }

    public void deleteItem(int id) throws SQLException {
        itemsDAO.deleteItem(id);
    }

    public ResultSet getItem(int id) throws SQLException {
        return itemsDAO.getItem(id);
    }

    public ResultSet getAllItems() throws SQLException {
        return itemsDAO.getAllItems();
    }

    private void itemCheck(String name, int quantity, double weight) {
        if (name == null || quantity <= 0 || weight <= 0) {
            throw new IllegalArgumentException("Invalid item data provided.");
        }
    }

}