package org.matbylin.gui.fixtures.practiceform;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import org.matbylin.core.faker.FakerProvider;
import org.matbylin.core.models.personalinfo.PersonalDetailsModel;
import org.matbylin.core.models.personalinfo.gender.Gender;

@UtilityClass
public class PersonalDetailsData {
    private static final Faker FAKER = FakerProvider.get();

    public static PersonalDetailsModel valid() {
        return PersonalDetailsModel.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .email(FAKER.internet().emailAddress())
                .phone(FAKER.phoneNumber().subscriberNumber(10))
                .dateOfBirth(FakerProvider.get().timeAndDate().birthday("ddMMYYYY"))
                .gender(Gender.MALE)
                .build();
    }
}
