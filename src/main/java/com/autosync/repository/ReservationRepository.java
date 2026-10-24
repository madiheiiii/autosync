
package com.autosync.repository;

import com.autosync.model.Reservation;
import com.autosync.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    List<Reservation> findByVehicleIdAndStatus(
            Long vehicleId,
            ReservationStatus status
    );

    List<Reservation> findByCustomerId(Long customerId);
}
