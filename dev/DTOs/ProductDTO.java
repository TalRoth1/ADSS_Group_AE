package DTOs;

public class ProductDTO {
    private final int productID;
    private final String name;
    private final double sellingPrice;
    private final int discount;
    private final int producerID;
    private final String[] categories;

    public ProductDTO(int productID, String name, double sellingPrice, int discount, int producerID,
            String[] categories) {
        this.productID = productID;
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.producerID = producerID;
        this.categories = categories;
    }

    public int getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public int getProducerID() {
        return producerID;
    }

    public String[] getCategories() {
        return categories;
    }
}
