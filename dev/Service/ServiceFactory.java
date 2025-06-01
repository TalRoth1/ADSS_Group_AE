package Service;

import java.util.Calendar;
import java.util.Date;

import Domain.BranchFacade;

public class ServiceFactory
{
    private BranchService BS;
    private ProductService PS;
    private ItemService IS;
    private ReportService RS;
    private static ServiceFactory instance = null;

    private ServiceFactory()
    {
        BS = new BranchService();
        PS = new ProductService();
        IS = new ItemService();
        RS = new ReportService();
    }

    public static ServiceFactory getFactory()
    {
        if(instance == null)
        {
            synchronized(ServiceFactory.class)
            {
                if(instance == null)
                {
                    ServiceFactory x = new ServiceFactory();
                    instance = x;
                }
            }
        }
        return instance;
    }

    public BranchService getBranchService()
    {
        return BS;
    }

    public ProductService getProductService()
    {
        return PS;
    }

    public ItemService getItemService()
    {
        return IS;
    }

    public ReportService getReportService()
    {
        return RS;
    }

    public void activateDemo() 
    {
        try
        {
            //Fake Branches for system presentation only!
            int branch1 = Integer.parseInt(BS.AddBranch("Haifa", "Main street 1").getResponseValue());
            int branch2 = Integer.parseInt(BS.AddBranch("Beer Sheva", "Rager street 27").getResponseValue());
            int branch3 = Integer.parseInt(BS.AddBranch("Tel Aviv", "Unknown street 5").getResponseValue());

            //Fake product for system presentation only!
            int product1 = Integer.parseInt(PS.AddProduct("Milk", 5, 0, 1, new String[]{"Dairy"}).getResponseValue());
            int product2 = Integer.parseInt(PS.AddProduct("Chocolate", 3, 0, 2, new String[]{"Sweets"}).getResponseValue());
            int product3 = Integer.parseInt(PS.AddProduct("Cheese", 34, 0, 3, new String[]{"Dairy"}).getResponseValue());
            int product4 = Integer.parseInt(PS.AddProduct("Cake", 24, 0, 4, new String[]{"Sweets"}).getResponseValue());
            int product5 = Integer.parseInt(PS.AddProduct("Water", 4, 0, 5, new String[]{"Drinks"}).getResponseValue());

            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.MONTH, 6); // Add 6 months
            Date sixMonthFromNow = cal.getTime();




            //Fake items for system presentation only!
            // Add items for product1 (Milk)
            IS.AddItem(product1, "Milk Bottle 1", false, now, branch1, new String[]{"Shelf A1"});
            IS.AddItem(product1, "Milk Bottle 2", false, sixMonthFromNow, branch2, new String[]{"Shelf A2"});

            // Add items for product2 (Chocolate)
            IS.AddItem(product2, "Chocolate Bar 1", false, sixMonthFromNow, branch1, new String[]{"Shelf B1"});
            IS.AddItem(product2, "Chocolate Bar 2", false, now, branch3, new String[]{"Shelf B2"});

            // Add items for product3 (Cheese)
            IS.AddItem(product3, "Cheese Slice 1", false, now, branch2, new String[]{"Shelf C1"});
            IS.AddItem(product3, "Cheese Slice 2", false, sixMonthFromNow, branch3, new String[]{"Warehouse C2"});

            // Add items for product4 (Cake)
            IS.AddItem(product4, "Cake Piece 1", false, sixMonthFromNow, branch1, new String[]{"Warehouse D1"});
            IS.AddItem(product4, "Cake Piece 2", false, now, branch2, new String[]{"Shelf D2"});

            // Add items for product5 (Water)
            IS.AddItem(product5, "Water Bottle 1", false, now, branch1, new String[]{"Warehouse E1"});
            IS.AddItem(product5, "Water Bottle 2", false, sixMonthFromNow, branch3, new String[]{"Shelf E2"});
        }
        catch (Exception IGNORE){}
    }
}