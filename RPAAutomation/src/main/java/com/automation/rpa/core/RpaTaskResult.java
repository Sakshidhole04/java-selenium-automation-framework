package com.automation.rpa.core;

/**
 * Immutable result object returned after each RPA task execution.
 * Carries the task name, outcome status, message, and elapsed time.
 */
public class RpaTaskResult {

    private final String taskName;
    private final RpaStatus status;
    private final String message;
    private final long durationMs;

    public RpaTaskResult(String taskName, RpaStatus status, String message, long durationMs) {
        this.taskName   = taskName;
        this.status     = status;
        this.message    = message;
        this.durationMs = durationMs;
    }

    public String   getTaskName()   { return taskName; }
    public RpaStatus getStatus()    { return status; }
    public String   getMessage()    { return message; }
    public long     getDurationMs() { return durationMs; }

    @Override
    public String toString() {
        return String.format("[%-11s] %-45s | %s (%.3fs)",
                status, taskName, message, durationMs / 1000.0);
    }
}
