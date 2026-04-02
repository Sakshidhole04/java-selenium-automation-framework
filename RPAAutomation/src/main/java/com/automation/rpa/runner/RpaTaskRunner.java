package com.automation.rpa.runner;

import com.automation.logging.CustomLog;
import com.automation.rpa.core.RpaBaseTask;
import com.automation.rpa.core.RpaStatus;
import com.automation.rpa.core.RpaTaskResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates and executes a pipeline of {@link RpaBaseTask} instances.
 *
 * Tasks are executed sequentially in the order they were added.
 * If {@code stopOnFailure} is true, the pipeline halts at the first failure.
 *
 * Usage:
 * <pre>
 *   new RpaTaskRunner(true)
 *       .addTask(task1)
 *       .addTask(task2)
 *       .runAll();
 * </pre>
 */
public class RpaTaskRunner {

    private static final CustomLog log = new CustomLog(RpaTaskRunner.class);

    private final List<RpaBaseTask> tasks         = new ArrayList<>();
    private final boolean           stopOnFailure;

    /**
     * @param stopOnFailure if true, the pipeline stops immediately on first FAILED task
     */
    public RpaTaskRunner(boolean stopOnFailure) {
        this.stopOnFailure = stopOnFailure;
    }

    /** Adds a task to the end of the pipeline. Supports method chaining. */
    public RpaTaskRunner addTask(RpaBaseTask task) {
        tasks.add(task);
        return this;
    }

    /**
     * Executes all tasks in order and returns the list of results.
     * Always prints a summary at the end.
     */
    public List<RpaTaskResult> runAll() {
        List<RpaTaskResult> results = new ArrayList<>();

        log.Info("╔══════════════════════════════════════════════════════╗");
        log.Info("║         RPA Task Runner — " + tasks.size() + " task(s) queued          ║");
        log.Info("╚══════════════════════════════════════════════════════╝");

        for (int i = 0; i < tasks.size(); i++) {
            RpaBaseTask task = tasks.get(i);
            log.Info("[" + (i + 1) + "/" + tasks.size() + "] Running: " + task.getTaskName());

            RpaTaskResult result = task.run();
            results.add(result);

            if (result.getStatus() == RpaStatus.FAILED && stopOnFailure) {
                log.Error("Pipeline halted — stopOnFailure=true. Failed task: " + task.getTaskName());
                break;
            }
        }

        printSummary(results);
        return results;
    }

    private void printSummary(List<RpaTaskResult> results) {
        long completed = results.stream().filter(r -> r.getStatus() == RpaStatus.COMPLETED).count();
        long failed    = results.stream().filter(r -> r.getStatus() == RpaStatus.FAILED).count();
        long skipped   = tasks.size() - results.size();

        log.Info("══════════════════ RPA RUN SUMMARY ═══════════════════");
        log.Info(String.format("  Total: %d  |  ✔ Completed: %d  |  ✘ Failed: %d  |  ⊘ Skipped: %d",
                tasks.size(), completed, failed, skipped));
        log.Info("───────────────────────────────────────────────────────");
        results.forEach(r -> log.Info("  " + r));
        log.Info("═══════════════════════════════════════════════════════");
    }
}
