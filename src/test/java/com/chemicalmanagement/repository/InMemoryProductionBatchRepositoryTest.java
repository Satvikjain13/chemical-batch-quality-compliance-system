
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Chemical;
import com.chemicalmanagement.domain.ProductionBatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class InMemoryProductionBatchRepositoryTest {

    private InMemoryProductionBatchRepository repository;
    private Chemical chemical;

    @BeforeEach
    void setup() {
        repository = new InMemoryProductionBatchRepository();
        chemical = new Chemical(
                "CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365
        );
    }

    private ProductionBatch createBatch(String batchNumber) {
        return new ProductionBatch(batchNumber, chemical, LocalDate.of(2026,11,1),new BigDecimal("1500"));
    }

    @Test
    void shouldSaveValidProductionBatch() {
        ProductionBatch batch = createBatch("BATCH-001");

        repository.save(batch);

        Assertions.assertTrue(repository.existsById("BATCH-001"));
    }

    @Test
    void shouldThrowExceptionWhenSavingNullBatch() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateBatch() {
        ProductionBatch batch1 = createBatch("BATCH-001");
        ProductionBatch batch2 = createBatch("BATCH-001");

        repository.save(batch1);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(batch2)
        );
    }

    @Test
    void shouldReturnBatchWhenIdExists() {
        ProductionBatch batch = createBatch("BATCH-001");

        repository.save(batch);

        Optional<ProductionBatch> result =
                repository.findById("BATCH-001");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(
                "BATCH-001",
                result.get().getBatchNumber()
        );
    }

    @Test
    void shouldReturnEmptyWhenBatchDoesNotExist() {
        Optional<ProductionBatch> result =
                repository.findById("BATCH-999");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenFindingBatchWithNullId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingBatchWithBlankId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById("  ")
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingBatchWithEmptyId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById("")
        );
    }

    @Test
    void shouldReturnAllSavedBatches() {
        ProductionBatch batch1 = createBatch("BATCH-001");
        ProductionBatch batch2 = createBatch("BATCH-002");

        repository.save(batch1);
        repository.save(batch2);

        List<ProductionBatch> resultList = repository.findAll();

        Assertions.assertEquals(2, resultList.size());
        Assertions.assertTrue(resultList.contains(batch1));
        Assertions.assertTrue(resultList.contains(batch2));
    }

    @Test
    void shouldReturnEmptyListWhenNoBatchesAreSaved() {
        List<ProductionBatch> resultList = repository.findAll();

        Assertions.assertTrue(resultList.isEmpty());
    }

    @Test
    void shouldReturnUnmodifiableList() {
        ProductionBatch batch1 = createBatch("BATCH-001");
        ProductionBatch batch2 = createBatch("BATCH-002");

        repository.save(batch1);

        List<ProductionBatch> resultList = repository.findAll();

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> resultList.add(batch2)
        );
    }

    @Test
    void shouldReturnTrueWhenBatchExists() {
        repository.save(createBatch("BATCH-001"));

        Assertions.assertTrue(repository.existsById("BATCH-001"));
    }

    @Test
    void shouldReturnFalseWhenBatchDoesNotExist() {
        Assertions.assertFalse(repository.existsById("BATCH-999"));
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithNullId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithEmptyId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById("")
        );
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithBlankId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById("   ")
        );
    }

    @Test
    void shouldNotModifyRepositoryWhenReturnedListIsModified() {
        ProductionBatch batch1 = createBatch("BATCH-001");
        ProductionBatch batch2 = createBatch("BATCH-002");

        repository.save(batch1);

        List<ProductionBatch> resultList = repository.findAll();

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> resultList.add(batch2)
        );

        Assertions.assertTrue(repository.existsById("BATCH-001"));
        Assertions.assertEquals(1, repository.findAll().size());
    }
}
