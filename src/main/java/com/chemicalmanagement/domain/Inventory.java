package com.chemicalmanagement.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Inventory {
    private final String inventoryId;
    private final ProductionBatch batch;
    private final BigDecimal totalQuantity;  //Invariant
    private BigDecimal availableQuantity;
    private BigDecimal reservedQuantity;
    private BigDecimal dispatchedQuantity;
    private BigDecimal damagedQuantity;

    //constructors
    public Inventory(String inventoryId,
                     ProductionBatch batch
    ) {
        if (inventoryId == null || inventoryId.isBlank()) {
            throw new IllegalArgumentException("Inventory Id cannot be null or Blank.");
        }
        if (batch == null) {
            throw new IllegalArgumentException("ProductionBatch cannot be null");
        }
        if (batch.getBatchStatus() != BatchStatus.APPROVED) {
            throw new IllegalStateException("Inventory can only be created for approved batches.");
        }
        this.inventoryId = inventoryId;
        this.batch = batch;
        this.totalQuantity = batch.getQuantity();
        this.availableQuantity = batch.getQuantity();
        this.reservedQuantity = BigDecimal.ZERO;
        this.dispatchedQuantity = BigDecimal.ZERO;
        this.damagedQuantity = BigDecimal.ZERO;
    }

    //getters
    public String getInventoryId() {
        return inventoryId;
    }

    public ProductionBatch getBatch() {
        return batch;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getAvailableQuantity() {
        return availableQuantity;
    }

    public BigDecimal getReservedQuantity() {
        return reservedQuantity;
    }

    public BigDecimal getDispatchedQuantity() {
        return dispatchedQuantity;
    }

    public BigDecimal getDamagedQuantity() {
        return damagedQuantity;
    }

    public void reserveQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Reserve Quantity cannot be null");
        }
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Reserve Quantity should be greater than zero");
        }
        if (availableQuantity.compareTo(quantity) < 0) {
            throw new IllegalStateException("Insufficient available stock.");
        }
        availableQuantity = availableQuantity.subtract(quantity);
        reservedQuantity = reservedQuantity.add(quantity);
    }

    public void releaseReservedQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Release Reservation Quantity cannot be null. ");
        }
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Release Reservation Quantity should be greater than zero");
        }
        if (reservedQuantity.compareTo(quantity) < 0) {
            throw new IllegalStateException("Release Quantity cannot be greater than Reserve Quantity");
        }
        reservedQuantity = reservedQuantity.subtract(quantity);
        availableQuantity = availableQuantity.add(quantity);
    }

    public void dispatchQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Dispatch Quantity cannot be null.");
        }
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Dispatch Quantity should be greater than zero");
        }
        if (reservedQuantity.compareTo(quantity) < 0) {
            throw new IllegalStateException("Dispatch Quantity cannot be greater than Reserve Quantity");
        }

        dispatchedQuantity = dispatchedQuantity.add(quantity);
        reservedQuantity = reservedQuantity.subtract(quantity);
    }

    public void recordDamageFromAvailable(BigDecimal quantity){
        if(quantity == null){
            throw new IllegalArgumentException("Damaged Quantity cannot be null.");
        }
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Damaged Quantity should be greater than zero");
        }
        if (availableQuantity.compareTo(quantity) < 0) {
            throw new IllegalStateException("Damaged Quantity cannot be greater than Available Quantity");
        }

        damagedQuantity = damagedQuantity.add(quantity);
        availableQuantity = availableQuantity.subtract(quantity);
    }

    public void restoreReservationState(BigDecimal originalAvailableQuantity,
                                        BigDecimal originalReservedQuantity){
        if(originalAvailableQuantity == null){
            throw new IllegalArgumentException("Original Available Quantity cannot be null");
        }
        if(originalReservedQuantity == null){
            throw new IllegalArgumentException("Original Reserved quantity cannot br null.");
        }
        if(originalAvailableQuantity.compareTo(BigDecimal.ZERO) < 0  ||
                originalReservedQuantity.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("OriginalAvailableQuantity or OriginalReservedQuantity cannot be negative");
        }
        BigDecimal inVariantTotal = originalAvailableQuantity.add(originalReservedQuantity).add(dispatchedQuantity).add(damagedQuantity);
        if(totalQuantity.compareTo(inVariantTotal) != 0){
            throw new IllegalArgumentException("Invariant should be equal to Total Quantity");
        }
        availableQuantity = originalAvailableQuantity;
        reservedQuantity = originalReservedQuantity;
    }

    public void restoreState(BigDecimal originalAvailableQuantity,
                             BigDecimal originalReservedQuantity,
                             BigDecimal originalDispatchedQuantity,
                             BigDecimal originalDamagedQuantity){
        if(originalAvailableQuantity == null){
            throw new IllegalArgumentException("Original Available Quantity cannot be null.");
        }
        if(originalReservedQuantity == null){
            throw new IllegalArgumentException("Original Reserved Quantity cannot be null.");
        }
        if(originalDispatchedQuantity == null){
            throw new IllegalArgumentException("Original Dispatched Quantity cannot be null.");
        }
        if(originalDamagedQuantity == null){
            throw new IllegalArgumentException("Original Damaged Quantity cannot be null.");
        }

        if(originalAvailableQuantity.compareTo(BigDecimal.ZERO) < 0 ||
        originalReservedQuantity.compareTo(BigDecimal.ZERO) < 0 ||
        originalDispatchedQuantity.compareTo(BigDecimal.ZERO) < 0 ||
        originalDamagedQuantity.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("OriginalAvailableQuantity or OriginalReservedQuantity or OriginalDispatchedQuantity or OriginalDamagedQuantity cannot be negative");
        }

        BigDecimal invariantTotal = originalAvailableQuantity.add(originalReservedQuantity).add(originalDispatchedQuantity).add(originalDamagedQuantity);
        if(totalQuantity.compareTo(invariantTotal) != 0){
            throw new IllegalArgumentException("Restored inventory quantities must equal total quantity.");
        }

        availableQuantity = originalAvailableQuantity;
        reservedQuantity = originalReservedQuantity;
        dispatchedQuantity = originalDispatchedQuantity;
        damagedQuantity = originalDamagedQuantity;
    }
}

