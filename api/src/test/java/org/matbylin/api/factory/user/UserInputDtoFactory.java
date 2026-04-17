package org.matbylin.api.factory.user;

import lombok.experimental.UtilityClass;
import org.matbylin.api.dto.user.UserInputDto;

@UtilityClass
public class UserInputDtoFactory {
    public static UserInputDto valid() {
        return UserInputDto.builder()
                .job("Waiter")
                .name("Alexander Jolie")
                .build();
    }
}
