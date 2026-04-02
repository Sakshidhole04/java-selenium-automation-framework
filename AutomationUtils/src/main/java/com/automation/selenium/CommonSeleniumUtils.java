package com.automation.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class CommonSeleniumUtils extends CommonSeleniumBaseUtils {

    // ── Click Methods ──────────────────────────────────────────────────────────

    public boolean clickElement(WebDriver driver, String locatorType, String locatorPath) {
        try {
            waitAndFindElement(driver, locatorType, locatorPath).click();
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to click element [" + locatorPath + "]");
        }
    }

    public boolean clickStaleElement(WebDriver driver, String locatorType, String locatorPath) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.stalenessOf(findWebObj(driver, locatorType, locatorPath)));
            waitAndFindElement(driver, locatorType, locatorPath).click();
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to click stale element [" + locatorPath + "]");
        }
    }

    // ── Text / Field Methods ───────────────────────────────────────────────────

    public String getWebFieldText(WebDriver driver, String locatorType, String locatorPath) {
        try {
            return waitAndFindElement(driver, locatorType, locatorPath).getText();
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to get text from element [" + locatorPath + "]");
        }
    }

    public boolean setWebFieldText(WebDriver driver, String locatorType, String locatorPath, String text) {
        try {
            WebElement el = waitAndFindElement(driver, locatorType, locatorPath);
            el.clear();
            el.sendKeys(text);
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to set text on element [" + locatorPath + "]");
        }
    }

    public boolean setPasswordField(WebDriver driver, String locatorType, String locatorPath, String password) {
        try {
            WebElement el = waitAndFindElement(driver, locatorType, locatorPath);
            el.clear();
            el.sendKeys(password);
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to set password field [" + locatorPath + "]");
        }
    }

    public boolean setWebFieldTextWithoutClick(WebDriver driver, String locatorType, String locatorPath, String text) {
        try {
            WebElement el = findWebObj(driver, locatorType, locatorPath);
            ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", el, text);
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to set text without click [" + locatorPath + "]");
        }
    }

    // ── Attribute / Selection Methods ─────────────────────────────────────────

    public String getAttributeValueFromATag(WebDriver driver, String locatorType, String locatorPath, String attribute) {
        try {
            return waitAndFindElement(driver, locatorType, locatorPath).getAttribute(attribute);
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to get attribute [" + attribute + "] from [" + locatorPath + "]");
        }
    }

    public boolean checkAndRetrieveRadioButton(WebDriver driver, String locatorType, String locatorPath) {
        try {
            WebElement el = waitAndFindElement(driver, locatorType, locatorPath);
            if (!el.isSelected()) {
                el.click();
            }
            return el.isSelected();
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to check radio button [" + locatorPath + "]");
        }
    }

    public String getSelectedOptionTextFromDropdown(WebDriver driver, String locatorType, String locatorPath) {
        try {
            Select select = new Select(waitAndFindElement(driver, locatorType, locatorPath));
            return select.getFirstSelectedOption().getText();
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to get selected dropdown option [" + locatorPath + "]");
        }
    }

    public boolean selectTextFromDropdown(WebDriver driver, String locatorType, String locatorPath, String visibleText) {
        try {
            Select select = new Select(waitAndFindElement(driver, locatorType, locatorPath));
            select.selectByVisibleText(visibleText);
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to select [" + visibleText + "] from dropdown [" + locatorPath + "]");
        }
    }

    // ── Keyboard Methods ──────────────────────────────────────────────────────

    public boolean pressKeyBoardKey(WebDriver driver, Keys key) {
        try {
            Actions action = new Actions(driver);
            action.sendKeys(key).perform();
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to press key [" + key.name() + "]");
        }
    }

    public boolean pressEnterToElement(WebDriver driver, String locatorType, String locatorPath) {
        try {
            waitAndFindElement(driver, locatorType, locatorPath).sendKeys(Keys.ENTER);
            return true;
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to press ENTER on element [" + locatorPath + "]");
        }
    }

    // ── Multi-element Methods ─────────────────────────────────────────────────

    public List<WebElement> findElements(WebDriver driver, String locatorType, String locatorPath) {
        try {
            return driver.findElements(getBy(locatorType, locatorPath));
        } catch (Exception ex) {
            throw new SeleniumUtilsException(ex, AutomationStatusMsgType.error,
                "Failed to find elements [" + locatorPath + "]");
        }
    }
}
