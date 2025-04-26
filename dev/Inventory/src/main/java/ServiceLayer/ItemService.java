package ServiceLayer;

import BusinessLayer.ItemFacade;
import java.util.Date;

public class ItemService {
    private ItemFacade ifa;

    public ItemService() {
        ifa = ItemFacade.getInstance();
    }

    public Response AddItem(int productid, String name, boolean isDef, Date expirationDate,
                            int branchID, String[] location) {
        try {
            int itemID = ifa.addItem(productid, name, isDef, expirationDate, branchID, location);
            return new Response(String.valueOf(itemID), null);
        } catch (Exception e) {
            return new Response(null, e.getMessage());
        }
    }

    public Response RemoveItem(int itemID) {
        try {
            ifa.removeItem(itemID);
            return new Response("Item removed successfully", null);
        } catch (Exception e) {
            return new Response(null, e.getMessage());
        }
    }

    public Response PurchaseItem(int itemID) {
        try {
            ifa.purchaseItem(itemID);
            return new Response("Item purchased successfully", null);
        } catch (Exception e) {
            return new Response(null, e.getMessage());
        }
    }

    public Response UpdateItem(int itemID, String name, boolean isDef, int branchID, String[] location) {
        try {
            ifa.updateItem(itemID, name, isDef, branchID, location);
            return new Response("Item updated successfully", null);
        } catch (Exception e) {
            return new Response(null, e.getMessage());
        }
    }
}
