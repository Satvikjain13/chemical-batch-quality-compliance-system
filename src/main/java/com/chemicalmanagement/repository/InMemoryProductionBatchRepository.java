package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.ProductionBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductionBatchRepository implements ProductionBatchRepository {

    private final Map<String,ProductionBatch> productionBatchMap = new HashMap<>();

    @Override
    public void save(ProductionBatch batch) {
        if(batch == null){
            throw new IllegalArgumentException("Batch cannot be null.");
        }
        if(productionBatchMap.containsKey(batch.getBatchNumber())){
            throw new IllegalArgumentException("Production Batch number " +batch.getBatchNumber()+ "already exists");
        }
        productionBatchMap.put(batch.getBatchNumber(),batch);
    }

    @Override
    public Optional<ProductionBatch> findById(String batchNumber) {
        if(batchNumber == null || batchNumber.isBlank()){
            throw new IllegalArgumentException("Batch Number cannot be null or blank.");
        }
       return Optional.ofNullable(productionBatchMap.get(batchNumber));
    }

    @Override
    public List<ProductionBatch> findAll() {
        return List.copyOf(productionBatchMap.values());
    }

    @Override
    public boolean existsById(String batchNumber) {
        if(batchNumber == null || batchNumber.isBlank()){
            throw new IllegalArgumentException("Batch Number cannot be null or blank.");
        }
        return productionBatchMap.containsKey(batchNumber);
    }
}
