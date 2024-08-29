package com.bankserver.bankserver.server;

import com.bankserver.bankserver.TestUtils;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class ServerSuccessTests {
    private static final Faker faker = new Faker();

    @Test
    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    // Init server
    @Test
    void testServer() {
        String apiKey = TestUtils.initServer(201);
        assertThat(apiKey, notNullValue());
    }
}
