import Domain.OrderFacade;
import Domain.SupplierFacade;
import Presentation.CLI;

public class Main
{
    public static void main(String[] args)
    {
        SupplierFacade sf = new SupplierFacade();
        OrderFacade of = new OrderFacade(sf);
        CLI cli = new CLI(sf, of);
        cli.run();
    }
}
