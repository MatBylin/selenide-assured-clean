package org.matbylin.api.tests;

import org.matbylin.core.test.BaseTest;
import org.testng.annotations.BeforeSuite;

public abstract class BaseApiTest extends BaseTest {

    @BeforeSuite
    static void setupApiClient() {
        // future set up
    }
}
