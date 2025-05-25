package Domain;

import Utils.PaymentMethod;

import java.util.List;

import DTOs.SupplierDTO;

public class SupplierDL {
    private int supplierID;
    private int companyID;
    private int bankAccount;
    private PaymentMethod paymentMethod;
    private String contactMail;
    private String contactPhone;
    private List<ContractDL> agreements;
    private int nextAgreementID = 1; // Static variable to keep track of the next agreement ID

    public SupplierDL(int supplierID, int companyID, int banckAccount, PaymentMethod paymentMethod, String contactMail,
            String contactPhone, List<ContractDL> agreements) {
        this.supplierID = supplierID;
        this.companyID = companyID;
        this.bankAccount = banckAccount;
        this.paymentMethod = paymentMethod;
        this.contactMail = contactMail;
        this.contactPhone = contactPhone;
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

    public void addAgreement(ContractDL agreement) {
        this.agreements.add(agreement);
    }

    public void removeAgreement(int agreementID) {
        for (ContractDL agreement : this.agreements) {
            if (agreement.getAgreementID() == agreementID) {
                this.agreements.remove(agreement);
                break;
            }
        }
    }

    public void setAgreements(List<ContractDL> agreements) {
        this.agreements = agreements;
    }

    public List<ContractDL> getAgreements() {
        return agreements;
    }

    public ContractDL getAgreement(int agreementID) {
        for (ContractDL agreement : agreements) {
            if (agreement.getAgreementID() == agreementID) {
                return agreement;
            }
        }
        return null; // Agreement not found
    }

    public int getNextAgreementID() {
        return nextAgreementID++;
    }

    public SupplierDTO toDTO() {
        return new SupplierDTO(supplierID, companyID, bankAccount, paymentMethod, contactMail, contactPhone, agreements);
    }
}