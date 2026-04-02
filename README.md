# Java Selenium Automation Framework

A robust **Java 17 + Selenium 4** automation framework designed with clear separation between reusable utilities and test execution logic. Built as a **Maven multi‑module project** to support scalability, maintainability, and real‑world automation practices.

---

## 📌 Framework Overview
- Java 17 based Selenium WebDriver framework
- Selenium 4 compliant with modern browser automation
- Maven **multi‑module architecture**
  - `AutomationUtils` (Reusable utility library)
  - `WebAutomation` (Test execution layer)

---

## ⚙️ AutomationUtils (Reusable Utility Module)
A standalone, reusable automation library providing core web interaction capabilities.

- Custom base utilities with **explicit wait mechanisms**
- Supports multiple locator strategies:
  - XPath
  - ID
  - CSS Selector
  - Name
  - TagName
- Advanced element handling:
  - Stale element recovery
  - Hidden or off‑screen element interaction
  - JavaScript-based clicking
- Dropdown handling:
  - Select by visible text
  - Select by value
  - Select by index
- Keyboard and mouse actions using **Selenium Actions API**
- Custom exception framework:
  - Status-type tracking for better error reporting
- **Log4j2 structured logging**
  - Daily rolling log files
  - Consistent log format for debugging and analysis

---

## 🧪 WebAutomation (Test Layer Module)
The test execution layer built on top of the reusable utilities.

- Extends `BaseTest` for:
  - Clean WebDriver initialization
  - Controlled setup and teardown
- Externalized configuration:
  - Managed via `config.properties`
  - Supports browser selection and base URL configuration
- Test target:
  - https://the-internet.herokuapp.com
- Demonstrates:
  - Page navigation
  - Web element interactions
  - CSS selector usage
  - Real-world automation scenarios

---

## 🏗 Design Highlights
- Clear separation between **utility logic** and **test logic**
- Reusable utility module installable via **local Maven repository**
- Centralized and consistent error handling
- Scalable framework design aligned with enterprise automation standards
- Clean, date-based, structured logging for traceability

---

## 🎯 Purpose
- Practice and demonstrate Selenium automation best practices
- Build a maintainable and reusable automation framework
- Prepare a portfolio-ready framework suitable for enterprise projects

---

## 👤 Author
**Sakshi Ashok Dhole**
