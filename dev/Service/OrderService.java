package Service;

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
    public OrderSL getOrder(int orderID){
        return null;
    }
    public List<OrderSL> getOrderHistory(int supplierID){
        return null;
    }

}
