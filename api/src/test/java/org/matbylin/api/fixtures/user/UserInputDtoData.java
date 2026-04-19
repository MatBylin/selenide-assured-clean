package org.matbylin.api.fixtures.user;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import org.matbylin.api.dto.user.UserInputDto;
import org.matbylin.core.faker.FakerProvider;

@UtilityClass
public class UserInputDtoData {
    private static final Faker FAKER = FakerProvider.get();

    public static UserInputDto valid() {
        return UserInputDto.builder()
                .job(FAKER.job().position())
                .name(FAKER.name().fullName())
                .build();
    }
}
