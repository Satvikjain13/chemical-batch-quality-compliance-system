
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> customerMap = new HashMap<>();

    @Override
    public void save(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        if (customerMap.containsKey(customer.getCustomerId())) {
            throw new IllegalArgumentException(
                    "Customer with ID " + customer.getCustomerId() + " already exists."
            );
        }

        customerMap.put(customer.getCustomerId(), customer);
    }

    @Override
    public Optional<Customer> findById(String customerId) {

        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be null or blank.");
        }

        return Optional.ofNullable(customerMap.get(customerId));
    }

    @Override
    public List<Customer> findAll() {
        return List.copyOf(customerMap.values());
    }

    @Override
    public boolean existsById(String customerId) {

        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be null or blank.");
        }

        return customerMap.containsKey(customerId);
    }
}
