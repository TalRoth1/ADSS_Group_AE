package DAL;

import java.util.List;
import java.util.Map;

import Domain.DeliveryMethod;

public class ContractDAO 
{
    private boolean isPersisted = false;
    private int contractID;
    private int supplierID;
    private List<Integer> items;
    private List<DiscountDAO> billOfQuantities;
    private DeliveryMethod deliveryMethod;
    private ContractController contractController;

    public ContractDAO(int contractID, int supplierID, List<Integer> items, List<DiscountDAO> billOfQuantities, DeliveryMethod deliveryMethod)
    {
        this.contractID = contractID;
        this.supplierID = supplierID;
        this.items = items;
        this.billOfQuantities = billOfQuantities;
        this.deliveryMethod = deliveryMethod;
        this.contractController = new ContractController();
    }

    public int getContractID() 
    {
        return contractID;
    }

    public int getSupplierID() 
    {
        return supplierID;
    }
    public List<Integer> getItemCatalogID() 
    {
        return items;
    }
    public List<DiscountDAO> getBillOfQuantities() 
    {
        return billOfQuantities;
    }
    public DeliveryMethod getDeliveryMethod() 
    {
        return deliveryMethod;
    }
    public void setDeliveryMethod(DeliveryMethod deliveryMethod) 
    {
        if (isPersisted) 
        {
            this.deliveryMethod = deliveryMethod;
        }
    }
    public void setItemCatalogID(List<Integer> items) 
    {
        if (isPersisted) 
        {
            this.items = items;
        }
    }
    public void setBillOfQuantities(List<DiscountDAO> billOfQuantities) 
    {
        if (isPersisted) 
        {
            this.billOfQuantities = billOfQuantities;
        }
    }

    public void persist()
    {
        isPersisted = true;
        contractController.insert(this);
    }
}
