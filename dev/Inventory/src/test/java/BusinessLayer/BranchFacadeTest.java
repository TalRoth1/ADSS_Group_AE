package BusinessLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BranchFacadeTest {

    private BranchFacade branchFacade;

    @BeforeEach
    public void setUp() {
        branchFacade = BranchFacade.getInstance();
    }

    @Test
    public void testAddBranchSuccessfully() {
        int branchId = branchFacade.addBranch("Downtown Branch", "123 Main Street");
        assertTrue(branchId > 0);
        String name = branchFacade.getBranchName(branchId);
        assertEquals("Downtown Branch", name);
    }

    @Test
    public void testRemoveBranchSuccessfully() {
        int branchId = branchFacade.addBranch("Test Branch", "456 Another St");
        assertDoesNotThrow(() -> branchFacade.removeBranch(branchId));
    }

    @Test
    public void testRemoveNonExistentBranch() {
        assertThrows(IllegalArgumentException.class, () -> branchFacade.removeBranch(9999));
    }

    @Test
    public void testChangeBranchNameSuccessfully() {
        int branchId = branchFacade.addBranch("Old Name", "789 Street");
        branchFacade.changeBranchName("New Name", branchId);
        assertEquals("New Name", branchFacade.getBranchName(branchId));
    }

    @Test
    public void testChangeBranchAddressSuccessfully() {
        int branchId = branchFacade.addBranch("Address Branch", "Initial Address");
        branchFacade.changeBranchAddress("Updated Address", branchId);
        assertEquals("Updated Address", branchFacade.getBranchAddress(branchId));
    }

    @Test
    public void testGetAllBranches() {
        branchFacade.addBranch("Branch1", "Addr1");
        branchFacade.addBranch("Branch2", "Addr2");
        String branchesList = branchFacade.getListOfAllBranches();
        assertTrue(branchesList.contains("Branch1"));
        assertTrue(branchesList.contains("Branch2"));
    }
}
