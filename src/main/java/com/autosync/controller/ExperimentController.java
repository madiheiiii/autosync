package com.autosync.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.autosync.dto.ExperimentRequest;
import com.autosync.dto.ReservationAttemptResult;
import com.autosync.service.ExperimentService;
import com.autosync.dto.ExperimentResult;
import com.autosync.dto.ExperimentSetupRequest;
import com.autosync.service.ExperimentSetupService;
import com.autosync.dto.ComparisonRequest;

@RestController
@RequestMapping("/experiments")
public class ExperimentController {

    private final ExperimentService experimentService;
    private final ExperimentSetupService experimentSetupService;

    public ExperimentController(
            ExperimentService experimentService,
            ExperimentSetupService experimentSetupService) {

        this.experimentService = experimentService;
        this.experimentSetupService = experimentSetupService;
    }

    @PostMapping
    public ExperimentResult runExperiment(
            @RequestBody ExperimentRequest request) {

        return experimentService.runExperiment(request);
    }

    @PostMapping("/setup")
    public ExperimentRequest setup(
            @RequestBody ExperimentSetupRequest request) {

        return experimentSetupService.setup(request);
    }

    @PostMapping("/compare")
    public List<ExperimentResult> compare(
            @RequestBody ComparisonRequest request) {

        if (request.customerCount() < 1 || request.customerCount() > 20) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Customer count must be between 1 and 20");
        }

        List<ExperimentResult> results = new java.util.ArrayList<>();

        for (String strategy : List.of("constraint", "optimistic", "pessimistic")) {

            ExperimentRequest experiment = experimentSetupService.setup(
                    new ExperimentSetupRequest(
                            strategy, request.customerCount()));

            results.add(experimentService.runExperiment(experiment));
        }

        return results;
    }

    private <T> T runExclusively(Supplier<T> operation) {
        if (!busy.compareAndSet(false, true)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An experiment operation is already running. Please wait.");
        }

        try {
            return operation.get();
        } finally {
            busy.set(false);
        }
    }
}