package com.autosync.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autosync.dto.ExperimentRequest;
import com.autosync.dto.ReservationAttemptResult;
import com.autosync.service.ExperimentService;

@RestController
@RequestMapping("/experiments")
public class ExperimentController {

    private final ExperimentService experimentService;

    public ExperimentController(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @PostMapping
    public List<ReservationAttemptResult> runExperiment(
            @RequestBody ExperimentRequest request) {

        return experimentService.runExperiment(request);
    }
}