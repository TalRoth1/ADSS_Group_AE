package Domain;

import java.util.List;
import java.util.Map;

public class AgreementDL
{
    private int agreementID;
    private Map<Item, Integer> itemCatalog;
    private List<DiscountDL> billOfQuantities;

    public AgreementDL(int agreementID, List<DiscountDL> billOfQuantities)
    {
        this.agreementID = agreementID;
        this.billOfQuantities = billOfQuantities;
    }

    public int getAgreementID()
    {
        return agreementID;
    }

    public List<DiscountDL> getBillOfQuantities()
    {
        return billOfQuantities;
    }

    public void setBillOfQuantities(List<DiscountDL> billOfQuantities)
    {
        this.billOfQuantities = billOfQuantities;
    }
}