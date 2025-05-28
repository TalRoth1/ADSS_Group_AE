package DAL;

public class CatalogDAO
{
    private boolean isPersisted = false;
    private int supplierID;
    private int contractID;
    private int productID;
    private int catalogID;
    private CatalogController catalogController;

    public CatalogDAO(int supplierID, int contractID, int productID, int catalogID)
    {
        this.supplierID = supplierID;
        this.contractID = contractID;
        this.productID = productID;
        this.catalogID = catalogID;
        this.catalogController = new CatalogController();
    }

    public int getSupplierID() 
    {
        return supplierID;
    }

    public int getContractID() 
    {
        return contractID;
    }

    public int getProductID() 
    {
        return productID;
    }

    public int getCatalogID() 
    {
        return catalogID;
    }

    public void setProductID(int productID) 
    {
        if (isPersisted) 
        {
            this.productID = productID;
            catalogController.update(supplierID, contractID, productID, "productID", String.valueOf(productID));
        }
    }
    
    public void setCatalogID(int catalogID) 
    {
        if (isPersisted) 
        {
            this.catalogID = catalogID;
                catalogController.update(supplierID, contractID, productID, "catalogID", String.valueOf(catalogID));
        }
    }

    public void persist() 
    {
        if (!isPersisted) 
        {
            isPersisted = true;
            catalogController.insert(this);
        }
    }
    
}
