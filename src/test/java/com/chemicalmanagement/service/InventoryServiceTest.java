package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.*;
import com.chemicalmanagement.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryServiceTest {
    private Chemical sulphuricAcid;
    private ProductionBatch batch1;
    private ProductionBatch batch2;
    private ProductionBatch batch3;
    private Inventory inventory1;
    private Inventory inventory2;
    private Inventory inventory3;
    private InventoryRepository inventoryRepository;
    private ReservationRepository reservationRepository;
    private Customer customer;
    private CustomerOrder order;
    private InventoryService service;


    @BeforeEach
    void setUp(){
        sulphuricAcid = new Chemical("CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365);
        batch1 = new ProductionBatch("BATCH-001",
                sulphuricAcid,
                LocalDate.of(2026,11,1),
                new BigDecimal("700"));
        batch2 = new ProductionBatch("BATCH-002",
                sulphuricAcid,
                LocalDate.of(2026,11,5),
                new BigDecimal("1000"));
        batch3 = new ProductionBatch("BATCH-003",
                sulphuricAcid,
                LocalDate.of(2026,10,13),
                new BigDecimal("300"));
        batch1.completeQualityEvaluation(QualityStatus.PASSED);
        batch2.completeQualityEvaluation(QualityStatus.PASSED);
        batch3.completeQualityEvaluation(QualityStatus.PASSED);

        inventory1 = new Inventory("INV-001",
                batch1);
        inventory2 = new Inventory("INV-002",
                batch2);
        inventory3 = new Inventory("INV-003",
                batch3);

        inventoryRepository = new InMemoryInventoryRepository();
        inventoryRepository.save(inventory1);
        inventoryRepository.save(inventory2);
        inventoryRepository.save(inventory3);

        customer = new Customer("CUST-001",
                "ABC Chemicals",
                "9039779122",
                "sats@gmail.com",
                "Indore");

        order = new CustomerOrder("ORD-001",
                customer,
                sulphuricAcid,
                new BigDecimal("1800"));

        reservationRepository = new InMemoryReservationRepository();
        service = new InventoryService(inventoryRepository,reservationRepository);
    }


    private ReservationRepository createTwoReservationRepository(
            boolean failOnRelease,
            boolean failOnDispatch) {

        order.markAsValidated();
        order.markAsReserved();

        inventory1.reserveQuantity(new BigDecimal("100"));
        inventory2.reserveQuantity(new BigDecimal("100"));

        TestReservationRepository repository =
                new TestReservationRepository();

        Reservation firstReservation = new Reservation(
                "RES-001",
                order,
                inventory1,
                new BigDecimal("100")
        );

        Reservation secondReservation = new FailingReservation(
                "RES-002",
                order,
                inventory2,
                new BigDecimal("100"),
                failOnRelease,
                failOnDispatch
        );

        repository.save(firstReservation);
        repository.save(secondReservation);

        return repository;
    }


    @Test
    void shouldThrowExceptionWhenInventoryRepositoryIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new InventoryService(null, reservationRepository);
        });
    }

    @Test
    void shouldThrowExceptionWhenReservationRepositoryIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new InventoryService(inventoryRepository, null);
        });
    }

    @Test
    void shouldThrowExceptionWhenInventoryListIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.hasSufficientInventory(null, new BigDecimal("500"));
        });
    }

    @Test
    void shouldThrowExceptionWhenInventoryListContainsNull(){
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.hasSufficientInventory(inventories, new BigDecimal("500"));
        });
    }

    @Test
    void shouldThrowExceptionWhenRequestedQuantityIsNull(){
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            service.hasSufficientInventory(inventories, null);
        });
    }

    @Test
    void shouldThrowExceptionWhenRequestedQuantityIsZero(){
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            service.hasSufficientInventory(inventories, BigDecimal.ZERO);
        });
    }

    @Test
    void shouldThrowExceptionWhenRequestedQuantityIsNegative(){
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            service.hasSufficientInventory(inventories, new BigDecimal("-100"));
        });
    }

    @Test
    void shouldReturnFalseWhenInventoryListIsEmpty(){
        List<Inventory> inventories = new ArrayList<>();

        Assertions.assertFalse(
                service.hasSufficientInventory(inventories, new BigDecimal("500"))
        );
    }

    @Test
    void shouldReturnFalseWhenAvailableInventoryIsInsufficient(){
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        Assertions.assertFalse(service.hasSufficientInventory(inventories,new BigDecimal("800")));
    }

    @Test
    void shouldReturnTrueWhenAvailableInventoryIsSufficient(){
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        Assertions.assertTrue(service.hasSufficientInventory(inventories,new BigDecimal("700")));
    }

    @Test
    void shouldReturnTrueWhenAvailableInventoryExceedsRequestedQuantity(){
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(inventory1);
        inventories.add(inventory2);
        Assertions.assertTrue(service.hasSufficientInventory(inventories,new BigDecimal("1500")));

    }

    @Test
    void shouldReserveInventoryAcrossMultipleBatchesUsingFEFO(){
        order.markAsValidated();
        List<Reservation> result = service.reserveInventoryForOrder(order);
        Assertions.assertEquals(3,result.size());
        Assertions.assertEquals(new BigDecimal("0"),inventory1.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("200"),inventory2.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory3.getAvailableQuantity());
        Assertions.assertEquals(OrderStatus.RESERVED,order.getOrderStatus());
        Assertions.assertEquals(new BigDecimal("300"),result.get(0).getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("700"),result.get(1).getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("800"),result.get(2).getReservedQuantity());
        Assertions.assertEquals(3,reservationRepository.findAll().size());
        Assertions.assertSame(inventory3,result.get(0).getInventory());
        Assertions.assertSame(inventory1,result.get(1).getInventory());
        Assertions.assertSame(inventory2,result.get(2).getInventory());
    }

    @Test
    void shouldRollbackInventoryAndReservationsWhenSaveFails(){
        order.markAsValidated();
        FailingReservationRepository failingRepository = new FailingReservationRepository();
        InventoryService failingService = new InventoryService(inventoryRepository,failingRepository);
        Assertions.assertThrows(IllegalStateException.class,() -> {
            failingService.reserveInventoryForOrder(order);
        });
        Assertions.assertEquals(new BigDecimal("700"),inventory1.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory1.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory2.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory2.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("300"),inventory3.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory3.getReservedQuantity());
        Assertions.assertEquals(0,failingRepository.findAll().size());
        Assertions.assertEquals(OrderStatus.VALIDATED,order.getOrderStatus());
    }

    @Test
    void shouldReleaseMultipleActiveReservationsSuccessfully(){
        order.markAsValidated();
        List<Reservation> reservedReservations = service.reserveInventoryForOrder(order);
        Assertions.assertEquals(ReservationStatus.ACTIVE, reservedReservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE, reservedReservations.get(1).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE, reservedReservations.get(2).getReservationStatus());
        Assertions.assertEquals(3,reservedReservations.size());
        service.releaseReservationForOrder(order);
        Assertions.assertEquals(new BigDecimal("700"),inventory1.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory1.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory2.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory2.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("300"),inventory3.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory3.getReservedQuantity());
        Assertions.assertEquals(ReservationStatus.RELEASED,reservedReservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.RELEASED,reservedReservations.get(1).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.RELEASED,reservedReservations.get(2).getReservationStatus());
    }

    @Test
    void shouldNotReleaseAlreadyReleasedReservationsAgain(){
        order.markAsValidated();
        List<Reservation> reservedReservations = service.reserveInventoryForOrder(order);
        Assertions.assertEquals(ReservationStatus.ACTIVE, reservedReservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE, reservedReservations.get(1).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE, reservedReservations.get(2).getReservationStatus());
        Assertions.assertEquals(3,reservedReservations.size());
        service.releaseReservationForOrder(order);
        service.releaseReservationForOrder(order);
        Assertions.assertEquals(new BigDecimal("700"),inventory1.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory1.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory2.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory2.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("300"),inventory3.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory3.getReservedQuantity());
        Assertions.assertEquals(ReservationStatus.RELEASED,reservedReservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.RELEASED,reservedReservations.get(1).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.RELEASED,reservedReservations.get(2).getReservationStatus());
    }

    @Test
    void shouldHandleOrderWithNoReservations(){
        Assertions.assertDoesNotThrow(() -> {
            service.releaseReservationForOrder(order);
        });
        Assertions.assertEquals(0, reservationRepository.findAll().size());
        Assertions.assertEquals(new BigDecimal("700"),inventory1.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory1.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("1000"),inventory2.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,inventory2.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("300"),inventory3.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO, inventory3.getReservedQuantity());

    }

    @Test
    void shouldRollbackInventoryWhenReleaseFails(){
        Inventory failingInventory = new FailingInventory("INV-001",batch1);
        InventoryRepository failingInventoryRepository = new InMemoryInventoryRepository();
        failingInventoryRepository.save(inventory3);
        failingInventoryRepository.save(failingInventory);
        failingInventoryRepository.save(inventory2);

        InventoryService failingService = new InventoryService(failingInventoryRepository,reservationRepository);
        order.markAsValidated();
        List<Reservation> reservedReservations = failingService.reserveInventoryForOrder(order);
        Assertions.assertEquals(3,reservedReservations.size());
        Assertions.assertSame(inventory3, reservedReservations.get(0).getInventory());
        Assertions.assertSame(failingInventory, reservedReservations.get(1).getInventory());
        Assertions.assertSame(inventory2, reservedReservations.get(2).getInventory());
        Assertions.assertEquals(ReservationStatus.ACTIVE,reservedReservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE,reservedReservations.get(1).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE,reservedReservations.get(2).getReservationStatus());

        Assertions.assertThrows(IllegalStateException.class,() -> {
            failingService.releaseReservationForOrder(order);
        });

        Assertions.assertEquals(new BigDecimal("200"),inventory2.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("800"),inventory2.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("0"),failingInventory.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("700"),failingInventory.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("0"),inventory3.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("300"), inventory3.getReservedQuantity());
        Assertions.assertEquals(ReservationStatus.ACTIVE,reservedReservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE,reservedReservations.get(1).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE,reservedReservations.get(2).getReservationStatus());
        Assertions.assertEquals(
                OrderStatus.RESERVED,
                order.getOrderStatus()
        );
    }


    @Test
    void shouldReleaseReservationsSuccessfully() {
        ReservationRepository testRepository =
                createTwoReservationRepository(false, false);

        InventoryService testService =
                new InventoryService(inventoryRepository, testRepository);

        List<Reservation> reservations = testRepository.findAll();

        testService.releaseReservationForOrder(order);

        Assertions.assertEquals(new BigDecimal("700"),
                inventory1.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,
                inventory1.getReservedQuantity());

        Assertions.assertEquals(new BigDecimal("1000"),
                inventory2.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,
                inventory2.getReservedQuantity());

        Assertions.assertEquals(ReservationStatus.RELEASED,
                reservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.RELEASED,
                reservations.get(1).getReservationStatus());
    }

    @Test
    void shouldRollbackAllChangesWhenReleaseFailsMidway() {
        ReservationRepository testRepository =
                createTwoReservationRepository(true, false);

        InventoryService testService =
                new InventoryService(inventoryRepository, testRepository);

        List<Reservation> reservations = testRepository.findAll();

        Assertions.assertThrows(IllegalStateException.class, () ->
                testService.releaseReservationForOrder(order));

        // Verify inventory rollback
        Assertions.assertEquals(new BigDecimal("600"),
                inventory1.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("100"),
                inventory1.getReservedQuantity());

        Assertions.assertEquals(new BigDecimal("900"),
                inventory2.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("100"),
                inventory2.getReservedQuantity());

        // Verify reservation status rollback
        Assertions.assertEquals(ReservationStatus.ACTIVE,
                reservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE,
                reservations.get(1).getReservationStatus());
    }

    @Test
    void shouldDispatchReservationsSuccessfully() {
        ReservationRepository testRepository =
                createTwoReservationRepository(false, false);

        InventoryService testService =
                new InventoryService(inventoryRepository, testRepository);

        List<Reservation> reservations = testRepository.findAll();

        testService.dispatchReservationForOrder(order);

        // Verify inventory quantities
        Assertions.assertEquals(new BigDecimal("600"),
                inventory1.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,
                inventory1.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("100"),
                inventory1.getDispatchedQuantity());

        Assertions.assertEquals(new BigDecimal("900"),
                inventory2.getAvailableQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,
                inventory2.getReservedQuantity());
        Assertions.assertEquals(new BigDecimal("100"),
                inventory2.getDispatchedQuantity());

        // Verify reservation statuses
        Assertions.assertEquals(ReservationStatus.DISPATCHED,
                reservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.DISPATCHED,
                reservations.get(1).getReservationStatus());
    }

    @Test
    void shouldRollbackAllChangesWhenDispatchFailsMidway() {
        ReservationRepository testRepository =
                createTwoReservationRepository(false, true);

        InventoryService testService =
                new InventoryService(inventoryRepository, testRepository);

        List<Reservation> reservations = testRepository.findAll();

        Assertions.assertThrows(IllegalStateException.class, () ->
                testService.dispatchReservationForOrder(order));

        // Verify inventory rollback
        Assertions.assertEquals(new BigDecimal("600"),
                inventory1.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("100"),
                inventory1.getReservedQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,
                inventory1.getDispatchedQuantity());

        Assertions.assertEquals(new BigDecimal("900"),
                inventory2.getAvailableQuantity());
        Assertions.assertEquals(new BigDecimal("100"),
                inventory2.getReservedQuantity());
        Assertions.assertEquals(BigDecimal.ZERO,
                inventory2.getDispatchedQuantity());

        // Verify reservation status rollback
        Assertions.assertEquals(ReservationStatus.ACTIVE,
                reservations.get(0).getReservationStatus());
        Assertions.assertEquals(ReservationStatus.ACTIVE,
                reservations.get(1).getReservationStatus());
    }


    private static class FailingReservationRepository implements ReservationRepository{
        private final InMemoryReservationRepository delegate = new InMemoryReservationRepository();
        private int  saveAttempts = 0;
        @Override
        public void save(Reservation reservation) {
            saveAttempts++;
            if(saveAttempts == 3){
                throw new IllegalStateException("Simulated failure on third reservation save.");
            }
            delegate.save(reservation);
        }

        @Override
        public Optional<Reservation> findById(String reservationId) {
           return delegate.findById(reservationId);
        }

        @Override
        public List<Reservation> findByOrderId(String orderId) {
            return delegate.findByOrderId(orderId);
        }

        @Override
        public void deleteById(String reservationId) {
            delegate.deleteById(reservationId);
        }

        @Override
        public List<Reservation> findAll() {
           return delegate.findAll();
        }

        @Override
        public boolean existsById(String reservationId) {
           return delegate.existsById(reservationId);
        }
    }

    private static class FailingInventory extends Inventory{

        public FailingInventory(String inventoryId, ProductionBatch batch) {
            super(inventoryId, batch);
        }

        @Override
        public void releaseReservedQuantity(BigDecimal quantity){
            throw new IllegalStateException("Simulated inventory release failure");
        }
    }


    private static class TestReservationRepository
            implements ReservationRepository {

        private final List<Reservation> reservations = new ArrayList<>();

        @Override
        public void save(Reservation reservation) {
            reservations.add(reservation);
        }

        @Override
        public Optional<Reservation> findById(String reservationId) {
            return reservations.stream()
                    .filter(r -> r.getReservationId().equals(reservationId))
                    .findFirst();
        }

        @Override
        public List<Reservation> findByOrderId(String orderId) {
            return new ArrayList<>(reservations);
        }

        @Override
        public void deleteById(String reservationId) {
            reservations.removeIf(
                    r -> r.getReservationId().equals(reservationId)
            );
        }

        @Override
        public List<Reservation> findAll() {
            return new ArrayList<>(reservations);
        }

        @Override
        public boolean existsById(String reservationId) {
            return reservations.stream()
                    .anyMatch(r -> r.getReservationId().equals(reservationId));
        }
    }

    private static class FailingReservation extends Reservation {

        private final boolean failOnRelease;
        private final boolean failOnDispatch;

        public FailingReservation(
                String reservationId,
                CustomerOrder order,
                Inventory inventory,
                BigDecimal quantity,
                boolean failOnRelease,
                boolean failOnDispatch) {

            super(reservationId, order, inventory, quantity);

            this.failOnRelease = failOnRelease;
            this.failOnDispatch = failOnDispatch;
        }

        @Override
        public void markAsReleased() {
            if (failOnRelease) {
                throw new IllegalStateException(
                        "Simulated failure during reservation release."
                );
            }

            super.markAsReleased();
        }

        @Override
        public void markAsDispatched() {
            if (failOnDispatch) {
                throw new IllegalStateException(
                        "Simulated failure during reservation dispatch."
                );
            }

            super.markAsDispatched();
        }
    }

}
