package org.matbylin.api.validators;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.api.core.ApiResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
public abstract class Validator<T extends Validator<T>> {

    protected final ApiResponse<?> apiResponse;

    protected Validator(ApiResponse<?> apiResponse) {
        this.apiResponse = apiResponse;
    }

    @Step("Validating status code is {expected}")
    public T hasStatusCode(int expected) {
        log.info("Validating expected status code: {}", expected);
        assertThat("Validating expected status code", apiResponse.getStatusCode(), equalTo(expected));
        return self();
    }

    @Step("Validating response has body")
    public T hasBody() {
        log.info("Validating response has no empty body");
        var raw = apiResponse.getRawBody();
        assertThat("Raw body should be non null", raw, notNullValue());
        assertThat("Deserialized body should be non null", apiResponse.getBody(), notNullValue());
        return self();
    }

    @Step("Validating response has no body")
    public T hasEmptyBody() {
        log.info("Validating raw body is blank and deserialized body is null");
        var raw = apiResponse.getRawBody();
        assertThat("Raw body should be blank", raw, anyOf(nullValue(), emptyOrNullString()));
        assertThat("Deserialized body should be null", apiResponse.getBody(), nullValue());
        return self();
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
}
