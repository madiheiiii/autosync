package com.autosync.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.autosync.dto.ExperimentRequest;
import com.autosync.dto.ReservationAttemptResult;
import com.autosync.service.optimistic.OptimisticReservationService;
import com.autosync.service.pessimistic.PessimisticReservationService;
import com.autosync.service.constraint.ReservationService;
import com.autosync.repository.constraint.ReservationRepository;
import com.autosync.repository.optimistic.OptimisticReservationRepository;
import com.autosync.repository.pessimistic.PessimisticReservationRepository;
import com.autosync.dto.ExperimentResult;
import com.autosync.model.constraint.ReservationStatus;
import com.autosync.model.optimistic.OptimisticReservationStatus;
import com.autosync.model.pessimistic.PessimisticReservationStatus;
import java.util.Locale;

@Service
public class ExperimentService {
    private final ReservationRepository constraintReservationRepository;
    private final OptimisticReservationRepository optimisticReservationRepository;
    private final PessimisticReservationRepository pessimisticReservationRepository;
    private final PessimisticReservationService pessimisticReservationService;
    private final OptimisticReservationService optimisticReservationService;
    private final ReservationService constraintReservationService;

    public ExperimentService(PessimisticReservationService pessimisticReservationService,
            OptimisticReservationService optimisticReservationService, ReservationService reservationService,
            ReservationRepository constraintReservationRepository,
            OptimisticReservationRepository optimisticReservationRepository,
            PessimisticReservationRepository pessimisticReservationRepository) {
        this.pessimisticReservationService = pessimisticReservationService;
        this.optimisticReservationService = optimisticReservationService;
        this.constraintReservationService = reservationService;
        this.constraintReservationRepository = constraintReservationRepository;
        this.optimisticReservationRepository = optimisticReservationRepository;
        this.pessimisticReservationRepository = pessimisticReservationRepository;
    }

    public ExperimentResult runExperiment(ExperimentRequest request) {

        if (!"pessimistic".equalsIgnoreCase(request.strategy()) && !"optimistic".equalsIgnoreCase(request.strategy())
                && !"constraint".equalsIgnoreCase(request.strategy())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "strategy must be pessimistic, optimistic, or constraint");
        }

        if (request.vehicleId() == null
                || request.customerIds() == null
                || request.customerIds().isEmpty()
                || request.customerIds().size() > 20
                || request.customerIds().stream().anyMatch(id -> id == null)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Provide a vehicle ID and between 1 and 20 customer IDs");
        }

        ExecutorService executor = Executors.newFixedThreadPool(request.customerIds().size());

        CountDownLatch startSignal = new CountDownLatch(1);

        try {
            List<CompletableFuture<ReservationAttemptResult>> attempts = request.customerIds().stream()
                    .map(customerId -> CompletableFuture.supplyAsync(() -> {
                        try {
                            startSignal.await();
                        } catch (InterruptedException exception) {
                            Thread.currentThread().interrupt();

                            return new ReservationAttemptResult(
                                    customerId,
                                    "ERROR",
                                    0,
                                    "Attempt interrupted before starting");
                        }

                        return runAttempt(request.strategy(), customerId, request.vehicleId());
                    }, executor))
                    .toList();

            long start = System.nanoTime();
            startSignal.countDown();

            List<ReservationAttemptResult> results = attempts.stream().map(CompletableFuture::join).toList();

            long totalDurationMs = (System.nanoTime() - start) / 1_000_000;
            long successCount = results.stream().filter(result -> "SUCCESS".equals(result.outcome())).count();
            long conflictCount = results.stream().filter(result -> "CONFLICT".equals(result.outcome())).count();
            long errorCount = results.stream().filter(result -> "ERROR".equals(result.outcome())).count();
            double averageAttemptDurationMs = results.stream().mapToLong(ReservationAttemptResult::durationMs).average()
                    .orElse(0);
            String strategy = request.strategy().toLowerCase(Locale.ROOT);
            long activeReservationCount = switch (strategy) {
                case "constraint" -> constraintReservationRepository.countByVehicleIdAndStatus(request.vehicleId(),
                        ReservationStatus.ACTIVE);
                case "optimistic" -> optimisticReservationRepository.countByVehicleIdAndStatus(request.vehicleId(),
                        OptimisticReservationStatus.ACTIVE);
                case "pessimistic" -> pessimisticReservationRepository.countByVehicleIdAndStatus(request.vehicleId(),
                        PessimisticReservationStatus.ACTIVE);
                default -> throw new IllegalStateException("Unexpected strategy");
            };

            return new ExperimentResult(
                    strategy,
                    request.vehicleId(),
                    results.size(),
                    successCount,
                    conflictCount,
                    errorCount,
                    totalDurationMs,
                    averageAttemptDurationMs,
                    activeReservationCount,
                    activeReservationCount <= 1,
                    results);

        } finally {
            executor.shutdown();
        }
    }

    public ReservationAttemptResult runAttempt(String strategy, Long customerId, Long vehicleId) {
        long start = System.nanoTime();
        String outcome;
        String message;

        try {
            switch (strategy.toLowerCase(java.util.Locale.ROOT)) {
                case "optimistic" ->
                    optimisticReservationService.createReservation(customerId, vehicleId);

                case "pessimistic" ->
                    pessimisticReservationService.createReservation(customerId, vehicleId);

                case "constraint" ->
                    constraintReservationService.createReservation(customerId, vehicleId);

                default ->
                    throw new IllegalArgumentException("Unknown strategy: " + strategy);
            }
            outcome = "SUCCESS";
            message = "Reservation created";
        } catch (OptimisticLockingFailureException exception) {
            outcome = "CONFLICT";
            message = "Vehicle version changed during this reservation attempt";

        } catch (PessimisticLockingFailureException exception) {
            outcome = "CONFLICT";
            message = "Could not acquire the vehicle lock";

        } catch (ResponseStatusException exception) {
            outcome = exception.getStatusCode().value() == 409
                    ? "CONFLICT"
                    : "ERROR";
            message = exception.getReason();

        } catch (Exception exception) {
            exception.printStackTrace();
            outcome = "ERROR";
            message = exception.getClass().getSimpleName();
        }

        long durationMs = (System.nanoTime() - start) / 1_000_000;

        return new ReservationAttemptResult(
                customerId, outcome, durationMs, message);
    }
}