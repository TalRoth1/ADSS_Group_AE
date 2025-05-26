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

    public SupplierDAO(int id, int companyID, int bankAccount, String paymentMethod, String contactMail, String contactPhone) {
        this.id = id;
        this.companyID = companyID;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.contactMail = contactMail;
        this.contactPhone = contactPhone;
        this.supplierController = new SupplierController();
    }

    public int getId()
    {
        return id;
    }

    public int getCompanyID() 
    {
        return companyID;
    }

    public void setBankAccount(int bankAccount) 
    {
        if(isPersisted)
        {
            this.bankAccount = bankAccount;
            supplierController.Update(id, "bankAccount", String.valueOf(bankAccount));
        }
    }

    public void persist()
    {
        isPersisted = true;
        supplierController.insert(this);
    }
}
