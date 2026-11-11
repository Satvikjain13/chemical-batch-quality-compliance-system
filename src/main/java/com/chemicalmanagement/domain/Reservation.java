package com.chemicalmanagement.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Reservation {
    private final String reservationId;
    private final CustomerOrder order;
    private final Inventory inventory;
    private final BigDecimal reservedQuantity;
    private final LocalDateTime reservationDate;
    private ReservationStatus reservationStatus;

    //constructors
    public Reservation(String reservationId,
                       CustomerOrder order,
                       Inventory inventory,
                       BigDecimal reservedQuantity){
        if(reservationId == null || reservationId.isBlank()){
            throw new IllegalArgumentException("Reservation Id cannot be null or blank.");
        }
        if(order == null){
            throw new IllegalArgumentException("Customer Order cannot be null.");
        }
        if(inventory == null){
            throw new IllegalArgumentException("Inventory cannot be null.");
        }
        if(reservedQuantity == null || reservedQuantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Reserved Quantity cannot be null or should be greater than zero.");
        }
        if (reservedQuantity.compareTo(order.getOrderQuantity()) > 0) {
            throw new IllegalArgumentException("Reserved quantity cannot exceed order quantity.");
        }
        this.reservationId = reservationId;
        this.order = order;
        this.inventory = inventory;
        this.reservedQuantity = reservedQuantity;
        this.reservationDate = LocalDateTime.now();
        this.reservationStatus = ReservationStatus.ACTIVE;
    }

    //getters


    public String getReservationId() {
        return reservationId;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public BigDecimal getReservedQuantity() {
        return reservedQuantity;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void markAsReleased(){
        if(this.reservationStatus != ReservationStatus.ACTIVE){
            throw new IllegalStateException("Only ACTIVE reservations can be RELEASED.");
        }
        this.reservationStatus = ReservationStatus.RELEASED;
    }

    public void markAsDispatched() {
        if (this.reservationStatus != ReservationStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Only ACTIVE reservations can be DISPATCHED."
            );
        }

        this.reservationStatus = ReservationStatus.DISPATCHED;
    }
}
