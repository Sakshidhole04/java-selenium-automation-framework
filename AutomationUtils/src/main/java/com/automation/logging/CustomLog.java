package main.java.com.automation.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomLog {

    private final Logger logger;
    private StringBuilder transStrings = new StringBuilder();
    public String operationName = "";

    public CustomLog(Class<?> c) {
        logger = LogManager.getLogger(c);
    }

    public void Debug(String message) {
        logger.debug(message);
    }

    public void Info(String message) {
        logger.info(message);
    }

    public void Warn(String message) {
        logger.warn(message);
    }

    public void Error(String message) {
        logger.error(message);
    }

    public void Error(String message, Exception ex) {
        logger.error(message, ex);
    }

    public void Fatal(String message) {
        logger.fatal(message);
    }

    // ── Transaction Log Helpers ───────────────────────────────────────────────

    public void InitTransLog(String operationName) {
        this.operationName = operationName;
        this.transStrings = new StringBuilder();
        transStrings.append("[START] ").append(operationName).append("\n");
    }

    public void addTransLog(String step) {
        transStrings.append("  >> ").append(step).append("\n");
        logger.info(step);
    }

    public String readTransLog() {
        transStrings.append("[END] ").append(operationName);
        return transStrings.toString();
    }
}
