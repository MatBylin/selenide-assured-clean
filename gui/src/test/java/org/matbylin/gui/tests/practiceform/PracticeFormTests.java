package org.matbylin.gui.tests.practiceform;

import io.qameta.allure.Story;
import org.matbylin.gui.fixtures.practiceform.AccountDetailsData;
import org.matbylin.gui.fixtures.practiceform.AddressDetailsData;
import org.matbylin.gui.fixtures.practiceform.PersonalDetailsData;
import org.matbylin.gui.groups.Tag;
import org.matbylin.gui.tests.BaseGuiTest;
import org.matbylin.gui.validators.practiceform.PracticeFormValidator;
import org.testng.annotations.Test;

@Story("Form Automation Practice")
public class PracticeFormTests extends BaseGuiTest {
    @Test(groups = {Tag.SMOKE})
    void validateAutomationFormFilledSuccessfully() {
        var personalDetails = PersonalDetailsData.valid();
        var addressDetails = AddressDetailsData.valid();
        var accountDetails = AccountDetailsData.valid();

        var practiceFormPage = dashboardPage
                .goToFormAutomation()
                .fillPersonalDetails(personalDetails)
                .fillAddressDetails(addressDetails)
                .fillAccountDetails(accountDetails)
                .submitForm();

        PracticeFormValidator.assertThat(practiceFormPage)
                .formSubmittedSuccessfully(personalDetails);
    }

    @Test(groups = {Tag.SMOKE})
    void validateAutomationFormFilledUnsuccessfullyTermsNotAccepted() {
        var personalDetails = PersonalDetailsData.valid();
        var addressDetails = AddressDetailsData.valid();
        var accountDetails = AccountDetailsData.termsNotAgreed();

        var practiceFormPage = dashboardPage
                .goToFormAutomation()
                .fillPersonalDetails(personalDetails)
                .fillAddressDetails(addressDetails)
                .fillAccountDetails(accountDetails)
                .submitForm();

        PracticeFormValidator.assertThat(practiceFormPage)
                .formSubmittedTermsNotAccepted(personalDetails);
    }
}
