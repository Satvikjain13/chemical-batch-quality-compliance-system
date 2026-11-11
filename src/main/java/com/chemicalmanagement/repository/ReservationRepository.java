package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    void save(Reservation reservation);
    Optional<Reservation> findById(String reservationId);
    List<Reservation> findByOrderId(String orderId);
    List<Reservation> findAll();
    boolean existsById(String reservationId);
}
