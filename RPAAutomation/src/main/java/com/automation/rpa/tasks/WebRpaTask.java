package main.java.com.automation.rpa.tasks;

import com.automation.rpa.core.RpaBaseTask;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * RPA task that drives a real browser using Selenium WebDriver.
 *
 * Demo scenario on https://the-internet.herokuapp.com:
 *   1. Open the site home page
 *   2. Navigate to Form Authentication (Login page)
 *   3. Enter credentials and submit
 *   4. Verify the secure area flash message
 *   5. Click Logout and verify return to login page
 *
 * The browser is closed automatically in the finally block.
 */
public class WebRpaTask extends RpaBaseTask {

    private static final String BASE_URL  = "https://the-internet.herokuapp.com";
    private static final String USERNAME  = "tomsmith";
    private static final String PASSWORD  = "SuperSecretPassword!";
    private static final int    WAIT_SECS = 15;

    private final boolean headless;

    /**
     * @param headless true = run Chrome without a visible window (CI-friendly)
     */
    public WebRpaTask(boolean headless) {
        super("WebTask → Login & Logout on the-internet.herokuapp.com");
        this.headless = headless;
    }

    @Override
    protected void execute() throws Exception {
        WebDriver driver = buildDriver();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECS));

            // ── Step 1: Open homepage ───────────────────────────────────────
            log.Info("Step 1 | Opening: " + BASE_URL);
            driver.get(BASE_URL);
            log.Info("        Title: " + driver.getTitle());

            // ── Step 2: Click 'Form Authentication' link ────────────────────
            log.Info("Step 2 | Clicking 'Form Authentication' link");
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.linkText("Form Authentication"))).click();
            log.Info("        URL: " + driver.getCurrentUrl());

            // ── Step 3: Enter credentials ───────────────────────────────────
            log.Info("Step 3 | Entering credentials (user: " + USERNAME + ")");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
                    .sendKeys(USERNAME);
            driver.findElement(By.id("password")).sendKeys(PASSWORD);

            // ── Step 4: Submit login form ───────────────────────────────────
            log.Info("Step 4 | Submitting login form");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // ── Step 5: Verify secure area flash message ────────────────────
            WebElement flash = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
            String flashText = flash.getText().trim();
            log.Info("Step 5 | Flash message: " + flashText);

            if (!flashText.toLowerCase().contains("you logged into a secure area")) {
                throw new RuntimeException("Login failed — unexpected flash: " + flashText);
            }
            log.Info("        Login VERIFIED successfully!");

            // ── Step 6: Click Logout ────────────────────────────────────────
            log.Info("Step 6 | Clicking Logout");
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("a[href='/logout']"))).click();

            // ── Step 7: Verify redirect back to login page ──────────────────
            wait.until(ExpectedConditions.urlContains("/login"));
            log.Info("Step 7 | Logout VERIFIED — redirected to login page.");
            log.Info("        URL: " + driver.getCurrentUrl());

        } finally {
            driver.quit();
            log.Info("Browser closed.");
        }
    }

    private WebDriver buildDriver() {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080"
        );
        return new ChromeDriver(options);
    }
}
