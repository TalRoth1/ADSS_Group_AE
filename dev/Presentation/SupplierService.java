package Presentation;

import java.util.List;

import Domain.Item;
import Domain.SupplierFacade;
import Utils.DeliveryMethod;

public class SupplierService
{
    private SupplierFacade sf;

    public SupplierService()
    {
        sf = new SupplierFacade();
    }

    public void addSupplier(int supplierID, int companyID, int bankAccount, int paymentMethod, String contactMail, String contactPhone, DeliveryMethod deliveryMethod, List<Item> suppliedItems)
    {
        SupplierPL supplier = new SupplierPL(supplierID, companyID, bankAccount, paymentMethod, contactMail, contactPhone, deliveryMethod, suppliedItems);
        sf.addSupplier(supplier);
    }
}
