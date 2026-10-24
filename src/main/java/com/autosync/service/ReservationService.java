
package com.autosync.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.autosync.model.Customer;
import com.autosync.model.Reservation;
import com.autosync.model.ReservationStatus;
import com.autosync.model.Vehicle;
import com.autosync.repository.CustomerRepository;
import com.autosync.repository.ReservationRepository;
import com.autosync.repository.VehicleRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            CustomerRepository customerRepository,
            VehicleRepository vehicleRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Reservation createReservation(Long customerId, Long vehicleId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"));

        List<Reservation> activeReservations =
                reservationRepository.findByVehicleIdAndStatus(
                        vehicleId, ReservationStatus.ACTIVE);

        if (!activeReservations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Vehicle already has an active reservation");
        }

        Reservation reservation = new Reservation(customer, vehicle);

        try {
            return reservationRepository.saveAndFlush(reservation);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Vehicle already has an active reservation",
                    exception
            );
        }
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reservation not found"));
    }
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer not found");
        }

        return reservationRepository.findByCustomerId(customerId);
    }

    public Reservation cancelReservation(Long id) {
        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Reservation is already cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);

        return reservationRepository.save(reservation);
    }

        public Reservation activeReservation(Long id) {
        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() == ReservationStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Reservation is already active");
        }

        reservation.setStatus(ReservationStatus.ACTIVE);

        return reservationRepository.save(reservation);
    }
}
