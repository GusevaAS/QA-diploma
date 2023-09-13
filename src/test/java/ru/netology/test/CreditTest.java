package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.page.DashboardPage;


import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;

public class CreditTest {

    DashboardPage page = open(System.getProperty("sut.url"), DashboardPage.class);

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void openPage() {
        Configuration.holdBrowserOpen = true;
    }

    @AfterAll
    static void removeAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    void closePage() {
        closeWindow();
    }

    @Test
    @DisplayName("Card must be approved")
    void shouldShowASuccessfulCardTransaction() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Card must be declined")
    void shouldShowAnSuccessfulCardTransaction() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithDeclineCard();
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkErrorMessageDeclineFromBank();
    }

    @Test
    @DisplayName("Card with a random number must be rejected")
    void shouldDeclineWithRandomCard() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithRandomCardNumber();
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkErrorMessageDeclineFromBank();
    }

    @Test
    @DisplayName("Card with a maximum date must be approved by the bank to purchase a tour on credit")
    void shouldSuccessTransactionWithMaxAllowedDate() {
        var creditPage = page.openCreditPage();
        var currentMonth = DataGenerator.getCurrentMonth();
        var maxYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 5;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear(currentMonth, String.valueOf(maxYear));
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Card with a current date must be approved by the bank to purchase a tour on credit")
    void shouldASuccessfulTransactionWithTheCurrentDate() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear(DataGenerator.getCurrentMonth(), DataGenerator.getCurrentYear());
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Card with a date minus one month from the maximum must be approved by the bank to purchase a tour on credit")
    void transactionMustBeSuccessfulWithTheDateMinusOneMonthFromTheMaximum() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithMaxDateMinusOneMonth();
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Card with a validity period following the current month must be approved")
    void shouldSuccessTransactionCardWithAnExpirationDateFollowingTheCurrentOne() {
        var creditPage = page.openCreditPage();
        var nextMonth = Integer.parseInt(DataGenerator.getCurrentMonth()) + 1;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear(String.valueOf(nextMonth), DataGenerator.getCurrentYear());
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Red warning is displayed when an expired card is entered in the year field")
    void shouldShowAnExpiredCardWarningInTheYearField() {
        var creditPage = page.openCreditPage();
        var currentMonth = DataGenerator.getCurrentMonth();
        var lastYear = Integer.parseInt(DataGenerator.getCurrentYear()) - 1;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear(currentMonth, String.valueOf(lastYear));
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderYearField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Red warning is displayed when an expired card is entered in the month field")
    void shouldShowWarningWithExpiredCardForMonth() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithAnExpiredCardInTheMonthField();
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Red warning should be displayed if the month field is set to 00")
    void shouldShowWarningWhenMonthIs00() {
        var creditPage = page.openCreditPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear("00", String.valueOf(validYear));
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Should be a red warning if you put an invalid value 13 in the month field")
    void shouldShowWarningAnInvalidMonthData() {
        var creditPage = page.openCreditPage();
        var currentYear = DataGenerator.getCurrentYear();
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear("13", currentYear);
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Cards must be approved with one digit in the month field")
    void transactionMustBeSuccessfulIndicatingTheMonthInOneDigit() {
        var creditPage = page.openCreditPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear("5", String.valueOf(validYear));
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Use a card with the maximum length of the owner name")
    void shouldSuccessTransactionMaxLengthCardOwnerName() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerNameOfDifferentLength(50);
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Use a card with the minimum length of the owner name")
    void shouldSuccessTransactionMinLengthCardOwnerName() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerNameOfDifferentLength(1);
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Red warning is displayed when you enter a special characters in the Card Owner field")
    void shouldShowWarningWithSpecCharsCardOwnerName() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerName("&|#{}№@!");
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    @DisplayName("Red warning is displayed when you enter a Cyrillic value in the Card Owner field")
    void shouldShowWarningWithCyrillicCardOwnerName() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerName("Александр Пушкин");
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    @DisplayName("Red warning is displayed when you enter a numbers in the card owner field")
    void shouldShowWarningWithNumbersInCardOwnerName() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerName("Pushkin 12345");
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    @DisplayName("Field: card number with red warning if not filled")
    void shouldShowWarningWithEmptyCardNumberField() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setNumber("");
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderCardNumberField("Неверный формат");
    }

    @Test
    @DisplayName("Field: month with red warning if not filled")
    void shouldShowWarningWithEmptyMonthField() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setMonth("");
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверный формат");
    }

    @Test
    @DisplayName("Field: year with red warning if not filled")
    void shouldShowWarningWithEmptyYearField() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setYear("");
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderYearField("Неверный формат");
    }

    @Test
    @DisplayName("Field: card owner red warning if not filled")
    void shouldShowWarningWithEmptyCardOwnerField() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setHolder("");
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderCardOwnerField("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Field: CVC/CVV red warning if not filled")
    void shouldShowWarningWithEmptyCVCCVVField() {
        var creditPage = page.openCreditPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setCvc("");
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    @DisplayName("Red warning if all form fields are empty")
    void shouldShowWarningWithEmptyAllForm() {
        var creditPage = page.openCreditPage();
        creditPage.clickContinueButton();
        creditPage.checkWarningUnderCardNumberField("Неверный формат");
        creditPage.checkWarningUnderMonthField("Неверный формат");
        creditPage.checkWarningUnderYearField("Неверный формат");
        creditPage.checkWarningUnderCardOwnerField("Поле обязательно для заполнения");
        creditPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    @DisplayName("Red warning should not be displayed after filling the fields with valid values")
    void shouldNotShowWarningAfterFillingAllField() {
        var creditPage = page.openCreditPage();
        creditPage.clickContinueButton();
        creditPage.checkWarningUnderCardNumberField("Неверный формат");
        creditPage.checkWarningUnderMonthField("Неверный формат");
        creditPage.checkWarningUnderYearField("Неверный формат");
        creditPage.checkWarningUnderCardOwnerField("Поле обязательно для заполнения");
        creditPage.checkWarningUnderCvcField("Неверный формат");

        var cardInfo = DataGenerator.getDataWithApprovedCard();
        creditPage.enterValidCardDetailsForBankToPurchaseOnCredit(cardInfo);
        creditPage.checkApprovedMessageFromBank();
        creditPage.notCheckWarningUnderAllFields();
    }
}
