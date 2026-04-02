package com.automation.web.internet;

import com.automation.web.BaseTest;

public class App extends BaseTest {

    public void run() {
        log.Info("Navigating to: " + baseUrl);
        driver.get(baseUrl);
        log.Info("Title = " + driver.getTitle());

        commonSeleniumUtils.clickElement(driver, "xpath", "//a[text()='Form Authentication']");
        log.Info("Clicked Form Authentication link");

        String actualHeader = commonSeleniumUtils.getWebFieldText(driver, "tagname", "h2");
        log.Info("Header: " + actualHeader);

        if (actualHeader.equals("Login Page")) {
            log.Info("Login page is verified");
        } else {
            log.Error("Login page is NOT verified");
        }
        tearDown();
    }

    public static void main(String[] args) {
        new App().run();
    }
}
