package Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import DAL.CatalogController;
import DAL.CatalogDAO;
import DAL.ContractController;
import DAL.ContractDAO;
import DAL.DiscountController;
import DAL.DiscountDAO;
import DAL.SupplierController;
import DAL.SupplierDAO;
import Utils.PaymentMethod;

public class SupplierFacade {
    private final List<SupplierDL> suppliers;
    private List<ProductBL> items; // Will be saved in inventory after the merge
    private int nextId = 1;
    private SupplierController supplierController = new SupplierController();
    private ContractController contractController = new ContractController();
    private CatalogController catalogController = new CatalogController();
    private DiscountController discountController = new DiscountController();

    public SupplierFacade() {
        this.suppliers = new ArrayList<>();
        List<SupplierDAO> sups = supplierController.getAllSuppliers();
        for (SupplierDAO sup : sups) 
        {
            List<ContractDAO> contracts = contractController.getSupplierContracts(sup.getId());
            List<ContractDL> contractList = new ArrayList<>();
            List<DiscountDL> discounts = new ArrayList<>();
            for (ContractDAO contract : contracts)
            {
                Map<ProductBL, Integer> itemCatalog = new HashMap<>();
                List<CatalogDAO> catalogItems = catalogController.getContractSupplierCatalogs(sup.getId(), contract.getContractID());
                for (CatalogDAO catalogItem : catalogItems) {
                    ProductBL item = new ProductBL(catalogItem.getProductID());// need to get item from inventory based on Item id
                    DiscountDAO dis = discountController.getDiscount(catalogItem.getCatalogID());
                    if (dis != null) {
                        discounts.add(new DiscountDL(dis.getCatalogID(), dis.getMinimumQuantity(), dis.getDiscountPercentage()));
                    }
                    itemCatalog.put(item, catalogItem.getCatalogID());
                }
                ContractDL contractDL = new ContractDL(contract.getContractID(), itemCatalog, discounts, contract.getDeliveryMethod());
                contractList.add(contractDL);
            }
            PaymentMethod paymentMethod = PaymentMethod.valueOf(sup.getPaymentMethod().toUpperCase());
            SupplierDL supplier = new SupplierDL(sup.getId(), sup.getCompanyID(), sup.getBankAccount(), paymentMethod, sup.getContactMail(), sup.getContactPhone(), contractList);
            suppliers.add(supplier);
        }
    }

    public void addSupplier(int companyID, int bankAccount, PaymentMethod paymentMethod, String contactEmail,
            String contactPhone, List<ContractDL> contracts) {
        SupplierDL newSupplier = new SupplierDL(nextId++, companyID, bankAccount, paymentMethod, contactEmail,
                contactPhone, contracts);
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

    public void addContract(int supplierID, Map<Integer, Integer> itemCat, List<String[]> billOfQuantities, 
            DeliveryMethod deliveryMethod) {
        SupplierDL supplier = getSupplier(supplierID);
        int contractID = supplier.getNextContractID();
        List<DiscountDL> discounts = new ArrayList<>();
        for (String[] item : billOfQuantities) {
            int itemID = Integer.parseInt(item[0]);
            int minimumQuantity = Integer.parseInt(item[1]);
            int discountPercentage = Integer.parseInt(item[2]);
            DiscountDL discount = new DiscountDL(itemID, minimumQuantity, discountPercentage);
            discounts.add(discount);
        }
        Map<ProductBL, Integer> itemCatalog = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : itemCat.entrySet()) {
            for (ProductBL item : items) {
                if (item.getProductID() == entry.getKey()) {
                    itemCatalog.put(item, entry.getValue());
                    break;
                }
            }
        }
        if (supplier != null) {
            ContractDL newContract = new ContractDL(contractID, itemCatalog, discounts, deliveryMethod);
            supplier.addContract(newContract);
        }
    }

    public void changeContract(int supplierID, int contractID, List<String[]> newBill) {
        List<DiscountDL> newBoQ = new ArrayList<>();
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) {
            for (ContractDL contract : supplier.getContracts()) {
                if (contract.getContractID() == contractID) {
                    for (String[] item : newBill) {
                        int itemID = Integer.parseInt(item[0]);
                        int minimumQuantity = Integer.parseInt(item[1]);
                        int discountPercentage = Integer.parseInt(item[2]);
                        DiscountDL discount = new DiscountDL(itemID, minimumQuantity, discountPercentage);
                        newBoQ.add(discount);
                    }
                    contract.setBillOfQuantities(newBoQ);
                    break;
                }
            }
        }
    }

    public void removeContract(int supplierID, int contractID) {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) {
            supplier.removeContract(contractID);
        }
    }

    public ContractDL getContract(int supplierID, int contractID) {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) {
            for (ContractDL contract : supplier.getContracts()) {
                if (contract.getContractID() == contractID) {
                    return contract;
                }
            }
        }
        return null; // contract not found
    }

    public Set<String> getSuppliedItems(int supplierID) {
        SupplierDL supplier = getSupplier(supplierID);
        if (supplier != null) {
            Set<String> suppliedItems = new HashSet<>();
            for (ContractDL contract : supplier.getContracts()) {
                Map<ProductBL, Integer> itemCatalog = contract.getItemCatalog();
                for (ProductBL item : itemCatalog.keySet()) {
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
            for (ContractDL contract : supplier.getContracts()) {
                Map<ProductBL, Integer> itemCatalog = contract.getItemCatalog();
                for (Map.Entry<ProductBL, Integer> entry : itemCatalog.entrySet()) {
                    suppliedItems.put(entry.getKey().getProductID(), entry.getValue());
                }
            }
            return suppliedItems;
        }
        return null; // Supplier not found
    }

    public void loadData() {
        items = new ArrayList<>();
        items.add(new ProductBL(1, "Milk", 3.70, 10, 155, new String[]{"dairy", "beverages"}));
        items.add(new ProductBL(2, "Eggs", 3.00, 5, 156, new String[]{"dairy", "proteins"}));
        items.add(new ProductBL(3, "Butter", 5.30, 8, 157, new String[]{"dairy", "fats"}));
        items.add(new ProductBL(4, "Tomato", 1.10, 2, 158, new String[]{"vegetables"}));
        items.add(new ProductBL(5, "Cucumber", 2.10, 3, 159, new String[]{"vegetables"}));
        items.add(new ProductBL(6, "Carrot", 3.10, 4, 160, new String[]{"vegetables"}));
        items.add(new ProductBL(7, "Chicken", 4.70, 12, 161, new String[]{"meat", "proteins"}));
        items.add(new ProductBL(8, "Beef", 5.50, 15, 162, new String[]{"meat", "proteins"}));
        items.add(new ProductBL(9, "Rice", 2.60, 6, 163, new String[]{"grains"}));
        items.add(new ProductBL(10, "Pasta", 6.20, 7, 164, new String[]{"grains"}));
        items.add(new ProductBL(11, "Cheese", 4.00, 9, 165, new String[]{"dairy", "fats"}));
        items.add(new ProductBL(12, "Yogurt", 1.50, 11, 166, new String[]{"dairy", "beverages"}));
        items.add(new ProductBL(13, "Juice", 2.80, 4, 167, new String[]{"beverages"}));
        items.add(new ProductBL(14, "Bread", 1.90, 6, 168, new String[]{"bakery"}));
        items.add(new ProductBL(15, "Salt", 0.70, 2, 169, new String[]{"spices"}));

        addSupplier(2001, 111222333, PaymentMethod.CASH, "contact@supplierone.com", "+1-555-1111",
                 new ArrayList<>());
        Map<Integer, Integer> itemCat = new HashMap<>();
        itemCat.put(1, 1);
        itemCat.put(2, 2);
        itemCat.put(3, 3);
        List<String[]> billOfQuantities = new ArrayList<>();
        billOfQuantities.add(new String[] { "1", "100", "10" });
        billOfQuantities.add(new String[] { "2", "150", "15" });
        addContract(1, itemCat, billOfQuantities, new PeriodicDelivery(nextId, new ArrayList<>()));

        addSupplier(2002, 444555666, PaymentMethod.CREDIT, "support@supplier2.com", "+1-555-2222", new ArrayList<>());
        Map<Integer, Integer> itemCat2 = new HashMap<>();
        itemCat2.put(4, 4);
        itemCat2.put(5, 5);
        itemCat2.put(6, 6);
        List<String[]> billOfQuantities2 = new ArrayList<>();
        billOfQuantities.add(new String[] { "6", "200", "5" });
        addContract(2, itemCat2, billOfQuantities2, new OnOrderDelivery());

        addSupplier(2003, 777888999, PaymentMethod.CREDIT, "hello@supplierthree.com", "+1-555-3333", new ArrayList<>());
        Map<Integer, Integer> itemCat3 = new HashMap<>();
        itemCat3.put(7, 7);
        itemCat3.put(8, 8);
        List<String[]> billOfQuantities3 = new ArrayList<>();
        billOfQuantities.add(new String[] { "7", "120", "12" });
        addContract(3, itemCat3, billOfQuantities3, new PickupDelivery());
        Map<Integer, Integer> itemCat4 = new HashMap<>();
        itemCat4.put(9, 9);
        itemCat4.put(10, 10);
        List<String[]> billOfQuantities4 = new ArrayList<>();
        billOfQuantities.add(new String[] { "10", "100", "8" });
        addContract(3, itemCat4, billOfQuantities4, new PickupDelivery());
    }

}
