package DAL;

import java.util.List;
import java.util.Map;

import Domain.DeliveryMethod;

public class ContractDAO 
{
    private boolean isPersisted = false;
    private int contractID;
    private int supplierID;
    private Map<Integer, Integer> itemCatalog;
    private List<DiscountDAO> billOfQuantities;
    private DeliveryMethod deliveryMethod;
    private ContractController contractController;

    public ContractDAO(int contractID, int supplierID, Map<Integer, Integer> items, List<DiscountDAO> billOfQuantities, DeliveryMethod deliveryMethod)
    {
        this.contractID = contractID;
        this.supplierID = supplierID;
        this.itemCatalog = items;
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
    public Map<Integer, Integer> getItemCatalog() 
    {
        return itemCatalog;
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
    public void setItemCatalog(Map<Integer, Integer> itemCatalog) 
    {
        if (isPersisted) 
        {
            this.itemCatalog = itemCatalog;
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
