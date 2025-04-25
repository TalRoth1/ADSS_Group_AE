package Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Utils.DeliveryMethod;
import Utils.PaymentMethod;

public class SupplierFacade {
    private final List<SupplierDL> suppliers;
    private List<Item> items; // Will be saved in inventory after the merge
    private int nextId = 1;

    public SupplierFacade() {
        this.suppliers = new ArrayList<>();
    }

    public void addSupplier(int companyID, int bankAccount, PaymentMethod paymentMethod, String contactEmail,
            String contactPhone, DeliveryMethod deliveryMethod, List<AgreementDL> agreements) {
        SupplierDL newSupplier = new SupplierDL(nextId++, companyID, bankAccount, paymentMethod, contactEmail,
                contactPhone, deliveryMethod, agreements);
        suppliers.add(newSupplier);
    }

    public SupplierDL getSupplier(int supplierID) {
        for (SupplierDL supplier : suppliers) {
            if (supplier.getSupplierID() == supplierID) {
                return supplier;
            }
        }
        return null; // Supplier not found
    }

    public void addAgreement(int supplierID, Map<Integer, Integer> itemCat, List<String[]> billOfQuantities) {
        SupplierDL supplier = getSupplier(supplierID);
        int agreementID = supplier.getNextAgreementID();
        List<DiscountDL> discounts = new ArrayList<>();
        for (String[] item : billOfQuantities) {
            int itemID = Integer.parseInt(item[0]);
            int minimumQuantity = Integer.parseInt(item[1]);
            int discountPercentage = Integer.parseInt(item[2]);
            DiscountDL discount = new DiscountDL(itemID, minimumQuantity, discountPercentage);
            discounts.add(discount);
        }
        Map<Item, Integer> itemCatalog = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : itemCat.entrySet()) {
            for (Item item : items) {
                if (item.getItemID() == entry.getKey()) {
                    itemCatalog.put(item, entry.getValue());
                    break;
                }
            }
        }
        if (supplier != null) {
            AgreementDL newAgreement = new AgreementDL(agreementID, itemCatalog, discounts);
            supplier.addAgreement(newAgreement);
        }
    }

    public void changeAgreement(int supplierID, int agreementID, List<String[]> newBill) {
        List<DiscountDL> newBoQ = new ArrayList<>();
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) {
            for (AgreementDL agreement : supplier.getAgreements()) {
                if (agreement.getAgreementID() == agreementID) {
                    for (String[] item : newBill) {
                        int itemID = Integer.parseInt(item[0]);
                        int minimumQuantity = Integer.parseInt(item[1]);
                        int discountPercentage = Integer.parseInt(item[2]);
                        DiscountDL discount = new DiscountDL(itemID, minimumQuantity, discountPercentage);
                        newBoQ.add(discount);
                    }
                    agreement.setBillOfQuantities(newBoQ);
                    break;
                }
            }
        }
    }

    public void removeAgreement(int supplierID, int agreementID) {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) {
            supplier.removeAgreement(agreementID);
        }
    }

    public AgreementDL getAgreement(int supplierID, int agreementID) {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) {
            for (AgreementDL agreement : supplier.getAgreements()) {
                if (agreement.getAgreementID() == agreementID) {
                    return agreement;
                }
            }
        }
        return null; // Agreement not found
    }

    public Set<String> getSuppliedItems(int supplierID) {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) {
            Set<String> suppliedItems = new HashSet<>();
            for (AgreementDL agreement : supplier.getAgreements()) {
                Map<Item, Integer> itemCatalog = agreement.getItemCatalog();
                for (Item item : itemCatalog.keySet()) {
                    suppliedItems.add(item.getName());
                }
            }
            return suppliedItems;
        }
        return null; // Supplier not found
    }

    public Map<Integer, Integer> getSuppliedCatlogItems(int supplierID) {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) {
            Map<Integer, Integer> suppliedItems = new HashMap<>();
            for (AgreementDL agreement : supplier.getAgreements()) {
                Map<Item, Integer> itemCatalog = agreement.getItemCatalog();
                for (Map.Entry<Item, Integer> entry : itemCatalog.entrySet()) {
                    suppliedItems.put(entry.getKey().getItemID(), entry.getValue());
                }
            }
            return suppliedItems;
        }
        return null; // Supplier not found
    }

    public void loadData() {
        items = new ArrayList<>();
        items.add(new Item(1, "Milk", 3.70));
        items.add(new Item(2, "Eggs", 3.00));
        items.add(new Item(3, "Butter", 5.30));
        items.add(new Item(4, "Tomato", 1.10));
        items.add(new Item(5, "Cucumber", 2.10));
        items.add(new Item(6, "Carrot", 3.10));
        items.add(new Item(7, "Chicken", 4.70));
        items.add(new Item(8, "Beef", 5.50));
        items.add(new Item(9, "Rice", 2.60));
        items.add(new Item(10, "Pasta", 6.20));
        items.add(new Item(11, "Cheese", 4.00));
        items.add(new Item(12, "Yogurt", 1.50));
        items.add(new Item(13, "Juice", 2.80));
        items.add(new Item(14, "Bread", 1.90));
        items.add(new Item(15, "Salt", 0.70));

        addSupplier(2001, 111222333, PaymentMethod.CASH, "contact@supplierone.com", "+1-555-1111",
                DeliveryMethod.SCHEDULED, new ArrayList<>());
        Map<Integer, Integer> itemCat = new HashMap<>();
        itemCat.put(1, 1);
        itemCat.put(2, 2);
        itemCat.put(3, 3);
        List<String[]> billOfQuantities = new ArrayList<>();
        billOfQuantities.add(new String[] { "1", "100", "10" });
        billOfQuantities.add(new String[] { "2", "150", "15" });
        addAgreement(1, itemCat, billOfQuantities);

        addSupplier(2002, 444555666, PaymentMethod.CREDIT, "support@supplier2.com", "+1-555-2222",
                DeliveryMethod.ON_ORDER, new ArrayList<>());
        Map<Integer, Integer> itemCat2 = new HashMap<>();
        itemCat2.put(4, 4);
        itemCat2.put(5, 5);
        itemCat2.put(6, 6);
        List<String[]> billOfQuantities2 = new ArrayList<>();
        billOfQuantities.add(new String[] { "6", "200", "5" });
        addAgreement(2, itemCat2, billOfQuantities2);

        addSupplier(2003, 777888999, PaymentMethod.CREDIT, "hello@supplierthree.com", "+1-555-3333",
                DeliveryMethod.PICKUP, new ArrayList<>());
        Map<Integer, Integer> itemCat3 = new HashMap<>();
        itemCat3.put(7, 7);
        itemCat3.put(8, 8);
        List<String[]> billOfQuantities3 = new ArrayList<>();
        billOfQuantities.add(new String[] { "7", "120", "12" });
        addAgreement(3, itemCat3, billOfQuantities3);
        Map<Integer, Integer> itemCat4 = new HashMap<>();
        itemCat4.put(9, 9);
        itemCat4.put(10, 10);
        List<String[]> billOfQuantities4 = new ArrayList<>();
        billOfQuantities.add(new String[] { "10", "100", "8" });
        addAgreement(3, itemCat4, billOfQuantities4);
    }

}
