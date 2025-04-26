package ServiceLayer;

import BusinessLayer.ProductFacade;
import BusinessLayer.ReportBL;

public class ReportService {

    public Response DeficiencyReport()
    {
        try
        {
            ProductFacade pf = ProductFacade.getInstance();
            ReportBL rep = pf.deficiencyReport();
            Response res = new Response(rep.getBody() + "\n" + "This report is valid for the date: " +  rep.getDateString(),null);
            return res;
        }
        catch (Exception e)
        {
            return new Response(null, e.getMessage());
        }
    }

    public Response SalesReport()
    {
        try
        {
            ProductFacade pf = ProductFacade.getInstance();
            ReportBL rep = pf.salesReport();
            Response res = new Response(rep.getBody() + "\n" + "This report is valid for the date: " +  rep.getDateString(),null);
            return res;
        }
        catch (Exception e)
        {
            return new Response(null, e.getMessage());
        }
    }

    public Response DefectedReport()
    {
        try
        {
            ProductFacade pf = ProductFacade.getInstance();
            ReportBL rep = pf.defectedReport();
            Response res = new Response(rep.getBody() + "\n" + "This report is valid for the date: " +  rep.getDateString(),null);
            return res;
        }
        catch (Exception e)
        {
            return new Response(null, e.getMessage());
        }
    }

    public Response ExpiredReport()
    {
        try
        {
            ProductFacade pf = ProductFacade.getInstance();
            ReportBL rep = pf.expiredReport();
            Response res = new Response(rep.getBody() + "\n" + "This report is valid for the date: " + rep.getDateString(),null);
            return res;
        }
        catch (Exception e)
        {
            return new Response(null, e.getMessage());
        }
    }
}
