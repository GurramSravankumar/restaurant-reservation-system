package com.sk.restaurant.service;

import com.sk.restaurant.dto.ReservationRequest;
import com.sk.restaurant.dto.ReservationResponse;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest request, String userEmail);

    List<ReservationResponse> getMyReservations(String userEmail);

    void cancelReservation(Long id, String userEmail);

    // Admin specific operations
    List<ReservationResponse> getAllReservations();

    List<ReservationResponse> getReservationsByDate(LocalDate date);

    ReservationResponse updateReservation(Long id, ReservationRequest request);

    void adminCancelReservation(Long id);
}
