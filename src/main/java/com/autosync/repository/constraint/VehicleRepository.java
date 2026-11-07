package com.autosync.repository.constraint;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autosync.model.constraint.Vehicle;


public interface VehicleRepository extends JpaRepository<Vehicle, Long> {}
