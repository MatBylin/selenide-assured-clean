package org.matbylin.gui.validators.practiceform;

import io.qameta.allure.Step;
import org.matbylin.core.models.personalinfo.PersonalDetailsModel;
import org.matbylin.gui.pages.qaplayground.practiceform.PracticeFormPage;

public class PracticeFormValidator {
    private final PracticeFormPage page;

    private PracticeFormValidator(PracticeFormPage page) {
        this.page = page;
    }

    public static PracticeFormValidator assertThat(PracticeFormPage page) {
        return new PracticeFormValidator(page);
    }

    @Step("Validating that form was successfully submitted")
    public PracticeFormValidator formSubmittedSuccessfully(PersonalDetailsModel personalDetails) {
        page.getUserRegistrationForm().shouldNotBeVisible();
        var formSubmitted = page.getSubmittedForm().shouldBeVisible();
        formSubmitted.getTitleText()
                .shouldHaveExactText("Form Submitted Successfully!");
        formSubmitted.getMessageText()
                .shouldHaveExactText("Hi %s %s, your details have been recorded.".formatted(personalDetails.getFirstName(), personalDetails.getLastName()));
        formSubmitted.getFillAgainButton().shouldBeVisible();
        return this;
    }

    @Step("Validating that form was not submitted - terms not accepted")
    public PracticeFormValidator formSubmittedTermsNotAccepted(PersonalDetailsModel personalDetails) {
        page.getUserRegistrationForm().shouldBeVisible();
        page.getSubmittedForm().shouldBeNotVisible();
        page.getUserRegistrationForm().getTermErrorText().shouldHaveExactText("You must accept the terms.");
        return this;
    }
}
