package app.catalogo.com.catalogoapp.Model;

public class Product {
    private String productKey;
    private String name;
    private String description;
    private String price;
    private String amount;
    private String image;

    public Product(String productKey, String name, String description, String price, String amount, String image) {
        this.productKey = productKey;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.image = image;
    }

    public Product() {
    }

    public String getProductKey() { return productKey; }

    public void setProductKey(String productKey) { this.productKey = productKey; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
