package com.automation.selenium;

public class SeleniumUtilsException extends RuntimeException {

    public SeleniumUtilsException(Exception ex, AutomationStatusMsgType msgType, String message) {
        super("[" + msgType.name().toUpperCase() + "] " + message + " | Cause: " + ex.getMessage(), ex);
    }

    public SeleniumUtilsException(String message, AutomationStatusMsgType msgType) {
        super("[" + msgType.name().toUpperCase() + "] " + message);
    }
}
