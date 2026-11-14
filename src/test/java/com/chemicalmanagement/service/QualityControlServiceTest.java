package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QualityControlServiceTest {

    Chemical sulphuricAcid;
    ProductionBatch batch1;
    QualityControlService service;
    QualityTest sulphuricPurity;
    QualityTest sulphurViscosity;
    QualityTest sulphurDensity;
    List<QualityTest> requiredTests;
    QualityTestResult resultBatch1Purity;
    QualityTestResult resultBatch1Viscosity;
    QualityTestResult resultBatch1Density;
    List<QualityTestResult> results ;
    @BeforeEach
    void setup(){
        sulphuricAcid = new Chemical("CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365);
        batch1 = new ProductionBatch("Batch-001",
                sulphuricAcid,
                LocalDate.of(2026,11,12),
                new BigDecimal("1000"));
        service = new QualityControlService();
        sulphuricPurity = new QualityTest("TEST-001",
                "Purity",
                new BigDecimal("95"),
                new BigDecimal("100"),
                "%");
        sulphurViscosity = new QualityTest("TEST-002",
                "Viscosity",
                new BigDecimal("10"),
                new BigDecimal("20"),
                "cP");
        sulphurDensity = new QualityTest("TEST-003",
                "Density",
                new BigDecimal("0"),
                new BigDecimal("1"),
                "g/cm3");
        requiredTests = new ArrayList<>();
        requiredTests.add(sulphuricPurity);
        requiredTests.add(sulphurViscosity);
        requiredTests.add(sulphurDensity);

        resultBatch1Purity = new QualityTestResult("QTR-001",
                batch1,
                sulphuricPurity,
                new BigDecimal("96")
        );
        resultBatch1Viscosity = new QualityTestResult("QTR-002",
                batch1,
                sulphurViscosity,
                new BigDecimal("12")
        );
        resultBatch1Density = new QualityTestResult("QTR-003",
                batch1,
                sulphurDensity,
                new BigDecimal("0")
        );

        results = new ArrayList<>();
        results.add(resultBatch1Purity);
        results.add(resultBatch1Viscosity);
        results.add(resultBatch1Density);
    }
    @Test
    void shouldThrowExceptionWhenBatchIsNull(){
        assertThrows(IllegalArgumentException.class,() -> {
            service.evaluateBatch(null,requiredTests,results);
        });
    }

    @Test
    void shouldThrowExceptionWhenRequiredTestsIsNull(){
        assertThrows(IllegalArgumentException.class,() -> {
            service.evaluateBatch(batch1,null,results);
        });
    }

    @Test
    void shouldThrowExceptionWhenRequiredTestsIsEmpty(){
        requiredTests.clear();
        assertThrows(IllegalArgumentException.class,() -> {
            service.evaluateBatch(batch1,requiredTests,results);
        });
    }

    @Test
    void shouldThrowExceptionWhenResultsIsNull(){
        assertThrows(IllegalArgumentException.class,() -> {
            service.evaluateBatch(batch1,requiredTests,null);
        });
    }

    @Test
    void shouldThrowExceptionWhenRequiredTestsContainsNull(){
        requiredTests.clear();
        requiredTests.add(sulphuricPurity);
        requiredTests.add(null);

        assertThrows(IllegalArgumentException.class,() -> {
            service.evaluateBatch(batch1,requiredTests,results);
        });
    }

    @Test
    void shouldThrowExceptionWhenResultsContainsNull(){
        results.clear();
        results.add(resultBatch1Purity);
        results.add(null);

        assertThrows(IllegalArgumentException.class,() -> {
            service.evaluateBatch(batch1,requiredTests,results);
        });
    }

    @Test
    void shouldApproveBatchWhenAllRequiredTestsPass(){
        service.evaluateBatch(batch1,requiredTests,results);
        Assertions.assertEquals(QualityStatus.PASSED,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.APPROVED,batch1.getBatchStatus());
    }

    @Test
    void shouldQuarantineBatchWhenOneRequiredTestFails(){
        QualityTestResult failedViscosityResult = new QualityTestResult(
                "QTR-004",
                batch1,
                sulphurViscosity,
                new BigDecimal("25")
        );

        results.set(1,failedViscosityResult);
        service.evaluateBatch(batch1,requiredTests,results);
        Assertions.assertEquals(QualityStatus.FAILED,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUARANTINED,batch1.getBatchStatus());
    }

    @Test
    void shouldQuarantineBatchWhenMultipleRequiredTestsFail(){
        QualityTestResult failedSulphurResult = new QualityTestResult("QTR-005",
                batch1,
                sulphuricPurity,
                new BigDecimal("90"));

        QualityTestResult failedViscosityResult = new QualityTestResult("QTR-006",
                batch1,
                sulphurViscosity,
                new BigDecimal("25"));

        results.set(0,failedSulphurResult);
        results.set(1,failedViscosityResult);

        service.evaluateBatch(batch1,requiredTests,results);
        Assertions.assertEquals(QualityStatus.FAILED,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUARANTINED,batch1.getBatchStatus());
    }

    @Test
    void shouldQuarantineBatchWhenOneTestFailsAndAnotherIsMissing(){
        QualityTestResult failedSulphurResult = new QualityTestResult("QTR-007",
                batch1,
                sulphuricPurity,
                new BigDecimal("90"));
        results.remove(resultBatch1Viscosity);
        results.set(0,failedSulphurResult);

        service.evaluateBatch(batch1,requiredTests,results);
        Assertions.assertEquals(QualityStatus.FAILED,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUARANTINED,batch1.getBatchStatus());
    }

    @Test
    void shouldKeepBatchPendingWhenOneRequiredTestIsMissing(){
        results.remove(resultBatch1Viscosity);

        service.evaluateBatch(batch1,requiredTests,results);
        Assertions.assertEquals(QualityStatus.PENDING,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUALITY_PENDING,batch1.getBatchStatus());
    }

    @Test
    void shouldKeepBatchPendingWhenMultipleRequiredTestsAreMissing(){
        results.remove(resultBatch1Viscosity);
        results.remove(resultBatch1Density);

        service.evaluateBatch(batch1,requiredTests,results);
        Assertions.assertEquals(QualityStatus.PENDING,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUALITY_PENDING,batch1.getBatchStatus());
    }

    @Test
    void shouldKeepBatchPendingWhenResultsListIsEmpty(){
        results.clear();

        service.evaluateBatch(batch1,requiredTests,results);
        Assertions.assertEquals(QualityStatus.PENDING,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUALITY_PENDING,batch1.getBatchStatus());
    }

    @Test
    void shouldKeepBatchPendingWhenResultsBelongToAnotherBatch(){
        ProductionBatch batch2 = new ProductionBatch("Batch-002",
                sulphuricAcid,
                LocalDate.of(2026,11,01),
                new BigDecimal("1500")
        );
        QualityTestResult batch2Purity = new QualityTestResult(
                "QTR-008", batch2, sulphuricPurity, new BigDecimal("96")
        );

        QualityTestResult batch2Viscosity = new QualityTestResult(
                "QTR-009", batch2, sulphurViscosity, new BigDecimal("12")
        );

        QualityTestResult batch2Density = new QualityTestResult(
                "QTR-010", batch2, sulphurDensity, new BigDecimal("0.5")
        );

        results.clear();
        results.add(batch2Purity);
        results.add(batch2Viscosity);
        results.add(batch2Density);
        service.evaluateBatch(batch1,requiredTests,results);
        Assertions.assertEquals(QualityStatus.PENDING,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUALITY_PENDING,batch1.getBatchStatus());

    }

    @Test
    void shouldKeepBatchPendingWhenResultsBelongToDifferentQualityTests(){
        QualityTest sulphuricPurity2 = new QualityTest("TEST-004",
                "Purity",
                new BigDecimal("95"),
                new BigDecimal("100"),
                "%");
        QualityTest sulphurViscosity2 = new QualityTest("TEST-005",
                "Viscosity",
                new BigDecimal("10"),
                new BigDecimal("20"),
                "cP");
        QualityTest sulphurDensity2 = new QualityTest("TEST-006",
                "Density",
                new BigDecimal("0"),
                new BigDecimal("1"),
                "g/cm3");
        QualityTestResult resultBatch1Purity2 = new QualityTestResult("QTR-008",
                batch1,
                sulphuricPurity2,
                new BigDecimal("98"));
        QualityTestResult resultBatch1Viscosity2 = new QualityTestResult("QTR-009",
                batch1,
                sulphurViscosity2,
                new BigDecimal("15"));
        QualityTestResult resultBatch1Density2 = new QualityTestResult("QTR-010",
                batch1,
                sulphurDensity2,
                new BigDecimal("1"));
        List<QualityTestResult> results2 = new ArrayList<>();
        results2.add(resultBatch1Purity2);
        results2.add(resultBatch1Viscosity2);
        results2.add(resultBatch1Density2);

        service.evaluateBatch(batch1,requiredTests,results2);
        Assertions.assertEquals(QualityStatus.PENDING,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUALITY_PENDING,batch1.getBatchStatus());

    }

    @Test
    void shouldApproveBatchWhenCorrectResultsAreMixedWithUnrelatedResults() {

        // Create another batch
        ProductionBatch batch2 = new ProductionBatch(
                "Batch-002",
                sulphuricAcid,
                LocalDate.of(2026, 11, 13),
                new BigDecimal("500")
        );

        // Create a different quality test
        QualityTest temperatureTest = new QualityTest(
                "TEST-004",
                "Temperature",
                new BigDecimal("20"),
                new BigDecimal("40"),
                "C"
        );

        // Create unrelated results
        QualityTestResult unrelatedBatchResult = new QualityTestResult(
                "QTR-011",
                batch2,
                sulphuricPurity,
                new BigDecimal("96")
        );

        QualityTestResult unrelatedTestResult = new QualityTestResult(
                "QTR-012",
                batch1,
                temperatureTest,
                new BigDecimal("30")
        );

        // Add unrelated results alongside the correct results
        results.add(unrelatedBatchResult);
        results.add(unrelatedTestResult);

        // Evaluate batch1
        service.evaluateBatch(batch1, requiredTests, results);

        // Verify the expected outcome
        Assertions.assertEquals(
                QualityStatus.PASSED,
                batch1.getQualityStatus()
        );

        Assertions.assertEquals(
                BatchStatus.APPROVED,
                batch1.getBatchStatus()
        );
    }

}
