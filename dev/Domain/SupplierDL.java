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
    private List<ContractDL> contracts;
    private int nextcontractID = 1; // Static variable to keep track of the next contract ID

    public SupplierDL(int supplierID, int companyID, int banckAccount, PaymentMethod paymentMethod, String contactMail,
            String contactPhone, List<ContractDL> contracts) {
        this.supplierID = supplierID;
        this.companyID = companyID;
        this.bankAccount = banckAccount;
        this.paymentMethod = paymentMethod;
        this.contactMail = contactMail;
        this.contactPhone = contactPhone;
        this.contracts = contracts;
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

    public void addContract(ContractDL contract) {
        this.contracts.add(contract);
    }

    public void removeContract(int contractID) {
        for (ContractDL contract : this.contracts) {
            if (contract.getContractID() == contractID) {
                this.contracts.remove(contract);
                break;
            }
        }
    }

    public void setContracts(List<ContractDL> contracts) {
        this.contracts = contracts;
    }

    public List<ContractDL> getContracts() {
        return contracts;
    }

    public ContractDL getContract(int contractID) {
        for (ContractDL contract : contracts) {
            if (contract.getContractID() == contractID) {
                return contract;
            }
        }
        return null; // contract not found
    }

    public int getNextContractID() {
        return nextcontractID++;
    }

    public SupplierDTO toDTO() {
        return new SupplierDTO(supplierID, companyID, bankAccount, paymentMethod, contactMail, contactPhone, contracts);
    }
}