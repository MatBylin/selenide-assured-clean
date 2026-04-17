package org.matbylin.gui.pages.qaplayground.practiceform.components;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.matbylin.core.models.personalinfo.AccountDetailsModel;
import org.matbylin.core.models.personalinfo.AddressDetailsModel;
import org.matbylin.core.models.personalinfo.PersonalDetailsModel;
import org.matbylin.gui.core.components.base.BaseComponent;
import org.matbylin.gui.core.elements.*;
import org.matbylin.gui.pages.qaplayground.practiceform.PracticeFormPage;

import static com.codeborne.selenide.Condition.disappear;

@Getter
public class UserRegistrationForm extends BaseComponent<UserRegistrationForm> {

    private final Input firstNameInput = new Input($("#firstName"));
    private final Input lastNameInput = new Input($("#lastName"));
    private final Input emailInput = new Input($("#email"));
    private final Input phoneInput = new Input($("#phone"));
    private final Input dateOfBirthInput = new Input($("#dob"));
    private final RadioButton genderRadioButton = new RadioButton($("#genderGroup"));
    private final Select countrySelect = new Select($("#country"));
    private final Input cityInput = new Input($("#city"));
    private final Input passwordInput = new Input($("#password"));
    private final Input confirmPasswordInput = new Input($("#confirmPassword"));
    private final Checkbox agreeTermsCheckbox = new Checkbox($("#terms"));
    private final Text termErrorText = new Text($("#termsError"));
    private final Button submitFormButton = new Button($("#submitFormBtn"));

    public UserRegistrationForm(SelenideElement root) {
        super(root);
    }

    public UserRegistrationForm fillPersonalDetails(PersonalDetailsModel personalDetailsModel) {
        firstNameInput.setValue(personalDetailsModel.getFirstName());
        lastNameInput.setValue(personalDetailsModel.getLastName());
        emailInput.setValue(personalDetailsModel.getEmail());
        phoneInput.setValue(personalDetailsModel.getPhone());
        dateOfBirthInput.setValue(personalDetailsModel.getDateOfBirth());
        genderRadioButton.select(personalDetailsModel.getGender().getValue());
        return this;
    }

    public UserRegistrationForm fillAddressDetails(AddressDetailsModel addressModel) {
        countrySelect.selectByText(addressModel.getCountry());
        cityInput.setValue(addressModel.getCity());
        return this;
    }

    public UserRegistrationForm fillAccountDetails(AccountDetailsModel accountDetailsModel) {
        passwordInput.setSensitiveValue(accountDetailsModel.getPassword());
        confirmPasswordInput.setSensitiveValue(accountDetailsModel.getPassword());
        if (accountDetailsModel.isAgreeTerms()) {
            agreeTermsCheckbox.check();
        } else {
            agreeTermsCheckbox.uncheck();
        }
        return this;
    }

    public PracticeFormPage submitForm() {
        submitFormButton.click();
        return new PracticeFormPage();
    }

    @Override
    public UserRegistrationForm shouldBeVisible() {
        super.shouldBeVisible();
        firstNameInput.shouldBeVisible();
        lastNameInput.shouldBeVisible();
        return this;
    }

    public UserRegistrationForm shouldNotBeVisible() {
        firstNameInput.shouldBe(disappear);
        lastNameInput.shouldBe(disappear);
        return this;
    }
}
