package com.sk.restaurant.repository;

import com.sk.restaurant.entity.Reservation;
import com.sk.restaurant.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByReservationDate(LocalDate date);

    boolean existsByTableIdAndReservationDateAndTimeSlotAndStatus(
            Long tableId, LocalDate date, String timeSlot, ReservationStatus status);
}
