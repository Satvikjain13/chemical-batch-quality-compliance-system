package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.CustomerOrder;
import com.chemicalmanagement.domain.CustomerStatus;

public class OrderService {
    public void validateOrder(CustomerOrder order){
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null.");
        }
        if(order.getCustomer().getCustomerStatus() == CustomerStatus.INACTIVE){
            throw new IllegalStateException("Orders from inactive customers cannot be validated.");
        }
        order.markAsValidated();
    }

}
