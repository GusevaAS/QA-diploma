package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.APIHelper;
import ru.netology.data.DataGenerator;
import ru.netology.data.SQLHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataBaseTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void removeAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should add the payment data to the database with APPROVAL via the API")
    void successTransactionWithApprovedDebitCardViaAPI() {
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        APIHelper.createPayment(cardInfo);
        var paymentCardData = SQLHelper.getDebitCardData();
        assertEquals("APPROVED", paymentCardData.getStatus());

    }

    @Test
    @DisplayName("Should add card details when buying on credit to the database with APPROVAL via API")
    void shouldTransactionWithTheApprovedCardMustBeSuccessfulWhenBuyingOnCreditThroughTheAPI() {
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        APIHelper.createCredit(cardInfo);
        var creditCardData = SQLHelper.getCardDataWhenBuyingOnCredit();
        assertEquals("APPROVED", creditCardData.getStatus());

    }

    @Test
    @DisplayName("Should add the payment data to the database with a DECLINED via the API")
    void shouldSuccessTransactionWithDeclinedDebitCardViaAPI() {
        var cardInfo = DataGenerator.getDataWithDeclineCard();
        APIHelper.createPayment(cardInfo);
        var paymentCardData = SQLHelper.getDebitCardData();
        assertEquals("DECLINED", paymentCardData.getStatus());

    }

    @Test
    @DisplayName("Should add the credit data to the database with a DECLINED via the API")
    void shouldASuccessfulTransactionWithARejectedCardWhenBuyingOnCreditThroughTheAPI() {
        var cardInfo = DataGenerator.getDataWithDeclineCard();
        APIHelper.createCredit(cardInfo);
        var creditCardData = SQLHelper.getCardDataWhenBuyingOnCredit();
        assertEquals("DECLINED", creditCardData.getStatus());

    }

    @Test
    @DisplayName("Should add the correct created date to the payment table with the APPROVED card")
    void shouldAddCorrectDateInPaymentTableWithApprovedCard() {
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        APIHelper.createPayment(cardInfo);
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        var paymentCardData = SQLHelper.getDebitCardData();
        var dateFromDB = paymentCardData.getCreated();
        var dateDB = dateFromDB.substring(0, dateFromDB.length() - 10);
        assertEquals(formatForDateNow.format(dateNow), dateDB);
    }

    @Test
    @DisplayName("Should add the correct created date to the credit table with the APPROVED card")
    void shouldAddCorrectDateInCreditTableWithApprovedCard() {
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        APIHelper.createCredit(cardInfo);
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        var creditCardData = SQLHelper.getCardDataWhenBuyingOnCredit();
        var dateFromDB = creditCardData.getCreated();
        var dateDB = dateFromDB.substring(0, dateFromDB.length() - 10);
        assertEquals(formatForDateNow.format(dateNow), dateDB);
    }

    @Test
    @DisplayName("Should add the correct created date to the payment table with the DECLINED card")
    void shouldAddCorrectDateInPaymentTableWithDeclinedCard() {
        var cardInfo = DataGenerator.getDataWithDeclineCard();
        APIHelper.createPayment(cardInfo);
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        var paymentCardData = SQLHelper.getDebitCardData();
        var dateFromDB = paymentCardData.getCreated();
        var dateDB = dateFromDB.substring(0, dateFromDB.length() - 10);
        assertEquals(formatForDateNow.format(dateNow), dateDB);
    }

    @Test
    @DisplayName("Should add the correct created date to the credit table with the DECLINED card")
    void shouldAddCorrectDateInCreditTableWithDeclinedCard() {
        var cardInfo = DataGenerator.getDataWithDeclineCard();
        APIHelper.createCredit(cardInfo);
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        var creditCardData = SQLHelper.getCardDataWhenBuyingOnCredit();
        var dateFromDB = creditCardData.getCreated();
        var dateDB = dateFromDB.substring(0, dateFromDB.length() - 10);
        assertEquals(formatForDateNow.format(dateNow), dateDB);
    }

    @Test
    @DisplayName("Should add the correct payment data in order_entity table")
    void shouldAddCorrectPaymentDataInOrderTable() {
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        APIHelper.createPayment(cardInfo);
        var cardDataFromPaymentTable = SQLHelper.getDebitCardData();
        var cardDataFromOrderTable = SQLHelper.getTableOrderEntity();
        assertEquals(cardDataFromPaymentTable.getTransaction_id(), cardDataFromOrderTable.getPayment_id());
    }

    @Test
    @DisplayName("Should add the correct credit data in order_entity table")
    void shouldAddCorrectCreditDataInOrderTable() {
        var cardInfo = DataGenerator.getDataWithApprovedCard();
        APIHelper.createCredit(cardInfo);
        var cardDataFromCreditTable = SQLHelper.getCardDataWhenBuyingOnCredit();
        var cardDataFromOrderTable = SQLHelper.getTableOrderEntity();
        assertEquals(cardDataFromCreditTable.getBank_id(), cardDataFromOrderTable.getCredit_id());
    }
}
