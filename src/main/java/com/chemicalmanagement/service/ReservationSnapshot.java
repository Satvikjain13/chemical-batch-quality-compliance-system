package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.Reservation;
import com.chemicalmanagement.domain.ReservationStatus;

public class ReservationSnapshot {
    private final Reservation reservation;
    private final ReservationStatus reservationStatus;

    public ReservationSnapshot(Reservation reservation,
                               ReservationStatus reservationStatus){
        if(reservation == null){
            throw new IllegalArgumentException("Reservation cannot be null.");
        }
        if(reservationStatus == null){
            throw new IllegalArgumentException("Reservation Status cannot be null.");
        }
        this.reservation = reservation;
        this.reservationStatus = reservationStatus;
    }

    public Reservation getReservation(){
        return reservation;
    }

    public ReservationStatus getReservationStatus(){
        return reservationStatus;
    }
}
