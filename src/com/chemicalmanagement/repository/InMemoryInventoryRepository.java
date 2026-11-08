package com.chemicalmanagement.repository;
import com.chemicalmanagement.domain.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryInventoryRepository implements InventoryRepository {

    private final Map<String,Inventory> inventoryMap = new HashMap<>();

    @Override
    public void save(Inventory inventory){
        if(inventory == null){
            throw new IllegalArgumentException("Inventory cannot be null.");
        }
        if(inventoryMap.containsKey(inventory.getInventoryId())){
            throw new IllegalArgumentException("Duplicate inventory ID is not allowed.");
        }
        inventoryMap.put(inventory.getInventoryId(),inventory);
    }

    @Override
    public Optional<Inventory> findById(String inventoryId){
        if(inventoryId == null || inventoryId.isBlank()){
            throw new IllegalArgumentException("InventoryId cannot be Null or Blank");
        }
       return Optional.ofNullable(inventoryMap.get(inventoryId));
    }

    @Override
    public List<Inventory> findAll(){
        return List.copyOf(inventoryMap.values());
    }

    @Override
    public boolean existsById(String inventoryId){
        if(inventoryId == null || inventoryId.isBlank()){
            throw new IllegalArgumentException("InventoryId cannot be Null or Blank");
        }
        return inventoryMap.containsKey(inventoryId);
    }
}
