package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Domain.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ProductFacadeTest {

    private ProductFacade productFacade;
    private BranchFacade branchFacade;
    private int validBranchId;

    @BeforeEach
    public void setUp() {
        productFacade = ProductFacade.getInstance();
        branchFacade = BranchFacade.getInstance();
        validBranchId = branchFacade.addBranch("Main Branch", "David Ben Gurion Blvd 1");
    }

    // Test for part 1 (Only domain logic without checking persistance)

    @Test
    public void testRemoveNonExistentProduct() {
        int invalidId = Integer.MAX_VALUE;
        assertThrows(RuntimeException.class, () -> productFacade.removeProduct(invalidId));
    }

    @Test
    public void testAddAndRemoveProductSuccessfully() {
        int productId = productFacade.addProduct("Tnuva Milk", 1.0, 0, 1, new String[] { "Milk" });
        assertDoesNotThrow(() -> productFacade.removeProduct(productId));
    }

    @Test
    public void testAddItemToNonExistentBranchFails() {
        int invalidBranchId = Integer.MAX_VALUE;
        int productId = productFacade.addProduct("Tnuva Milk", 1.0, 0, 1, new String[] { "Milk" });
        assertThrows(RuntimeException.class, () -> productFacade.addItem(productId, "Milk Bottle", false, new Date(),
                invalidBranchId, new String[] { "Shelf A1" }));
        productFacade.removeProduct(productId);
    }

    @Test
    public void testRemoveProductBeforeAndAfterAdding() {
        int productId = Integer.MAX_VALUE;
        assertThrows(RuntimeException.class, () -> productFacade.removeProduct(productId));
        int addedId = productFacade.addProduct("Banana", 0.5, 0, 31, new String[] { "Fruit" });
        assertDoesNotThrow(() -> productFacade.removeProduct(addedId));
    }

    @Test
    public void testUpdateNonExistentProduct() {
        assertThrows(RuntimeException.class,
                () -> productFacade.updateProduct(0, "Fake product", 0.0, 0, 123, new String[] { "Fake" }));
    }

    @Test
    public void testUpdateNonExistentItem() {
        assertThrows(RuntimeException.class,
                () -> productFacade.updateItem(0, "Fake product", false, 9999, new String[] { "Somewhere" }));
    }

    @Test
    public void testAddItemToNonExistentProduct() {
        assertThrows(RuntimeException.class, () -> productFacade.addItem(9999, "Fake item", false, new Date(),
                validBranchId, new String[] { "Fake" }));
    }

    @Test
    public void testAddProductAndItemAndUpdateItem() {
        int productId = productFacade.addProduct("Tnuva milk", 0.7, 0, 1, new String[] { "Milk" });
        int itemId = productFacade.addItem(productId, "Milk bottle", false, new Date(), validBranchId,
                new String[] { "Shelf A1" });
        assertDoesNotThrow(
                () -> productFacade.updateItem(itemId, null, false, validBranchId, new String[] { "Shelf B5" }));
        productFacade.removeItem(itemId);
        productFacade.removeProduct(productId);
    }

    @Test
    public void testAddProductAndItemAndUpdateItem2() {
        int productId = productFacade.addProduct("Tnuva Milk", 2.0, 0, 3, new String[] { "Dairy" });
        int itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId,
                new String[] { "Shelf C1" });
        assertDoesNotThrow(() -> productFacade.updateItem(itemId, "Milk Bottle Large", true, validBranchId,
                new String[] { "Shelf C2" }));
        productFacade.removeItem(itemId);
        productFacade.removeProduct(productId);
    }

    @Test
    public void testSalesReportIsNotNull() {
        int productId = productFacade.addProduct("Milk", 2.0, 0, 3, new String[] { "Dairy" });
        int itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId,
                new String[] { "Shelf C1" });
        productFacade.purchaseItem(itemId);
        ReportBL report = productFacade.salesReport();
        assertNotNull(report);
        productFacade.removeItem(itemId);
        productFacade.removeProduct(productId);
    }

    @Test
    public void testDefectedReportIsNotNull() {
        int productId = productFacade.addProduct("Milk", 2.0, 0, 3, new String[] { "Dairy" });
        int itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId,
                new String[] { "Shelf C1" });
        productFacade.updateItem(itemId, null, false, validBranchId, null);
        ReportBL report = productFacade.defectedReport();
        assertNotNull(report);
        productFacade.removeItem(itemId);
        productFacade.removeProduct(productId);
    }

    @Test
    public void testExpiredReportIsNotNull() {
        int productId = productFacade.addProduct("Milk", 2.0, 0, 3, new String[] { "Dairy" });
        int itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId,
                new String[] { "Shelf C1" });
        ReportBL report = productFacade.expiredReport();
        assertNotNull(report);
        productFacade.removeItem(itemId);
        productFacade.removeProduct(productId);
    }

    // Tests for part 2 (DataBase included)

    @Test
    public void testPersistence_AddProductAndReloadFacade() {
        int productId = productFacade.addProduct("Milk", 2.5, 0, 14, new String[] { "Dairy" });
        ProductFacade.resetInstance();
        productFacade = ProductFacade.getInstance();
        assertDoesNotThrow(() -> productFacade.updateProduct(productId, "Persistent Milk Updated", 2.5, 14, 0,
                new String[] { "Dairy" }));
        productFacade.removeProduct(productId);
    }

    @Test
    public void testPersistence_AddItemAndReloadFacade() {
        int productId = productFacade.addProduct("Persistent Butter", 3.0, 0, 7, new String[] { "Dairy" });
        int itemId = productFacade.addItem(productId, "Butter Pack", false, new Date(), validBranchId,
                new String[] { "Shelf Z9" });
        ProductFacade.resetInstance();
        productFacade = ProductFacade.getInstance();
        assertDoesNotThrow(() -> productFacade.updateItem(itemId, "Butter Pack Updated", false, validBranchId,
                new String[] { "Shelf Z10" }));
        productFacade.removeItem(itemId);
        productFacade.removeProduct(productId);
    }

    @Test
    public void testPersistence_UpdateProductAndReloadFacade() {
        int productId = productFacade.addProduct("Test Cheese", 4.0, 0, 30, new String[] { "Dairy" });
        productFacade.updateProduct(productId, "Updated Cheese", 5.5, 25, 0, new String[] { "Dairy", "Updated" });
        ProductFacade.resetInstance();
        productFacade = ProductFacade.getInstance();
        assertDoesNotThrow(() -> productFacade.updateProduct(productId, "Final Cheese", 6.0, 20, 0,
                new String[] { "Final" }));
        productFacade.removeProduct(productId);
    }

    @Test
    public void testPersistence_UpdateItemAndReloadFacade() {
        int productId = productFacade.addProduct("Yogurt tnuva", 1.5, 0, 10, new String[] { "Dairy" });
        int itemId = productFacade.addItem(productId, "Yogurt Cup", false, new Date(), validBranchId,
                new String[] { "A1" });
        productFacade.updateItem(itemId, "Yogurt Cup", true, validBranchId, new String[] { "B2" });
        ProductFacade.resetInstance();
        productFacade = ProductFacade.getInstance();
        assertDoesNotThrow(
                () -> productFacade.updateItem(itemId, "Big Yogurt Cup", true, validBranchId, new String[] { "B3" }));
        productFacade.removeItem(itemId);
        productFacade.removeProduct(productId);
    }

    @Test
    public void testProductDeletionPersistsAfterReload() {
        int productId = productFacade.addProduct("Butter", 3.5, 0, 14, new String[] { "Dairy" });
        productFacade.removeProduct(productId);
        ProductFacade.resetInstance();
        productFacade = ProductFacade.getInstance();
        assertThrows(RuntimeException.class,
                () -> productFacade.updateProduct(productId, "Should Fail", 0.0, 0, 0, new String[] { "Fail" }));
    }

    @Test
    public void testItemDeletionPersistsAfterReload() {
        int productId = productFacade.addProduct("Temp Yogurt", 1.0, 0, 10, new String[] { "Dairy" });
        int itemId = productFacade.addItem(productId, "Yogurt Small", false, new Date(), validBranchId,
                new String[] { "Shelf Z9" });
        productFacade.removeItem(itemId);
        ProductFacade.resetInstance();
        productFacade = ProductFacade.getInstance();
        assertThrows(RuntimeException.class, () -> productFacade.updateItem(itemId, "Should Not Work", false,
                validBranchId, new String[] { "Shelf A0" }));
        productFacade.removeProduct(productId);
    }

    @AfterEach
    public void afterTests() {
        branchFacade.removeBranch(validBranchId);
        ProductFacade.resetInstance();
        BranchFacade.resetInstance();
    }
}