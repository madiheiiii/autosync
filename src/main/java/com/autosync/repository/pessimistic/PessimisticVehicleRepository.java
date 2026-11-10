package com.autosync.repository.pessimistic;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autosync.model.pessimistic.PessimisticVehicle;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

import java.util.Optional;

public interface PessimisticVehicleRepository extends JpaRepository<PessimisticVehicle, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")
    })
    @Query("select v from PessimisticVehicle v where v.id = :id")
    Optional<PessimisticVehicle> findForReservation(@Param("id") Long id);
}
