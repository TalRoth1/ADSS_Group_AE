package DAL;

public class SupplierDAO 
{
    private boolean isPersisted = false;
    private int id;
    private int companyID;
    private int bankAccount;
    private String paymentMethod;
    private String contactMail;
    private String contactPhone;

    private SupplierController supplierController;

    public SupplierDAO(int id, int companyID, int bankAccount, String paymentMethod, String contactMail, String contactPhone, SupplierController supplierController) {
        this.id = id;
        this.companyID = companyID;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.contactMail = contactMail;
        this.contactPhone = contactPhone;
        this.supplierController = supplierController;
    }

    public int getId()
    {
        return id;
    }

    public int getCompanyID() 
    {
        return companyID;
    }

    public void setCompanyID(int companyID) 
    {
        if(isPersisted)
        {
            this.companyID = companyID;
            supplierController.Update(id, "companyID", String.valueOf(companyID));
        }
    }

    public int getBankAccount() 
    {
        return bankAccount;
    }
    
    public void setBankAccount(int bankAccount) 
    {
        if(isPersisted)
        {
            this.bankAccount = bankAccount;
            supplierController.Update(id, "bankAccount", String.valueOf(bankAccount));
        }
    }

    public String getPaymentMethod() 
    {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) 
    {
        if(isPersisted)
        {
            this.paymentMethod = paymentMethod;
            supplierController.Update(id, "paymentMethod", paymentMethod);
        }
    }

    public String getContactMail() 
    {
        return contactMail;
    }

    public void setContactMail(String contactMail) 
    {
        if(isPersisted)
        {
            this.contactMail = contactMail;
            supplierController.Update(id, "contactMail", contactMail);
        }
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) 
    {
        if(isPersisted)
        {
            this.contactPhone = contactPhone;
            supplierController.Update(id, "contactPhone", contactPhone);
        }
    }

    public void persist()
    {
        isPersisted = true;
        supplierController.insert(this);
    }
}
