package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PaymentWithCreditPage {
    private SelenideElement heading = $$(".button").find(exactText("Купить в кредит"));
    private SelenideElement cardNumberField = $("input[type=\"text\"][placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement monthField = $("input[type=\"text\"][placeholder=\"08\"]");
    private SelenideElement yearField = $("input[type=\"text\"][placeholder=\"22\"]");
    private SelenideElement cardOwnerField = $$(".input").find(exactText("Владелец")).$(".input__control");
    private SelenideElement cvcField = $("input[type=\"text\"][placeholder=\"999\"]");
    private SelenideElement continueButton = $$(".button").find(exactText("Продолжить"));
    private SelenideElement errorMessageWithDecline = $(".notification_status_error");
    private SelenideElement approvedMessage = $(".notification_status_ok");
    private SelenideElement warningCardNumberField = $x("//*[text()='Номер карты']/..//*[@class='input__sub']");
    private SelenideElement warningMonthField = $x("//*[text()='Месяц']/..//*[@class='input__sub']");
    private SelenideElement warningYearField = $x("//*[text()='Год']/..//*[@class='input__sub']");
    private SelenideElement warningCardOwnerField = $x("//*[text()='Владелец']/..//*[@class='input__sub']");
    private SelenideElement warningCvcField = $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']");

    public PaymentWithCreditPage() {
        heading.shouldBe(visible);
    }

    public void enterValidCardDetailsForBankToPurchaseOnCredit(DataGenerator.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        cardOwnerField.setValue(cardInfo.getHolder());
        cvcField.setValue(cardInfo.getCvc());
        continueButton.click();
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void checkErrorMessageDeclineFromBank() {
        errorMessageWithDecline.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void checkApprovedMessageFromBank() {
        approvedMessage.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void checkWarningUnderCardNumberField(String warningText) {
        warningCardNumberField.shouldBe(visible);
        warningCardNumberField.shouldHave(text(warningText));
    }

    public void checkWarningUnderMonthField(String warningText) {
        warningMonthField.shouldBe(visible);
        warningMonthField.shouldHave(text(warningText));
    }

    public void checkWarningUnderYearField(String warningText) {
        warningYearField.shouldBe(visible);
        warningYearField.shouldHave(text(warningText));
    }

    public void checkWarningUnderCardOwnerField(String warningText) {
        warningCardOwnerField.shouldBe(visible);
        warningCardOwnerField.shouldHave(text(warningText));
    }

    public void checkWarningUnderCvcField(String warningText) {
        warningCvcField.shouldBe(visible);
        warningCvcField.shouldHave(text(warningText));
    }

    public void notCheckWarningUnderAllFields() {
        warningCardNumberField.shouldNotBe(visible);
        warningMonthField.shouldNotBe(visible);
        warningYearField.shouldNotBe(visible);
        warningCardOwnerField.shouldNotBe(visible);
        warningCvcField.shouldNotBe(visible);
    }
}
