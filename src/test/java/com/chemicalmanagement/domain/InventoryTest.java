
package com.chemicalmanagement.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Chemical chemical;
    private ProductionBatch approvedBatch;
    private ProductionBatch unapprovedBatch;
    private Inventory inventory;
    @BeforeEach
    void setUp() {
        chemical = new Chemical(
                "CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365
        );

        approvedBatch = new ProductionBatch(
                "BATCH-001",
                chemical,
                LocalDate.of(2026, 11, 12),
                new BigDecimal("1000")
        );

        approvedBatch.completeQualityEvaluation(QualityStatus.PASSED);

        unapprovedBatch = new ProductionBatch(
                "BATCH-002",
                chemical,
                LocalDate.of(2026, 11, 12),
                new BigDecimal("500")
        );

        inventory = new Inventory("INV-001",
                approvedBatch);
    }

    @Test
    void shouldThrowExceptionWhenInventoryIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Inventory(null, approvedBatch)
        );
    }

    @Test
    void shouldThrowExceptionWhenInventoryIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Inventory("   ", approvedBatch)
        );
    }

    @Test
    void shouldThrowExceptionWhenBatchIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Inventory("INV-001", null)
        );
    }

    @Test
    void shouldThrowExceptionWhenBatchIsNotApproved() {
        assertThrows(IllegalStateException.class, () ->
                new Inventory("INV-001", unapprovedBatch)
        );
    }

    @Test
    void shouldInitializeInventoryWithCorrectQuantities(){
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory.getDispatchedQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory.getDamagedQuantity());
    }

    @Test
    void shouldReserveQuantitySuccessfully(){
        inventory.reserveQuantity(new BigDecimal("300"));
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("700"),inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("300"),inventory.getReservedQuantity());
    }

    @Test
    void shouldAllowReservationOfEntireAvailableQuantity(){
        inventory.reserveQuantity(new BigDecimal("1000"));
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getReservedQuantity());
    }

    @Test
    void shouldThrowExceptionWhenReservationExceedsAvailableQuantity(){
        Assertions.assertThrows(IllegalStateException.class,() -> {
            inventory.reserveQuantity(new BigDecimal("1100"));
        });
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory.getReservedQuantity());
    }

    @Test
    void shouldThrowExceptionWhenReservationQuantityIsZero(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            inventory.reserveQuantity(new BigDecimal("0"));
        });
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory.getReservedQuantity());
    }

    @Test
    void shouldThrowExceptionWhenReservationQuantityIsNegative(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            inventory.reserveQuantity(new BigDecimal("-500"));
        });
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory.getReservedQuantity());
    }

    @Test
    void shouldThrowExceptionWhenReservationQuantityIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            inventory.reserveQuantity(null);
        });
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory.getReservedQuantity());
    }


    // Successful release
    @Test
    void shouldReleaseReservedQuantitySuccessfully() {
        inventory.reserveQuantity(new BigDecimal("300"));

        inventory.releaseReservedQuantity(new BigDecimal("100"));

        Assertions.assertEquals(new BigDecimal("1000"), inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("800"), inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("200"), inventory.getReservedQuantity());
    }

    // Release all reserved quantity
    @Test
    void shouldReleaseEntireReservedQuantity() {
        inventory.reserveQuantity(new BigDecimal("300"));

        inventory.releaseReservedQuantity(new BigDecimal("300"));

        Assertions.assertEquals(new BigDecimal("1000"), inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("0"), inventory.getReservedQuantity());
    }

    // Release more than reserved
    @Test
    void shouldThrowExceptionWhenReleaseExceedsReservedQuantity() {
        inventory.reserveQuantity(new BigDecimal("200"));

        Assertions.assertThrows(IllegalStateException.class, () ->
                inventory.releaseReservedQuantity(new BigDecimal("300"))
        );

        Assertions.assertEquals(new BigDecimal("800"), inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("200"), inventory.getReservedQuantity());
    }

    @Test
    void shouldThrowExceptionWhenReleaseQuantityIsNull() {
        inventory.reserveQuantity(new BigDecimal("200"));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                inventory.releaseReservedQuantity(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenReleaseQuantityIsZero() {
        inventory.reserveQuantity(new BigDecimal("200"));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                inventory.releaseReservedQuantity(new BigDecimal("0"))
        );
    }

    @Test
    void shouldThrowExceptionWhenReleaseQuantityIsNegative() {
        inventory.reserveQuantity(new BigDecimal("200"));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                inventory.releaseReservedQuantity(new BigDecimal("-100"))
        );
    }


    // Successful dispatch
    @Test
    void shouldDispatchReservedQuantitySuccessfully() {
        inventory.reserveQuantity(new BigDecimal("300"));

        inventory.dispatchQuantity(new BigDecimal("200"));

        Assertions.assertEquals(new BigDecimal("1000"), inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("700"), inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("100"), inventory.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("200"), inventory.getDispatchedQuantity());
    }

    // Dispatch all reserved stock
    @Test
    void shouldDispatchEntireReservedQuantity() {
        inventory.reserveQuantity(new BigDecimal("300"));

        inventory.dispatchQuantity(new BigDecimal("300"));

        Assertions.assertEquals(new BigDecimal("0"), inventory.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("300"), inventory.getDispatchedQuantity());
    }

    // Dispatch more than reserved
    @Test
    void shouldThrowExceptionWhenDispatchExceedsReservedQuantity() {
        inventory.reserveQuantity(new BigDecimal("200"));

        Assertions.assertThrows(IllegalStateException.class, () ->
                inventory.dispatchQuantity(new BigDecimal("300"))
        );

        Assertions.assertEquals(new BigDecimal("200"), inventory.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("0"), inventory.getDispatchedQuantity());
    }

    @Test
    void shouldThrowExceptionWhenDispatchQuantityIsNull() {
        inventory.reserveQuantity(new BigDecimal("200"));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                inventory.dispatchQuantity(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenDispatchQuantityIsZero() {
        inventory.reserveQuantity(new BigDecimal("200"));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                inventory.dispatchQuantity(new BigDecimal("0"))
        );
    }

    @Test
    void shouldThrowExceptionWhenDispatchQuantityIsNegative() {
        inventory.reserveQuantity(new BigDecimal("200"));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                inventory.dispatchQuantity(new BigDecimal("-100"))
        );
    }


    // Successful damage recording
    @Test
    void shouldRecordDamageFromAvailableQuantitySuccessfully() {
        inventory.recordDamageFromAvailable(new BigDecimal("100"));

        Assertions.assertEquals(new BigDecimal("1000"), inventory.getTotalQuantity());
        Assertions.assertEquals(new BigDecimal("900"), inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("100"), inventory.getDamagedQuantity());
    }

    // Damage entire available stock
    @Test
    void shouldRecordDamageForEntireAvailableQuantity() {
        inventory.recordDamageFromAvailable(new BigDecimal("1000"));

        Assertions.assertEquals(new BigDecimal("0"), inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("1000"), inventory.getDamagedQuantity());
    }

    // Damage more than available
    @Test
    void shouldThrowExceptionWhenDamageExceedsAvailableQuantity() {
        Assertions.assertThrows(IllegalStateException.class, () ->
                inventory.recordDamageFromAvailable(new BigDecimal("1100"))
        );

        Assertions.assertEquals(new BigDecimal("1000"), inventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("0"), inventory.getDamagedQuantity());
    }

    @Test
    void shouldThrowExceptionWhenDamageQuantityIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                inventory.recordDamageFromAvailable(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenDamageQuantityIsZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                inventory.recordDamageFromAvailable(new BigDecimal("0"))
        );
    }

    @Test
    void shouldThrowExceptionWhenDamageQuantityIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                inventory.recordDamageFromAvailable(new BigDecimal("-100"))
        );
    }

    @Test
    void shouldMaintainTotalQuantityAcrossInventoryOperations() {

        // Initial: Available 1000
        inventory.reserveQuantity(new BigDecimal("300"));

        // Available 700, Reserved 300
        inventory.releaseReservedQuantity(new BigDecimal("50"));

        // Available 750, Reserved 250
        inventory.dispatchQuantity(new BigDecimal("100"));

        // Available 750, Reserved 150, Dispatched 100
        inventory.recordDamageFromAvailable(new BigDecimal("25"));

        // Final:
        // Available 725
        // Reserved 150
        // Dispatched 100
        // Damaged 25

        Assertions.assertEquals(new BigDecimal("1000"), inventory.getTotalQuantity());

        BigDecimal accountedQuantity =
                inventory.getAvailableQuantity()
                        .add(inventory.getReservedQuantity())
                        .add(inventory.getDispatchedQuantity())
                        .add(inventory.getDamagedQuantity());

        Assertions.assertEquals(
                0,
                inventory.getTotalQuantity().compareTo(accountedQuantity)
        );
    }

}
