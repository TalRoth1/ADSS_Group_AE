
import Domain.OrderFacade;
import Domain.SupplierFacade;
import Presentation.MainCLI;
import Service.ServiceFactory;

public class Main {
    public static void main(String[] args) {
        SupplierFacade sf = SupplierFacade.getInstance();
        OrderFacade of = OrderFacade.getInstance();
        ServiceFactory invSF = ServiceFactory.getFactory();
        MainCLI mainCli = new MainCLI(of, sf, invSF);
        sf.clearData();
        mainCli.run();
    }
}
