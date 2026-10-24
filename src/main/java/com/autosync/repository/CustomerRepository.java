package com.autosync.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.autosync.model.Customer;
public interface CustomerRepository extends JpaRepository<Customer, Long>{}

