package Domain;

import java.util.List;

class AgreementDL
{
    private int agreementID;
    private int supplierID;
    private List<DiscountDL> billOfQuantities;

    public AgreementDL(int agreementID, int supplierID, List<DiscountDL> billOfQuantities)
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

    public List<DiscountDL> getBillOfQuantities()
    {
        return billOfQuantities;
    }

    public void setBillOfQuantities(List<DiscountDL> billOfQuantities)
    {
        this.billOfQuantities = billOfQuantities;
    }
}