package org.matbylin.api.validators.user;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.api.core.ApiResponse;
import org.matbylin.api.dto.user.UserCreatedDto;
import org.matbylin.api.dto.user.UserInputDto;
import org.matbylin.api.validators.Validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class UserCreatedDtoValidator extends Validator<UserCreatedDtoValidator> {

    private final ApiResponse<UserCreatedDto> response;

    private UserCreatedDtoValidator(ApiResponse<UserCreatedDto> response) {
        super(response);
        this.response = response;
    }

    public static UserCreatedDtoValidator validate(ApiResponse<UserCreatedDto> response) {
        return new UserCreatedDtoValidator(response);
    }

    @Step("Validating created user has expected data")
    public UserCreatedDtoValidator hasExpectedInputData(UserInputDto userInputDto) {
        log.info("Validating UserCreatedDto has expected -> userInputDto {}", userInputDto);
        assertThat(response.getBody().getName(), equalTo(userInputDto.getName()));
        assertThat(response.getBody().getJob(), equalTo(userInputDto.getJob()));
        assertThat(response.getBody().getId(), notNullValue());
        return this;
    }
}
