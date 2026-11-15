
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.QualityTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryQualityTestRepository implements QualityTestRepository {

    private final Map<String, QualityTest> qualityTestMap = new HashMap<>();

    @Override
    public void save(QualityTest qualityTest) {

        if (qualityTest == null) {
            throw new IllegalArgumentException("Quality test cannot be null.");
        }

        if (qualityTestMap.containsKey(qualityTest.getTestId())) {
            throw new IllegalArgumentException(
                    "Quality test with ID " + qualityTest.getTestId() + " already exists."
            );
        }

        qualityTestMap.put(qualityTest.getTestId(), qualityTest);
    }

    @Override
    public Optional<QualityTest> findById(String testId) {

        if (testId == null || testId.isBlank()) {
            throw new IllegalArgumentException("Test ID cannot be null or blank.");
        }

        return Optional.ofNullable(qualityTestMap.get(testId));
    }

    @Override
    public List<QualityTest> findAll() {
        return List.copyOf(qualityTestMap.values());
    }

    @Override
    public boolean existsById(String testId) {

        if (testId == null || testId.isBlank()) {
            throw new IllegalArgumentException("Test ID cannot be null or blank.");
        }

        return qualityTestMap.containsKey(testId);
    }
}
