package DTOs;

import java.util.List;
import java.util.Map;

import Domain.DeliveryMethod;
import Domain.DiscountDL;
import Domain.ProductBL;

public class ContractDTO {
    private int contractID;
    private Map<ProductBL, Integer> itemCatalog;
    private List<DiscountDL> billOfQuantities;
    private DeliveryMethod deliveryMethod;

    public ContractDTO(int contractID, Map<ProductBL, Integer> itemCatalog, List<DiscountDL> billOfQuantities,
            DeliveryMethod deliveryMethod) {
        this.contractID = contractID;
        this.itemCatalog = itemCatalog;
        this.billOfQuantities = billOfQuantities;
        this.deliveryMethod = deliveryMethod;
    }
}
