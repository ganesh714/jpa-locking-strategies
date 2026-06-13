package com.software.jpa_locking.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import com.software.jpa_locking.dto.StatsResponse;

@Service
public class OrderStatsService {

    private final AtomicLong successfulOrdersPessimistic = new AtomicLong(0);
    private final AtomicLong successfulOrdersOptimistic = new AtomicLong(0);
    private final AtomicLong failedOrdersOutOfStock = new AtomicLong(0);
    private final AtomicLong failedOrdersConflict = new AtomicLong(0);

    public void incrementSuccessfulPessimistic() {
        successfulOrdersPessimistic.incrementAndGet();
    }

    public void incrementSuccessfulOptimistic() {
        successfulOrdersOptimistic.incrementAndGet();
    }

    public void incrementFailedOutOfStock() {
        failedOrdersOutOfStock.incrementAndGet();
    }

    public void incrementFailedConflict() {
        failedOrdersConflict.incrementAndGet();
    }

    public void reset() {
        successfulOrdersPessimistic.set(0);
        successfulOrdersOptimistic.set(0);
        failedOrdersOutOfStock.set(0);
        failedOrdersConflict.set(0);
    }

    public StatsResponse getStats() {
        return new StatsResponse(
                successfulOrdersPessimistic.get(),
                successfulOrdersOptimistic.get(),
                failedOrdersOutOfStock.get(),
                failedOrdersConflict.get(),
                -1 // averageLockWaitTimeMs as required placeholder
        );
    }
}
