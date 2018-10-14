package app.catalogo.com.catalogoapp.Model;

public class Customer {
    private String customerKey;
    private String name;
    private String city;
    private String address;
    private String phoneNumber;
    private String email;
    private String image;

    public Customer() {
    }

    public Customer(String customerKey, String name, String city, String direction, String phoneNumber, String email, String image) {
        this.customerKey = customerKey;
        this.name = name;
        this.city = city;
        this.address = direction;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
