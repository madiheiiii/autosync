
package com.autosync.service.optimistic;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.autosync.model.optimistic.OptimisticCustomer;
import com.autosync.model.optimistic.OptimisticReservation;
import com.autosync.model.optimistic.OptimisticReservationStatus;
import com.autosync.model.optimistic.OptimisticVehicle;
import com.autosync.repository.optimistic.OptimisticCustomerRepository;
import com.autosync.repository.optimistic.OptimisticReservationRepository;
import com.autosync.repository.optimistic.OptimisticVehicleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptimisticReservationService {

    private final OptimisticReservationRepository reservationRepository;
    private final OptimisticCustomerRepository customerRepository;
    private final OptimisticVehicleRepository vehicleRepository;

    public OptimisticReservationService(
            OptimisticReservationRepository reservationRepository,
            OptimisticCustomerRepository customerRepository,
            OptimisticVehicleRepository vehicleRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public OptimisticReservation createReservation(Long customerId, Long vehicleId) {
        OptimisticCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        OptimisticVehicle vehicle = vehicleRepository.findForReservation(vehicleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"));

        List<OptimisticReservation> activeReservations =
                reservationRepository.findByVehicleIdAndStatus(
                        vehicleId, OptimisticReservationStatus.ACTIVE);

        if (!activeReservations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Vehicle already has an active reservation");
        }

        OptimisticReservation reservation = new OptimisticReservation(customer, vehicle);

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

    public OptimisticReservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reservation not found"));
    }
    public List<OptimisticReservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<OptimisticReservation> getReservationsByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer not found");
        }

        return reservationRepository.findByCustomerId(customerId);
    }

    public OptimisticReservation cancelReservation(Long id) {
        OptimisticReservation reservation = getReservationById(id);

        if (reservation.getStatus() == OptimisticReservationStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Reservation is already cancelled");
        }
        reservation.setStatus(OptimisticReservationStatus.CANCELLED);

        return reservationRepository.save(reservation);
    }

        public OptimisticReservation activeReservation(Long id) {
        OptimisticReservation reservation = getReservationById(id);

        if (reservation.getStatus() == OptimisticReservationStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Reservation is already active");
        }

        reservation.setStatus(OptimisticReservationStatus.ACTIVE);

        return reservationRepository.save(reservation);
    }
}
