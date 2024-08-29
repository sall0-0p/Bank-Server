package com.bankserver.bankserver.account;

import com.bankserver.bankserver.TestUtils;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountBadAuthTests {
    private static final Faker faker = new Faker();

    @Test
    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testAccountCreateFakeAuth() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String id = userData.path("id");

        // HERE
        String gotHouse = faker.gameOfThrones().house();
        TestUtils.createAccount(401, TestUtils.generateGabrageKey(), id, gotHouse);
    }

    @Test
    void testAccountGetFakeAuth() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String userId = userData.path("id");

        Response accountData = TestUtils.createAccount(201, apiKey, userId, faker.aviation().airport());
        String accountId = accountData.path("id");

        // HERE
        TestUtils.getAccount(401, TestUtils.generateGabrageKey(), accountId);
    }

    @Test
    void testAccountPatchFakeAuth() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String accountId = userData.path("personalAccountId");

        // HERE
        TestUtils.patchAccount(401, TestUtils.generateGabrageKey(), accountId, "{\"balance\":5000}");
    }

    @Test
    void testAccountListFakeAuth() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String userId = userData.path("id");

        // HERE
        TestUtils.getAccounts(401, TestUtils.generateGabrageKey(), userId);
    }
}
