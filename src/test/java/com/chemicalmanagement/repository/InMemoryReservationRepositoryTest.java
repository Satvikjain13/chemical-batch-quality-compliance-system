
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class InMemoryReservationRepositoryTest {

    private InMemoryReservationRepository repository;
    private Chemical chemical;
    private ProductionBatch batch;
    private Customer customer;
    private CustomerOrder order;
    private Inventory inventory;

    @BeforeEach
    void setup() {
        repository = new InMemoryReservationRepository();

        chemical = new Chemical(
                "CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365
        );

        batch = new ProductionBatch(
                "BATCH-001",
                chemical,
                LocalDate.of(2026, 11, 1),
                new BigDecimal("1000")
        );

        batch.completeQualityEvaluation(QualityStatus.PASSED);

        inventory = new Inventory("INV-001", batch);

        customer = new Customer(
                "CUST-001",
                "ABC Chemicals",
                "9039779122",
                "sats@gmail.com",
                "Indore"
        );

        order = new CustomerOrder(
                "ORD-001",
                customer,
                chemical,
                new BigDecimal("500")
        );
    }

    private Reservation createReservation(String id) {
        return new Reservation(
                id,
                order,
                inventory,
                new BigDecimal("300")
        );
    }

    // SAVE TESTS

    @Test
    void shouldSaveReservationSuccessfully() {
        Reservation reservation = createReservation("RES-001");

        repository.save(reservation);

        Optional<Reservation> result =
                repository.findById("RES-001");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertSame(reservation, result.get());
    }

    @Test
    void shouldThrowExceptionWhenSavingNullReservation() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateReservation() {
        Reservation reservation = createReservation("RES-001");

        repository.save(reservation);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(createReservation("RES-001"))
        );
    }

    // FIND BY ID TESTS

    @Test
    void shouldFindReservationById() {
        Reservation reservation = createReservation("RES-001");

        repository.save(reservation);

        Optional<Reservation> result =
                repository.findById("RES-001");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertSame("RES-001", result.get().getReservationId());
    }

    @Test
    void shouldReturnEmptyWhenReservationIdDoesNotExist() {
        Optional<Reservation> result =
                repository.findById("RES-999");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenFindingReservationWithNullId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingReservationWithEmptyId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById("")
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingReservationWithBlankId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById("  ")
        );
    }

    // FIND ALL TESTS

    @Test
    void shouldReturnAllReservations() {
        Reservation reservation1 = createReservation("RES-001");
        Reservation reservation2 = createReservation("RES-002");

        repository.save(reservation1);
        repository.save(reservation2);

        List<Reservation> reservations = repository.findAll();

        Assertions.assertEquals(2, reservations.size());
        Assertions.assertTrue(reservations.contains(reservation1));
        Assertions.assertTrue(reservations.contains(reservation2));
    }

    @Test
    void shouldReturnEmptyListWhenNoReservationsExist() {
        List<Reservation> reservations = repository.findAll();

        Assertions.assertTrue(reservations.isEmpty());
    }

    @Test
    void shouldReturnUnmodifiableReservationList() {
        repository.save(createReservation("RES-001"));

        List<Reservation> reservations = repository.findAll();

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> reservations.add(createReservation("RES-002"))
        );
    }

    // EXISTS BY ID TESTS

    @Test
    void shouldReturnTrueWhenReservationExists() {
        repository.save(createReservation("RES-001"));

        Assertions.assertTrue(repository.existsById("RES-001"));
    }

    @Test
    void shouldReturnFalseWhenReservationDoesNotExist() {
        Assertions.assertFalse(repository.existsById("RES-999"));
    }

    @Test
    void shouldThrowExceptionWhenCheckingReservationWithNullId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCheckingReservationWithEmptyId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById("")
        );
    }

    @Test
    void shouldThrowExceptionWhenCheckingReservationWithBlankId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById("  ")
        );
    }

    // REPOSITORY INTEGRITY TEST

    @Test
    void shouldNotModifyRepositoryWhenReturnedListIsModified() {
        repository.save(createReservation("RES-001"));

        List<Reservation> reservations = repository.findAll();

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> reservations.clear()
        );

        Assertions.assertEquals(1, repository.findAll().size());
    }
}
