package Inventory.BusinessLayer;

public class BranchBL
{
    private int branchID;
    private String name;
    private String address;
    private final Object nameLock = new Object();
    private final Object addressLock = new Object();

    protected BranchBL(int id, String name, String adress)
    {
        this.branchID = id;
        this.name = name;
        this.address = adress;
    }

    protected int getID()
    {
        return branchID;
    }

    protected String getName()
    {
        return this.name;
    }

    protected String getAddress()
    {
        return this.address;
    }
    protected void setName(String newName)
    {
        synchronized(this.nameLock)
        {
            this.name = newName;
        }
    }

    protected void setAddress(String newAddress)
    {
        synchronized(this.addressLock)
        {
            this.address = newAddress;
        }
    }

}