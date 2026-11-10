package com.autosync.service.pessimistic;

import java.util.List;
import org.springframework.stereotype.Service;
import com.autosync.model.pessimistic.PessimisticVehicle;
import com.autosync.repository.pessimistic.PessimisticVehicleRepository;
import java.util.Optional;

@Service
public class PessimisticVehicleService {
    private final PessimisticVehicleRepository vehicleRepository;

    public PessimisticVehicleService(PessimisticVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<PessimisticVehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<PessimisticVehicle> getAllVehicles(Long id) {
        return vehicleRepository.findById(id);
    }

    public PessimisticVehicle createVehicle(PessimisticVehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

}
