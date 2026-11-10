package com.autosync.service.optimistic;

import java.util.List;
import org.springframework.stereotype.Service;
import com.autosync.model.optimistic.OptimisticVehicle;
import com.autosync.repository.optimistic.OptimisticVehicleRepository;
import java.util.Optional;

@Service
public class OptimisticVehicleService {
    private final OptimisticVehicleRepository vehicleRepository;

    public OptimisticVehicleService(OptimisticVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<OptimisticVehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<OptimisticVehicle> getAllVehicles(Long id) {
        return vehicleRepository.findById(id);
    }

    public OptimisticVehicle createVehicle(OptimisticVehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

}
