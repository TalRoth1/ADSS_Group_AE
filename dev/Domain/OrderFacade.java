package Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import DAL.OrderController;
import DTOs.OrderDTO;
import DTOs.OrderItemDTO;
import Utils.OrderStatus;

public class OrderFacade {

    private final List<OrderDL> orders;
    private final SupplierFacade sf;
    private final OrderController oc;
    private int nextID = 0;

    public OrderFacade(SupplierFacade sf) {
        this.sf = sf;
        this.oc = new OrderController();
        this.orders = new ArrayList<>();
    }

    public void createOrder(OrderDTO orderDTO) {
        List<OrderItemDL> items = new ArrayList<>();
        if(!verifySupplier(orderDTO.getSupplierID())) {
            System.out.println("Can't create order, supplier not found");
            return;
        }
        if(!verifycontract(orderDTO.getSupplierID(), orderDTO.getContractID())) {
            System.out.println("Can't create order, contract not found");
            return;
        }
        for (OrderItemDTO order : orderDTO.getOrderItems()) {
            int itemID = order.getItemID();
            int quantity = order.getQuantity();
            int catalogID = sf.getContract(orderDTO.getSupplierID(), orderDTO.getContractID()).getItemCatalogID(itemID);
            double totalPrice = calculateTotalPrice(quantity, catalogID, orderDTO.getSupplierID(), orderDTO.getContractID());
            OrderItemDL item = new OrderItemDL(nextID, itemID, quantity, catalogID, totalPrice, this.oc);
            items.add(item);
        }
        OrderDL newOrder = new OrderDL(nextID++, orderDTO.getSupplierID(), orderDTO.getSupplierID(), orderDTO.getOrderDate(), orderDTO.getDestination(), items, this.oc);
        oc.insertOrder(newOrder.getDao());
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
            List<OrderItemDL> items = new ArrayList<>();
            for (int[] item : newItems) {
                int itemID = item[0];
                int quantity = item[1];
                int catalogID = getCatalogID(itemID, order.getSupplierID(), order.getContractID());
                double totalPrice = calculateTotalPrice(quantity, catalogID, order.getSupplierID(), order.getContractID());
                OrderItemDL newItem = new OrderItemDL(orderID, itemID, quantity, catalogID, totalPrice, this.oc);
                items.add(newItem);
            }
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
            oc.updateOrder(orderID, "destination", destination);
            order.setDestination(destination);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order not found: " + orderID);
        }
    }

    private void changeOrderDate(int orderID, Date orderDate) throws IllegalArgumentException {
        try {
            OrderDL order = getOrder(orderID);
            oc.updateOrder(orderID, "orderDate", orderDate.toString());
            order.setOrderDate(orderDate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order not found: " + orderID);
        }
    }

    private void changeOrderItems(int orderID, List<OrderItemDL> newItems) throws IllegalArgumentException {
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

    
    // public void updateScheduledDeliveryItems(int supplierID, int contractID, List<int[]> newItems) throws IllegalArgumentException {
    //     SupplierDL supplier = sf.getSupplier(supplierID);
    //     if (supplier == null) {
    //         throw new IllegalArgumentException("Supplier not found: " + supplierID);
    //     }
    //     contractDL contract = supplier.getcontract(contractID);
    //     if (contract == null) {
    //         throw new IllegalArgumentException("contract not found: " + contractID);
    //     }
    //     List<OrderItemDL> items = new ArrayList<>();
    //     try{
    //         for (int[] item : newItems) {
    //             int itemID = item[0];
    //             int quantity = item[1];
    //             int catalogID = getCatalogID(itemID, supplierID, contractID);
    //             double totalPrice = calculateTotalPrice(quantity, catalogID, supplierID, contractID);
    //             OrderItemDL newItem = new OrderItemDL(itemID, quantity, catalogID, totalPrice);
    //             items.add(newItem);
    //         }
    //         contract.getDeliveryMethod().setItems(items);
    //     } catch (IllegalArgumentException e) {
    //         throw new IllegalArgumentException("Error updating scheduled delivery items: " + e.getMessage());
    //     }
        
    // }

    private boolean verifySupplier(int supplierID){
        return sf.getSupplier(supplierID) != null;
    }

    private boolean verifycontract(int supplierID, int contractID){
        return sf.getContract(supplierID, contractID) != null;
    }

    private double calculateTotalPrice(int quantity, int catalogID, int supplierID, int contractID) {
        double price = quantity * sf.getContract(supplierID, contractID).getItem(catalogID).getPrice();
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

    // public void loadData() {
    //     createOrder(1, "Central Warehouse", 1, java.sql.Date.valueOf("2025-04-10"), Arrays.asList(
    //             new int[] { 1, 120 },
    //             new int[] { 2, 160 }));

    //     createOrder(1, "Branch A", 1, java.sql.Date.valueOf("2025-04-15"), List.of(
    //             new int[]{1, 50}));

    //     createOrder(1, "Branch B", 1, java.sql.Date.valueOf("2025-04-20"), List.of(
    //             new int[]{2, 90}));

    //     createOrder(1, "Branch C", 1, java.sql.Date.valueOf("2025-04-22"), List.of(
    //             new int[]{2, 80}));

    //     createOrder(2, "Central Warehouse", 1, java.sql.Date.valueOf("2025-04-11"), List.of(
    //             new int[]{6, 220}));

    //     createOrder(2, "Branch A", 1, java.sql.Date.valueOf("2025-04-16"), List.of(
    //             new int[]{6, 100}));

    //     createOrder(2, "Branch B", 1, java.sql.Date.valueOf("2025-04-18"), List.of(
    //             new int[]{6, 80}));

    //     createOrder(2, "Branch C", 1, java.sql.Date.valueOf("2025-04-21"), List.of(
    //             new int[]{6, 90}));

    //     createOrder(3, "Central Warehouse", 1, java.sql.Date.valueOf("2025-04-12"), List.of(
    //             new int[]{7, 150}));

    //     createOrder(3, "Branch A", 2, java.sql.Date.valueOf("2025-04-14"), List.of(
    //             new int[]{10, 100}));

    //     createOrder(3, "Branch B", 2, java.sql.Date.valueOf("2025-04-17"), List.of(
    //             new int[]{10, 120}));

    //     createOrder(3, "Branch C", 1, java.sql.Date.valueOf("2025-04-23"), List.of(
    //             new int[]{7, 100}));

    // }
}
