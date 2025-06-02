package Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Domain.OrderDL;
import Domain.OrderFacade;
import Domain.ProductBL;
import Domain.SupplierFacade;
import Utils.OrderStatus;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderFacadeTest {
    private OrderFacade orderFacade;
    private SupplierFacade supplierFacade;

    @BeforeEach
    public void setUp() {
        supplierFacade = SupplierFacade.getInstance();
        supplierFacade.loadData();
        orderFacade = OrderFacade.getInstance();
        orderFacade.loadData();
    }

    @Test
    void testCreateOrder_AddsOrderCorrectly() {
        int initialSize = orderFacade.getOrderHistory(1).size();

        orderFacade.createOrder(1, 1, Date.valueOf("2025-05-01"), "Test Branch", Arrays.asList(
                new int[]{1, 10},
                new int[]{2, 20}
        ));

        List<OrderDL> orderHistory = orderFacade.getOrderHistory(1);
        assertEquals(initialSize + 1, orderHistory.size());

        OrderDL createdOrder = orderHistory.get(orderHistory.size() - 1);
        assertEquals("Test Branch", createdOrder.getDestination());
        assertEquals(2, createdOrder.getOrderItems().size());
    }

    @Test
    void testChangeOrder_UpdatesOrderCorrectly() {
        OrderDL order = orderFacade.getOrder(0);
        assertNotNull(order);
        int oldAgreementID = order.getContractID();

        List<int[]> newItems = List.of(new int[]{1, 200});
        orderFacade.changeOrder(0, "New Branch", Date.valueOf("2025-05-01"), newItems);

        OrderDL updatedOrder = orderFacade.getOrder(0);
        assertEquals("New Branch", updatedOrder.getDestination());
        assertEquals(1, updatedOrder.getOrderItems().size());
        assertEquals(Date.valueOf("2025-05-01"), updatedOrder.getOrderDate());
        assertNotEquals(oldAgreementID, -1);
    }

    @Test
    void testChangeOrder_OrderNotFound_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderFacade.changeOrder(999, "Nowhere", Date.valueOf("2025-05-01"), List.of(new int[]{1, 50}));
        });
        assertEquals("Order not found: 999", exception.getMessage());
    }

    @Test
    void testCancelOrder_SetsOrderStatusToCancelled() {
        orderFacade.cancelOrder(0);
        OrderDL order = orderFacade.getOrder(0);
        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
    }

    @Test
    void testCancelOrder_OrderNotFound_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderFacade.cancelOrder(999);
        });
        assertEquals("Order not found: 999", exception.getMessage());
    }

    @Test
    void testGetOrder_ReturnsCorrectOrder() {
        OrderDL order = orderFacade.getOrder(1);
        assertNotNull(order);
        assertEquals(1, order.getOrderID());
    }

    @Test
    void testGetOrder_NotFound_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderFacade.getOrder(999);
        });
        assertEquals("Order not found: 999", exception.getMessage());
    }

    @Test
    void testGetOrderHistory_ReturnsCorrectOrders() {
        List<OrderDL> history = orderFacade.getOrderHistory(1);
        assertNotNull(history);
        System.out.println(history);
        assertFalse(history.isEmpty());
        assertTrue(history.stream().allMatch(o -> o.getSupplierID() == 1));
    }

    @Test
    public void testUpdateScheduledDeliveryItems_validInput() {
        List<int[]> newItems = Arrays.asList(new int[]{1, 50});
        assertDoesNotThrow(() ->
            orderFacade.updateScheduledDeliveryItems(1, 1, newItems)
        );
    }

    @Test
    public void testUpdateScheduledDeliveryItems_wrongDeliveryMethod() {
        List<int[]> newItems = Arrays.asList(new int[]{6, 20});
        Exception e = assertThrows(IllegalArgumentException.class, () ->
            orderFacade.updateScheduledDeliveryItems(2, 1, newItems)
        );
        assertTrue(e.getMessage().contains("Delivery method for contract is not periodic: "));
    }

    @Test
    public void testHandleLowSupply_createsOrderWhenStockIsLow() {
        List<int[]> newItems = Arrays.asList(new int[]{1, 30});
        orderFacade.updateScheduledDeliveryItems(1, 1, newItems);
        // Set up low stock for Carrot (ID 6)
        ProductBL product = new ProductBL(1, "Milk", 3.70, 10, 155, new String[]{"dairy", "beverages"});
        List<ProductBL> lowProducts = Arrays.asList(product);
        List<Integer> minQtys = Arrays.asList(100);
        List<Integer> currentQtys = Arrays.asList(50);
        List<String> destinations = Arrays.asList("Branch A");
        orderFacade.handleLowSupply(lowProducts, minQtys, currentQtys, destinations);
        // Expecting one more order to be added
        List<OrderDL> orderHistory = orderFacade.getOrderHistory(1);
        assertTrue(orderHistory.stream().anyMatch(order ->
            order.getDestination().equals("Branch A") &&
            order.getOrderItems().values().stream().anyMatch(item -> item.getItemID() == 1)
        ));
    }
}
