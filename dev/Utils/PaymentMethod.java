package Utils;

public enum PaymentMethod 
{
    CASH,
    CREDIT;

    public static PaymentMethod getPaymentMethod(String paymentMethod) {
        switch (paymentMethod.toUpperCase()) 
        {
            case "CASH":
                return CASH;
            case "CREDIT":
                return CREDIT;
            default:
                return null;
        }
    }
}
