package Service;

import java.util.ArrayList;
import java.util.List;
import Domain.OrderDL;
import Domain.OrderFacade;
import Domain.OrderItemDL;
import Domain.SupplierFacade;

public class OrderService {
    private OrderFacade of;

    public OrderService(SupplierFacade sf) {
        of = new OrderFacade(sf);
    }

    public void createOrder(int supplierID, String destination, List<OrderItemSL> items) {
        // validate input
        if (supplierID <= 0) {
            System.out.println("Error creating order\nInvalid supplier ID: " + supplierID);
            return;
        }
        // convert data types
        List<OrderItemDL> orderItems = new ArrayList<>();
        for (OrderItemSL item : items) {
            OrderItemDL orderItem = new OrderItemDL(item.getItemID(), item.getQuantity(), item.getCatalogID(),
                    item.getTotalPrice());
            orderItems.add(orderItem);
        }
        // forward the request to the domain layer
        try {
            of.createOrder(supplierID, destination, orderItems);
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }

    public void changeOrder(int orderID, List<OrderItemSL> newItems) {
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
        for (OrderItemSL item : newItems) {
            OrderItemDL orderItem = new OrderItemDL(item.getItemID(), item.getQuantity(), item.getCatalogID(),
                    item.getTotalPrice());
            orderItems.add(orderItem);
        }
        // forward the request to the domain layer
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
        // forward the request to the domain layer
        try {
            of.cancelOrder(orderID);
        } catch (IllegalArgumentException e) {
            System.out.println("Error cancelling order: " + e.getMessage());
        }

    }

    public OrderSL getOrder(int orderID) {
        // validate input
        if (orderID <= 0) {
            System.out.println("Invalid order ID: " + orderID);
            return null;
        }
        OrderSL orderPL = null;
        // forward the request to the domain layer
        try {
            OrderDL order = of.getOrder(orderID);
            orderPL = convertToOrderSL(order);
        } catch (IllegalArgumentException e) {
            System.out.println("Error getting order: " + e.getMessage());
        }
        return orderPL;
    }

    public List<OrderSL> getOrderHistory(int supplierID) {
        // validate input
        if (supplierID <= 0) {
            System.out.println("Invalid supplier ID: " + supplierID);
            return null;
        }
        // forward the request to the domain layer
        try {
            List<OrderDL> orderHistory = of.getOrderHistory(supplierID);
            List<OrderSL> orderPLs = new ArrayList<>();
            for (OrderDL order : orderHistory) {
                orderPLs.add(convertToOrderSL(order));
            }
            return orderPLs;
        } catch (IllegalArgumentException e) {
            System.out.println("Error getting order history: " + e.getMessage());
            return null;
        }
    }

    public OrderSL convertToOrderSL(OrderDL order) {
        List<OrderItemSL> orderItems = new ArrayList<>();
        for (OrderItemDL item : order.getOrderItems()) {
            OrderItemSL orderItemPL = new OrderItemSL(item.getItemID(), item.getQuantity(), item.getCatalogID(),
                    item.getTotalPrice());
            orderItems.add(orderItemPL);
        }
        return new OrderSL(order.getOrderID(), order.getSupplierID(), order.getOrderDate(), order.getDestination(),
                orderItems, order.getOrderStatus());
    }
}
