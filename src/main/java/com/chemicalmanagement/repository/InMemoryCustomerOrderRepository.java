
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.CustomerOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerOrderRepository implements CustomerOrderRepository {

    private final Map<String, CustomerOrder> customerOrderMap = new HashMap<>();

    @Override
    public void save(CustomerOrder customerOrder) {

        if (customerOrder == null) {
            throw new IllegalArgumentException("Customer order cannot be null.");
        }

        if (customerOrderMap.containsKey(customerOrder.getOrderId())) {
            throw new IllegalArgumentException(
                    "Customer order with ID " + customerOrder.getOrderId() + " already exists."
            );
        }

        customerOrderMap.put(customerOrder.getOrderId(), customerOrder);
    }

    @Override
    public Optional<CustomerOrder> findById(String orderId) {

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank.");
        }

        return Optional.ofNullable(customerOrderMap.get(orderId));
    }

    @Override
    public List<CustomerOrder> findAll() {
        return List.copyOf(customerOrderMap.values());
    }

    @Override
    public boolean existsById(String orderId) {

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank.");
        }

        return customerOrderMap.containsKey(orderId);
    }
}
