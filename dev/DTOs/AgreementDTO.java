package DTOs;

import java.util.List;
import java.util.Map;

import Domain.DeliveryMethod;
import Domain.DiscountDL;
import Domain.Item;

public class AgreementDTO {
    private int agreementID;
    private Map<Item, Integer> itemCatalog;
    private List<DiscountDL> billOfQuantities;
    private DeliveryMethod deliveryMethod;

    public AgreementDTO(int agreementID, Map<Item, Integer> itemCatalog, List<DiscountDL> billOfQuantities,
            DeliveryMethod deliveryMethod) {
        this.agreementID = agreementID;
        this.itemCatalog = itemCatalog;
        this.billOfQuantities = billOfQuantities;
        this.deliveryMethod = deliveryMethod;
    }
}
