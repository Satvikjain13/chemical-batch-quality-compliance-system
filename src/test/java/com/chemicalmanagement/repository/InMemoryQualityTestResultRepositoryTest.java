
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Chemical;
import com.chemicalmanagement.domain.ProductionBatch;
import com.chemicalmanagement.domain.QualityTest;
import com.chemicalmanagement.domain.QualityTestResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class InMemoryQualityTestResultRepositoryTest {

    private InMemoryQualityTestResultRepository repository;
    private ProductionBatch batch;
    private QualityTest qualityTest;

    @BeforeEach
    void setup() {
        repository = new InMemoryQualityTestResultRepository();

        Chemical chemical = new Chemical(
                "CHEM-001", "Sulphuric Acid", "Litres", 365
        );

        batch = new ProductionBatch(
                "BATCH-001", chemical,
                LocalDate.of(2026, 11, 1),
                new BigDecimal("1500")
        );

        qualityTest = new QualityTest(
                "TEST-001", "Purity Test",
                new BigDecimal("95"),
                new BigDecimal("100"),
                "Percentage"
        );
    }

    private QualityTestResult createResult(String id) {
        return new QualityTestResult(
                id, batch, qualityTest, new BigDecimal("98")
        );
    }

    @Test
    void shouldSaveValidQualityTestResult() {
        QualityTestResult result = createResult("RESULT-001");
        repository.save(result);
        Assertions.assertTrue(repository.existsById("RESULT-001"));
    }

    @Test
    void shouldThrowExceptionWhenSavingNullQualityTestResult() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateQualityTestResult() {
        repository.save(createResult("RESULT-001"));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.save(createResult("RESULT-001")));
    }

    @Test
    void shouldReturnResultWhenIdExists() {
        repository.save(createResult("RESULT-001"));

        Optional<QualityTestResult> result =
                repository.findById("RESULT-001");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("RESULT-001", result.get().getResultId());
    }

    @Test
    void shouldReturnEmptyWhenResultDoesNotExist() {
        Assertions.assertTrue(repository.findById("RESULT-999").isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenFindingResultWithNullId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.findById(null));
    }

    @Test
    void shouldThrowExceptionWhenFindingResultWithBlankId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.findById("  "));
    }

    @Test
    void shouldThrowExceptionWhenFindingResultWithEmptyId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.findById(""));
    }

    @Test
    void shouldReturnAllSavedResults() {
        QualityTestResult result1 = createResult("RESULT-001");
        QualityTestResult result2 = createResult("RESULT-002");

        repository.save(result1);
        repository.save(result2);

        List<QualityTestResult> results = repository.findAll();

        Assertions.assertEquals(2, results.size());
        Assertions.assertTrue(results.contains(result1));
        Assertions.assertTrue(results.contains(result2));
    }

    @Test
    void shouldReturnEmptyListWhenNoResultsAreSaved() {
        Assertions.assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldReturnUnmodifiableList() {
        repository.save(createResult("RESULT-001"));
        List<QualityTestResult> results = repository.findAll();

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> results.add(createResult("RESULT-002")));
    }

    @Test
    void shouldReturnTrueWhenResultExists() {
        repository.save(createResult("RESULT-001"));
        Assertions.assertTrue(repository.existsById("RESULT-001"));
    }

    @Test
    void shouldReturnFalseWhenResultDoesNotExist() {
        Assertions.assertFalse(repository.existsById("RESULT-999"));
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithNullId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.existsById(null));
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithEmptyId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.existsById(""));
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithBlankId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.existsById("  "));
    }

    @Test
    void shouldNotModifyRepositoryWhenReturnedListIsModified() {
        repository.save(createResult("RESULT-001"));
        List<QualityTestResult> results = repository.findAll();

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> results.add(createResult("RESULT-002")));

        Assertions.assertEquals(1, repository.findAll().size());
        Assertions.assertTrue(repository.existsById("RESULT-001"));
    }
}
