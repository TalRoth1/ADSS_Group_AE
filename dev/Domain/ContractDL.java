package Domain;

import java.util.List;
import java.util.Map;

public class ContractDL {
    private int agreementID;
    private Map<Item, Integer> itemCatalog;
    private List<DiscountDL> billOfQuantities;
    private DeliveryMethod deliveryMethod;

    public ContractDL(int agreementID,Map <Item, Integer> itemCatalog, List<DiscountDL> billOfQuantities ,DeliveryMethod deliveryMethod) {
        this.agreementID = agreementID;
        this.itemCatalog = itemCatalog;
        this.billOfQuantities = billOfQuantities;
        this.deliveryMethod = deliveryMethod;
    }

    public int getAgreementID() {
        return agreementID;
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

    public Item getItem(int itemID) {
        for (Item item : itemCatalog.keySet()) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public int getItemCatalogID(int itemID) {
        for (Map.Entry<Item, Integer> entry : itemCatalog.entrySet()) {
            if (entry.getKey().getItemID() == itemID) {
                return entry.getValue();
            }
        }
        return -1;
    }

    public Map<Item, Integer> getItemCatalog()
    {
        return itemCatalog;
    }
}