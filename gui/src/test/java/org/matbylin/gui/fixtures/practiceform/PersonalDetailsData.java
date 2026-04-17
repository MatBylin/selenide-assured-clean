package org.matbylin.gui.fixtures.practiceform;

import lombok.experimental.UtilityClass;
import org.matbylin.core.models.personalinfo.PersonalDetailsModel;
import org.matbylin.core.models.personalinfo.gender.Gender;

@UtilityClass
public class PersonalDetailsData {
    public static PersonalDetailsModel valid() {
        return PersonalDetailsModel.builder()
                .firstName("Matthew")
                .lastName("Hoffman")
                .email("matt_hoffman@email.com")
                .phone("9876543210")
                .dateOfBirth("12121980")
                .gender(Gender.MALE)
                .build();
    }
}
