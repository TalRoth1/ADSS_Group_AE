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
        productFacade = ProductFacade.GetInstance();
        branchFacade = BranchFacade.getInstance();
        validBranchId =  branchFacade.addBranch("Main Branch", "David Ben Gurion Blvd 1");
    }

    @Test
    public void testRemoveNonExistentProduct()
    {
        long invalidId = 999;
        assertThrows(RuntimeException.class, () -> productFacade.RemoveProduct(invalidId));
    }

    @Test
    public void testAddAndRemoveProductSuccessfully()
    {
        long productId = productFacade.AddProduct("Tnuva Milk", 1.0, 1.5, 0, 1, new String[]{"Milk"});
        assertDoesNotThrow(() -> productFacade.RemoveProduct(productId));
    }

    @Test
    public void testAddProductToInvalidBranchFails()
    {
        int invalidBranchId = 9999;
        assertThrows(RuntimeException.class, () -> productFacade.AddProduct("Fake product", 1.0, 1.5, 0, 999, new String[]{"Fake"}));
    }

    @Test
    public void testRemoveProductBeforeAndAfterAdding()
    {
        long productId = 12345;
        assertThrows(RuntimeException.class, () -> productFacade.RemoveProduct(productId));
        long addedId = productFacade.AddProduct("Banana", 0.5, 0.8, 0, 31, new String[]{"Fruit"});
        assertDoesNotThrow(() -> productFacade.RemoveProduct(addedId));
    }

    @Test
    public void testUpdateNonExistentProduct()
    {
        assertThrows(RuntimeException.class, () -> productFacade.UpdateProduct("Fake product", 0.0, 0.0, 0, 123, new String[]{"Fake"}));
    }

    @Test
    public void testUpdateNonExistentItem()
    {
        assertThrows(RuntimeException.class, () -> productFacade.UpdateItem("Fake product", false, 9999, new String[]{"Somewhere"}));
    }

    @Test
    public void testAddItemToNonExistentProduct()
    {
        assertThrows(RuntimeException.class, () -> productFacade.AddItem(9999, "Fake item", false, new Date(), validBranchId, new String[]{"Fake"}));
    }

    @Test
    public void testAddProductAndItemAndUpdateItem()
    {
        long productId = productFacade.AddProduct("Tnuva milk", 0.7, 1.0, 0, 1, new String[]{"Milk"});
        long itemId = productFacade.AddItem(productId, "Milk bottle", false, new Date(), validBranchId, new String[]{"Shelf A1"});
        assertDoesNotThrow(() -> productFacade.UpdateItem(null,null, itemId, new String[]{"Shelf B5"}));
    }

    @Test
    public void testAddProductAndItemAndUpdateItem2()
    {
        long productId = productFacade.AddProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        long itemId = productFacade.AddItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        assertDoesNotThrow(() -> productFacade.UpdateItem("Milk Bottle Large", true, itemId, new String[]{"C2"}));
    }

    @Test
    public void testSalesReportIsNotNull()
    {
        long productId = productFacade.AddProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        long itemId = productFacade.AddItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        productFacade.PurchaseItem(itemId);
        ReportBL report = productFacade.SalesReport();
        assertNotNull(report);
    }


    @Test
    public void testDefectedReportIsNotNull()
    {
        long productId = productFacade.AddProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        long itemId = productFacade.AddItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        productFacade.UpdateItem(null, true, itemId, null);
        ReportBL report = productFacade.DefectedReport();
        assertNotNull(report);
    }

    @Test
    public void testExpiredReportIsNotNull()
    {
        //TODO
        long productId = productFacade.AddProduct("Milk", 2.0, 2.5, 0, 3, new String[]{"Dairy"});
        long itemId = productFacade.AddItem(productId, "Milk Bottle", false, new Date(), validBranchId, new String[]{"C1"});
        ReportBL report = productFacade.ExpiredReport();
        assertNotNull(report);
    }
}
