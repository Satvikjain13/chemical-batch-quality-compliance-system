package com.chemicalmanagement.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

class QualityTestResultTest {

    private Chemical chemical;
    private ProductionBatch batch;
    private QualityTest qualityTest;

    @BeforeEach
    void setup() {
        chemical = new Chemical(
                "CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365
        );

        batch = new ProductionBatch(
                "BATCH-001",
                chemical,
                LocalDate.of(2026, 1, 1),
                new BigDecimal("500")
        );

        qualityTest = new QualityTest(
                "QT-001",
                "Purity",
                new BigDecimal("95"),
                new BigDecimal("100"),
                "%"
        );

    }
        @Test
        void shouldThrowExceptionWhenResultIdIsNull () {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                new QualityTestResult(
                        null, batch, qualityTest, new BigDecimal("98")
                );
            });
        }

        @Test
        void shouldThrowExceptionWhenResultIdIsEmpty () {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                new QualityTestResult(
                        "", batch, qualityTest, new BigDecimal("98")
                );
            });
        }

        @Test
        void shouldThrowExceptionWhenResultIdIsBlank () {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                new QualityTestResult(
                        "   ", batch, qualityTest, new BigDecimal("98")
                );
            });
        }

        @Test
        void shouldThrowExceptionWhenBatchIsNull () {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                new QualityTestResult(
                        "QTR-001", null, qualityTest, new BigDecimal("98")
                );
            });
        }

        @Test
        void shouldThrowExceptionWhenQualityTestIsNull () {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                new QualityTestResult(
                        "QTR-001", batch, null, new BigDecimal("98")
                );
            });
        }

        @Test
        void shouldThrowExceptionWhenMeasuredValueIsNull () {
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                new QualityTestResult(
                        "QTR-001", batch, qualityTest, null
                );
            });
        }

    @Test
    void shouldCreateQualityTestResultSuccessfullyAndReturnCorrectValues() {
        QualityTestResult result = new QualityTestResult("QTR-001",
                batch,
                qualityTest,
                new BigDecimal("97")
        );
        Assertions.assertEquals("QTR-001",result.getResultId());
        Assertions.assertSame(batch,result.getBatch());
        Assertions.assertSame(qualityTest,result.getQualityTest());
        Assertions.assertEquals(0,new BigDecimal("97").compareTo(result.getMeasuredValue()));
    }

    @Test
    void shouldMarkResultAsPassedWhenMeasuredValueIsWithinAcceptableRange() {
        QualityTestResult result = new QualityTestResult("QTR-002",
                batch,
                qualityTest,
                new BigDecimal("98"));

        Assertions.assertTrue(result.isPassed());
    }

    @Test
    void shouldMarkResultAsFailedWhenMeasuredValueIsBelowMinimum() {
        QualityTestResult result = new QualityTestResult("QTR-003",
                batch,
                qualityTest,
                new BigDecimal("91"));

        Assertions.assertFalse(result.isPassed());
     }

    @Test
    void shouldMarkResultAsFailedWhenMeasuredValueIsAboveMaximum() {
        QualityTestResult result = new QualityTestResult("QTR-004",
                batch,
                qualityTest,
                new BigDecimal("110"));

        Assertions.assertFalse(result.isPassed());
    }

    @Test
    void shouldMarkResultAsPassedWhenMeasuredValueEqualsMinimum() {
        QualityTestResult result = new QualityTestResult("QTR-002",
                batch,
                qualityTest,
                new BigDecimal("95"));

        Assertions.assertTrue(result.isPassed());
    }

    @Test
    void shouldMarkResultAsPassedWhenMeasuredValueEqualsMaximum() {
        QualityTestResult result = new QualityTestResult("QTR-002",
                batch,
                qualityTest,
                new BigDecimal("100"));

        Assertions.assertTrue(result.isPassed());
    }
}

