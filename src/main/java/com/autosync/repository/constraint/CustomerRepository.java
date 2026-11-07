package com.autosync.repository.constraint;
import org.springframework.data.jpa.repository.JpaRepository;

import com.autosync.model.constraint.Customer;
public interface CustomerRepository extends JpaRepository<Customer, Long>{}

