package Domain;
import Presentation.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierFacade
{
    private List<SupplierDL> suppliers;
    private List<AgreementDL> agreements;
    private int nextId;
    
    public SupplierFacade() 
    {
        this.suppliers = new ArrayList<>();
        this.nextId = 0;
    }

    public void addSupplier(SupplierPL supplier) 
    {
        SupplierDL newSupplier = new SupplierDL(nextId++, supplier.getCompanyID(), supplier.getBankAccount(), supplier.getPaymentMethod(), supplier.getContactMail(), supplier.getContactPhone(), supplier.getDeliveryMethod(), supplier.getSuppliedItems(), null);
        List<AgreementDL> agreements = new ArrayList<>();
        for (AgreementPL agreement : supplier.getAgreements()) 
        {
            List<DiscountDL> discounts = new ArrayList<>();
            for (DiscountPL discount : agreement.getBillOfQuantities()) 
            {
                DiscountDL newDiscount = new DiscountDL(discount.getItemID(), discount.getMinimumQuantity(), discount.getDiscountPercentage());
                discounts.add(newDiscount);
            }
            AgreementDL newAgreement = new AgreementDL(agreement.getAgreementID(), discounts);
            agreements.add(newAgreement);
        }
        newSupplier.setAgreements(agreements);
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

    public void addAgreement(AgreementPL agreement) 
    {
        List<DiscountDL> discounts = new ArrayList<>();
        for (DiscountPL discount : agreement.getBillOfQuantities()) 
        {
            DiscountDL newDiscount = new DiscountDL(discount.getItemID(), discount.getMinimumQuantity(), discount.getDiscountPercentage());
            discounts.add(newDiscount);
        }
        AgreementDL newAgreement = new AgreementDL(agreement.getAgreementID(), discounts);
        agreements.add(newAgreement);
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

    public List<Item> getSuppliedItems(int supplierID) 
    {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) 
        {
            return supplier.getSuppliedItems();
        }
        return null; // Supplier not found
    }
}
