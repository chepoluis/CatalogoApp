package app.catalogo.com.catalogoapp.creditPurchase;

public class CreditObject {
    private String saleId;
    private String customerName;
    private String customerAddress;
    private String customerCity;
    private String customerPhone;
    private String customerEmail;
    private String seller;
    private String productPurchased;
    private String productCost;
    private String customerImage;
    private String productImage;
    private String productDescription;
    private String saleDate;

    private String debt;
    private String collectWhen;
    private String collectDay;
    private String firstHour;
    private String secondHour;
    private String amountMoney;
    private String initialPay;
    private String numberPayments;

    public CreditObject(String saleId, String customerName, String customerAddress, String customerCity, String customerPhone, String customerEmail, String seller, String productPurchased, String productCost, String customerImage, String productImage, String productDescription, String saleDate, String debt, String collectWhen, String collectDay, String firstHour, String secondHour, String amountMoney, String numberPayments) {
        this.saleId = saleId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerCity = customerCity;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.seller = seller;
        this.productPurchased = productPurchased;
        this.productCost = productCost;
        this.customerImage = customerImage;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.saleDate = saleDate;
        this.debt = debt;
        this.collectWhen = collectWhen;
        this.collectDay = collectDay;
        this.firstHour = firstHour;
        this.secondHour = secondHour;
        this.amountMoney = amountMoney;
        this.numberPayments = numberPayments;
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

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getCollectWhen() {
        return collectWhen;
    }

    public void setCollectWhen(String collectWhen) {
        this.collectWhen = collectWhen;
    }

    public String getCollectDay() {
        return collectDay;
    }

    public void setCollectDay(String collectDay) {
        this.collectDay = collectDay;
    }

    public String getFirstHour() {
        return firstHour;
    }

    public void setFirstHour(String firstHour) {
        this.firstHour = firstHour;
    }

    public String getSecondHour() {
        return secondHour;
    }

    public void setSecondHour(String secondHour) {
        this.secondHour = secondHour;
    }

    public String getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(String amountMoney) {
        this.amountMoney = amountMoney;
    }

    public String getInitialPay() {
        return initialPay;
    }

    public void setInitialPay(String initialPay) {
        this.initialPay = initialPay;
    }

    public String getNumberPayments() {
        return numberPayments;
    }

    public void setNumberPayments(String numberPayments) {
        this.numberPayments = numberPayments;
    }
}
