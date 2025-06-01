package DAL;
public class ProductDAO {
    private boolean isPersisted = false;

    private final int productID;
    private String name;
    private double sellingPrice;
    private int discount;
    private final int producerID;
    private String[] categories;

    public ProductDAO(int productID, String name, double sellingPrice, int discount, int producerID,
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

    public void setName(String name) {
        if (isPersisted)
            this.name = name;
    }

    public void setSellingPrice(double price) {
        if (isPersisted)
            this.sellingPrice = price;
    }

    public void setDiscount(int discount) {
        if (isPersisted)
            this.discount = discount;
    }

    public void setCategories(String[] categories) {
        if (isPersisted)
            this.categories = categories;
    }

    public void persist() {
        this.isPersisted = true;
    }
}
