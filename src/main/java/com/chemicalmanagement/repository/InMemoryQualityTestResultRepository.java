
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.QualityTestResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryQualityTestResultRepository implements QualityTestResultRepository {

    private final Map<String, QualityTestResult> qualityTestResultMap = new HashMap<>();

    @Override
    public void save(QualityTestResult qualityTestResult) {

        if (qualityTestResult == null) {
            throw new IllegalArgumentException("Quality test result cannot be null.");
        }

        if (qualityTestResultMap.containsKey(qualityTestResult.getResultId())) {
            throw new IllegalArgumentException(
                    "Quality test result with ID " + qualityTestResult.getResultId() + " already exists."
            );
        }

        qualityTestResultMap.put(qualityTestResult.getResultId(), qualityTestResult);
    }

    @Override
    public Optional<QualityTestResult> findById(String resultId) {

        if (resultId == null || resultId.isBlank()) {
            throw new IllegalArgumentException("Result ID cannot be null or blank.");
        }

        return Optional.ofNullable(qualityTestResultMap.get(resultId));
    }

    @Override
    public List<QualityTestResult> findAll() {
        return List.copyOf(qualityTestResultMap.values());
    }

    @Override
    public boolean existsById(String resultId) {

        if (resultId == null || resultId.isBlank()) {
            throw new IllegalArgumentException("Result ID cannot be null or blank.");
        }

        return qualityTestResultMap.containsKey(resultId);
    }
}
