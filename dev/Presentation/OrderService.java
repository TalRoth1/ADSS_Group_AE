package Presentation;

import java.util.ArrayList;
import java.util.List;

import Domain.OrderDL;
import Domain.OrderFacade;
import Domain.OrderItemDL;

public class OrderService {
    private OrderFacade of;
    
    public OrderService() {
        of = new OrderFacade();
    }

    public void createOrder(int supplierID, List<OrderItemPL> items) {
        // validate input
        if (supplierID <= 0) {
            System.out.println("Invalid supplier ID: " + supplierID);
            return;
        }
        // convert data types
        List<OrderItemDL> orderItems = new ArrayList<>();
        for (OrderItemPL item : items) {
            OrderItemDL orderItem = new OrderItemDL(item.getItemID(), item.getQuantity(), item.getCatalogID(), item.getTotalPrice());
            orderItems.add(orderItem);
        }
        //forward the request to the domain layer
        try {
            of.createOrder(supplierID, orderItems);
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }

    public void changeOrder(int orderID, List<OrderItemPL> newItems) {
        // validate input
        if (orderID <= 0) {
            System.out.println("Invalid order ID: " + orderID);
            return;
        }
        if (newItems == null || newItems.isEmpty()) {
            System.out.println("Invalid order items: " + newItems);
            return;
        }
        // convert data types
        List<OrderItemDL> orderItems = new ArrayList<>();
        for (OrderItemPL item : newItems) {
            OrderItemDL orderItem = new OrderItemDL(item.getItemID(), item.getQuantity(), item.getCatalogID(), item.getTotalPrice());
            orderItems.add(orderItem);
        }
        //forward the request to the domain layer
        try {
            of.changeOrder(orderID, orderItems);
        } catch (IllegalArgumentException e) {
            System.out.println("Error changing order: " + e.getMessage());
        }

    }
    public void cancelOrder(int orderID) {
        // validate input
        if (orderID <= 0) {
            System.out.println("Invalid order ID: " + orderID);
            return;
        }
        //forward the request to the domain layer
        try {
            of.cancelOrder(orderID);
        } catch (IllegalArgumentException e) {
            System.out.println("Error cancelling order: " + e.getMessage());
        }

    }
    public OrderPL getOrder(int orderID){
        // validate input
        if (orderID <= 0) {
            System.out.println("Invalid order ID: " + orderID);
            return null;
        }
        OrderPL orderPL = null;
        //forward the request to the domain layer
        try {
            OrderDL order = of.getOrder(orderID);
            orderPL = convertToOrderPL(order);
        } catch (IllegalArgumentException e) {
            System.out.println("Error getting order: " + e.getMessage());
        }
        return orderPL;
    }
    public List<OrderPL> getOrderHistory(int supplierID){
        // validate input
        if (supplierID <= 0) {
            System.out.println("Invalid supplier ID: " + supplierID);
            return null;
        }
        //forward the request to the domain layer
        try {
            List<OrderDL> orderHistory = of.getOrderHistory(supplierID);
            List<OrderPL> orderPLs = new ArrayList<>();
            for (OrderDL order : orderHistory) {
                orderPLs.add(convertToOrderPL(order));
            }
            return orderPLs;
        } catch (IllegalArgumentException e) {
            System.out.println("Error getting order history: " + e.getMessage());
            return null;
        }
    }

    public OrderPL convertToOrderPL(OrderDL order) {
        List<OrderItemPL> orderItems = new ArrayList<>();
        for (OrderItemDL item : order.getOrderItems()) {
            OrderItemPL orderItemPL = new OrderItemPL(item.getItemID(), item.getQuantity(), item.getCatalogID(), item.getTotalPrice());
            orderItems.add(orderItemPL);
        }
        return new OrderPL(order.getOrderID(), order.getSupplierID(), order.getOrderDate(), orderItems, order.getOrderStatus());
    }
}
