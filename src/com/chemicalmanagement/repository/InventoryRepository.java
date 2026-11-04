package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    void save(Inventory inventory);
    Optional<Inventory> findById(String inventoryId);
    List<Inventory> findAll();
    boolean existsById(String inventoryId);
}
