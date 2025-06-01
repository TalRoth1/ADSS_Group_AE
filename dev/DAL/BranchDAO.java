package DAL;

public class BranchDAO 
{
    private boolean isPersisted = false;
    private int branchID;
    private String name;
    private String address;

    public BranchDAO(int branchID, String name, String address) 
    {
        this.branchID = branchID;
        this.name = name;
        this.address = address;
    }

    public int getBranchID() 
    {
        return branchID;
    }

    public void setBranchID(int branchID) 
    {
        if (isPersisted)
        {
            this.branchID = branchID;
        }
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        if (isPersisted)
            this.name = name;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setAddress(String address)
    {
        if (isPersisted)
        {
            this.address = address;
        }
    }

    public void persist() 
    {
        this.isPersisted = true;
    }

    public boolean isPersisted() 
    {
        return isPersisted;
    }
}
