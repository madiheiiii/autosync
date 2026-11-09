package com.autosync.repository.pessimistic;
import org.springframework.data.jpa.repository.JpaRepository;
import com.autosync.model.pessimistic.PessimisticReservation;
import com.autosync.model.pessimistic.PessimisticReservationStatus;
import java.util.List;

public interface PessimisticReservationRepository extends JpaRepository<PessimisticReservation, Long>{
    List<PessimisticReservation> findByVehicleIdAndStatus(Long vehicleId, PessimisticReservationStatus status);
    List<PessimisticReservation> findByCustomerId(Long customerId);
    
}
