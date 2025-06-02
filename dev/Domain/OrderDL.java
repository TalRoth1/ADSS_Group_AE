package Domain;

import java.util.Date;
import java.util.Map;

import DAL.OrderController;
import DAL.OrderDAO;
import Utils.OrderStatus;

public class OrderDL {
    private final int orderID;
    private final int supplierID;
    private final int contractID;
    private Date orderDate;
    private String destination;
    private Map<Integer, OrderItemDL> orderItems;
    private OrderStatus orderStatus;
    private OrderDAO DAO;

    public OrderDL(int orderID, int supplierID, int contractID, Date orderDate, String destination,
            Map<Integer, OrderItemDL> orderItems, OrderController orderController) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.contractID = contractID;
        this.orderDate = orderDate;
        this.destination = destination;
        this.orderItems = orderItems;
        this.orderStatus = OrderStatus.IN_PROGRESS;
        this.DAO = new OrderDAO(orderID, supplierID, contractID, orderDate, destination, orderStatus, orderController);
    }

    public OrderDL(OrderDAO orderDAO) {
        this.orderID = orderDAO.getOrderID();
        this.supplierID = orderDAO.getSupplierID();
        this.contractID = orderDAO.getContractID();
        this.orderDate = orderDAO.getOrderDate();
        this.destination = orderDAO.getDestination();
        this.orderStatus = orderDAO.getOrderStatus();
        this.DAO = orderDAO;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public int getContractID() {
        return contractID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getDestination() {
        return destination;
    }

    public Map<Integer, OrderItemDL> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderDAO getDao() {
        return DAO;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderItems(Map<Integer, OrderItemDL> orderItems) {
        this.orderItems = orderItems;
    }

    public void setDao(OrderDAO dao) {
        this.DAO = dao;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderID).append("\n")
                .append("Supplier ID: ").append(supplierID).append("\n")
                .append("Order Date: ").append(orderDate).append("\n")
                .append("Destination: ").append(destination).append("\n")
                .append("Order Items: \n");
        for (OrderItemDL item : orderItems.values()) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("Order Status: ").append(orderStatus).append("\n");
        return sb.toString();
    }
}
