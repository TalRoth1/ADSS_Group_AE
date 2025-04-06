package Presentation;

import java.util.List;

public class AgreementPL
{
    private int agreementID;
    private int supplierID;
    private List<DiscountPL> billOfQuantities;

    public AgreementPL(int agreementID, int supplierID, List<DiscountPL> billOfQuantities)
    {
        this.agreementID = agreementID;
        this.supplierID = supplierID;
        this.billOfQuantities = billOfQuantities;
    }

    public int getAgreementID()
    {
        return agreementID;
    }

    public int getSupplierID()
    {
        return supplierID;
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
