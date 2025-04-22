package Utils;

public enum DeliveryMethod{
    SCHEDULED,
    ON_ORDER,
    PICKUP;

    public static DeliveryMethod fromString(String method) {
        switch (method.toUpperCase())
        {
            case "SCHEDULED":
                return SCHEDULED;
            case "ON ORDER":
                return ON_ORDER;
            case "PICKUP":
                return PICKUP;
            default:
                return null;
        }
    }
}