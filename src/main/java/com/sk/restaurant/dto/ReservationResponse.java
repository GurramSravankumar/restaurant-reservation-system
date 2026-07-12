package com.sk.restaurant.dto;

import com.sk.restaurant.enums.ReservationStatus;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private LocalDate reservationDate;
    private String timeSlot;
    private Integer guests;
    private ReservationStatus status;
    private UserResponse user;
    private TableResponse table;
}
