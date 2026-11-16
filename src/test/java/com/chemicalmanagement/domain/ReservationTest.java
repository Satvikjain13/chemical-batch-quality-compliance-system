package com.chemicalmanagement.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationTest {

    private Chemical chemical;
    private ProductionBatch batch1;
    private Customer customer;
    private CustomerOrder order1;
    private Inventory inventory1;

    @BeforeEach
    void setup() {
        chemical = new Chemical("CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365);

        batch1 = new ProductionBatch("BATCH-001",
                chemical,
                LocalDate.of(2026, 11, 1),
                new BigDecimal("1000"));

        batch1.completeQualityEvaluation(QualityStatus.PASSED);

        inventory1 = new Inventory("INV-001",
                batch1);

        customer = new Customer("CUSt-001",
                "ABC Chemicals",
                "9039779122",
                "sats@gmail.com",
                "Indore"
        );

        order1 = new CustomerOrder("ORD-001",
                customer,
                chemical,
                new BigDecimal("500")
        );
    }

    @Test
    void shouldCreateReservationWithValidDetails() {
        Reservation reservation1 = new Reservation("RES-001",
                order1,
                inventory1,
                new BigDecimal("300")
        );
        Assertions.assertEquals("RES-001", reservation1.getReservationId());
        Assertions.assertSame(order1, reservation1.getOrder());
        Assertions.assertSame(inventory1, reservation1.getInventory());
        Assertions.assertEquals(0, new BigDecimal("300").compareTo(reservation1.getReservedQuantity()));
        Assertions.assertNotNull(reservation1.getReservationDate());
        Assertions.assertEquals(ReservationStatus.ACTIVE, reservation1.getReservationStatus());
    }

    @Test
    void shouldThrowExceptionWhenReservationIdIsNull() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation(null, order1, inventory1, new BigDecimal("300"))
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationIdIsEmpty() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation("", order1, inventory1, new BigDecimal("300"))
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationIdIsBlank() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation("  ", order1, inventory1, new BigDecimal("300"))
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderIsNull() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation("RES-002", null, inventory1, new BigDecimal("300"))
        );
    }

    @Test
    void shouldThrowExceptionWhenInventoryIsNull() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation("RES-003", order1, null, new BigDecimal("300"))
        );
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNull() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation("RES-004", order1, inventory1, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation("RES-005", order1, inventory1, new BigDecimal("0"))
        );
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation("RES-006", order1, inventory1, new BigDecimal("-500"))
        );
    }

    @Test
    void shouldThrowExceptionWhenReservedQuantityExceedsOrderQuantity() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Reservation("RES-007", order1, inventory1, new BigDecimal("600"))
        );
    }

    @Test
    void shouldMarkReservationAsReleased(){
        Reservation reservation = new Reservation("RES-008",order1,inventory1,new BigDecimal("300"));
        reservation.markAsReleased();
        Assertions.assertEquals(ReservationStatus.RELEASED,reservation.getReservationStatus());

    }

    @Test
    void shouldThrowExceptionWhenReleasingAlreadyReleasedReservation(){
        Reservation reservation = new Reservation("RES-009",order1,inventory1,new BigDecimal("300"));
        reservation.markAsReleased();
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> reservation.markAsReleased()
        );
    }

    @Test
    void shouldMarkReservationAsDispatched(){
        Reservation reservation = new Reservation("RES-010",order1,inventory1,new BigDecimal("300"));
        reservation.markAsDispatched();
        Assertions.assertEquals(ReservationStatus.DISPATCHED,reservation.getReservationStatus());
    }

    @Test
    void shouldThrowExceptionWhenDispatchingAlreadyDispatchedReservation(){
        Reservation reservation = new Reservation("RES-011",order1,inventory1,new BigDecimal("300"));
        reservation.markAsDispatched();
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> reservation.markAsDispatched()
        );
    }

    @Test
    void shouldThrowExceptionWhenReleasingDispatchedReservation(){
        Reservation reservation = new Reservation("RES-012",order1,inventory1,new BigDecimal("300"));
        reservation.markAsDispatched();
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> reservation.markAsReleased()
        );
    }

    @Test
    void shouldThrowExceptionWhenDispatchingReleasedReservation(){
        Reservation reservation = new Reservation("RES-013",order1,inventory1,new BigDecimal("300"));
        reservation.markAsReleased();
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> reservation.markAsDispatched()
        );
    }

    @Test
    void shouldReturnCorrectReservationDate() {

        LocalDateTime beforeCreation = LocalDateTime.now();

        Reservation reservation = new Reservation(
                "RES-014",
                order1,
                inventory1,
                new BigDecimal("300")
        );

        LocalDateTime afterCreation = LocalDateTime.now();

        Assertions.assertFalse(
                reservation.getReservationDate().isBefore(beforeCreation)
        );

        Assertions.assertFalse(
                reservation.getReservationDate().isAfter(afterCreation)
        );
    }

}
