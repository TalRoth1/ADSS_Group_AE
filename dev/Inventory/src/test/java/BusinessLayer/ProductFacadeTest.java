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
        int invalidId = 999;
        assertThrows(RuntimeException.class, () -> productFacade.removeProduct(invalidId));
    }

    @Test
    public void testAddAndRemoveProductSuccessfully()
    {
        int productId = productFacade.addProduct("Tnuva Milk", 1.0, 1.5, 0, 1, new String[]{"Milk"});
        assertDoesNotThrow(() -> productFacade.removeProduct(productId));
    }

    @Test
    public void testAddItemToNonExistentBranchFails()
    {
        int invalidBranchId = 9999;
        int productId = productFacade.addProduct("Tnuva Milk", 1.0, 1.5, 0, 1, new String[]{"Milk"});
        assertThrows(RuntimeException.class, () -> productFacade.addItem(productId, "Milk Bottle", false, new Date(), invalidBranchId, new String[]{"Shelf A1"}));
    }

    @Test
    public void testRemoveProductBeforeAndAfterAdding()
    {
        int productId = 12345;
        assertThrows(RuntimeException.class, () -> productFacade.removeProduct(productId));
        int addedId = productFacade.addProduct("Banana", 0.5, 0.8, 0, 31, new String[]{"Fruit"});
        assertDoesNotThrow(() -> productFacade.removeProduct(addedId));
    }

    @Test
    public void testUpdateNonExistentProduct()
    {
        assertThrows(RuntimeException.class, () -> productFacade.updateProduct(0,"Fake product", 0.0, 0.0, 0, 123, new String[]{"Fake"}));
    }

    @Test
    public void testUpdateNonExistentItem()
    {
        assertThrows(RuntimeException.class, () -> productFacade.updateItem(0,"Fake product", false, 9999, new String[]{"Somewhere"}));
    }

    @Test
    public void testAddItemToNonExistentProduct()
    {
        assertThrows(RuntimeException.class, () -> productFacade.addItem(9999, "Fake item", false, new Date(), validBranchId, new String[]{"Fake"}));
    }

    @Test
    public void testAddProductAndItemAndUpdateItem()
    {
        int productId = productFacade.addProduct("Tnuva milk", 0.7, 1.0, 0, 1, new String[]{"Milk"});
        int itemId = productFacade.addItem(productId, "Milk bottle", false, new Date(), validBranchId, new String[]{"Shelf A1"});
        assertDoesNotThrow(() -> productFacade.updateItem(itemId,null, false, validBranchId, new String[]{"Shelf B5"}));
    }

    @Test
    public void testAddProductAndItemAndUpdateItem2()
    {
        int productId = productFacade.addProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        int itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        assertDoesNotThrow(() -> productFacade.updateItem(itemId, "Milk Bottle Large", true,validBranchId , new String[]{"C2"}));
    }

    @Test
    public void testSalesReportIsNotNull()
    {
        int productId = productFacade.addProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        int itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        productFacade.purchaseItem(itemId);
        ReportBL report = productFacade.salesReport();
        assertNotNull(report);
    }


    @Test
    public void testDefectedReportIsNotNull()
    {
        int productId = productFacade.addProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        int itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        productFacade.updateItem(itemId, null, false , validBranchId, null);
        ReportBL report = productFacade.defectedReport();
        assertNotNull(report);
    }

    @Test
    public void testExpiredReportIsNotNull()
    {
        int productId = productFacade.addProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        int itemId = productFacade.addItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        ReportBL report = productFacade.expiredReport();
        assertNotNull(report);
    }
}
