
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    void save(Customer customer);

    Optional<Customer> findById(String customerId);

    List<Customer> findAll();

    boolean existsById(String customerId);
}
