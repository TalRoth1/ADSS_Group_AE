package DTOs;

import java.util.List;

import Domain.ContractDL;
import Utils.PaymentMethod;

public class SupplierDTO {
    private int supplierID;
    private int companyID;
    private int bankAccount;
    private PaymentMethod paymentMethod;
    private String contactMail;
    private String contactPhone;
    private List<ContractDL> agreements;

    public SupplierDTO(int supplierID, int companyID, int bankAccount, PaymentMethod paymentMethod, String contactMail,
            String contactPhone, List<ContractDL> agreements) {
        this.supplierID = supplierID;
        this.companyID = companyID;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
        this.contactMail = contactMail;
        this.contactPhone = contactPhone;
        this.agreements = agreements;
    }
}
