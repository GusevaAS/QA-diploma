package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.page.DashboardPage;


import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;

public class PaymentTest {

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
    @DisplayName("Debit card must be approved")
    void shouldShowASuccessfulDebitCardTransaction() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Debit card must be declined")
    void shouldShowAnSuccessfulDebitCardTransaction() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithDeclineCard();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkErrorMessageDeclineFromBank();
    }

    @Test
    @DisplayName("Debit card with a random number must be rejected")
    void shouldDeclineWithRandomDebitCard() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithRandomCardNumber();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkErrorMessageDeclineFromBank();
    }

    @Test
    @DisplayName("Debit card with a maximum date must be approved by the bank to purchase a tour")
    void shouldSuccessTransactionWithMaxAllowedDate() {
        var paymentPage = page.openPaymentPage();
        var currentMonth = DataGenerator.getCurrentMonth();
        var maxYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 5;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear(currentMonth,
                String.valueOf(maxYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Debit card with a current date must be approved by the bank to purchase a tour")
    void shouldASuccessfulTransactionWithTheCurrentDate() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear
                (DataGenerator.getCurrentMonth(), DataGenerator.getCurrentYear());
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Debit card with a date minus one month from the maximum must be approved by the bank to purchase a tour")
    void transactionMustBeSuccessfulWithTheDateMinusOneMonthFromTheMaximum() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithMaxDateMinusOneMonth();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Debit card with a validity period following the current month must be approved")
    void shouldSuccessTransactionDebitCardWithAnExpirationDateFollowingTheCurrentOne() {
        var paymentPage = page.openPaymentPage();
        var nextMonth = Integer.parseInt(DataGenerator.getCurrentMonth()) + 1;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear
                (String.valueOf(nextMonth), DataGenerator.getCurrentYear());
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Red warning is displayed when an expired debit card is entered in the year field")
    void shouldShowAnExpiredDebitCardWarningInTheYearField() {
        var paymentPage = page.openPaymentPage();
        var currentMonth = DataGenerator.getCurrentMonth();
        var lastYear = Integer.parseInt(DataGenerator.getCurrentYear()) - 1;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear(currentMonth,
                String.valueOf(lastYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderYearField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Red warning is displayed when an expired debit card is entered in the month field")
    void shouldShowWarningWithExpiredDebitCardForMonth() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithAnExpiredCardInTheMonthField();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Red warning should be displayed if the month field is set to 00")
    void shouldShowWarningWhenMonthIs00() {
        var paymentPage = page.openPaymentPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear
                ("00", String.valueOf(validYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Should be a red warning if you put an invalid value 13 in the month field")
    void shouldShowWarningAnInvalidMonthData() {
        var paymentPage = page.openPaymentPage();
        var currentYear = DataGenerator.getCurrentYear();
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear("13",
                currentYear);
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Debit card must be approved with one digit in the month field")
    void transactionMustBeSuccessfulIndicatingTheMonthInOneDigit() {
        var paymentPage = page.openPaymentPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.getDataWithApprovedCardAndMonthAndYear
                ("5", String.valueOf(validYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Use a debit card with the maximum length of the owner name")
    void shouldSuccessTransactionMaxLengthDebitCardOwnerName() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerNameOfDifferentLength(50);
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Use a debit card with the minimum length of the owner name")
    void shouldSuccessTransactionMinLengthDebitCardOwnerName() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerNameOfDifferentLength(1);
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMessageFromBank();
    }

    @Test
    @DisplayName("Red warning is displayed when you enter a special characters in the Card Owner field")
    void shouldShowWarningWithSpecCharsCardOwnerName() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerName("&|#{}№@!");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    @DisplayName("Red warning is displayed when you enter a Cyrillic value in the Card Owner field")
    void shouldShowWarningWithCyrillicCardOwnerName() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerName("Александр Пушкин");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    @DisplayName("Red warning is displayed when you enter a numbers in the card owner field")
    void shouldShowWarningWithNumbersInCardOwnerName() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithCardOwnerName("Pushkin 12345");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    @DisplayName("Field: debit card number with red warning if not filled")
    void shouldShowWarningWithEmptyCardNumberField() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setNumber("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardNumberField("Неверный формат");
    }

    @Test
    @DisplayName("Field: month with red warning if not filled")
    void shouldShowWarningWithEmptyMonthField() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setMonth("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверный формат");
    }

    @Test
    @DisplayName("Field: year with red warning if not filled")
    void shouldShowWarningWithEmptyYearField() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setYear("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderYearField("Неверный формат");
    }

    @Test
    @DisplayName("Field: card owner red warning if not filled")
    void shouldShowWarningWithEmptyCardOwnerField() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setHolder("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardOwnerField("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Field: CVC/CVV red warning if not filled")
    void shouldShowWarningWithEmptyCVCCVVField() {
        var paymentPage = page.openPaymentPage();
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        cardInfo.setCvc("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    @DisplayName("Red warning if all form fields are empty")
    void shouldShowWarningWithEmptyAllForm() {
        var paymentPage = page.openPaymentPage();
        paymentPage.clickContinueButton();
        paymentPage.checkWarningUnderCardNumberField("Неверный формат");
        paymentPage.checkWarningUnderMonthField("Неверный формат");
        paymentPage.checkWarningUnderYearField("Неверный формат");
        paymentPage.checkWarningUnderCardOwnerField("Поле обязательно для заполнения");
        paymentPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    @DisplayName("Red warning should not be displayed after filling the fields with valid values")
    void shouldNotShowWarningAfterFillingAllField() {
        var paymentPage = page.openPaymentPage();
        paymentPage.clickContinueButton();
        paymentPage.checkWarningUnderCardNumberField("Неверный формат");
        paymentPage.checkWarningUnderMonthField("Неверный формат");
        paymentPage.checkWarningUnderYearField("Неверный формат");
        paymentPage.checkWarningUnderCardOwnerField("Поле обязательно для заполнения");
        paymentPage.checkWarningUnderCvcField("Неверный формат");

        var cardInfo = DataGenerator.getDataWithApprovedCard();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMessageFromBank();
        paymentPage.notCheckWarningUnderAllFields();
    }
}
