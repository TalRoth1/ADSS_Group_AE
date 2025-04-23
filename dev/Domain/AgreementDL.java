package Domain;

import java.util.List;
import java.util.Map;

public class AgreementDL {
    private int agreementID;
    private Map<Item, Integer> itemCatalog;
    private List<DiscountDL> billOfQuantities;

    public AgreementDL(int agreementID, List<DiscountDL> billOfQuantities) {
        this.agreementID = agreementID;
        this.billOfQuantities = billOfQuantities;
    }

    public int getAgreementID() {
        return agreementID;
    }

    public List<DiscountDL> getBillOfQuantities() {
        return billOfQuantities;
    }

    public DiscountDL getDiscount(int itemID) {
        for (DiscountDL discount : billOfQuantities) {
            if (discount.getcatalogID() == itemID) {
                return discount;
            }
        }
        return null;
    }

    public void setBillOfQuantities(List<DiscountDL> billOfQuantities) {
        this.billOfQuantities = billOfQuantities;
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