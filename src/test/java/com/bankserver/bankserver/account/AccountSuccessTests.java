package com.bankserver.bankserver.account;

import com.bankserver.bankserver.TestUtils;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
public class AccountSuccessTests {
    private static final Faker faker = new Faker();

    @Test
    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testAccountCreate() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String id = userData.path("id");

        String gotHouse = faker.gameOfThrones().house();
        Response accountData = TestUtils.createAccount(201, apiKey, id, gotHouse);
        assertThat(accountData.path("balance"), equalTo(0F));
        assertThat(accountData.path("displayName"), equalTo(gotHouse));
    }

    @Test
    void testAccountGet() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String userId = userData.path("id");

        Response accountData = TestUtils.createAccount(201, apiKey, userId, faker.aviation().airport());
        String accountId = accountData.path("id");

        Response accountGetData = TestUtils.getAccount(200, apiKey, accountId);

        assertThat(accountGetData.path("balance"), equalTo(accountData.path("balance")));
        assertThat(accountGetData.path("displayName"), equalTo(accountData.path("displayName")));
        assertThat(accountGetData.path("id"), equalTo(accountData.path("id")));
    }

    @Test
    void testAccountPatch() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String accountId = userData.path("personalAccountId");

        Response accountData = TestUtils.patchAccount(200, apiKey, accountId, "{\"balance\":5000}");
        assertThat(accountData.path("balance"), equalTo(5000F));

        Response accountGetData = TestUtils.getAccount(200, apiKey, accountId);
        assertThat(accountGetData.path("balance"), equalTo(5000F));
    }

    @Test
    void testAccountList() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String userId = userData.path("id");
        String accountId1 = userData.path("personalAccountId");

        Response accountData = TestUtils.createAccount(201, apiKey, userId, faker.aviation().airport());
        String accountId2 = accountData.path("id");

        Response listResponse = TestUtils.getAccounts(200, apiKey, userId);
        ResponseBody<?> listResponseBody = listResponse.getBody();

        assertThat(listResponseBody.asString().toLowerCase().contains(accountId1), equalTo(true));
        assertThat(listResponseBody.asString().toLowerCase().contains(accountId2), equalTo(true));
    }
}
