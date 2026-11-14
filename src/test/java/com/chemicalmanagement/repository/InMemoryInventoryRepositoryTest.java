package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class InMemoryInventoryRepositoryTest {

    private InMemoryInventoryRepository repository;
    private Inventory inventory1;
    private Inventory inventory2;
    private Inventory duplicateInventory ;
    private Chemical sulphurAcid;
    private ProductionBatch batch1;
    private ProductionBatch batch2;
    private ProductionBatch batch3;

    @BeforeEach
    void setup(){
        repository = new InMemoryInventoryRepository();

        sulphurAcid = new Chemical("CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365);

        batch1 = new ProductionBatch("BATCH-001",
                sulphurAcid,
                LocalDate.of(2026,11,01),
                new BigDecimal("1000"));

        batch2 = new ProductionBatch("BATCH-002",
                sulphurAcid,
                LocalDate.of(2026,11,11),
                new BigDecimal("1500"));

        batch3 = new ProductionBatch("BATCH-003",
                sulphurAcid,
                LocalDate.of(2026,11,11),
                new BigDecimal("500")
                );

        batch1.completeQualityEvaluation(QualityStatus.PASSED);
        batch2.completeQualityEvaluation(QualityStatus.PASSED);
        batch3.completeQualityEvaluation(QualityStatus.PASSED);

        inventory1 = new Inventory("INV-001",
                batch1);

        duplicateInventory  = new Inventory("INV-001",
                batch2);

        inventory2 = new Inventory("INV-002",
                batch3);


    }
    @Test
    void shouldSaveInventorySuccessfully(){
        repository.save(inventory1);
        Assertions.assertTrue(repository.existsById("INV-001"));
    }

    @Test
    void shouldThrowExceptionWhenSavingNullInventory(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            repository.save(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateInventoryId(){
        repository.save(inventory1);
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            repository.save(duplicateInventory);
        });
    }

    @Test
    void shouldFindInventoryByExistingId(){
        repository.save(inventory1);

        Optional<Inventory> result = repository.findById(inventory1.getInventoryId());
        Assertions.assertTrue(result.isPresent());

        Assertions.assertSame(inventory1, result.get());
    }

    @Test
    void shouldReturnEmptyWhenInventoryIdDoesNotExist(){
        Assertions.assertTrue(repository.findById("INV-999").isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenFindingInventoryWithNullId(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            repository.findById(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenFindingInventoryWithBlankId(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            repository.findById("   ");
        });
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryIsEmpty(){
        Assertions.assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldReturnAllSavedInventories(){
        repository.save(inventory1);
        repository.save(inventory2);
        Assertions.assertEquals(2,repository.findAll().size());
        Assertions.assertTrue(repository.findAll().contains(inventory1));
        Assertions.assertTrue(repository.findAll().contains(inventory2));
    }

    @Test
    void shouldReturnUnmodifiableListFromFindAll(){
        repository.save(inventory1);
        List<Inventory> result =repository.findAll();
        Assertions.assertThrows(UnsupportedOperationException.class,() ->{
            result.add(inventory2);
        });
    }

    @Test
    void shouldReturnTrueWhenInventoryExists(){
        repository.save(inventory1);
        Assertions.assertTrue(repository.existsById("INV-001"));
    }

    @Test
    void shouldReturnFalseWhenInventoryDoesNotExist(){
        Assertions.assertFalse(repository.existsById("INV-999"));
    }

    @Test
    void shouldThrowExceptionWhenCheckingNullInventoryId(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            repository.existsById(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenCheckingBlankInventoryId(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            repository.existsById("    ");
        });
    }

    @Test
    void shouldThrowExceptionWhenCheckingEmptyInventoryId(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            repository.existsById("");
        });
    }


}
