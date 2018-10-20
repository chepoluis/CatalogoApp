package app.catalogo.com.catalogoapp.historyCashPurchase;

public class HistoryCashObject {
    private String saleId;
    private String customerName;
    private String productPurchased;
    private String productCost;
    private String image;

    public HistoryCashObject(String saleId, String customerName, String productPurchased, String productCost, String image) {
        this.saleId = saleId;
        this.customerName = customerName;
        this.productPurchased = productPurchased;
        this.productCost = productCost;
        this.image = image;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductPurchased() {
        return productPurchased;
    }

    public void setProductPurchased(String productPurchased) {
        this.productPurchased = productPurchased;
    }

    public String getProductCost() {
        return productCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
