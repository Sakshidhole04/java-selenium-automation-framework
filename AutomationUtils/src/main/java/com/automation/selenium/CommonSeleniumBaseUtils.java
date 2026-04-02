package com.automation.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CommonSeleniumBaseUtils {

    private static final int DEFAULT_WAIT = 10;

    public WebElement findWebObj(WebDriver driver, String locatorType, String locatorPath) {
        try {
            return driver.findElement(getBy(locatorType, locatorPath));
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Unable to find element [" + locatorType + "=" + locatorPath + "]");
        }
    }

    public WebElement waitAndFindElement(WebDriver driver, String locatorType, String locatorPath) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(getBy(locatorType, locatorPath)));
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Element not visible within timeout [" + locatorType + "=" + locatorPath + "]");
        }
    }

    public boolean clickHiddenElement(WebDriver driver, String waitLocatorType, String waitLocatorPath,
                                      String clickLocatorType, String clickLocatorPath) {
        try {
            waitAndFindElement(driver, waitLocatorType, waitLocatorPath);
            Actions action = new Actions(driver);
            WebElement elementToClick = findWebObj(driver, clickLocatorType, clickLocatorPath);
            action.moveToElement(elementToClick).perform();
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementToClick);
            ((JavascriptExecutor) driver).executeScript("return arguments[0].click();", elementToClick);
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to click hidden element [" + clickLocatorPath + "]");
        }
    }

    public boolean doubleClickHiddenElement(WebDriver driver, String waitLocatorType, String waitLocatorPath,
                                             String clickLocatorType, String clickLocatorPath) {
        try {
            waitAndFindElement(driver, waitLocatorType, waitLocatorPath);
            Actions action = new Actions(driver);
            WebElement elementToClick = findWebObj(driver, clickLocatorType, clickLocatorPath);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementToClick);
            action.moveToElement(elementToClick).doubleClick(elementToClick).perform();
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to double-click element [" + clickLocatorPath + "]");
        }
    }

    protected By getBy(String locatorType, String locatorValue) {
        switch (locatorType.toLowerCase()) {
            case "xpath":   return By.xpath(locatorValue);
            case "id":      return By.id(locatorValue);
            case "css":     return By.cssSelector(locatorValue);
            case "name":    return By.name(locatorValue);
            case "tagname": return By.tagName(locatorValue);
            default: throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        }
    }
}
