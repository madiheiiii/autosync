package com.autosync.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autosync.dto.ExperimentRequest;
import com.autosync.dto.ReservationAttemptResult;
import com.autosync.service.ExperimentService;
import com.autosync.dto.ExperimentResult;
import com.autosync.dto.ExperimentSetupRequest;
import com.autosync.service.ExperimentSetupService;

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
}