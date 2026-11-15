package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Chemical;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryChemicalRepository implements ChemicalRepository {
    private final Map<String, Chemical> chemicalMap = new HashMap<>();

    @Override
    public void save(Chemical chemical){
        if(chemical == null){
            throw new IllegalArgumentException("Chemical cannot be null.");
        }
        if(chemicalMap.containsKey(chemical.getChemicalId())){
            throw new IllegalArgumentException(
                    "Chemical with ID " + chemical.getChemicalId() + " already exists."
            );
        }
        chemicalMap.put(chemical.getChemicalId(),chemical);
    }

    @Override
    public Optional<Chemical> findById(String chemicalId){
        if (chemicalId == null || chemicalId.isBlank()) {
            throw new IllegalArgumentException("Chemical ID cannot be null or blank."
            );
        }
        return Optional.ofNullable(chemicalMap.get(chemicalId));
    }

    @Override
    public List<Chemical> findAll(){
        return List.copyOf(chemicalMap.values());
    }

    @Override
    public boolean existsById(String chemicalId){
        if (chemicalId == null || chemicalId.isBlank()) {
            throw new IllegalArgumentException("Chemical ID cannot be null or blank."
            );
        }
        return chemicalMap.containsKey(chemicalId);
    }
}
