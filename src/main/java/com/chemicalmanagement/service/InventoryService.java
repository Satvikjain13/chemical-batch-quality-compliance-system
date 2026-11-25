package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.*;
import com.chemicalmanagement.repository.InventoryRepository;
import com.chemicalmanagement.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationIdGenerator reservationIdGenerator;

    public InventoryService(InventoryRepository inventoryRepository,
                            ReservationRepository reservationRepository){
        if(inventoryRepository == null){
            throw new IllegalArgumentException("Inventory Repository cannot be null.");
        }

        if(reservationRepository == null){
            throw new IllegalArgumentException("Reservation Repository cannot be null.");
        }

        this.inventoryRepository = inventoryRepository;
        this.reservationRepository = reservationRepository;
        this.reservationIdGenerator = new ReservationIdGenerator();
    }

    public List<Inventory> findInventoriesByChemical(Chemical chemical){
        if(chemical == null){
            throw new IllegalArgumentException("Chemical cannot be null.");
        }
        List<Inventory> allInventories = inventoryRepository.findAll();
        List<Inventory> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(Inventory inventory : allInventories){
            if(!inventory.getBatch().getChemical().getChemicalId().equals(chemical.getChemicalId())){
                continue;
            }
            if(inventory.getBatch().getBatchStatus() != BatchStatus.APPROVED){
                continue;
            }
            if(inventory.getAvailableQuantity().compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            if(!inventory.getBatch().getExpiryDate().isAfter(today)){
                 continue;
            }
            result.add(inventory);
        }

        Collections.sort(result, new Comparator<Inventory>() {
            @Override
            public int compare(Inventory o1, Inventory o2) {
                return  o1.getBatch().getExpiryDate().compareTo(o2.getBatch().getExpiryDate());
            }
        });
        return result;
    }

    public boolean hasSufficientInventory(List<Inventory> inventories,
                                          BigDecimal requestedQuantity){
        if (inventories == null) {
            throw new IllegalArgumentException("Inventories cannot be null.");
        }
        for(Inventory inventory : inventories){
            if(inventory == null){
                throw new IllegalArgumentException("Inventory cannot be null.");
            }
        }
        if(requestedQuantity == null){
            throw new IllegalArgumentException("Requested Quantity cannot be null");
        }
        if(requestedQuantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Requested quantity must be greater than zero.");
        }
        BigDecimal totalAvailable = BigDecimal.ZERO;
        for(Inventory inventory : inventories){
            totalAvailable = totalAvailable.add(inventory.getAvailableQuantity());
        }
        return totalAvailable.compareTo(requestedQuantity) >= 0;
    }

    public List<Reservation> reserveInventoryForOrder(CustomerOrder order){
        if(order == null){
            throw new IllegalArgumentException("Customer Order cannot be null.");
        }
        if(order.getOrderStatus() != OrderStatus.VALIDATED){
            throw new IllegalStateException("Only validated orders are eligible for inventory reservation.");
        }
        List<Inventory> eligibleInventories = findInventoriesByChemical(order.getChemical());
        boolean sufficient = hasSufficientInventory(eligibleInventories,order.getOrderQuantity());
        BigDecimal remainingQuantity = order.getOrderQuantity();
        if(!sufficient){
            order.markAsRejected();
            return new ArrayList<>();
        }

        List<Reservation> createdReservations = new ArrayList<>();
        List<InventorySnapshot> inventorySnapshots = new ArrayList<>();
        try {
            for (Inventory inventory : eligibleInventories) {
                if (remainingQuantity.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
                InventorySnapshot snapShot = new InventorySnapshot(inventory,
                        inventory.getAvailableQuantity(),
                        inventory.getReservedQuantity());
                inventorySnapshots.add(snapShot);
                BigDecimal quantityToReserve = remainingQuantity.min(inventory.getAvailableQuantity());
                inventory.reserveQuantity(quantityToReserve);
                remainingQuantity = remainingQuantity.subtract(quantityToReserve);
                Reservation reservation = new Reservation(reservationIdGenerator.generateId(),
                        order,
                        inventory,
                        quantityToReserve);
                reservationRepository.save(reservation);
                createdReservations.add(reservation);
            }
            order.markAsReserved();
        }catch(RuntimeException originalException){
            for(InventorySnapshot inventorySnapshot: inventorySnapshots){
                try {
                    Inventory inventory = inventorySnapshot.getInventory();
                    BigDecimal originalAvailableQuantity = inventorySnapshot.getOriginalAvailableQuantity();
                    BigDecimal originalReservedQuantity = inventorySnapshot.getOriginalReservedQuantity();
                    inventory.restoreReservationState(originalAvailableQuantity, originalReservedQuantity);
                }catch(RuntimeException rollbackException){
                    originalException.addSuppressed(rollbackException);
                }
            }
            for(Reservation reservation : createdReservations){
                try {
                    reservationRepository.deleteById(reservation.getReservationId());
                }catch(RuntimeException rollbackException){
                    originalException.addSuppressed(rollbackException);
                }
            }
            throw originalException;
        }
        return createdReservations;
    }


    public void releaseReservationForOrder(CustomerOrder order){
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null");
        }
        List<Reservation> retrieveReservation = reservationRepository.findByOrderId(order.getOrderId());
        List<Reservation> activeReservations = new ArrayList<>();
        List<InventorySnapshot> inventorySnapshots = new ArrayList<>();
        try {
            for (Reservation reservation : retrieveReservation) {
                if (reservation.getReservationStatus() == ReservationStatus.ACTIVE) {
                InventorySnapshot inventorySnapshot = new InventorySnapshot(reservation.getInventory(),
                        reservation.getInventory().getAvailableQuantity(),
                        reservation.getInventory().getReservedQuantity());
                inventorySnapshots.add(inventorySnapshot);
                activeReservations.add(reservation);
                }
            }
            for(Reservation reservation : activeReservations){
                reservation.getInventory().releaseReservedQuantity(reservation.getReservedQuantity());
            }
            for(Reservation reservation : activeReservations){
                if(reservation.getReservationStatus() == ReservationStatus.ACTIVE){
                    reservation.markAsReleased();
                }

            }
        }
        catch(RuntimeException originalException){
                    for (InventorySnapshot inventorySnapshot : inventorySnapshots) {
                        try {
                            Inventory inventory = inventorySnapshot.getInventory();
                            BigDecimal originalAvailableQuantity = inventorySnapshot.getOriginalAvailableQuantity();
                            BigDecimal originalReservedQuantity = inventorySnapshot.getOriginalReservedQuantity();

                            inventory.restoreReservationState(originalAvailableQuantity, originalReservedQuantity);
                        }catch(RuntimeException rollbackException){
                            originalException.addSuppressed(rollbackException);
                    }
                    }
                    throw originalException;
            }
        }
    }

