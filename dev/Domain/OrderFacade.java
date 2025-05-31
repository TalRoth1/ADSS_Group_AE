package Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DAL.OrderController;
import DAL.OrderDAO;
import DAL.OrderItemDAO;
import Utils.OrderStatus;

public class OrderFacade {

    private final List<OrderDL> orders;
    private final SupplierFacade sf;
    private final OrderController orderController;
    private int nextID = 0;

    public OrderFacade(SupplierFacade sf) {
        this.sf = sf;
        this.orderController = new OrderController();
        this.orders = new ArrayList<>();
        List<OrderDAO> orderDAOs = orderController.getAllOrders();
        for (OrderDAO orderDAO : orderDAOs) {
            OrderDL order = new OrderDL(orderDAO);
            if (order.getOrderID() >= nextID) {
                nextID = order.getOrderID() + 1;
            }
            List<OrderItemDAO> itemDAOs = orderController.getOrderItems(order.getOrderID());
            Map<Integer,OrderItemDL> items = new HashMap<>();
            for (OrderItemDAO itemDAO : itemDAOs) {
                OrderItemDL item = new OrderItemDL(itemDAO);
                items.putIfAbsent(item.getItemID(), item);
            }
            order.setOrderItems(items);
            orders.add(order);
        }
    }

    public void createOrder(int supplierID, int contractID, Date orderDate, String destination, List<int[]> Orders) {
        if(!verifySupplier(supplierID)) {
            System.out.println("Can't create order, supplier not found");
            return;
        }
        if(!verifycontract(supplierID, contractID)) {
            System.out.println("Can't create order, contract not found");
            return;
        }
        Map<Integer,OrderItemDL> items = makeItemsFromArray(Orders);
        OrderDL newOrder = new OrderDL(nextID++, supplierID, supplierID, orderDate, destination, items, this.orderController);
        orderController.insertOrder(newOrder.getDao());
        orders.add(newOrder);
    }

    public void changeOrder(int orderID, String destination, Date newDate, List<int[]> newItems) throws IllegalArgumentException {
        try{
            OrderDL order = getOrder(orderID);
            if(order.getOrderStatus() != OrderStatus.CANCELLED) {
                throw new IllegalArgumentException("Cannot change items of a " + order.getOrderStatus().toString().toLowerCase() + "order.");
            }
            if (!order.getDestination().equals(destination)) {
                changeOrderDestination(orderID, destination);
            }
            if (!order.getOrderDate().equals(newDate)) {
                changeOrderDate(orderID, newDate);
            }
            Map<Integer,OrderItemDL> items = makeItemsFromArray(newItems);
            if (!order.getOrderItems().equals(items)){
                changeOrderItems(orderID, items);
            }
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order not found: " + orderID);
        }
    }

    private void changeOrderDestination(int orderID, String destination) throws IllegalArgumentException {
        try {
            OrderDL order = getOrder(orderID);
            order.setDestination(destination);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order not found: " + orderID);
        }
    }

    private void changeOrderDate(int orderID, Date orderDate) throws IllegalArgumentException {
        try {
            OrderDL order = getOrder(orderID);
            orderController.updateOrder(orderID, "orderDate", orderDate.toString());
            order.setOrderDate(orderDate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order not found: " + orderID);
        }
    }

    private void changeOrderItems(int orderID, Map<Integer,OrderItemDL> newItems) throws IllegalArgumentException {
        try { 
            OrderDL order = getOrder(orderID);
            order.setOrderItems(newItems);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order not found: " + orderID);
        }
    }

    public void cancelOrder(int orderID) throws IllegalArgumentException {
        for (OrderDL order : orders) {
            if (order.getOrderID() == orderID) {
                order.setOrderStatus(OrderStatus.CANCELLED);
                return;
            }
        }
        throw new IllegalArgumentException("Order not found: " + orderID);
    }

    public OrderDL getOrder(int orderID) throws IllegalArgumentException {
        for (OrderDL order : orders) {
            if (order.getOrderID() == orderID) {
                return order;
            }
        }
        throw new IllegalArgumentException("Order not found: " + orderID);
    }

    public List<OrderDL> getOrderHistory(int supplierID) {
        List<OrderDL> orderHistory = new ArrayList<>();
        for (OrderDL order : orders) {
            if (order.getSupplierID() == supplierID) {
                orderHistory.add(order);
            }
        }
        return orderHistory;
    }

    
    public void updateScheduledDeliveryItems(int supplierID, int contractID, List<int[]> newItems) throws IllegalArgumentException {
        SupplierDL supplier = sf.getSupplier(supplierID);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found: " + supplierID);
        }
        ContractDL contract = supplier.getContract(contractID);
        if (contract == null) {
            throw new IllegalArgumentException("contract not found: " + contractID);
        }
        if( !contract.getDeliveryMethod().toString().equals("Periodic Delivery") ) {
            throw new IllegalArgumentException("Delivery method for contract is not periodic: " + contractID);
        }
        else{
            PeriodicDelivery periodicDelivery = (PeriodicDelivery) contract.getDeliveryMethod();
            if(periodicDelivery.getOrderItems() == null) {
                periodicDelivery.setOrderItems(new ArrayList<>());
            }
            List<OrderItemDL> items = new ArrayList<>();
            try{
                for (int[] item : newItems) {
                    int itemID = item[0];
                    int quantity = item[1];
                    int catalogID = getCatalogID(itemID, supplierID, contractID);
                    double totalPrice = calculateTotalPrice(quantity, catalogID, supplierID, contractID);
                    OrderItemDL newItem = new OrderItemDL(-1, itemID, quantity, catalogID, totalPrice, this.orderController);
                    items.add(newItem);
                }
                periodicDelivery.setOrderItems(items);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Error updating scheduled delivery items: " + e.getMessage());
            }
        }
    }

    private boolean verifySupplier(int supplierID){
        return sf.getSupplier(supplierID) != null;
    }

    private boolean verifycontract(int supplierID, int contractID){
        return sf.getContract(supplierID, contractID) != null;
    }

    private double calculateTotalPrice(int quantity, int catalogID, int supplierID, int contractID) {
        double price = quantity * sf.getContract(supplierID, contractID).getItem(catalogID).getSellingPrice();
        DiscountDL discount = sf.getContract(supplierID, contractID).getDiscount(catalogID);
        if (discount != null) {
            if (discount.getMinimumQuantity() <= quantity) {
                price -= price * discount.getDiscountPercentage() / 100;
            }
        }
        return price;
    }

    private int getCatalogID(int itemID, int supplierID, int contractID) {
        return sf.getContract(supplierID, contractID).getItemCatalogID(itemID);
    }

    private Map<Integer,OrderItemDL> makeItemsFromArray(List<int[]> items) {
        Map<Integer,OrderItemDL> itemMap = new HashMap<>();
        for (int[] item : items) {
            int itemID = item[0];
            int quantity = item[1];
            int catalogID = getCatalogID(itemID, 1, 1); // Assuming supplierID and contractID are 1 for this example
            double totalPrice = calculateTotalPrice(quantity, catalogID, 1, 1);
            OrderItemDL orderItem = new OrderItemDL(nextID++, itemID, quantity, catalogID, totalPrice, this.orderController);
            itemMap.put(itemID, orderItem);
        }
        return itemMap;
    }

    public void loadData() {
        createOrder(1, 1, java.sql.Date.valueOf("2025-04-10"), "Central Warehouse", Arrays.asList(
                new int[] { 1, 120 },
                new int[] { 2, 160 }));

        createOrder(1, 1, java.sql.Date.valueOf("2025-04-15"), "Branch A", List.of(
                new int[]{1, 50}));

        createOrder(1, 1, java.sql.Date.valueOf("2025-04-20"), "Branch B", List.of(
                new int[]{2, 90}));

        createOrder(1, 1, java.sql.Date.valueOf("2025-04-22"), "Branch C", List.of(
                new int[]{2, 80}));

        createOrder(2, 1, java.sql.Date.valueOf("2025-04-11"), "Central Warehouse", List.of(
                new int[]{6, 220}));

        createOrder(2, 1, java.sql.Date.valueOf("2025-04-16"), "Branch A", List.of(
                new int[]{6, 100}));

        createOrder(2, 1, java.sql.Date.valueOf("2025-04-18"), "Branch B", List.of(
                new int[]{6, 80}));

        createOrder(2, 1, java.sql.Date.valueOf("2025-04-21"), "Branch C", List.of(
                new int[]{6, 90}));

        createOrder(3, 1, java.sql.Date.valueOf("2025-04-12"), "Central Warehouse", List.of(
                new int[]{7, 150}));

        createOrder(3, 2, java.sql.Date.valueOf("2025-04-14"), "Branch A", List.of(
                new int[]{10, 100}));

        createOrder(3, 2, java.sql.Date.valueOf("2025-04-17"), "Branch B", List.of(
                new int[]{10, 120}));

        createOrder(3, 1, java.sql.Date.valueOf("2025-04-23"), "Branch C", List.of(
                new int[]{7, 100}));

    }
}
