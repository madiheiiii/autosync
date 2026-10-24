
package com.autosync.controller;

import com.autosync.model.Reservation;
import com.autosync.service.ReservationService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
//import java.util.Map;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public record CreateReservationRequest(
            Long customerId,
            Long vehicleId
    ) {}

    @PostMapping
    public Reservation createReservation(
            @RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(
                request.customerId(),
                request.vehicleId());
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/customer/{customerId}")
    public List<Reservation> getReservationsByCustomerId(
            @PathVariable Long customerId) {
        return reservationService.getReservationsByCustomerId(customerId);
    }

    @PatchMapping("/{id}/cancel")
    public Reservation cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }
    @PatchMapping("/{id}/active")
    public Reservation activeReservation(@PathVariable Long id) {
        return reservationService.activeReservation(id);
    }
}
