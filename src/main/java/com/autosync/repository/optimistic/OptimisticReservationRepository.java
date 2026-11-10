package com.autosync.repository.optimistic;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autosync.model.optimistic.OptimisticReservation;
import com.autosync.model.optimistic.OptimisticReservationStatus;
import java.util.List;

public interface OptimisticReservationRepository extends JpaRepository<OptimisticReservation, Long> {
    List<OptimisticReservation> findByVehicleIdAndStatus(Long vehicleId, OptimisticReservationStatus status);

    List<OptimisticReservation> findByCustomerId(Long customerId);

    long countByVehicleIdAndStatus(Long vehicleId, OptimisticReservationStatus status);

}
