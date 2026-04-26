package org.matbylin.api.validators.user;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.api.core.ApiResponse;
import org.matbylin.api.dto.user.UserOutputDto;
import org.matbylin.api.validators.Validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class UserOutputDtoValidator extends Validator<UserOutputDtoValidator> {

    private final ApiResponse<UserOutputDto> response;

    private UserOutputDtoValidator(ApiResponse<UserOutputDto> response) {
        super(response);
        this.response = response;
    }

    public static UserOutputDtoValidator validate(ApiResponse<UserOutputDto> response) {
        return new UserOutputDtoValidator(response);
    }

    @Step("Validating user has expected data")
    public UserOutputDtoValidator hasExpectedName(String firstName, String lastName) {
        log.info("Validating UserOutputDto has expected: firstName:{}, lastName: {}", firstName, lastName);
        assertThat(response.getBody().getData().getFirstName(), equalTo(firstName));
        assertThat(response.getBody().getData().getLastName(), equalTo(lastName));
        return this;
    }
}
