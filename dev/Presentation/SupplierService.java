package Presentation;

import Domain.SupplierFacade;

public class SupplierService
{
    private SupplierFacade sf;

    public SupplierService()
    {
        sf = new SupplierFacade();
    }

    public void addSupplier(String name, String address, String phoneNumber, String email)
    {
        
        sf.addSupplier(name, address, phoneNumber, email);
    }
}
