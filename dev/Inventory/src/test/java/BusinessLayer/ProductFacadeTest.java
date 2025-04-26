package BusinessLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ProductFacadeTest {

    private ProductFacade productFacade;
    private BranchFacade branchFacade;
    private int validBranchId;

    @BeforeEach
    public void setUp()
    {
        productFacade = ProductFacade.getInstance();
        branchFacade = BranchFacade.getInstance();
        validBranchId =  branchFacade.addBranch("Main Branch", "David Ben Gurion Blvd 1");
    }

    @Test
    public void testRemoveNonExistentProduct()
    {
        long invalidId = 999;
        assertThrows(RuntimeException.class, () -> productFacade.removeProduct(invalidId));
    }

    @Test
    public void testAddAndRemoveProductSuccessfully()
    {
        long productId = productFacade.addProduct("Tnuva Milk", 1.0, 1.5, 0, 1, new String[]{"Milk"});
        assertDoesNotThrow(() -> productFacade.removeProduct(productId));
    }

    @Test
    public void testAddProductToInvalidBranchFails()
    {
        int invalidBranchId = 9999;
        assertThrows(RuntimeException.class, () -> productFacade.addProduct("Fake product", 1.0, 1.5, 0, 999, new String[]{"Fake"}));
    }

    @Test
    public void testRemoveProductBeforeAndAfterAdding()
    {
        long productId = 12345;
        assertThrows(RuntimeException.class, () -> productFacade.removeProduct(productId));
        long addedId = productFacade.addProduct("Banana", 0.5, 0.8, 0, 31, new String[]{"Fruit"});
        assertDoesNotThrow(() -> productFacade.removeProduct(addedId));
    }

    @Test
    public void testUpdateNonExistentProduct()
    {
        assertThrows(RuntimeException.class, () -> productFacade.updateProduct("Fake product", 0.0, 0.0, 0, 123, new String[]{"Fake"}));
    }

    @Test
    public void testUpdateNonExistentItem()
    {
        assertThrows(RuntimeException.class, () -> productFacade.updateItem("Fake product", false, 9999, new String[]{"Somewhere"}));
    }

    @Test
    public void testAddItemToNonExistentProduct()
    {
        assertThrows(RuntimeException.class, () -> productFacade.addItem(9999, "Fake item", false, new Date(), validBranchId, new String[]{"Fake"}));
    }

    @Test
    public void testAddProductAndItemAndUpdateItem()
    {
        long productId = productFacade.addProduct("Tnuva milk", 0.7, 1.0, 0, 1, new String[]{"Milk"});
        long itemId = productFacade.addItem(productId, "Milk bottle", false, new Date(), validBranchId, new String[]{"Shelf A1"});
        assertDoesNotThrow(() -> productFacade.updateItem(null,null, itemId, new String[]{"Shelf B5"}));
    }

    @Test
    public void testAddProductAndItemAndUpdateItem2()
    {
        long productId = productFacade.addProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        long itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        assertDoesNotThrow(() -> productFacade.updateItem("Milk Bottle Large", true, itemId, new String[]{"C2"}));
    }

    @Test
    public void testSalesReportIsNotNull()
    {
        long productId = productFacade.addProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        long itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        productFacade.purchaseItem(itemId);
        ReportBL report = productFacade.salesReport();
        assertNotNull(report);
    }


    @Test
    public void testDefectedReportIsNotNull()
    {
        long productId = productFacade.addProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        long itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        productFacade.updateItem(null, true, itemId, null);
        ReportBL report = productFacade.defectedReport();
        assertNotNull(report);
    }

    @Test
    public void testExpiredReportIsNotNull()
    {
        //TODO
        long productId = productFacade.addProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        long itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        ReportBL report = productFacade.expiredReport();
        assertNotNull(report);
    }
}
