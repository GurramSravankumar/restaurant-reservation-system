package com.sk.restaurant.service;

import com.sk.restaurant.dto.ReservationRequest;
import com.sk.restaurant.dto.ReservationResponse;
import com.sk.restaurant.dto.TableResponse;
import com.sk.restaurant.dto.UserResponse;
import com.sk.restaurant.entity.Reservation;
import com.sk.restaurant.entity.RestaurantTable;
import com.sk.restaurant.entity.User;
import com.sk.restaurant.enums.ReservationStatus;
import com.sk.restaurant.repository.ReservationRepository;
import com.sk.restaurant.repository.TableRepository;
import com.sk.restaurant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private TableResponse mapToTableResponse(RestaurantTable table) {
        return TableResponse.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .build();
    }

    private ReservationResponse mapToReservationResponse(Reservation r) {
        return ReservationResponse.builder()
                .id(r.getId())
                .reservationDate(r.getReservationDate())
                .timeSlot(r.getTimeSlot())
                .guests(r.getGuests())
                .status(r.getStatus())
                .user(mapToUserResponse(r.getUser()))
                .table(mapToTableResponse(r.getTable()))
                .build();
    }

    @Override
    public ReservationResponse createReservation(ReservationRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        if (request.getGuests() <= 0) {
            throw new RuntimeException("Invalid number of guests. Must be at least 1.");
        }

        if (request.getReservationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Reservation date cannot be in the past.");
        }

        List<RestaurantTable> allTables = tableRepository.findAll();
        List<RestaurantTable> suitableTables = new java.util.ArrayList<>();
        for (RestaurantTable t : allTables) {
            if (t.getCapacity() >= request.getGuests()) {
                suitableTables.add(t);
            }
        }
        suitableTables.sort(new Comparator<RestaurantTable>() {
            @Override
            public int compare(RestaurantTable o1, RestaurantTable o2) {
                return o1.getCapacity().compareTo(o2.getCapacity());
            }
        });

        if (suitableTables.isEmpty()) {
            throw new RuntimeException("No table available that can accommodate " + request.getGuests() + " guests.");
        }

        RestaurantTable assignedTable = null;
        for (RestaurantTable table : suitableTables) {
            boolean isBooked = reservationRepository.existsByTableIdAndReservationDateAndTimeSlotAndStatus(
                    table.getId(), request.getReservationDate(), request.getTimeSlot(), ReservationStatus.BOOKED);
            if (!isBooked) {
                assignedTable = table;
                break;
            }
        }

        if (assignedTable == null) {
            throw new RuntimeException("No table available for the selected slot (" + request.getTimeSlot() + ") on "
                    + request.getReservationDate());
        }

        Reservation reservation = Reservation.builder()
                .reservationDate(request.getReservationDate())
                .timeSlot(request.getTimeSlot())
                .guests(request.getGuests())
                .status(ReservationStatus.BOOKED)
                .user(user)
                .table(assignedTable)
                .build();

        return mapToReservationResponse(reservationRepository.save(reservation));
    }

    @Override
    public List<ReservationResponse> getMyReservations(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
        List<Reservation> reservations = reservationRepository.findByUserId(user.getId());
        List<ReservationResponse> responses = new java.util.ArrayList<>();
        for (Reservation r : reservations) {
            responses.add(mapToReservationResponse(r));
        }
        return responses;
    }

    @Override
    public void cancelReservation(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

        if (!reservation.getUser().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Access denied. You can only cancel your own reservations.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationResponse> responses = new java.util.ArrayList<>();
        for (Reservation r : reservations) {
            responses.add(mapToReservationResponse(r));
        }
        return responses;
    }

    @Override
    public List<ReservationResponse> getReservationsByDate(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByReservationDate(date);
        List<ReservationResponse> responses = new java.util.ArrayList<>();
        for (Reservation r : reservations) {
            responses.add(mapToReservationResponse(r));
        }
        return responses;
    }

    @Override
    public ReservationResponse updateReservation(Long id, ReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

        if (request.getGuests() <= 0) {
            throw new RuntimeException("Invalid number of guests.");
        }

        boolean slotChanged = !reservation.getTimeSlot().equals(request.getTimeSlot()) ||
                !reservation.getReservationDate().equals(request.getReservationDate());
        boolean guestsChanged = request.getGuests() > reservation.getTable().getCapacity();

        if (slotChanged || guestsChanged) {
            List<RestaurantTable> allTables = tableRepository.findAll();
            List<RestaurantTable> suitableTables = new java.util.ArrayList<>();
            for (RestaurantTable t : allTables) {
                if (t.getCapacity() >= request.getGuests()) {
                    suitableTables.add(t);
                }
            }
            suitableTables.sort(new Comparator<RestaurantTable>() {
                @Override
                public int compare(RestaurantTable o1, RestaurantTable o2) {
                    return o1.getCapacity().compareTo(o2.getCapacity());
                }
            });

            RestaurantTable newTable = null;
            for (RestaurantTable table : suitableTables) {
                if (table.getId().equals(reservation.getTable().getId()) && !slotChanged) {
                    newTable = table;
                    break;
                }
                boolean isBooked = reservationRepository.existsByTableIdAndReservationDateAndTimeSlotAndStatus(
                        table.getId(), request.getReservationDate(), request.getTimeSlot(), ReservationStatus.BOOKED);
                if (!isBooked) {
                    newTable = table;
                    break;
                }
            }

            if (newTable == null) {
                throw new RuntimeException("Cannot update. No table available of capacity " + request.getGuests()
                        + " at the requested slot.");
            }
            reservation.setTable(newTable);
        }

        reservation.setReservationDate(request.getReservationDate());
        reservation.setTimeSlot(request.getTimeSlot());
        reservation.setGuests(request.getGuests());
        reservation.setStatus(ReservationStatus.BOOKED);
        return mapToReservationResponse(reservationRepository.save(reservation));
    }

    @Override
    public void adminCancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
}
