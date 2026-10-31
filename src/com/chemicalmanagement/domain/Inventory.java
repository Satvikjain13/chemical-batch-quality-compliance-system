package com.chemicalmanagement.domain;

import java.math.BigDecimal;

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
}

