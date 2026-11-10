package com.autosync.dto;

public record ReservationAttemptResult(
                Long customerId,
                String outcome,
                long durationMs,
                String message) {
}