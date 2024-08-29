package com.bankserver.bankserver.user;

import com.bankserver.bankserver.TestUtils;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserSuccessfulTests {
    private static final Faker faker = new Faker();

    @Test
    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testUserCreation() {
        String apiKey = TestUtils.initServer(201);
        String username = TestUtils.generateUsername();
        Response userData = TestUtils.createUser(201, apiKey, username);

        assertThat(userData.path("username"), equalTo(username));
    }

    @Test
    void testUserPatch() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String id = userData.path("id");

        Response patchedUserData = TestUtils.patchUser(200, apiKey, id, "{\"accountLimit\":3}");

        assertThat(patchedUserData.path("accountLimit"), equalTo(3));
    }

    @Test
    void testUserGetById() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String id = userData.path("id");

        Response userGetData = TestUtils.getUserByIdOrUsername(200, apiKey, id);
        assertThat(userGetData.path("username"), equalTo(userData.path("username")));
        assertThat(userGetData.path("id"), equalTo(userData.path("id")));
        assertThat(userGetData.path("personalAccountId"), equalTo(userData.path("personalAccountId")));
    }

    @Test
    void testUserGetByUsername() {
        String apiKey = TestUtils.initServer(201);
        String username = TestUtils.generateUsername();
        Response userData = TestUtils.createUser(201, apiKey, username);

        Response userGetData = TestUtils.getUserByIdOrUsername(200, apiKey, username);
        assertThat(userGetData.path("username"), equalTo(userData.path("username")));
        assertThat(userGetData.path("id"), equalTo(userData.path("id")));
        assertThat(userGetData.path("personalAccountId"), equalTo(userData.path("personalAccountId")));
    }
}
