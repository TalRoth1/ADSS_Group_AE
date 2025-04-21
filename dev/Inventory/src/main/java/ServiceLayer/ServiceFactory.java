package ServiceLayer;

import BusinessLayer.BranchFacade;

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
}