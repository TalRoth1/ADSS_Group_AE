//package Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import Domain.DiscountDL;
//import Domain.Item;
//import Domain.SupplierFacade;
//import Domain.AgreementDL;
//import Utils.DeliveryMethod;
//
//public class SupplierService
//{
//    private SupplierFacade sf;
//
//    public SupplierService()
//    {
//        sf = new SupplierFacade();
//    }
//
//    public void addSupplier(int supplierID, int companyID, int bankAccount, int paymentMethod, String contactMail, String contactPhone, DeliveryMethod deliveryMethod, List<Item> suppliedItems, AgreementSL agreement)
//    {
//        try
//        {
//            // SupplierSL supplier = new SupplierSL(supplierID, companyID, bankAccount, paymentMethod, contactMail, contactPhone, deliveryMethod, suppliedItems, agreement);
//            // sf.addSupplier(supplier);
//        }
//        catch (Exception e)
//        {
//            System.out.println("Error adding supplier: " + e.getMessage());
//        }
//    }
//
//    public List<Item> getSuppliedItems(int supplierID)
//    {
//        try
//        {
//            // return sf.getSuppliedItems(supplierID);
//            return null; // Placeholder for actual implementation
//        }
//        catch (Exception e)
//        {
//            System.out.println("Error getting supplied items: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public void addAgreement(int supplierID, List<DiscountSL> billOfQuantities)
//    {
//        try
//        {
//            List<DiscountDL> discounts = new ArrayList<>();
//            for (DiscountSL discount : billOfQuantities)
//            {
//                DiscountDL newDiscount = new DiscountDL(discount.getItemID(), discount.getMinimumQuantity(), discount.getDiscountPercentage());
//                discounts.add(newDiscount);
//            }
//            // sf.addAgreement(supplierID ,agreementID, discounts);
//        }
//        catch (Exception e)
//        {
//            System.out.println("Error adding agreement: " + e.getMessage());
//        }
//    }
//
//    public void changeAgreement(int supplierID, int agreementID, AgreementSL agreement)
//    {
//        try
//        {
//            List<DiscountDL> discounts = new ArrayList<>();
//            for (DiscountSL discount : agreement.getBillOfQuantities())
//            {
//                DiscountDL newDiscount = new DiscountDL(discount.getItemID(), discount.getMinimumQuantity(), discount.getDiscountPercentage());
//                discounts.add(newDiscount);
//            }
//            AgreementDL newAgreement = new AgreementDL(agreement.getAgreementID(), discounts);
//            // sf.changeAgreement(supplierID, agreementID, newAgreement);
//        }
//        catch (Exception e)
//        {
//            System.out.println("Error changing agreement: " + e.getMessage());
//        }
//    }
//
//
//    public void removeAgreement(int supplierID, int agreementID)
//    {
//        try
//        {
//            sf.removeAgreement(supplierID, agreementID);
//        }
//        catch (Exception e)
//        {
//            System.out.println("Error removing agreement: " + e.getMessage());
//        }
//    }
//}
