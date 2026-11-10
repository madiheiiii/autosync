package com.autosync.dto;
import java.util.List;

public record ExperimentResult (
        String strategy,
        Long vehicleId,
        int totalAttempts,
        long successCount,
        long conflictCount,
        long errorCount,
        long totalDurationMs,
        double averageAttemptDurationMs,
        long activeReservationCount,
        boolean invariantSatisfied,
        List<ReservationAttemptResult> attempts
)   
{}
