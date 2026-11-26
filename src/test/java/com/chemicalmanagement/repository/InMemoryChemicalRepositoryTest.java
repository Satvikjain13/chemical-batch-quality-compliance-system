package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Chemical;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class InMemoryChemicalRepositoryTest {
    private InMemoryChemicalRepository repository;
    @BeforeEach
    void setup(){
        repository = new InMemoryChemicalRepository();
    }
    private Chemical createChemical(String chemicalId,String name){
        return new Chemical(chemicalId,name,"Litres",365);
    }

    @Test
    void shouldSaveValidChemical(){
        Chemical sulphuricAcid = createChemical("CHEM-001","Sulphuric Acid");
        repository.save(sulphuricAcid);
        Assertions.assertTrue(repository.existsById("CHEM-001"));
    }

    @Test
    void shouldThrowExceptionWhenSavingNullChemical(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            repository.save(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateChemical() {
        Chemical sulphuricAcid = createChemical("CHEM-001", "Sulphuric Acid");
        Chemical hydrochloricAcid = createChemical("CHEM-001", "Hydrochloric Acid");

        repository.save(sulphuricAcid);
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(hydrochloricAcid)
        );
    }

    @Test
    void shouldReturnChemicalWhenIdExists(){
        Chemical sulphuricAcid = createChemical("CHEM-002","Sulphuric Acid");
        repository.save(sulphuricAcid);
        Optional<Chemical> result = repository.findById("CHEM-002");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Sulphuric Acid", result.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenChemicalDoesNotExist(){
        Optional<Chemical> result = repository.findById("CHEM-999");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenFindingChemicalWithNullId(){
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    repository.findById(null);
                });
    }

    @Test
    void shouldThrowExceptionWhenFindingChemicalWithBlankId(){
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    repository.findById("  ");
                });
    }

    @Test
    void shouldThrowExceptionWhenFindingChemicalWithEmptyId(){
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    repository.findById("");
                });
    }

    @Test
    void shouldReturnAllSavedChemicals(){
        Chemical sulphuricAcid = createChemical("CHEM-001","Sulphuric Acid");
        Chemical hydrochloricAcid = createChemical("CHEM-002","Hydrochloric Acid");
        repository.save(sulphuricAcid);
        repository.save(hydrochloricAcid);
        List<Chemical> resultList = repository.findAll();
        Assertions.assertEquals(2,resultList.size());
        Assertions.assertTrue(resultList.contains(sulphuricAcid));
        Assertions.assertTrue(resultList.contains(hydrochloricAcid));
    }

    @Test
    void shouldReturnEmptyListWhenNoChemicalsAreSaved(){
        List<Chemical> resultList = repository.findAll();
        Assertions.assertTrue(resultList.isEmpty());
    }

    @Test
    void shouldNotModifyRepositoryWhenReturnedListIsModified(){
        Chemical sulphuricAcid = createChemical("CHEM-001","Sulphuric Acid");
        Chemical hydrochloricAcid = createChemical("CHEM-002","Hydrochloric Acid");
        repository.save(sulphuricAcid);
        List<Chemical> resultList = repository.findAll();
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> resultList.add(hydrochloricAcid)
        );
        Assertions.assertTrue(repository.existsById("CHEM-001"));
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldReturnTrueWhenChemicalExists(){
        Chemical sulphuricAcid = createChemical("CHEM-001","Sulphuric Acid");
        repository.save(sulphuricAcid);
        Assertions.assertTrue(repository.existsById("CHEM-001"));
    }

    @Test
    void shouldReturnFalseWhenChemicalDoesNotExist(){
        Assertions.assertFalse(repository.existsById("CHEM-1028"));
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithNullId(){
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    repository.existsById(null);
                }
        );
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithEmptyId(){
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    repository.existsById("");
                }
        );
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithBlankId(){
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    repository.existsById("   ");
                }
        );
    }


}
