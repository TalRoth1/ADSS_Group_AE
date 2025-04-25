package Domain;

import Utils.DeliveryMethod;
import Utils.PaymentMethod;

import java.util.List;

public class SupplierDL {
    private int supplierID;
    private int companyID;
    private int bankAccount;
    private PaymentMethod paymentMethod;
    private String contactMail;
    private String contactPhone;
    private DeliveryMethod deliveryMethod;
    private List<AgreementDL> agreements;
    private int nextAgreementID = 1; // Static variable to keep track of the next agreement ID

    public SupplierDL(int supplierID, int companyID, int banckAccount, PaymentMethod paymentMethod, String contactMail,
            String contactPhone, DeliveryMethod deliveryMethod, List<AgreementDL> agreements) {
        this.supplierID = supplierID;
        this.companyID = companyID;
        this.bankAccount = banckAccount;
        this.paymentMethod = paymentMethod;
        this.contactMail = contactMail;
        this.contactPhone = contactPhone;
        this.deliveryMethod = deliveryMethod;
        this.agreements = agreements;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setBankAccount(int bankAccount) {
        this.bankAccount = bankAccount;
    }

    public int getBankAccount() {
        return bankAccount;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail;
    }

    public String getContactMail() {
        return contactMail;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void addAgreement(AgreementDL agreement) {
        this.agreements.add(agreement);
    }

    public void removeAgreement(int agreementID) {
        for (AgreementDL agreement : this.agreements) {
            if (agreement.getAgreementID() == agreementID) {
                this.agreements.remove(agreement);
                break;
            }
        }
    }

    public void setAgreements(List<AgreementDL> agreements) {
        this.agreements = agreements;
    }

    public List<AgreementDL> getAgreements() {
        return agreements;
    }

    public int getNextAgreementID() {
        return nextAgreementID++;
    }
}