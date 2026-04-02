package com.automation.web.internet;

import com.automation.web.BaseTest;
import org.openqa.selenium.WebElement;
import java.util.List;

public class CSSSelectorPractice extends BaseTest {

    // Goal: Practice locating via CSS selectors
    public void run() {
        log.Info("Navigating to add/remove elements page");
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        commonSeleniumUtils.clickElement(driver, "css", "button[onclick='addElement()']");
        commonSeleniumUtils.clickElement(driver, "css", "button[onclick='addElement()']");
        commonSeleniumUtils.clickElement(driver, "css", "button[onclick='addElement()']");

        List<WebElement> deleteButtons = commonSeleniumUtils.findElements(driver, "css", "button[onclick='deleteElement()']");
        log.Info("Number of delete buttons = " + deleteButtons.size());

        deleteButtons.get(0).click();

        deleteButtons = commonSeleniumUtils.findElements(driver, "css", "button[onclick='deleteElement()']");
        log.Info("Number of delete buttons after clicking delete = " + deleteButtons.size());

        if (deleteButtons.size() == 2) {
            log.Info("Delete button count is decreased by 1");
        } else {
            log.Error("Delete button count is NOT decreased by 1");
        }
        tearDown();
    }

    public static void main(String[] args) {
        new CSSSelectorPractice().run();
    }
}
