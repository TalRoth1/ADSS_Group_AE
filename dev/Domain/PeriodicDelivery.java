package Domain;

import java.util.List;

public class PeriodicDelivery implements DeliveryMethod{
    private int day;
    private List<PeriodicItem> orderItems;

    public PeriodicDelivery(int day, List<PeriodicItem> orderItems) {
        if (day < 1 || day > 7) {
            throw new IllegalArgumentException("Day must be between 1 and 7");
        }
        this.day = day;
        this.orderItems = orderItems;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        if (day < 1 || day > 7) {
            throw new IllegalArgumentException("Day must be between 1 and 7");
        }
        this.day = day;
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
