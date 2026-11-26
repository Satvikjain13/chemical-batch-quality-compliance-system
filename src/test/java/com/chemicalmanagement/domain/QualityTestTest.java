
package com.chemicalmanagement.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class QualityTestTest {

    private QualityTest sulphuricPurity;

    @BeforeEach
    void setup() {
        sulphuricPurity = new QualityTest(
                "TEST-001",
                "Purity",
                new BigDecimal("95"),
                new BigDecimal("100"),
                "%"
        );
    }

    // Test ID validation

    @Test
    void shouldThrowExceptionWhenTestIdIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest(null, "Purity",
                    new BigDecimal("95"), new BigDecimal("100"), "%");
        });
    }

    @Test
    void shouldThrowExceptionWhenTestIdIsEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("", "Purity",
                    new BigDecimal("95"), new BigDecimal("100"), "%");
        });
    }

    @Test
    void shouldThrowExceptionWhenTestIdIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("   ", "Purity",
                    new BigDecimal("95"), new BigDecimal("100"), "%");
        });
    }

    // Test name validation

    @Test
    void shouldThrowExceptionWhenTestNameIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("TEST-001", null,
                    new BigDecimal("95"), new BigDecimal("100"), "%");
        });
    }

    @Test
    void shouldThrowExceptionWhenTestNameIsEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("TEST-001", "",
                    new BigDecimal("95"), new BigDecimal("100"), "%");
        });
    }

    @Test
    void shouldThrowExceptionWhenTestNameIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("TEST-001", "   ",
                    new BigDecimal("95"), new BigDecimal("100"), "%");
        });
    }

    // Unit validation

    @Test
    void shouldThrowExceptionWhenUnitIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("TEST-001", "Purity",
                    new BigDecimal("95"), new BigDecimal("100"), null);
        });
    }

    @Test
    void shouldThrowExceptionWhenUnitIsEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("TEST-001", "Purity",
                    new BigDecimal("95"), new BigDecimal("100"), "");
        });
    }

    @Test
    void shouldThrowExceptionWhenUnitIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("TEST-001", "Purity",
                    new BigDecimal("95"), new BigDecimal("100"), "   ");
        });
    }

    // Minimum acceptable value validation

    @Test
    void shouldThrowExceptionWhenMinimumAcceptableValueIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("TEST-001", "Purity",
                    null, new BigDecimal("100"), "%");
        });
    }

    // Maximum acceptable value validation

    @Test
    void shouldThrowExceptionWhenMaximumAcceptableValueIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("TEST-001", "Purity",
                    new BigDecimal("95"), null, "%");
        });
    }

    @Test
    void shouldThrowExceptionWhenMinimumAcceptableValueExceedsMaximumAcceptableValue() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new QualityTest("TEST-001", "Purity",
                    new BigDecimal("100"), new BigDecimal("95"), "%");
        });
    }

    // Successful construction

    @Test
    void shouldCreateQualityTestSuccessfully() {
        Assertions.assertEquals("TEST-001", sulphuricPurity.getTestId());
        Assertions.assertEquals("Purity", sulphuricPurity.getTestName());
        Assertions.assertEquals(
                0,
                sulphuricPurity.getMinimumAcceptableValue()
                        .compareTo(new BigDecimal("95"))
        );
        Assertions.assertEquals(
                0,
                sulphuricPurity.getMaximumAcceptableValue()
                        .compareTo(new BigDecimal("100"))
        );
        Assertions.assertEquals("%", sulphuricPurity.getUnit());
    }

    // Acceptable measured values

    @Test
    void shouldAcceptMeasuredValueExactlyEqualToMinimum() {
        Assertions.assertTrue(
                sulphuricPurity.isWithinAcceptableLimit(new BigDecimal("95"))
        );
    }

    @Test
    void shouldAcceptMeasuredValueExactlyEqualToMaximum() {
        Assertions.assertTrue(
                sulphuricPurity.isWithinAcceptableLimit(new BigDecimal("100"))
        );
    }

    @Test
    void shouldAcceptMeasuredValueWithinAcceptableRange() {
        Assertions.assertTrue(
                sulphuricPurity.isWithinAcceptableLimit(new BigDecimal("97"))
        );
    }

    // Unacceptable measured values

    @Test
    void shouldRejectMeasuredValueBelowMinimum() {
        Assertions.assertFalse(
                sulphuricPurity.isWithinAcceptableLimit(new BigDecimal("94"))
        );
    }

    @Test
    void shouldRejectMeasuredValueAboveMaximum() {
        Assertions.assertFalse(
                sulphuricPurity.isWithinAcceptableLimit(new BigDecimal("102"))
        );
    }

    // Equal minimum and maximum

    @Test
    void shouldAllowMinimumAndMaximumToBeEqual() {
        QualityTest sulphurViscosity = new QualityTest(
                "QT-002",
                "Sulphur Viscosity",
                new BigDecimal("10"),
                new BigDecimal("10"),
                "%"
        );

        Assertions.assertTrue(
                sulphurViscosity.isWithinAcceptableLimit(new BigDecimal("10"))
        );
    }

    // Measured value validation

    @Test
    void shouldThrowExceptionWhenMeasuredValueIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sulphuricPurity.isWithinAcceptableLimit(null);
        });
    }
}
