package com.autosync.controller.pessimistic;

import java.util.List;
import java.util.Map;

import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autosync.model.pessimistic.PessimisticReservation;
import com.autosync.service.pessimistic.PessimisticReservationService;
//import java.util.Map;

@RestController
@RequestMapping("/pessimistic/reservations")
public class PessimisticReservationController {

    private final PessimisticReservationService reservationService;

    public PessimisticReservationController(PessimisticReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public record CreateReservationRequest(
            Long customerId,
            Long vehicleId) {
    }

    @PostMapping
    public PessimisticReservation createReservation(
            @RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(
                request.customerId(),
                request.vehicleId());
    }

    @GetMapping("/{id}")
    public PessimisticReservation getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @GetMapping
    public List<PessimisticReservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/customer/{customerId}")
    public List<PessimisticReservation> getReservationsByCustomerId(
            @PathVariable Long customerId) {
        return reservationService.getReservationsByCustomerId(customerId);
    }

    @PatchMapping("/{id}/cancel")
    public PessimisticReservation cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }

    @PatchMapping("/{id}/active")
    public PessimisticReservation activeReservation(@PathVariable Long id) {
        return reservationService.activeReservation(id);
    }

    @ExceptionHandler(PessimisticLockingFailureException.class)
    public ResponseEntity<Map<String, String>> handlePessimisticConflict(
            PessimisticLockingFailureException exception) {

        System.out.println("PESSIMISTIC CONFLICT: " + exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "message",
                        "Could not acquire the vehicle lock. Please try again."));
    }
}
