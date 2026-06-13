package com.software.jpa_locking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {
    private long successfulOrdersPessimistic;
    private long successfulOrdersOptimistic;
    private long failedOrdersOutOfStock;
    private long failedOrdersConflict;
    private long averageLockWaitTimeMs;
}
