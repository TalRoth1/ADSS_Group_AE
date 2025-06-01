package Domain;

import DAL.OrderItemDAO;

public class OrderItemDL 
{
    private int itemID;
    private int quantity;
    private int catalogID;
    private double totalPrice;
    private OrderItemDAO DAO;
    
    public OrderItemDL(int orderID, int itemID, int quantity, int catalogID, double totalPrice)
    {
        this.itemID = itemID;
        this.quantity = quantity;
        this.catalogID = catalogID;
        this.totalPrice = totalPrice;
        this.DAO = new OrderItemDAO(orderID, itemID, quantity, catalogID, totalPrice);
    }
    
    public OrderItemDL(OrderItemDAO orderItemDAO)
    {
        this.itemID = orderItemDAO.getItemID();
        this.quantity = orderItemDAO.getQuantity();
        this.catalogID = orderItemDAO.getCatalogID();
        this.totalPrice = orderItemDAO.getTotalPrice();
        this.DAO = orderItemDAO;
    }

    public int getItemID()
    {
        return itemID;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setCatalogID(int catalogID)
    {
        this.catalogID = catalogID;
    }

    public int getCatalogID()
    {
        return catalogID;
    }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }
    
    public OrderItemDAO getDAO()
    {
        return DAO;
    }

    public String toString()
    {
        return "{itemID=" + itemID + ", quantity=" + quantity + ", catalogID=" + catalogID + ", totalPrice="
                + totalPrice + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderItemDL)) return false;
        OrderItemDL other = (OrderItemDL) obj;
        return itemID == other.itemID && quantity == other.quantity && catalogID == other.catalogID && Double.compare(other.totalPrice, totalPrice) == 0;
    }
}
