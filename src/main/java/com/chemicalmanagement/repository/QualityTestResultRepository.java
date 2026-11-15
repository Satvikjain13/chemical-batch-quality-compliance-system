
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.QualityTestResult;

import java.util.List;
import java.util.Optional;

public interface QualityTestResultRepository {

    void save(QualityTestResult qualityTestResult);

    Optional<QualityTestResult> findById(String resultId);

    List<QualityTestResult> findAll();

    boolean existsById(String resultId);
}
