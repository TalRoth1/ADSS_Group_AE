package Service;
import Utils.DeliveryMethod;

import java.util.ArrayList;
import java.util.List;

import Domain.Item;

public class SupplierSL
{
    private int supplierID;
    private int companyID;
    private int bankAccount;
    private int paymentMethod;
    private String contactMail;
    private String contactPhone;
    private DeliveryMethod deliveryMethod;
    private List<Item> suppliedItems;
    private List<AgreementSL> agreements;


    public SupplierSL(int supplierID, int companyID, int bankAccount, int paymentMethod, String contactMail, String contactPhone, DeliveryMethod deliveryMethod, List<Item> suppliedItems, AgreementSL agreement)
    {
        this.supplierID = supplierID;
        this.companyID = companyID;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.contactMail = contactMail;
        this.contactPhone = contactPhone;
        this.deliveryMethod = deliveryMethod;
        this.suppliedItems = suppliedItems;
        this.agreements = new ArrayList<>();
        this.agreements.add(agreement);
    }

    public int getSupplierID() 
    {
        return supplierID;
    }

    public void setCompanyID(int companyID) 
    {
        this.companyID = companyID;
    }

    public int getCompanyID() 
    {
        return companyID;
    }

    public void setBankAccount(int bankAccount) 
    {
        this.bankAccount = bankAccount;
    }

    public int getBankAccount() 
    {
        return bankAccount;
    }

    public void setPaymentMethod(int paymentMethod) 
    {
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentMethod() 
    {
        return paymentMethod;
    }

    public void setContactMail(String contactMail) 
    {
        this.contactMail = contactMail;
    }

    public String getContactMail() 
    {
        return contactMail;
    }

    public void setContactPhone(String contactPhone) 
    {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) 
    {
        this.deliveryMethod = deliveryMethod;
    }

    public DeliveryMethod getDeliveryMethod() 
    {
        return deliveryMethod;
    }

    public void setSuppliedItems(List<Item> suppliedItems) 
    {
        this.suppliedItems = suppliedItems;
    }

    public List<Item> getSuppliedItems() 
    {
        return suppliedItems;
    }

    public void addSuppliedItem(Item item) 
    {
        this.suppliedItems.add(item);
    }

    public void removeSuppliedItem(Item item) 
    {
        this.suppliedItems.remove(item);
    }

    public void setAgreements(List<AgreementSL> agreements) 
    {
        this.agreements = agreements;
    }
    public List<AgreementSL> getAgreements() 
    {
        return agreements;
    }
}
