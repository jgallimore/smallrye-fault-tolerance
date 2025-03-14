package io.smallrye.faulttolerance.core.rate.limit;

import io.smallrye.faulttolerance.core.stopwatch.RunningStopwatch;
import io.smallrye.faulttolerance.core.stopwatch.Stopwatch;

final class FixedWindow implements TimeWindow {
    private final RunningStopwatch stopwatch;

    private final int maxInvocations;
    private final long timeWindowInMillis;
    private final long minSpacingInMillis;

    private long currentPermits; // could be int, but that would require guarding against underflow
    private long nextRefresh;

    private long lastInvocation;

    FixedWindow(Stopwatch stopwatch, int maxInvocations, long timeWindowInMillis, long minSpacingInMillis) {
        this.stopwatch = stopwatch.start();
        this.maxInvocations = maxInvocations;
        this.timeWindowInMillis = timeWindowInMillis;
        this.minSpacingInMillis = minSpacingInMillis;

        this.currentPermits = maxInvocations;
        this.nextRefresh = timeWindowInMillis;
        this.lastInvocation = -minSpacingInMillis;
    }

    @Override
    public synchronized boolean record() {
        long now = stopwatch.elapsedTimeInMillis();
        if (now >= nextRefresh) {
            currentPermits = maxInvocations;
            // how many time windows has passed: (now - nextRefresh) / timeWindowInMillis
            // how many time windows needs to be added to obtain the next refresh time: 1 + time windows passed
            nextRefresh += timeWindowInMillis * (1 + (now - nextRefresh) / timeWindowInMillis);
        }

        boolean allowInvocation = currentPermits > 0;
        if (allowInvocation && minSpacingInMillis != 0 && now - lastInvocation < minSpacingInMillis) {
            allowInvocation = false;
        }

        currentPermits--;
        lastInvocation = now;

        return allowInvocation;
    }
}
