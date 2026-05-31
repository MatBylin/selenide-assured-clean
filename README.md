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

# 2. Run the full SMOKE GUI suite on the test environment in headless mode
mvn clean test -Dsuite=SMOKE_GUI -Ptest,browser-headless

# 3. View the Allure report
mvn allure:serve
```

---

## Technology Stack

| Technology         | Version | Purpose                                      |
|--------------------|---------|----------------------------------------------|
| **JDK**            | 21      | Java runtime                                 |
| **Maven**          | 3.x     | Build and dependency management              |
| **Selenide**       | 7.16.0  | UI automation wrapper over Selenium          |
| **RestAssured**    | 6.0.0   | API testing library                          |
| **TestNG**         | 7.12.0  | Test framework and suite runner              |
| **Allure**         | 2.33.0  | Test reporting with step-level detail        |
| **Lombok**         | 1.18.44 | Boilerplate reduction (`@Data`, `@Slf4j`)    |
| **Jackson**        | 2.21.2  | JSON serialization / deserialization         |
| **OWNER**          | 1.0.12  | Property-file based configuration management |
| **Hamcrest**       | 3.0     | Fluent assertion matchers                    |
| **AspectJ Weaver** | 1.9.24  | AOP support for Allure `@Step` annotations   |
| **SLF4J**          | 2.0.17  | Logging facade (SimpleLogger backend)        |
| **Pdfbox**         | 3.0.7   | Pdf reader                                   |

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
├── faker/            # Faker library global provider
├── groups/           # Test groups/tags
├── listeners/
│   ├── execution/    # TestExecutionListener — logs test lifecycle events
│   └── retry/        # RetryAnalyzer + RetryTransformer — configurable test retry
├── models/           # Shared data models
├── pdf/              # Pdf filer reader
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
    .hasStatusCode(HttpStatus.SC_CREATED)
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
# SMOKE GUI suite, default dev environment
mvn clean test -Dsuite=SMOKE_GUI

# SMOKE GUI suite on the test environment, headless browser
mvn clean test -Dsuite=SMOKE_GUI -Ptest,browser-headless

# SMOKE GUI suite on stage, headless, with 2 retries on failure
mvn clean test -Dsuite=SMOKE_GUI -Pstage,browser-headless -Dretry.count=2

# API tests only, test environment
mvn clean test -pl api -Dsuite=SMOKE_API -Ptest

```

---

## Allure Reporting

Allure results are written to `target/allure-results` at the repository root (configured in root `pom.xml`).

```bash
# Run tests and immediately open the HTML report in a browser
mvn clean test -Dsuite=SMOKE_GUI && mvn allure:serve

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
    
    //further implementation
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



public String loginAndGetToken(String keycloakUrl, String user, String pass) {
    open(keycloakUrl + "/admin/master/console/");

    $("#username").shouldBe(visible).setValue(user);
    $("#password").setValue(pass);
    $("#kc-login").click();

    $(".pf-v5-c-page__header").shouldBe(visible); // wait for console header

    return (String) executeJavaScript("""
        for (const key of Object.keys(sessionStorage)) {
            if (key.includes('token')) {
                const v = JSON.parse(sessionStorage.getItem(key));
                if (v?.access_token) return v.access_token;
            }
        }
        return null;
    """);


    String sessionDump = (String) executeJavaScript("""
    const result = {};
    for (const key of Object.keys(sessionStorage)) {
        result[key] = sessionStorage.getItem(key);
    }
    return JSON.stringify(result);
""");
System.out.println("SESSION: " + sessionDump);

// Dump all localStorage keys + values  
String localDump = (String) executeJavaScript("""
    const result = {};
    for (const key of Object.keys(localStorage)) {
        result[key] = localStorage.getItem(key);
    }
    return JSON.stringify(result);
""");
System.out.println("LOCAL: " + localDump);


Capture token via ResponseFilter
javaimport com.browserup.bup.util.HttpMessageInfo;
import com.browserup.bup.filters.ResponseFilter;
import com.browserup.harreader.model.HarPostDataParam;
import io.netty.handler.codec.http.HttpResponse;
import com.browserup.bup.util.HttpMessageContents;

import java.util.concurrent.atomic.AtomicReference;

public class KeycloakTokenCapture {

    private final AtomicReference<String> capturedToken = new AtomicReference<>();

    public void registerTokenFilter() {
        proxy.addResponseFilter((response, contents, messageInfo) -> {
            if (messageInfo.getOriginalUrl().contains("/openid-connect/token")) {
                String body = contents.getTextContents();
                String token = extractField(body, "access_token");
                capturedToken.set(token);
            }
        });
    }

    public String getToken() {
        return capturedToken.get();
    }

    private String extractField(String json, String field) {
        String key = "\"" + field + "\":\"";
        int start = json.indexOf(key) + key.length();
        int end   = json.indexOf("\"", start);
        return (start > key.length() - 1) ? json.substring(start, end) : null;
    }
}

Wire it into your test
javapublic class LoginTest {

    private KeycloakTokenCapture tokenCapture = new KeycloakTokenCapture();
    private String accessToken;

    @BeforeClass
    public void setUp() {
        // Register filter BEFORE opening browser
        tokenCapture.registerTokenFilter();

        // Normal Selenide login
        open("https://keycloak.example.com/admin/master/console/");
        $("#username").setValue("admin");
        $("#password").setValue("password");
        $("#kc-login").click();
        $(".pf-v5-c-page__header").shouldBe(visible);

        // Token was captured during login redirect
        accessToken = tokenCapture.getToken();
        System.out.println("Access token: " + accessToken);
    }
}



///


public class LoginTest {

    private String accessToken;

    @BeforeClass
    public void setUp() {
        open("https://keycloak.example.com/admin/master/console/");

        // Get underlying ChromeDriver from Selenide
        ChromeDriver driver = (ChromeDriver) WebDriverRunner.getWebDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        AtomicReference<String> capturedToken = new AtomicReference<>();

        devTools.addListener(Network.responseReceived(), (ResponseReceived event) -> {
            if (event.getResponse().getUrl().contains("/openid-connect/token")) {
                devTools.send(Network.getResponseBody(event.getRequestId()))
                    .ifPresent(body -> capturedToken.set(extractField(body.getBody(), "access_token")));
            }
        });

        // Login
        $("#username").setValue("admin");
        $("#password").setValue("password");
        $("#kc-login").click();
        $(".pf-v5-c-page__header").shouldBe(visible);

        accessToken = capturedToken.get();
        System.out.println("Token: " + accessToken);
    }

    private String extractField(String json, String field) {
        String key = "\"" + field + "\":\"";
        int start = json.indexOf(key) + key.length();
        int end   = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}


///
        <dependency>
            <groupId>com.github.valfirst.browserup-proxy</groupId>
            <artifactId>browserup-proxy-core</artifactId>
            <version>3.3.0</version>
            <exclusions>
                <exclusion>
                    <groupId>org.seleniumhq.selenium</groupId>
                    <artifactId>selenium-api</artifactId>
                </exclusion>
            </exclusions>
        </dependency>


proxy.addResponseFilter((response, contents, messageInfo) -> {
    if (messageInfo.getOriginalUrl().contains("/openid-connect/token")) {
        capturedToken.set(extractField(contents.getTextContents(), "access_token"));
    }
});



        <dependency>
            <groupId>com.github.valfirst.browserup-proxy</groupId>
            <artifactId>browserup-proxy-core</artifactId>
            <version>3.3.0</version>
            <exclusions>
                <exclusion>
                    <groupId>org.seleniumhq.selenium</groupId>
                    <artifactId>selenium-api</artifactId>
                </exclusion>
            </exclusions>
        </dependency>


                <dependency>
            <groupId>com.github.valfirst.browserup-proxy</groupId>
            <artifactId>browserup-proxy-core</artifactId>
            <version>3.3.0</version>
            <exclusions>
                <exclusion>
                    <groupId>org.seleniumhq.selenium</groupId>
                    <artifactId>selenium-api</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

///
TOKEN
package org.matbylin.api.config.auth;

import io.restassured.http.ContentType;
import org.matbylin.core.config.EnvironmentConfigProvider;

import java.time.Instant;

import static io.restassured.RestAssured.given;

public class TokenProvider implements AuthProvider {

    private static final int EXPIRY_BUFFER_SECONDS = 30;
    private static final Object LOCK = new Object();

    private static String cachedToken;
    private static Instant tokenExpiresAt = Instant.MIN;

    @Override
    public String getToken() {
        synchronized (LOCK) {
            if (isTokenExpired()) {
                refreshToken();
            }
            return cachedToken;
        }
    }

    private static boolean isTokenExpired() {
        return Instant.now().plusSeconds(EXPIRY_BUFFER_SECONDS).isAfter(tokenExpiresAt);
    }

    private static void refreshToken() {
        var config = EnvironmentConfigProvider.get();
        String tokenUrl = config.keycloakUrl()
                + "/realms/" + config.keycloakRealm()
                + "/protocol/openid-connect/token";

        KeycloakTokenResponse response = given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", config.keycloakClientId())
                .formParam("client_secret", config.keycloakClientSecret())
                .post(tokenUrl)
                .then()
                .statusCode(200)
                .extract()
                .as(KeycloakTokenResponse.class);

        cachedToken = response.getAccessToken();
        tokenExpiresAt = Instant.now().plusSeconds(response.getExpiresIn());
    }
}


//

    public static String fromClasspath(String classpathPath) {
        try (InputStream stream = JsonFileLoader.class.getClassLoader().getResourceAsStream(classpathPath)) {
            if (stream == null) {
                throw new IllegalArgumentException("JSON file not found on classpath: " + classpathPath);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read JSON file: " + classpathPath, e);
        }
    }

    public static String fromFile(String filePath) {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read JSON file: " + filePath, e);
        }
    }
}



///


package org.matbylin.core.listeners.xray;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.TmsLink;
import lombok.extern.slf4j.Slf4j;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class XrayExporterListener implements IReporter {

    private static final String PROP_TEST_EXECUTION_KEY = "xray.testExecutionKey";
    private static final String PROP_JIRA_BASE_URL = "xray.jira.baseUrl";
    private static final String PROP_JIRA_TOKEN = "xray.jira.token";
    private static final String XRAY_IMPORT_ENDPOINT = "/rest/raven/1.0/import/execution";

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        String testExecutionKey = System.getProperty(PROP_TEST_EXECUTION_KEY);
        if (testExecutionKey == null || testExecutionKey.isBlank()) {
            log.info("Xray export skipped — '{}' not provided", PROP_TEST_EXECUTION_KEY);
            return;
        }

        String jiraBaseUrl = System.getProperty(PROP_JIRA_BASE_URL);
        String jiraToken = System.getProperty(PROP_JIRA_TOKEN);

        if (jiraBaseUrl == null || jiraBaseUrl.isBlank()) {
            log.error("Xray export failed — '{}' not provided", PROP_JIRA_BASE_URL);
            return;
        }
        if (jiraToken == null || jiraToken.isBlank()) {
            log.error("Xray export failed — '{}' not provided", PROP_JIRA_TOKEN);
            return;
        }

        List<Map<String, String>> tests = collectResults(suites);
        if (tests.isEmpty()) {
            log.warn("Xray export skipped — no tests with @TmsLink found");
            return;
        }

        exportToXray(jiraBaseUrl, jiraToken, testExecutionKey, tests);
    }

    private List<Map<String, String>> collectResults(List<ISuite> suites) {
        List<Map<String, String>> results = new ArrayList<>();
        for (ISuite suite : suites) {
            suite.getResults().values().forEach(suiteResult -> {
                var ctx = suiteResult.getTestContext();
                collectFromSet(ctx.getPassedTests().getAllResults(), "PASSED", results);
                collectFromSet(ctx.getFailedTests().getAllResults(), "FAILED", results);
                collectFromSet(ctx.getSkippedTests().getAllResults(), "ABORTED", results);
            });
        }
        return results;
    }

    private void collectFromSet(Set<ITestResult> testResults, String status, List<Map<String, String>> collected) {
        testResults.forEach(result -> {
            var method = result.getMethod().getConstructorOrMethod().getMethod();
            var tmsLink = method.getAnnotation(TmsLink.class);
            if (tmsLink != null && !tmsLink.value().isBlank()) {
                collected.add(Map.of("testKey", tmsLink.value(), "status", status));
                log.debug("Collected: {} -> {}", tmsLink.value(), status);
            }
        });
    }

    private void exportToXray(String jiraBaseUrl, String jiraToken, String testExecutionKey, List<Map<String, String>> tests) {
        try {
            var payload = Map.of(
                    "testExecutionKey", testExecutionKey,
                    "tests", tests
            );

            var json = new ObjectMapper().writeValueAsString(payload);

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(jiraBaseUrl + XRAY_IMPORT_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + jiraToken)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            var response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                log.info("Xray export successful — TestExecution: {}, tests exported: {}", testExecutionKey, tests.size());
            } else {
                log.error("Xray export failed — HTTP {}: {}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            log.error("Xray export failed — {}", e.getMessage(), e);
        }
    }
}


////

package org.matbylin.core.db;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:environment/${env}.properties",
})
public interface DatabaseConfig extends Config {

    @Key("db.host")
    @DefaultValue("localhost")
    String host();

    @Key("db.port")
    @DefaultValue("5432")
    int port();

    @Key("db.name")
    String name();

    @Key("db.username")
    String username();

    @Key("db.password")
    String password();
}

////
package org.matbylin.core.db;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigFactory;
import org.matbylin.core.config.Environment;

import java.util.Map;

@UtilityClass
public class DatabaseConfigProvider {

    private static final String ENV_PROPERTY = "env";
    private static final DatabaseConfig CONFIG = ConfigFactory.create(DatabaseConfig.class, Map.of(ENV_PROPERTY, getEnvironment()));

    public static DatabaseConfig get() {
        return CONFIG;
    }

    private static Environment getEnvironment() {
        return Environment.from(System.getProperty(ENV_PROPERTY));
    }
}
////
package org.matbylin.core.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;

import javax.sql.DataSource;

@UtilityClass
public class DatabaseConnectionProvider {

    private static final HikariDataSource DATA_SOURCE = buildDataSource();

    public static DataSource get() {
        return DATA_SOURCE;
    }

    private static HikariDataSource buildDataSource() {
        DatabaseConfig config = DatabaseConfigProvider.get();
        HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl("jdbc:postgresql://%s:%d/%s".formatted(config.host(), config.port(), config.name()));
        hikari.setUsername(config.username());
        hikari.setPassword(config.password());
        hikari.setMaximumPoolSize(5);
        hikari.setConnectionTimeout(30_000);
        return new HikariDataSource(hikari);
    }
}
////

package org.matbylin.core.db;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

@Slf4j
@UtilityClass
public class DbClient {

    public static Optional<DbRow> selectOne(SqlScript script, Object... params) {
        List<DbRow> results = select(script, params);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    public static List<DbRow> select(SqlScript script, Object... params) {
        String sql = readScript(script);
        log.debug("Executing script [{}]: {}", script.name(), sql);
        try (Connection connection = DatabaseConnectionProvider.get().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return mapResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Query failed [%s]".formatted(script.name()), e);
        }
    }

    private static String readScript(SqlScript script) {
        try (InputStream is = DbClient.class.getClassLoader().getResourceAsStream(script.getPath())) {
            if (is == null) {
                throw new RuntimeException("SQL script not found on classpath: %s".formatted(script.getPath()));
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SQL script: %s".formatted(script.getPath()), e);
        }
    }

    private static List<DbRow> mapResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData meta = resultSet.getMetaData();
        int columnCount = meta.getColumnCount();
        List<DbRow> rows = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(meta.getColumnName(i), resultSet.getObject(i));
            }
            rows.add(new DbRow(row));
        }
        return rows;
    }
}

///

package org.matbylin.core.db;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class DbRow {

    private final Map<String, Object> data;

    public String getString(String column) {
        return get(column, String.class);
    }

    public Integer getInt(String column) {
        return get(column, Integer.class);
    }

    public Long getLong(String column) {
        return get(column, Long.class);
    }

    public Boolean getBoolean(String column) {
        return get(column, Boolean.class);
    }

    public <T> T get(String column, Class<T> type) {
        Object value = data.get(column);
        if (value == null) return null;
        return type.cast(value);
    }

    public boolean hasColumn(String column) {
        return data.containsKey(column);
    }

    public Map<String, Object> getRaw() {
        return data;
    }
}
////

        <postgresql-version>42.7.4</postgresql-version>
        <hikaricp-version>5.1.0</hikaricp-version>

                    </dependency>
            <dependency>
                <groupId>org.postgresql</groupId>
                <artifactId>postgresql</artifactId>
                <version>${postgresql-version}</version>
            </dependency>
            <dependency>
                <groupId>com.zaxxer</groupId>
                <artifactId>HikariCP</artifactId>
                <version>${hikaricp-version}</version>
            </dependency>

            ///

            ///

            
@Getter
@RequiredArgsConstructor
public enum SqlScript {

    FIND_USER_BY_ID("sql/users/find_user_by_id.sql"),
    FIND_USERS_BY_STATUS("sql/users/find_users_by_status.sql"),
    FIND_ALL_USERS("sql/users/find_all_users.sql");

    private final String path;
}

# without Xray export (skips silently)
mvn test -Dsuite=SMOKE_API

# with Xray export
mvn test -Dsuite=SMOKE_API \
  -Dxray.testExecutionKey=PROJ-666 \
  -Dxray.jira.baseUrl=https://your-jira.com \
  -Dxray.jira.token=your-token
Jenkins
withCredentials([string(credentialsId: 'jira-token', variable: 'JIRA_TOKEN')]) {
    def xrayArgs = params.XRAY_TEST_EXECUTION_KEY?.trim()
        ? "-Dxray.testExecutionKey=${params.XRAY_TEST_EXECUTION_KEY} -Dxray.jira.baseUrl=${params.JIRA_BASE_URL} -Dxray.jira.token=${JIRA_TOKEN}"
        : ""
    sh "mvn test -Dsuite=SMOKE_API ${xrayArgs}"
}
//

if [ "$exportResultToXray" = "true" ]; then
    echo "XRAY_EXTRA_PARAMS=-Dxray.testExecutionKey=${xrayTestExecutionKey} -Dxray.jira.url=${XRAY_URL}" > xray.properties
else
    echo "XRAY_EXTRA_PARAMS=" > xray.properties
fi




////

MVN_PARAMS="clean compile test -Dsuite=MOJA_SUITA"

if [ "$exportResultToXray" = "true" ]; then
    MVN_PARAMS="$MVN_PARAMS -Dxray.testExecutionKey=${xrayTestExecutionKey}"
    MVN_PARAMS="$MVN_PARAMS -Dxray.jira.url=${XRAY_URL}"
fi

mvn $MVN_PARAMS


///

bashMVN_PARAMS="clean compile test -Dsuite=MOJA_SUITA"

if [ "$exportResultToXray" = "true" ]; then
    MVN_PARAMS="$MVN_PARAMS -Dxray.testExecutionKey=${xrayTestExecutionKey}"
    MVN_PARAMS="$MVN_PARAMS -Dxray.jira.url=${XRAY_URL}"
fi

mvn $MVN_PARAMS



////
public class CachedTokenService {

    private final KeycloakTokenProvider tokenProvider;
    private final TokenCache cache;

    public CachedTokenService(KeycloakTokenProvider tokenProvider, TokenCache cache) {
        this.tokenProvider = tokenProvider;
        this.cache = cache;
    }

    public String getToken() {
        String cached = cache.get();
        if (cached != null) {
            return cached;
        }
        TokenResponse fresh = tokenProvider.fetchToken();
        cache.put(fresh.accessToken(), fresh.expiresInSeconds());
        return fresh.accessToken();
    }

    public void invalidate() {
        cache.invalidate();
    }
}
////
public class TokenCache {

    private static final int EXPIRY_BUFFER_SECONDS = 30;

    private String token;
    private Instant expiresAt;

    public synchronized String get() {
        if (token != null && expiresAt != null && Instant.now().isBefore(expiresAt)) {
            return token;
        }
        return null;
    }

    public synchronized void put(String token, int expiresInSeconds) {
        this.token = token;
        this.expiresAt = Instant.now().plusSeconds(expiresInSeconds - EXPIRY_BUFFER_SECONDS);
    }

    public synchronized void invalidate() {
        this.token = null;
        this.expiresAt = null;
    }
}
////
private static final CachedTokenService tokenService = new CachedTokenService(
        new KeycloakTokenProvider("https://keycloak.example.com", "my-realm", "client-id", "secret"),
        new TokenCache()
);

@AfterMethod(alwaysRun = true)
public void invalidateToken() {
    tokenService.invalidate();
}

////

public class ApiPoller {
    public static <T> ApiResponse<T> pollUntil(
        Supplier<ApiResponse<T>> requestSupplier,
        Predicate<ApiResponse<T>> condition,
        Duration timeout,
        Duration interval
    ) {
        AtomicReference<ApiResponse<T>> ref = new AtomicReference<>();
        await().atMost(timeout).pollInterval(interval)
               .until(() -> { ref.set(requestSupplier.get()); return condition.test(ref.get()); });
        return ref.get();
    }
}
Service method uses it cleanly:

// ElasticApi.java
@Step("Wait for log matching: {query}")
public ApiResponse<LogResultDto> waitForLog(String query, Duration timeout) {
    ApiRequest request = ApiRequest.builder()...queryParam("q", query).build();
    return ApiPoller.pollUntil(
        () -> restExecutor.get(request, LogResultDto.class),
        r -> r.getStatusCode() == 200 && r.getBody() != null,
        timeout,
        Duration.ofSeconds(3)
    );
}
///

try {
    await().atMost(timeout).pollInterval(interval).alias(alias)
           .until(() -> { ref.set(requestSupplier.get()); return condition.test(ref.get()); });
} catch (ConditionTimeoutException e) {
    throw new AssertionError("Polling condition not met: " + alias, e);
}

///
public ApiResponse<LogResultDto> waitForLog(String query, Duration timeout) {
    ApiRequest request = ApiRequest.builder()...queryParam("q", query).build();
    AtomicReference<ApiResponse<LogResultDto>> ref = new AtomicReference<>();

    ApiPoller.pollUntilAsserted(
        () -> {
            ref.set(restExecutor.get(request, LogResultDto.class));
            assertThat(ref.get().getStatusCode(), equalTo(200));
            assertThat(ref.get().getBody(), notNullValue());
        },
        timeout,
        Duration.ofSeconds(3),
        "Log matching query: " + query
    );

    return ref.get();
}


///
echo "=== Env ==="
env | grep -iE 'proxy|java'
echo "=== DNS ==="
nslookup your-es-host
echo "=== TCP ==="
nc -zv your-es-host 9200
echo "=== HTTP ==="
curl -v --max-time 15 https://your-es-host:9200 \
     -H "Authorization: ApiKey $ES_API_KEY"





// XPath locators do NOT work inside shadow DOM — use CSS selectors only.
public abstract class BaseShadowComponent<T extends BaseShadowComponent<T>> extends BaseComponent<T> {

    protected BaseShadowComponent(SelenideElement shadowHost) {
        super(shadowHost);
    }

    protected SelenideElement getShadowRoot() {
        return Selenide.$((WebElement) getRoot().getShadowRoot());
    }

    @Override
    protected SelenideElement $(String cssSelector) {
        return getShadowRoot().$(cssSelector);
    }

    @Override
    protected ElementsCollection $$(String cssSelector) {
        return getShadowRoot().$$(cssSelector);
    }

    @Override
    protected SelenideElement $(By locator) {
        return getShadowRoot().$(locator);
    }

    @Override
    protected ElementsCollection $$(By locator) {
        return getShadowRoot().$$(locator);
    }
}




public class MyWebComponent extends BaseShadowComponent<MyWebComponent> {

    // shadowHost is the <custom-element> that has a shadow root attached
    public MyWebComponent(SelenideElement shadowHost) {
        super(shadowHost);
    }

    public Text getLabel() {
        return new Text($(".label"));       // resolves inside shadow root
    }

    public Button getSubmitButton() {
        return new Button($("#submit-btn")); // resolves inside shadow root
    }
}
