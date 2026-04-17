package org.matbylin.core.models.personalinfo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.matbylin.core.models.personalinfo.gender.Gender;

@Getter
@ToString
@Builder(toBuilder = true)
public class PersonalDetailsModel {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private Gender gender;
}
