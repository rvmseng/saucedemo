# SauceDemo Automation — Java · Cucumber · TestNG · Allure

> Automated test suite for the Sauce Demo application implemented using Java, Cucumber (BDD), TestNG for execution, and Allure for reporting.

## Table of Contents

1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Repository Structure](#repository-structure)
4. [Prerequisites](#prerequisites)
5. [Local Setup](#local-setup)
6. [Running Tests](#running-tests)

   * [Run full test suite](#run-full-test-suite)
   * [Run tests by Cucumber tag](#run-tests-by-cucumber-tag)
   * [Run a single feature or scenario](#run-a-single-feature-or-scenario)
7. [Allure Reporting](#allure-reporting)

   * [Generate Allure report (Maven + Allure CLI)](#generate-allure-report-maven--allure-cli)
   * [Common troubleshooting for Allure attachments/screenshots](#common-troubleshooting-for-allure-attachmentsscreenshots)
8. [Test Design & Conventions](#test-design--conventions)
9. [CI / GitHub Actions Example](#ci--github-actions-example)
10. [Best Practices & Recommendations](#best-practices--recommendations)
11. [Contribution](#contribution)
12. [License](#license)

---

## Project Overview

This repository contains an end-to-end automated test suite for the Sauce Demo web application. Tests are authored in Gherkin (`.feature` files) and implemented using Cucumber step definitions and service/page classes in Java. Test execution is managed by TestNG and test results are reported with Allure for clear, structured results and attachments (screenshots, logs).

Goals:

* Maintainable BDD-style acceptance tests.
* Reliable execution in local and CI environments.
* Rich, developer-friendly test reports (Allure).
* Reusable test utilities and clear separation of concerns (Page/Service/Steps).

---

## Tech Stack

* Java 17 (recommended) — JDK
* Maven (build & dependency management)
* Cucumber JVM (Gherkin features)
* TestNG (test runner / execution control)
* Allure (reporting)
* Playwright 
* Git & GitHub
* (Optional) Docker (for headless/containerized runs)

Example Maven dependencies (indicative):

* `io.cucumber:cucumber-java`
* `io.cucumber:cucumber-testng`
* `org.testng:testng`
* `io.qameta.allure:allure-testng`
* `org.seleniumhq.selenium:selenium-java` (or Playwright bindings)

---

## Repository Structure (recommended)

```
.
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   └── java
│   │       └── com.saucedemo
│   │           
│   └── test
│       ├── java
│       │   └── com.saucedemo
│       │       ├── runners         # TestNG / Cucumber runner classes
│       │       ├── steps           # Step definitions
│       │       └── listeners       # TestNG / Allure listeners
		|		├── config           # configuration classes, environment loader
│   	│		├── pages            # Page Object Model classes
│   	│       ├── services         # API / helper services
│   	│       └── utils            # logging, screenshots, test context
│       └── resources
│           └── features           # .feature files
│               ├── login.feature
│               └── checkout.feature
├── .github/workflows
│   └── ci.yml
└── target
    └── allure-results
```

---

## Prerequisites

* JDK 17+ installed and `JAVA_HOME` set.
* Maven 3.6+ installed and on `PATH` (`mvn` command).
* Node & Allure CLI (optional but recommended for `allure serve`):

  * Install Allure CLI (macOS via `brew install allure` or Linux/Windows via binary). Or use `io.qameta.allure:allure-maven` plugin.
* Browser driver or appropriate test runner available (e.g., ChromeDriver) or use headless containerized browsers.
* Internet connection to download dependencies.

---

## Local Setup

1. Clone repository:

```bash
git clone https://github.com/<your-org>/saucedemo-automation.git
cd saucedemo-automation
```

2. Configure environment properties:

* Provide `config.properties` or Maven profiles for environments (e.g., `uat`, `staging`, `prod`).
* Recommended keys: `base.url`, `browser`, `headless`, `timeout`, `remote.webdriver.url`.

Example `src/test/resources/config.properties`:

```
base.url=https://www.saucedemo.com
browser=chrome
headless=true
implicit.wait=5
```

3. Build project and download dependencies:

```bash
mvn clean compile -DskipTests
```

---

## Running Tests

> All commands assume you are in project root.

### Run full test suite

Default execution (runs all scenarios):

```bash
mvn test
```

If you use TestNG XML runner:

```bash
mvn -Dtest=TestRunner test
```

### Run tests by Cucumber tag

Use Cucumber tag filtering (recommended approach):

```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

If you use `cucumber.options` (older versions):

```bash
mvn test -Dcucumber.options="--tags @smoke"
```

### Run a single feature or scenario

Run a single feature file:

```bash
mvn test -Dcucumber.filter.features="src/test/resources/features/login.feature"
```

Run a single scenario by line number (Cucumber supports `:line` syntax):

```bash
mvn test -Dcucumber.filter.features="src/test/resources/features/login.feature:12"
```

### Parallel execution (TestNG)

If TestNG is configured for parallel execution in `testng.xml`:

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml
```

Ensure thread-safety for test context, WebDriver factories, and logger/attachments.

---

## Allure Reporting

Allure collects results (XML/json files, attachments) into `target/allure-results`. Two common approaches to view the report:

### Generate Allure report (Maven + Allure CLI)

1. Run tests so `target/allure-results` is populated:

```bash
mvn test
```

2. Generate report using Allure CLI:

```bash
allure serve target/allure-results
```

This command creates a temporary report and opens it in a browser.

3. Alternatively, generate static report files:

```bash
allure generate target/allure-results -o target/allure-report --clean
# then open with:
allure open target/allure-report
```

### Generate Allure report using Maven plugin

You may use `io.qameta.allure:allure-maven` plugin in `pom.xml`:

```bash
mvn allure:serve
# or
mvn allure:report
```

### Common troubleshooting for Allure attachments/screenshots

* Ensure that attachments (screenshots/logs) are added while a test is active. If Allure logs `Could not add attachment: no test is running`, it indicates the code attempted to attach outside the test lifecycle.
* Use TestNG listeners (e.g., `ITestListener`) to capture failure events (`onTestFailure`) and attach artifacts via `Allure.addAttachment(...)` or `Allure.getLifecycle().addAttachment(...)`.
* When running tests in parallel, ensure attachment code is thread-safe (use unique file names, thread-local contexts).
* Verify `target/allure-results` contains `.json` and attachment files after execution.

Example Java snippet to attach screenshot on failure (in `onTestFailure`):

```java
@Listener
public class AllureListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        try {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Failure screenshot", "image/png", "png", screenshot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## Test Design & Conventions

* **Feature files**: keep features at acceptance level — one business capability per feature file, scenarios focused, use Background for common steps.
* **Step definitions**: implement thin steps that delegate to page/service classes. Avoid heavy logic in steps.
* **Page Objects**: single responsibility — one page per class, exposing behavior methods returning domain objects or chainable actions.
* **Context / Test data**: use a thread-safe `TestContext` to share state within a scenario (e.g., tokens, userId).
* **Tags**: use a consistent tagging strategy:

  * `@smoke`, `@regression`, `@critical`, `@wip`
* **Expected status codes**:

  * Invalid user ID -> `400`
  * Invalid token -> `403`
    (Apply to your API tests if any — adjust to your API contract.)

---

## CI / GitHub Actions Example

Below is an example GitHub Actions workflow to run tests and upload the `allure-results` as an artifact. Paste this as `.github/workflows/ci.yml`.

```yaml
name: Java CI — Saucedemo

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache Maven packages
        uses: actions/cache@v4
        with:
          path: |
            ~/.m2/repository
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
          restore-keys: |
            ${{ runner.os }}-maven-

      - name: Build and run tests
        run: mvn -B clean test -Dcucumber.filter.tags="@smoke"

      - name: Upload Allure results
        uses: actions/upload-artifact@v4
        with:
          name: allure-results
          path: target/allure-results

  # Optional: add a job to generate and publish Allure report to GitHub Pages or a dedicated storage
```

Notes:

* For publishing Allure HTML to GitHub Pages you may add a separate job that installs Allure CLI, generates the report, and pushes to `gh-pages`. Alternatively, publish `target/allure-report` as a release artifact.

---

## Best Practices & Recommendations

1. **Thread safety**: If tests run in parallel, ensure WebDriver instances are isolated per thread (ThreadLocal). Make context objects thread-local.
2. **Retries & Flakiness**: Avoid hiding flaky tests — add controlled retries for known transient failures, but fix root causes. Use `RetryAnalyzer` in TestNG if necessary.
3. **Deterministic test data**: Use fixtures or test accounts to avoid brittle tests. Reset state between scenarios where applicable.
4. **Keep steps granular**: Step definitions should call high-level domain methods (e.g., `login.as(user)`) rather than manipulating low-level UI calls.
5. **Artifact retention**: Save Allure results and screenshots to artifacts in CI for post-mortem analysis.
6. **Logging**: Centralize logging (thread-safe), and attach logs to Allure on failures.

---

## Contribution

If you want to contribute:

1. Fork repository.
2. Create a feature branch: `feature/<short-description>`.
3. Add tests/implementations and run `mvn test`.
4. Open a pull request with a descriptive summary and mention any breaking changes.

Coding standards:

* Java style consistent with project conventions.
* Unit tests for helper classes where applicable.
* Update README and add migration notes if interface changes.

---

## License

Specify your chosen license here (e.g., MIT, Apache-2.0). Example:

```
NO License
```
