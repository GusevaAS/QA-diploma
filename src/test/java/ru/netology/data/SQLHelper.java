package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {

    static QueryRunner runner = new QueryRunner();
    static String url = System.getProperty("db.url");
    static String userName = System.getProperty("db.username");
    static String password = System.getProperty("db.password");

    public SQLHelper() {
    }

    @SneakyThrows
    private static Connection getConn() {
        return DriverManager.getConnection(url, userName, password);
    }

    @SneakyThrows
    public static DataGenerator.CreditData getCardDataWhenBuyingOnCredit() {
        var cardDataSQL = "SELECT * FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            return runner.query(conn, cardDataSQL,
                    new BeanHandler<>(DataGenerator.CreditData.class));
        }
    }

    @SneakyThrows
    public static DataGenerator.DebitCardData getDebitCardData() {
        var cardDataSQL = "SELECT * FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            return runner.query(conn, cardDataSQL,
                    new BeanHandler<>(DataGenerator.DebitCardData.class));
        }
    }

    @SneakyThrows
    public static DataGenerator.TableOrderEntity getTableOrderEntity() {
        var orderEntityDataSQL = "SELECT * FROM order_entity ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            return runner.query(conn, orderEntityDataSQL,
                    new BeanHandler<>(DataGenerator.TableOrderEntity.class));
        }
    }
}
