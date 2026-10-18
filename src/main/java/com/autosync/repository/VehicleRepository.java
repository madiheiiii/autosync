package com.autosync.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autosync.model.Vehicle;


public interface VehicleRepository
        extends JpaRepository<Vehicle, Long> {
                
}
