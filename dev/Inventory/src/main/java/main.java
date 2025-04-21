import PresentationLayer.CLI;
import ServiceLayer.ServiceFactory;

public class main
{
    public static void main(String[] args)
    {
        ServiceFactory SF = ServiceFactory.getFactory();
        CLI commandLine = new CLI(SF);
        commandLine.openInterface();
    }
}
