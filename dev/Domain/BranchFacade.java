package Domain;

import DAL.BranchController;
import DAL.BranchDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class BranchFacade 
{
    private static BranchFacade instance = null;
    private final Map<Integer, BranchBL> branches = new HashMap<>();
    private final BranchController controller;
    private int nextBranchID;
    private boolean demonstrationMode = false;

    private BranchFacade() 
    {
        controller = new BranchController();
        nextBranchID = controller.getMaxBranchID() + 1;
        loadAllBranchesFromDB();
    }

    public static BranchFacade getInstance()
    {
        if(instance == null) 
        {
            synchronized(BranchFacade.class)
            {
                if(instance == null)
                {
                    instance = new BranchFacade();
                }
            }
        }
        return instance;
    }

    private void loadAllBranchesFromDB()
    {           
        if(!demonstrationMode)
        {
            for (BranchDAO dao : controller.getAllBranches())
            {
                branches.put(dao.getBranchID(), new BranchBL(dao.getBranchID(), dao.getName(), dao.getAddress()));
            }
        }
    }

    public synchronized int addBranch(String name, String address)
    {
        if (name == null || address == null)
        {
            throw new IllegalArgumentException("Name and address for branch can't be null");
        }

        int id = nextBranchID++;
        BranchDAO dao = new BranchDAO(id, name, address);
        if(!demonstrationMode)
        {
            controller.insert(dao);
        }
        branches.put(id, new BranchBL(id, name, address));
        return id;
    }

    public void removeBranch(int branchID)
    {
        synchronized (branches)
        {
            if (!branches.containsKey(branchID))
            {
                throw new IllegalArgumentException("Branch with this ID not found");
            }

            ProductFacade.getInstance().deleteAllItemsForBranch(branchID);
            if(!demonstrationMode)
            {
                controller.delete(branchID);
            }
            branches.remove(branchID);
        }
    }

    public void changeBranchName(String newName, int branchID)
    {
        synchronized(branches)
        {
            BranchBL branch = branches.get(branchID);
            if (branch == null)
            {
                throw new IllegalArgumentException("Branch with this ID not found");
            }

            if (newName == null)
            {
                throw new IllegalArgumentException("New Name for branch can't be null");
            }

            branch.setName(newName);
            if(!demonstrationMode)
            {
                controller.updateName(branchID, newName);
            }
        }
    }

    public void changeBranchAddress(String newAddress, int branchID)
    {
        synchronized(branches)
        {
            BranchBL branch = branches.get(branchID);
            if (branch == null)
            {
                throw new IllegalArgumentException("Branch with this ID not found");
            }

            if (newAddress == null)
            {
                throw new IllegalArgumentException("New Address for branch can't be null");
            }

            branch.setAddress(newAddress);
            if(!demonstrationMode)
            {
                controller.updateAddress(branchID, newAddress);
            }
        }
    }

    public boolean isBranchExists(int id)
    {
        synchronized (branches)
        {
            return branches.containsKey(id);
        }
    }

    public String getBranchName(int id)
    {
        synchronized (branches)
        {
            if (!branches.containsKey(id))
                throw new IllegalArgumentException("Branch with this ID not found");

            return branches.get(id).getName();
        }
    }

    public String getBranchAddress(int id)
    {
        synchronized (branches)
        {
            if (!branches.containsKey(id))
                throw new IllegalArgumentException("Branch with this ID not found");

            return branches.get(id).getAddress();
        }
    }

    public String getListOfAllBranches()
    {
        synchronized (branches)
        {
            if (branches.isEmpty())
                throw new IllegalArgumentException("No branches found");

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Integer, BranchBL> entry : branches.entrySet())
            {
                BranchBL branch = entry.getValue();
                sb.append("Branch ID: ").append(entry.getKey())
                  .append(", Name: ").append(branch.getName())
                  .append(", Address: ").append(branch.getAddress())
                  .append("\n");
            }
            return sb.toString();
        }
    }

    public int getTotalBranchCount()
    {
        synchronized (branches)
        {
            return branches.size();
        }
    }

    public Set<Integer> getAllBranchIDs()
    {
        synchronized (branches)
        {
            return new HashSet<>(branches.keySet());
        }
    }

    public synchronized void enterDemo() 
    {
        demonstrationMode = true;
        this.branches.clear();
        nextBranchID = 0;
    }

    public synchronized void deactivateDemo()
    {
        demonstrationMode = false;
        this.branches.clear();
        nextBranchID = controller.getMaxBranchID() + 1;
        loadAllBranchesFromDB();
    }

    // This function is for TESTS ONLY, do not use in real life.
    public static void resetInstance() 
    {
        instance = null;
    }
}
