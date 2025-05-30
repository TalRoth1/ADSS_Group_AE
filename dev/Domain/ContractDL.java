package Domain;

import java.util.List;
import java.util.Map;

public class ContractDL {
    private int contractID;
    private Map<ProductBL, Integer> itemCatalog;
    private List<DiscountDL> billOfQuantities;
    private DeliveryMethod deliveryMethod;

    public ContractDL(int contractID,Map <ProductBL, Integer> itemCatalog, List<DiscountDL> billOfQuantities ,DeliveryMethod deliveryMethod) {
        this.contractID = contractID;
        this.itemCatalog = itemCatalog;
        this.billOfQuantities = billOfQuantities;
        this.deliveryMethod = deliveryMethod;
    }

    public int getContractID() {
        return contractID;
    }

    public List<DiscountDL> getBillOfQuantities() {
        return billOfQuantities;
    }

    public DiscountDL getDiscount(int itemID) {
        for (DiscountDL discount : billOfQuantities) {
            if (discount.getCatalogID() == itemID) {
                return discount;
            }
        }
        return null;
    }

    public void setBillOfQuantities(List<DiscountDL> billOfQuantities) {
        this.billOfQuantities = billOfQuantities;
    }
    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public ProductBL getItem(int itemID) {
        for (ProductBL item : itemCatalog.keySet()) {
            if (item.getProductID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public int getItemCatalogID(int itemID) {
        for (Map.Entry<ProductBL, Integer> entry : itemCatalog.entrySet()) {
            if (entry.getKey().getProductID() == itemID) {
                return entry.getValue();
            }
        }
        return -1;
    }

    public Map<ProductBL, Integer> getItemCatalog()
    {
        return itemCatalog;
    }
}