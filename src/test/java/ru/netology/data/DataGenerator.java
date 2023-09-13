package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {

    public static Faker faker = new Faker(new Locale("en"));

    private static int validYear = Integer.parseInt(getCurrentYear()) + 1;
    private static String ApprovedCard = "4444 4444 4444 4441";
    private static String DeclinedCard = "4444 4444 4444 4442";


    public static CardInfo getDataWithApprovedCard() {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        return new CardInfo(ApprovedCard, getCurrentMonth(), String.valueOf(validYear), randomName, randomCvc);
    }

    public static CardInfo getDataWithDeclineCard() {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        return new CardInfo(DeclinedCard, getCurrentMonth(), String.valueOf(validYear), randomName, randomCvc);
    }

    public static CardInfo getDataWithRandomCardNumber() {
        var randomName = faker.name().fullName();
        var randomCardNumber = faker.number().digits(16);
        var randomCvc = faker.number().digits(3);
        return new CardInfo(randomCardNumber, getCurrentMonth(), String.valueOf(validYear), randomName, randomCvc);
    }

    public static CardInfo getDataWithApprovedCardAndMonthAndYear(String month, String year) {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        return new CardInfo(ApprovedCard, month, year, randomName, randomCvc);
    }

    public static CardInfo getDataWithCardOwnerNameOfDifferentLength(int length) {
        var randomName = faker.lorem().fixedString(length);
        var randomCvc = faker.number().digits(3);
        return new CardInfo(ApprovedCard, getCurrentMonth(), String.valueOf(validYear), randomName, randomCvc);
    }

    public static CardInfo getDataWithCardOwnerName(String name) {
        var randomCvc = faker.number().digits(3);
        return new CardInfo(ApprovedCard, getCurrentMonth(), String.valueOf(validYear), name, randomCvc);
    }

    public static CardInfo getDataWithAnExpiredCardInTheMonthField() {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        var currentMonth = Integer.parseInt(getCurrentMonth());
        var currentYear = Integer.parseInt(getCurrentYear());
        if (currentMonth == 1) {
            currentMonth = 12;
            currentYear = currentYear - 1;
        } else currentMonth = currentMonth - 1;

        var minusOneFromCurrentMonth = "";
        if (currentMonth < 10) {
            minusOneFromCurrentMonth = "0" + currentMonth;
        }
        return new CardInfo(ApprovedCard, minusOneFromCurrentMonth,
                String.valueOf(currentYear), randomName, randomCvc);
    }

    public static CardInfo getDataWithMaxDateMinusOneMonth() {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        var currentMonth = Integer.parseInt(getCurrentMonth());
        var preMaxMonth = 0;
        var maxYear = Integer.parseInt(getCurrentYear()) + 5;
        if (currentMonth == 1) {
            preMaxMonth = 12;
            maxYear = maxYear - 1;
        } else preMaxMonth = currentMonth - 1;
        var strPreMaxMonth = "";
        if (preMaxMonth < 10) {
            strPreMaxMonth = "0" + preMaxMonth;
        }
        return new CardInfo(ApprovedCard, strPreMaxMonth,
                String.valueOf(maxYear), randomName, randomCvc);
    }

    public static String getCurrentMonth() {
        LocalDate date = LocalDate.now();
        var currentMonth = date.format(DateTimeFormatter.ofPattern("MM"));
        return currentMonth;
    }

    public static String getCurrentYear() {
        LocalDate date = LocalDate.now();
        var currentYear = date.format(DateTimeFormatter.ofPattern("yy"));
        return currentYear;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardInfo {
        String number;
        String month;
        String year;
        String holder;
        String cvc;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditData {
        private String id;
        private String bank_id;
        private String created;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DebitCardData {
        private String id;
        private String amount;
        private String created;
        private String status;
        private String transaction_id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TableOrderEntity {
        private String id;
        private String created;
        private String credit_id;
        private String payment_id;
    }
}
