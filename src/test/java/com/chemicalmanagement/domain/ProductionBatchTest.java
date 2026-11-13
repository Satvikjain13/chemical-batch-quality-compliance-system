package com.chemicalmanagement.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductionBatchTest {
    private Chemical sulphuricAcid;
    private ProductionBatch batch1;
    private BigDecimal quantity;
    private LocalDate manufacturingDate = LocalDate.of(2026, 1, 1);
    private LocalDate expectedExpiryDate = LocalDate.of(2027, 1, 1);

    @BeforeEach
    void setup() {
        sulphuricAcid = new Chemical("CHEM-001", "Sulphuric Acid", "Litres", 365);
         quantity = new BigDecimal("1000");
         batch1 = new ProductionBatch("BATCH-001",
                sulphuricAcid,
                manufacturingDate,
                quantity);
    }

    @Test
    void shouldThrowExceptionWhenProductionBatchNumberIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ProductionBatch(null, sulphuricAcid, LocalDate.now().minusDays(1), new BigDecimal("1000"));
        });
    }

    @Test
    void shouldThrowExceptionWhenProductionBatchNumberIsEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ProductionBatch("", sulphuricAcid, LocalDate.now().minusDays(1), new BigDecimal("1000"));
        });
    }

    @Test
    void shouldThrowExceptionWhenProductionBatchNumberIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ProductionBatch("   ", sulphuricAcid, LocalDate.now().minusDays(1), new BigDecimal("1000"));
        });
    }

    @Test
    void shouldThrowExceptionWhenChemicalIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ProductionBatch("BATCH-001", null, LocalDate.now().minusDays(1), new BigDecimal("1000"));

        });
    }

    @Test
    void shouldThrowExceptionWhenManufacturingDateIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ProductionBatch("BATCH-001", sulphuricAcid, null, new BigDecimal("1000"));
        });
    }

    @Test
    void shouldThrowExceptionWhenManufacturingDateIsFuture(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ProductionBatch("BATCH-001", sulphuricAcid, LocalDate.now().plusDays(1), new BigDecimal("1000"));
        });
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ProductionBatch("BATCH-001", sulphuricAcid, LocalDate.now().minusDays(1), null);
        });
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ProductionBatch("BATCH-001", sulphuricAcid, LocalDate.now().minusDays(1), new BigDecimal("0"));
        });
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ProductionBatch("BATCH-001", sulphuricAcid, LocalDate.now().minusDays(1), new BigDecimal("-5"));
        });
    }

    @Test
    void shouldCreateProductionBatchSuccessfully(){
        Assertions.assertEquals("BATCH-001",batch1.getBatchNumber());
        Assertions.assertEquals(sulphuricAcid,batch1.getChemical());
        Assertions.assertEquals(manufacturingDate,batch1.getManufacturingDate());
        Assertions.assertEquals(quantity,batch1.getQuantity());
        Assertions.assertEquals(QualityStatus.PENDING,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.IN_PRODUCTION,batch1.getBatchStatus());
    }

    @Test
    void shouldCalculateExpiryDateCorrectly(){
        Assertions.assertEquals(expectedExpiryDate,batch1.getExpiryDate());
    }

    @Test
    void shouldQuarantineBatchWhenQualityEvaluationFails(){
        batch1.completeQualityEvaluation(QualityStatus.FAILED);
        Assertions.assertEquals(QualityStatus.FAILED,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUARANTINED,batch1.getBatchStatus());
    }

    @Test
    void shouldApprovedBatchWhenQualityEvaluationPass(){
        batch1.completeQualityEvaluation(QualityStatus.PASSED);
        Assertions.assertEquals(QualityStatus.PASSED,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.APPROVED,batch1.getBatchStatus());
    }

    @Test
    void shouldKeepBatchInQualityPendingWhenEvaluationIsPending(){
        batch1.completeQualityEvaluation(QualityStatus.PENDING);
        Assertions.assertEquals(QualityStatus.PENDING,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.QUALITY_PENDING,batch1.getBatchStatus());
    }

    @Test
    void shouldThrowExceptionWhenEvaluationOutcomeIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            batch1.completeQualityEvaluation(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenQuarantinedBatchIsEvaluatedAgain(){
        batch1.completeQualityEvaluation(QualityStatus.FAILED);
        Assertions.assertThrows(IllegalStateException.class,() -> {
            batch1.completeQualityEvaluation(QualityStatus.PASSED);
        });
    }

    @Test
    void shouldThrowExceptionWhenApprovedBatchIsEvaluatedAgain(){
        batch1.completeQualityEvaluation(QualityStatus.PASSED);

        Assertions.assertThrows(IllegalStateException.class,() -> {
            batch1.completeQualityEvaluation(QualityStatus.FAILED);
        });
    }

    @Test
    void shouldAllowReevaluationWhenBatchIsQualityPending(){
        batch1.completeQualityEvaluation(QualityStatus.PENDING);
        batch1.completeQualityEvaluation(QualityStatus.PASSED);
        Assertions.assertEquals(QualityStatus.PASSED,batch1.getQualityStatus());
        Assertions.assertEquals(BatchStatus.APPROVED,batch1.getBatchStatus());
    }
}
