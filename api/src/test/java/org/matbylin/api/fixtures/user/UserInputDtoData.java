package org.matbylin.api.fixtures.user;

import lombok.experimental.UtilityClass;
import org.matbylin.api.dto.user.UserInputDto;
import org.matbylin.api.fixtures.BaseDtoData;

@UtilityClass
public class UserInputDtoData extends BaseDtoData {

    public static UserInputDto valid() {
        return UserInputDto.builder()
                .job(getFaker().job().position())
                .name(getFaker().name().fullName())
                .build();
    }
}
