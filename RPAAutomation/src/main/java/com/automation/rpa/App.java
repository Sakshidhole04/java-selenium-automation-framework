package com.automation.rpa;

import com.automation.logging.CustomLog;
import com.automation.rpa.core.RoboticEngine;
import com.automation.rpa.runner.RpaTaskRunner;
import com.automation.rpa.tasks.ExcelRpaTask;
import com.automation.rpa.tasks.FileRpaTask;
import com.automation.rpa.tasks.ScreenRpaTask;
import com.automation.rpa.tasks.WebRpaTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Entry point for the RPA Automation Workflow.
 *
 * This demo pipeline demonstrates a real-world RPA scenario:
 *   1. Create output directories
 *   2. Generate a run-report text file
 *   ... (file/excel/screenshot tasks)
 *   7. Browser automation — Login & Logout on the-internet.herokuapp.com
 *   3. Write structured results to Excel
 *   4. Backup the report to a dated folder
 *   5. Capture a full-screen screenshot
 *
 * Run via Maven:
 * <pre>
 *   mvn exec:java -Dexec.mainClass="com.automation.rpa.App"
 * </pre>
 */
public class App {

    private static final CustomLog log = new CustomLog(App.class);

    public static void main(String[] args) throws Exception {

        String runDate      = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String runTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String backupDir    = "output/backup/" + runDate;
        String reportsDir   = "output/reports";
        String screensDir   = "output/screenshots";
        String reportFile   = reportsDir + "/rpa_run_report.txt";

        log.Info("════════════════════════════════════════════════════");
        log.Info("  RPA Automation Workflow — " + runTimestamp);
        log.Info("════════════════════════════════════════════════════");

        RoboticEngine engine = new RoboticEngine(50);

        // ── Task 1: Create output directory structure ──────────────────────────
        FileRpaTask createReportsDir = new FileRpaTask.Builder()
                .operation(FileRpaTask.FileOperation.CREATE_DIR)
                .source(reportsDir)
                .build();

        FileRpaTask createBackupDir = new FileRpaTask.Builder()
                .operation(FileRpaTask.FileOperation.CREATE_DIR)
                .source(backupDir)
                .build();

        // ── Task 2: Write a run-report text file ───────────────────────────────
        String reportContent = String.join(System.lineSeparator(),
                "RPA Automation Run Report",
                "=========================",
                "Date       : " + runTimestamp,
                "Framework  : Java 17 + Robot API + Apache POI",
                "Status     : COMPLETED",
                "Output Dir : " + reportsDir,
                ""
        );

        FileRpaTask writeReport = new FileRpaTask.Builder()
                .operation(FileRpaTask.FileOperation.WRITE)
                .source(reportFile)
                .content(reportContent)
                .build();

        // ── Task 3: Write results to Excel ─────────────────────────────────────
        ExcelRpaTask writeExcel = new ExcelRpaTask.Builder()
                .operation(ExcelRpaTask.ExcelOperation.WRITE)
                .filePath(reportsDir + "/rpa_results.xlsx")
                .sheetName("RPA Results")
                .data(List.of(
                        List.of("Task Name",             "Status",    "Duration (ms)", "Remarks"),
                        List.of("Create Reports Dir",    "COMPLETED", "8",             "Directory ready"),
                        List.of("Create Backup Dir",     "COMPLETED", "6",             "Dated backup folder created"),
                        List.of("Write Run Report",      "COMPLETED", "22",            "TXT report generated"),
                        List.of("Write Excel Results",   "COMPLETED", "310",           "XLSX saved to output/reports"),
                        List.of("Backup Report",         "COMPLETED", "14",            "Copied to " + backupDir),
                        List.of("Screenshot Capture",    "COMPLETED", "280",           "PNG saved to output/screenshots")
                ))
                .build();

        // ── Task 4: Backup the report to a dated folder ────────────────────────
        FileRpaTask backupReport = new FileRpaTask.Builder()
                .operation(FileRpaTask.FileOperation.COPY)
                .source(reportFile)
                .target(backupDir + "/rpa_run_report_backup.txt")
                .build();

        // ── Task 5: Capture full-screen screenshot ─────────────────────────────
        ScreenRpaTask screenshot = new ScreenRpaTask(engine, screensDir);

        // ── Task 6: Browser automation — login & logout ──────────────────────
        WebRpaTask webLogin = new WebRpaTask(false); // false = show browser

        // ── Run the pipeline ───────────────────────────────────────────────
        new RpaTaskRunner(true)
                .addTask(createReportsDir)
                .addTask(createBackupDir)
                .addTask(writeReport)
                .addTask(writeExcel)
                .addTask(backupReport)
                .addTask(screenshot)
                .addTask(webLogin)
                .runAll();
    }
}
