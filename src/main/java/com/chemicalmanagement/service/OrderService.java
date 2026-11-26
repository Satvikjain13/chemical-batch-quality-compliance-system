package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.CustomerOrder;
import com.chemicalmanagement.domain.CustomerStatus;
import com.chemicalmanagement.domain.OrderStatus;

public class OrderService {
    private final InventoryService inventoryService;

    public OrderService(InventoryService inventoryService){
        if(inventoryService == null){
            throw new IllegalArgumentException("Inventory Service cannot be null.");
        }
        this.inventoryService = inventoryService;
    }
    public void validateOrder(CustomerOrder order){
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null.");
        }
        if(order.getCustomer().getCustomerStatus() != CustomerStatus.ACTIVE){
            throw new IllegalStateException("Orders from inactive customers cannot be validated.");
        }
        order.markAsValidated();
    }

    public boolean reserveOrder(CustomerOrder order) {
        if(order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        if(order.getOrderStatus() != OrderStatus.VALIDATED) {
            throw new IllegalStateException(
                    "Only validated orders can be reserved."
            );
        }

        return !inventoryService
                .reserveInventoryForOrder(order)
                .isEmpty();
    }

    public void cancelOrder(CustomerOrder order){
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null.");
        }
        OrderStatus status = order.getOrderStatus();
        if (status != OrderStatus.CREATED &&
                status != OrderStatus.VALIDATED &&
                status != OrderStatus.RESERVED) {

            throw new IllegalStateException(
                    "Order cannot be cancelled in its current status."
            );
        }
        if(status == OrderStatus.RESERVED){
            inventoryService.releaseReservationForOrder(order);

        }
        order.markAsCancelled();
    }

    public void dispatchOrder(CustomerOrder order){
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null");
        }
        if(OrderStatus.RESERVED != order.getOrderStatus()){
            throw new IllegalStateException("Only reserved order can be dispatched.");
        }
        inventoryService.dispatchReservationForOrder(order);
        order.markAsDispatched();
    }

}
