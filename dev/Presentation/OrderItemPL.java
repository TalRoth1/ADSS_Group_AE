package Presentation;

public class OrderItemPL
{
    private final int itemID;
    private int quantity;
    private final int catalogID;
    private float totalPrice;
    
    OrderItemPL(int itemID, int quantity, int catalogID, float totalPrice){
        this.itemID = itemID;
        this.quantity = quantity;
        this.catalogID = catalogID;
        this.totalPrice = totalPrice;
    }

    public int getItemID(){
        return itemID;
    }

    public int getQuantity(){
        return quantity;
    }

    public int getCatalogID(){
        return catalogID;
    }

    public float getTotalPrice(){
        return totalPrice;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setTotalPrice(float totalPrice){
        this.totalPrice = totalPrice;
    }
}
