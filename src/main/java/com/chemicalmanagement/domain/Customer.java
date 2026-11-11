package com.chemicalmanagement.domain;

public class Customer {
    private final String customerId;
    private String customerName;
    private String contactNumber;
    private String email;
    private String address;
    private CustomerStatus customerStatus;

    public Customer(String customerId,String customerName,String contactNumber,String email,String address){
        if(customerId == null || customerId.isBlank()){
            throw new IllegalArgumentException("Customer Id cannot be null or Blank.");
        }
        if(customerName == null || customerName.isBlank()){
            throw new IllegalArgumentException("Customer Name cannot be null or blank.");
        }
        if(contactNumber == null || contactNumber.isBlank()){
            throw new IllegalArgumentException("Contact Number cannot be null or Blank");
        }
        if(email != null && email.isBlank()){
            throw new IllegalArgumentException("Email cannot be blank.");
        }
        if(address != null && address.isBlank()){
            throw new IllegalArgumentException("Address cannot be blank.");
        }
        validateContactNumber(contactNumber);
        validateEmail(email);
        this.customerId = customerId;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.customerStatus = CustomerStatus.ACTIVE;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerName(String customerName) {
        if(customerName == null || customerName.isBlank()){
            throw new IllegalArgumentException("Customer Name cannot be null or blank.");
        }
        this.customerName = customerName;
    }

    public void setContactNumber(String contactNumber) {
        if(contactNumber == null || contactNumber.isBlank()){
            throw new IllegalArgumentException("Contact Number cannot be null or Blank");
        }
        validateContactNumber(contactNumber);
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        if(email != null && email.isBlank()){
            throw new IllegalArgumentException("Email cannot be blank.");
        }
        validateEmail(email);
        this.email = email;
    }

    public void setAddress(String address) {
        if(address != null && address.isBlank()){
            throw new IllegalArgumentException("Address cannot be blank.");
        }
        this.address = address;
    }

    public void setCustomerStatus(CustomerStatus customerStatus) {
        if(customerStatus == null){
            throw new IllegalArgumentException("Customer Status cannot be null.");
        }
        this.customerStatus = customerStatus;
    }

    private void validateContactNumber(String contactNumber) {
        if (contactNumber == null || !contactNumber.matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException(
                    "Contact number must contain exactly 10 digits."
            );
        }
    }

    private void validateEmail(String email) {
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}
