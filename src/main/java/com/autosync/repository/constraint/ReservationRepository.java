
package com.autosync.repository.constraint;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autosync.model.constraint.Reservation;
import com.autosync.model.constraint.ReservationStatus;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    List<Reservation> findByVehicleIdAndStatus(
            Long vehicleId,
            ReservationStatus status
    );

    List<Reservation> findByCustomerId(Long customerId);

    long countByVehicleIdAndStatus(Long vehicleId, ReservationStatus status);
}
