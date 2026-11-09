package com.autosync.repository.optimistic;
import org.springframework.data.jpa.repository.JpaRepository;
import com.autosync.model.optimistic.OptimisticVehicle;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface OptimisticVehicleRepository extends JpaRepository<OptimisticVehicle, Long>{   
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select v from OptimisticVehicle v where v.id = :id")
    Optional<OptimisticVehicle> findForReservation(@Param("id") Long id);
}
