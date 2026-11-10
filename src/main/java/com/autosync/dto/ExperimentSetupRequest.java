package com.autosync.dto;

public record ExperimentSetupRequest(
        String strategy,
        int customerCount) {
}
