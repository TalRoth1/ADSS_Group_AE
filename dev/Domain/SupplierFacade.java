package Domain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Utils.DeliveryMethod;
import Utils.PaymentMethod;

public class SupplierFacade
{
    private List<SupplierDL> suppliers;
    private List<Item> items; // Will be saved in inventory after the merge
    private int nextId;
    
    public SupplierFacade() 
    {
        this.suppliers = new ArrayList<>();
        this.nextId = 0;
    }

    public void addSupplier(int companyID, int bankAccount, PaymentMethod paymentMethod, String contactEmail, String contactPhone, DeliveryMethod deliveryMethod, List<AgreementDL> agreements)
    {
        SupplierDL newSupplier = new SupplierDL(nextId++, companyID, bankAccount, paymentMethod, contactEmail, contactPhone, deliveryMethod, agreements);
        suppliers.add(newSupplier);
    }

    public SupplierDL getSupplier(int supplierID) 
    {
        for (SupplierDL supplier : suppliers) 
        {
            if (supplier.getSupplierID() == supplierID) 
            {
                return supplier;
            }
        }
        return null; // Supplier not found
    }

    public void addAgreement(int supplierID, List<String[]> billOfQuantities) 
    {
        SupplierDL supplier = getSupplier(supplierID);
        int agreementID = supplier.getNextAgreementID();
        List<DiscountDL> discounts = new ArrayList<>();
        for (String[] item : billOfQuantities) 
        {
            int itemID = Integer.parseInt(item[0]);
            int minimumQuantity = Integer.parseInt(item[1]);
            int discountPercentage = Integer.parseInt(item[2]);
            DiscountDL discount = new DiscountDL(itemID, minimumQuantity, discountPercentage);
            discounts.add(discount);
        }
        if (supplier != null) 
        {
            AgreementDL newAgreement = new AgreementDL(agreementID, discounts);
            supplier.addAgreement(newAgreement);
        }
    }

    public void changeAgreement(int supplierID, int agreementID, List<String[]> newBill) 
    {
        List<DiscountDL> newBoQ = new ArrayList<>();
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) 
        {
            for (AgreementDL agreement : supplier.getAgreements()) 
            {
                if (agreement.getAgreementID() == agreementID) 
                {
                    for (String[] item : newBill) 
                    {
                        int itemID = Integer.parseInt(item[0]);
                        int minimumQuantity = Integer.parseInt(item[1]);
                        int discountPercentage = Integer.parseInt(item[2]);
                        DiscountDL discount = new DiscountDL(itemID, minimumQuantity, discountPercentage);
                        newBoQ.add(discount);
                    }
                    agreement.setBillOfQuantities(newBoQ);
                    break;
                }
            }
        }
    }

    public void removeAgreement(int supplierID, int agreementID) 
    {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) 
        {
            supplier.removeAgreement(agreementID);
        }
    }

    public AgreementDL getAgreement(int supplierID,int agreementID) 
    {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) 
        {
            for (AgreementDL agreement : supplier.getAgreements()) 
            {
                if (agreement.getAgreementID() == agreementID) 
                {
                    return agreement;
                }
            }
        }
        return null; // Agreement not found
    }

    public Set<String> getSuppliedItems(int supplierID) 
    {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) 
        {
            Set<String> suppliedItems = new HashSet<>();
            for (AgreementDL agreement : supplier.getAgreements()) 
            {
                Map<Item, Integer> itemCatalog = agreement.getItemCatalog();
                for (Item item : itemCatalog.keySet()) 
                {
                    suppliedItems.add(item.getName());
                }
            }
            return suppliedItems;
        }
        return null; // Supplier not found
    }

    public Map<Integer, Integer> getSuppliedCatlogItems(int supplierID)
    {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) 
        {
            Map<Integer, Integer> suppliedItems = new HashMap<>();
            for (AgreementDL agreement : supplier.getAgreements()) 
            {
                Map<Item, Integer> itemCatalog = agreement.getItemCatalog();
                for (Map.Entry<Item, Integer> entry : itemCatalog.entrySet()) 
                {
                    suppliedItems.put(entry.getKey().getItemID(), entry.getValue());
                }
            }
            return suppliedItems;
        }
        return null; // Supplier not found
    }
}
