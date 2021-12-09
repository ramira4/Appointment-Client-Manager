package Model;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Customer Class
 */
public class Customer {
    private int customerId;
    private String customerName;
    private String address;
    private String phone;
    private String postalCode;
    private String division;
    private String country;

    /**
     * CUSTOMER CONSTRUCTOR
     * @param customerId
     * @param customerName
     * @param address
     * @param phone
     * @param postalCode
     * @param division
     * @param country
     */
    public Customer(int customerId, String customerName, String address, String phone, String postalCode, String division, String country) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.postalCode = postalCode;
        this.division = division;
        this.country = country;
    }

    /**
     * CUSTOMERS SETTERS AND GETTERS
     * @return
     */
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private StringProperty customer_Name;

    public Customer(String customer_name) {
        this.customer_Name = new SimpleStringProperty(customer_name);
    }

    public StringProperty nameProperty() {
        return customer_Name;

    }
}
