package com.automation.rpa.tasks;

import com.automation.rpa.core.RpaBaseTask;
import com.automation.rpa.core.RoboticEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RPA task that captures a full-screen PNG screenshot with a timestamped filename.
 *
 * Screenshots are saved to the configured output directory:
 * <pre>
 *   output/screenshots/screenshot_2026-04-02_22-41-00.png
 * </pre>
 *
 * Usage:
 * <pre>
 *   ScreenRpaTask task = new ScreenRpaTask(engine, "output/screenshots");
 * </pre>
 */
public class ScreenRpaTask extends RpaBaseTask {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private final RoboticEngine engine;
    private final String        screenshotsDir;

    public ScreenRpaTask(RoboticEngine engine, String screenshotsDir) {
        super("ScreenTask → capture to " + screenshotsDir);
        this.engine         = engine;
        this.screenshotsDir = screenshotsDir;
    }

    @Override
    protected void execute() throws Exception {
        String timestamp  = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String outputPath = screenshotsDir + "/screenshot_" + timestamp + ".png";
        engine.captureFullScreen(outputPath);
        log.Info("Screenshot saved: " + outputPath);
    }
}
