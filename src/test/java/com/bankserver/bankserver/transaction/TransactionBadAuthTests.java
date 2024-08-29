package com.bankserver.bankserver.transaction;

import com.bankserver.bankserver.TestUtils;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionBadAuthTests {
    private static final Faker faker = new Faker();

    @Test
    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testTransactionFakeAuth() {
        String apiKey = TestUtils.initServer(201);
        Response user1Data = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        Response user2Data = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());

        String accountId1 = user1Data.path("personalAccountId");
        String accountId2 = user2Data.path("personalAccountId");

        TestUtils.patchAccount(200, apiKey, accountId1, "{\"balance\":500}");

        String description = faker.book().title();
        TestUtils.postTransaction(401, TestUtils.generateGabrageKey(), accountId1, accountId2, 500F, description);
    }
}
