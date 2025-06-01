package Domain;

import java.util.List;

public class PeriodicDelivery implements DeliveryMethod{
    private int deliveryInterval;
    private List<PeriodicItem> orderItems;
    
    public PeriodicDelivery(int deliveryInterval, List<PeriodicItem> orderItems) {
        this.deliveryInterval = deliveryInterval;
        this.orderItems = orderItems;
    }
    public int getDeliveryInterval() {
        return deliveryInterval;
    }
    public void setDeliveryInterval(int deliveryInterval) {
        this.deliveryInterval = deliveryInterval;
    }
    public List<PeriodicItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<PeriodicItem> orderItems) {
        this.orderItems = orderItems;
    }
    public String toString() {
        return "Periodic Delivery";
    }
}
