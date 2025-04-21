package Presentation;

import java.util.List;

public class AgreementPL
{
    private int agreementID;
    private List<DiscountPL> billOfQuantities;

    public AgreementPL(int agreementID, List<DiscountPL> billOfQuantities)
    {
        this.agreementID = agreementID;
        this.billOfQuantities = billOfQuantities;
    }

    public int getAgreementID()
    {
        return agreementID;
    }

    public List<DiscountPL> getBillOfQuantities()
    {
        return billOfQuantities;
    }

    public void setBillOfQuantities(List<DiscountPL> billOfQuantities)
    {
        this.billOfQuantities = billOfQuantities;
    }
}
