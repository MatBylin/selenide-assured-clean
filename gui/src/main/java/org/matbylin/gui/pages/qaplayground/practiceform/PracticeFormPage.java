package org.matbylin.gui.pages.qaplayground.practiceform;

import io.qameta.allure.Step;
import lombok.Getter;
import org.matbylin.core.models.personalinfo.AccountDetailsModel;
import org.matbylin.core.models.personalinfo.AddressDetailsModel;
import org.matbylin.core.models.personalinfo.PersonalDetailsModel;
import org.matbylin.gui.core.components.generic.Footer;
import org.matbylin.gui.core.components.generic.TopBar;
import org.matbylin.gui.core.elements.Text;
import org.matbylin.gui.pages.BasePage;
import org.matbylin.gui.pages.qaplayground.practiceform.components.SubmittedForm;
import org.matbylin.gui.pages.qaplayground.practiceform.components.UserRegistrationForm;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class PracticeFormPage extends BasePage<PracticeFormPage> {
    private static final String PAGE_TITLE = "Form Automation Practice";

    private final Text pageTitle = new Text($("section h1"));
    private final TopBar topBar = new TopBar($("header"));
    private final Footer footer = new Footer($("footer"));
    private final UserRegistrationForm userRegistrationForm = new UserRegistrationForm($("#userRegistrationForm"));
    private final SubmittedForm submittedForm = new SubmittedForm($("#formSuccessMsg"));

    @Override
    @Step("Validating Form Automation page loaded")
    public void validateLoaded() {
        shouldHaveTitle(PAGE_TITLE);
    }

    @Step("Filling form with personal details: {0}")
    public PracticeFormPage fillPersonalDetails(PersonalDetailsModel personalDetails) {
        userRegistrationForm.fillPersonalDetails(personalDetails);
        return this;
    }

    @Step("Filling form with address details: {0}")
    public PracticeFormPage fillAddressDetails(AddressDetailsModel address) {
        userRegistrationForm.fillAddressDetails(address);
        return this;
    }

    @Step("Filling form with accountDetails: {0}")
    public PracticeFormPage fillAccountDetails(AccountDetailsModel accountDetails) {
        userRegistrationForm.fillAccountDetails(accountDetails);
        return this;
    }

    @Step("Submitting form")
    public PracticeFormPage submitForm() {
        return userRegistrationForm.submitForm();
    }
}
