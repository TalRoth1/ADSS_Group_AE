package Domain;
import Utils.DeliveryMethod;
import Utils.PaymentMethod;

import java.util.List;

public class SupplierDL
{
    private int supplierID;
    private int companyID;
    private int banckAccount;
    private PaymentMethod paymentMethod;
    private String contactMail;
    private String contactPhone;
    private DeliveryMethod deliveryMethod;
    private List<Item> suppliedItems;
    private List<AgreementDL> agreements;

    public SupplierDL(int supplierID, int companyID, int banckAccount, PaymentMethod paymentMethod, String contactMail, String contactPhone, DeliveryMethod deliveryMethod, List<Item> suppliedItems, List<AgreementDL> agreements)
    {
        this.supplierID = supplierID;
        this.companyID = companyID;
        this.banckAccount = banckAccount;
        this.paymentMethod = paymentMethod;
        this.contactMail = contactMail;
        this.contactPhone = contactPhone;
        this.deliveryMethod = deliveryMethod;
        this.suppliedItems = suppliedItems;
        this.agreements = agreements;
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

    public void setBanckAccount(int banckAccount) 
    {
        this.banckAccount = banckAccount;
    }

    public int getBanckAccount() 
    {
        return banckAccount;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) 
    {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod()
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

    public void addAgreement(AgreementDL agreement) 
    {
        this.agreements.add(agreement);
    }

    public void removeAgreement(int agreementID) 
    {
        for (AgreementDL agreement : this.agreements) {
            if (agreement.getAgreementID() == agreementID) {
                this.agreements.remove(agreement);
                break;
            }
        }
    }

    public void setAgreements(List<AgreementDL> agreements) 
    {
        this.agreements = agreements;
    }

    public List<AgreementDL> getAgreements() 
    {
        return agreements;
    }
}