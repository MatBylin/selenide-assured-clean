# selenide-assured-clean

Multi-module test automation framework for **UI** (Selenide) and **API** (RestAssured), with shared configuration,
Allure reporting, and TestNG.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Technology Stack](#technology-stack)
- [Module Structure](#module-structure)
- [Architecture](#architecture)
- [Framework Patterns](#framework-patterns)
- [Profiles and Configuration](#profiles-and-configuration)
- [Running Tests](#running-tests)
- [Allure Reporting](#allure-reporting)
- [Extending the Framework](#extending-the-framework)

---

## Prerequisites

- **JDK 21** — set `JAVA_HOME` to your JDK installation
- **Maven 3.x** — `mvn` must be available on `PATH`
- **Chrome/Firefox** — Selenide auto-downloads the matching WebDriver
- **Selenium Grid** *(optional)* — only required for the `browser-remote` profile

---

## Quick Start

```bash
# 1. Clone and build (skips tests)
mvn clean install -DskipTests

# 2. Run the full SMOKE suite on the test environment in headless mode
mvn clean test -Dsuite=SMOKE -Ptest,browser-headless

# 3. View the Allure report
mvn allure:serve
```

---

## Technology Stack

| Technology         | Version | Purpose                                      |
|--------------------|---------|----------------------------------------------|
| **JDK**            | 21      | Java runtime                                 |
| **Maven**          | 3.x     | Build and dependency management              |
| **Selenide**       | 7.15.0  | UI automation wrapper over Selenium          |
| **RestAssured**    | 5.5.6   | API testing library                          |
| **TestNG**         | 7.12.0  | Test framework and suite runner              |
| **Allure**         | 2.33.0  | Test reporting with step-level detail        |
| **Lombok**         | 1.18.44 | Boilerplate reduction (`@Data`, `@Slf4j`)    |
| **Jackson**        | 2.21.2  | JSON serialization / deserialization         |
| **OWNER**          | 1.0.12  | Property-file based configuration management |
| **Hamcrest**       | 3.0     | Fluent assertion matchers                    |
| **AspectJ Weaver** | 1.9.24  | AOP support for Allure `@Step` annotations   |
| **SLF4J**          | 2.0.17  | Logging facade (SimpleLogger backend)        |

---

## Module Structure

```
selenide-assured-clean/
├── core/          # Shared config, environments, base classes, models
├── api/           # RestAssured API tests
├── gui/           # Selenide UI tests
└── suites/        # TestNG suite XML files
```

---

## Architecture

### Module dependency graph

```
        suites
          │
    ┌─────┴─────┐
   gui          api
    └─────┬─────┘
         core
```

### Key directories

```
core/
├── config/           # OWNER-based environment config (EnvironmentConfig, EnvironmentConfigProvider)
├── listeners/
│   ├── execution/    # TestExecutionListener — logs test lifecycle events
│   └── retry/        # RetryAnalyzer + RetryTransformer — configurable test retry
├── models/           # Shared data models
└── test/             # BaseTest — common @BeforeMethod / @AfterMethod hooks

gui/
├── browser/          # BrowserCapabilitiesFactory, Chrome/Firefox capability builders
├── core/
│   ├── components/   # Composite UI components (TopBar, Footer, Menu, …)
│   └── elements/     # Atomic UI elements (Button, Input, Text, Select, Checkbox, …)
├── pages/            # Page Objects (BasePage + concrete pages per feature)
├── wait/             # WaitManager — centralised waits
└── test/             # BaseGuiTest, test classes, fixtures, validators

api/
├── config/           # TargetApi enum, auth (AuthProvider, TokenProvider)
├── core/             # ApiRequest builder, ApiResponse wrapper, RestExecutor
├── dto/              # Data Transfer Objects (UserInputDto, UserOutputDto, …)
└── service/          # BaseApi + concrete service classes (UsersApi, …)
    test/             # BaseApiTest, test classes, factories, validators
```

---

## Framework Patterns

### Page Object Model with fluent chaining

`BasePage<T>` uses a generic self-referential type parameter so every page method returns the page itself, enabling
readable, chainable test steps:

``` java
    dashboardPage
        .goToFormAutomation()
        .fillPersonalDetails(personalDetails)
        .fillAddressDetails(addressDetails)
        .submitForm();
```

Each page must implement `validateLoaded()` (assertion that the correct page is displayed) and `getPageTitle()`. Both
are called automatically on navigation.

### UI Element abstraction

Raw `SelenideElement` usage is wrapped in typed element classes that provide consistent logging and wait-enhanced
assertions:

```
Element (interface)
  └── BaseElement<T>          # wraps SelenideElement, adds visibility/value checks
        ├── Button
        ├── Input
        ├── Text
        ├── Checkbox
        ├── Select
        ├── RadioButton
        └── Table / TableRow / TableCell
```

Using these wrappers instead of bare Selenide calls keeps pages readable, centralises wait logic, and produces
meaningful Allure step names.

### Composite Components

Groups of related elements are encapsulated into reusable `Component` objects (composition over inheritance):

```
Component (interface)
  └── BaseComponent<T>
        ├── TopBar
        ├── Footer
        ├── Product
        └── CartItem
```

A `Page` owns `Component`'s or/and `Elements`'s; a `Component` owns `Element`'s (and optionally nested `Component`'s).

### API layer

```
ApiRequest (fluent builder)  ──►  RestExecutor  ──►  ApiResponse<T>
                                      │
                              GET / POST / PUT / DELETE
```

Service classes (e.g. `UsersApi`) extend `BaseApi`, which injects auth headers automatically. DTOs are separated into
input (`UserInputDto`) and output (`UserOutputDto`, `UserCreatedDto`) to mirror real API contracts.

### Fluent validators

Both GUI and API layers use a validator pattern for assertions:

``` java
ResponseValidator.validate(response)
    .hasStatusCode(201)
    .hasNonEmptyBody();

UserCreatedDtoValidator.validate(response)
    .hasId()
    .hasName(expected.getName());
```

This keeps assertion logic out of test methods and makes failures report at a meaningful level.

### Test data builders

Test data is created through static factory methods, not inline construction:

``` java
UserInputDto user = UserInputDtoFactory.valid();
AccountDetails account = AccountDetailsData.valid();
AccountDetails noTerms = AccountDetailsData.termsNotAgreed();
```

Add new variants by adding static methods to the relevant factory — tests themselves should never hardcode data.

### Retry mechanism

`RetryAnalyzer` and `RetryTransformer` are registered as TestNG listeners in `BaseTest`. The number of retry attempts is
controlled by the `retry.count` system property (default: `0`):

```bash
mvn test -Dsuite=SMOKE -Dretry.count=2
```

Retries apply only to failed tests; skipped and passed tests are unaffected.

---

## Profiles and Configuration

### Environment properties

Configuration is loaded by the **OWNER** library from:

```
core/src/main/resources/environment/{dev,test,stage}.properties
```

The active file is selected by the `env` system property, which is set automatically by the Maven environment profiles.

| Key           | Meaning                                          |
|---------------|--------------------------------------------------|
| `environment` | Environment label (e.g. `dev`, `test`, `stage`). |
| `api.app.url` | Base URL for API tests.                          |
| `ui.app.url`  | Base URL for UI tests.                           |

Individual keys can be overridden at runtime: `-Dapi.app.url=http://localhost:8080`.

### Maven profiles — environments (root `pom.xml`)

| Profile | Purpose                               |
|---------|---------------------------------------|
| `dev`   | **Active by default.** Sets `env=dev` |
| `test`  | Sets `env=test`                       |
| `stage` | Sets `env=stage`                      |

### Maven profiles — browser (`gui/pom.xml`)

| Profile            | Effect                                        |
|--------------------|-----------------------------------------------|
| `browser-headless` | Sets `selenide.headless=true`                 |
| `browser-remote`   | Sets `selenide.remote` to a Selenium Grid URL |

Additional Surefire system properties for **gui**:

| Property            | Purpose                                   |
|---------------------|-------------------------------------------|
| `retry.count`       | Number of retry attempts for failed tests |
| `selenide.headless` | Run browser in headless mode              |
| `selenide.remote`   | Remote WebDriver / Grid URL               |

---

## Running Tests

Surefire is configured to resolve TestNG suite files from:

```
suites/xml/${suite}.xml
```

The `-Dsuite` flag is **required** and must match the file name without the `.xml` extension.

```bash
# SMOKE suite, default dev environment
mvn clean test -Dsuite=SMOKE

# SMOKE suite on the test environment, headless browser
mvn clean test -Dsuite=SMOKE -Ptest,browser-headless

# SMOKE suite on stage, headless, with 2 retries on failure
mvn clean test -Dsuite=SMOKE -Pstage,browser-headless -Dretry.count=2

# API tests only, test environment
mvn clean test -pl api -Dsuite=SMOKE -Ptest

# GUI tests only, remote Selenium Grid
mvn clean test -pl gui -Dsuite=SMOKE -Pbrowser-remote
```

---

## Allure Reporting

Allure results are written to `target/allure-results` at the repository root (configured in root `pom.xml`).

```bash
# Run tests and immediately open the HTML report in a browser
mvn clean test -Dsuite=SMOKE && mvn allure:serve

# Generate a static HTML report without serving
mvn allure:report
# Output: target/site/allure-maven-plugin/index.html
```

**AspectJ** weaver is attached at build time so that `@Step`-annotated methods appear as named steps inside the Allure
report. Add `@Step` to any public method in a Page, Component, or API service class to expose it in the report.

---

## Extending the Framework

### Add a new Page Object

1. Create a class in `gui/src/main/java/.../pages/<feature>/` extending `BasePage<YourPage>`.
2. Implement `validateLoaded()` (assert a unique element is visible) and `getPageTitle()`.
3. Declare page elements as fields using the typed element wrappers (`Text`, `Button`, etc.)
4. Declare page components (optional)
4. Return `self()` from every public action method.

``` java
public class CartPage extends BasePage<CartPage> {
    private static final String PAGE_TITLE = "Your Cart";

    @Getter
    private final Text pageTitle = new Text($(".title"));
    private final TopBar topBar = new TopBar($(".header_container"));
    private final Footer footer = new Footer($(".footer"));
    private final CartItems cartItems = new CartItems($("#cart_contents_container"));

    @Override
    @Step("Validating CartPage loaded")
    public void validateLoaded() {
        shouldHaveTitle(PAGE_TITLE);
        topBar.shouldBeVisible();
        cartItems.shouldBeVisible();
        footer.shouldBeVisible();
    }
    
    //further methods
}
```

### Add a new API service

1. Create a class in `api/src/main/java/.../service/<resource>/` extending `BaseApi`.
2. Add the endpoint to the `TargetApi` enum if it does not exist yet.
3. Use `restExecutor.get(...)` / `restExecutor.post(...)` etc., wrapping results in `ApiResponse<T>`.

``` java
public class UsersApi extends BaseApi {

    public UsersApi() {
        super(new AuthProvider());
    }

    @Step("GET user /users/{id}")
    public ApiResponse<UserOutputDto> getUser(String id) {
        var request = ApiRequest.builder()
                .targetApi(TargetApi.SERVICE)
                .pathParam("id", id)
                .path("/users/{id}")
                .build();

        return restExecutor.get(request, UserOutputDto.class);
    }
    
    // further implementation
}
```

### Add a new test data builder

Create a factory class next to the existing ones in the `factory` (gui) or `factory` (api) package:

``` java
public class ProductInputDtoFactory {

    public static ProductInputDto valid() {
        return ProductInputDto.builder()
                .name("Test Product")
                .price(9.99)
                .build();
    }
}
```

### Add a new TestNG suite

Create an XML file under `suites/xml/` following the same structure as `SMOKE.xml`, then run it
with `-Dsuite=<filename-without-extension>`.
