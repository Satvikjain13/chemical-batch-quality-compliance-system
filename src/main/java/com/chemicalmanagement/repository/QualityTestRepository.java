
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.QualityTest;

import java.util.List;
import java.util.Optional;

public interface QualityTestRepository {

    void save(QualityTest qualityTest);

    Optional<QualityTest> findById(String testId);

    List<QualityTest> findAll();

    boolean existsById(String testId);
}
