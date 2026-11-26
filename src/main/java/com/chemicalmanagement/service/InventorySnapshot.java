package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.Inventory;

import java.math.BigDecimal;

public class InventorySnapshot {
    private final Inventory inventory;
    private final BigDecimal originalAvailableQuantity;
    private final BigDecimal originalReservedQuantity;
    private final BigDecimal originalDispatchedQuantity;
    private final BigDecimal originalDamagedQuantity;

    public InventorySnapshot(Inventory inventory,
                             BigDecimal originalAvailableQuantity,
                             BigDecimal originalReservedQuantity,
                             BigDecimal originalDispatchedQuantity,
                             BigDecimal originalDamagedQuantity){
        this.inventory = inventory;
        this.originalAvailableQuantity = originalAvailableQuantity;
        this.originalReservedQuantity = originalReservedQuantity;
        this.originalDispatchedQuantity = originalDispatchedQuantity;
        this.originalDamagedQuantity = originalDamagedQuantity;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public BigDecimal getOriginalAvailableQuantity() {
        return originalAvailableQuantity;
    }

    public BigDecimal getOriginalReservedQuantity() {
        return originalReservedQuantity;
    }

    public BigDecimal getOriginalDispatchedQuantity() {
        return originalDispatchedQuantity;
    }

    public BigDecimal getOriginalDamagedQuantity() {
        return originalDamagedQuantity;
    }
}