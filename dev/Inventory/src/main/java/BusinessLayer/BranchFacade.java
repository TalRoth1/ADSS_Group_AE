package BusinessLayer;

import java.util.HashMap;
import java.util.Map;

public class BranchFacade 
{
    private static BranchFacade instance = null;
    private Map<Integer, BranchBL> branches = new HashMap<>();
    private int nextBranchID;

    private BranchFacade() 
    {
        nextBranchID = 1;
    }

    public static BranchFacade getInstance()
    {
        if(instance == null) 
        {
            synchronized(BranchFacade.class)
            {
                if(instance == null)
                {
                    BranchFacade x = new BranchFacade();
                    instance = x;
                }
            }
        }
        return instance;
    }

    public synchronized int addBranch(String name, String address)
    {
        if(name != null && address != null)
        {
            int id = nextBranchID++;
            branches.put(id, new BranchBL(id, name, address));
            return id;
        }
        else
        {
            throw new IllegalArgumentException("Name and address for branch can't be null");
        }
    }

    public void removeBranch(int branchID)
    {
        synchronized(this.branches)
        {
            if(branches.containsKey(branchID))
            {
                branches.remove(branchID);
            }
            else
            {
                throw new IllegalArgumentException("Branch with this id not found");
            }
        }
    }

    public void changeBranchName(String newName, int branchID)
    {
        synchronized(branches)
        {
            BranchBL branch = branches.get(branchID);
            if(branch != null) 
            {
                if(newName != null)
                {
                    branch.setName(newName);
                }
                else
                {
                    throw new IllegalArgumentException("New Name for branch can't be null");
                }
            }
            else
            {
                throw new IllegalArgumentException("Branch with this id not found");
            }
        }
    }

    public void changeBranchAddress(String newAddress, int branchID)
    {
        synchronized(branches)
        {
            BranchBL branch = branches.get(branchID);
            if(branch != null) 
            {
                if(newAddress != null)
                {
                    branch.setAddress(newAddress);
                }
                else
                {
                    throw new IllegalArgumentException("New Address for branch can't be null");
                }
            }
            else
            {
                throw new IllegalArgumentException("Branch with this id not found");
            }
        }
    }

    public String getBranchName(int id)
    {
        synchronized(this.branches)
        {
            if(branches.containsKey(id))
            {
                return branches.get(id).getName();
            }
            else
            {
                throw new IllegalArgumentException("Branch with this id not found");
            }
        }
    }

    public String getBranchAddress(int id)
    {
        synchronized(this.branches)
        {
            if(branches.containsKey(id))
            {
                return branches.get(id).getAddress();
            }
            else
            {
                throw new IllegalArgumentException("Branch with this id not found");
            }
        }
    }

    public String getListOfAllBranches()
    {
        StringBuilder sb = new StringBuilder();
        synchronized (branches)
        {
            if (branches.isEmpty())
            {
                throw new IllegalArgumentException("Branch not founded");
            }
            for (Map.Entry<Integer, BranchBL> entry : branches.entrySet())
            {
                int id = entry.getKey();
                BranchBL branch = entry.getValue();
                sb.append("Branch ID: ").append(id).append(", Name: ").append(branch.getName()).append(", Address: ").append(branch.getAddress()).append("\n");
            }
        }
        return sb.toString();
    }
}
