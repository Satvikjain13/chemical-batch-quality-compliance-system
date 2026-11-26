
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.QualityTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class InMemoryQualityTestRepositoryTest {

    private InMemoryQualityTestRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryQualityTestRepository();
    }

    private QualityTest createQualityTest(String id, String name) {
        return new QualityTest(
                id, name,
                new BigDecimal("95"),
                new BigDecimal("100"),
                "Percentage"
        );
    }

    @Test
    void shouldSaveValidQualityTest() {
        QualityTest test = createQualityTest("TEST-001", "Purity Test");
        repository.save(test);
        Assertions.assertTrue(repository.existsById("TEST-001"));
    }

    @Test
    void shouldThrowExceptionWhenSavingNullQualityTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateQualityTest() {
        repository.save(createQualityTest("TEST-001", "Purity Test"));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.save(createQualityTest("TEST-001", "Density Test")));
    }

    @Test
    void shouldReturnQualityTestWhenIdExists() {
        repository.save(createQualityTest("TEST-001", "Purity Test"));

        Optional<QualityTest> result = repository.findById("TEST-001");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Purity Test", result.get().getTestName());
    }

    @Test
    void shouldReturnEmptyWhenQualityTestDoesNotExist() {
        Assertions.assertTrue(repository.findById("TEST-999").isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenFindingQualityTestWithNullId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.findById(null));
    }

    @Test
    void shouldThrowExceptionWhenFindingQualityTestWithBlankId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.findById("  "));
    }

    @Test
    void shouldThrowExceptionWhenFindingQualityTestWithEmptyId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.findById(""));
    }

    @Test
    void shouldReturnAllSavedQualityTests() {
        QualityTest test1 = createQualityTest("TEST-001", "Purity Test");
        QualityTest test2 = createQualityTest("TEST-002", "Density Test");

        repository.save(test1);
        repository.save(test2);

        List<QualityTest> result = repository.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(test1));
        Assertions.assertTrue(result.contains(test2));
    }

    @Test
    void shouldReturnEmptyListWhenNoQualityTestsAreSaved() {
        Assertions.assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldReturnUnmodifiableList() {
        repository.save(createQualityTest("TEST-001", "Purity Test"));
        List<QualityTest> result = repository.findAll();

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> result.add(createQualityTest("TEST-002", "Density Test")));
    }

    @Test
    void shouldReturnTrueWhenQualityTestExists() {
        repository.save(createQualityTest("TEST-001", "Purity Test"));
        Assertions.assertTrue(repository.existsById("TEST-001"));
    }

    @Test
    void shouldReturnFalseWhenQualityTestDoesNotExist() {
        Assertions.assertFalse(repository.existsById("TEST-999"));
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
        repository.save(createQualityTest("TEST-001", "Purity Test"));
        List<QualityTest> result = repository.findAll();

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> result.add(createQualityTest("TEST-002", "Density Test")));

        Assertions.assertEquals(1, repository.findAll().size());
        Assertions.assertTrue(repository.existsById("TEST-001"));
    }
}
