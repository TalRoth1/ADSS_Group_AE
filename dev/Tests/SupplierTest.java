package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Domain.ContractDL;
import Domain.DiscountDL;
import Domain.OnOrderDelivery;
import Domain.ProductBL;
import Domain.SupplierDL;
import Domain.SupplierFacade;
import Utils.PaymentMethod;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    private SupplierFacade supplierFacade;
    private ProductBL testItem;

    @BeforeEach
    void setUp() {
        supplierFacade = SupplierFacade.getInstance();

        // Simulate adding items to the SupplierFacade's internal list
        testItem = new ProductBL(1, "TestItem", 3.00, 100, 5, new String[]{"Test Unit"});
        List<ProductBL> itemList = new ArrayList<>();
        itemList.add(testItem);

        // Use reflection or a setter if accessible to inject the item list
        try {
            var field = SupplierFacade.class.getDeclaredField("items");
            field.setAccessible(true);
            field.set(supplierFacade, itemList);
        } catch (Exception e) {
            fail("Failed to set item list using reflection");
        }
    }

    @AfterEach
    void tearDown() {
        supplierFacade.clearData();
    }

    @Test
    void testAddAndGetSupplier() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890",new ArrayList<>());
        SupplierDL supplier = supplierFacade.getSupplier(1);
        assertNotNull(supplier);
        assertEquals(123, supplier.getCompanyID());
    }

    @Test
    void testAddAgreementAndGetAgreement() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", new ArrayList<>());

        Map<ProductBL, Integer> itemCat = new HashMap<>();
        List<DiscountDL> boq = new ArrayList<>();
        DiscountDL discount = new DiscountDL(1, 5, 10);
        boq.add(discount);
        SupplierDL supplier = supplierFacade.getSupplier(1);
        ContractDL contract = new ContractDL(1, itemCat, boq, new OnOrderDelivery());
        supplierFacade.getSupplier(1).addContract(contract);

        ContractDL agreement = supplier.getContract(1);
        assertNotNull(agreement);
        assertEquals(1, agreement.getContractID());
        assertEquals(1, agreement.getBillOfQuantities().size());
    }

    @Test
    void testChangeAgreement() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", new ArrayList<>());
        Map<ProductBL, Integer> itemCat = new HashMap<>();
        List<DiscountDL> boq = new ArrayList<>();
        DiscountDL discount = new DiscountDL(1, 5, 10);
        boq.add(discount);
        ContractDL contract = new ContractDL(1, itemCat, boq, new OnOrderDelivery());
        supplierFacade.getSupplier(1).addContract(contract);
        List<DiscountDL> newBoq = new ArrayList<>();
        DiscountDL newDiscount = new DiscountDL(1, 10, 20);
        newBoq.add(newDiscount);
        supplierFacade.getSupplier(1).getContract(1).setBillOfQuantities(newBoq);
        ContractDL agreement = supplierFacade.getSupplier(1).getContract(1);
        assertNotNull(agreement);
        assertEquals(10, agreement.getBillOfQuantities().get(0).getMinimumQuantity());
    }

    @Test
    void testRemoveAgreement() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", new ArrayList<>());
        Map<ProductBL, Integer> itemCat = new HashMap<>();
        List<DiscountDL> boq = new ArrayList<>();
        DiscountDL discount = new DiscountDL(1, 5, 10);
        boq.add(discount);
        ContractDL contract = new ContractDL(1, itemCat, boq, new OnOrderDelivery());
        supplierFacade.getSupplier(1).addContract(contract);
        supplierFacade.getSupplier(1).removeContract(1);

        assertNull(supplierFacade.getSupplier(1).getContract(1));
    }

    @Test
    void testGetSuppliedItems() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", new ArrayList<>());
        Map<ProductBL, Integer> itemCat = new HashMap<>();
        ProductBL item = new ProductBL(1, "TestItem", 3.00, 100, 5, new String[]{"Test Unit"});
        itemCat.put(item, 20);
        List<DiscountDL> boq = new ArrayList<>();
        DiscountDL discount = new DiscountDL(1, 5, 10);
        boq.add(discount);
        ContractDL contract = new ContractDL(1, itemCat, boq, new OnOrderDelivery());
        supplierFacade.getSupplier(1).addContract(contract);
        Set<String> items = supplierFacade.getSuppliedItems(1);
        assertTrue(items.contains("TestItem"));
    }

    @Test
    void testGetSuppliedCatalogItems() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", new ArrayList<>());
        Map<ProductBL, Integer> itemCat = new HashMap<>();
        ProductBL item = new ProductBL(1, "TestItem", 3.00, 100, 5, new String[]{"Test Unit"});
        itemCat.put(item, 20);
        List<DiscountDL> boq = new ArrayList<>();
        DiscountDL discount = new DiscountDL(1, 5, 10);
        boq.add(discount);
        ContractDL contract = new ContractDL(1, itemCat, boq, new OnOrderDelivery());
        supplierFacade.getSupplier(1).addContract(contract);

        Map<Integer, Integer> items = supplierFacade.getSuppliedCatalogItems(1);
        assertEquals(20, items.get(1));
    }
}
