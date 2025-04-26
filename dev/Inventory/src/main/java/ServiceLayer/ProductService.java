package ServiceLayer;

import BusinessLayer.ProductFacade;

public class ProductService {
    private ProductFacade pf;

    public ProductService() {
        pf = ProductFacade.getInstance();
    }

    public Response AddProduct(String name, double costPrice, double sellingPrice, int discount,
            int producerID, String[] categories) {
        try {
            int productID = pf.addProduct(name, costPrice, sellingPrice, discount, producerID, categories);
            return new Response(String.valueOf(productID), null);
        } catch (Exception e) {
            return new Response(null, e.getMessage());
        }
    }

    public Response RemoveProduct(int productID) {
        try {
            pf.removeProduct(productID);
            return new Response("Product removed successfully", null);
        } catch (Exception e) {
            return new Response(null, e.getMessage());
        }
    }

    public Response UpdateProduct(int productID, String name, double costPrice, double sellingPrice, int discount,
            int producerID, String[] categories) {
        try {
            pf.updateProduct(productID, name, costPrice, sellingPrice, discount, producerID, categories);
            return new Response("Product updated successfully", null);
        } catch (Exception e) {
            return new Response(null, e.getMessage());
        }
    }

    public Response GetAllProducts() {
        try {
            String productsList = ProductFacade.getInstance().getListOfAllProducts();
            return new Response(productsList, null);
        } catch (Exception e) {
            return new Response(null, e.getMessage());
        }
    }

    public Response setMinQuantity(int productID, int branchID, int minQuantity) {
        try {
            pf.setMinQuantity(productID, branchID, minQuantity);
            return new Response("Minimal quantity set successfully.", null);
        } catch (Exception e) {
            return new Response(null, e.getMessage());
        }
    }

}
