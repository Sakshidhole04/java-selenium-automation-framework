# java-selenium-automation-framework

A modular Java-based Selenium web automation framework built with Maven. The project is split into two modules:

- **AutomationUtils** — reusable Selenium utility library
- **WebAutomation** — web test scripts that consume the utility library

---

## Project Structure

```
java-selenium-automation-framework/
├── AutomationUtils/          # Reusable Selenium utility library (JAR)
│   └── src/main/java/com/automation/
│       ├── selenium/
│       │   ├── CommonSeleniumBaseUtils.java   # Core: findElement, wait, click hidden
│       │   ├── CommonSeleniumUtils.java        # Extended: click, text, dropdowns, JS actions
│       │   ├── AutomationStatusMsgType.java    # Enum for status message types
│       │   └── SeleniumUtilsException.java     # Custom exception wrapper
│       └── logging/
│           └── CustomLog.java                  # Log4j2 wrapper
│
└── WebAutomation/            # Web test scripts
    └── src/main/java/com/automation/web/
        ├── BaseTest.java                        # Driver setup, config loading
        └── internet/
            ├── OpenAPageAndReadTitle.java
            ├── FirstRealPageInteraction.java
            ├── CSSSelectorPractice.java
            └── App.java
```

---

## Tech Stack

| Tool | Version |
|---|---|
| Java | 17 |
| Selenium Java | 4.19.1 |
| Maven | 3.x |
| Log4j2 | 2.23.1 |
| JUnit | 4.11 |
| ChromeDriver | Auto-managed |

---

## Prerequisites

- Java 17+
- Maven 3.x
- Google Chrome installed

---

## Setup

### 1. Build AutomationUtils (install to local Maven repo)

```bash
cd AutomationUtils
mvn clean install
```

### 2. Configure the target URL

Edit `WebAutomation/src/main/resources/config.properties`:

```properties
base.url=https://the-internet.herokuapp.com
browser=chrome
```

### 3. Run a test script

```bash
cd WebAutomation
mvn exec:java -Dexec.mainClass="com.automation.web.internet.OpenAPageAndReadTitle"
```

---

## Key Features

- **Fluent locator support** — `xpath`, `id`, `css`, `name`, `tagname`
- **Smart waits** — explicit `WebDriverWait` with `ExpectedConditions` built into every interaction
- **Stale element handling** — dedicated `clickStaleElement()` method
- **Hidden/off-screen element clicks** — `clickHiddenElement()` uses JS executor + scroll into view
- **Dropdown support** — select by text, value, or index via `Select`
- **Keyboard & mouse actions** — `Actions` API wrappers for hover, drag, key combos
- **Structured logging** — Log4j2 with daily rolling file output under `logs/`
- **Custom exceptions** — `SeleniumUtilsException` carries status type and root cause

---

## Logging

Logs are written to the `WebAutomation/logs/` directory, organized by date:

```
logs/
└── 2026-04-02/
    └── automation.log
```

---

## Module Dependency

`WebAutomation` depends on `AutomationUtils` as a local Maven artifact:

```xml
<dependency>
    <groupId>com.automation</groupId>
    <artifactId>automation-utils</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Always run `mvn clean install` in `AutomationUtils` first before building `WebAutomation`.

---

## License

This project is for demonstration and learning purposes.
