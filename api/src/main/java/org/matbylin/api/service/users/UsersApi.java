package org.matbylin.api.service.users;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.api.config.TargetApi;
import org.matbylin.api.config.auth.TokenProvider;
import org.matbylin.api.core.ApiRequest;
import org.matbylin.api.core.ApiResponse;
import org.matbylin.api.dto.user.UserCreatedDto;
import org.matbylin.api.dto.user.UserInputDto;
import org.matbylin.api.dto.user.UserOutputDto;
import org.matbylin.api.service.BaseApi;

@Slf4j
public class UsersApi extends BaseApi {

    public UsersApi() {
        super(new TokenProvider());
    }

    @Step("GET user /users/{id}")
    public ApiResponse<UserOutputDto> getUser(String id) {
        log.info("GET request to /users/{}", id);
        var request = ApiRequest.builder()
                .targetApi(TargetApi.REQRES)
                .pathParam("id", id)
                .path("/users/{id}")
                .build();

        return restExecutor.get(request, UserOutputDto.class);
    }

    @Step("POST user /users")
    public ApiResponse<UserCreatedDto> createUser(UserInputDto user) {
        log.info("POST request to /users");
        var request = ApiRequest.builder()
                .targetApi(TargetApi.REQRES)
                .body(user)
                .path("/users")
                .build();

        return restExecutor.post(request, UserCreatedDto.class);
    }

    @Step("DELETE user /users")
    public ApiResponse<Void> deleteUser(String id) {
        log.info("DELETE request to /users/{}", id);
        var request = ApiRequest.builder()
                .targetApi(TargetApi.REQRES)
                .pathParam("id", id)
                .path("/users/{id}")
                .build();

        return restExecutor.delete(request, Void.class);
    }
}
