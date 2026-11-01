package com.chemicalmanagement.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CustomerOrder {
    private final String orderId;
    private final Customer customer;
    private final Chemical chemical;
    private final BigDecimal orderQuantity;
    private final LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private final String customerNameAtOrder;
    private final String customerContactAtOrder;
    private final String customerEmailAtOrder;
    private final String customerAddressAtOrder;

    public CustomerOrder(String orderId,
                         Customer customer,
                         Chemical chemical,
                         BigDecimal orderQuantity){
        if(orderId == null || orderId.isBlank()){
            throw new IllegalArgumentException("Order Id cannot be null or Blank");
        }
        if(customer == null){
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if(chemical == null){
            throw new IllegalArgumentException("Chemical cannotbe null.");
        }
        if(orderQuantity == null || orderQuantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("OrderQuantity cannot be null or should be greater than 0");
        }
        this.orderId = orderId;
        this.customer = customer;
        this.chemical = chemical;
        this.orderQuantity = orderQuantity;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.CREATED;
        this.customerNameAtOrder = customer.getCustomerName();
        this.customerContactAtOrder = customer.getContactNumber();
        this.customerEmailAtOrder = customer.getEmail();
        this.customerAddressAtOrder = customer.getAddress();
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Chemical getChemical() {
        return chemical;
    }

    public BigDecimal getOrderQuantity() {
        return orderQuantity;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getCustomerNameAtOrder() {
        return customerNameAtOrder;
    }

    public String getCustomerContactAtOrder() {
        return customerContactAtOrder;
    }

    public String getCustomerEmailAtOrder() {
        return customerEmailAtOrder;
    }

    public String getCustomerAddressAtOrder() {
        return customerAddressAtOrder;
    }

}
