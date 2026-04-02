package com.automation.web.internet;

import com.automation.web.BaseTest;

public class FirstRealPageInteraction extends BaseTest {

    // Goal: Locate and click links using simple locators
    public void run() {
        log.Info("Navigating to: " + baseUrl);
        driver.get(baseUrl + "/");

        commonSeleniumUtils.clickElement(driver, "xpath", "//a[text()='Form Authentication']");
        log.Info("Clicked Form Authentication link");

        String headerName = commonSeleniumUtils.getWebFieldText(driver, "tagname", "h2");
        log.Info("Header: " + headerName);

        if (headerName.equals("Login Page")) {
            log.Info("Login page is verified");
        } else {
            log.Error("Login page is NOT verified");
        }
        tearDown();
    }

    public static void main(String[] args) {
        new FirstRealPageInteraction().run();
    }
}
