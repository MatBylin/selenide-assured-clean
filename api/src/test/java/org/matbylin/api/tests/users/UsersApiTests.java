package org.matbylin.api.tests.users;

import org.apache.http.HttpStatus;
import org.matbylin.api.fixtures.user.UserInputDtoData;
import org.matbylin.api.service.users.UsersApi;
import org.matbylin.api.tests.BaseApiTest;
import org.matbylin.api.validators.ResponseValidator;
import org.matbylin.api.validators.user.UserCreatedDtoValidator;
import org.matbylin.api.validators.user.UserOutputDtoValidator;
import org.testng.annotations.Test;

public class UsersApiTests extends BaseApiTest {

    @Test
    void validateUserNameById() {
        var getResponse = new UsersApi().getUser("10");

        UserOutputDtoValidator.validate(getResponse)
                .hasStatusCode(HttpStatus.SC_OK)
                .hasExpectedName("Byron", "Fields");
    }

    @Test
    void validateNewUserCreation() {
        var userInput = UserInputDtoData.valid();
        var postResponse = new UsersApi().createUser(userInput);

        UserCreatedDtoValidator.validate(postResponse)
                .hasStatusCode(HttpStatus.SC_CREATED)
                .hasExpectedName(userInput);
    }

    @Test
    void validateUserDelete() {
        var userResponse = new UsersApi().deleteUser("2");

        ResponseValidator.validate(userResponse)
                .hasStatusCode(HttpStatus.SC_NO_CONTENT)
                .hasEmptyBody();
    }
}
