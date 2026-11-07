
package com.autosync.controller.constraint;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.autosync.model.constraint.Vehicle;
import com.autosync.service.constraint.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping ("/{id}")
    public Vehicle getVehicleById(@PathVariable Long id){
        return vehicleService.getAllVehicles(id)
                .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Vehicle not found"
        ));
    }
    @PostMapping
    public Vehicle createVehicle(@Valid @RequestBody Vehicle vehicle) {
      return vehicleService.createVehicle(vehicle);
    }
}
