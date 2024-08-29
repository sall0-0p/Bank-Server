package com.bankserver.bankserver.transaction;

import com.bankserver.bankserver.TestUtils;
import com.bankserver.bankserver.account.Account;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TransactionSuccessTests {
    private static final Faker faker = new Faker();

    @Test
    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testTransaction() {
        String apiKey = TestUtils.initServer(201);
        Response user1Data = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        Response user2Data = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());

        String accountId1 = user1Data.path("personalAccountId");
        String accountId2 = user2Data.path("personalAccountId");

        TestUtils.patchAccount(200, apiKey, accountId1, "{\"balance\":500}");

        String description = faker.book().title();
        Response transactionResponse = TestUtils.postTransaction(201, apiKey, accountId1, accountId2, 500F, description);

        assertThat(transactionResponse.path("description"), equalTo(description));
        assertThat(transactionResponse.path("sourceAccount.id"), equalTo(accountId1));
        assertThat(transactionResponse.path("destinationAccount.id"), equalTo(accountId2));
        assertThat(transactionResponse.path("amount"), equalTo(500F));
    }
}
