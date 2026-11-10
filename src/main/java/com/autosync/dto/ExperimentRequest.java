package com.autosync.dto;

import java.util.List;

public record ExperimentRequest(
                String strategy,
                Long vehicleId,
                List<Long> customerIds) {
}