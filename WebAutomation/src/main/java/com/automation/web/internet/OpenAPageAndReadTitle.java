package com.automation.web.internet;

import com.automation.web.BaseTest;

public class OpenAPageAndReadTitle extends BaseTest {

    // Goal: Launch a browser and validate page title
    public void run() {
        log.Info("Navigating to: https://example.com/");
        driver.get("https://example.com/");

        String title = driver.getTitle();
        log.Info("Page title is: " + title);

        if (title.equals("Example Domain")) {
            log.Info("Title is verified");
        } else {
            log.Error("Title is NOT verified");
        }
        tearDown();
    }

    public static void main(String[] args) {
        new OpenAPageAndReadTitle().run();
    }
}
