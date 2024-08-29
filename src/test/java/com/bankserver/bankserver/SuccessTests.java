package com.bankserver.bankserver;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IndividualTests {
    private static final Faker faker = new Faker();

    @Test
    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    // Init server
    @Test
    void testServer() {
        String apiKey = initServer();
        assertThat(apiKey, notNullValue());
    }

    @Test
    void testUserCreation() {
        String apiKey = initServer();
        String username = generateUsername();
        Response userData = createUser(apiKey, username);

        assertThat(userData.path("username"), equalTo(username));
    }

    @Test
    void testUserPatch() {
        String apiKey = initServer();
        Response userData = createUser(apiKey, generateUsername());
        String id = userData.path("id");

        Response patchedUserData = patchUser(apiKey, id, "{\"accountLimit\":3}");

        assertThat(patchedUserData.path("accountLimit"), equalTo(3));
    }

    @Test
    void testAccountCreate() {
        String apiKey = initServer();
        Response userData = createUser(apiKey, generateUsername());
        String id = userData.path("id");

        String gotHouse = faker.gameOfThrones().house();
        Response accountData = createAccount(apiKey, id, gotHouse);
        assertThat(accountData.path("balance"), equalTo(0F));
        assertThat(accountData.path("displayName"), equalTo(gotHouse));
    }

    @Test
    void testUserGet() {
        String apiKey = initServer();
        Response userData = createUser(apiKey, generateUsername());
        String id = userData.path("id");

        Response userGetData = getUser(apiKey, id);
        assertThat(userGetData.path("username"), equalTo(userData.path("username")));
        assertThat(userGetData.path("id"), equalTo(userData.path("id")));
        assertThat(userGetData.path("personalAccountId"), equalTo(userData.path("personalAccountId")));
    }

    @Test
    void testAccountGet() {
        String apiKey = initServer();
        Response userData = createUser(apiKey, generateUsername());
        String userId = userData.path("id");

        Response accountData = createAccount(apiKey, userId, faker.aviation().airport());
        String accountId = accountData.path("id");

        Response accountGetData = getAccount(apiKey, accountId);

        assertThat(accountGetData.path("balance"), equalTo(accountData.path("balance")));
        assertThat(accountGetData.path("displayName"), equalTo(accountData.path("displayName")));
        assertThat(accountGetData.path("id"), equalTo(accountData.path("id")));
    }

    @Test
    void testAccountPatch() {
        String apiKey = initServer();
        Response userData = createUser(apiKey, generateUsername());
        String accountId = userData.path("personalAccountId");

        Response accountData = patchAccount(apiKey, accountId, "{\"balance\":5000}");
        assertThat(accountData.path("balance"), equalTo(5000F));

        Response accountGetData = getAccount(apiKey, accountId);
        assertThat(accountGetData.path("balance"), equalTo(5000F));
    }

    @Test
    void testTransaction() {
        String apiKey = initServer();
        Response user1Data = createUser(apiKey, generateUsername());
        Response user2Data = createUser(apiKey, generateUsername());

        String accountId1 = user1Data.path("personalAccountId");
        String accountId2 = user2Data.path("personalAccountId");

        patchAccount(apiKey, accountId1, "{\"balance\":500}");

        String description = faker.book().title();
        Response transactionResponse = postTransaction(apiKey, accountId1, accountId2, 500F, description);

        System.out.println(transactionResponse.asPrettyString());
        assertThat(transactionResponse.path("description"), equalTo(description));
        assertThat(transactionResponse.path("sourceAccount.id"), equalTo(accountId1));
        assertThat(transactionResponse.path("destinationAccount.id"), equalTo(accountId2));
        assertThat(transactionResponse.path("amount"), equalTo(500F));
    }

    // misc functions
    private static String initServer() {
        Response initResponse = given()
                .post("/api/server/" + UUID.randomUUID())
                .then()
                .statusCode(201)
                .extract()
                .response();

        return initResponse.path("apiKey");
    }

    private static Response createUser(String apiKey, String username1) {
        return given()
                .header("X-API-KEY", apiKey)
                .post("/api/user/" + username1)
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    private static Response createAccount(String apiKey, String id, String displayName) {
        String accountCreateBody = "{\"displayName\":\"" + displayName + "\"}";
        return given()
                .header("X-API-KEY", apiKey)
                .contentType("application/json")
                .body(accountCreateBody)
                .post("/api/account/" + id)
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    private static Response patchUser(String apiKey, String userId, String data) {
        return given()
                .header("X-API-KEY", apiKey)
                .contentType("application/json")
                .body(data)
                .patch("/api/user/" + userId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    private static Response patchAccount(String apiKey, String accountId, String data) {
        return given()
                .header("X-API-KEY", apiKey)
                .contentType("application/json")
                .body(data)
                .patch("/api/account/" + accountId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    private static Response getUser(String apiKey, String id) {
        return given()
                .header("X-API-KEY", apiKey)
                .get("/api/user/" + id)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    private static Response getAccount(String apiKey, String id) {
        return given()
                .header("X-API-KEY", apiKey)
                .get("/api/account/" + id)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    private static Response postTransaction(String apiKey, String accountId1, String accountId2, float amount, String description) {
        String transactionPostBody = "{\"sourceAccountId\":\"" + accountId1 + "\",\"destinationAccountId\":\"" + accountId2 + "\",\"amount\":" + amount + ",\"description\":\"" + description + "\"}";
        System.out.println(transactionPostBody);
        return given()
                .header("X-API-KEY", apiKey)
                .contentType("application/json")
                .body(transactionPostBody)
                .post("/api/transaction")
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    private static String generateUsername() {
        return faker.name().firstName() + faker.name().lastName() + faker.address().buildingNumber();
    }
}
