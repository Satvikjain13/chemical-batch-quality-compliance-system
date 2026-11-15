
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.CustomerOrder;

import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepository {

    void save(CustomerOrder customerOrder);

    Optional<CustomerOrder> findById(String orderId);

    List<CustomerOrder> findAll();

    boolean existsById(String orderId);
}
