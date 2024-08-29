package com.bankserver.bankserver;

import io.restassured.response.Response;
import com.github.javafaker.Faker;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class TestUtils {
    private static final Faker faker = new Faker();

    // misc functions
    public static String initServer(int codeToExpect) {
        Response initResponse = given()
                .post("/api/server/" + UUID.randomUUID())
                .then()
                .statusCode(codeToExpect) // 201
                .extract()
                .response();

        return initResponse.path("apiKey");
    }

    public static Response createUser(int codeToExpect, String apiKey, String username1) {
        return given()
                .header("X-API-KEY", apiKey)
                .post("/api/user/" + username1)
                .then()
                .statusCode(codeToExpect) // 201
                .extract()
                .response();
    }

    public static Response createAccount(int codeToExpect, String apiKey, String id, String displayName) {
        String accountCreateBody = "{\"displayName\":\"" + displayName + "\"}";
        return given()
                .header("X-API-KEY", apiKey)
                .contentType("application/json")
                .body(accountCreateBody)
                .post("/api/account/" + id)
                .then()
                .statusCode(codeToExpect) // 201
                .extract()
                .response();
    }

    public static Response patchUser(int codeToExpect, String apiKey, String userId, String data) {
        return given()
                .header("X-API-KEY", apiKey)
                .contentType("application/json")
                .body(data)
                .patch("/api/user/" + userId)
                .then()
                .statusCode(codeToExpect) // 200
                .extract()
                .response();
    }

    public static Response patchAccount(int codeToExpect, String apiKey, String accountId, String data) {
        return given()
                .header("X-API-KEY", apiKey)
                .contentType("application/json")
                .body(data)
                .patch("/api/account/" + accountId)
                .then()
                .statusCode(codeToExpect) // 200
                .extract()
                .response();
    }

    public static Response getUserByIdOrUsername(int codeToExpect, String apiKey, String id) {
        return given()
                .header("X-API-KEY", apiKey)
                .get("/api/user/" + id)
                .then()
                .statusCode(codeToExpect) // 200
                .extract()
                .response();
    }

    public static Response getAccount(int codeToExpect, String apiKey, String id) {
        return given()
                .header("X-API-KEY", apiKey)
                .get("/api/account/" + id)
                .then()
                .statusCode(codeToExpect) // 200
                .extract()
                .response();
    }

    public static Response getAccounts(int codeToExpect, String apiKey, String userId) {
        return given()
                .header("X-API-KEY", apiKey)
                .get("/api/user/" + userId + "/accounts")
                .then()
                .statusCode(codeToExpect) // 200
                .extract()
                .response();
    }

    public static Response postTransaction(int codeToExpect, String apiKey, String accountId1, String accountId2, float amount, String description) {
        String transactionPostBody = "{\"sourceAccountId\":\"" + accountId1 + "\",\"destinationAccountId\":\"" + accountId2 + "\",\"amount\":" + amount + ",\"description\":\"" + description + "\"}";
        return given()
                .header("X-API-KEY", apiKey)
                .contentType("application/json")
                .body(transactionPostBody)
                .post("/api/transaction")
                .then()
                .statusCode(codeToExpect) // 201
                .extract()
                .response();
    }

    // generators

    public static String generateUsername() {
        return faker.name().firstName() + faker.name().lastName() + faker.address().buildingNumber();
    }

    public static String generateGabrageKey() {
        return faker.pokemon().name();
    }
}
