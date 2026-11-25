package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.Inventory;

import java.math.BigDecimal;

public class InventorySnapshot {
    private final Inventory inventory;
    private final BigDecimal originalAvailableQuantity;
    private final BigDecimal originalReservedQuantity;

    public InventorySnapshot(Inventory inventory,
                             BigDecimal originalAvailableQuantity,
                             BigDecimal originalReservedQuantity){
        this.inventory = inventory;
        this.originalAvailableQuantity = originalAvailableQuantity;
        this.originalReservedQuantity = originalReservedQuantity;
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
}