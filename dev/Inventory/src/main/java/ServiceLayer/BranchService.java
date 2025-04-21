package ServiceLayer;

import BusinessLayer.BranchFacade;

public class BranchService
{
    private BranchFacade bf;

    public BranchService()
    {
        bf = BranchFacade.getInstance();
    }

    public Response AddBranch(String name, String address)
    {
        try
        {
            int id = bf.addBranch(name, address);
            return new Response(String.valueOf(id), null);
        }
        catch (Exception e)
        {
            return new Response(null, e.getMessage());
        }
    }

    public Response RemoveBranch(int branchID)
    {
        try
        {
            bf.removeBranch(branchID);
            return new Response("Branch removed successfully", null);
        }
        catch (Exception e)
        {
            return new Response(null, e.getMessage());
        }
    }

    public Response ChangeBranchName(String newName, int branchID) {
        try
        {
            bf.changeBranchName(newName, branchID);
            return new Response("Branch name updated successfully", null);
        }
        catch (Exception e)
        {
            return new Response(null, e.getMessage());
        }
    }

    public Response ChangeBranchAddress(String newAdd, int branchID)
    {
        try
        {
            bf.changeBranchAddress(newAdd, branchID);
            return new Response("Branch address updated successfully", null);
        }
        catch (Exception e)
        {
            return new Response(null, e.getMessage());
        }
    }

    public Response GetAllBranches()
    {
        try
        {
            String allBranches = bf.getListOfAllBranches();
            int total = bf.getTotalBranchCount();
            allBranches += "Total Branches: " + total + ".";
            return new Response(allBranches, null);
        }
        catch (Exception e)
        {
            return new Response(null, e.getMessage());
        }
    }

}
