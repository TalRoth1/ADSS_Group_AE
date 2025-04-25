package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Domain.AgreementDL;
import Domain.Item;
import Domain.SupplierDL;
import Domain.SupplierFacade;
import Utils.DeliveryMethod;
import Utils.PaymentMethod;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    private SupplierFacade supplierFacade;
    private Item testItem;

    @BeforeEach
    void setUp() {
        supplierFacade = new SupplierFacade();

        // Simulate adding items to the SupplierFacade's internal list
        testItem = new Item(1, "TestItem", 3.50);
        List<Item> itemList = new ArrayList<>();
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

    @Test
    void testAddAndGetSupplier() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", DeliveryMethod.ON_ORDER, new ArrayList<>());
        SupplierDL supplier = supplierFacade.getSupplier(1);
        assertNotNull(supplier);
        assertEquals(123, supplier.getCompanyID());
    }

    @Test
    void testAddAgreementAndGetAgreement() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", DeliveryMethod.ON_ORDER, new ArrayList<>());

        Map<Integer, Integer> itemCat = Map.of(1, 10);
        List<String[]> boq = List.of(new String[][]{new String[]{"1", "5", "10"}});
        supplierFacade.addAgreement(1, itemCat, boq);

        AgreementDL agreement = supplierFacade.getAgreement(1, 1);
        assertNotNull(agreement);
        assertEquals(1, agreement.getAgreementID());
        assertEquals(1, agreement.getBillOfQuantities().size());
    }

    @Test
    void testChangeAgreement() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", DeliveryMethod.ON_ORDER, new ArrayList<>());
        supplierFacade.addAgreement(1, Map.of(1, 10), List.of(new String[][]{new String[]{"1", "5", "10"}}));

        supplierFacade.changeAgreement(1, 1, List.of(new String[][]{new String[]{"1", "10", "20"}}));
        AgreementDL agreement = supplierFacade.getAgreement(1, 1);
        assertNotNull(agreement);
        assertEquals(10, agreement.getBillOfQuantities().get(0).getMinimumQuantity());
    }

    @Test
    void testRemoveAgreement() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", DeliveryMethod.PICKUP, new ArrayList<>());
        supplierFacade.addAgreement(1, Map.of(1, 10), List.of(new String[][]{new String[]{"1", "5", "10"}}));
        supplierFacade.removeAgreement(1, 1);

        assertNull(supplierFacade.getAgreement(1, 1));
    }

    @Test
    void testGetSuppliedItems() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", DeliveryMethod.SCHEDULED, new ArrayList<>());
        supplierFacade.addAgreement(1, Map.of(1, 10), List.of(new String[][]{new String[]{"1", "5", "10"}}));

        Set<String> items = supplierFacade.getSuppliedItems(1);
        assertTrue(items.contains("TestItem"));
    }

    @Test
    void testGetSuppliedCatalogItems() {
        supplierFacade.addSupplier(123, 456, PaymentMethod.CREDIT, "email@test.com", "1234567890", DeliveryMethod.ON_ORDER, new ArrayList<>());
        supplierFacade.addAgreement(1, Map.of(1, 20), List.of(new String[][]{new String[]{"1", "5", "10"}}));

        Map<Integer, Integer> items = supplierFacade.getSuppliedCatlogItems(1);
        assertEquals(20, items.get(1));
    }
}
