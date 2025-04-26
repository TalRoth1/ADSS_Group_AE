import PresentationLayer.CLI;
import ServiceLayer.ServiceFactory;

import java.util.Calendar;
import java.util.Date;

public class Main
{
    public static void main(String[] args)
    {
        ServiceFactory SF = ServiceFactory.getFactory();
        try
        {
            //Fake Branches for system presentation only!
            int branch1 = Integer.parseInt(SF.getBranchService().AddBranch("Haifa", "Main street 1").getResponseValue());
            int branch2 = Integer.parseInt(SF.getBranchService().AddBranch("Beer Sheva", "Rager street 27").getResponseValue());
            int branch3 = Integer.parseInt(SF.getBranchService().AddBranch("Tel Aviv", "Unknown street 5").getResponseValue());

            //Fake product for system presentation only!
            int product1 = Integer.parseInt(SF.getProductService().AddProduct("Milk", 5, 10, 0, 1, new String[]{"Dairy"}).getResponseValue());
            int product2 = Integer.parseInt(SF.getProductService().AddProduct("Chocolate", 3, 5, 0, 2, new String[]{"Sweets"}).getResponseValue());
            int product3 = Integer.parseInt(SF.getProductService().AddProduct("Cheese", 34, 62, 0, 3, new String[]{"Dairy"}).getResponseValue());
            int product4 = Integer.parseInt(SF.getProductService().AddProduct("Cake", 24, 30, 0, 4, new String[]{"Sweets"}).getResponseValue());
            int product5 = Integer.parseInt(SF.getProductService().AddProduct("Water", 4, 6, 0, 5, new String[]{"Drinks"}).getResponseValue());

            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.MONTH, 6); // Add 6 months
            now = cal.getTime();

            //Fake items for system presentation only!
            // Add items for product1 (Milk)
            SF.getItemService().AddItem(product1, "Milk Bottle 1", false, now, branch1, new String[]{"Shelf A1"});
            SF.getItemService().AddItem(product1, "Milk Bottle 2", false, now, branch2, new String[]{"Shelf A2"});

            // Add items for product2 (Chocolate)
            SF.getItemService().AddItem(product2, "Chocolate Bar 1", false, now, branch1, new String[]{"Shelf B1"});
            SF.getItemService().AddItem(product2, "Chocolate Bar 2", false, now, branch3, new String[]{"Shelf B2"});

            // Add items for product3 (Cheese)
            SF.getItemService().AddItem(product3, "Cheese Slice 1", false, now, branch2, new String[]{"Shelf C1"});
            SF.getItemService().AddItem(product3, "Cheese Slice 2", false, now, branch3, new String[]{"Warehouse C2"});

            // Add items for product4 (Cake)
            SF.getItemService().AddItem(product4, "Cake Piece 1", false, now, branch1, new String[]{"Warehouse D1"});
            SF.getItemService().AddItem(product4, "Cake Piece 2", false, now, branch2, new String[]{"Shelf D2"});

            // Add items for product5 (Water)
            SF.getItemService().AddItem(product5, "Water Bottle 1", false, now, branch1, new String[]{"Warehouse E1"});
            SF.getItemService().AddItem(product5, "Water Bottle 2", false, now, branch3, new String[]{"Shelf E2"});
        }
        catch (Exception IGNORE){}


        //Start command line interface
        CLI commandLine = new CLI(SF);
        commandLine.openInterface();
    }
}
