package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Chemical;

import java.util.List;
import java.util.Optional;

public interface ChemicalRepository {
    void save(Chemical chemical);
    Optional<Chemical> findById(String chemicalId);
    List<Chemical> findAll();
    boolean existsById(String chemicalId);
}
