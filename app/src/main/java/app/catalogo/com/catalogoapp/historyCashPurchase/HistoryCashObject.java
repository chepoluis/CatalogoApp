package app.catalogo.com.catalogoapp.historyCashPurchase;

public class HistoryCashObject {
    private String saleId;
    private String customerName;
    private String customerAddress;
    private String customerCity;
    private String customerPhone;
    private String seller;
    private String productPurchased;
    private String productCost;
    private String customerImage;
    private String productImage;
    private String productDescription;
    private String saleDate;

    public HistoryCashObject(String saleId, String customerName, String customerAddress, String customerCity, String customerPhone, String seller, String productPurchased, String productCost, String customerImage, String productImage, String productDescription, String saleDate) {
        this.saleId = saleId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerCity = customerCity;
        this.customerPhone = customerPhone;
        this.seller = seller;
        this.productPurchased = productPurchased;
        this.productCost = productCost;
        this.customerImage = customerImage;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.saleDate = saleDate;
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

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
