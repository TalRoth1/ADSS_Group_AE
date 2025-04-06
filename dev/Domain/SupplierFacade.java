package Domain;
import Presentation.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierFacade
{
    private List<SupplierDL> suppliers;
    private int nextId;
    
    public SupplierFacade() 
    {
        this.suppliers = new ArrayList<>();
        this.nextId = 0;
    }

    public void addSupplier(SupplierPL supplier) 
    {
        SupplierDL newSupplier = new SupplierDL(nextId++, supplier.getCompanyID(), supplier.getBankAccount(), supplier.getPaymentMethod(), supplier.getContactMail(), supplier.getContactPhone(), supplier.getDeliveryMethod(), supplier.getSuppliedItems());
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
}
