package com.sk.restaurant.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationRequest {
    private LocalDate reservationDate;
    private String timeSlot;
    private Integer guests;
}
