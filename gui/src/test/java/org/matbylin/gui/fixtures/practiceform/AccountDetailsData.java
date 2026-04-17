package org.matbylin.gui.fixtures.practiceform;

import lombok.experimental.UtilityClass;
import org.matbylin.core.models.personalinfo.AccountDetailsModel;

@UtilityClass
public class AccountDetailsData {
    public static AccountDetailsModel valid() {
        return AccountDetailsModel.builder()
                .password("secret")
                .agreeTerms(true)
                .build();
    }

    public static AccountDetailsModel termsNotAgreed() {
        return AccountDetailsModel.builder()
                .password("secret")
                .agreeTerms(false)
                .build();
    }
}
