package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Reservation;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class InMemoryReservationRepository implements ReservationRepository{

    private final Map<String,Reservation> reservationMap = new HashMap<>();

    @Override
    public void save(Reservation reservation) {
        if(reservation == null){
            throw new IllegalArgumentException("Reservation cannot be null.");
        }
        if(reservationMap.containsKey(reservation.getReservationId())){
            throw new IllegalArgumentException("Reservation with ID " + reservation.getReservationId() + " already exists.");
        }
        reservationMap.put(reservation.getReservationId(),reservation);
    }

    @Override
    public Optional<Reservation> findById(String reservationId) {
        if(reservationId == null || reservationId.isBlank()){
            throw new IllegalArgumentException("Reservation Id cannot be null or blank.");
        }
        return Optional.ofNullable(reservationMap.get(reservationId));
    }

    @Override
    public List<Reservation> findByOrderId(String orderId) {
        if(orderId == null || orderId.isBlank()){
            throw new IllegalArgumentException("Order ID cannot be null or blank.");
        }
        List<Reservation> resList = new ArrayList<>();
        for(Reservation res : reservationMap.values()){
            if(res.getOrder().getOrderId().equals(orderId)){
                resList.add(res);
            }
        }
        return List.copyOf(resList);
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservationMap.values());
    }

    @Override
    public boolean existsById(String reservationId) {
        if (reservationId == null || reservationId.isBlank()) {
            throw new IllegalArgumentException("Reservation ID cannot be null or blank.");
        }
        return reservationMap.containsKey(reservationId);
    }
}
