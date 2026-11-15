package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.ProductionBatch;

import java.util.List;
import java.util.Optional;

public interface ProductionBatchRepository {
    void save(ProductionBatch batch);
    Optional<ProductionBatch> findById(String batchNumber);
    List<ProductionBatch> findAll();
    boolean existsById(String batchNumber);
}
