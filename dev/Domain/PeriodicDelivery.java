package Domain;

import java.util.List;

public class PeriodicDelivery implements DeliveryMethod{
    private int deliveryInterval;
    private List<OrderItemDL> orderItems;
    
    public PeriodicDelivery(int deliveryInterval, List<OrderItemDL> orderItems) {
        this.deliveryInterval = deliveryInterval;
        this.orderItems = orderItems;
    }
    public int getDeliveryInterval() {
        return deliveryInterval;
    }
    public void setDeliveryInterval(int deliveryInterval) {
        this.deliveryInterval = deliveryInterval;
    }
    public List<OrderItemDL> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItemDL> orderItems) {
        this.orderItems = orderItems;
    }
}
