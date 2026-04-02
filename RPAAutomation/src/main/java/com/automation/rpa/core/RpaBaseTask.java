package com.automation.rpa.core;

import com.automation.logging.CustomLog;

/**
 * Abstract base class for all RPA tasks.
 *
 * Subclasses implement {@link #execute()} with the actual automation logic.
 * This class handles timing, logging, and wrapping the result into
 * an {@link RpaTaskResult}.
 *
 * Usage:
 * <pre>
 *   public class MyTask extends RpaBaseTask {
 *       public MyTask() { super("My Task Description"); }
 *
 *       {@literal @}Override
 *       protected void execute() throws Exception {
 *           // automation steps
 *       }
 *   }
 * </pre>
 */
public abstract class RpaBaseTask {

    protected final CustomLog log = new CustomLog(this.getClass());

    private final String taskName;

    protected RpaBaseTask(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Runs the task, measures execution time, and returns a result.
     * Never throws — failures are captured in the returned {@link RpaTaskResult}.
     */
    public final RpaTaskResult run() {
        long start = System.currentTimeMillis();
        log.Info("▶ Starting  : " + taskName);
        try {
            execute();
            long duration = System.currentTimeMillis() - start;
            log.Info("✔ Completed : " + taskName + " (" + duration + "ms)");
            return new RpaTaskResult(taskName, RpaStatus.COMPLETED, "Success", duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.Error("✘ Failed    : " + taskName + " — " + e.getMessage(), e);
            return new RpaTaskResult(taskName, RpaStatus.FAILED, e.getMessage(), duration);
        }
    }

    /**
     * Implement the core automation logic here.
     * Throw any exception to mark the task as FAILED.
     */
    protected abstract void execute() throws Exception;

    public String getTaskName() {
        return taskName;
    }
}
