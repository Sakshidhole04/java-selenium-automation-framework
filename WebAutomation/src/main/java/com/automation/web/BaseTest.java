package com.automation.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.automation.selenium.CommonSeleniumUtils;
import com.automation.logging.CustomLog;
import java.io.InputStream;
import java.util.Properties;

public class BaseTest {

    protected CustomLog log = new CustomLog(this.getClass());
    protected WebDriver driver;
    protected CommonSeleniumUtils commonSeleniumUtils = new CommonSeleniumUtils();
    protected static final String baseUrl;

    static {
        Properties props = new Properties();
        try (InputStream in = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
        baseUrl = props.getProperty("base.url");
    }

    public BaseTest() {
        driver = new ChromeDriver();
    }

    protected void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
