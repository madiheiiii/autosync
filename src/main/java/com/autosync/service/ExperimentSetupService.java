package com.autosync.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.autosync.dto.ExperimentRequest;
import com.autosync.dto.ExperimentSetupRequest;
import com.autosync.model.constraint.Customer;
import com.autosync.model.constraint.Vehicle;
import com.autosync.model.optimistic.OptimisticCustomer;
import com.autosync.model.optimistic.OptimisticVehicle;
import com.autosync.model.pessimistic.PessimisticCustomer;
import com.autosync.model.pessimistic.PessimisticVehicle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExperimentSetupService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ExperimentRequest setup(ExperimentSetupRequest request) {
        if (request.strategy() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Strategy is required");
        }

        String strategy = request.strategy().toLowerCase(Locale.ROOT);

        if (!List.of("constraint", "optimistic", "pessimistic")
                .contains(strategy)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Unknown strategy");
        }

        if (request.customerCount() < 1 || request.customerCount() > 20) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Customer count must be between 1 and 20");
        }

        String runId = UUID.randomUUID().toString().replace("-", "");
        String vin = "TEST" + runId.substring(0, 13);

        Long vehicleId = switch (strategy) {
            case "constraint" -> {
                Vehicle vehicle = new Vehicle(vin, "Toyota", "Corolla", 2024);
                entityManager.persist(vehicle);
                yield vehicle.getId();
            }
            case "optimistic" -> {
                OptimisticVehicle vehicle = new OptimisticVehicle(vin, "Toyota", "Corolla", 2024);
                entityManager.persist(vehicle);
                yield vehicle.getId();
            }
            case "pessimistic" -> {
                PessimisticVehicle vehicle = new PessimisticVehicle(vin, "Toyota", "Corolla", 2024);
                entityManager.persist(vehicle);
                yield vehicle.getId();
            }
            default -> throw new IllegalStateException("Unexpected strategy");
        };

        List<Long> customerIds = new ArrayList<>();

        for (int i = 1; i <= request.customerCount(); i++) {
            String name = "Test Customer " + i;
            String email = "test-" + runId + "-" + i + "@example.com";

            Long customerId = switch (strategy) {
                case "constraint" -> {
                    Customer customer = new Customer(name, email);
                    entityManager.persist(customer);
                    yield customer.getId();
                }
                case "optimistic" -> {
                    OptimisticCustomer customer = new OptimisticCustomer(name, email);
                    entityManager.persist(customer);
                    yield customer.getId();
                }
                case "pessimistic" -> {
                    PessimisticCustomer customer = new PessimisticCustomer(name, email);
                    entityManager.persist(customer);
                    yield customer.getId();
                }
                default -> throw new IllegalStateException("Unexpected strategy");
            };

            customerIds.add(customerId);
        }

        entityManager.flush();

        return new ExperimentRequest(
                strategy, vehicleId, List.copyOf(customerIds));
    }
}