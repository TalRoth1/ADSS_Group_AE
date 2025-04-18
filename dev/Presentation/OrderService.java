package Presentation;

import java.util.List;

import Domain.OrderFacade;

public class OrderService {
    private OrderFacade of;
    
    public OrderService() {
        of = new OrderFacade();
    }

    public void createOrder(){

    }

    public void changeOrder(){

    }
    public void cancelOrder(){

    }
    public OrderPL getOrder(int orderID){
        return null;
    }
    public List<OrderPL> getOrderHistory(int supplierID){
        return null;
    }

}
