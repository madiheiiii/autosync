
package com.autosync.controller.optimistic;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.autosync.model.optimistic.OptimisticReservation;
import com.autosync.service.optimistic.OptimisticReservationService;
//import java.util.Map;

@RestController
@RequestMapping("optimistic/reservations")
public class OptimisticReservationController {

    private final OptimisticReservationService reservationService;

    public OptimisticReservationController(OptimisticReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public record CreateReservationRequest(
            Long customerId,
            Long vehicleId
    ) {}

    @PostMapping
    public OptimisticReservation createReservation(
            @RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(
                request.customerId(),
                request.vehicleId());
    }

    @GetMapping("/{id}")
    public OptimisticReservation getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }
    @GetMapping
    public List<OptimisticReservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/customer/{customerId}")
    public List<OptimisticReservation> getReservationsByCustomerId(
            @PathVariable Long customerId) {
        return reservationService.getReservationsByCustomerId(customerId);
    }

    @PatchMapping("/{id}/cancel")
    public OptimisticReservation cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }
    @PatchMapping("/{id}/active")
    public OptimisticReservation activeReservation(@PathVariable Long id) {
        return reservationService.activeReservation(id);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, String>> handleOptimisticConflict(OptimisticLockingFailureException exception) {
        System.out.println("OPTIMISTIC CONFLICT: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Vehicle status changed during this reservation attempt. Please try again."));
    }
}
