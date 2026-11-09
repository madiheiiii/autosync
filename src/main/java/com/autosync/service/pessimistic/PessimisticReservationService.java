
package com.autosync.service.pessimistic;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.autosync.model.pessimistic.PessimisticCustomer;
import com.autosync.model.pessimistic.PessimisticReservation;
import com.autosync.model.pessimistic.PessimisticReservationStatus;
import com.autosync.model.pessimistic.PessimisticVehicle;
import com.autosync.repository.pessimistic.PessimisticCustomerRepository;
import com.autosync.repository.pessimistic.PessimisticReservationRepository;
import com.autosync.repository.pessimistic.PessimisticVehicleRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class PessimisticReservationService {

    private final PessimisticReservationRepository reservationRepository;
    private final PessimisticCustomerRepository customerRepository;
    private final PessimisticVehicleRepository vehicleRepository;

    public PessimisticReservationService(
            PessimisticReservationRepository reservationRepository,
            PessimisticCustomerRepository customerRepository,
            PessimisticVehicleRepository vehicleRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
    }
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public PessimisticReservation createReservation(Long customerId, Long vehicleId) {
        PessimisticCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        PessimisticVehicle vehicle = vehicleRepository.findForReservation(vehicleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"));


        List<PessimisticReservation> activeReservations =
                reservationRepository.findByVehicleIdAndStatus(
                        vehicleId, PessimisticReservationStatus.ACTIVE);

        if (!activeReservations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Vehicle already has an active reservation");
        }

        PessimisticReservation reservation = new PessimisticReservation(customer, vehicle);
        return reservationRepository.saveAndFlush(reservation);
    }

    public PessimisticReservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reservation not found"));
    }
    public List<PessimisticReservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<PessimisticReservation> getReservationsByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer not found");
        }

        return reservationRepository.findByCustomerId(customerId);
    }

    @Transactional
    public PessimisticReservation cancelReservation(Long id) {
        PessimisticReservation reservation = getReservationById(id);

        Long vehicleId = reservation.getVehicle().getId();

        vehicleRepository.findForReservation(vehicleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"));
        entityManager.refresh(reservation);

        if (reservation.getStatus() == PessimisticReservationStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Reservation is already cancelled");
        }
        reservation.setStatus(PessimisticReservationStatus.CANCELLED);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public PessimisticReservation activeReservation(Long id) {
        PessimisticReservation reservation = getReservationById(id);

        Long vehicleId = reservation.getVehicle().getId();

        vehicleRepository.findForReservation(vehicleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"));
        entityManager.refresh(reservation);

        List<PessimisticReservation> activeReservations =
                reservationRepository.findByVehicleIdAndStatus(
                        vehicleId, PessimisticReservationStatus.ACTIVE);

        if (!activeReservations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Vehicle already has an active reservation");
        }

        reservation.setStatus(PessimisticReservationStatus.ACTIVE);

        return reservationRepository.save(reservation);
    }
}
