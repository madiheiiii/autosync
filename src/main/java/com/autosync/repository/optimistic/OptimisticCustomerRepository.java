package com.autosync.repository.optimistic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autosync.model.optimistic.OptimisticCustomer;

public interface OptimisticCustomerRepository extends JpaRepository<OptimisticCustomer, Long> {
}
