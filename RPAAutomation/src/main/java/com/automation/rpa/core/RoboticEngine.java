package com.automation.rpa.core;

import com.automation.logging.CustomLog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Low-level robotic engine wrapping Java's {@link java.awt.Robot} API.
 *
 * Provides keyboard simulation, mouse control, clipboard interaction,
 * screen capture, and pixel color inspection. This engine is the core
 * driver behind all desktop-level RPA operations.
 */
public class RoboticEngine {

    private static final CustomLog log = new CustomLog(RoboticEngine.class);

    private final Robot robot;

    public RoboticEngine() throws AWTException {
        this.robot = new Robot();
        this.robot.setAutoDelay(100);
    }

    public RoboticEngine(int autoDelayMs) throws AWTException {
        this.robot = new Robot();
        this.robot.setAutoDelay(autoDelayMs);
    }

    // ── Keyboard ──────────────────────────────────────────────────────────────

    /**
     * Types text by pushing it to the system clipboard and pasting (Ctrl+V).
     * This is the most reliable approach for Unicode and special characters.
     */
    public void typeText(String text) {
        StringSelection sel = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        log.Info("Typed text via clipboard.");
    }

    /** Presses and releases the Enter key. */
    public void pressEnter() {
        pressKey(KeyEvent.VK_ENTER);
    }

    /** Presses and releases the Tab key. */
    public void pressTab() {
        pressKey(KeyEvent.VK_TAB);
    }

    /** Presses and releases the Escape key. */
    public void pressEscape() {
        pressKey(KeyEvent.VK_ESCAPE);
    }

    /** Presses and releases a single key by {@link KeyEvent} constant. */
    public void pressKey(int keyCode) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
    }

    /**
     * Presses a hotkey combination, e.g. Ctrl+S.
     * @param modifier e.g. {@link KeyEvent#VK_CONTROL}
     * @param key      e.g. {@link KeyEvent#VK_S}
     */
    public void pressHotkey(int modifier, int key) {
        robot.keyPress(modifier);
        robot.keyPress(key);
        robot.keyRelease(key);
        robot.keyRelease(modifier);
        log.Info("Hotkey pressed: modifier=" + modifier + ", key=" + key);
    }

    // ── Mouse ─────────────────────────────────────────────────────────────────

    /** Moves the mouse cursor to the specified screen coordinates. */
    public void moveTo(int x, int y) {
        robot.mouseMove(x, y);
    }

    /** Moves to (x, y) and performs a single left click. */
    public void leftClick(int x, int y) {
        moveTo(x, y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        log.Info("Left click at (" + x + ", " + y + ")");
    }

    /** Moves to (x, y) and performs a double left click. */
    public void doubleClick(int x, int y) {
        leftClick(x, y);
        delay(80);
        leftClick(x, y);
        log.Info("Double click at (" + x + ", " + y + ")");
    }

    /** Moves to (x, y) and performs a right click. */
    public void rightClick(int x, int y) {
        moveTo(x, y);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        log.Info("Right click at (" + x + ", " + y + ")");
    }

    /** Performs a click-and-drag from (x1,y1) to (x2,y2). */
    public void dragAndDrop(int x1, int y1, int x2, int y2) {
        moveTo(x1, y1);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        delay(200);
        moveTo(x2, y2);
        delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        log.Info("Drag from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
    }

    // ── Screen ────────────────────────────────────────────────────────────────

    /**
     * Captures the entire screen and saves it as a PNG file.
     * @param outputPath full file path including filename (e.g. "output/screen.png")
     * @return the saved {@link File}
     */
    public File captureFullScreen(String outputPath) throws IOException {
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return captureRegion(screen, outputPath);
    }

    /**
     * Captures a specific region of the screen and saves it as a PNG.
     * @param region     the screen area to capture
     * @param outputPath full file path including filename
     * @return the saved {@link File}
     */
    public File captureRegion(Rectangle region, String outputPath) throws IOException {
        BufferedImage screenshot = robot.createScreenCapture(region);
        File file = new File(outputPath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        ImageIO.write(screenshot, "png", file);
        log.Info("Screenshot saved → " + outputPath);
        return file;
    }

    /**
     * Returns the RGB color of a specific pixel on screen.
     * Useful for conditional checks (e.g. waiting for a button to turn green).
     */
    public Color getPixelColor(int x, int y) {
        return robot.getPixelColor(x, y);
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    /** Pauses execution for the given number of milliseconds. */
    public void delay(int milliseconds) {
        robot.delay(milliseconds);
    }

    /** Changes the automatic delay between every robot action. Default is 100ms. */
    public void setAutoDelay(int ms) {
        robot.setAutoDelay(ms);
    }
}
