
package com.autosync.controller.optimistic;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.autosync.model.optimistic.OptimisticVehicle;
import com.autosync.service.optimistic.OptimisticVehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("optimistic/vehicles")
public class OptimisticVehicleController {

    private final OptimisticVehicleService vehicleService;

    public OptimisticVehicleController(OptimisticVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<OptimisticVehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public OptimisticVehicle getVehicleById(@PathVariable Long id) {
        return vehicleService.getAllVehicles(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"));
    }

    @PostMapping
    public OptimisticVehicle createVehicle(@Valid @RequestBody OptimisticVehicle vehicle) {
        return vehicleService.createVehicle(vehicle);
    }
}
