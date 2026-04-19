package org.matbylin.gui.fixtures.practiceform;

import lombok.experimental.UtilityClass;
import org.matbylin.core.faker.FakerProvider;
import org.matbylin.core.models.personalinfo.AccountDetailsModel;

@UtilityClass
public class AccountDetailsData {
    public static AccountDetailsModel valid() {
        return AccountDetailsModel.builder()
                .password(FakerProvider.get().credentials().password())
                .agreeTerms(true)
                .build();
    }

    public static AccountDetailsModel termsNotAgreed() {
        return AccountDetailsModel.builder()
                .password(FakerProvider.get().credentials().password())
                .agreeTerms(false)
                .build();
    }
}
