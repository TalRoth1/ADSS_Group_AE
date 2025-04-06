package Domain;

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
        SupplierDL supplier = new SupplierDL(nextId++, name, address, phoneNumber, email);
        suppliers.add(supplier);
    }
}
