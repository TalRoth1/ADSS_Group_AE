package Inventory.BusinessLayer;

public class BranchBL
{
    private int branchID;
    private String name;
    private String address;
    private final Object nameLock = new Object();
    private final Object addressLock = new Object();

    public BranchBL(int id, String name, String adress)
    {
        this.branchID = id;
        this.name = name;
        this.address = adress;
    }

    public int getID()
    {
        return branchID;
    }

    public String getName()
    {
        return this.name;
    }

    public String getAddress()
    {
        return this.address;
    }
    public void setName(String newName)
    {
        synchronized(this.nameLock)
        {
            this.name = newName;
        }
    }

    public void setAddress(String newAddress)
    {
        synchronized(this.addressLock)
        {
            this.address = newAddress;
        }
    }

}