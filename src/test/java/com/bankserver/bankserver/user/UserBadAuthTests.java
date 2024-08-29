package com.bankserver.bankserver.user;

import com.bankserver.bankserver.TestUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserBadAuthTests {
    @Test
    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }


    @Test
    void testUserCreationFakeAuth() {
        String username = TestUtils.generateUsername();

        // HERE
        TestUtils.createUser(401, TestUtils.generateGabrageKey(), username);
    }

    @Test
    void testUserPatchFakeAuth() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String id = userData.path("id");

        // HERE
        TestUtils.patchUser(401, TestUtils.generateGabrageKey(), id, "{\"accountLimit\":3}");
    }

    @Test
    void testUserGetByIdFakeAuth() {
        String apiKey = TestUtils.initServer(201);
        Response userData = TestUtils.createUser(201, apiKey, TestUtils.generateUsername());
        String id = userData.path("id");

        // HERE
        TestUtils.getUserByIdOrUsername(401, TestUtils.generateGabrageKey(), id);
    }

    @Test
    void testUserGetByUsernameFakeAuth() {
        String apiKey = TestUtils.initServer(201);
        String username = TestUtils.generateUsername();
        TestUtils.createUser(201, apiKey, username);

        // HERE
        TestUtils.getUserByIdOrUsername(401, TestUtils.generateGabrageKey(), username);
    }
}
