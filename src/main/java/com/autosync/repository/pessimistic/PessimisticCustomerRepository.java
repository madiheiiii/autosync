package com.autosync.repository.pessimistic;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autosync.model.pessimistic.PessimisticCustomer;

public interface PessimisticCustomerRepository extends JpaRepository<PessimisticCustomer, Long> {
}
